import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        // Typical use case
        whois.queryTextField.setText("example query");
        whois.startQuery();

        // Edge case: query field is empty
        whois.queryTextField.setText("");
        whois.startQuery();

        // Error scenario: Unknown host
        whois.WHOIS_ADDRESS = "unknownhost";
        whois.queryTextField.setText("example query");
        whois.startQuery();
    }

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();

        // Edge case: Empty query
        whois.query("");
        // Edge case: Unknown host
        whois.WHOIS_ADDRESS = "unknownhost";
        whois.query("example query");
        // Error scenario: IO exception
        whois.WHOIS_ADDRESS = "whois.apnic.net";
        whois.WHOIS_PORT = 9999; // Invalid port
        whois.query("example query");
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();

        // Typical use case
        whois.queryTextField.setText("example query");
        String query = whois.getQuery();
        assertEquals("example query", query);

        // Edge case: Inverse query selected
        whois.queryTextField.setText("example query");
        whois.inverseQueries.setSelectedIndex(1);
        query = whois.getQuery();
        assertEquals("-i admin-c example query", query);

        // Edge case: MiscList selected
        whois.queryTextField.setText("example query");
        whois.miscList.setSelectedIndex(1);
        query = whois.getQuery();
        assertEquals("-T as-set example query", query);
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();

        // Typical use case
        whois.queryTextField.setText("example query");
        whois.reset();
        assertEquals("", whois.queryTextField.getText());

        // Edge case: Radio button reset
        whois.reset();
        assertTrue(whois.resetRadioButton.isSelected());

        // Edge case: Checkbox reset
        whois.reset();
        assertFalse(whois.lowercaseD.isSelected());
    }
}