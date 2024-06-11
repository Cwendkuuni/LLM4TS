package ipac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
    
    private WhoIS whois;
    
    @Before
    public void setup() {
        whois = new WhoIS();
    }
    
    @Test
    public void testStartQuery() {
        whois.queryTextField.setText("query");
        whois.startQuery();
        // assertion to check if query sent successfully
    }
    
    @Test
    public void testStartQueryWithEmptyQuery() {
        whois.queryTextField.setText("");
        whois.startQuery();
        // assertion to check if error message displayed
    }
    
    @Test
    public void testGetQuery() {
        whois.queryTextField.setText("query");
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseL.setSelected(true);
        whois.uppercaseL.setSelected(false);
        whois.lowercaseM.setSelected(false);
        whois.uppercaseM.setSelected(false);
        whois.lowercaseX.setSelected(false);
        whois.booleanD = false;
        whois.miscList.setSelectedIndex(0);
        String expectedQuery = "-i admin-c query -l";
        assertEquals(expectedQuery, whois.getQuery());
    }
    
    @Test
    public void testGetQueryWithAllSelected() {
        whois.queryTextField.setText("query");
        whois.inverseQueries.setSelectedIndex(0);
        whois.lowercaseL.setSelected(false);
        whois.uppercaseL.setSelected(false);
        whois.lowercaseM.setSelected(false);
        whois.uppercaseM.setSelected(false);
        whois.lowercaseX.setSelected(true);
        whois.booleanD = true;
        whois.miscList.setSelectedIndex(0);
        String expectedQuery = "-x query -d";
        assertEquals(expectedQuery, whois.getQuery());
    }
    
    @Test
    public void testReset() {
        whois.queryTextField.setText("query");
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseL.setSelected(true);
        whois.uppercaseL.setSelected(false);
        whois.lowercaseM.setSelected(false);
        whois.uppercaseM.setSelected(false);
        whois.lowercaseX.setSelected(false);
        whois.booleanD = false;
        whois.miscList.setSelectedIndex(1);
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseL.isSelected());
        assertFalse(whois.uppercaseL.isSelected());
        assertFalse(whois.lowercaseM.isSelected());
        assertFalse(whois.uppercaseM.isSelected());
        assertFalse(whois.lowercaseX.isSelected());
        assertFalse(whois.booleanD);
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}