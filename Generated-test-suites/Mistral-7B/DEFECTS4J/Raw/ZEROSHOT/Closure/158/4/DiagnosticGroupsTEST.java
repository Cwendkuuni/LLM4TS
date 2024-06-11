import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerOptions;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupNames = DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES.split(",");

    for (String name : diagnosticGroupNames) {
      DiagnosticGroup group = diagnosticGroups.forName(name);
      assertNotNull(group);
      assertEquals(name, group.getName());
    }
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    CompilerOptions options = new CompilerOptions();

    assertEquals(registeredGroups.size(), DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES.split(",").length);

    for (DiagnosticGroup group : registeredGroups.values()) {
      assertNotNull(group.getName());
      if (group instanceof DiagnosticType[]) {
        DiagnosticType[] types = (DiagnosticType[]) group;
        for (DiagnosticType type : types) {
          assertNotNull(type.getName());
        }
      } else if (group instanceof DiagnosticGroup[]) {
        DiagnosticGroup[] subGroups = (DiagnosticGroup[]) group;
        for (DiagnosticGroup subGroup : subGroups) {
          assertNotNull(subGroup.getName());
          testGetRegisteredGroupsRecursive(subGroup, options);
        }
      }
    }
  }

  private void testGetRegisteredGroupsRecursive(DiagnosticGroup group, CompilerOptions options) {
    assertNotNull(group.getName());

    if (group instanceof DiagnosticType[]) {
      DiagnosticType[] types = (DiagnosticType[]) group;
      for (DiagnosticType type : types) {
        assertNotNull(type.getName());
        options.setWarningLevel(type, CheckerFactory.getCheckLevel(CheckerFactory.WarningLevel.WARNING));
      }
    } else if (group instanceof DiagnosticGroup[]) {
      DiagnosticGroup[] subGroups = (DiagnosticGroup[]) group;
      for (DiagnosticGroup subGroup : subGroups) {
        testGetRegisteredGroupsRecursive(subGroup, options);
      }
    }
  }
}