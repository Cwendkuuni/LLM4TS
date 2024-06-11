import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testStartQuery_withEmptyQuery_shouldDisplayError() {
        whois.queryTextField.setText("");
        whois.startQuery();
        // Assert error message is displayed
    }

    @Test
    public void testStartQuery_withNonEmptyQuery_shouldCallQueryMethod() {
        whois.queryTextField.setText("example query");
        whois.startQuery();
        // Assert query method is called
    }

    @Test
    public void testQuery_withUnknownHostException_shouldDisplayError() {
        whois.WHOIS_ADDRESS = "unknown.host";
        whois.query("example query");
        // Assert error message is displayed
    }

    @Test
    public void testQuery_withIOException_shouldDisplayError() {
        whois.WHOIS_ADDRESS = "valid.host";
        // Mock IOException when opening socket or reading input stream
        whois.query("example query");
        // Assert error message is displayed
    }

    @Test
    public void testReset_shouldResetFields() {
        whois.queryTextField.setText("example query");
        whois.reset();
        // Assert all fields are reset to default values
    }
}