import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticGroups.*;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group = registerGroup("testGroup", new DiagnosticType[] {});
    assertNotNull(group);
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupNullName() {
    DiagnosticGroup.registerGroup(null, new DiagnosticType[] {});
  }

  @Test(expected = NullPointerException.class)
  public void testRegisterGroupNullTypes() {
    DiagnosticGroup.registerGroup("testGroup", null);
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> registeredGroups = getRegisteredGroups();
    assertNotNull(registeredGroups);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    forName(null);
  }

  @Test
  public void testForNameExistingGroup() {
    DiagnosticGroup group = forName("DEPRECATED");
    assertNotNull(group);
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNonExistentGroup() {
    forName("nonExistentGroup");
  }
}