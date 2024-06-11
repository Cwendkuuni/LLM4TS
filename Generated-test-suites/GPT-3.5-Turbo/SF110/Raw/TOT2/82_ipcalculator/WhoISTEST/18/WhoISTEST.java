import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class WhoISTest {
    
    private WhoIS whoIS;
    
    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }
    
    @Test
    public void testStartQueryNoQueryDetected() {
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // Check if error dialog is displayed
        // assertErrorDialogDisplayed();
    }
    
    @Test
    public void testStartQueryWithQuery() {
        whoIS.queryTextField.setText("test query");
        whoIS.startQuery();
        // Check if query is executed without errors
        // assertQueryExecutedSuccessfully();
    }
    
    @Test
    public void testQueryUnknownHostException() {
        whoIS.WHOIS_ADDRESS = "invalidaddress";
        whoIS.query("test query");
        // Check if error dialog is displayed for UnknownHostException
        // assertErrorDialogDisplayed();
    }
    
    @Test
    public void testQueryIOException() {
        whoIS.WHOIS_ADDRESS = "invalidhost";
        whoIS.query("test query");
        // Check if error dialog is displayed for IOException
        // assertErrorDialogDisplayed();
    }
    
    @Test
    public void testGetQueryWithInverseQuery() {
        whoIS.queryTextField.setText("test query");
        whoIS.inverseQueries.setSelectedItem("admin-c");
        String query = whoIS.getQuery();
        assertEquals("-i admin-c test query", query);
    }
    
    @Test
    public void testGetQueryWithOptions() {
        whoIS.queryTextField.setText("test query");
        whoIS.lowercaseX.setSelected(true);
        whoIS.booleanD = true;
        whoIS.miscList.setSelectedIndex(2);
        String query = whoIS.getQuery();
        assertEquals("-x -d -T admin-c,test query", query);
    }
    
    // Add more test cases for edge cases and error scenarios
    
}