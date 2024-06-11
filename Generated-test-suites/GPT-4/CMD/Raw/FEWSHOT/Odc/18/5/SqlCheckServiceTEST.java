package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    
    @Before
    public void setupMock() {
        sqlCheckService = new SqlCheckService();
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }
    
    @Test
    public void testCheckMethod1() {
        ConnectionSession session = new ConnectionSession();
        session.setDialectType(DialectType.MYSQL);
        SqlCheckReq req = mock(SqlCheckReq.class);
        SqlCheckReq.Delimiter delimiter = SqlCheckReq.Delimiter.SEMICOLON;
        req.setDelimiter(delimiter);
        
        List<Rule> rules = Arrays.asList(new Rule());
        
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertNotNull(results);
    }
    
    @Test
    public void testCheckMethod2() {
        Long environmentId = 1L;
        String databaseName = "testDb";
        List<OffsetString> sqls = Arrays.asList(new OffsetString());
        ConnectionConfig config = mock(ConnectionConfig.class);
        Environment env = mock(Environment.class);
        when(environmentService.detail(environmentId)).thenReturn(env);
        
        List<Rule> rules = Arrays.asList(new Rule());
        when(ruleService.list(any(Long.class), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertTrue(checkViolations.isEmpty());
    }
    
    @Test
    public void testGetRules1() {
        ConnectionSession session = new ConnectionSession();
        session.setDialectType(DialectType.MYSQL);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Arrays.asList(new Rule()), session);
        assertNotNull(sqlCheckRules);
    }
    
    @Test
    public void testGetRules2() {
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Arrays.asList(new Rule()), dialectType, jdbc);
        assertNotNull(sqlCheckRules);
    }
    
    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = Arrays.asList(new CheckViolation());
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(Arrays.asList(new Rule()), violations);
        assertTrue(violatedRules.isEmpty());
    }
    
}