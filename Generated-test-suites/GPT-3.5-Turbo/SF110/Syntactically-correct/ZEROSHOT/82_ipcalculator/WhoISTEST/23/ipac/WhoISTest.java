import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        String expected = "-l example";
        assertEquals(expected, whois.getQuery());
        
        whois.reset();
        whois.queryTextField.setText("example");
        whois.uppercaseL.setSelected(true);
        expected = "-L example";
        assertEquals(expected, whois.getQuery());
        
        whois.reset();
        whois.queryTextField.setText("example");
        whois.lowercaseM.setSelected(true);
        expected = "-m example";
        assertEquals(expected, whois.getQuery());
        
        whois.reset();
        whois.queryTextField.setText("example");
        whois.uppercaseM.setSelected(true);
        expected = "-M example";
        assertEquals(expected, whois.getQuery());
        
        whois.reset();
        whois.queryTextField.setText("example");
        whois.lowercaseX.setSelected(true);
        expected = "-x example";
        assertEquals(expected, whois.getQuery());
        
        whois.reset();
        whois.queryTextField.setText("example");
        whois.booleanD = true;
        expected = "-d example";
        assertEquals(expected, whois.getQuery());
        
        whois.reset();
        whois.queryTextField.setText("example");
        whois.inverseQueries.setSelectedItem("admin-c");
        expected = "-i admin-c example";
        assertEquals(expected, whois.getQuery());
        
        whois.reset();
        whois.queryTextField.setText("example");
        whois.miscList.setSelectedValue("as-set", true);
        expected = "-T as-set example";
        assertEquals(expected, whois.getQuery());
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseL.isSelected());
    }
}