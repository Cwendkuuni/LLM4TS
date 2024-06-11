import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WhoISTest {
    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testStartQueryNoQueryDetected() {
        whois.queryButton.doClick();
        // Verify that an error message is displayed
    }

    @Test
    public void testStartQuery() {
        whois.queryTextField.setText("example query");
        whois.queryButton.doClick();
        // Verify that the query is sent and processed
    }

    @Test
    public void testReset() {
        whois.queryTextField.setText("example query");
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedIndex(1);
        whois.miscList.setSelectedIndex(1);

        whois.reset();

        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }

    // Additional test cases for edge cases and exception handling scenarios
    // ...

    // Test cases for other public methods in the class
    // ...

}