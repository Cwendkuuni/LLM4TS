import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void shouldReportGlobalThisWhenAssignedToVariable() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);

    t.enterScope();
    checkGlobalThis.assignLhsChild = parent.getFirstChild();
    boolean result = checkGlobalThis.shouldReportThis(n);

    assertTrue(result);
  }

  @Test
  public void shouldReportGlobalThisWithPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    t.enterScope();
    parent.addChildToFront(new Node(Token.NAME, "property"));
    parent.addChildToFront(n);

    boolean result = checkGlobalThis.shouldReportThis(n);

    assertTrue(result);
  }

  @Test
  public void shouldNotReportGlobalThisWhenNotAssignedToVariableOrPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.LT);

    t.enterScope();
    boolean result = checkGlobalThis.shouldReportThis(n);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseFunctionWithConstructorAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.LP);

    Node jsDocNode = new Node(Token.JSDOC);
    Node constructorAnnotation = new Node(Token.OTHER, "@constructor");

    jsDocNode.addChildToFront(constructorAnnotation);
    n.addChildToFront(jsDocNode);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseFunctionWithThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.LP);

    Node jsDocNode = new Node(Token.JSDOC);
    Node thisAnnotation = new Node(Token.OTHER, "@this");

    jsDocNode.addChildToFront(thisAnnotation);
    n.addChildToFront(jsDocNode);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseFunctionsWithoutLogicalPlaceForThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.LP);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverseFunctionInBlockParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void shouldTraverseFunctionInScriptParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void shouldTraverseFunctionInNameParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }

  @Test
  public void shouldTraverseFunctionInAssignParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertTrue(result);
  }
  
  @Test
  public void shouldNotTraverseFunctionInOtherParent() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ADD);

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }

  @Test
  public void shouldNotReportGlobalThisWhenNotEncountered() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.LP);

    checkGlobalThis.assignLhsChild = null;
    checkGlobalThis.shouldReportThis(n);

    t.enterScope();
    boolean result = checkGlobalThis.shouldReportThis(n);

    assertFalse(result);
  }

  @Test
  public void shouldReportGlobalThisWhenEncounteredAndDoesNotHaveDescendantAssign() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.LP);

    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(parent.getFirstChild());

    checkGlobalThis.assignLhsChild = assignNode.getFirstChild();
    checkGlobalThis.shouldReportThis(n);

    t.enterScope();
    boolean result = checkGlobalThis.shouldReportThis(n);

    assertTrue(result);
  }

  @Test
  public void shouldReportGlobalThisWhenEncounteredAndHasDescendantAssign() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.LP);

    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(parent.getFirstChild());

    checkGlobalThis.assignLhsChild = parent.getFirstChild();
    checkGlobalThis.shouldReportThis(n);

    t.enterScope();
    boolean result = checkGlobalThis.shouldReportThis(n);

    assertTrue(result);
  }
  
  @Test
  public void shouldNotReportGlobalThisWhenNotThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);

    checkGlobalThis.assignLhsChild = null;
    checkGlobalThis.shouldReportThis(n);

    t.enterScope();
    boolean result = checkGlobalThis.shouldReportThis(n);

    assertFalse(result);
  }

}