import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;

public class SqlCheckServiceTest {

    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        sqlCheckService = new SqlCheckService();
    }

    @Test
    public void testCheck_ValidSessionAndRequest_ReturnCheckResults() {
        // Given
        ConnectionSession session = new ConnectionSession();
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        
        // When
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        // Add more assertions as needed
    }

    @Test
    public void testCheck_InvalidSession_ReturnEmptyList() {
        // Given
        ConnectionSession session = null;
        SqlCheckReq req = new SqlCheckReq();
        
        // When
        List<CheckResult> result = sqlCheckService.check(session, req);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCheckViolations_ValidEnvironmentAndSQL_ReturnCheckViolations() {
        // Given
        Long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(0, "SELECT * FROM table"));
        ConnectionConfig config = new ConnectionConfig();
        
        // When
        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        // Add more assertions as needed
    }
}