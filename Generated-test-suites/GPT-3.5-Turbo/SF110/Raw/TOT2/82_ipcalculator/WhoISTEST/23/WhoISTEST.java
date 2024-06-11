import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        // Typical use case
        whois.queryTextField.setText("example query");
        whois.startQuery();
        // Edge case: query is empty
        whois.queryTextField.setText("");
        whois.startQuery();
        // Error scenario
        // Test if an error message is displayed when query is empty
        // (this depends on the implementation of displayError() method)
    }

    @Test
    public void testDisplayError() {
        WhoIS whois = new WhoIS();
        // Typical use case
        whois.displayError("Sample error message");
        // Edge case: error message is empty
        whois.displayError("");
        // Error scenario
        // Test if the error message is displayed correctly
    }

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        // Typical use case
        // This test requires mocking the Socket, PrintStream, and BufferedReader classes
        // and verifying the interaction between them
        // Edge case: UnknownHostException
        // This test requires mocking the Socket class to throw UnknownHostException
        // Error scenario: IOException
        // This test requires mocking the Socket class to throw IOException
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        // Typical use case
        whois.queryTextField.setText("example query");
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        whois.miscList.setSelectedIndex(1);
        assertEquals("-i admin-c -l -d -T as-set example query", whois.getQuery());
        // Edge case: No inverse query selected
        whois.inverseQueries.setSelectedItem("None");
        assertEquals("-l -d -T as-set example query", whois.getQuery());
        // Error scenario
        // Test if the query is correctly constructed with different combinations of options
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        // Typical use case
        whois.queryTextField.setText("example query");
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        whois.miscList.setSelectedIndex(1);
        whois.reset();
        // Check if all fields are reset to their default values
        assertEquals("", whois.queryTextField.getText());
        assertEquals(true, whois.resetRadioButton.isSelected());
        assertEquals(false, whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}