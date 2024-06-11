import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private JdbcOperations jdbcOperations;

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithConnectionSession() {
        // Create mock ConnectionSession and SqlCheckReq
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM USERS;");
        
        // Mock ruleService and environmentService
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor("console_ds_key")).thenReturn(jdbcOperations);

        // Mock rule data and SqlCheckRule
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setEnabled(true);
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        // Call the method and assert the result
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertNotNull(checkResults);
        assertEquals(1, checkResults.size());
    }

    @Test
    public void testCheckWithConnectionConfig() {
        // Create mock data for environmentId, databaseName, sqls, and ConnectionConfig
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM USERS;", 0));
        ConnectionConfig config = new ConnectionConfig();

        // Mock environmentService and ruleService
        when(environmentService.detail(environmentId)).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());
        when(jdbcOperations.queryForList(anyString())).thenReturn(new ArrayList<>());

        // Call the method and assert the result
        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertNotNull(checkViolations);
        assertEquals(1, checkViolations.size());
    }
}