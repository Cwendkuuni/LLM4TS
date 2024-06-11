import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {
    
    @Mock
    private RuleService ruleService;
    
    @Mock
    private EnvironmentService environmentService;
    
    @Mock
    private ConnectionSession session;
    
    @Mock
    private JdbcOperations jdbc;
    
    @Mock
    private Environment env;
    
    @Mock
    private ConnectionConfig config;
    
    private SqlCheckService sqlCheckService;
    
    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }
    
    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbc);
        
        Rule rule1 = new Rule();
        rule1.setEnabled(true);
        rule1.setMetadata(new RuleMetadata());
        rule1.getMetadata().setType(RuleType.SQL_CHECK);
        
        List<Rule> rules = Collections.singletonList(rule1);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        List<CheckResult> result = sqlCheckService.check(session, req);
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testCheckWithEnvironmentId() {
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM table", 0, 0));
        
        Rule rule1 = new Rule();
        rule1.setEnabled(true);
        rule1.setMetadata(new RuleMetadata());
        rule1.getMetadata().setType(RuleType.SQL_CHECK);
        
        List<Rule> rules = Collections.singletonList(rule1);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        
        when(environmentService.detail(anyLong())).thenReturn(env);
        
        OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
        when(factory.getDataSource()).thenReturn(new SingleConnectionDataSource(config, true, false));
        
        List<CheckViolation> violations = sqlCheckService.check(1L, "database", sqls, config);
        
        assertNotNull(violations);
        assertEquals(1, violations.size());
    }
}