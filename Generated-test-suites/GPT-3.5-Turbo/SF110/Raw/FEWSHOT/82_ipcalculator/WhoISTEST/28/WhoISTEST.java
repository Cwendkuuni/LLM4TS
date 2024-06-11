import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
    
    private WhoIS whois;

    @Before
    public void setup() {
        whois = new WhoIS();
    }

    @Test
    public void testStartQuery() {
        // Test case when query is empty
        whois.queryTextField.setText("");
        whois.startQuery(); // Error message should be displayed

        // Test case when query is not empty
        whois.queryTextField.setText("query");
        whois.startQuery(); // Successful query should be made
    }

    @Test
    public void testReset() {
        whois.queryTextField.setText("query");
        whois.reset();

        // Check if query text field is empty after reset
        assertEquals("", whois.queryTextField.getText());

        // Check if radio buttons and checkboxes are reset to default state
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());

        // Check if combobox and list selection are reset to default
        assertEquals("None", whois.inverseQueries.getSelectedItem());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}