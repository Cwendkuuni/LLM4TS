import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class CheckSideEffectsTest {

  CheckSideEffects checkSideEffects;
  AbstractCompiler compiler;
  CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setWarningLevel(Parser.WarningLevel.QUIET);
    compiler = new Compiler(options);
    checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
  }

  @Test
  public void testProcess_EmptyProgram() {
    Node root = new Parser(new CharSequenceReader("")).parseProgram();
    checkSideEffects.process(null, root);
    assertTrue("No warnings expected", compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testProcess_SimpleAssignment() {
    Node root = new Parser(new CharSequenceReader("x = 1;")).parseProgram();
    checkSideEffects.process(null, root);
    List<String> diagnostics = compiler.getDiagnostics();
    assertEquals("One warning expected", 1, diagnostics.size());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", diagnostics.get(0).getMessage());
  }

  @Test
  public void testProcess_VoidStatement() {
    Node root = new Parser(new CharSequenceReader(";")).parseProgram();
    checkSideEffects.process(null, root);
    assertTrue("No warnings expected", compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testProcess_CommaExpression() {
    Node root = new Parser(new CharSequenceReader("x, y;")).parseProgram();
    checkSideEffects.process(null, root);
    List<String> diagnostics = compiler.getDiagnostics();
    assertEquals("One warning expected", 1, diagnostics.size());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", diagnostics.get(0).getMessage());
  }

  @Test
  public void testProcess_SimpleStatementWithSideEffects() {
    Node root = new Parser(new CharSequenceReader("document.write('Hello World');")).parseProgram();
    checkSideEffects.process(null, root);
    assertTrue("No warnings expected", compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testProcess_ExpressionResultWithSideEffects() {
    Node root = new Parser(new CharSequenceReader("1 + document.write('Hello World');")).parseProgram();
    checkSideEffects.process(null, root);
    List<String> diagnostics = compiler.getDiagnostics();
    assertEquals("One warning expected", 1, diagnostics.size());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", diagnostics.get(0).getMessage());
  }

  @Test
  public void testProcess_SimpleOperatorWithSideEffects() {
    Node root = new Parser(new CharSequenceReader("1 + x;")).parseProgram();
    checkSideEffects.process(null, root);
    List<String> diagnostics = compiler.getDiagnostics();
    assertEquals("No warnings expected", diagnostics.isEmpty());
  }

  @Test
  public void testProcess_SimpleOperatorNoSideEffects() {
    Node root = new Parser(new CharSequenceReader("1 + 1;")).parseProgram();
    checkSideEffects.process(null, root);
    List<String> diagnostics = compiler.getDiagnostics();
    assertEquals("One warning expected", 1, diagnostics.size());
    assertEquals("The result of the '+' operator is not being used.", diagnostics.get(0).getMessage());
  }

  @Test
  public void testProcess_ForLoopWithSideEffects() {
    Node root = new Parser(new CharSequenceReader("for (var i = 0; i < 10; i++) { document.write('i: ' + i); }")).parseProgram();
    checkSideEffects.process(null, root);
    assertTrue("No warnings expected", compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testProcess_ForLoopNoSideEffects() {
    Node root = new Parser(new CharSequenceReader("for (var i = 0; i < 10; i++) {}")).parseProgram();
    checkSideEffects.process(null, root);
    List<String> diagnostics = compiler.getDiagnostics();
    assertEquals("One warning expected", 1, diagnostics.size());
    assertEquals("The result of the 'for' statement is not being used.", diagnostics.get(0).getMessage());
  }

  @Test(expected = AssertionError.class)
  public void testProcess_ProtectedSideEffects() {
    Node root = new Parser(new CharSequenceReader("JSCOMPILER_PRESERVE(1 + document.write('Hello World'));")).parseProgram();
    checkSideEffects.process(null, root);
  }

  @Test
  public void testProtectSideEffects() {
    Node root = new Parser(new CharSequenceReader("var s = 'this string is " + "continued on the next line but you forgot the +'; x == foo(); ;")).parseProgram();
    checkSideEffects = new CheckSideEffects(compiler, WARNING, true);
    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();
    Node rootProtected = compiler.getIntermediateRepresentation();
    List<Node> problemNodes = Lists.newArrayList();
    NodeTraversal.traverse(compiler, rootProtected, new NodeTraversal.AbstractCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isCall() && n.getFirstChild().isName() && n.getFirstChild().getString().equals("JSCOMPILER_PRESERVE")) {
          problemNodes.add(n);
        }
      }
    });
    assertEquals("Three protected nodes expected", 3, problemNodes.size());
  }

  @Test
  public void testStripProtection() {
    Node root = new Parser(new CharSequenceReader("function JSCOMPILER_PRESERVE(x) { return x; } JSCOMPILER_PRESERVE(1 + document.write('Hello World'));")).parseProgram();
    checkSideEffects = new CheckSideEffects(compiler, WARNING, false);
    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();
    CompilerPass stripProtection = new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(null, root);
    Node rootStripped = compiler.getIntermediateRepresentation();
    List<String> diagnostics = compiler.getDiagnostics();
    assertEquals("One warning expected", 1, diagnostics.size());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", diagnostics.get(0).getMessage());
    Node nodeToCheck = rootStripped.getLastChild().getLastChild();
    assertFalse("Protected node should be removed", nodeToCheck.isCall());
  }

  @After
  public void tearDown() {
    compiler = null;
    checkSideEffects = null;
  }
}