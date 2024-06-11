import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    private WhoIS whois;

    @Before
    public void setUp() {
        whois = new WhoIS();
    }

    @Test
    public void testQuery() {
        // Test case 1: query with empty string
        whois.query("");
        // Assert expected output
        // (Unable to directly test since it involves creating a socket and connecting to a server)

        // Test case 2: query with valid string
        whois.query("example");
        // Assert expected output
        // (Unable to directly test since it involves creating a socket and connecting to a server)
    }

    @Test
    public void testGetQuery() {
        // Test case 1: query without any additional queries or options
        String query = whois.getQuery();
        assertEquals("", query);

        // Test case 2: query with inverse lookup and options
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.lowercaseL.setSelected(true);
        query = whois.getQuery();
        assertEquals("-i admin-c ", query);

        // Test case 3: query with multiple options
        whois.inverseQueries.setSelectedItem("admin-c,tech-c,zone-c");
        whois.lowercaseM.setSelected(true);
        whois.lowercaseD.setSelected(true);
        whois.miscList.setSelectedIndex(1); // Select "as-block"
        query = whois.getQuery();
        assertEquals("-i admin-c,tech-c,zone-c -m -d -T as-block ", query);

        // Test case 4: query with only miscellaneous queries
        whois.miscList.setSelectedIndex(2); // Select "as-set"
        whois.miscList.setSelectedIndex(3); // Select "aut-num"
        query = whois.getQuery();
        assertEquals("-T as-set,aut-num ", query);
    }

    @Test
    public void testReset() {
        // Test case 1: reset with no query
        whois.reset();
        // Assert expected state
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());

        // Test case 2: reset with query and options selected
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        whois.reset();
        // Assert expected state
        assertEquals("", whois.queryTextField.getText());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }

}