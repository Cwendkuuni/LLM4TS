
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WhoISTest {

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        
        String query = "example";
        
        whois.query(query);
        
        // assert statements
        // ...
    }
    
    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        
        String expected = "-m example";
        whois.lowercaseM.setSelected(true);
        whois.queryTextField.setText("example");
        
        String actual = whois.getQuery();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        
        String query = "example";
        
        whois.queryTextField.setText(query);
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedIndex(1);
        whois.miscList.setSelectedIndex(2);
        
        whois.reset();
        
        assertTrue(whois.queryTextField.getText().isEmpty());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}
