import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  private AbstractCompiler compilerMock;
  private String filenamePrefixMock;
  private boolean reportDependenciesMock;
  private ProcessCommonJSModules processCommonJSModules;

  @Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    filenamePrefixMock = "/test/prefix/";
    reportDependenciesMock = true;
    processCommonJSModules = new ProcessCommonJSModules(compilerMock, filenamePrefixMock, reportDependenciesMock);
  }

  @Test
  public void testProcess_WithValidInput_CallsExpectedMethods() {
    // Arrange
    Node externs = IR.empty();
    Node root = IR.empty();

    // Act
    processCommonJSModules.process(externs, root);

    // Assert
    verify(compilerMock).reportCodeChange();
  }

  @Test
  public void testGuessCJSModuleName_WithValidFilename_ReturnsExpectedModuleName() {
    // Arrange
    String filename = "test.js";
    String expectedModuleName = "module$test";

    // Act
    String moduleName = processCommonJSModules.guessCJSModuleName(filename);

    // Assert
    assertEquals(expectedModuleName, moduleName);
  }

  @Test
  public void testToModuleName_WithValidFilename_ReturnsExpectedModuleName() {
    // Arrange
    String filename = "/test.js";
    String expectedModuleName = "module$test";

    // Act
    String moduleName = ProcessCommonJSModules.toModuleName(filename);

    // Assert
    assertEquals(expectedModuleName, moduleName);
  }

  @Test
  public void testToModuleName_WithRelativeAddressing_ReturnsExpectedModuleName() {
    // Arrange
    String requiredFilename = "./module.js";
    String currentFilename = "/test/test.js";
    String expectedModuleName = "module$test$module";

    // Act
    String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);

    // Assert
    assertEquals(expectedModuleName, moduleName);
  }

  @Test(expected = RuntimeException.class)
  public void testToModuleName_WithInvalidURISyntax_ThrowsException() {
    // Arrange
    String requiredFilename = "./module.js";
    String currentFilename = "test1:test2";
    String expectedModuleName = "module$test$module";

    // Act
    String moduleName = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);

    // Assert
    // Expecting RuntimeException
  }

  @Test
  public void testNormalizeSourceName_WithValidFilename_ReturnsExpectedNormalizedName() {
    // Arrange
    String filename = "/test/prefix/test.js";
    String expectedNormalizedName = "test.js";

    // Act
    String normalizedName = processCommonJSModules.normalizeSourceName(filename);

    // Assert
    assertEquals(expectedNormalizedName, normalizedName);
  }

  @Test
  public void testProcessCommonJsModulesCallback_VisitRequireCall_CallsExpectedMethods() {
    // Arrange
    NodeTraversal tMock = mock(NodeTraversal.class);
    Node require = IR.call(IR.name("require"), IR.string("module"));

    // Act
    processCommonJSModules.new ProcessCommonJsModulesCallback().visitRequireCall(tMock, require, null);

    // Assert
    verify(tMock.getInput()).addRequire("module");
    verify(compilerMock).reportCodeChange();
  }

  @Test
  public void testProcessCommonJsModulesCallback_VisitScript_CallsExpectedMethods() {
    // Arrange
    NodeTraversal tMock = mock(NodeTraversal.class);
    Node script = IR.script();

    // Act
    processCommonJSModules.new ProcessCommonJsModulesCallback().visitScript(tMock, script);

    // Assert
    verify(compilerMock).reportCodeChange();
  }

  @Test
  public void testProcessCommonJsModulesCallback_VisitModuleExports_CallsExpectedMethod() {
    // Arrange
    Node moduleExports = IR.getprop(IR.name("module"), IR.string("exports"));

    // Act
    processCommonJSModules.new ProcessCommonJsModulesCallback().visitModuleExports(moduleExports);

    // Assert
    assertEquals("module$module", moduleExports.getChildAtIndex(0).getString());
    assertEquals("module$exports", moduleExports.getChildAtIndex(1).getString());
  }

  @Test
  public void testSuffixVarsCallback_Visit_WithValidVarName_SuffixesName() {
    // Arrange
    NodeTraversal tMock = mock(NodeTraversal.class);
    Node n = IR.name("testVar");

    // Act
    processCommonJSModules.new SuffixVarsCallback("module").visit(tMock, n, null);

    // Assert
    assertEquals("testVar$$module", n.getString());
    assertEquals("testVar", n.getProp(Node.ORIGINALNAME_PROP));
  }

  @Test
  public void testSuffixVarsCallback_Visit_WithExportsVar_SuffixesName() {
    // Arrange
    NodeTraversal tMock = mock(NodeTraversal.class);
    Node n = IR.name("exports");

    // Act
    processCommonJSModules.new SuffixVarsCallback("module").visit(tMock, n, null);

    // Assert
    assertEquals("module", n.getString());
    assertEquals("exports", n.getProp(Node.ORIGINALNAME_PROP));
  }

}