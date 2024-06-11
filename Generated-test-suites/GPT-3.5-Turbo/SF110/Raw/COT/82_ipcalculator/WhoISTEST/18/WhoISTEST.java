import org.junit.Test;

public class WhoISTest {

    @Test
    public void testQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.query("example query");
        // Add assertions to verify the query results
    }

    @Test
    public void testGetQuery() {
        WhoIS whoIS = new WhoIS();
        String query = whoIS.getQuery();
        // Add assertions to verify the returned query
    }

    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();
        whoIS.reset();
        // Add assertions to verify that the fields have been reset
    }
}