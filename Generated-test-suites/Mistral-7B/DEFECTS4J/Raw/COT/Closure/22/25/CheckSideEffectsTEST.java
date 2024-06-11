import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.SourceName;

import java.util.List;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private CompilerPassManager passManager;
  private NodeOperations nodeOperations;

  @org.junit.Before
  public void setUp() throws Exception {
    this.compiler = mock(AbstractCompiler.class);
    this.checker = mock(Checker.class);
    this.passManager = mock(CompilerPassManager.class);
    this.nodeOperations = mock(NodeOperations.class);

    when(compiler.getChecker()).thenReturn(checker);
    when(compiler.getCompilerPassManager()).thenReturn(passManager);
    when(compiler.getNodeOperations()).thenReturn(nodeOperations);
  }

  @org.junit.Test
  public void testProcess_NoUselessCode() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = NodeOperations.createTopLevelScript("var x = 1;");

    checkSideEffects.process(null, root);

    assertTrue("No warnings expected", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProcess_UselessAssignment() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = NodeOperations.createTopLevelScript("var x = 1; x = 1;");

    checkSideEffects.process(null, root);

    assertTrue("One warning expected", checkSideEffects.problemNodes.size() == 1);
    Node warningNode = checkSideEffects.problemNodes.get(0);
    DiagnosticType diagnosticType = checkSideEffects.getLastDiagnostic();
    String message = diagnosticType.getMessage();
    String expectedMessage = "Suspicious code. This code lacks side-effects. Is there a bug?";

    assertTrue("Unexpected warning message", message.contains(expectedMessage));
  }

  @org.junit.Test
  public void testProcess_UselessExpression() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = NodeOperations.createTopLevelScript("1 + 1;");

    checkSideEffects.process(null, root);

    assertTrue("No warnings expected", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProcess_UselessExpressionWithString() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = NodeOperations.createTopLevelScript("\"this string is continued on the next line but you forgot the +\";");

    checkSideEffects.process(null, root);

    assertTrue("One warning expected", checkSideEffects.problemNodes.size() == 1);
    Node warningNode = checkSideEffects.problemNodes.get(0);
    DiagnosticType diagnosticType = checkSideEffects.getLastDiagnostic();
    String message = diagnosticType.getMessage();
    String expectedMessage = "Suspicious code. Is there a missing '+' on the previous line?";

    assertTrue("Unexpected warning message", message.contains(expectedMessage));
  }

  @org.junit.Test
  public void testProcess_CallWithUselessArg() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = NodeOperations.createTopLevelScript("function foo() {} foo();");

    checkSideEffects.process(null, root);

    assertTrue("One warning expected", checkSideEffects.problemNodes.size() == 1);
    Node warningNode = checkSideEffects.problemNodes.get(0);
    DiagnosticType diagnosticType = checkSideEffects.getLastDiagnostic();
    String message = diagnosticType.getMessage();
    String expectedMessage = "Suspicious code. The result of the 'call' operator is not being used.";

    assertTrue("Unexpected warning message", message.contains(expectedMessage));
  }

  @org.junit.Test
  public void testProcess_EmptyBlock() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = NodeOperations.createTopLevelScript("{}");

    checkSideEffects.process(null, root);

    assertTrue("No warnings expected", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProcess_ExpressionStatementInsideBlock() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = NodeOperations.createTopLevelScript("{1 + 1;}");

    checkSideEffects.process(null, root);

    assertTrue("No warnings expected", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProcess_StatementInsideBlock() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = NodeOperations.createTopLevelScript("{var x = 1;}");

    checkSideEffects.process(null, root);

    assertTrue("No warnings expected", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProcess_ConditionalStatement() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = NodeOperations.createTopLevelScript("if (true) {} else {}");

    checkSideEffects.process(null, root);

    assertTrue("No warnings expected", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProcess_LoopStatement() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = NodeOperations.createTopLevelScript("for (var i = 0; i < 10; i++) {}");

    checkSideEffects.process(null, root);

    assertTrue("No warnings expected", checkSideEffects.problemNodes.isEmpty());
  }

  @org.junit.Test
  public void testProtectSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = NodeOperations.createTopLevelScript("var x = 1;");

    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();

    // Protected nodes are not stored in problemNodes, so we can't test them directly.
    // However, we can check that they were indeed protected.
    // TODO: add a way to verify that protected nodes were not removed.
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testProtectSideEffects_NoProblemNodes() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = NodeOperations.createTopLevelScript("1 + 1;");

    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testProtectSideEffects_NullProblemNodes() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = NodeOperations.createTopLevelScript("1 + 1;");

    checkSideEffects.problemNodes = null;
    checkSideEffects.protectSideEffects();
  }

  @org.junit.Test
  public void testStripProtection() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = NodeOperations.createTopLevelScript("function JSCOMPILER_PRESERVE(x) { return x; } JSCOMPILER_PRESERVE(1 + 1);");

    checkSideEffects.process(null, root);
    checkSideEffects.protectSideEffects();

    StripProtection stripProtection = new StripProtection(compiler);
    stripProtection.process(null, root);

    Node expectedNode = NodeOperations.createTopLevelScript("1 + 1;");
    Node actualNode = root;

    assertTrue("Unexpected tree after stripping protection", NodeUtil.isTreeEquivalent(expectedNode, actualNode));
  }

  private DiagnosticType getLastDiagnostic() {
    DiagnosticType[] diagnostics = checker.getDiagnostics();
    return diagnostics[diagnostics.length - 1];
  }
}