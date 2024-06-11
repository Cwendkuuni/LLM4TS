import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserOptions;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.SourceFile;

public class VarCheckTest {

  private Compiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;
  private SourceFile inputSource;
  private Node root;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setLanguageIn(ParserOptions.ECMASCRIPT5);
    compiler = new Compiler(options);
    nodeFactory = options.getInitializer().getFactory();
    inputSource = new SourceFile("Test.js", "function foo() { bar(); }", null);
  }

  @Test
  public void testUndefinedVariable() {
    compiler.compile(inputSource, null, null, this);
    NodeTraversal traversal = new NodeTraversal(compiler);
    NodeTraversal.traverse(compiler, traversal.getRoot(), new VarCheck(compiler));
    Node error = traversal.getError();
    assertEquals(UNDEFINED_VAR_ERROR, error.getDiagnosticType());
    assertEquals("variable bar is undefined", error.getMessage());
  }

  @Test
  public void testVariableAccessInSameFile() {
    inputSource = new SourceFile("Test.js", "function foo() { var bar; bar = 1; }", null);
    compiler.compile(inputSource, null, null, this);
    NodeTraversal traversal = new NodeTraversal(compiler);
    NodeTraversal.traverse(compiler, traversal.getRoot(), new VarCheck(compiler));
    assertTrue(traversal.getErrors().isEmpty());
  }

  @Test
  public void testVariableAccessFromDifferentFile() {
    String otherFile = "OtherFile.js";
    inputSource = new SourceFile("Test.js", "function foo() { require(otherFile); }", null);
    SourceFile otherInputSource = new SourceFile(otherFile, "var bar = 1;", null);
    compiler.compileBatch(new SourceFile[] { inputSource, otherInputSource }, null, null, this);
    NodeTraversal traversal = new NodeTraversal(compiler.getRoot());
    NodeTraversal.traverse(compiler, traversal.getRoot(), new VarCheck(compiler));
    Node error = traversal.getError();
    if (error != null) {
      assertEquals(VIOLATED_MODULE_DEP_ERROR, error.getDiagnosticType());
    } else {
      fail("Expected error: module OtherFile cannot reference Test, defined in Test, since Test loads after OtherFile");
    }
  }

  @Test
  public void testMissingModuleDependency() {
    String otherFile = "OtherFile.js";
    inputSource = new SourceFile("Test.js", "function foo() { require(otherFile); }", null);
    SourceFile otherInputSource = new SourceFile(otherFile, "var bar = 1;", null);
    compiler.compileBatch(new SourceFile[] { inputSource, otherInputSource }, null, null, this);
    NodeTraversal traversal = new NodeTraversal(compiler.getRoot());
    Node error = traversal.getWarning(MISSING_MODULE_DEP_ERROR);
    assertNotNull(error);
    assertEquals("missing module dependency; module Test should depend on OtherFile because it references OtherFile", error.getMessage());
  }

  @Test
  public void testStrictModuleDependency() {
    String otherFile = "OtherFile.js";
    inputSource = new SourceFile("Test.js", "function foo() { require(otherFile); }", null);
    SourceFile otherInputSource = new SourceFile(otherFile, "var bar = 1;", null);
    compiler = new Compiler(options.withErrorLevel(JSError.make("", 0, 0, STRICT_MODULE_DEP_ERROR)));
    compiler.compileBatch(new SourceFile[] { inputSource, otherInputSource }, null, null, this);
    NodeTraversal traversal = new NodeTraversal(compiler.getRoot());
    Node warning = traversal.getWarning(STRICT_MODULE_DEP_ERROR);
    assertNotNull(warning);
    assertEquals("module Test cannot reference OtherFile, defined in OtherFile", warning.getMessage());
  }

  @Test
  public void testInvalidFunctionDeclaration() {
    inputSource = new SourceFile("Test.js", "function() { }", null);
    compiler.compile(inputSource, null, null, this);
    NodeTraversal traversal = new NodeTraversal(compiler.getRoot());
    Node error = traversal.getError();
    assertEquals(INVALID_FUNCTION_DECL, error.getDiagnosticType());
  }

  @Test
  public void testVariableDeclaredInSameFile() {
    inputSource = new SourceFile("Test.js", "function foo() { var bar = 1; }", null);
    compiler.compile(inputSource, null, null, this);
    NodeTraversal traversal = new NodeTraversal(compiler.getRoot());
    NodeTraversal.traverse(compiler, traversal.getRoot(), new VarCheck(compiler));
    assertTrue(traversal.getErrors().isEmpty());
  }

  @Test
  public void testVariableDeclaredInExterns() {
    CompilerInput externsInput = compiler.newExternInput("{var bar: number;}");
    inputSource = new SourceFile("Test.js", "function foo() { console.log(bar); }", externsInput);
    compiler.compile(inputSource, null, null, this);
    NodeTraversal traversal = new NodeTraversal(compiler.getRoot());
    NodeTraversal.traverse(compiler, traversal.getRoot(), new VarCheck(compiler, true));
    assertTrue(traversal.getErrors().isEmpty());
  }

  @Test
  public void testSynthesizedExterns() {
    inputSource = new SourceFile("Test.js", "function foo() { bar(); }", null);
    compiler.compile(inputSource, null, null, this);
    NodeTraversal traversal = new NodeTraversal(compiler.getRoot());
    NodeTraversal.traverse(compiler, traversal.getRoot(), new VarCheck(compiler));
    Node error = traversal.getError();
    assertEquals(UNDEFINED_VAR_ERROR, error.getDiagnosticType());
    assertEquals("variable bar is undefined", error.getMessage());
    Node synthesizedExternsRoot = traversal.getSynthesizedExternsRoot();
    Node barDeclaration = synthesizedExternsRoot.getFirstChild();
    assertEquals("bar", barDeclaration.getString());
    assertEquals(Node.VAR, barDeclaration.getType());
  }

  @Test
  public void testStrictMode() {
    inputSource = new SourceFile("Test.js", "function foo() { bar(); }", null);
    CompilerOptions strictOptions = new CompilerOptions();
    strictOptions.setLanguageIn(ParserOptions.ECMASCRIPT5);
    strictOptions.setWarningLevel(JSError.make("", 0, 0, STRICT_MODULE_DEP_ERROR), CheckLevel.ERROR);
    compiler = new Compiler(strictOptions);
    compiler.compile(inputSource, null, null, this);
    NodeTraversal traversal = new NodeTraversal(compiler.getRoot());
    Node error = traversal.getError();
    assertEquals(STRICT_MODULE_DEP_ERROR, error.getDiagnosticType());
  }
}