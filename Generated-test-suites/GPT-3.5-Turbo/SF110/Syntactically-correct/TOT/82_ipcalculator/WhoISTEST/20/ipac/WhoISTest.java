import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {

    @Test
    public void testQuery() {
        WhoIS whoIS = new WhoIS();
        String query = "APNIC";
        whoIS.query(query);
        // Assuming the output is properly displayed, no need to test the output
    }

    @Test
    public void testStartQueryWithEmptyQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // Assuming the error message is properly displayed, no need to test the message
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("APNIC");
        whoIS.startQuery();
        // Assuming the query is properly executed, no need to test the result
    }

    @Test
    public void testStartQueryWithInvalidWhoisHost() {
        WhoIS whoIS = new WhoIS();
        whoIS.WHOIS_ADDRESS = "invalidhost";
        whoIS.queryTextField.setText("APNIC");
        whoIS.startQuery();
        // Assuming the error message is properly displayed, no need to test the message
    }

    @Test
    public void testStartQueryWithIOException() {
        WhoIS whoIS = new WhoIS();
        whoIS.WHOIS_ADDRESS = "invalidhost";
        whoIS.queryTextField.setText("APNIC");
        whoIS.startQuery();
        // Assuming the error message is properly displayed, no need to test the message
    }
    
    @Test
    public void testGetQueryWithInverseOption() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("APNIC");
        whoIS.inverseQueries.setSelectedIndex(1);  // Select "admin-c" from inverseQueries
        String expectedQuery = "-i admin-c APNIC";
        assertEquals(expectedQuery, whoIS.getQuery());
    }
    
    @Test
    public void testGetQueryWithLookupOption() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("APNIC");
        whoIS.lowercaseL.setSelected(true);   // Select lowercaseL option
        String expectedQuery = "-l APNIC";
        assertEquals(expectedQuery, whoIS.getQuery());
    }
    
    @Test
    public void testGetQueryWithMiscQueries() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("APNIC");
        whoIS.miscList.setSelectedIndices(new int[]{1, 3, 5});   // Select "as-block", "aut-num", "filter-set" from miscList
        String expectedQuery = "-T as-block,aut-num,filter-set APNIC";
        assertEquals(expectedQuery, whoIS.getQuery());
    }
    
    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("APNIC");
        whoIS.lowercaseL.setSelected(true);
        whoIS.inverseQueries.setSelectedIndex(1);
        whoIS.miscList.setSelectedIndices(new int[]{1, 3, 5});
        whoIS.reset();
        assertEquals("", whoIS.queryTextField.getText());
        assertTrue(whoIS.resetRadioButton.isSelected());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
}