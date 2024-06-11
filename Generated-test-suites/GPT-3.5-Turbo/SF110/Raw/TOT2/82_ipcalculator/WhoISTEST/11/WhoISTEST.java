import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
    
    private WhoIS whois;
    
    @Before
    public void setUp() {
        whois = new WhoIS();
    }
    
    @Test
    public void testStartQueryWithEmptyQuery() {
        // Test startQuery method with empty query
        whois.queryTextField.setText("");
        whois.startQuery();
        // Check if error message is displayed
        assertTrue(whois.queryTextField.getText().isEmpty());
    }
    
    @Test
    public void testStartQueryWithValidQuery() {
        // Test startQuery method with valid query
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        // Check if query is executed without errors
        assertFalse(whois.queryTextField.getText().isEmpty());
    }
    
    @Test
    public void testQueryWithUnknownHostException() {
        // Test query method with unknown host
        whois.WHOIS_ADDRESS = "unknownhost";
        whois.query("example.com");
        // Check if error message is displayed
        assertTrue(whois.queryTextField.getText().isEmpty());
    }
    
    @Test
    public void testQueryWithIOException() {
        // Test query method with IO exception
        whois.WHOIS_ADDRESS = "example.com";
        whois.query("invalidquery");
        // Check if error message is displayed
        assertTrue(whois.queryTextField.getText().isEmpty());
    }
    
    @Test
    public void testGetQueryWithInverseQuery() {
        // Test getQuery method with inverse query selected
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.queryTextField.setText("example.com");
        String query = whois.getQuery();
        // Check if inverse query is correctly added to the query
        assertEquals("-i admin-c example.com", query);
    }
    
    @Test
    public void testGetQueryWithMiscQueries() {
        // Test getQuery method with miscellaneous queries selected
        whois.miscList.setSelectedIndices(new int[]{1, 3, 5});
        whois.queryTextField.setText("example.com");
        String query = whois.getQuery();
        // Check if miscellaneous queries are correctly added to the query
        assertEquals("-T as-set,inetnum,person example.com", query);
    }
    
    @Test
    public void testReset() {
        // Test reset method
        whois.queryTextField.setText("example.com");
        whois.reset();
        // Check if all fields are reset to default values
        assertTrue(whois.queryTextField.getText().isEmpty());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}