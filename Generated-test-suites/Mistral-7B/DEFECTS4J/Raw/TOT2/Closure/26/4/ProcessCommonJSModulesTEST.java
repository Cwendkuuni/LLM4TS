import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeVisitor;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class ProcessCommonJSModulesTest {

  private static final String TEST_INPUT_FOLDER = "test_inputs";
  private static final String TEST_OUTPUT_FOLDER = "test_outputs";

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String inputFile = TEST_INPUT_FOLDER + File.separator + "input.js";
    String expectedOutputFile = TEST_OUTPUT_FOLDER + File.separator + "expectedOutput.js";

    CompilerOptions options = CompilerOptions.getDefaults();
    Compiler compiler = new Compiler(options);

    Node root = NodeFactory.createRoot();
    Node externs = NodeFactory.createExterns();
    compiler.compilationUnitToCompilationUnit(externs, root, new File(inputFile));

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".");
    pass.process(externs, root);

    String actualOutput = getRewrittenCode(compiler, root);
    String expectedOutput = new String(new File(expectedOutputFile).getBytes());
    assertEquals(expectedOutput, actualOutput);
  }

  @org.junit.Test
  public void testProcessCommonJSModules_multipleModules()
      throws IOException, URISyntaxException {

    String inputFile = TEST_INPUT_FOLDER + File.separator + "multiple_modules.js";
    String expectedOutputFile1 = TEST_OUTPUT_FOLDER + File.separator + "module1_expectedOutput.js";
    String expectedOutputFile2 = TEST_OUTPUT_FOLDER + File.separator + "module2_expectedOutput.js";

    CompilerOptions options = CompilerOptions.getDefaults();
    Compiler compiler = new Compiler(options);

    Node root = NodeFactory.createRoot();
    Node externs = NodeFactory.createExterns();
    compiler.compilationUnitToCompilationUnit(externs, root, new File(inputFile));

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".");
    pass.process(externs, root);

    String actualOutput1 = getRewrittenCode(compiler, root, "module1");
    String expectedOutput1 = new String(new File(expectedOutputFile1).getBytes());
    assertEquals(expectedOutput1, actualOutput1);

    String actualOutput2 = getRewrittenCode(compiler, root, "module2");
    String expectedOutput2 = new String(new File(expectedOutputFile2).getBytes());
    assertEquals(expectedOutput2, actualOutput2);
  }

  @org.junit.Test
  public void testProcessCommonJSModules_requireRelativePath()
      throws IOException, URISyntaxException {

    String inputFile = TEST_INPUT_FOLDER + File.separator + "require_relative_path.js";
    String expectedOutputFile = TEST_OUTPUT_FOLDER + File.separator + "expectedOutput.js";

    CompilerOptions options = CompilerOptions.getDefaults();
    Compiler compiler = new Compiler(options);

    Node root = NodeFactory.createRoot();
    Node externs = NodeFactory.createExterns();
    compiler.compilationUnitToCompilationUnit(externs, root, new File(inputFile));

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".");
    pass.process(externs, root);

    String actualOutput = getRewrittenCode(compiler, root);
    String expectedOutput = new String(new File(expectedOutputFile).getBytes());
    assertEquals(expectedOutput, actualOutput);
  }

  @org.junit.Test
  public void testProcessCommonJSModules_requireAbsolutePath()
      throws IOException, URISyntaxException {

    String inputFile = TEST_INPUT_FOLDER + File.separator + "require_absolute_path.js";
    String expectedOutputFile = TEST_OUTPUT_FOLDER + File.separator + "expectedOutput.js";

    CompilerOptions options = CompilerOptions.getDefaults();
    Compiler compiler = new Compiler(options);

    Node root = NodeFactory.createRoot();
    Node externs = NodeFactory.createExterns();
    compiler.compilationUnitToCompilationUnit(externs, root, new File(inputFile));

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".");
    pass.process(externs, root);

    String actualOutput = getRewrittenCode(compiler, root);
    String expectedOutput = new String(new File(expectedOutputFile).getBytes());
    assertEquals(expectedOutput, actualOutput);
  }

  @org.junit.Test
  public void testProcessCommonJSModules_invalidRequireCall()
      throws IOException, URISyntaxException {

    String inputFile = TEST_INPUT_FOLDER + File.separator + "invalid_require_call.js";
    String expectedErrorMessage =
        "Error: Unexpected token : in require call";

    CompilerOptions options = CompilerOptions.getDefaults();
    Compiler compiler = new Compiler(options);

    Node root = NodeFactory.createRoot();
    Node externs = NodeFactory.createExterns();
    compiler.compilationUnitToCompilationUnit(externs, root, new File(inputFile));

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, ".");
    try {
      pass.process(externs, root);
      fail("Expected compilation error");
    } catch (AssertionError e) {
      List<String> errors = Arrays.asList(e.getStackTrace()[0].getLineNumber(),
          e.getLocalizedMessage());
      List<String> actualErrors = Arrays.asList(compiler.getErrors().getErrorList()
          .get(0).getMessage().split("\n"));
      assertEquals(errors, actualErrors);
    }
  }

  private String getRewrittenCode(Compiler compiler, Node root, String moduleName) {
    ProcessCommonJSModulesCallback callback = new ProcessCommonJSModulesCallback();
    NodeTraversal.traverse(compiler, root, callback);

    String actualOutput = "";
    for (Node child : callback.getRewrittenNodes().values()) {
      actualOutput += child.toSource(compiler);
    }

    return actualOutput;
  }

  private String getRewrittenCode(Compiler compiler, Node root) {
    ProcessCommonJSModulesCallback callback = new ProcessCommonJSModulesCallback();
    NodeTraversal.traverse(compiler, root, callback);

    String actualOutput = "";
    for (Node child : callback.getRewrittenNodes().values()) {
      actualOutput += child.toSource(compiler);
    }

    return actualOutput;
  }

  private static class ProcessCommonJSModulesCallback extends NodeVisitor {
    private Set<Node> rewrittenNodes = Sets.newLinkedHashSet();

    Set<Node> getRewrittenNodes() {
      return rewrittenNodes;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getChildCount() == 2 &&
          "require".equals(n.getFirstChild().getQualifiedName()) &&
          n.getChildAtIndex(1).isString()) {
        Node moduleRef = IR.name(toModuleName(n.getChildAtIndex(1).getString(),
            normalizeSourceName(t.getSourceName())))
            .srcref(n);
        parent.replaceChild(n, moduleRef);
        Node script = getCurrentScriptNode(parent);
        script.addChildToBack(IR.comment("// " + n.getQualifiedName()));
      } else if (n.isScript()) {
        rewrittenNodes.add(n);
      }

      super.visit(t, n, parent);
    }

    private Node getCurrentScriptNode(Node n) {
      while (true) {
        if (n.isScript()) {
          return n;
        }
        n = n.getParent();
      }
    }
  }
}