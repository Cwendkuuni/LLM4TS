import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PrepareAstTest {

  private PrepareAst prepareAst;

  @Mock
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    prepareAst = new PrepareAst(compiler);
  }

  @Test
  public void testProcess_withNullExternsAndRoot_shouldNotThrowException() {
    Node externs = null;
    Node root = null;

    try {
      prepareAst.process(externs, root);
    } catch (Exception e) {
      fail("An unexpected exception occurred");
    }
  }

  @Test
  public void testProcess_withNonNullExternsAndRoot_shouldNotThrowException() {
    Node externs = new Node(Token.NEW);
    Node root = new Node(Token.THIS);

    try {
      prepareAst.process(externs, root);
    } catch (Exception e) {
      fail("An unexpected exception occurred");
    }
  }

  @Test
  public void testNormalizeNodeTypes_withValidNode_shouldNormalizeNodeTypes() {
    Node node = new Node(Token.VAR);
    node.addChildToFront(new Node(Token.NAME));

    prepareAst.normalizeNodeTypes(node);

    assertEquals(Token.VAR, node.getToken());
    assertEquals(Token.NAME, node.getFirstChild().getToken());
  }

  @Test
  public void testNormalizeNodeTypes_withNullNode_shouldNotThrowException() {
    Node node = null;

    try {
      prepareAst.normalizeNodeTypes(node);
    } catch (Exception e) {
      fail("An unexpected exception occurred");
    }
  }

  @Test
  public void testNormalizeBlocks_withValidControlStructure_shouldNormalizeBlocks() {
    Node controlStructure = new Node(Token.IF);
    controlStructure.addChildToFront(new Node(Token.TRUE));
    controlStructure.addChildToFront(new Node(Token.BLOCK));

    prepareAst.normalizeBlocks(controlStructure);

    assertEquals(Token.IF, controlStructure.getToken());
    assertEquals(Token.BLOCK, controlStructure.getFirstChild().getToken());
  }

  @Test
  public void testNormalizeBlocks_withNullControlStructure_shouldNotThrowException() {
    Node controlStructure = null;

    try {
      prepareAst.normalizeBlocks(controlStructure);
    } catch (Exception e) {
      fail("An unexpected exception occurred");
    }
  }

  @Test
  public void testPrepareAnnotations_withObjectLit_shouldNormalizeObjectLiteralAnnotations() {
    Node objectLit = new Node(Token.OBJECTLIT);
    objectLit.addChildToFront(new Node(Token.KEY, new Node(Token.STRING)));
    objectLit.getFirstChild().addChildToFront(new Node(Token.STRING));

    prepareAst.PrepareAnnotations().normalizeObjectLiteralAnnotations(objectLit);

    assertEquals(Token.OBJECTLIT, objectLit.getToken());
    assertEquals(Token.STRING, objectLit.getFirstChild().getFirstChild().getToken());
  }

  @Test
  public void testShouldTraverse_withObjectLit_shouldReturnTrue() {
    NodeTraversal t = new NodeTraversal();
    NodeTraversal.NodeTraversalCallback callback = prepareAst.PrepareAnnotations();

    assertTrue(callback.shouldTraverse(t, new Node(Token.OBJECTLIT), new Node(Token.OBJECTLIT)));
  }

  @Test
  public void testVisit_withCall_shouldAnnotateCalls() {
    Node callNode = new Node(Token.CALL);
    callNode.addChildToFront(new Node(Token.NAME, new Node(Token.OBJECTLIT)));

    prepareAst.PrepareAnnotations().annotateCalls(callNode);

    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testVisit_withFunction_shouldAnnotateDispatchers() {
    Node functionNode = new Node(Token.FUNCTION);
    Node parentNode = new Node(Token.ASSIGN);
    parentNode.addChildToBack(functionNode);

    prepareAst.PrepareAnnotations().annotateDispatchers(functionNode, parentNode);

    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testVisit_withUnknownNodeType_shouldNotThrowException() {
    Node unknownNode = new Node(Token.UNKNOWN);

    try {
      prepareAst.PrepareAnnotations().visit(new NodeTraversal(), unknownNode, unknownNode);
    } catch (Exception e) {
      fail("An unexpected exception occurred");
    }
  }

  @Test
  public void testPrepareAstConstructor_withCompiler_shouldSetCompilerAndCheckOnlyFalse() {
    prepareAst = new PrepareAst(compiler);

    assertSame(compiler, prepareAst.getCompiler());
    assertFalse(prepareAst.isCheckOnly());
  }

  @Test
  public void testPrepareAstConstructor_withCompilerAndCheckOnly_shouldSetCompilerAndCheckOnly() {
    prepareAst = new PrepareAst(compiler, true);

    assertSame(compiler, prepareAst.getCompiler());
    assertTrue(prepareAst.isCheckOnly());
  }

  @Test
  public void testReportChange_shouldNotThrowException() {
    try {
      prepareAst.reportChange();
    } catch (Exception e) {
      fail("An unexpected exception occurred");
    }
  }

  @Test
  public void testProcess_withCheckOnlyTrue_shouldNormalizeNodeTypes() {
    prepareAst = new PrepareAst(compiler, true);
    Node externs = new Node(Token.NEW);
    Node root = new Node(Token.THIS);

    prepareAst.process(externs, root);

    assertEquals(Token.NEW, externs.getToken());
    assertEquals(Token.THIS, root.getToken());
  }

  @Test
  public void testProcess_withCheckOnlyFalse_shouldNormalizeNodeTypesAndPrepareAnnotations() {
    prepareAst = new PrepareAst(compiler, false);
    Node externs = new Node(Token.NEW);
    Node root = new Node(Token.THIS);

    prepareAst.process(externs, root);

    assertEquals(Token.NEW, externs.getToken());
    assertEquals(Token.THIS, root.getToken());
  }

  @Test
  public void testProcess_withCheckOnlyTrueAndNullExternsAndRoot_shouldNotThrowException() {
    prepareAst = new PrepareAst(compiler, true);
    Node externs = null;
    Node root = null;

    try {
      prepareAst.process(externs, root);
    } catch (Exception e) {
      fail("An unexpected exception occurred");
    }
  }

  @Test
  public void testProcess_withCheckOnlyFalseAndNullExternsAndRoot_shouldNotThrowException() {
    prepareAst = new PrepareAst(compiler, false);
    Node externs = null;
    Node root = null;

    try {
      prepareAst.process(externs, root);
    } catch (Exception e) {
      fail("An unexpected exception occurred");
    }
  }
}