import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {
    
    @Test
    public void testQueryTextField() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois.queryTextField);
        
        // Test setting query text
        whois.queryTextField.setText("example query");
        assertEquals("example query", whois.getQuery());
    }
    
    @Test
    public void testActionPerformed() {
        WhoIS whois = new WhoIS();
        
        // Test query button action
        whois.queryTextField.setText("example query");
        whois.actionPerformed(new ActionEvent(whois.queryButton, ActionEvent.ACTION_PERFORMED, ""));
        // Assert the query is sent
        // Add assertions for the expected behavior
        
        // Test reset button action
        whois.actionPerformed(new ActionEvent(whois.resetButton, ActionEvent.ACTION_PERFORMED, ""));
        // Assert all fields are reset
        // Add assertions for the expected behavior
    }
    
    // Add more test cases for other public methods
    
    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        
        // Test start query with empty query
        whois.queryTextField.setText("");
        whois.startQuery();
        // Assert that an error is displayed for empty query
        // Add assertions for the expected behavior
        
        // Test start query with valid query
        whois.queryTextField.setText("example query");
        whois.startQuery();
        // Assert that the query is executed
        // Add assertions for the expected behavior
    }
    
    // Add test cases for other methods testing edge cases and exception scenarios
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedIndex(1);
        whois.miscList.setSelectedIndex(1);
        
        whois.reset();
        // Assert all fields are reset to their default values
        // Add assertions for the expected behavior
    }
}