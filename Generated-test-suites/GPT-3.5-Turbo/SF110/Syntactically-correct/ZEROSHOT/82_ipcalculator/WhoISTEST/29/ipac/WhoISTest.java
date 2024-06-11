package ipac;

import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.reset();
        whois.queryButton.doClick();
        assertNotNull(whois);
        assertTrue(whois.queryTextField.getText().trim().isEmpty());
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedIndex(1);
        whois.miscList.setSelectedIndex(2);
        whois.resetButton.doClick();
        assertNotNull(whois);
        assertTrue(whois.queryTextField.getText().trim().isEmpty());
        assertFalse(whois.lowercaseL.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }

    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.queryButton.doClick();
        assertNotNull(whois);
        assertEquals("example", whois.getQuery());
    }

    @Test
    public void testQuery() {
        // Test the query method
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedIndex(1);
        whois.miscList.setSelectedIndex(2);
        assertEquals("-l -d -i admin-c example", whois.getQuery());
    }

    @Test
    public void testDisplayError() {
        // Test the displayError method
    }

}