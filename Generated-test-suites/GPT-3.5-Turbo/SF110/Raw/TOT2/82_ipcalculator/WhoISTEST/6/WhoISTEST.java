import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {
    
    @Test
    public void testQueryPanel() {
        // Test basic functionality of queryPanel() method
        WhoIS whois = new WhoIS();
        assertNotNull(whois.queryPanel());
    }
    
    @Test
    public void testIPAddressLookups() {
        // Test IP Address lookups functionality
        WhoIS whois = new WhoIS();
        assertNotNull(whois.IPAddressLookups());
    }
    
    @Test
    public void testMiscellaneousQueries() {
        // Test Miscellaneous Queries functionality
        WhoIS whois = new WhoIS();
        assertNotNull(whois.MiscellaneousQueries());
    }
    
    @Test
    public void testCreatePopupMenu() {
        // Test creating the popup menu
        WhoIS whois = new WhoIS();
        whois.createPopupMenu();
        assertNotNull(whois.popupMenu);
    }
    
    @Test
    public void testActionPerformed() {
        // Test actionPerformed method
        WhoIS whois = new WhoIS();
        ActionEvent e = new ActionEvent(new Object(), 0, "test");
        whois.actionPerformed(e); // Expecting no exceptions
    }
    
    @Test
    public void testStartQuery() {
        // Test startQuery method
        WhoIS whois = new WhoIS();
        whois.startQuery(); // Expecting no exceptions
    }
    
    @Test
    public void testDisplayError() {
        // Test displayError method
        WhoIS whois = new WhoIS();
        whois.displayError("Test Error Message"); // Expecting no exceptions
    }
    
    @Test
    public void testQuery() {
        // Test query method
        WhoIS whois = new WhoIS();
        whois.query("Test Query"); // Expecting no exceptions
    }
    
    @Test
    public void testGetQuery() {
        // Test getQuery method
        WhoIS whois = new WhoIS();
        String query = whois.getQuery();
        assertNotNull(query);
    }
    
    @Test
    public void testReset() {
        // Test reset method
        WhoIS whois = new WhoIS();
        whois.reset(); // Expecting no exceptions
    }
}