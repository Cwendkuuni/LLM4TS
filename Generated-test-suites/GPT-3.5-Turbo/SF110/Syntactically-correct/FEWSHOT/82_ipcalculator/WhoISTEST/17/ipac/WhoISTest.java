package ipac;

import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {
    
    @Test
    public void testQueryTextField() {
        // Create a WhoIS object
        WhoIS whois = new WhoIS();
        
        // Set the query text field
        String query = "example";
        whois.queryTextField.setText(query);
        
        // Check if the query text field value matches
        assertEquals(query, whois.getQuery());
    }
    
    @Test
    public void testLowerCaseL() {
        // Create a WhoIS object
        WhoIS whois = new WhoIS();
        
        // Set the lowercaseL radio button as selected
        whois.lowercaseL.setSelected(true);
        
        // Check if the query has the -l flag
        assertTrue(whois.getQuery().contains("-l"));
    }
    
    @Test
    public void testUpperCaseL() {
        // Create a WhoIS object
        WhoIS whois = new WhoIS();
        
        // Set the uppercaseL radio button as selected
        whois.uppercaseL.setSelected(true);
        
        // Check if the query has the -L flag
        assertTrue(whois.getQuery().contains("-L"));
    }
    
    @Test
    public void testLowerCaseM() {
        // Create a WhoIS object
        WhoIS whois = new WhoIS();
        
        // Set the lowercaseM radio button as selected
        whois.lowercaseM.setSelected(true);
        
        // Check if the query has the -m flag
        assertTrue(whois.getQuery().contains("-m"));
    }
    
    @Test
    public void testUpperCaseM() {
        // Create a WhoIS object
        WhoIS whois = new WhoIS();
        
        // Set the uppercaseM radio button as selected
        whois.uppercaseM.setSelected(true);
        
        // Check if the query has the -M flag
        assertTrue(whois.getQuery().contains("-M"));
    }
    
    @Test
    public void testLowerCaseX() {
        // Create a WhoIS object
        WhoIS whois = new WhoIS();
        
        // Set the lowercaseX radio button as selected
        whois.lowercaseX.setSelected(true);
        
        // Check if the query has the -x flag
        assertTrue(whois.getQuery().contains("-x"));
    }
    
    @Test
    public void testBooleanD() {
        // Create a WhoIS object
        WhoIS whois = new WhoIS();
        
        // Set the booleanD checkbox as selected
        whois.booleanD = true;
        
        // Check if the query has the -d flag
        assertTrue(whois.getQuery().contains("-d"));
    }
    
    @Test
    public void testMiscList() {
        // Create a WhoIS object
        WhoIS whois = new WhoIS();
        
        // Set selected values in the miscList
        whois.miscList.setSelectedIndices(new int[]{1, 3});
        
        // Check if the query contains the selected misc queries
        assertTrue(whois.getQuery().contains("as-set"));
        assertTrue(whois.getQuery().contains("domain"));
    }
}