import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

  private RenameLabels renameLabels;

  @Before
  public void setUp() {
    renameLabels = new RenameLabels(new AbstractCompiler()) {

      // Mock implementation of the getNameForId method
      @Override
      String getNameForId(int id) {
        return "name" + id;
      }

    };
  }

  @Test
  public void testProcess() {
    // Create mock nodes for testing
    Node externs = new Node(Token.EXTERN);

    Node root = new Node(Token.ROOT);
    Node label1 = new Node(Token.LABEL);
    Node labelName1 = new Node(Token.NAME, "a");
    Node break1 = new Node(Token.BREAK);
    root.addChildToBack(label1);
    label1.addChildToBack(labelName1);
    root.addChildToBack(break1);

    // Call the process method
    renameLabels.process(externs, root);

    // Verify that the label name is renamed
    assertEquals("name1", label1.getFirstChild().getString());

    // Verify that the break is unchanged
    assertEquals(Token.BREAK, break1.getType());
  }

}