import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Preconditions;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = Node.objectLiteral();
    prepareAst.process(null, root);
    assertFalse(root.isLabel());
    assertFalse(root.isSwitch());
    assertTrue(root.isObjectLit());

    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return true;
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isBlock() && !n.isEmpty()) {
          fail("Unexpected block node found.");
        }
      }
    });
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessWithNullRoot() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, null);
  }

  @Test
  public void testNormalizeNodeTypes() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = Node.objectLiteral();
    prepareAst.process(null, root);

    Node child1 = Node.objectLiteral();
    root.addChild(child1);

    prepareAst.normalizeNodeTypes(root);

    Node expectedBlock = IR.block().srcref(root);
    assertEquals(expectedBlock, root.getChild(0));
    assertTrue(expectedBlock.isBlock());
    assertFalse(expectedBlock.isEmpty());

    Node child2 = IR.block().srcref(child1);
    assertEquals(child2, child1.getFirstChild());

    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return true;
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isBlock() && !n.isEmpty()) {
          fail("Unexpected block node found.");
        }
      }
    });
  }

  @Test
  public void testProcessWithCheckOnly() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler, true);

    Node root = Node.objectLiteral();
    prepareAst.process(null, root);

    try {
      prepareAst.normalizeNodeTypes(root);
      fail("Expected an exception.");
    } catch (IllegalStateException e) {
      // Expected.
    }
  }

  @Test
  public void testNormalizeBlocks() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = Node.ifStatement(
        Node.booleanLiteral(true),
        Node.block(),
        Node.block());

    prepareAst.process(null, root);

    Node ifStatement = root;
    assertNotNull(ifStatement.getFirstChild());
    assertEquals(NodeType.BLOCK, ifStatement.getFirstChild().getType());

    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return true;
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isBlock() && n.isEmpty()) {
          fail("Unexpected empty block node found.");
        }
      }
    });
  }

  @Test
  public void testPrepareAnnotations() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = Node.objectLiteral();
    prepareAst.process(null, root);

    Node property1 = Node.name("p1");
    Node value1 = Node.stringLiteral("v1");
    root.addChild(property1);
    root.addChild(value1);

    Node property2 = Node.name("p2");
    Node value2 = Node.functionExpression(
        Node.name("f"),
        Node.block(),
        Node.emptyStatementList());
    root.addChild(property2);
    root.addChild(value2);

    prepareAst.process(null, root);

    Node expectedValue1 = Node.objectLiteral();
    expectedValue1.setJSDocInfo(Node.createJSDocInfo("@this {a:number}", null));
    assertEquals(expectedValue1, value1);

    Node expectedValue2 = Node.functionExpression(
        Node.name("f"),
        Node.block(),
        Node.emptyStatementList());
    expectedValue2.setJSDocInfo(Node.createJSDocInfo("@this {a:number}", null));
    assertEquals(expectedValue2, value2);
  }

  @Test
  public void testPrepareAnnotationsWithExplicitThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = Node.objectLiteral();
    prepareAst.process(null, root);

    Node property1 = Node.name("p1");
    Node value1 = Node.functionExpression(
        Node.name("f"),
        Node.block(),
        Node.emptyStatementList());
    value1.addChild(Node.name("this"));
    root.addChild(property1);
    root.addChild(value1);

    prepareAst.process(null, root);

    Node expectedValue1 = Node.functionExpression(
        Node.name("f"),
        Node.block(),
        Node.emptyStatementList());
    expectedValue1.setJSDocInfo(Node.createJSDocInfo("@this {a:number}", null));
    assertEquals(expectedValue1, value1);
  }

  @Test
  public void testPrepareAnnotationsWithJavaDispatch() {
    AbstractCompiler compiler = new AbstractCompiler();
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = Node.objectLiteral();
    prepareAst.process(null, root);

    Node property1 = Node.name("p1");
    Node value1 = Node.objectLiteral();
    root.addChild(property1);
    root.addChild(value1);

    Node property2 = Node.name("p2");
    Node dispatcher = Node.functionExpression(
        Node.name("f"),
        Node.block(),
        Node.emptyStatementList());
    dispatcher.putBooleanProp(Node.IS_DISPATCHER, true);
    value1.addChild(property2);
    value1.addChild(dispatcher);

    prepareAst.process(null, root);

    Node expectedDispatcher = Node.functionExpression(
        Node.name("f"),
        Node.block(),
        Node.emptyStatementList());
    expectedDispatcher.putBooleanProp(Node.IS_DISPATCHER, true);
    assertEquals(expectedDispatcher, dispatcher);
  }
}