import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private ConnectionSession session;

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheck_WithValidSessionAndReq() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table1;");
        
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        
        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheck_WithInvalidSession() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table1;");
        
        when(session.getDialectType()).thenReturn(null);
        
        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        
        assertNotNull(checkResults);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = Arrays.asList(new Rule(), new Rule());
        
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, mock(JdbcTemplate.class));
        
        assertNotNull(sqlCheckRules);
        assertEquals(0, sqlCheckRules.size());
    }

}