import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.CheckerFactory.CheckerFactoryBuilder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckerFactory checkerFactory;
  private CheckSideEffects checkSideEffects;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    nodeFactory = mock(NodeFactory.class);
    CompilerOptions options = new CompilerOptions();
    compiler = new Compiler(options, null, null, null, null, null, null);
    compiler.setInput(mock(CompilerInput.class));
    CheckerFactoryBuilder checkerFactoryBuilder = mock(CheckerFactoryBuilder.class);
    checkerFactory = new CheckerFactory(checkerFactoryBuilder, nodeFactory);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
  }

  @Test
  public void testEmptyBlock() {
    Node root = newNode("{}");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @Test
  public void testVoidStatement() {
    Node root = newNode(";");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @Test
  public void testSemicolonAtEndOfStatementBlock() {
    Node root = newNode("if (true) {}", ";");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @Test
  public void testStatementWithoutSideEffect() {
    Node root = newNode("x = 1 + 2;");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @Test
  public void testStatementWithSideEffect() {
    Node root = newNode("document.write('Hello, world!');");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectInIfStatement() {
    Node root = newNode("if (true) document.write('Hello, world!');");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectInForStatement() {
    Node root = newNode(
        "for (int i = 0; i < 10; i++) {" +
            "document.write('Hello, world!');" +
            "}");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectInExpressionStatement() {
    Node root = newNode("document.write('Hello, world!')");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectInReturnStatement() {
    Node root = newNode(
        "function foo() {" +
            "document.write('Hello, world!');" +
            "return 1;" +
        "}");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectInAssignmentStatement() {
    Node root = newNode(
        "function foo() {" +
            "document.write('Hello, world!');" +
            "x = 1;" +
        "}");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectInExpression() {
    Node root = newNode(
        "function foo() {" +
            "document.write('Hello, world!') + 1;" +
        "}");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectInExpressionInAssignmentStatement() {
    Node root = newNode(
        "function foo() {" +
            "x = document.write('Hello, world!') + 1;" +
        "}");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectInCommaExpression() {
    Node root = newNode(
        "function foo() {" +
            "document.write('Hello, world!'), x = 1;" +
        "}");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectInCommaExpressionInAssignmentStatement() {
    Node root = newNode(
        "function foo() {" +
            "x = document.write('Hello, world!'), 1;" +
        "}");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectInExpressionInAssignmentExpression() {
    Node root = newNode(
        "function foo() {" +
            "x = document.write('Hello, world!') + 1;" +
            "y = document.write('Hello, world!') + 2;" +
        "}");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectProtected() {
    checkSideEffects.protectSideEffects();
    Node root = newNode(
        "function foo() {" +
            "document.write('Hello, world!');" +
        "}");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @Test
  public void testStatementWithSideEffectProtectedAndRemoved() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {}");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @Test
  public void testStatementWithSideEffectProtectedButNotRemoved() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectProtectedButRemovedLater() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    checkSideEffects.process(null, root);
    verifyNoWarnings();
  }

  @Test
  public void testStatementWithSideEffectProtectedButNotRemovedLater() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectProtectedAndRemovedLaterWithDifferentNames() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    Node root2 = newNode("function bar() {document.write('Hello, world!')}");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
    checkSideEffects.process(null, root2);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectProtectedButNotRemovedLaterWithDifferentNames() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    Node root2 = newNode("function bar() {document.write('Hello, world!')}");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
    checkSideEffects.process(null, root2);
    verifyNoWarnings();
  }

  @Test
  public void testStatementWithSideEffectProtectedAndRemovedLaterWithSameName() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    Node root2 = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.process(null, root);
    verifyNoWarnings();
    checkSideEffects.process(null, root2);
    verifyNoWarnings();
  }

  @Test
  public void testStatementWithSideEffectProtectedButNotRemovedLaterWithSameName() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    Node root2 = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
    checkSideEffects.process(null, root2);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectProtectedAndRemovedLaterWithSameNameAndDifferentLocation() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    Node root2 = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    checkSideEffects.process(null, root);
    verifyNoWarnings();
    checkSideEffects.process(null, root2);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectProtectedButNotRemovedLaterWithSameNameAndDifferentLocation() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    Node root2 = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
    checkSideEffects.process(null, root2);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectProtectedAndRemovedLaterWithSameNameAndSameLocation() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    Node root2 = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    checkSideEffects.process(null, root);
    verifyNoWarnings();
    checkSideEffects.process(null, root2);
    verifyNoWarnings();
  }

  @Test
  public void testStatementWithSideEffectProtectedButNotRemovedLaterWithSameNameAndSameLocation() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    Node root2 = newNode("function foo() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
    checkSideEffects.process(null, root2);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectProtectedAndRemovedLaterWithDifferentNamesAndSameLocation() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    Node root2 = newNode("function bar() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    checkSideEffects.process(null, root);
    verifyNoWarnings();
    checkSideEffects.process(null, root2);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  @Test
  public void testStatementWithSideEffectProtectedButNotRemovedLaterWithDifferentNamesAndSameLocation() {
    checkSideEffects.protectSideEffects();
    Node root = newNode("function foo() {document.write('Hello, world!')}");
    Node root2 = newNode("function bar() {document.write('Hello, world!')}");
    checkSideEffects.stripProtection();
    checkSideEffects.process(null, root);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
    checkSideEffects.process(null, root2);
    verifyWarning("Suspicious code. document.write('Hello, world!')");
  }

  private void verifyNoWarnings() {
    ArgumentCaptor<Node> argumentCaptor = ArgumentCaptor.forClass(Node.class);
    verify(checkerFactory, never()).recordWarning(argumentCaptor.capture(Node.class),
        eq(USELESS_CODE_ERROR), anyString());
  }

  private void verifyWarning(String message) {
    ArgumentCaptor<Node> argumentCaptor = ArgumentCaptor.forClass(Node.class);
    verify(checkerFactory, times(1)).recordWarning(argumentCaptor.capture(Node.class),
        eq(USELESS_CODE_ERROR), eq(message));
  }

  private Node newNode(String code) {
    CompilerInput input = compiler.getInput();
    Node root = (Node) input.parse(code, null, null, null, null);
    checkSideEffects.process(null, root);
    return root;
  }

  private void stripProtection() {
    checkSideEffects.stripProtection = new StripProtection(compiler);
  }
}