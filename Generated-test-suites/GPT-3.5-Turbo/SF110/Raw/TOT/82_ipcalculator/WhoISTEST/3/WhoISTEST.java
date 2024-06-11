import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    private WhoIS whoIS;

    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }

    @Test
    public void testStartQueryWithEmptyQuery() {
        String query = "";
        whoIS.queryTextField.setText(query);
        whoIS.startQuery();
        // Validate that an error message is displayed
    }

    @Test
    public void testStartQueryWithValidQuery() {
        String query = "exampleQuery";
        whoIS.queryTextField.setText(query);
        whoIS.startQuery();
        // Validate that the query is sent to the server
    }

    @Test
    public void testStartQueryWithInvalidWhoisAddress() {
        whoIS.WHOIS_ADDRESS = "invalidAddress";
        whoIS.startQuery();
        // Validate that an error message is displayed
    }

    @Test
    public void testStartQueryWithUnreachableWhoisServer() {
        // Simulate an unreachable Whois server by closing the connection
        Socket connection = mock(Socket.class);
        when(connection.getInputStream()).thenThrow(IOException.class);
        whoIS.connection = connection;
        whoIS.startQuery();
        // Validate that an error message is displayed
    }

    @Test
    public void testGetQueryWithInverseQuery() {
        String query = "exampleQuery";
        String inverseQuery = "admin-c";
        whoIS.queryTextField.setText(query);
        whoIS.inverseQueries.setSelectedItem(inverseQuery);
        String expectedQuery = "-i " + inverseQuery + " " + query;
        assertEquals(expectedQuery, whoIS.getQuery());
    }

    @Test
    public void testGetQueryWithSpecificLevelLookup() {
        String query = "exampleQuery";
        whoIS.queryTextField.setText(query);
        whoIS.lowercaseL.setSelected(true);
        String expectedQuery = "-l " + query;
        assertEquals(expectedQuery, whoIS.getQuery());
    }

    @Test
    public void testGetQueryWithAllLevelLookup() {
        String query = "exampleQuery";
        whoIS.queryTextField.setText(query);
        whoIS.uppercaseL.setSelected(true);
        String expectedQuery = "-L " + query;
        assertEquals(expectedQuery, whoIS.getQuery());
    }

    @Test
    public void testGetQueryWithSpecificLevelMoreSpecificLookup() {
        String query = "exampleQuery";
        whoIS.queryTextField.setText(query);
        whoIS.lowercaseM.setSelected(true);
        String expectedQuery = "-m " + query;
        assertEquals(expectedQuery, whoIS.getQuery());
    }

    @Test
    public void testGetQueryWithAllLevelMoreSpecificLookup() {
        String query = "exampleQuery";
        whoIS.queryTextField.setText(query);
        whoIS.uppercaseM.setSelected(true);
        String expectedQuery = "-M " + query;
        assertEquals(expectedQuery, whoIS.getQuery());
    }

    @Test
    public void testGetQueryWithExactMatchOnlyLookup() {
        String query = "exampleQuery";
        whoIS.queryTextField.setText(query);
        whoIS.lowercaseX.setSelected(true);
        String expectedQuery = "-x " + query;
        assertEquals(expectedQuery, whoIS.getQuery());
    }

    @Test
    public void testGetQueryWithAssociatedReverseDomain() {
        String query = "exampleQuery";
        whoIS.queryTextField.setText(query);
        whoIS.lowercaseD.setSelected(true);
        String expectedQuery = "-d " + query;
        assertEquals(expectedQuery, whoIS.getQuery());
    }

    @Test
    public void testGetQueryWithSelectedMiscQueries() {
        String query = "exampleQuery";
        whoIS.queryTextField.setText(query);
        whoIS.miscList.setSelectedValue("as-set", true);
        whoIS.miscList.setSelectedValue("filter-set", true);
        String expectedQuery = "-T as-set,filter-set " + query;
        assertEquals(expectedQuery, whoIS.getQuery());
    }

    @Test
    public void testReset() {
        String query = "exampleQuery";
        String inverseQuery = "admin-c";
        whoIS.queryTextField.setText(query);
        whoIS.inverseQueries.setSelectedItem(inverseQuery);
        whoIS.lowercaseD.setSelected(true);
        whoIS.miscList.setSelectedValue("as-set", true);

        whoIS.reset();

        assertEquals("", whoIS.queryTextField.getText());
        assertFalse(whoIS.resetRadioButton.isSelected());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
}