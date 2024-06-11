import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups);
    assertEquals(DiagnosticGroups.registerGroup("globalThis", CheckGlobalThis.GLOBAL_THIS),
        registeredGroups.get("globalThis"));
    assertEquals(DiagnosticGroups.registerGroup("deprecated",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON),
        registeredGroups.get("deprecated"));
    // Add test cases for other registered groups
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertEquals(DiagnosticGroups.registerGroup("globalThis", CheckGlobalThis.GLOBAL_THIS),
        diagnosticGroups.forName("globalThis"));
    assertEquals(DiagnosticGroups.registerGroup("deprecated",
        CheckAccessControls.DEPRECATED_NAME,
        CheckAccessControls.DEPRECATED_NAME_REASON,
        CheckAccessControls.DEPRECATED_PROP,
        CheckAccessControls.DEPRECATED_PROP_REASON,
        CheckAccessControls.DEPRECATED_CLASS,
        CheckAccessControls.DEPRECATED_CLASS_REASON),
        diagnosticGroups.forName("deprecated"));
    // Add test cases for other diagnostic group names
  }

  // Add test cases for other public methods in the DiagnosticGroups class

  

  // Add additional test cases for edge cases and exceptions

  
}