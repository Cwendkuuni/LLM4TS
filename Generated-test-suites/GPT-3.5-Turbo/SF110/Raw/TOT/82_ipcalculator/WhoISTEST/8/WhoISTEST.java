import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
    
    private WhoIS whoIS;
    
    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }

    @Test
    public void testStartQueryWithNoQuery() {
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // The method should display an error
        // We can check if the error dialog is displayed by checking the background color of the error dialog
        assertEquals(Color.RED, whoIS.getBackground());
    }

    @Test
    public void testStartQueryWithValidQuery() {
        whoIS.queryTextField.setText("example.com");
        whoIS.startQuery();
        // The startQuery() method should only initiate the query with the given query string
        // Since we cannot verify the actual query result, we can assert that the method does not throw any exception
        // If the query is successful, a new WhoISOutput object would be created, but it's not easy to test its validity
        // So, in this test case, we just assert that no exception is thrown
    }

    @Test
    public void testDisplayError() {
        whoIS.displayError("Error Message");
        // The displayError() method should display an error dialog with the given message
        // We can check if the error dialog is displayed by checking the background color of the error dialog
        assertEquals(Color.RED, whoIS.getBackground());
    }

    @Test
    public void testReset() {
        whoIS.queryTextField.setText("example.com");
        whoIS.lowercaseD.setSelected(true);
        whoIS.inverseQueries.setSelectedIndex(1);
        whoIS.miscList.setSelectedIndex(1);
        whoIS.reset();
        // The reset() method should reset the query text field, deselect all radio buttons and check boxes, and reset the combo box and list to their initial states
        // We can assert these conditions
        assertEquals("", whoIS.queryTextField.getText());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }

    // Add more test cases for other public and private methods if needed
    
    @Test
    public void testWhoISConstructor() {
        // The constructor should initialize all the instance variables and create the necessary panels and components
        // We can assert that the instance variables are properly initialized
        assertEquals("APNIC", whoIS.WHOIS_NAME);
        assertEquals("whois.apnic.net", whoIS.WHOIS_ADDRESS);
        assertEquals(43, whoIS.WHOIS_PORT);
        // We can also assert that the panels and components are created, but it's difficult to test their validity
        // So, we will just assert that they are not null
        assertNotNull(whoIS.queryTextField);
        assertNotNull(whoIS.queryButton);
        assertNotNull(whoIS.resetButton);
        assertNotNull(whoIS.lowercaseD);
        assertNotNull(whoIS.radioButtonGroup);
        assertNotNull(whoIS.popupMenu);
        assertNotNull(whoIS.inverseQueries);
        assertNotNull(whoIS.miscList);
    }
}