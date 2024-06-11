import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.PrepareAst.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

@RunWith(JUnit4.class)
public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node root = NodeFactory.createCall(Token.NO_SOURCE_FILE, "evaluate", null);
    NodeTraversal.traverse(compiler, root, new PrepareAst(compiler));

    Node block = NodeFactory.createBlock("test");
    Node statement = NodeFactory.createExpressionStatement(root);
    block.addChild(statement);

    assertEquals(Node.BLOCK, block.getType(), "Block should be a block");
    assertEquals(Node.CALL, root.getType(), "Root should be a call");
    assertEquals(Node.EXPR_RESULT, root.getFirstChild().getType(),
        "First child of root should be EXPR_RESULT");
    assertEquals(Node.DIRECT_EVAL, root.getFirstChild().getFirstChild().getBooleanProp(Node.DIRECT_EVAL),
        "First child of root should have DIRECT_EVAL property");

    Preconditions.checkState(statement.getParent() == block, "statement parent");

    NodeTraversal.traverse(compiler, block, new PrepareAst(compiler, true));

    assertEquals(Node.EXPR_VOID, root.getFirstChild().getType(),
        "First child of root should be EXPR_VOID");
  }

  @Test
  public void testNormalizeBlocks() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node root = NodeFactory.createCall(Token.NO_SOURCE_FILE, "foo", null);

    Node ifNode = NodeFactory.createIf(
        NodeFactory.createBinaryOp(Token.NO_SOURCE_FILE, Token.IF,
            NodeFactory.createIdentifier("x"), NodeFactory.createLiteral(5)),
        NodeFactory.createCall(Token.NO_SOURCE_FILE, "bar", null), null);

    root.addChild(ifNode);

    NodeTraversal.traversalStart(compiler, root);
    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);
    NodeTraversal.traversalDone();

    Node ifBlock = ifNode.getFirstChild();
    Node ifStatement = ifBlock.getFirstChild();

    assertEquals(Node.BLOCK, ifBlock.getType(), "ifBlock should be a block");
    assertEquals(Node.EXPR_STATEMENT, ifStatement.getType(),
        "ifStatement should be an expr statement");
    assertEquals("bar", ifStatement.getLastChild().getIdentifier().getName(),
        "if statement should call bar");

    Node elseBlock = ifNode.getLastChild();
    Node elseStatement = elseBlock.getFirstChild();

    assertEquals(Node.BLOCK, elseBlock.getType(), "elseBlock should be a block");
    assertEquals(Node.EXPR_STATEMENT, elseStatement.getType(),
        "else statement should be an expr statement");
  }

  @Test
  public void testPrepareAnnotations() {
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node root = NodeFactory.createCall(Token.NO_SOURCE_FILE, "evaluate", null);

    Node objLit = NodeFactory.createObjectLiteral();
    Node property = NodeFactory.createProperty("a", NodeFactory.createString("b"));
    objLit.addChild(property);

    root.addChild(objLit);

    NodeTraversal.traversalStart(compiler, root);
    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);
    NodeTraversal.traversalDone();

    Node propertyValue = property.getLastChild();

    assertEquals("b", propertyValue.getString(), "Property value should be 'b'");
    assertEquals("a", property.getFirstChild().getIdentifier().getName(),
        "Property key should be 'a'");
  }
}