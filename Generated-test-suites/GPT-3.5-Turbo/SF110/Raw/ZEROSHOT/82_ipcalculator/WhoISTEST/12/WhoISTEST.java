import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
  
  private WhoIS whoIS;
  
  @Before
  public void setUp() {
    whoIS = new WhoIS();
  }
  
  @Test
  public void testQueryPanel() {
    assertNotNull(whoIS.queryPanel());
  }
  
  @Test
  public void testAdditionalQueriesPanel() {
    assertNotNull(whoIS.additionalQueriesPanel());
  }
  
  @Test
  public void testIPAddressLookups() {
    assertNotNull(whoIS.IPAddressLookups());
  }
  
  @Test
  public void testMiscellaneousQueries() {
    assertNotNull(whoIS.MiscellaneousQueries());
  }
  
  @Test
  public void testQueryHintsPanel() {
    assertNotNull(whoIS.queryHintsPanel());
  }
  
  @Test
  public void testCreatePopupMenu() {
    whoIS.createPopupMenu();
    assertNotNull(whoIS.popupMenu);
  }
  
  @Test
  public void testActionPerformedQueryButton() {
    // Assume queryButton is clicked
    whoIS.queryTextField.setText("test");
    whoIS.startQuery();
    // Test if query is sent
    // Assert statements for expected results
  }
  
  @Test
  public void testActionPerformedResetButton() {
    // Assume resetButton is clicked
    whoIS.reset();
    // Test if UI elements are reset
    // Assert statements for expected results
  }
  
  @Test
  public void testQueryEmpty() {
    // Assume queryButton is clicked with empty queryTextField
    whoIS.startQuery();
    // Test if error message is displayed
    // Assert statements for expected results
  }
  
  @Test
  public void testQueryValid() {
    // Assume queryButton is clicked with valid queryTextField
    whoIS.queryTextField.setText("example.com");
    whoIS.startQuery();
    // Test if query is sent and correct data is received
    // Assert statements for expected results
  }
  
  @Test
  public void testDisplayError() {
    // Mock JOptionPane.showMessageDialog and test if called with correct parameters
    whoIS.displayError("Test Error Message");
    // Assert statements for expected results
  }
  
  // Include tests for other methods in the class
  
}