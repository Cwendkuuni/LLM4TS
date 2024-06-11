import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenameLabelsTest {

  private RenameLabels renameLabels;

  @Before
  public void setUp() {
    renameLabels = new RenameLabels();
  }

  @Test
  public void testProcess_singleLabel() {
    Node rootNode = createTestNode();
    renameLabels.process(null, rootNode);

    assertEquals("a", rootNode.getFirstChild().getFirstChild().getString());
  }

  @Test
  public void testProcess_multipleLabels() {
    Node rootNode = createTestNodeWithMultipleLabels();
    renameLabels.process(null, rootNode);

    assertEquals("a", rootNode.getFirstChild().getFirstChild().getString());
    assertEquals("b", rootNode.getFirstChild().getFirstChild().getNext().getFirstChild().getString());
  }

  @Test
  public void testProcess_labelNotReferenced() {
    Node rootNode = createTestNodeWithUnreferencedLabel();
    renameLabels.process(null, rootNode);

    assertNull(rootNode.getFirstChild());
  }

  @Test
  public void testProcess_nestedLabels() {
    Node rootNode = createTestNodeWithNestedLabels();
    renameLabels.process(null, rootNode);

    assertEquals("a", rootNode.getFirstChild().getFirstChild().getString());
    assertEquals("b", rootNode.getFirstChild().getFirstChild().getNext().getFirstChild().getString());
  }

  private Node createTestNode() {
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "oldLabelName");
    labelNode.addChildToBack(nameNode);
    return new Node(labelNode);
  }

  private Node createTestNodeWithMultipleLabels() {
    Node labelNode1 = new Node(Token.LABEL);
    Node nameNode1 = new Node(Token.NAME, "oldLabelName1");
    labelNode1.addChildToBack(nameNode1);

    Node labelNode2 = new Node(Token.LABEL);
    Node nameNode2 = new Node(Token.NAME, "oldLabelName2");
    labelNode2.addChildToBack(nameNode2);

    Node statementBlockNode = new Node(Token.BLOCK);
    statementBlockNode.addChildToBack(labelNode1);
    statementBlockNode.addChildToBack(labelNode2);

    return new Node(statementBlockNode);
  }

  private Node createTestNodeWithUnreferencedLabel() {
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "oldLabelName");
    labelNode.addChildToBack(nameNode);
    labelNode.addChildToBack(new Node(Token.LET));
    return new Node(labelNode);
  }

  private Node createTestNodeWithNestedLabels() {
    Node outerLabelNode = new Node(Token.LABEL);
    Node outerNameNode = new Node(Token.NAME, "outerLabelName");
    outerLabelNode.addChildToBack(outerNameNode);

    Node innerLabelNode = new Node(Token.LABEL);
    Node innerNameNode = new Node(Token.NAME, "innerLabelName");
    innerLabelNode.addChildToBack(innerNameNode);

    Node statementBlockNode1 = new Node(Token.BLOCK);
    statementBlockNode1.addChildToBack(innerLabelNode);

    Node blockNode = new Node(Token.BLOCK);
    blockNode.addChildToBack(statementBlockNode1);

    Node statementBlockNode2 = new Node(Token.BLOCK);
    statementBlockNode2.addChildToBack(blockNode);

    Node statementBlockNode3 = new Node(Token.BLOCK);
    statementBlockNode3.addChildToBack(outerLabelNode);
    statementBlockNode3.addChildToBack(statementBlockNode2);

    return new Node(statementBlockNode3);
  }
}