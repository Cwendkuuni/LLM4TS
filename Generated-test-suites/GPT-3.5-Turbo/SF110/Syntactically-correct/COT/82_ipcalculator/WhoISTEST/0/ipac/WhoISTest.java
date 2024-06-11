import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {
    
    @Test
    public void testStartQuery_withValidQueryLength_ShouldNotThrowError() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.startQuery();
    }
    
    @Test(expected = NullPointerException.class)
    public void testStartQuery_withNullQuery_ShouldThrowNullPointerException() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText(null);
        whois.startQuery();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testStartQuery_withEmptyQuery_ShouldThrowIllegalArgumentException() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("");
        whois.startQuery();
    }
    
    @Test
    public void testReset_withValidInput_ShouldResetFields() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
    
    @Test
    public void testGetQuery_withValidInput_ShouldReturnCorrectQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.lowercaseL.setSelected(true);
        assertEquals("-l example query", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_withAllLowerCaseLSelected_ShouldReturnCorrectQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.lowercaseL.setSelected(true);
        whois.uppercaseL.setSelected(false);
        whois.lowercaseM.setSelected(false);
        whois.uppercaseM.setSelected(false);
        whois.lowercaseX.setSelected(false);
        assertEquals("-l example query", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_withAllUpperCaseLSelected_ShouldReturnCorrectQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.lowercaseL.setSelected(false);
        whois.uppercaseL.setSelected(true);
        whois.lowercaseM.setSelected(false);
        whois.uppercaseM.setSelected(false);
        whois.lowercaseX.setSelected(false);
        assertEquals("-L example query", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_withAllLowerCaseMSelected_ShouldReturnCorrectQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.lowercaseL.setSelected(false);
        whois.uppercaseL.setSelected(false);
        whois.lowercaseM.setSelected(true);
        whois.uppercaseM.setSelected(false);
        whois.lowercaseX.setSelected(false);
        assertEquals("-m example query", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_withAllUpperCaseMSelected_ShouldReturnCorrectQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.lowercaseL.setSelected(false);
        whois.uppercaseL.setSelected(false);
        whois.lowercaseM.setSelected(false);
        whois.uppercaseM.setSelected(true);
        whois.lowercaseX.setSelected(false);
        assertEquals("-M example query", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_withAllLowerCaseXSelected_ShouldReturnCorrectQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.lowercaseL.setSelected(false);
        whois.uppercaseL.setSelected(false);
        whois.lowercaseM.setSelected(false);
        whois.uppercaseM.setSelected(false);
        whois.lowercaseX.setSelected(true);
        assertEquals("-x example query", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_withLowerCaseDSelected_ShouldReturnCorrectQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.lowercaseD.setSelected(true);
        assertTrue(whois.booleanD);
        assertEquals("-d example query", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_withInverseQuerySelected_ShouldReturnCorrectQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.inverseQueries.setSelectedItem("admin-c");
        assertEquals("-i admin-c example query", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_withMiscQueriesSelected_ShouldReturnCorrectQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example query");
        whois.miscList.setSelectedIndices(new int[] {1, 2, 3});
        assertEquals("-T as-block,as-set,aut-num example query", whois.getQuery());
    }
}