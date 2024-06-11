import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.query("exampleQuery");
        // Assert the expected output or behavior
    }

    @Test
    public void testGetQuery() {
        WhoIS whoIS = new WhoIS();
        String query = whoIS.getQuery();

        // Test for the expected query string
        assertEquals("Expected query string", query);
    }

    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();
        whoIS.reset();
        // Assert the expected state after reset
    }

    // Add more test cases for other methods in the WhoIS class

    // Test edge cases and exception handling scenarios

}



// Individual test cases go here as generated above

