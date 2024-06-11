import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.rhino.Node;

public class PrepareAstTest {

  @Test
  public void testProcessNormalNode() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = Node.objectLiteral();
    prepareAst.process(null, root);
    assertTrue(root.isObjectLit());
  }

  @Test
  public void testProcessWithExterns() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node externs = Node.objectLiteral();
    Node root = Node.objectLiteral();
    prepareAst.process(externs, root);
    assertTrue(root.isObjectLit());
  }

  @Test
  public void testProcessWithCheckOnly() {
    PrepareAst prepareAst = new PrepareAst(null, true);
    Node root = Node.objectLiteral();
    prepareAst.process(null, root);
    assertFalse(prepareAst.checkOnly); // Check-only should throw an error
  }

  @Test
  public void testNormalizeNodeTypesSimpleCase() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = Node.objectLiteral();
    Node child = Node.statement(Node.returnStatement(Node.numberLiteral(1)));
    root.addChild(child);
    prepareAst.process(null, root);
    Node block = (Node) root.getFirstChild();
    assertTrue(block.isBlock());
  }

  @Test(expected = AssertionError.class)
  public void testNormalizeNodeTypesInvalidConstraint() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = Node.objectLiteral();
    Node child = Node.statement(Node.returnStatement(Node.numberLiteral(1)));
    root.addChild(child);
    prepareAst.process(null, root);
    prepareAst.reportChange(); // This should throw an error
  }

  @Test
  public void testNormalizeBlocksSimpleCase() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = Node.expressionStatement(Node.assign(Node.name("x"), Node.numberLiteral(1)));
    prepareAst.process(null, root);
    Node block = (Node) root.getFirstChild();
    assertTrue(block.isBlock());
  }

  @Test
  public void testNormalizeBlocksEmptyCodeBlock() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = Node.objectLiteral();
    prepareAst.process(null, root);
    Node block = (Node) root.getFirstChild();
    assertTrue(block.isBlock());
    assertTrue(block.isEmptyNode());
  }

  @Test
  public void testPrepareAnnotationsSimpleCase() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = Node.objectLiteral();
    Node child = Node.statement(Node.returnStatement(Node.stringLiteral("Hello World")));
    root.addChild(child);
    prepareAst.process(null, root);
    Node function = (Node) child;
    assertEquals("Hello World", function.getJSDocInfo().getTagValue("name"));
  }

  @Test
  public void testPrepareAnnotationsFunctionLiteral() {
    PrepareAst prepareAst = new PrepareAst(null, false);
    Node root = Node.objectLiteral();
    Node functionLiteral = Node.expressionStatement(Node.assign(Node.name("x"), Node.functionLiteral(
        Node.parameter("a"), Node.block(Node.returnStatement(Node.numberLiteral(2))), null)));
    root.addChild(functionLiteral);
    prepareAst.process(null, root);
    Node function = (Node) functionLiteral.getFirstChild();
    assertEquals("x", function.getJSDocInfo().getTagValue("name"));
  }
}