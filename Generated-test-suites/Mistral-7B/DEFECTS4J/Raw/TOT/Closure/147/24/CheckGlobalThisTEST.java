import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Checker checker;
  private Parser parser;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new CompilerOptions().getChecker();
    checker = new Checker(compiler);
    parser = new Parser();
    nodeFactory = new NodeFactory();
  }

  @Test
  public void testSafeUsage() {
    String code = "function Foo() {}";
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertEquals(0, checker.getNumErrors());
  }

  @Test
  public void testUnsafeAssignment() {
    String code = "var x = this;";
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertEquals(1, checker.getNumErrors());
    DiagnosticType warning = checker.getError(0).getType();
    assertEquals(DiagnosticType.WARNING, warning);
    assertEquals("JSC_USED_GLOBAL_THIS", warning.getId());
  }

  @Test
  public void testUnsafeMethodCall() {
    String code = "function Foo() { this.bar(); }";
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertEquals(1, checker.getNumErrors());
    DiagnosticType warning = checker.getError(0).getType();
    assertEquals(DiagnosticType.WARNING, warning);
    assertEquals("JSC_USED_GLOBAL_THIS", warning.getId());
  }

  @Test
  public void testUnsafeConstructorCall() {
    String code = "function Foo() {} new Foo();";
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertEquals(1, checker.getNumErrors());
    DiagnosticType warning = checker.getError(0).getType();
    assertEquals(DiagnosticType.WARNING, warning);
    assertEquals("JSC_USED_GLOBAL_THIS", warning.getId());
  }

  @Test
  public void testSafeMethodCall() {
    String code = "function Foo() {} Foo.prototype.bar = function() {};";
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertEquals(0, checker.getNumErrors());
  }

  @Test
  public void testSafeConstructorCall() {
    String code = "function Foo() {} function FooBar() extends Foo {}";
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertEquals(0, checker.getNumErrors());
  }

  @Test
  public void testNoCheck() {
    String code = "checkGlobalThis = null;";
    Node ast = parser.parse(code, null, null);
    NodeTraversal traversal = new NodeTraversal(ast, checker);
    traversal.traverse();
    assertEquals(0, checker.getNumErrors());
  }
}