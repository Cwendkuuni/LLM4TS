import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
    }

    @Test
    public void testQueryPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        assertNotNull(queryPanel);
        
        // Testing mousePressed event
        queryPanel.getQueryTextField().mousePressed();
        assertTrue(queryPanel.getPopupMenu().isVisible());

        // Testing mouseReleased event
        queryPanel.getQueryTextField().mouseReleased();
        assertTrue(queryPanel.getPopupMenu().isVisible());

        // Testing keyPressed event
        queryPanel.getQueryTextField().setKeyCode(10);  // Enter key
        queryPanel.getQueryTextField().keyPressed();
        verify(whois).startQuery();
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
        assertNotNull(whois.getPopupMenu());
    }

    @Test
    public void testActionPerformed() {
        WhoIS whois = new WhoIS();
        
        // Testing queryButton action
        ActionEvent queryButtonEvent = new ActionEvent(whois.getQueryButton(), ActionEvent.ACTION_PERFORMED, "Query");
        queryButtonEvent.getSource().actionPerformed(queryButtonEvent);
        verify(whois).startQuery();

        // Testing resetButton action
        ActionEvent resetButtonEvent = new ActionEvent(whois.getResetButton(), ActionEvent.ACTION_PERFORMED, "Reset");
        resetButtonEvent.getSource().actionPerformed(resetButtonEvent);
        verify(whois).reset();
    }

    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();

        // Testing empty query
        whois.getQueryTextField().setText("");
        whois.startQuery();
        verify(whois).displayError("No Query Detected");

        // Testing valid query
        whois.getQueryTextField().setText("example");
        whois.startQuery();
        verify(whois).query("example");
    }

    @Test
    public void testDisplayError() {
        WhoIS whois = new WhoIS();
        whois.displayError("Error message");
        // Check if error message is displayed correctly
    }

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        // TODO: Add test cases for query method
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        // TODO: Add test cases for getQuery method
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.reset();
        // TODO: Verify if necessary components are reset to their initial state
    }

}