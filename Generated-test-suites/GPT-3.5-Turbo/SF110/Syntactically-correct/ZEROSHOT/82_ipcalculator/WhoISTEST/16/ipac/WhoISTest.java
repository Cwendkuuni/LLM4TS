import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {

    private WhoIS whoIS;

    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }

    @Test
    public void testStartQueryWithEmptyQuery() {
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // Assert that an error message is displayed
        // Assert the error message content
    }

    @Test
    public void testStartQueryWithValidQuery() {
        whoIS.queryTextField.setText("examplequery");
        whoIS.startQuery();
        // Assert that the query is sent to the server
        // Assert the output is handled correctly
    }

    @Test
    public void testStartQueryWithUnknownHostException() {
        whoIS.queryTextField.setText("examplequery");
        whoIS.WHOIS_ADDRESS = "invalidaddress";
        whoIS.startQuery();
        // Assert that an error message is displayed
        // Assert the error message content
    }

    @Test
    public void testStartQueryWithIOException() {
        whoIS.queryTextField.setText("examplequery");
        whoIS.WHOIS_ADDRESS = "validaddress";
        whoIS.WHOIS_PORT = 9999; // Invalid port
        whoIS.startQuery();
        // Assert that an error message is displayed
        // Assert the error message content
    }

    @Test
    public void testGetQueryWithNullTextField() {
        whoIS.queryTextField = null;
        String query = whoIS.getQuery();
        Assert.assertEquals("", query);
    }

    @Test
    public void testGetQueryWithEmptyTextField() {
        whoIS.queryTextField.setText("");
        String query = whoIS.getQuery();
        Assert.assertEquals("", query);
    }

    @Test
    public void testGetQueryWithValidTextField() {
        whoIS.queryTextField.setText("examplequery");
        String query = whoIS.getQuery();
        Assert.assertEquals("examplequery", query);
    }

    @Test
    public void testReset() {
        whoIS.queryTextField.setText("examplequery");
        whoIS.inverseQueries.setSelectedIndex(1);
        whoIS.miscList.setSelectedIndex(1);

        whoIS.reset();

        Assert.assertEquals("", whoIS.queryTextField.getText());
        Assert.assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        Assert.assertEquals(0, whoIS.miscList.getSelectedIndex());
    }

}