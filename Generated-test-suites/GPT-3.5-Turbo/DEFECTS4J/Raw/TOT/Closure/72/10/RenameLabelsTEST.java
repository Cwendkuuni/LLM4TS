import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenameLabelsTest {

  private AbstractCompiler compiler;
  private RenameLabels renameLabels;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    renameLabels = new RenameLabels(compiler);
  }

  @Test
  public void testProcess_LabelNode_Success() {
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "labelName");
    labelNode.addChildToBack(nameNode);

    renameLabels.process(null, labelNode);

    // Assert that the label node's name has been updated to a shorter name
    assertEquals("a", nameNode.getString());
  }

  @Test
  public void testProcess_BreakNode_Success() {
    Node breakNode = new Node(Token.BREAK);
    Node nameNode = new Node(Token.NAME, "breakName");
    breakNode.addChildToBack(nameNode);

    // Add a label to the label map of the current namespace
    renameLabels.namespaceStack.peek().renameMap.put("breakName", new LabelInfo(1));

    renameLabels.process(null, breakNode);

    // Assert that the break node's name has been updated to a shorter name
    assertEquals("a", nameNode.getString());
  }

  @Test
  public void testProcess_ContinueNode_Success() {
    Node continueNode = new Node(Token.CONTINUE);
    Node nameNode = new Node(Token.NAME, "continueName");
    continueNode.addChildToBack(nameNode);

    // Add a label to the label map of the current namespace
    renameLabels.namespaceStack.peek().renameMap.put("continueName", new LabelInfo(1));

    renameLabels.process(null, continueNode);

    // Assert that the continue node's name has been updated to a shorter name
    assertEquals("a", nameNode.getString());
  }

  @Test
  public void testProcess_LabelNode_NotReferenced() {
    Node labelNode = new Node(Token.LABEL);
    Node nameNode = new Node(Token.NAME, "labelName");
    labelNode.addChildToBack(nameNode);

    // Remove the label from the label map of the current namespace
    renameLabels.namespaceStack.peek().renameMap.remove("labelName");

    renameLabels.process(null, labelNode);

    // Assert that the label node has been removed
    assertNull(labelNode.getParent());
  }

  @Test
  public void testProcess_BreakNode_NotReferenced() {
    Node breakNode = new Node(Token.BREAK);
    Node nameNode = new Node(Token.NAME, "breakName");
    breakNode.addChildToBack(nameNode);

    // Remove the label from the label map of the current namespace
    renameLabels.namespaceStack.peek().renameMap.remove("breakName");

    renameLabels.process(null, breakNode);

    // Assert that the break node's name has not been updated
    assertEquals("breakName", nameNode.getString());
  }

  @Test
  public void testProcess_ContinueNode_NotReferenced() {
    Node continueNode = new Node(Token.CONTINUE);
    Node nameNode = new Node(Token.NAME, "continueName");
    continueNode.addChildToBack(nameNode);

    // Remove the label from the label map of the current namespace
    renameLabels.namespaceStack.peek().renameMap.remove("continueName");

    renameLabels.process(null, continueNode);

    // Assert that the continue node's name has not been updated
    assertEquals("continueName", nameNode.getString());
  }

}