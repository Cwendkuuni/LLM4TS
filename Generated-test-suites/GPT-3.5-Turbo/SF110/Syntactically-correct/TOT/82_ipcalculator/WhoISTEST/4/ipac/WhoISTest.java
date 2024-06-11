import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testStartQueryWithEmptyQuery() {
        String query = "";
        whois.queryTextField.setText(query);
        whois.startQuery();
        // Assert that an error message is displayed
        // Assert that the query text field is not cleared
        // Assert that no connection is made
    }

    @Test
    public void testStartQueryWithValidQuery() {
        String query = "example";
        whois.queryTextField.setText(query);
        whois.startQuery();
        // Assert that no error message is displayed
        // Assert that the query text field is cleared
        // Assert that a connection is made
    }

    @Test
    public void testStartQueryWithInvalidHost() {
        String query = "example";
        whois.queryTextField.setText(query);
        whois.WHOIS_ADDRESS = "invalidhost";
        whois.startQuery();
        // Assert that an error message is displayed
        // Assert that the query text field is not cleared
        // Assert that no connection is made
    }

    @Test
    public void testQueryWithValidQuery() {
        String query = "example";
        whois.query(query);
        // Assert that no error message is displayed
        // Assert that a connection is made
    }

    @Test
    public void testQueryWithInvalidHost() {
        String query = "example";
        whois.WHOIS_ADDRESS = "invalidhost";
        whois.query(query);
        // Assert that an error message is displayed
        // Assert that no connection is made
    }

    @Test
    public void testGetQueryWithDefaultSettings() {
        String expected = "-l example";
        String query = "example";
        whois.queryTextField.setText(query);
        String result = whois.getQuery();
        assertEquals(expected, result);
    }

    @Test
    public void testGetQueryWithInverseQueries() {
        // Test each inverse query option and check the resulting query string
    }

    @Test
    public void testGetQueryWithIPAddressLookups() {
        // Test each IP address lookup option and check the resulting query string
    }

    @Test
    public void testGetQueryWithMiscellaneousQueries() {
        // Test selecting different object types and check the resulting query string
    }

    @Test
    public void testReset() {
        // Assert that all fields are reset to their default values
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }

    @After
    public void tearDown() {
        whois = null;
    }
}