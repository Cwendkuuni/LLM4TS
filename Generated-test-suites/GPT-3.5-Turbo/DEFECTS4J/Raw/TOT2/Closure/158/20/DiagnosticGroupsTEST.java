import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.*;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroupWithType() {
    DiagnosticGroup group = new DiagnosticGroup("testGroup", DiagnosticType.WARNING);
    assertNotNull(DiagnosticGroups.registerGroup("test", group));
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagGroups = new DiagnosticGroups();
    assertNotNull(diagGroups.getRegisteredGroups());
  }

  @Test
  public void testForName() {
    assertNotNull(new DiagnosticGroups().forName("INVALID_CASTS"));
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups diagGroups = new DiagnosticGroups();
    diagGroups.setWarningLevels(options, "INVALID_CASTS", CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagGroups.forName("INVALID_CASTS")));
  }

}