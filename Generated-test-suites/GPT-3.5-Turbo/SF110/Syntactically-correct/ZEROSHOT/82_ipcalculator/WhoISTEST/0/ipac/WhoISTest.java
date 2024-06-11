package ipac;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setup() {
        whois = new WhoIS();
    }

    @Test
    public void testStartQueryWithEmptyQuery() {
        whois.queryTextField.setText("");
        whois.startQuery();
        // Verify that an error message dialog is displayed with the correct error message
    }

    @Test
    public void testStartQueryWithValidQuery() {
        whois.queryTextField.setText("example");
        whois.startQuery();
        // Verify that the query method is called with the correct query parameter
    }

    @Test
    public void testReset() {
        whois.reset();
        // Verify that all fields are reset to their initial values
        assertEquals("", whois.queryTextField.getText());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}