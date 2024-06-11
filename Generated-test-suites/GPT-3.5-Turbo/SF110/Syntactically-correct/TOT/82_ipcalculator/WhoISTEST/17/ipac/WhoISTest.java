import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
    
    private WhoIS whois;
    
    @Before
    public void setUp() {
        whois = new WhoIS();
    }
    
    @After
    public void tearDown() {
        whois = null;
    }
    
    // Test cases for queryPanel() method
    @Test
    public void testQueryPanel() {
        assertNotNull(whois.queryPanel());
    }
    
    // Test cases for additionalQueriesPanel() method
    @Test
    public void testAdditionalQueriesPanel() {
        assertNotNull(whois.additionalQueriesPanel());
    }
    
    // Test cases for IPAddressLookups() method
    @Test
    public void testIPAddressLookups() {
        assertNotNull(whois.IPAddressLookups());
    }
    
    // Test cases for MiscellaneousQueries() method
    @Test
    public void testMiscellaneousQueries() {
        assertNotNull(whois.MiscellaneousQueries());
    }
    
    // Test cases for queryHintsPanel() method
    @Test
    public void testQueryHintsPanel() {
        assertNotNull(whois.queryHintsPanel());
    }
    
    // Test cases for startQuery() method
    @Test
    public void testStartQuery() {
        // Testing with valid query
        whois.queryTextField.setText("example");
        whois.startQuery();
        // Add assertions
        
        // Testing with empty query
        whois.queryTextField.setText("");
        whois.startQuery();
        // Add assertions
    }
    
    // Test cases for displayError() method
    @Test
    public void testDisplayError() {
        whois.displayError("Error message");
        // Add assertions
    }
    
    // Test cases for query() method
    @Test
    public void testQuery() {
        // Testing with valid query
        whois.query("example");
        // Add assertions
        
        // Testing with unknown host
        whois.WHOIS_ADDRESS = "unknownHost";
        whois.query("example");
        // Add assertions
        
        // Testing with failure in server connection
        whois.WHOIS_ADDRESS = "failureServer";
        whois.query("example");
        // Add assertions
    }
    
    // Test cases for getQuery() method
    @Test
    public void testGetQuery() {
        // Testing with default values
        String expectedQuery = "";
        String actualQuery = whois.getQuery();
        assertEquals(expectedQuery, actualQuery);
        
        // Testing with different query types
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        expectedQuery = "-l example";
        actualQuery = whois.getQuery();
        assertEquals(expectedQuery, actualQuery);
        
        // Testing with inverse query
        whois.inverseQueries.setSelectedItem("admin-c");
        expectedQuery = "-i admin-c example";
        actualQuery = whois.getQuery();
        assertEquals(expectedQuery, actualQuery);
        
        // Testing with multiple selected misc queries
        whois.miscList.setSelectedValue("as-block", true);
        whois.miscList.setSelectedValue("domain", true);
        expectedQuery = "-T as-block,domain example";
        actualQuery = whois.getQuery();
        assertEquals(expectedQuery, actualQuery);
    }
    
    // Test cases for reset() method
    @Test
    public void testReset() {
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.reset();
        // Add assertions
    }
}