import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.token.TokenType.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private Compiler compiler;
  private CheckGlobalThis checker;
  private Diagnostics diagnostics;

  @Before
  public void setUp() {
    compiler = new Compiler();
    checker = new CheckGlobalThis(compiler, WARNING);
    diagnostics = new Diagnostics();
  }

  // Test cases from Experts 1, 2, and 3
  // ...

  @Test
  public void testFunctionWithBothThisAndConstructorAnnotation() {
    String code = "/** @constructor */ /** @this */ function Test() {}";
    Node root = Node.fromString(code, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertEquals(0, diagnostics.getLength());
  }

  @Test
  public void testAssignmentToPrototypeProperty() {
    String code = "function Test() { Test.prototype.foo = this; }";
    Node root = Node.fromString(code, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertEquals(1, diagnostics.getLength());
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostics.get(0).getType());
  }

  // ...

  @Test
  public void testInvalidThisAssignment() {
    String code = "function Test() { this = 1; }";
    Node root = Node.fromString(code, null, null);
    NodeTraversal traversal = new NodeTraversal(root, checker, diagnostics);
    traversal.traverse();
    assertEquals(1, diagnostics.getLength());
    assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnostics.get(0).getType());
  }

  // ...
}