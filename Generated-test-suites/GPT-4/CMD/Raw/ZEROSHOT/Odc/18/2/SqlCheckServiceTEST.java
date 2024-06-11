
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.sql.split.OffsetString;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
public class SqlCheckServiceTest {

    @MockBean
    private SqlCheckService sqlCheckService;

    @MockBean
    private ConnectionSession session;

    @MockBean
    private SqlCheckReq req;

    @Test
    public void testCheck(){
        when(sqlCheckService.check(any(ConnectionSession.class), any(SqlCheckReq.class)))
            .thenReturn(new ArrayList<CheckResult>());
        List<CheckResult> result = sqlCheckService.check(session, req);
        assertNotNull(result);
        assertEquals(new ArrayList<CheckResult>(), result);
    }

    @Test
    public void testCheckWithEnvironment(){
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = new ConnectionConfig();
        when(sqlCheckService.check(anyLong(), anyString(), anyList(), any(ConnectionConfig.class)))
            .thenReturn(new ArrayList<CheckViolation>());
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "databaseName", sqls, config);
        assertNotNull(checkViolations);
        assertEquals(new ArrayList<CheckViolation>(), checkViolations);
    }

    @Test
    public void testGetRulesWithSession(){
        when(sqlCheckService.getRules(anyList(), any(ConnectionSession.class)))
            .thenReturn(new ArrayList<SqlCheckRule>());
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<Rule>(), session);
        assertNotNull(rules);
        assertEquals(new ArrayList<SqlCheckRule>(), rules);
    }

    @Test
    public void testGetRulesWithDialectTypeAndJdbc(){
        JdbcOperations jdbc = new JdbcOperations();
        when(sqlCheckService.getRules(anyList(), any(DialectType.class), any(JdbcOperations.class)))
            .thenReturn(new ArrayList<SqlCheckRule>());
        List<SqlCheckRule> rules = sqlCheckService.getRules(new ArrayList<Rule>(), DialectType.MYSQL, jdbc);
        assertNotNull(rules);
        assertEquals(new ArrayList<SqlCheckRule>(), rules);
    }

    @Test
    public void testFullFillRiskLevel(){
        when(sqlCheckService.fullFillRiskLevel(anyList(), anyList()))
            .thenReturn(new ArrayList<Rule>());
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(new ArrayList<Rule>(), new ArrayList<CheckViolation>());
        assertNotNull(violatedRules);
        assertEquals(new ArrayList<Rule>(), violatedRules);
    }
}
