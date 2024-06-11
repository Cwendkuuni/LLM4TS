import org.junit.Test;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {

    @Test
    public void testGetConnection() {
        // Test when connection is null
        // Test when connection is closed
        // Test when connection is not valid
    }

    @Test
    public void testGetConnectionWithCredentials() {
        // Test with valid username and password
        // Test with invalid username or password
    }

    @Test
    public void testResetConnection() {
        // Test resetting connection
        // Test exceptions thrown during connection reset
    }

    @Test
    public void testAddInitializer() {
        // Test adding valid ConnectionInitializer
        // Test adding invalid ConnectionInitializer
    }

    @Test
    public void testClose() {
        // Test closing connection
        // Test exceptions thrown during connection close
    }
}

