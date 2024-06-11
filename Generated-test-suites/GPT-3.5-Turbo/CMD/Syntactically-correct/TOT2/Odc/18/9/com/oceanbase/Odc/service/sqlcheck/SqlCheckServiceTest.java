import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    @Test
    public void testCheck_sessionAndReqNotNull_returnCheckResults() {
        // Arrange
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        SqlCheckService service = new SqlCheckService();

        // Act
        List<CheckResult> results = service.check(session, req);

        // Assert
        assertNotNull(results);
        // Add more assertions based on expected behavior
    }

    @Test
    public void testCheck_sessionNull_returnEmptyList() {
        // Arrange
        ConnectionSession session = null;
        SqlCheckReq req = new SqlCheckReq();
        SqlCheckService service = new SqlCheckService();

        // Act
        List<CheckResult> results = service.check(session, req);

        // Assert
        assertTrue(results.isEmpty());
        // Add more assertions based on expected behavior
    }

    // Add more test cases for other public methods in the SqlCheckService class

}