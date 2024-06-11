import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testStartQueryEmptyQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // Expect error message "No Query Detected"
        // Verify error message is displayed
    }

    @Test
    public void testStartQueryValidQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example query");
        whoIS.startQuery();
        // Verify that query method is called successfully
        // Verify appropriate output is displayed
    }

    @Test
    public void testStartQueryUnknownHostException() {
        WhoIS whoIS = new WhoIS();
        whoIS.WHOIS_ADDRESS = "invalidaddress";
        whoIS.queryTextField.setText("example query");
        whoIS.startQuery();
        // Expect error message "Unknown Whois host: invalidaddress"
        // Verify error message is displayed
    }

    @Test
    public void testStartQueryIOException() {
        WhoIS whoIS = new WhoIS();
        // Simulate IOException by setting WHOIS_PORT to an invalid value
        whoIS.WHOIS_PORT = 9999;
        whoIS.queryTextField.setText("example query");
        whoIS.startQuery();
        // Expect error message "Whois server could not be contacted. Please check your internet connection."
        // Verify error message is displayed
    }

    @Test
    public void testDisplayError() {
        WhoIS whoIS = new WhoIS();
        String errorMessage = "Test Error Message";
        whoIS.displayError(errorMessage);
        // Verify that the error message is displayed on the JPanel
    }

    @Test
    public void testGetQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example query");
        String query = whoIS.getQuery();
        // Verify the constructed query string based on various options and selections
    }

    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example query");
        whoIS.reset();
        // Verify that all fields are reset to their initial values
    }
}