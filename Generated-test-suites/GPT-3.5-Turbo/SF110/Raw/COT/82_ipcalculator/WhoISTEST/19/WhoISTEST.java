import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class WhoISTest {
    
    private WhoIS whoIS;
    
    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }
    
    @After
    public void tearDown() {
        whoIS = null;
    }
    
    @Test
    public void testStartQueryWithNoQuery() {
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // verify that an error message is displayed
    }
    
    @Test
    public void testStartQueryWithValidQuery() {
        whoIS.queryTextField.setText("example.com");
        // perform a query with a mocked response
        // verify that the result is displayed correctly
    }
    
    @Test
    public void testStartQueryWithInvalidHost() {
        whoIS.queryTextField.setText("example.com");
        whoIS.WHOIS_ADDRESS = "invalidhost";
        whoIS.startQuery();
        // verify that an error message is displayed
    }
    
    @Test
    public void testStartQueryWithNoInternetConnection() {
        whoIS.queryTextField.setText("example.com");
        whoIS.startQuery();
        // verify that an error message is displayed
    }
    
    @Test
    public void testReset() {
        whoIS.queryTextField.setText("example.com");
        whoIS.reset();
        assertEquals("", whoIS.queryTextField.getText());
        assertFalse(whoIS.resetRadioButton.isSelected());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
    
    // Add more test cases to cover all public methods
    
}