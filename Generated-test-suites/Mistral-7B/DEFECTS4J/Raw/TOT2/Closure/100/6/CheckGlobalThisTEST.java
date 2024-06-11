import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.JSDocInfo;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckerFactory checkerFactory;
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    checkerFactory = new CheckerFactory();
    compiler = checkerFactory.newCompiler();
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  @Test
  public void testNoGlobalThis() {
    Node root = Parser.parseJs("function foo() {};");
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAllChildren();
    assertFalse(checker.assignLhsChild == null);
  }

  // Add tests for edge cases and error scenarios here...

  // Basic test case for constructor function
  @Test
  public void testConstructorFunction() {
    Node root = Parser.parseJs("function Foo() {}");
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAllChildren();
    assertFalse(checker.compiler.getDiagnostics().getDiagnostics().isEmpty());
  }

  // Basic test case for prototype method
  @Test
  public void testPrototypeMethod() {
    Node root = Parser.parseJs("var Foo = function() {}; Foo.prototype.bar = function() {};");
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAllChildren();
    assertFalse(checker.compiler.getDiagnostics().getDiagnostics().isEmpty());
  }

  // Basic test case for function annotated with @this
  @Test
  public void testThisAnnotation() {
    Node root = Parser.parseJs("/** @this {MyType} */ function foo() {}");
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAllChildren();
    assertFalse(checker.compiler.getDiagnostics().getDiagnostics().isEmpty());
  }

  // Basic test case for function where there's no logical place to put a @this annotation
  @Test
  public void testNoPlaceForThisAnnotation() {
    Node root = Parser.parseJs("function foo() { this.bar = 1; }");
    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverseAllChildren();
    assertFalse(checker.compiler.getDiagnostics().getDiagnostics().isEmpty());
  }
}