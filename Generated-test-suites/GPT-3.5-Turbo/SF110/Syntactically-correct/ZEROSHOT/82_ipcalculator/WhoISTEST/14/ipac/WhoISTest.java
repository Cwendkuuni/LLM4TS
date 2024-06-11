import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testQueryTextField() {
        WhoIS whois = new WhoIS();
        assertEquals("", whois.queryTextField.getText());
        
        whois.queryTextField.setText("example");
        assertEquals("example", whois.queryTextField.getText());
    }
    
    @Test
    public void testLowerCaseL() {
        WhoIS whois = new WhoIS();
        assertFalse(whois.lowercaseL.isSelected());
        
        whois.lowercaseL.setSelected(true);
        assertTrue(whois.lowercaseL.isSelected());
    }
    
    @Test
    public void testUpperCaseL() {
        WhoIS whois = new WhoIS();
        assertFalse(whois.uppercaseL.isSelected());
        
        whois.uppercaseL.setSelected(true);
        assertTrue(whois.uppercaseL.isSelected());
    }
    
    @Test
    public void testLowerCaseM() {
        WhoIS whois = new WhoIS();
        assertFalse(whois.lowercaseM.isSelected());
        
        whois.lowercaseM.setSelected(true);
        assertTrue(whois.lowercaseM.isSelected());
    }
    
    @Test
    public void testUpperCaseM() {
        WhoIS whois = new WhoIS();
        assertFalse(whois.uppercaseM.isSelected());
        
        whois.uppercaseM.setSelected(true);
        assertTrue(whois.uppercaseM.isSelected());
    }
    
    @Test
    public void testLowerCaseX() {
        WhoIS whois = new WhoIS();
        assertFalse(whois.lowercaseX.isSelected());
        
        whois.lowercaseX.setSelected(true);
        assertTrue(whois.lowercaseX.isSelected());
    }
    
    @Test
    public void testLowerCaseD() {
        WhoIS whois = new WhoIS();
        assertFalse(whois.booleanD);
        
        whois.lowercaseD.setSelected(true);
        assertTrue(whois.booleanD);
    }
    
    @Test
    public void testInverseQueries() {
        WhoIS whois = new WhoIS();
        assertEquals("None", whois.inverseQueries.getSelectedItem());
        
        whois.inverseQueries.setSelectedItem("admin-c");
        assertEquals("admin-c", whois.inverseQueries.getSelectedItem());
    }
    
    @Test
    public void testMiscList() {
        WhoIS whois = new WhoIS();
        assertEquals("All", whois.miscList.getSelectedValue());
        
        whois.miscList.setSelectedIndex(1);
        assertEquals("as-block", whois.miscList.getSelectedValue());
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        whois.uppercaseL.setSelected(true);
        whois.lowercaseM.setSelected(true);
        whois.uppercaseM.setSelected(true);
        whois.lowercaseX.setSelected(true);
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.miscList.setSelectedIndex(1);
        
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseL.isSelected());
        assertFalse(whois.uppercaseL.isSelected());
        assertFalse(whois.lowercaseM.isSelected());
        assertFalse(whois.uppercaseM.isSelected());
        assertFalse(whois.lowercaseX.isSelected());
        assertFalse(whois.booleanD);
        assertEquals("None", whois.inverseQueries.getSelectedItem());
        assertEquals("All", whois.miscList.getSelectedValue());
    }
}