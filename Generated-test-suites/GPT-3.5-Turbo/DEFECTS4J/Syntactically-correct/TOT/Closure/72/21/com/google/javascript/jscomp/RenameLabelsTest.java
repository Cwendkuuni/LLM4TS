import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.*;

public class RenameLabelsTest {

  private static final String LABEL_NAME = "label";

  private RenameLabels renameLabels;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    renameLabels = new RenameLabels(compiler);
  }

  @Test
  public void testProcess_withLabelNode_shouldRenameLabel() {
    String originalLabelName = "a";
    Node ast = createLabelNode(originalLabelName);

    renameLabels.process(null, ast);

    Node renamedLabelNode = ast.getFirstChild();
    Assert.assertEquals(LABEL_NAME, renamedLabelNode.getString());
  }

  @Test
  public void testProcess_withBreakNode_shouldRenameLabelReference() {
    String originalLabelName = "a";
    Node ast = createBreakNode(originalLabelName);

    renameLabels.process(null, ast);

    Node breakNode = ast.getFirstChild();
    Node labelReferenceNode = breakNode.getFirstChild();
    Assert.assertEquals(LABEL_NAME, labelReferenceNode.getString());
  }

  @Test
  public void testProcess_withUnreferencedLabel_shouldRemoveLabel() {
    String originalLabelName = "a";
    Node ast = createUnreferencedLabelNode(originalLabelName);

    renameLabels.process(null, ast);

    Assert.assertNull(ast.getFirstChild());
  }

  @Test
  public void testProcess_withLabelNodeReferencedAfterRename_shouldNotRemoveLabel() {
    String originalLabelName = "a";
    Node ast = createLabelNode(originalLabelName);
    Node referenceNode = NodeUtil.newName(compiler, LABEL_NAME);
    ast.getLastChild().addChildToBack(referenceNode);

    renameLabels.process(null, ast);

    Assert.assertNotNull(ast.getFirstChild());
  }

  private Node createLabelNode(String labelName) {
    Node label = new Node(Token.LABEL);
    Node name = NodeUtil.newName(compiler, labelName);
    label.addChildToBack(name);
    Node statements = new Node(Token.BLOCK);
    label.addChildToBack(statements);
    Node ast = new Node(Token.SCRIPT);
    ast.addChildToBack(label);
    return ast;
  }

  private Node createBreakNode(String labelName) {
    Node breakNode = new Node(Token.BREAK);
    Node name = NodeUtil.newName(compiler, labelName);
    breakNode.addChildToBack(name);
    Node ast = new Node(Token.SCRIPT);
    ast.addChildToBack(breakNode);
    return ast;
  }

  private Node createUnreferencedLabelNode(String labelName) {
    Node label = createLabelNode(labelName);
    label.getFirstChild().getFirstChild().removeChildAfter();
    return label;
  }
}