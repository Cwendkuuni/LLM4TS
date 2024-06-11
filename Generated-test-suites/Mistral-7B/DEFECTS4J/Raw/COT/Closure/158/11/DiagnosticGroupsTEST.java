import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticTypes;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
        DiagnosticTypes.TEST_DIAGNOSTIC);
    assertNotNull(group);
  }

  @Test(expected = NullPointerException.class)
  public void testNullGroup() {
    DiagnosticGroups.registerGroup(null, "testDiagnostic");
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1",
        DiagnosticTypes.TEST_DIAGNOSTIC);
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2",
        DiagnosticTypes.TEST_DIAGNOSTIC);

    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups
        .getRegisteredGroups();

    List<DiagnosticGroup> groups = newArrayList(registeredGroups.values());

    assertNotNull(registeredGroups);
    assertNotNull(groups);
    assertNotNull(groups.get(0));
    assertNotNull(groups.get(1));
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1",
        DiagnosticTypes.TEST_DIAGNOSTIC);
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2",
        DiagnosticTypes.TEST_DIAGNOSTIC);

    DiagnosticGroup foundGroup1 = diagnosticGroups.forName("group1");
    DiagnosticGroup foundGroup2 = diagnosticGroups.forName("group2");

    assertNotNull(foundGroup1);
    assertNotNull(foundGroup2);
  }

  @Test
  public void testForNameNull() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    assertNull(diagnosticGroups.forName(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithDuplicateName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1",
        DiagnosticTypes.TEST_DIAGNOSTIC);
    DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group1",
        DiagnosticTypes.TEST_DIAGNOSTIC);
  }
}