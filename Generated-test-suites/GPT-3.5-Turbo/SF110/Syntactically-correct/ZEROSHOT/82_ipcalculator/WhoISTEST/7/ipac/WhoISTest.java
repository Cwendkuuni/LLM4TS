import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        
        // Test empty query
        whois.query("");
        // Check if error message is displayed
        
        // Test valid query
        whois.query("example query");
        // Check if result is displayed properly
        
        // Test unknown host query
        whois.query("unknown host query");
        // Check if error message is displayed
        
        // Test connection error query
        whois.query("connection error query");
        // Check if error message is displayed
    }
    
    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        
        // Test default query without any options
        String query = whois.getQuery();
        assertEquals("", query);
        
        // Test query with inverse lookup
        whois.lowercaseX.setSelected(true);
        whois.booleanD = true;
        whois.inverseQueries.setSelectedItem("admin-c");
        query = whois.getQuery();
        assertEquals("-x -d -i admin-c ", query);
        
        // Test query with IP address lookup options
        whois.resetRadioButton.setSelected(true);
        whois.lowercaseL.setSelected(true);
        query = whois.getQuery();
        assertEquals("-i None -l ", query);
        
        whois.uppercaseL.setSelected(true);
        query = whois.getQuery();
        assertEquals("-i None -L ", query);
        
        whois.lowercaseM.setSelected(true);
        query = whois.getQuery();
        assertEquals("-i None -m ", query);
        
        whois.uppercaseM.setSelected(true);
        query = whois.getQuery();
        assertEquals("-i None -M ", query);
        
        // Test query with miscellaneous queries
        whois.reset();
        whois.miscList.setSelectedIndices(new int[]{1, 3});
        query = whois.getQuery();
        assertEquals("-T as-block,key-cert ", query);
        
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        
        // Set initial values
        whois.queryTextField.setText("example query");
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.miscList.setSelectedIndices(new int[]{1, 3});
        
        // Reset values
        whois.reset();
        
        // Check if values are reset properly
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }

    @Test
    public void testActionPerformed() {
        WhoIS whois = new WhoIS();

        // Test query button
        whois.queryTextField.setText("example query");
        whois.actionPerformed(new ActionEvent(whois.queryButton, ActionEvent.ACTION_PERFORMED, ""));
        // Check if query is started with correct input
        
        // Test reset button
        // Set initial values
        whois.queryTextField.setText("example query");
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.miscList.setSelectedIndices(new int[]{1, 3});
        whois.actionPerformed(new ActionEvent(whois.resetButton, ActionEvent.ACTION_PERFORMED, ""));
        // Check if values are reset properly
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}