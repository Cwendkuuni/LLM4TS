import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private NodeTraversal nodeTraversal;
  private Scope scope;
  private ControlFlowAnalysis controlFlowAnalysis;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    nodeTraversal = mock(NodeTraversal.class);
    scope = mock(Scope.class);
    controlFlowAnalysis = mock(ControlFlowAnalysis.class);

    when(compiler.getTraversal()).thenReturn(nodeTraversal);
    when(nodeTraversal.getScope()).thenReturn(scope);
    when(controlFlowAnalysis.process(nodeTraversal, scope.getRootNode())).thenReturn(new ControlFlowGraph<>());
  }

  @Test
  public void testUnreachableCodeElimination() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);

    Node root = createNode("function foo() { return; alert('unreachable'); }");

    unreachableCodeElimination.process(null, root);

    Node expectedResult = createNode("function foo() { return; }");

    Node actualResult = nodeTraversal.getCurrentNode();

    assertThat(actualResult.toString()).isEqualTo(expectedResult.toString());
  }

  @Test
  public void testNoOpStatementRemoval() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(mock(AbstractCompiler.class), false);

    Node root = createNode("function foo() { a = b.MyClass.prototype.propertyName; return; alert('unreachable'); }");

    unreachableCodeElimination.process(null, root);

    Node expectedResult = createNode("function foo() { a = b.MyClass.prototype.propertyName; }");

    Node actualResult = nodeTraversal.getCurrentNode();

    assertThat(actualResult.toString()).isEqualTo(expectedResult.toString());
  }

  @Test
  public void testUnconditionalBranchingRemoval() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(mock(AbstractCompiler.class), true);

    Node root = createNode("function foo() { if (false) { break; } break; alert('unreachable'); }");

    unreachableCodeElimination.process(null, root);

    Node expectedResult = createNode("function foo() { }");

    Node actualResult = nodeTraversal.getCurrentNode();

    assertThat(actualResult.toString()).isEqualTo(expectedResult.toString());
  }

  @Test
  public void testNestedBreaks() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(mock(AbstractCompiler.class), true);

    Node root = createNode("function foo() {"
        + " if (false) {"
        + "   break;"
        + " }"
        + " if (false) {"
        + "   break;"
        + " }"
        + " alert('unreachable');"
        + "}");

    unreachableCodeElimination.process(null, root);

    Node expectedResult = createNode("function foo() { }");

    Node actualResult = nodeTraversal.getCurrentNode();

    assertThat(actualResult.toString()).isEqualTo(expectedResult.toString());
  }

  @Test
  public void testNestedReturns() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(mock(AbstractCompiler.class), true);

    Node root = createNode("function foo() {"
        + " if (false) {"
        + "   return;"
        + " }"
        + " if (false) {"
        + "   return;"
        + " }"
        + " return;"
        + " alert('unreachable');"
        + "}");

    unreachableCodeElimination.process(null, root);

    Node expectedResult = createNode("function foo() { return; }");

    Node actualResult = nodeTraversal.getCurrentNode();

    assertThat(actualResult.toString()).isEqualTo(expectedResult.toString());
  }

  @Test
  public void testNoOpStatementInsideLoop() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(mock(AbstractCompiler.class), true);

    Node root = createNode("function foo() {"
        + " for (int i = 0; i < 10; i++) {"
        + "   a = b.MyClass.prototype.propertyName;"
        + " }"
        + " alert('unreachable');"
        + "}");

    unreachableCodeElimination.process(null, root);

    Node expectedResult = createNode("function foo() {"
        + " for (int i = 0; i < 10; i++) { }"
        + "}");

    Node actualResult = nodeTraversal.getCurrentNode();

    assertThat(actualResult.toString()).isEqualTo(expectedResult.toString());
  }

  @Test
  public void testUnreachableCodeEliminationWithConditionalBranching() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(mock(AbstractCompiler.class), true);

    Node root = createNode("function foo() {"
        + " if (true) {"
        + "   return;"
        + " } else {"
        + "   alert('unreachable');"
        + " }"
        + "}");

    unreachableCodeElimination.process(null, root);

    Node expectedResult = createNode("function foo() { return; }");

    Node actualResult = nodeTraversal.getCurrentNode();

    assertThat(actualResult.toString()).isEqualTo(expectedResult.toString());
  }

  @Test
  public void testUnreachableCodeWithFunctionCall() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(mock(AbstractCompiler.class), true);

    Node root = createNode("function foo() {"
        + " alert('unreachable');"
        + " someFunction();"
        + "}");

    unreachableCodeElimination.process(null, root);

    Node expectedResult = createNode("function foo() { someFunction(); }");

    Node actualResult = nodeTraversal.getCurrentNode();

    assertThat(actualResult.toString()).isEqualTo(expectedResult.toString());
  }

  @Test
  public void testBreakStatementWithLabel() {
    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(mock(AbstractCompiler.class), true);

    Node root = createNode("function foo() {"
        + " outer:"
        + " while (true) {"
        + "   if (false) {"
        + "     break outer;"
        + "   }"
        + "   alert('unreachable');"
        + " }"
        + "}");

    unreachableCodeElimination.process(null, root);

    Node expectedResult = createNode("function foo() { }");

    Node actualResult = nodeTraversal.getCurrentNode();

    assertThat(actualResult.toString()).isEqualTo(expectedResult.toString());
  }

  private Node createNode(String nodeContent) {
    List<Node> children = new LinkedList<>();
    Node node = new Node(Token.SCRIPT, 0, nodeContent, 0, null, null, null, null, null);
    if (nodeContent.contains("{")) {
      int index = nodeContent.indexOf("{");
      String subContent = nodeContent.substring(index);
      Node blockNode = new Node(Token.BLOCK, 0, subContent, index, null, null, null, null, null);
      node.setFirstChild(blockNode);
      children.add(blockNode);
    }
    if (nodeContent.contains("function")) {
      int functionIndex = nodeContent.indexOf("function");
      String functionName = nodeContent.substring(functionIndex, nodeContent.indexOf("("));
      Node functionNode = new Node(Token.FUNCTION, 0, functionName, functionIndex, null, null, null, null, null);
      node.setFirstChild(functionNode);
      children.add(functionNode);
    }
    node.setChildren(children.toArray(new Node[0]));

    return node;
  }
}