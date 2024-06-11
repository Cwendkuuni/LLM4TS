import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticType;

public class DiagnosticGroupsTest {

  @Test
  public void testRegisterGroup() {
    DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", new DiagnosticType[] {});
    assertNotNull(group);
    assertEquals("testGroup", group.getName());
  }

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroup deprecated = DiagnosticGroups.DEPRECATED;
    DiagnosticGroup accessControls = DiagnosticGroups.ACCESS_CONTROLS;

    Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();

    assertTrue(registeredGroups.containsKey(deprecated.getName()));
    assertTrue(registeredGroups.containsKey(accessControls.getName()));
    assertEquals(2, registeredGroups.size());
  }

  @Test
  public void testForName() {
    DiagnosticGroup deprecated = DiagnosticGroups.DEPRECATED;
    DiagnosticGroup accessControls = DiagnosticGroups.ACCESS_CONTROLS;

    DiagnosticGroup group = DiagnosticGroups.forName("DEPRECATED");
    DiagnosticGroup group2 = DiagnosticGroups.forName("ACCESS_CONTROLS");

    assertEquals(deprecated, group);
    assertEquals(accessControls, group2);
  }

  @Test
  public void testSetWarningLevels() {
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroups = Arrays.asList("DEPRECATED", "ACCESS_CONTROLS");
    DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);

    DiagnosticGroup deprecated = DiagnosticGroups.DEPRECATED;
    DiagnosticGroup accessControls = DiagnosticGroups.ACCESS_CONTROLS;

    assertEquals(CheckLevel.WARNING, options.getWarningLevel(deprecated));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(accessControls));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWarningLevelsNullDiagnosticGroups() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups.setWarningLevels(options, null, CheckLevel.WARNING);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWarningLevelsEmptyDiagnosticGroups() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups.setWarningLevels(options, Collections.emptyList(), CheckLevel.WARNING);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetWarningLevelsUnknownDiagnosticGroup() {
    CompilerOptions options = new CompilerOptions();
    DiagnosticGroups.setWarningLevels(options, Arrays.asList("UNKNOWN_GROUP"), CheckLevel.WARNING);
  }

  @Test
  public void testCreateChecker() {
    DiagnosticGroup deprecated = DiagnosticGroups.DEPRECATED;
    DiagnosticGroup accessControls = DiagnosticGroups.ACCESS_CONTROLS;

    CheckerFactory checkerFactory = new CheckerFactory();
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    DiagnosticGroups.setWarningLevels(options, Arrays.asList("DEPRECATED", "ACCESS_CONTROLS"), CheckLevel.WARNING);

    compiler.setCheckerFactory(checkerFactory);
    compiler.setOptions(options);

    assertNotNull(compiler.getChecker("deprecation"));
    assertNotNull(compiler.getChecker("access"));
  }

  @Test
  public void testCreateCheckerNullDiagnosticGroups() {
    DiagnosticGroup deprecated = DiagnosticGroups.DEPRECATED;
    DiagnosticGroup accessControls = DiagnosticGroups.ACCESS_CONTROLS;

    CheckerFactory checkerFactory = new CheckerFactory();
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    DiagnosticGroups.setWarningLevels(options, null, CheckLevel.WARNING);

    compiler.setCheckerFactory(checkerFactory);
    compiler.setOptions(options);

    assertNull(compiler.getChecker("deprecation"));
    assertNull(compiler.getChecker("access"));
  }
}