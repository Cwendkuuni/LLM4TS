
import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testConstructor() {
        WhoIS whoIs = new WhoIS();
        assertNotNull(whoIs);
    }

    @Test
    public void testQuery() {
        WhoIS whoIs = new WhoIS();
        whoIs.query("example");
        // verify the output is displayed properly
        // assert statements for output
    }

    @Test
    public void testStartQuery() {
        WhoIS whoIs = new WhoIS();
        // set queryTextField text with a query
        whoIs.startQuery();
        // verify the startQuery behavior
        // assert statements for expected behavior
    }

    @Test
    public void testDisplayError() {
        WhoIS whoIs = new WhoIS();
        whoIs.displayError("Error");
        // verify the error message is displayed properly
        // assert statements for error message
    }

    @Test
    public void testGetQuery() {
        WhoIS whoIs = new WhoIS();
        // set various options and values for the query fields
        String query = whoIs.getQuery();
        // verify the generated query string
        // assert statements for the query string
    }

    @Test
    public void testReset() {
        WhoIS whoIs = new WhoIS();
        whoIs.reset();
        // verify the reset behavior
        // assert statements for expected behavior after reset
    }
}

