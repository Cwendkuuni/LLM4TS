import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;

import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

public class DiagnosticGroupsTest {

  private static DiagnosticGroups diagnosticGroups;

  @BeforeClass
  public static void setup() {
    diagnosticGroups = new DiagnosticGroups();
  }

  @Test
  public void testGetRegisteredGroups() {
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertEquals(16, registeredGroups.size());
    assertEquals("accessControls", registeredGroups.keySet().iterator().next());
  }

  @Test
  public void testForName() {
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    assertSame(AccessControls.class, accessControls.getClass());
  }

  @Test(expected = NullPointerException.class)
  public void testForNameNullName() {
    diagnosticGroups.forName(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testForNameUnknownName() {
    diagnosticGroups.forName("unknownGroupName");
  }
}