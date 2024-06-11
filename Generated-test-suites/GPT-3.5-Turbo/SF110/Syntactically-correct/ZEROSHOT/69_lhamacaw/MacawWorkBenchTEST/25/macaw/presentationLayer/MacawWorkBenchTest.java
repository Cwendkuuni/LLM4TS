import org.junit.*;
import static org.junit.Assert.*;

public class MacawWorkBenchTest {
  
  @Test
  public void testMacawWorkBenchConstructor() {
    SessionProperties sessionProperties = new SessionProperties();
    MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
    
    assertNotNull(workBench);
    assertEquals(sessionProperties, workBench.sessionProperties);
  }
  
  @Test
  public void testShow() {
    SessionProperties sessionProperties = new SessionProperties();
    MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
    
    // Test that dialog is set to visible
    workBench.show();
    assertTrue(workBench.dialog.isVisible());
  }
  
  @Test
  public void testEditConstants() {
    SessionProperties sessionProperties = new SessionProperties();
    MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
    
    // Invoke editConstants method and check for expected behavior
    workBench.editConstants();
    // Assert expected behavior
  }
  
  @Test
  public void testEditVariables() {
    SessionProperties sessionProperties = new SessionProperties();
    MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
    
    // Invoke editVariables method and check for expected behavior
    workBench.editVariables();
    // Assert expected behavior
  }
  
  @Test
  public void testExportVariableData() {
    SessionProperties sessionProperties = new SessionProperties();
    MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
    
    // Invoke exportVariableData method and check for expected behavior
    workBench.exportVariableData();
    // Assert expected behavior
  }
  
  @Test
  public void testExit() {
    SessionProperties sessionProperties = new SessionProperties();
    MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
    
    // Invoke exit method and check for expected behavior
    workBench.exit();
    // Assert expected behavior
  }
  
  @Test
  public void testActionPerformed() {
    SessionProperties sessionProperties = new SessionProperties();
    MacawWorkBench workBench = new MacawWorkBench(sessionProperties);
    
    // Test that calling actionPerformed with editConstants button triggers editConstants method
    workBench.actionPerformed(new ActionEvent(workBench.editConstants, ActionEvent.ACTION_PERFORMED, ""));
    // Assert expected behavior
    
    // Test that calling actionPerformed with editVariables button triggers editVariables method
    workBench.actionPerformed(new ActionEvent(workBench.editVariables, ActionEvent.ACTION_PERFORMED, ""));
    // Assert expected behavior
    
    // Test that calling actionPerformed with exportVariableData button triggers exportVariableData method
    workBench.actionPerformed(new ActionEvent(workBench.exportVariableData, ActionEvent.ACTION_PERFORMED, ""));
    // Assert expected behavior
    
    // Test that calling actionPerformed with any other button triggers exit method
    workBench.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, ""));
    // Assert expected behavior
  }

}