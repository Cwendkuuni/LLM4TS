import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CheckGlobalThis.class})
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = PowerMockito.mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithConstructor() {
    Node node = new Node(Token.FUNCTION, null);
    JSDocInfo jsDoc = new JSDocInfo("constructor", null);
    node.setJSDocInfo(jsDoc);

    assertTrue(checker.shouldTraverse(new NodeTraversal(null), node, null));
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    Node node = new Node(Token.FUNCTION, null);
    JSDocInfo jsDoc = new JSDocInfo("", null);
    jsDoc.setThisType("self");
    node.setJSDocInfo(jsDoc);

    assertTrue(checker.shouldTraverse(new NodeTraversal(null), node, null));
  }

  @Test
  public void testShouldTraverseFunctionOutsideOfPrototypeProperty() {
    Node parent = new Node(Token.NAME, "parent");
    Node node = new Node(Token.FUNCTION, null);

    Node lhs = new Node(Token.GETPROP, null, parent, null);
    lhs.setString("property");

    parent.addChildToEnd(lhs);
    parent.addChildToEnd(node);

    assertTrue(checker.shouldTraverse(new NodeTraversal(null), node, parent));
  }

  @Test
  public void testShouldTraverseFunctionOutsideOfPrototypeSubProperty() {
    Node parent = new Node(Token.NAME, "parent");
    Node node = new Node(Token.FUNCTION, null);

    Node lhs = new Node(Token.GETPROP, null, parent, null);
    lhs.setString("subProperty");

    parent.addChildToEnd(lhs);
    parent.addChildToEnd(node);

    assertTrue(checker.shouldTraverse(new NodeTraversal(null), node, parent));
  }

  @Test
  public void testShouldTraverseFunctionOutsideOfPrototype() {
    Node node = new Node(Token.FUNCTION, null);

    Node parent = new Node(Token.BLOCK, null);
    parent.addChildToEnd(node);

    assertTrue(checker.shouldTraverse(new NodeTraversal(null), node, parent));
  }

  @Test
  public void testShouldTraverseFunctionOutsideOfScript() {
    Node node = new Node(Token.FUNCTION, null);

    Node parent = new Node(Token.SCRIPT, null);
    parent.addChildToEnd(node);

    assertTrue(checker.shouldTraverse(new NodeTraversal(null), node, parent));
  }

  @Test
  public void testShouldTraverseFunctionOutsideOfName() {
    Node node = new Node(Token.FUNCTION, null);

    Node parent = new Node(Token.NAME, "parentName");
    parent.addChildToEnd(node);

    assertTrue(checker.shouldTraverse(new NodeTraversal(null), node, parent));
  }

  @Test
  public void testShouldTraverseFunctionOutsideOfAssign() {
    Node node = new Node(Token.FUNCTION, null);

    Node parent = new Node(Token.ASSIGN, null, null, null);
    parent.addChildToEnd(node);

    assertTrue(checker.shouldTraverse(new NodeTraversal(null), node, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionAnnotatedWithOverride() {
    Node node = new Node(Token.FUNCTION, null);

    JSDocInfo jsDoc = new JSDocInfo("override", null);
    node.setJSDocInfo(jsDoc);

    assertFalse(checker.shouldTraverse(new NodeTraversal(null), node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionInsidePrototypeProperty() {
    Node parent = new Node(Token.NAME, "parent");
    Node node = new Node(Token.FUNCTION, null);

    Node lhs = new Node(Token.GETPROP, null, parent, null);
    lhs.setString("prototype");

    Node prototype = new Node(Token.NAME, "prototype");
    prototype.addChildToEnd(lhs);
    prototype.addChildToEnd(node);

    parent.addChildToEnd(prototype);

    assertFalse(checker.shouldTraverse(new NodeTraversal(null), node, parent));
  }

  @Test
  public void testShouldNotTraverseFunctionInsidePrototypeSubProperty() {
    Node parent = new Node(Token.NAME, "parent");
    Node node = new Node(Token.FUNCTION, null);

    Node lhs = new Node(Token.GETPROP, null, parent, null);
    lhs.setString("prototype");

    Node prototype = new Node(Token.NAME, "prototype");
    Node subProperty = new Node(Token.GETPROP, null, prototype, null);
    subProperty.setString("subProperty");

    lhs.addChildToEnd(prototype);
    lhs.addChildToEnd(subProperty);
    prototype.addChildToEnd(node);

    parent.addChildToEnd(lhs);

    assertFalse(checker.shouldTraverse(new NodeTraversal(null), node, parent));
  }

  @Test(expected = AssertionError.class)
  public void testAssignmentToThisKeyWord() {
    Node lhs = new Node(Token.GETPROP, null, null, null);
    lhs.setString("this");

    Node rhs = new Node(Token.THIS, null);

    Node node = new Node(Token.ASSIGN, null, lhs, rhs);

    checker.shouldTraverse(new NodeTraversal(null), node, null);
  }

  @Test(expected = AssertionError.class)
  public void testAssignmentToPrototypeProperty() {
    Node lhs = new Node(Token.GETPROP, null, null, null);
    lhs.setString("prototype");

    Node prototype = new Node(Token.NAME, "prototype");
    lhs.addChildToEnd(prototype);

    Node rhs = new Node(Token.THIS, null);

    Node node = new Node(Token.ASSIGN, null, lhs, rhs);

    checker.shouldTraverse(new NodeTraversal(null), node, null);
  }

  @Test(expected = AssertionError.class)
  public void testAssignmentToPrototypeSubProperty() {
    Node lhs = new Node(Token.GETPROP, null, null, null);
    lhs.setString("prototype");

    Node prototype = new Node(Token.NAME, "prototype");
    Node subProperty = new Node(Token.GETPROP, null, prototype, null);
    subProperty.setString("subProperty");

    lhs.addChildToEnd(prototype);
    lhs.addChildToEnd(subProperty);

    Node rhs = new Node(Token.THIS, null);

    Node node = new Node(Token.ASSIGN, null, lhs, rhs);

    checker.shouldTraverse(new NodeTraversal(null), node, null);
  }

  @Test
  public void testVisitThisKeyword() {
    Node node = new Node(Token.THIS, null);

    Node parent = new Node(Token.NAME, "parent");
    Node lhs = new Node(Token.GETPROP, null, parent, null);
    lhs.setString("property");

    parent.addChildToEnd(lhs);
    parent.addChildToEnd(node);

    new NodeTraversal(null).traverse(node, checker);

    verify(compiler, times(1)).report(eq(node), eq(WARNING), eq(GLOBAL_THIS));
  }

  @Test
  public void testVisitAssignmentToThisKeyword() {
    Node lhs = new Node(Token.GETPROP, null, null, null);
    lhs.setString("this");

    Node rhs = new Node(Token.THIS, null);

    Node node = new Node(Token.ASSIGN, null, lhs, rhs);

    Node parent = new Node(Token.ASSIGN, null, null, null);

    new NodeTraversal(null).traverse(parent, checker);
    parent.addChildToEnd(node);

    new NodeTraversal(null).traverse(node, checker);

    verify(compiler, times(1)).report(eq(lhs), eq(WARNING), eq(GLOBAL_THIS));
  }

  @Test
  public void testVisitAssignmentToPrototypeProperty() {
    Node lhs = new Node(Token.GETPROP, null, null, null);
    lhs.setString("prototype");

    Node prototype = new Node(Token.NAME, "prototype");
    Node rhs = new Node(Token.THIS, null);

    Node node = new Node(Token.ASSIGN, null, lhs, rhs);

    Node parent = new Node(Token.ASSIGN, null, null, null);

    new NodeTraversal(null).traverse(parent, checker);
    parent.addChildToEnd(lhs);
    parent.addChildToEnd(prototype);
    prototype.addChildToEnd(rhs);
    prototype.addChildToEnd(node);

    new NodeTraversal(null).traverse(node, checker);

    verify(compiler, times(1)).report(eq(lhs), eq(WARNING), eq(GLOBAL_THIS));
  }

  @Test
  public void testVisitAssignmentToPrototypeSubProperty() {
    Node lhs = new Node(Token.GETPROP, null, null, null);
    lhs.setString("prototype");

    Node prototype = new Node(Token.NAME, "prototype");
    Node subProperty = new Node(Token.GETPROP, null, prototype, null);
    subProperty.setString("subProperty");

    Node rhs = new Node(Token.THIS, null);

    Node node = new Node(Token.ASSIGN, null, lhs, rhs);

    Node parent = new Node(Token.ASSIGN, null, null, null);

    new NodeTraversal(null).traverse(parent, checker);
    parent.addChildToEnd(lhs);
    parent.addChildToEnd(prototype);
    prototype.addChildToEnd(subProperty);
    subProperty.addChildToEnd(rhs);
    prototype.addChildToEnd(node);

    new NodeTraversal(null).traverse(node, checker);

    verify(compiler, times(1)).report(eq(lhs), eq(WARNING), eq(GLOBAL_THIS));
  }
}