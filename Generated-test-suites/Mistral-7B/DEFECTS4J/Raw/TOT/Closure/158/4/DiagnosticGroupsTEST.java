import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DiagnosticGroupsTest {

  @org.junit.Test
  public void testRegisterGroup() {
    String name = "newGroup";
    DiagnosticType type1 = new DiagnosticType("TestType1");
    DiagnosticType type2 = new DiagnosticType("TestType2");

    DiagnosticGroup group = DiagnosticGroups.registerGroup(name, type1, type2);

    DiagnosticGroup actualGroup = DiagnosticGroups.forName(name);
    DiagnosticGroup expectedGroup = new DiagnosticGroup(name, Arrays.asList(type1, type2));

    assertEquals(expectedGroup, actualGroup);
  }

  @org.junit.Test
  public void testGetRegisteredGroups() {
    String groupName1 = "group1";
    String groupName2 = "group2";

    DiagnosticGroup group1 = new DiagnosticGroup(groupName1);
    DiagnosticGroup group2 = new DiagnosticGroup(groupName2);

    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    diagnosticGroups.registerGroup(groupName1, group1);
    diagnosticGroups.registerGroup(groupName2, group2);

    Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();

    List<String> groupNames = Collections.list(groups.keys());

    assertEquals(2, groupNames.size());
    assertEquals(groupName1, groupNames.get(0));
    assertEquals(groupName2, groupNames.get(1));

    DiagnosticGroup actualGroup1 = groups.get(groupName1);
    DiagnosticGroup expectedGroup1 = group1;

    assertEquals(expectedGroup1, actualGroup1);

    DiagnosticGroup actualGroup2 = groups.get(groupName2);
    DiagnosticGroup expectedGroup2 = group2;

    assertEquals(expectedGroup2, actualGroup2);
  }

  @org.junit.Test
  public void testForName() {
    String groupName = "GLOBAL_THIS";

    DiagnosticGroup actualGroup = DiagnosticGroups.forName(groupName);

    DiagnosticGroup expectedGroup = DiagnosticGroups.GLOBAL_THIS;

    assertEquals(expectedGroup, actualGroup);
  }
}