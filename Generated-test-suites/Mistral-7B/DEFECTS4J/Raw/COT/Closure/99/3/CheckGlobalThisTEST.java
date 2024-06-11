import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class CheckGlobalThisTest {

  @Mock private AbstractCompiler compiler;
  @Mock private NodeTraversal traversal;
  @Mock private Diagnostics diagnostics;
  @Mock private NodeUtil nodeUtil;

  @InjectMocks private CheckGlobalThis checker;

  private CheckLevel level;

  @Before
  public void setUp() {
    level = new CheckLevel(WARNING);
  }

  @Test
  public void testShouldTraverse_functionAnnotatedWithConstructor() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(parent);
    JSDocInfo jsDoc = Mockito.mock(JSDocInfo.class);
    Mockito.when(jsDoc.isConstructor()).thenReturn(true);
    Mockito.when(node.getJSDocInfo()).thenReturn(jsDoc);

    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverse_functionAnnotatedWithThis() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(parent);
    JSDocInfo jsDoc = Mockito.mock(JSDocInfo.class);
    Mockito.when(jsDoc.hasThisType()).thenReturn(true);
    Mockito.when(node.getJSDocInfo()).thenReturn(jsDoc);

    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverse_functionWithNoAnnotationAndParentIsAssign() {
    Node node = Mockito.mock(Node.class);
    Node assignParent = Mockito.mock(Node.class);
    Node lhs = Mockito.mock(Node.class);
    Node rhs = Mockito.mock(Node.class);

    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(assignParent);
    Mockito.when(assignParent.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(assignParent.getFirstChild()).thenReturn(lhs);
    Mockito.when(lhs.getType()).thenReturn(Token.NAME);
    Mockito.when(rhs.getType()).thenReturn(Token.NULL);
    Mockito.when(assignParent.getLastChild()).thenReturn(rhs);

    assertTrue(checker.shouldTraverse(traversal, node, assignParent));
  }

  @Test
  public void testShouldTraverse_functionWithNoAnnotationAndParentIsFunctionOrScript() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);

    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(parent);
    Mockito.when(parent.getType()).thenReturn(Token.SCRIPT); // or Token.FUNCTION

    assertTrue(checker.shouldTravere(traversal, node, parent));
  }

  @Test
  public void testShouldTraverse_functionWithNoAnnotationAndParentIsGetPropWithPrototype() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    Node getProp = Mockito.mock(Node.class);

    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(parent);
    Mockito.when(parent.getType()).thenReturn(Token.GETPROP);
    Mockito.when(parent.getLastChild()).thenReturn(getProp);
    Mockito.when(getProp.getString()).thenReturn("prototype");

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverse_functionWithNoAnnotationAndParentIsGetProp() {
    Node node = Mockito.mock(Node.class);
    Node parent = Mockito.mock(Node.class);
    Node getProp = Mockito.mock(Node.class);

    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(parent);
    Mockito.when(parent.getType()).thenReturn(Token.GETPROP);
    Mockito.when(parent.getLastChild()).thenReturn(getProp);

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverse_functionWithNoAnnotationAndParentIsAssignWithLhsNotPrototypeOrGetProp() {
    Node node = Mockito.mock(Node.class);
    Node assignParent = Mockito.mock(Node.class);
    Node lhs = Mockito.mock(Node.class);
    Node rhs = Mockito.mock(Node.class);

    Mockito.when(node.getType()).thenReturn(Token.FUNCTION);
    Mockito.when(node.getParent()).thenReturn(assignParent);
    Mockito.when(assignParent.getType()).thenReturn(Token.ASSIGN);
    Mockito.when(assignParent.getFirstChild()).thenReturn(lhs);
    Mockito.when(lhs.getType()).thenReturn(Token.NAME);

    assertTrue(checker.shouldTraverse(traversal, node, assignParent));
  }

  // TODO: Add more test cases for the remaining methods and edge cases.
}