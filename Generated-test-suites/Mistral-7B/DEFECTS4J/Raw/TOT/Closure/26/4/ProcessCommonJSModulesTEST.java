import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class ProcessCommonJSModulesTest {

  private static Compiler createCompiler(String filename) throws IOException {
    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    compiler.compile(new SourceFile(filename, null), options, null);
    return compiler;
  }

  @org.junit.Test
  public void testProcessCommonJSModule() throws IOException, URISyntaxException {
    String inputFile = "testInput.js";
    String expectedOutputFile = "testOutput.js";

    // Test input file
    Node inputNode = NodeFactory.compile(new File(inputFile));

    // Initialize compiler
    Compiler compiler = new Compiler();
    CompilerOptions options = CompilerOptions.defaultOptions();

    // Add test class as compiler input
    Node traverserRoot = NodeFactory.createScope();
    traverserRoot.setSourceFileName("TestCommonJSModules.js");
    compiler.process(new SourceFile(inputFile, null), traverserRoot);

    // Add test class as compiler pass
    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".", false);
    p.process(NodeFactory.EMPTY_NODE, traverserRoot);

    // Compile test input
    CompilerPass pass = p;
    compiler.addPass(pass);
    compiler.compile(new SourceFile(inputFile, null), options, pass);

    // Verify output file
    Node outputNode = compiler.getOutput();
    assertEquals(new File(expectedOutputFile).toURI().toURL().openStream(),
        outputNode.toPrintString(NodePrinter.plainJs));

    // Test that require calls are replaced with direct references
    Node requireCall = traverserRoot.getFirstChild().getLastChild();
    Node expectModule = NodeFactory.createName("expectModule");
    expectModule.setString("expectedModule");
    Node expectedReference = NodeFactory.createName(expectModule);
    Node expectedReferenceWithSuffix = NodeFactory.createName(expectModule + "$$module");

    Node expectedReferenceInScript = NodeFactory.createScript("" +
        "var expectedModule = " + expectedModule + "; " +
        "// Check that require(\"expectedModule\") is replaced with expectedModule" +
        "expectModule.require = function(moduleName) {" +
        "  return " + expectedReferenceWithSuffix + ";" +
        "}" +
        "// Check that require(\"anotherModule\") is unchanged" +
        "var anotherModule = require(\"anotherModule\");");

    Node actualReferenceInScript = outputNode.getLastChild();

    assertTrue(actualReferenceInScript.toString().contains(expectedReferenceWithSuffix.toString()));
    assertFalse(actualReferenceInScript.toString().contains(expectedReference.toString()));
    assertTrue(actualReferenceInScript.toString().contains(expectedReferenceInScript.toString()));

    // Test that module.exports is renamed to moduleName.module$exports
    Node moduleExports = traverserRoot.getChildAtIndex(1).getLastChild();
    Node moduleExportsWithSuffix = NodeFactory.createName(moduleExports.getString() + "$$exports");

    String moduleExportsRenamedInScript = "var " + moduleExports.getString() + " = " + moduleExportsWithSuffix + ";";

    assertTrue(actualReferenceInScript.toString().contains(moduleExportsRenamedInScript));

    // Test that goog.provide and goog.require are emitted for closure compiler automatic ordering
    Set<String> provides = Sets.newHashSet();
    Set<String> requires = Sets.newHashSet();

    NodeTraversal traversal = new NodeTraversal(compiler, outputNode, null);
    NodeTraversal.traverse(compiler, outputNode, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isGetProp() &&
            "goog".equals(n.getFirstChild().getString())) {
          if ("provide".equals(n.getLastChild().getString())) {
            provides.add(n.getLastChild().getChildAtIndex(1).getString());
          } else if ("require".equals(n.getLastChild().getString())) {
            requires.add(n.getLastChild().getChildAtIndex(1).getString());
          }
        }
      }
    });

    List<String> expectedProvides = Arrays.asList("testCommonJSModules", "expectedModule", "anotherModule");
    List<String> expectedRequires = Arrays.asList("anotherModule");

    assertEquals(expectedProvides, provides);
    assertEquals(expectedRequires, requires);
  }
}