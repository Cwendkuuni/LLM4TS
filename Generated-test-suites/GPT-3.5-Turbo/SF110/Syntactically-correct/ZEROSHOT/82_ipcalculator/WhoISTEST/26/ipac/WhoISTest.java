import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testQueryPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
    }
    
    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        assertNotNull(additionalQueriesPanel);
    }
    
    @Test
    public void testIPAddressLookups() {
        WhoIS whois = new WhoIS();
        JPanel ipAddressLookups = whois.IPAddressLookups();
        assertNotNull(ipAddressLookups);
    }
    
    @Test
    public void testMiscellaneousQueries() {
        WhoIS whois = new WhoIS();
        JPanel miscellaneousQueries = whois.MiscellaneousQueries();
        assertNotNull(miscellaneousQueries);
    }
    
    @Test
    public void testQueryHintsPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        assertNotNull(queryHintsPanel);
    }
    
    @Test
    public void testCreatePopupMenu() {
        WhoIS whois = new WhoIS();
        whois.createPopupMenu();
        assertNotNull(whois.popupMenu);
    }
    
    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.startQuery();
        // Check if a query was successfully started
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.reset();
        // Check if all components are reset to their initial states
    }
    
    @Test
    public void testgetQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        String query = whois.getQuery();
        assertEquals("-l example", query);
    }
}