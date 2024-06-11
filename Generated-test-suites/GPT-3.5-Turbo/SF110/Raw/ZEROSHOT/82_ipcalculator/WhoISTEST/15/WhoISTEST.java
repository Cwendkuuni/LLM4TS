
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
    public void testStartQuery() {
        whoIS.queryTextField.setText("example");
        whoIS.startQuery();
        // assert expected result after query execution
    }

    @Test
    public void testStartQueryWithEmptyQuery() {
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // assert expected result for empty query
    }

    @Test
    public void testGetQuery() {
        whoIS.queryTextField.setText("example");
        String query = whoIS.getQuery();
        // assert expected query string based on selected options
    }

    @Test
    public void testReset() {
        whoIS.queryTextField.setText("example");
        whoIS.reset();
        // assert expected state of UI components after reset
    }
}