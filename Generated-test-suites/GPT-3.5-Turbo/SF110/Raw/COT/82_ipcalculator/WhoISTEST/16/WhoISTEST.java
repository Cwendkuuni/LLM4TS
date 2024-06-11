import org.junit.Test;

public class WhoISTest {

    WhoIS whois = new WhoIS();
    
    @Test
    public void testQuery() {
        // Test normal query
        String query = "test query";
        whois.query(query);
        
        // Test empty query
        query = "";
        whois.query(query);
        
        // Test query with special characters
        query = "!@#$%^&*()";
        whois.query(query);
    }
    
    @Test
    public void testStartQuery() {
        // Test startQuery with valid query
        String query = "valid query";
        whois.queryTextField.setText(query);
        whois.startQuery();
        
        // Test startQuery with empty query
        query = "";
        whois.queryTextField.setText(query);
        whois.startQuery();
    }
    
    @Test
    public void testDisplayError() {
        // Test display error message
        String error = "Error message";
        whois.displayError(error);
    }
    
    @Test
    public void testGetQuery() {
        // Test basic getQuery method
        
        // Test with queryTextField empty
        String expectedResult = "";
        String actualResult = whois.getQuery();
        assertEquals(expectedResult, actualResult);
        
        // Test with queryTextField not empty and no additional options selected
        String query = "test query";
        whois.queryTextField.setText(query);
        expectedResult = "test query";
        actualResult = whois.getQuery();
        assertEquals(expectedResult, actualResult);
        
        // Test with inverseQueries selected
        whois.inverseQueries.setSelectedIndex(1);
        expectedResult = "-i admin-c test query";
        actualResult = whois.getQuery();
        assertEquals(expectedResult, actualResult);
        
        // Test with lowercaseL selected
        whois.inverseQueries.setSelectedIndex(0);
        whois.lowercaseL.setSelected(true);
        expectedResult = "-l test query";
        actualResult = whois.getQuery();
        assertEquals(expectedResult, actualResult);
        
        // Test with uppercaseL selected
        whois.lowercaseL.setSelected(false);
        whois.uppercaseL.setSelected(true);
        expectedResult = "-L test query";
        actualResult = whois.getQuery();
        assertEquals(expectedResult, actualResult);
        
        // Test with lowercaseM selected
        whois.uppercaseL.setSelected(false);
        whois.lowercaseM.setSelected(true);
        expectedResult = "-m test query";
        actualResult = whois.getQuery();
        assertEquals(expectedResult, actualResult);
        
        // Test with uppercaseM selected
        whois.lowercaseM.setSelected(false);
        whois.uppercaseM.setSelected(true);
        expectedResult = "-M test query";
        actualResult = whois.getQuery();
        assertEquals(expectedResult, actualResult);
        
        // Test with lowercaseX selected
        whois.uppercaseM.setSelected(false);
        whois.lowercaseX.setSelected(true);
        expectedResult = "-x test query";
        actualResult = whois.getQuery();
        assertEquals(expectedResult, actualResult);
        
        // Test with lowercaseD selected
        whois.lowercaseX.setSelected(false);
        whois.lowercaseD.setSelected(true);
        expectedResult = "-d test query";
        actualResult = whois.getQuery();
        assertEquals(expectedResult, actualResult);
        
        // Test with miscList selected
        whois.lowercaseD.setSelected(false);
        whois.miscList.setSelectedIndex(1);
        expectedResult = "-T as-block test query";
        actualResult = whois.getQuery();
        assertEquals(expectedResult, actualResult);
    }
    
    @Test
    public void testReset() {
        // Test reset method
        whois.queryTextField.setText("test query");
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedIndex(2);
        whois.miscList.setSelectedIndex(3);
        
        whois.reset();
        
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}