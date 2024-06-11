import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Mock
    private ConnectionSession session;

    @Mock
    private JdbcOperations jdbcOperations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheckWithSession() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        List<CheckResult> expectedResults = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType("TYPE");
        violation.setReason("REASON");
        violations.add(violation);
        CheckResult expectedResult = new CheckResult();
        expectedResult.setViolations(violations);
        expectedResults.add(expectedResult);
        
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);
        when(jdbcOperations.queryForObject(anyString(), any(), eq(Long.class))).thenReturn(123L);

        List<CheckResult> actualResults = sqlCheckService.check(session, req);
        assertEquals(expectedResults, actualResults);
    }

    @Test
    public void testCheckWithEnvironment() {
        Long environmentId = 456L;
        String databaseName = "database";
        List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM table", 0));
        
        List<CheckViolation> expectedViolations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType("TYPE");
        violation.setReason("REASON");
        expectedViolations.add(violation);
        
        when(jdbcOperations.queryForObject(anyString(), any(), eq(Long.class))).thenReturn(789L);

        List<CheckViolation> actualViolations = sqlCheckService.check(environmentId, databaseName, sqls, null);
        assertEquals(expectedViolations, actualViolations);
    }

}