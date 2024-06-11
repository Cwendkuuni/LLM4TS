import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.*;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;

import java.io.File;
import java.io.IOException;
import java.net.URISynthesisException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Category(CompilationTests.class)
public class ProcessCommonJSModulesTest {

  private static final String TEST_FILE_1 = "TestCommonJSModule1.js";
  private static final String TEST_FILE_2 = "TestCommonJSModule2.js";
  private static final String TEST_INPUT_DIR = "test/input/";
  private static final String TEST_OUTPUT_DIR = "test/output/";

  @Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    CompilerOptions options = new CompilerOptions(true, null);
    Compiler compiler = new Compiler(options);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    SourceFile input = new SourceFile(new File(TEST_INPUT_DIR + TEST_FILE_1));
    Node root = input.getTree();
    pass.process(input.getExterns(), root);

    SourceFile output = new SourceFile(new File(TEST_OUTPUT_DIR + TEST_FILE_1));
    assertEquals(output.getTree().getTreeText(), root.toString());

    String expectedModuleName1 = "module$TestCommonJSModule1";
    String expectedModuleName2 = "module$TestCommonJSModule2";

    Node require1 = root.getFirstChild();
    Node moduleRef1 = require1.getFirstChild();
    assertEquals(expectedModuleName1, moduleRef1.getString());

    Node require2 = root.getChildAtIndex(2);
    Node moduleRef2 = require2.getFirstChild();
    assertEquals(expectedModuleName2, moduleRef2.getString());

    Node moduleExports1 = root.getChildAtIndex(3);
    Node moduleExportsRef1 = moduleExports1.getFirstChild();
    assertEquals(expectedModuleName1, moduleExportsRef1.getString());

    Node moduleExports2 = root.getChildAtIndex(4);
    Node moduleExportsRef2 = moduleExports2.getChildAtIndex(1);
    assertEquals("module$exports", moduleExportsRef2.getString());

    Node script = root.getChildAtIndex(5);
    List<String> expectedLines = Arrays.asList(
        "goog.provide('" + expectedModuleName1 + "');",
        "goog.require('" + expectedModuleName2 + "');",
        "var " + expectedModuleName1 + " = " + expectedModuleName1 + ";",
        "var " + expectedModuleName2 + " = " + expectedModuleName2 + ";",
        "// Your test code here"
    );
    List<String> lines = treeToLines(script);
    for (int i = 0; i < lines.size(); i++) {
      assertEquals(expectedLines.get(i), lines.get(i));
    }
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire()
      throws IOException, URISyntaxException {
    CompilerOptions options = new CompilerOptions(true, null);
    Compiler compiler = new Compiler(options);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    String currentFileName = TEST_FILE_1;
    String requiredFileName = "TestCommonJSModule2.js";

    SourceFile input = new SourceFile(new File(TEST_INPUT_DIR + currentFileName));
    Node root = input.getTree();
    pass.process(input.getExterns(), root);

    SourceFile output = new SourceFile(new File(TEST_OUTPUT_DIR + currentFileName));
    assertEquals(output.getTree().getTreeText(), root.toString());

    String expectedModuleName1 = "module$TestCommonJSModule1";
    String expectedModuleName2 = "module$TestCommonJSModule2";

    Node require = root.getFirstChild();
    Node moduleRef = require.getFirstChild();
    assertEquals(expectedModuleName2, moduleRef.getString());

    Node moduleExports1 = root.getChildAtIndex(1);
    Node moduleExportsRef1 = moduleExports1.getChildAtIndex(1);
    assertEquals("module$exports", moduleExportsRef1.getString());

    Node script = root.getChildAtIndex(2);
    List<String> expectedLines = Arrays.asList(
        "goog.provide('" + expectedModuleName1 + "');",
        "goog.require('" + expectedModuleName2 + "');",
        "var " + expectedModuleName1 + " = " + expectedModuleName1 + ";",
        "// Your test code here",
        "// Rewritten require",
        "goog.require('" + requiredFileName + "');",
        "var " + expectedModuleName2 + " = " + expectedModuleName2 + ";"
    );
    List<String> lines = treeToLines(script);
    for (int i = 0; i < lines.size(); i++) {
      assertEquals(expectedLines.get(i), lines.get(i));
    }
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesWithInvalidRequire()
      throws IOException, URISyntaxException {
    CompilerOptions options = new CompilerOptions(true, null);
    Compiler compiler = new Compiler(options);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    SourceFile input = new SourceFile(new File(TEST_INPUT_DIR + TEST_FILE_1));
    Node root = input.getTree();
    pass.process(input.getExterns(), root);

    Node require = root.getFirstChild();
    require.getChildAtIndex(1).setString("invalidModule");

    SourceFile output = new SourceFile(new File(TEST_OUTPUT_DIR + TEST_FILE_1));
    pass.process(input.getExterns(), root);

    throw new RuntimeException("Expected error during compilation");
  }

  @Test
  public void testProcessCommonJSModulesWithDuplicateRequire()
      throws IOException, URISyntaxException {
    CompilerOptions options = new CompilerOptions(true, null);
    Compiler compiler = new Compiler(options);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    SourceFile input = new SourceFile(new File(TEST_INPUT_DIR + TEST_FILE_1));
    Node root = input.getTree();
    pass.process(input.getExterns(), root);

    SourceFile output = new SourceFile(new File(TEST_OUTPUT_DIR + TEST_FILE_1));
    assertEquals(output.getTree().getTreeText(), root.toString());

    String expectedModuleName1 = "module$TestCommonJSModule1";
    String expectedModuleName2 = "module$TestCommonJSModule2";

    Node require1 = root.getFirstChild();
    Node moduleRef1 = require1.getFirstChild();
    assertEquals(expectedModuleName2, moduleRef1.getString());

    Node require2 = root.getChildAtIndex(2);
    Node moduleRef2 = require2.getFirstChild();
    assertEquals(expectedModuleName2, moduleRef2.getString());

    Node moduleExports1 = root.getChildAtIndex(1);
    Node moduleExportsRef1 = moduleExports1.getChildAtIndex(1);
    assertEquals("module$exports", moduleExportsRef1.getString());

    Node script = root.getChildAtIndex(3);
    List<String> expectedLines = Arrays.asList(
        "goog.provide('" + expectedModuleName1 + "');",
        "goog.require('" + expectedModuleName2 + "');",
        "var " + expectedModuleName1 + " = " + expectedModuleName1 + ";",
        "// Your test code here",
        "goog.require('" + expectedModuleName2 + "');",
        "// Rewritten require",
        "goog.require('" + expectedModuleName2 + "');", // Duplicate require
        "var " + expectedModuleName2 + " = " + expectedModuleName2 + ";"
    );
    List<String> lines = treeToLines(script);
    for (int i = 0; i < lines.size(); i++) {
      assertEquals(expectedLines.get(i), lines.get(i));
    }

    // This should be reported as an error.
    Set<String> requires = Sets.newHashSet("module$TestCommonJSModule2");
    assertEquals(requires, compiler.getInput().getRequires());
  }
}