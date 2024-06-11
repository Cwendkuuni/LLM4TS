import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static com.google.javascript.jscomp.DiagnosticGroups.*; // Import all DiagnosticGroups constants

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class DiagnosticGroupsTest {

  private static ImmutableMap<String, DiagnosticGroup> registeredGroups;

  @BeforeClass
  public static void setup() {
    registeredGroups = DiagnosticGroups.getRegisteredGroups();
  }

  @Test
  public void testRegisterGroupWithNameAndTypes() {
    DiagnosticGroup diagnosticGroup = DiagnosticGroups.registerGroup("testGroup",
        DiagnosticType.ERROR, DiagnosticType.WARNING);
    assertNotNull(diagnosticGroup);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithNullName() {
    DiagnosticGroups.registerGroup(null, DiagnosticType.ERROR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithEmptyName() {
    DiagnosticGroups.registerGroup("", DiagnosticType.ERROR);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithNullTypes() {
    DiagnosticGroups.registerGroup("testGroup", (DiagnosticType[]) null);
  }

  @Test
  public void testGetRegisteredGroups() {
    assertNotNull(registeredGroups);
  }

  @Test
  public void testGetRegisteredGroupByName() {
    DiagnosticGroup diagnosticGroup = registeredGroups.get("ACCESS_CONTROLS");
    assertNotNull(diagnosticGroup);
  }

  @Test(expected = NullPointerException.class)
  public void testGetRegisteredGroupByNameNullName() {
    registeredGroups.get(null);
  }

  @Test
  public void testRegisterGroupWithNullDiagnosticGroup() {
    try {
      DiagnosticGroups.registerGroup(null, DiagnosticType.ERROR);
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRegisterGroupWithEmptyList() {
    DiagnosticGroup diagnosticGroup = new DiagnosticGroup("testGroup",
        ImmutableList.<DiagnosticType>of());
    try {
      DiagnosticGroups.registerGroup("testGroup2", diagnosticGroup);
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }
}