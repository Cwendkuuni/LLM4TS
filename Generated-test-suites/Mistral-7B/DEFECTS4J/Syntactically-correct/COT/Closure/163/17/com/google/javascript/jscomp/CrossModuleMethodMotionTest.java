import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerUtil.compile;
import static com.google.javascript.jscomp.CompilerUtil.getSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.Logger;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.compiler.Compiler;
import com.google.javascript.rhino.SourceFile;

@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {

  private AbstractCompiler compiler;
  private CompilerPass pass;
  private CompilerOptions options;
  private JSModuleGraph moduleGraph;

  @Before
  public void setUp() {
    compiler = new Compiler();
    options = new CompilerOptions();
    moduleGraph = new JSModuleGraph();
    pass = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
  }

  @Test
  public void testNullCommonModuleError() {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", "testNullCommonModuleError");
    Node root = compile(compiler, source, options);
    pass.process(null, root);
    assertEquals(1, compiler.getNumDiagnostics());
    assertEquals(DiagnosticType.ERROR, compiler.getDiagnostics(0).getType());
  }

  @Test
  public void testMoveMethods() {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", "testMoveMethods");
    Node root = compile(compiler, source, options);
    pass.process(null, root);
    Node expectedOutput = getSource(" CrossModuleMethodMotionTest-expected.js", "testMoveMethods");
    Node actualOutput = compiler.getSourceRootNode();
    assertEquals(expectedOutput.getText(), actualOutput.getText());
  }

  @Test(expected = NullPointerException.class)
  public void testMoveUnreferencedMethod() {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", "testMoveUnreferencedMethod");
    Node root = compile(compiler, source, options);
    pass.process(null, root);
  }

  @Test
  public void testMovePrototypeReadsClosureVariables() {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", "testMovePrototypeReadsClosureVariables");
    Node root = compile(compiler, source, options);
    pass.process(null, root);
    Node expectedOutput = getSource(" CrossModuleMethodMotionTest-expected.js", "testMovePrototypeReadsClosureVariables");
    Node actualOutput = compiler.getSourceRootNode();
    assertEquals(expectedOutput.getText(), actualOutput.getText());
  }

  @Test
  public void testMoveMethodAcrossModules() {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", "testMoveMethodAcrossModules");
    Node root = compile(compiler, source, options);
    pass.process(null, root);
    Node expectedOutput = getSource(" CrossModuleMethodMotionTest-expected.js", "testMoveMethodAcrossModules");
    Node actualOutput = compiler.getSourceRootNode();
    assertEquals(expectedOutput.getText(), actualOutput.getText());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveMethodToSelfModule() {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", "testMoveMethodToSelfModule");
    Node root = compile(compiler, source, options);
    pass.process(null, root);
  }

  @Test
  public void testMoveMethodToSameModule() {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", "testMoveMethodToSameModule");
    Node root = compile(compiler, source, options);
    pass.process(null, root);
    Node expectedOutput = getSource(" CrossModuleMethodMotionTest-expected.js", "testMoveMethodToSameModule");
    Node actualOutput = compiler.getSourceRootNode();
    assertEquals(expectedOutput.getText(), actualOutput.getText());
  }

  @Test
  public void testMoveMethodNotFunction() {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", "testMoveMethodNotFunction");
    Node root = compile(compiler, source, options);
    pass.process(null, root);
  }

  @Test
  public void testMoveMethodGetSet() {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", "testMoveMethodGetSet");
    Node root = compile(compiler, source, options);
    pass.process(null, root);
  }

  @Test(expected = NullPointerException.class)
  public void testMoveMethodNullPrototype() {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", "testMoveMethodNullPrototype");
    Node root = compile(compiler, source, options);
    pass.process(null, root);
  }

  @Test
  public void testMoveMethodDeclarationInExterns() {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", "testMoveMethodDeclarationInExterns");
    Node root = compile(compiler, source, options);
    pass.process(null, root);
    Node expectedOutput = getSource(" CrossModuleMethodMotionTest-expected.js", "testMoveMethodDeclarationInExterns");
    Node actualOutput = compiler.getSourceRootNode();
    assertEquals(expectedOutput.getText(), actualOutput.getText());
  }

  private Node getSourceRootNode(String testName) {
    SourceFile source = getSource(" CrossModuleMethodMotionTest.js", testName);
    Node root = compile(compiler, source, options);
    return root;
  }

  private Node getSource(String fileName, String testName) {
    String fileContent = String.format("(function() {\n%s\n})()",
        getClassContent("CrossModuleMethodMotion") +
        getTestContent(testName));
    return new SourceFile(fileName, fileContent).getAST();
  }

  private String getClassContent(String className) {
    return String.format("class %s {\n" +
        "  static get STUB_METHOD_NAME() { return '%s'; }\n" +
        "  static get UNSTUB_METHOD_NAME() { return '%s'; }\n" +
        "  static STUB_DECLARATIONS = %s;\n" +
        "  %s(AbstractCompiler, IdGenerator, %s);\n" +
        "}\n",
        className, CrossModuleMethodMotion.STUB_METHOD_NAME,
        CrossModuleMethodMotion.UNSTUB_METHOD_NAME,
        CrossModuleMethodMotion.STUB_DECLARATIONS,
        className, "boolean");
  }

  private String getTestContent(String testName) {
    return String.format("%sTest = {\n" +
        "  %s: function() {\n" +
        "    %s;\n" +
        "    %s;\n" +
        "  },\n" +
        "};\n",
        testName, testName, testName, testName);
  }
}