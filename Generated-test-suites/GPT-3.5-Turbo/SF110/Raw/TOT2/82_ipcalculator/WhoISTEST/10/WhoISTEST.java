import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.awt.event.ActionEvent;

public class WhoISTest {

    @Test
    public void testStartQueryNoQuery() {
        WhoIS whoIs = new WhoIS();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        whoIs.startQuery();
        assertEquals("No Query Detected\n", outContent.toString());
    }

    @Test
    public void testQueryUnknownHostException() {
        WhoIS whoIs = new WhoIS();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        whoIs.WHOIS_ADDRESS = "unknownhost";
        whoIs.query("testquery");
        assertEquals("Unknown Whois host: unknownhost\n", outContent.toString());
    }

    @Test
    public void testQueryIOException() {
        WhoIS whoIs = new WhoIS();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        whoIs.WHOIS_ADDRESS = "whois.apnic.net";
        whoIs.WHOIS_PORT = 1234; // Assuming an incorrect port
        whoIs.query("testquery");
        assertEquals("Whois server could not be contacted.\nPlease check your internet connection.\n", outContent.toString());
    }

    @Test
    public void testGetQueryWithInverseQuerySet() {
        WhoIS whoIs = new WhoIS();
        whoIs.queryTextField.setText("test");
        whoIs.inverseQueries.setSelectedItem("admin-c");
        whoIs.lowercaseL.setSelected(true);
        whoIs.booleanD = true;
        whoIs.miscList.setSelectedIndex(1); // Selecting admin-c
        String expectedQuery = "-i admin-c test -l -d -T admin-c,";
        assertEquals(expectedQuery, whoIs.getQuery());
    }

    @Test
    public void testReset() {
        WhoIS whoIs = new WhoIS();
        whoIs.queryTextField.setText("test");
        whoIs.reset();
        assertEquals("", whoIs.queryTextField.getText());
        assertTrue(whoIs.resetRadioButton.isSelected());
        assertFalse(whoIs.lowercaseD.isSelected());
        assertEquals(0, whoIs.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIs.miscList.getSelectedIndex());
    }

}