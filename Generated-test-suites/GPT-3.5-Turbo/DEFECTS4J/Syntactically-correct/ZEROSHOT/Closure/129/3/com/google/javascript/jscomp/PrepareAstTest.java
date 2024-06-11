import com.google.common.base.Preconditions;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrepareAstTest {
  private PrepareAst prepareAst;

  @Before
  public void setUp() {
    prepareAst = new PrepareAst(null);
  }

  @Test
  public void testProcess_checkOnly() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);

    prepareAst.process(externs, root);

    // Add assertions here
  }

  @Test
  public void testProcess_notCheckOnly() {
    Node externs = new Node(Token.EXTERN, new Node(Token.NAME, "externs"));
    Node root = new Node(Token.ROOT, new Node(Token.NAME, "root"));

    prepareAst = new PrepareAst(null, false);
    prepareAst.process(externs, root);

    // Add assertions here
  }

  @Test
  public void testReportChange() {
    prepareAst.reportChange();

    // Add assertions here
  }

  @Test
  public void testAnnotateCalls() {
    Node call = new Node(Token.CALL);
    Node name = new Node(Token.NAME, "eval");
    call.addChildToFront(name);

    prepareAst.annotateCalls(call);

    // Add assertions here
  }

  @Test
  public void testAnnotateDispatchers() {
    Node functionNode = new Node(Token.FUNCTION);

    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToBack(functionNode);

    prepareAst.annotateDispatchers(functionNode, assignNode);

    // Add assertions here
  }

  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    Node objectLiteral = new Node(Token.OBJECTLIT);

    Node key1 = new Node(Token.STRING_KEY);
    Node value1 = new Node(Token.STRING, "b");
    key1.addChildToBack(value1);
    objectLiteral.addChildToBack(key1);

    Node key2 = new Node(Token.STRING_KEY);
    Node value2 = new Node(Token.STRING, "d");
    key2.addChildToBack(value2);
    objectLiteral.addChildToBack(key2);

    prepareAst.normalizeObjectLiteralAnnotations(objectLiteral);

    // Add assertions here
  }

  @Test
  public void testNormalizeBlocks() {
    Node blockNode = new Node(Token.BLOCK);

    Node ifNode = new Node(Token.IF);
    blockNode.addChildToFront(ifNode);

    prepareAst.normalizeBlocks(blockNode);

    // Add assertions here
  }

  @Test
  public void testNormalizeNodeTypes() {
    Node rootNode = new Node(Token.ROOT);

    prepareAst.normalizeNodeTypes(rootNode);

    // Add assertions here
  }

  @Test
  public void testProcessNodeTraversal() {
    Node externs = new Node(Token.EXTERN);
    Node root = new Node(Token.ROOT);

    PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();
    NodeTraversal nodeTraversal = new NodeTraversal(null, prepareAnnotations);

    nodeTraversal.traverse(externs);
    nodeTraversal.traverse(root);

    // Add assertions here
  }
}