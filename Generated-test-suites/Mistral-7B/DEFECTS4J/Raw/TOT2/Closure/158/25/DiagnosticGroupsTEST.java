import static com.google.javascript.jscomp.DiagnosticGroups.*;
import static org.junit.Assert.assertNotNull;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroupWithDiagnosticType() {
    DiagnosticGroup deprecatedGroup = registerGroup("deprecated", DEPRECATED_NAME);
    assertNotNull(deprecatedGroup);
  }
}