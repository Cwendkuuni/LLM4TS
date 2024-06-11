
import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {
  
  // Test the constructor
  @Test
  public void testConstructor() {
    WhoIS whoIS = new WhoIS();
    assertNotNull(whoIS);
  }
  
  // Test the actionPerformed method when the source is the queryButton
  @Test
  public void testActionPerformed_QueryButton() {
    WhoIS whoIS = new WhoIS();
    whoIS.queryTextField.setText("example");
    
    // Test the case where the query is not empty
    whoIS.actionPerformed(new ActionEvent(whoIS.queryButton, ActionEvent.ACTION_PERFORMED, ""));
    
    // Test the case where the query is empty
    whoIS.queryTextField.setText("");
    whoIS.actionPerformed(new ActionEvent(whoIS.queryButton, ActionEvent.ACTION_PERFORMED, ""));
    
    // Test the case where the query is null
    whoIS.queryTextField.setText(null);
    whoIS.actionPerformed(new ActionEvent(whoIS.queryButton, ActionEvent.ACTION_PERFORMED, ""));
  }
  
  // Test the actionPerformed method when the source is the resetButton
  @Test
  public void testActionPerformed_ResetButton() {
    WhoIS whoIS = new WhoIS();
    
    // Test the case where the source is the resetButton
    whoIS.actionPerformed(new ActionEvent(whoIS.resetButton, ActionEvent.ACTION_PERFORMED, ""));
  }
  
  // Test the displayError method
  @Test
  public void testDisplayError() {
    WhoIS whoIS = new WhoIS();
    
    // Test when displaying an error message
    whoIS.displayError("Error Message");
  }
  
  // Test the query method
  @Test
  public void testQuery() {
    // Test when the WHOIS_ADDRESS is valid and the query is valid
    WhoIS whoIS = new WhoIS();
    whoIS.query("example");
    
    // Test when the WHOIS_ADDRESS is invalid
    whoIS.WHOIS_ADDRESS = "invalid address";
    whoIS.query("example");
    
    // Test when an IOException occurs
    whoIS.WHOIS_ADDRESS = "whois.apnic.net";
    whoIS.WHOIS_PORT = -1;
    whoIS.query("example");
  }
  
  // Test the getQuery method
  @Test
  public void testGetQuery() {
    WhoIS whoIS = new WhoIS();
    
    // Test when the inverseQueries is "None"
    whoIS.inverseQueries.setSelectedIndex(0);
    whoIS.getQuery();
    
    // Test when the inverseQueries is not "None"
    whoIS.inverseQueries.setSelectedItem("admin-c");
    whoIS.getQuery();
    
    // Test when lowercaseL is selected
    whoIS.lowercaseL.setSelected(true);
    whoIS.getQuery();
    
    // Test when uppercaseL is selected
    whoIS.lowercaseL.setSelected(false);
    whoIS.uppercaseL.setSelected(true);
    whoIS.getQuery();
    
    // Test when lowercaseM is selected
    whoIS.uppercaseL.setSelected(false);
    whoIS.lowercaseM.setSelected(true);
    whoIS.getQuery();
    
    // Test when uppercaseM is selected
    whoIS.lowercaseM.setSelected(false);
    whoIS.uppercaseM.setSelected(true);
    whoIS.getQuery();
    
    // Test when lowercaseX is selected
    whoIS.uppercaseM.setSelected(false);
    whoIS.lowercaseX.setSelected(true);
    whoIS.getQuery();
    
    // Test when lowercaseD is selected
    whoIS.lowercaseX.setSelected(false);
    whoIS.lowercaseD.setSelected(true);
    whoIS.getQuery();
    
    // Test when miscList has selected values
    Object[] selectedValues = {"as-block", "domain"};
    whoIS.miscList.setSelectedValues(selectedValues);
    whoIS.getQuery();
    
    // Test when miscList has no selected values
    selectedValues = null;
    whoIS.miscList.setSelectedValues(selectedValues);
    whoIS.getQuery();
  }
  
  // Test the reset method
  @Test
  public void testReset() {
    WhoIS whoIS = new WhoIS();
    
    // Test the reset method
    whoIS.reset();
  }
  
}
