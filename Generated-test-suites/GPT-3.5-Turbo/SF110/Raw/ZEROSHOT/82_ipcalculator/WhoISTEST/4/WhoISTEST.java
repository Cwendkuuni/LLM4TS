import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testQueryPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();

        // Assertion statements
        assertNotNull(queryPanel);
        assertEquals(FlowLayout.class, queryPanel.getLayout().getClass());
        assertEquals(4, queryPanel.getComponentCount());
    }

    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();

        // Assertion statements
        assertNotNull(additionalQueriesPanel);
        assertEquals(FlowLayout.class, additionalQueriesPanel.getLayout().getClass());
        assertEquals(2, additionalQueriesPanel.getComponentCount());
    }

    @Test
    public void testIPAddressLookups() {
        WhoIS whois = new WhoIS();
        JPanel ipAddressLookups = whois.IPAddressLookups();

        // Assertion statements
        assertNotNull(ipAddressLookups);
        assertEquals(BoxLayout.class, ipAddressLookups.getLayout().getClass());
        assertEquals(6, ipAddressLookups.getComponentCount());
    }

    @Test
    public void testMiscellaneousQueries() {
        WhoIS whois = new WhoIS();
        JPanel miscellaneousQueries = whois.MiscellaneousQueries();

        // Assertion statements
        assertNotNull(miscellaneousQueries);
        assertEquals(BoxLayout.class, miscellaneousQueries.getLayout().getClass());
        assertEquals(2, miscellaneousQueries.getComponentCount());
    }

    @Test
    public void testQueryHintsPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();

        // Assertion statements
        assertNotNull(queryHintsPanel);
        assertEquals(FlowLayout.class, queryHintsPanel.getLayout().getClass());
        assertEquals(1, queryHintsPanel.getComponentCount());
    }

    @Test
    public void testActionPerformed_queryButton() {
        WhoIS whois = new WhoIS();
        ActionEvent event = new ActionEvent(whois.queryButton, 0, "queryButton");
        whois.actionPerformed(event);

        // Assertion statements
        // Check if the startQuery() method is called
    }

    @Test
    public void testActionPerformed_resetButton() {
        WhoIS whois = new WhoIS();
        ActionEvent event = new ActionEvent(whois.resetButton, 0, "resetButton");
        whois.actionPerformed(event);

        // Assertion statements
        // Check if the reset() method is called
    }

    @Test
    public void testStartQuery_emptyQuery() {
        WhoIS whois = new WhoIS();
        whois.startQuery();

        // Assertion statements
        // Check if the displayError() method is called
    }

    @Test
    public void testStartQuery_validQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.startQuery();

        // Assertion statements
        // Check if the query() method is called with the correct query string
    }

    @Test
    public void testDisplayError() {
        WhoIS whois = new WhoIS();
        String error = "Test Error";
        whois.displayError(error);

        // Assertion statements
        // Check if the JOptionPane.showMessageDialog() method is called with the correct arguments
    }

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        String query = "example query";
        whois.query(query);

        // Assertion statements
        // Check if the Socket, PrintStream, and BufferedReader objects are created and used correctly
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        String expectedQuery = "-i admin-c example query -l -d";
        String actualQuery = whois.getQuery();

        // Assertion statements
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.miscList.setSelectedIndex(1);
        whois.reset();

        // Assertion statements
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}