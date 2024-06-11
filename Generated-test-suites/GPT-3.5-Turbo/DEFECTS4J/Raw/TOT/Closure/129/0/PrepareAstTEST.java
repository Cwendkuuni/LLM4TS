import static org.junit.Assert.*;

import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {

  private PrepareAst prepareAst;

  @Before
  public void setUp() {
    prepareAst = new PrepareAst(null);
  }

  // Test normalizeNodeTypes()
  @Test
  public void testNormalizeNodeTypes() {
    Node root = IR.root();
    Node child = IR.block().srcref(root);
    root.addChildToBack(child);

    prepareAst.normalizeNodeTypes(root);

    assertTrue(child.isBlock());
  }

  // Test normalizeBlocks()
  @Test
  public void testNormalizeBlocks() {
    Node controlStructure = IR.whileNode(IR.trueNode(), IR.block().srcref(controlStructure)).srcref(controlStructure);
    
    prepareAst.normalizeBlocks(controlStructure);

    assertTrue(controlStructure.getFirstChild().isObjectLit());
    assertTrue(controlStructure.getFirstChild().getFirstChild().isBlock());
  }

  // Test PrepareAnnotations.shouldTraverse()
  @Test
  public void testShouldTraverse() {
    Node node = IR.objectNode();
    NodeTraversal t = new NodeTraversal(null, null);
    NodeTraversal.Callback callback = new PrepareAst.PrepareAnnotations();

    assertTrue(callback.shouldTraverse(t, node, null));
  }

  // Test PrepareAnnotations.visit() with Token.CALL
  @Test
  public void testVisitCall() {
    Node node = IR.call(IR.name("someFunction"));

    prepareAst.new PrepareAnnotations().visit(null, node, null);

    assertTrue(node.getBooleanProp(Node.FREE_CALL));
  }

  // Test PrepareAnnotations.visit() with Token.FUNCTION
  @Test
  public void testVisitFunction() {
    Node node = IR.function(IR.name("someFunction"), IR.paramList(), IR.block());

    prepareAst.new PrepareAnnotations().visit(null, node, IR.assign(node, IR.nullNode()));

    assertTrue(node.getBooleanProp(Node.IS_DISPATCHER));
  }

  // Test PrepareAnnotations.normalizeObjectLiteralKeyAnnotations()
  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node objlit = IR.objectNode();
    Node key = IR.stringKey("a");
    Node value = IR.function(IR.name(""), IR.paramList(), IR.block());

    key.setJSDocInfo(IR.jSDocInfo());

    prepareAst.new PrepareAnnotations().normalizeObjectLiteralKeyAnnotations(objlit, key, value);

    assertNull(key.getJSDocInfo());
    assertNotNull(value.getJSDocInfo());
  }

}