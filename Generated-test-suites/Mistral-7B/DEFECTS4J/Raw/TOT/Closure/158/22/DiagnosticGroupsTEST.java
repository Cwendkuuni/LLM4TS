import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

  @org.junit.Test
  public void testRegisterGroup() {
    String name = "testGroup";
    DiagnosticGroup group = new DiagnosticGroup(name, DiagnosticType.class);
    DiagnosticGroups.registerGroup(name, group);

    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    DiagnosticGroup registeredGroup = registeredGroups.get(name);
    assertEquals(group, registeredGroup);
  }

  @org.junit.Test
  public void testRegisterGroupWithTypes() {
    String name = "testGroupWithTypes";
    DiagnosticType type1 = new DiagnosticType("Type1");
    DiagnosticType type2 = new DiagnosticType("Type2");
    DiagnosticGroup group = new DiagnosticGroup(name, type1, type2);
    DiagnosticGroups.registerGroup(name, group);

    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    DiagnosticGroup registeredGroup = registeredGroups.get(name);
    assertTrue(registeredGroup.containsDiagnosticType(type1));
    assertTrue(registeredGroup.containsDiagnosticType(type2));
  }

  @org.junit.Test
  public void testRegisterGroupWithOtherGroups() {
    String name = "testGroupWithOtherGroups";
    String group1Name = "testGroup1";
    DiagnosticGroup group1 = new DiagnosticGroup(group1Name);
    DiagnosticGroup group = new DiagnosticGroup(name, group1);
    DiagnosticGroups.registerGroup(name, group);

    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    DiagnosticGroup registeredGroup = registeredGroups.get(name);
    assertEquals(group, registeredGroup);
    DiagnosticGroup registeredGroup1 = registeredGroups.get(group1Name);
    assertTrue(registeredGroup.containsGroup(registeredGroup1));
  }

  @org.junit.Test
  public void testGetRegisteredGroups() {
    String name1 = "testGroup1";
    String name2 = "testGroup2";
    DiagnosticType type1 = new DiagnosticType("Type1");
    DiagnosticType type2 = new DiagnosticType("Type2");
    DiagnosticGroup group1 = new DiagnosticGroup(name1, type1);
    DiagnosticGroup group2 = new DiagnosticGroup(name2, type2);

    DiagnosticGroups.registerGroup(name1, group1);
    DiagnosticGroups.registerGroup(name2, group2);

    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
    assertEquals(2, registeredGroups.size());
    assertTrue(registeredGroups.containsKey(name1));
    assertTrue(registeredGroups.containsKey(name2));
  }

  @org.junit.Test
  public void testForName() {
    String name = "testGroup";
    DiagnosticGroup group = new DiagnosticGroup(name);
    DiagnosticGroups.registerGroup(name, group);

    DiagnosticGroup result = DiagnosticGroups.forName(name);
    assertEquals(group, result);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testForNameNull() {
    DiagnosticGroups.forName(null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testForNameUnknownName() {
    DiagnosticGroups.forName("UnknownName");
  }
}