import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;

  @org.junit.Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public Node parse(String source, int options) {
        Parser parser = new Parser(source);
        return parser.parse(null, null, null);
      }
    };
    checker = new Checker(compiler, new ErrorReporter());
  }

  /**
   * Test method for {@link CheckGlobalThis#shouldTraverse(NodeTraversal, Node, Node)}
   */
  @org.junit.Test
  public void testShouldTraverse() {
    String source = "function unsafeFunction() { this.property = 'global'; }";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertTrue(checker.shouldReportThis(checker.assignLhsChild, node));
  }

  /**
   * Test method for {@link CheckGlobalThis#shouldTraverse(NodeTraversal, Node, Node)}
   * with a constructor function
   */
  @org.junit.Test
  public void testShouldTraverseConstructor() {
    String source = "function MyConstructor() {}";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertFalse(checker.shouldReportThis(null, node));
  }

  /**
   * Test method for {@link CheckGlobalThis#shouldTraverse(NodeTraversal, Node, Node)}
   * with a function annotated with @this
   */
  @org.junit.Test
  public void testShouldTraverseAnnotatedFunction() {
    String source = "/**@this { type: 'MyType' } */ function safeFunction() {}";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertFalse(checker.shouldReportThis(null, node));
  }

  /**
   * Test method for {@link CheckGlobalThis#shouldReportThis(Node, Node)} with a
   * function annotated with @constructor
   */
  @org.junit.Test
  public void testShouldReportThisWithConstructor() {
    String source = "function MyConstructor() {}";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    Node assignNode = new Node(Token.ASSIGN);
    Node leftSide = new Node(Token.THIS);
    Node rightSide = new Node(Token.IDENT, "property");

    assignNode.addChildren(leftSide, rightSide);
    node.addChild(assignNode);

    traversal.traverse(node);

    assertTrue(checker.shouldReportThis(leftSide, node));
  }

  /**
   * Test method for {@link CheckGlobalThis#shouldReportThis(Node, Node)} with a
   * property access
   */
  @org.junit.Test
  public void testShouldReportThisWithPropertyAccess() {
    String source = "function unsafeFunction() { this.prototype.property = 'global'; }";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertTrue(checker.shouldReportThis(node, node));
  }

  /**
   * Test method for {@link CheckGlobalThis#shouldReportThis(Node, Node)} with a
   * prototype property access
   */
  @org.junit.Test
  public void testShouldReportThisWithPrototypePropertyAccess() {
    String source = "function unsafeFunction() { this.prototype.prototype.property = 'global'; }";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertTrue(checker.shouldReportThis(node, node));
  }

  /**
   * Test method for {@link CheckGlobalThis#test(NodeTraversal, Node, Node)}
   */
  @org.junit.Test
  public void testCheckGlobalThis() {
    String source = "function unsafeFunction() { this.property = 'global'; }";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object", checker.compiler.getDiagnostics().get(0).getMessage());
  }

  /**
   * Test method for {@link CheckGlobalThis#test(NodeTraversal, Node, Node)} with a
   * constructor function
   */
  @org.junit.Test
  public void testCheckGlobalThisWithConstructor() {
    String source = "function MyConstructor() {}";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertEquals(0, checker.compiler.getDiagnostics().size());
  }

  /**
   * Test method for {@link CheckGlobalThis#test(NodeTraversal, Node, Node)} with a
   * function annotated with @this
   */
  @org.junit.Test
  public void testCheckGlobalThisAnnotatedFunction() {
    String source = "/**@this { type: 'MyType' } */ function safeFunction() {}";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertEquals(0, checker.compiler.getDiagnostics().size());
  }

  /**
   * Test method for {@link CheckGlobalThis#test(NodeTraversal, Node, Node)} with a
   * function annotated with @constructor
   */
  @org.junit.Test
  public void testCheckGlobalThisWithConstructorAnnotation() {
    String source = "function MyConstructor() {}";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    Node assignNode = new Node(Token.ASSIGN);
    Node leftSide = new Node(Token.THIS);
    Node rightSide = new Node(Token.IDENT, "property");

    assignNode.addChildren(leftSide, rightSide);
    node.addChild(assignNode);

    traversal.traverse(node);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object", checker.compiler.getDiagnostics().get(0).getMessage());
  }

  /**
   * Test method for {@link CheckGlobalThis#test(NodeTraversal, Node, Node)} with a
   * property access
   */
  @org.junit.Test
  public void testCheckGlobalThisWithPropertyAccess() {
    String source = "function unsafeFunction() { this.prototype.property = 'global'; }";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object", checker.compiler.getDiagnostics().get(0).getMessage());
  }

  /**
   * Test method for {@link CheckGlobalThis#test(NodeTraversal, Node, Node)} with a
   * prototype property access
   */
  @org.junit.Test
  public void testCheckGlobalThisWithPrototypePropertyAccess() {
    String source = "function unsafeFunction() { this.prototype.prototype.property = 'global'; }";
    Node node = compiler.parse(source, 0);
    CheckGlobalThis checker = new CheckGlobalThis(checker, WARNING);
    NodeTraversal traversal = new NodeTraversal(checker);

    traversal.traverse(node);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object", checker.compiler.getDiagnostics().get(0).getMessage());
  }
}