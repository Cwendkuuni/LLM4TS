import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {

  private PrepareAst prepareAst;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    prepareAst = new PrepareAst(compiler);
  }

  @Test
  public void testProcess_WithExternsAndRoot_CallsPrepareAnnotations() {
    // Arrange
    Node externs = IR.root();
    Node root = IR.root();

    // Act
    prepareAst.process(externs, root);

    // Assert
    // Verify that PrepareAnnotations is called for externs and root
    assertTrue(compiler.getNodesForTraversal().contains(externs));
    assertTrue(compiler.getNodesForTraversal().contains(root));
  }

  @Test
  public void testProcess_WithNullExterns_CallsPrepareAnnotationsForRoot() {
    // Arrange
    Node externs = null;
    Node root = IR.root();

    // Act
    prepareAst.process(externs, root);

    // Assert
    // Verify that PrepareAnnotations is called only for root
    assertFalse(compiler.getNodesForTraversal().contains(externs));
    assertTrue(compiler.getNodesForTraversal().contains(root));
  }

  @Test
  public void testProcess_WithNullRoot_CallsPrepareAnnotationsForExterns() {
    // Arrange
    Node externs = IR.root();
    Node root = null;

    // Act
    prepareAst.process(externs, root);

    // Assert
    // Verify that PrepareAnnotations is called only for externs
    assertTrue(compiler.getNodesForTraversal().contains(externs));
    assertFalse(compiler.getNodesForTraversal().contains(root));
  }

  @Test
  public void testNormalizeNodeTypes_WhenCalled_NormalizesNodeTypes() {
    // Arrange
    Node node = createMockNode();

    // Act
    prepareAst.normalizeNodeTypes(node);

    // Assert
    // Verify that normalizeBlocks and normalizeNodeTypes are called for each child node recursively
    // Verify that normalizeBlocks is called for control structures and normalizeNodeTypes is called for other nodes
    assertTrue(node.hasChildren());
    for (Node child : node.children()) {
      if (NodeUtil.isControlStructure(child) && !child.isLabel() && !child.isSwitch()) {
        assertTrue(child.hasChildren());
        for (Node subChild : child.children()) {
          assertEquals(IR.block(), subChild);
        }
      } else {
        assertEquals(0, child.children().size());
      }
    }
  }

  @Test
  public void testNormalizeBlocks_WhenCalled_NormalizesBlocks() {
    // Arrange
    Node n = createMockNode();

    // Act
    prepareAst.normalizeBlocks(n);

    // Assert
    // Verify that blocks are added to IF, WHILE, DO, etc.
    assertTrue(n.hasChildren());
    for (Node child : n.children()) {
      if (NodeUtil.isControlStructure(child) && !child.isLabel() && !child.isSwitch()) {
        assertTrue(child.hasChildren());
        for (Node c : child.children()) {
          assertTrue(c.isBlock());
        }
      }
    }
  }

  @Test
  public void testPrepareAnnotations_NormalizesObjectLiteralAnnotations() {
    // Arrange
    Node objlit = createMockObjectLiteralNode();

    // Act
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.normalizeObjectLiteralAnnotations(objlit);

    // Assert
    // Verify that JsDoc annotations are normalized for each key-value pair in the object literal
    assertTrue(objlit.hasChildren());
    for (Node key = objlit.getFirstChild(); key != null; key = key.getNext()) {
      Node value = key.getFirstChild();
      assertNull(key.getJSDocInfo());
      assertNotNull(value.getJSDocInfo());
      assertEquals(key.getJSDocInfo(), value.getJSDocInfo());
    }
  }

  @Test
  public void testPrepareAnnotations_AnnotatesCalls() {
    // Arrange
    Node callNode = createMockCallNode();

    // Act
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateCalls(callNode);

    // Assert
    // Verify that "FREE_CALL" and "DIRECT_EVAL" properties are added correctly
    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
    assertNotNull(callNode.getFirstChild());
    assertEquals("eval", callNode.getFirstChild().getString());
    assertTrue(callNode.getFirstChild().getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAnnotations_AnnotateDispatchers() {
    // Arrange
    Node functionNode = createMockFunctionNode();
    Node parent = IR.assign();

    // Act
    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    prepareAnnotations.annotateDispatchers(functionNode, parent);

    // Assert
    // Verify that "IS_DISPATCHER" property is added correctly
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  private Node createMockNode() {
    Node node = IR.root();
    Node child1 = IR.doNode(
        IR.block(),
        IR.breakNode(),
        IR.caseNode(IR.empty(), IR.block(), IR.block()),
        IR.whileNode(IR.empty(), IR.block()),
        IR.ifNode(IR.empty(), IR.block(), IR.block()),
        IR.forNode(IR.empty(), IR.empty(), IR.empty(), IR.block()),
        IR.forInNode(IR.empty(), IR.empty(), IR.block()),
        IR.switchNode(IR.empty(), IR.caseNode(IR.empty(), IR.block()), IR.caseNode(IR.empty(), IR.block())),
        IR.tryCatch(IR.block(), IR.catchNode(IR.empty(), IR.block())),
        IR.function(IR.name(""), IR.paramList(), IR.block()),
        IR.constNode(IR.empty(), IR.empty()),
        IR.let(IR.empty(), IR.empty()),
        IR.trim()
    );
    node.addChildToBack(child1);
    return node;
  }

  private Node createMockObjectLiteralNode() {
    Node objlit = IR.objectlit();
    Node key1 = IR.string("a");
    Node key2 = IR.string("b");
    Node value1 = IR.string("c");
    Node value2 = IR.string("d");
    objlit.addChildToBack(key1);
    objlit.addChildToBack(key2);
    key1.addChildToBack(value1);
    key2.addChildToBack(value2);
    return objlit;
  }

  private Node createMockCallNode() {
    Node callNode = IR.call();
    Node firstChild = IR.name("eval");
    callNode.addChildToBack(firstChild);
    return callNode;
  }

  private Node createMockFunctionNode() {
    return IR.function(IR.name(""), IR.paramList(), IR.block());
  }
}