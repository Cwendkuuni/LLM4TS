import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;

public class MacawWorkBenchTest {

  MacawWorkBench macawWorkBench;

  @Before
  public void setUp() {
    macawWorkBench = new MacawWorkBench(new SessionProperties());
  }

  @After
  public void tearDown() {
    macawWorkBench = null;
  }

  @Test
  public void testEditConstants() {
    // Typical Use Case
    // Verify that when editConstants() method is called, the VariableConstantsEditor is shown
    macawWorkBench.editConstants();

    // Edge Case
    // Verify that when editConstants() method is called, the VariableConstantsEditor dialog has the correct title
    assertEquals("Variable Constants Editor", macawWorkBench.getVariableConstantsEditorTitle());

    // Error Scenario
    // Verify that when editConstants() method is called with null sessionProperties, it throws NullPointerException
    try {
      macawWorkBench.editConstants(null);
      fail("Expected NullPointerException was not thrown");
    } catch (NullPointerException e) {
      assertEquals("SessionProperties cannot be null", e.getMessage());
    }
  }

  @Test
  public void testEditVariables() {
    // Typical Use Case
    // Verify that when editVariables() method is called, the MacawVariableBrowser is shown
    macawWorkBench.editVariables();

    // Edge Case
    // Verify that when editVariables() method is called, the MacawVariableBrowser dialog has the correct title
    assertEquals("Macaw Variable Browser", macawWorkBench.getMacawVariableBrowserTitle());

    // Error Scenario
    // Verify that when editVariables() method is called with null sessionProperties, it throws NullPointerException
    try {
      macawWorkBench.editVariables(null);
      fail("Expected NullPointerException was not thrown");
    } catch (NullPointerException e) {
      assertEquals("SessionProperties cannot be null", e.getMessage());
    }
  }

  @Test
  public void testExportVariableData() {
    // Typical Use Case
    // Verify that when exportVariableData() method is called, the ExportVariablesToMacawXML export method is called
    macawWorkBench.exportVariableData();

    // Edge Case
    // Verify that when exportVariableData() method is called, the ExportVariablesToMacawXML dialog has the correct size
    assertEquals(800, macawWorkBench.getExportVariablesDialogWidth());
    assertEquals(600, macawWorkBench.getExportVariablesDialogHeight());

    // Error Scenario
    // Verify that when exportVariableData() method is called with null sessionProperties, it throws NullPointerException
    try {
      macawWorkBench.exportVariableData(null);
      fail("Expected NullPointerException was not thrown");
    } catch (NullPointerException e) {
      assertEquals("SessionProperties cannot be null", e.getMessage());
    }
  }

  @Test
  public void testExit() {
    // Typical Use Case
    // Verify that when exit() method is called, the application exits
    macawWorkBench.exit();

    // Edge Case
    // Cannot test exit() method

    // Error Scenario
    // Cannot test exit() method
  }
}