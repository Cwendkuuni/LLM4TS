import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private SourceFile sourceFile;
  private Node rootNode;

  @org.junit.Before
  public void setup() throws IOException {
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options);

    sourceFile = new SourceFile("test.js", new File(".").getAbsolutePath() + File.separator);
    rootNode = sourceFile.getTree();
  }

  @org.junit.Test
  public void testProcessCommonJSModules() {
    new ProcessCommonJSModules(compiler, ".", true).process(sourceFile.getExterns(), rootNode);
    Node module = rootNode.getChildAtIndex(0);
    assertNotNull(module.getChildAtIndex(0));
    assertNotNull(module.getChildAtIndex(1));
    assertEquals("module$exports", module.getChildAtIndex(1).getString());
  }

  @org.junit.Test
  public void testGuessCJSModuleName() {
    assertEquals("module$test", guessCJSModuleName("test.js"));
    assertEquals("module$test$subdir$file", guessCJSModuleName("subdir/file.js"));
  }

  @org.junit.Test
  public void testToModuleName() {
    assertEquals("module$test", toModuleName("test.js"));
    assertEquals("module$test$subdir$file", toModuleName("subdir/file.js"));
    assertEquals("module$test$subdir$file$subsubdir$file",
        toModuleName("subdir/subsubdir/file.js"));
    assertEquals("module$test$../subdir$file", toModuleName("./subdir/file.js"));
    assertEquals("module$test$./subdir$file", toModuleName("subdir/file.js"));
  }

  @org.junit.Test
  public void testNormalizeSourceName() {
    assertEquals("test", normalizeSourceName("./test.js"));
    assertEquals("test", normalizeSourceName("test.js"));
    assertEquals("test", normalizeSourceName("./test"));
  }

  @org.junit.Test
  public void testProcessCommonJsModulesCallback() {
    new ProcessCommonJSModules(compiler, ".", true).process(sourceFile.getExterns(), rootNode);
    Node script = rootNode.getChildAtIndex(0);
    Set<String> moduleNames = Sets.newHashSet("module$test", "module$test$exports");
    for (Node childNode : script.getChildren()) {
      if (childNode.isName()) {
        String name = childNode.getString();
        assertTrue(moduleNames.contains(name) || name.endsWith("$$module$test"));
      }
    }
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testToModuleNameWithInvalidURI() {
    toModuleName("test//invalid.js");
  }

  @org.junit.Test
  public void testProcessRequireCall() {
    String testRequire = "require('test.js')";
    Node requireNode = IR.parseTree(testRequire).getFirstChild();
    Node root = IR.parseTree(sourceFile.getCode().substring(0, sourceFile.getCode().length() - testRequire.length())).getFirstChild();
    new ProcessCommonJSModules(compiler, ".", true).process(sourceFile.getExterns(), root);

    Node requireCall = rootNode.getChildAtIndex(0).getChildAtIndex(1);
    String expectedModuleName = toModuleName("test.js");
    assertEquals(expectedModuleName, requireCall.getString());
  }
}