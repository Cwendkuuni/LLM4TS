import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
    }
    
    @Test
    public void testActionPerformed_queryButton() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.actionPerformed(new ActionEvent(whois.queryButton, ActionEvent.ACTION_PERFORMED, ""));
        // Add assertion here
    }
    
    @Test
    public void testActionPerformed_resetButton() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.actionPerformed(new ActionEvent(whois.resetButton, ActionEvent.ACTION_PERFORMED, ""));
        // Add assertion here
    }
    
    @Test
    public void testGetQuery_inverseQuerySelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.inverseQueries.setSelectedIndex(1);
        assertEquals("-i admin-c example", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_lowercaseLSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        assertEquals("-l example", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_uppercaseLSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.uppercaseL.setSelected(true);
        assertEquals("-L example", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_lowercaseMSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseM.setSelected(true);
        assertEquals("-m example", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_uppercaseMSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.uppercaseM.setSelected(true);
        assertEquals("-M example", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_lowercaseXSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseX.setSelected(true);
        assertEquals("-x example", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_booleanDSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseD.setSelected(true);
        assertEquals("-d example", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_miscListSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.miscList.setSelectedIndex(1);
        assertEquals("-T as-block,example", whois.getQuery());
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        whois.miscList.setSelectedIndex(1);
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseL.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}