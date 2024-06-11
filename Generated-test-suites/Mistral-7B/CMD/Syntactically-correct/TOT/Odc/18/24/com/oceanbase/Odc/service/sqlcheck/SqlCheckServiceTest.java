package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.AutowiredAnnotatedParameterNameValueAutoConfigurer;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.runner.AnnotationBeanContextTestRunner;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.ModelTest;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.service.DefaultSqlChecker;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.service.SqlChecker;
import com.oceanbase.odc.service.sqlcheck.util.SqlCheckUtil;
import com.oceanbase.odc.service.util.SqlCheckUtilTest;

@ContextConfiguration(classes = { SqlCheckServiceTest.ContextConfig.class })
@RunWith(AnnotationBeanContextTestRunner.class)
public class SqlCheckServiceTest {

    private static final String TEST_DBCP_URL = "jdbc:mock:myds";
    private static final String TEST_DBCP_USER = "testUser";
    private static final String TEST_DBCP_PASSWORD = "testPassword";

    @Value("${jdbc.dialect}")
    private DialectType dialectType;

    private ConnectionSession session;
    private Environment env;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private SqlCheckService sqlCheckService;
    private SqlChecker sqlChecker;
    private JdbcOperations jdbc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        new AutowiredAnnotationBeanPostProcessor().postProcessPropertyValues(this, getClass().getDeclaredFields());
        new AutowiredAnnotatedParameterNameValueAutoConfigurer().postProcessProperties(this, getClass().getDeclaredFields());
        Mockito.doReturn(new BasicDataSource()).when(this).getDataSource();
        Mockito.doAnswer(invocation -> {
            SqlCheckContext ctx = invocation.getArgument(0);
            ctx.setSession(session);
            return null;
        }).when(sqlChecker).setupCheckContext(ArgumentMatchers.argThat(context -> context != null));

        session = Mockito.mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(dialectType);

        env = Mockito.mock(Environment.class);
        Mockito.doReturn("envName").when(env).getName();
        Mockito.doReturn(1L).when(env).getRulesetId();

        ruleService = Mockito.mock(RuleService.class);
        when(ruleService.list(ArgumentMatchers.eq(1L), ArgumentMatchers.any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.singletonList(ModelTest.createRule()));

        environmentService = Mockito.mock(EnvironmentService.class);
        when(environmentService.detail(ArgumentMatchers.eq(1L))).thenReturn(env);

        SqlCheckRules sqlCheckRules = Mockito.mock(SqlCheckRules.class);
        when(sqlCheckRules.getAllFactories(ArgumentMatchers.eq(dialectType), ArgumentMatchers.any(JdbcOperations.class)))
                .thenReturn(Collections.singletonList(Mockito.mock(SqlCheckRuleFactory.class)));

        sqlCheckService = new SqlCheckService();
        sqlChecker = new DefaultSqlChecker(dialectType, null, Collections.emptyList());
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
        sqlCheckService.setSqlCheckRules(sqlCheckRules);

        jdbc = Mockito.mock(JdbcOperations.class);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbc);
    }

    @Test
    public void testCheckWithValidScriptContent() {
        String scriptContent = "SELECT 1 FROM test";
        List<CheckResult> checkResults = sqlCheckService.check(session, ModelTest.createSqlCheckReq(scriptContent));
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithInvalidScriptContent() {
        String scriptContent = "SELECT 1 FROM test WHERE test = test --";
        List<CheckResult> checkResults = sqlCheckService.check(session, ModelTest.createSqlCheckReq(scriptContent));
        assertFalse(checkResults.isEmpty());
        CheckResult result = checkResults.get(0);
        assertEquals(RuleType.SQL_CHECK, result.getRuleType());
        assertEquals(ModelTest.createRule(), result.getRule());
        assertEquals(RuleViolation.fromCheckViolation(ModelTest.createCheckViolation()), result.getViolation());
        assertEquals(1, result.getLineNumber());
        assertEquals("test = test --", result.getViolationMessage());
    }

    @Test
    public void testCheckWithEmptyScriptContent() {
        String scriptContent = "";
        List<CheckResult> checkResults = sqlCheckService.check(session, ModelTest.createSqlCheckReq(scriptContent));
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithNullScriptContent() {
        String scriptContent = null;
        List<CheckResult> checkResults = sqlCheckService.check(session, ModelTest.createSqlCheckReq(scriptContent));
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithScriptContentContainingOnlyWhitespace() {
        String scriptContent = "    ";
        List<CheckResult> checkResults = sqlCheckService.check(session, ModelTest.createSqlCheckReq(scriptContent));
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithEmptySqls() {
        List<OffsetString> sqls = Collections.emptyList();
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testDB", sqls,
                ModelTest.createConnectionConfig());
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testCheckWithValidSqls() {
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT 1 FROM test"));
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testDB", sqls,
                ModelTest.createConnectionConfig());
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testCheckWithInvalidSqls() {
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT 1 FROM test WHERE test = test --"));
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testDB", sqls,
                ModelTest.createConnectionConfig());
        assertFalse(checkViolations.isEmpty());
        CheckViolation violation = checkViolations.get(0);
        assertEquals(1, violation.getLineNumber());
        assertEquals("test = test --", violation.getSql());
        assertNull(violation.getColumn());
        assertEquals(RuleType.SQL_CHECK, violation.getRuleType());
        assertEquals(ModelTest.createRule(), violation.getRule());
    }

    @Configuration
    @Import(SqlCheckContext.class)
    static class ContextConfig {
        @Bean
        public ConnectionSession connectionSession() {
            return Mockito.mock(ConnectionSession.class);
        }

        @Bean
        public ConnectionConfig connectionConfig() {
            return ModelTest.createConnectionConfig();
        }

        @Bean
        public Rule rule() {
            return ModelTest.createRule();
        }

        @Bean
        public JdbcTemplate jdbcTemplate() {
            return Mockito.mock(JdbcTemplate.class);
        }

        @Bean
        public SingleConnectionDataSource dataSource() {
            return Mockito.mock(SingleConnectionDataSource.class);
        }

        @Bean
        public SqlCheckContext sqlCheckContext() {
            return new SqlCheckContext(1);
        }

        @Bean
        public SqlChecker sqlChecker() {
            return sqlChecker;
        }

        @Bean
        public SqlCheckService sqlCheckService() {
            return sqlCheckService;
        }

        @Bean
        public RuleService ruleService() {
            return ruleService;
        }

        @Bean
        public EnvironmentService environmentService() {
            return environmentService;
        }

        @Bean
        public SqlCheckRules sqlCheckRules() {
            return sqlCheckRules;
        }
    }

}