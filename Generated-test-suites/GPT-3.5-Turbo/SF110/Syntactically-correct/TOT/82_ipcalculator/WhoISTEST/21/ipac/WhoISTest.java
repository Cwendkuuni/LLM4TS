import static org.junit.Assert.*;
import org.junit.*;

public class WhoISTest {

    @Test
    public void testStartQueryWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText(""); // Setting empty query
        whois.startQuery();
        // Assert that an error message is displayed
        // Replace the message below with the actual error message displayed
        assertEquals("No Query Detected", whois.displayedErrorMessage);
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com"); // Setting valid query
        whois.startQuery();
        // Assert that the query is successful and the WhoISOutput is displayed
        // Replace the expectedOutput below with the expected output in the WhoISOutput window
        assertEquals(expectedOutput, whois.whoIsOutputWindow.displayedOutput);
    }

    @Test
    public void testStartQueryWithUnknownHostException() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com"); // Setting valid query
        // Injecting a fake WHOIS_ADDRESS that does not exist
        whois.WHOIS_ADDRESS = "fakeaddress.com";
        whois.startQuery();
        // Assert that an error message is displayed
        // Replace the message below with the actual error message displayed
        assertEquals("Unknown Whois host: fakeaddress.com", whois.displayedErrorMessage);
    }

    @Test
    public void testStartQueryWithIOException() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com"); // Setting valid query
        // Injecting a fake WHOIS_PORT that is not open
        whois.WHOIS_PORT = 12345;
        whois.startQuery();
        // Assert that an error message is displayed
        // Replace the message below with the actual error message displayed
        assertEquals("Whois server could not be contacted.\nPlease check your internet connection.", whois.displayedErrorMessage);
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.reset();

        // Assert that all fields have been reset to their initial state
        assertEquals("", whois.queryTextField.getText());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }

    @Test
    public void testGetQueryWithInverseQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com"); // Setting valid query
        whois.inverseQueries.setSelectedIndex(1); // Setting inverse query

        // Assert that the returned query contains the selected inverse query
        assertEquals("-i admin-c example.com", whois.getQuery());
    }

    @Test
    public void testGetQueryWithLevelSpecific() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com"); // Setting valid query
        whois.lowercaseL.setSelected(true); // Setting level specific

        // Assert that the returned query contains the selected level specific flag
        assertEquals("-l example.com", whois.getQuery());
    }

    @Test
    public void testGetQueryWithMoreSpecific() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com"); // Setting valid query
        whois.uppercaseM.setSelected(true); // Setting more specific

        // Assert that the returned query contains the selected more specific flag
        assertEquals("-M example.com", whois.getQuery());
    }

    @Test
    public void testGetQueryWithExactMatch() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com"); // Setting valid query
        whois.lowercaseX.setSelected(true); // Setting exact match

        // Assert that the returned query contains the selected exact match flag
        assertEquals("-x example.com", whois.getQuery());
    }

    @Test
    public void testGetQueryWithMiscQueries() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com"); // Setting valid query
        whois.miscList.setSelectedIndices(new int[] {1, 3, 5}); // Selecting multiple misc queries

        // Assert that the returned query contains the selected misc queries
        assertEquals("-T as-block,aut-num,filter-set example.com", whois.getQuery());
    }

}