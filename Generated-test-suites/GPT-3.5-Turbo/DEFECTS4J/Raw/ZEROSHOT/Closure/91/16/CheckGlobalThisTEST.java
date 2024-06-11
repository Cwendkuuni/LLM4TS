import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CheckGlobalThisTest {
  
  private AbstractCompiler mockedCompiler;
  private CheckLevel mockedLevel;
  private NodeTraversal.Callback callback;

  @Before
  public void setUp() {
    mockedCompiler = Mockito.mock(AbstractCompiler.class);
    mockedLevel = Mockito.mock(CheckLevel.class);
    callback = new CheckGlobalThis(mockedCompiler, mockedLevel);
  }

  @Test
  public void testShouldTraverse_withFunctionConstructor() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.FUNCTION);
    Node parent = Mockito.mock(Node.class);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);

    n.setJSDocInfo(jsDoc);
    Mockito.when(parent.getType()).thenReturn(Token.NAME);

    boolean result = callback.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_withFunctionThisAnnotation() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.FUNCTION);
    Node parent = Mockito.mock(Node.class);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();

    n.setJSDocInfo(jsDoc);
    Mockito.when(parent.getType()).thenReturn(Token.NAME);

    boolean result = callback.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_withFunctionNoLogicalPlaceForThisAnnotation() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.FUNCTION);
    Node parent = Mockito.mock(Node.class);

    JSDocInfo jsDoc = null;
    n.setJSDocInfo(jsDoc);
    Mockito.when(parent.getType()).thenReturn(Token.NAME);

    boolean result = callback.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_withNonFunctionType() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.STRING);
    Node parent = Mockito.mock(Node.class);

    JSDocInfo jsDoc = null;
    n.setJSDocInfo(jsDoc);
    Mockito.when(parent.getType()).thenReturn(Token.NAME);

    boolean result = callback.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_withFunctionNotTraversable() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.FUNCTION);
    Node parent = Mockito.mock(Node.class);

    JSDocInfo jsDoc = null;
    n.setJSDocInfo(jsDoc);
    Mockito.when(parent.getType()).thenReturn(Token.NAME);

    boolean result = callback.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void testVisit_withThisNodeOnLeftSideOfAssign() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    boolean result = callback.shouldReportThis(n, parent);

    assertTrue(result);
  }

  @Test
  public void testVisit_withThisNodeOnRightSideOfAssignWithPrototypeProperty() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);

    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    lhs.addChildToBack(new Node(Token.NAME));
    lhs.getLastChild().setString("prototype");
    parent.addChildToBack(lhs);

    boolean result = callback.shouldReportThis(n, parent);

    assertFalse(result);
  }

  @Test
  public void testVisit_withThisNodeOnRightSideOfAssignWithLentPrototype() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);

    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    Node llhs = new Node(Token.GETPROP);
    llhs.addChildToBack(new Node(Token.NAME));
    llhs.getLastChild().setString("prototype");
    lhs.addChildToBack(llhs);
    parent.addChildToBack(lhs);

    boolean result = callback.shouldReportThis(n, parent);

    assertFalse(result);
  }

  @Test
  public void testVisit_withThisNodeOnRightSideOfAssign() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    boolean result = callback.shouldReportThis(n, parent);

    assertTrue(result);
  }

  @Test
  public void testVisit_withLhsAssignment() {
    NodeTraversal t = Mockito.mock(NodeTraversal.class);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);

    callback.visit(t, n, parent);

    Mockito.verify(mockedCompiler).report(Mockito.any());
  }
}