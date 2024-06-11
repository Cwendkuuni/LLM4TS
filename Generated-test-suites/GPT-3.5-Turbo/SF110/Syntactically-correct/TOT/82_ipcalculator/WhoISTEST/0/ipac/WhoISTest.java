import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testActionPerformed_queryButton() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.actionPerformed(new ActionEvent(whois.queryButton, ActionEvent.ACTION_PERFORMED, null));
        // Add assertions here to verify the expected behavior
    }
    
    @Test
    public void testActionPerformed_resetButton() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.actionPerformed(new ActionEvent(whois.resetButton, ActionEvent.ACTION_PERFORMED, null));
        // Add assertions here to verify the expected behavior
    }
    
    @Test
    public void testStartQuery_noQueryDetected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("");
        whois.startQuery();
        // Add assertions here to verify the expected behavior
    }
    
    @Test
    public void testStartQuery_validQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.startQuery();
        // Add assertions here to verify the expected behavior
    }
    
    @Test
    public void testStartQuery_unknownHostException() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.WHOIS_ADDRESS = "unknownhost";
        whois.startQuery();
        // Add assertions here to verify the expected behavior
    }
    
    @Test
    public void testStartQuery_ioException() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.WHOIS_ADDRESS = "whois.apnic.net";
        whois.WHOIS_PORT = 12345;
        whois.startQuery();
        // Add assertions here to verify the expected behavior
    }
    
    @Test
    public void testGetQuery_inverseQuerySelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.inverseQueries.setSelectedItem("admin-c");
        String query = whois.getQuery();
        assertEquals("-i admin-c example", query);
    }
    
    @Test
    public void testGetQuery_lowerCaseLSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        String query = whois.getQuery();
        assertEquals("-l example", query);
    }
    
    @Test
    public void testGetQuery_upperCaseLSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.uppercaseL.setSelected(true);
        String query = whois.getQuery();
        assertEquals("-L example", query);
    }
    
    @Test
    public void testGetQuery_lowerCaseMSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseM.setSelected(true);
        String query = whois.getQuery();
        assertEquals("-m example", query);
    }
    
    @Test
    public void testGetQuery_upperCaseMSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.uppercaseM.setSelected(true);
        String query = whois.getQuery();
        assertEquals("-M example", query);
    }
    
    @Test
    public void testGetQuery_lowerCaseXSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.lowercaseX.setSelected(true);
        String query = whois.getQuery();
        assertEquals("-x example", query);
    }
    
    @Test
    public void testGetQuery_booleanDSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.booleanD = true;
        String query = whois.getQuery();
        assertEquals("-d example", query);
    }
    
    @Test
    public void testGetQuery_miscQueriesSelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.miscList.setSelectedValue("domain", true);
        whois.miscList.setSelectedValue("person", true);
        String query = whois.getQuery();
        assertEquals("-T domain,person example", query);
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.reset();
        // Add assertions here to verify the expected behavior
    }
}