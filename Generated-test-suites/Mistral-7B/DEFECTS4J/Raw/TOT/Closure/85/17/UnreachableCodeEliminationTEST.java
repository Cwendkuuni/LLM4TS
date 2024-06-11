import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.GLOBAL;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;

  @Before
  public void setup() {
    options = new CompilerOptions();
    compiler = new AbstractCompiler(options);
  }

  @Test
  public void testUnreachableCodeElimination() {
    String jsCode =
        "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  } else {" +
        "    alert('should not be called');" +
        "  }" +
        "  alert('this should be eliminated');" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    assertNull(foo.getFirstChild());
  }

  @Test
  public void testNoOpStatement() {
    String jsCode =
        "function foo() {" +
        "  return;" +
        "  alert('should not be called');" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    assertNull(foo.getFirstChild());
  }

  @Test
  public void testPrototypeProperty() {
    String jsCode =
        "function MyClass() {}" +
        "MyClass.prototype.property = 1;" +
        "function foo() {" +
        "  return;" +
        "  MyClass.prototype.property;" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    Node propertyNode = foo.getFirstChild();
    assertEquals(Token.GETPROP, propertyNode.getType());
    assertEquals("property", propertyNode.getProperties().getName());
  }

  @Test
  public void testUnreachableBranchingStatement() {
    String jsCode =
        "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i > 5) {" +
        "      break;" +
        "    } else {" +
        "      return;" +
        "    }" +
        "  }" +
        "  alert('this should be eliminated');" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    assertNull(foo.getFirstChild());
  }

  @Test
  public void testBreakStatementInLoop() {
    String jsCode =
        "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i > 5) {" +
        "      break;" +
        "    } else {" +
        "      return;" +
        "    }" +
        "  }" +
        "  alert('should not be called');" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    assertNull(foo.getFirstChild());
  }

  @Test
  public void testContinueStatementInLoop() {
    String jsCode =
        "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 == 0) {" +
        "      continue;" +
        "    } else {" +
        "      return;" +
        "    }" +
        "  }" +
        "  alert('should not be called');" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    assertNull(foo.getFirstChild());
  }

  @Test
  public void testReturnStatementInLoop() {
    String jsCode =
        "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i % 2 == 0) {" +
        "      return;" +
        "    }" +
        "  }" +
        "  alert('should not be called');" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    assertNull(foo.getFirstChild());
  }

  @Test
  public void testReturnStatementInIf() {
    String jsCode =
        "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  } else {" +
        "    alert('should not be called');" +
        "  }" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    assertNull(foo.getFirstChild());
  }

  @Test
  public void testIfStatementWithElse() {
    String jsCode =
        "function foo() {" +
        "  if (true) {" +
        "    return;" +
        "  } else {" +
        "    alert('should be called');" +
        "  }" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    Node elseBlock = foo.getFirstChild().getFirstChild();
    assertEquals("alert", elseBlock.getProperties().getName());
  }

  @Test
  public void testComplexControlFlowWithSwitch() {
    String jsCode =
        "function foo() {" +
        "  switch (Math.random()) {" +
        "    case 0.1: return;" +
        "    case 0.2: return;" +
        "    case 0.3: return;" +
        "    default: alert('should not be called');" +
        "  }" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    assertNull(foo.getFirstChild());
  }

  @Test
  public void testComplexControlFlowWithNestedIf() {
    String jsCode =
        "function foo() {" +
        "  if (Math.random() < 0.5) {" +
        "    if (Math.random() < 0.3) {" +
        "      return;" +
        "    }" +
        "  } else {" +
        "    alert('should not be called');" +
        "  }" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    assertNull(foo.getFirstChild());
  }

  @Test
  public void testComplexControlFlowWithNestedIfElse() {
    String jsCode =
        "function foo() {" +
        "  if (Math.random() < 0.5) {" +
        "    if (Math.random() < 0.3) {" +
        "      return;" +
        "    } else {" +
        "      alert('should not be called');" +
        "    }" +
        "  } else {" +
        "    alert('should not be called');" +
        "  }" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    assertNull(foo.getFirstChild());
  }

  @Test
  public void testComplexControlFlowWithNestedSwitch() {
    String jsCode =
        "function foo() {" +
        "  switch (Math.random()) {" +
        "    case 0.1: " +
        "      switch (Math.random()) {" +
        "        case 0.2: return;" +
        "        case 0.3: return;" +
        "        default: continue;" +
        "      }" +
        "    case 0.4: return;" +
        "    default: alert('should not be called');" +
        "  }" +
        "}";

    Node rootNode = NodeUtil.parseJs(jsCode);
    new NodeTraversal(compiler, rootNode, GLOBAL).traverse();

    Node foo = NodeUtil.lookupName(rootNode, "foo");
    assertNull(foo.getFirstChild());
  }
}