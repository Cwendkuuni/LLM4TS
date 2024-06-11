package ipac;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WhoISTest {

    private WhoIS whoIS;

    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }

    @Test
    public void testStartQueryWithEmptyQuery() {
        whoIS.startQuery();
        // Test that an error message is displayed
        // Assert the error message is correct
    }

    @Test
    public void testStartQueryWithValidQuery() {
        whoIS.queryTextField.setText("example");
        whoIS.startQuery();
        // Test that the query is executed correctly
        // Assert the output is as expected
    }

    @Test
    public void testReset() {
        whoIS.reset();
        // Test that all fields are reset to their initial values
        // Assert each field is in its initial state
    }
}