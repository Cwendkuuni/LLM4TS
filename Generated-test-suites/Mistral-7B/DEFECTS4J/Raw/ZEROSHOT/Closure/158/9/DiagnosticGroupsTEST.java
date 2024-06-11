import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    assertEquals(22, registeredGroups.size());
    assertNotNull(registeredGroups.get("accessControls"));
    assertNotNull(registeredGroups.get("ambiguousFunctionDecl"));
    // Add other assertions for all registered groups
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    assertNotNull(accessControls);
    assertEquals("accessControls", accessControls.getName());

    // Add other assertions for all registered groups
  }

  @Test
  public void testRegisterGroupWithDiagnosticTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup constantProperty = diagnosticGroups.registerGroup("constantProperty",
        ConstantPropertyCheck.CONSTANT_PROPERTY_REASSIGNED_VALUE);

    assertNotNull(constantProperty);
    assertEquals("constantProperty", constantProperty.getName());
    assertFalse(constantProperty.isGroup());
    assertEquals(1, constantProperty.getDiagnosticTypes().size());
    assertTrue(constantProperty.getDiagnosticTypes().contains(ConstantPropertyCheck.CONSTANT_PROPERTY_REASSIGNED_VALUE));

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertNotNull(registeredGroups.get("constantProperty"));
  }

  @Test
  public void testRegisterGroupWithDiagnosticGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    DiagnosticGroup accessControls = diagnosticGroups.registerGroup("accessControls");
    DiagnosticGroup deprecated = new DiagnosticGroup("deprecated");
    accessControls.addSubGroup(deprecated);

    assertNotNull(accessControls);
    assertEquals("accessControls", accessControls.getName());
    assertTrue(accessControls.isGroup());
    assertEquals(1, accessControls.getSubGroups().size());
    assertSame(deprecated, accessControls.getSubGroups().get(0));

    DiagnosticGroup deprecatedGroup = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecatedGroup);
    assertEquals("deprecated", deprecatedGroup.getName());
    assertFalse(deprecatedGroup.isGroup());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithDuplicateName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();

    diagnosticGroups.registerGroup("accessControls");
    diagnosticGroups.registerGroup("accessControls");
  }
}