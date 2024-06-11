import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static com.google.javascript.jscomp.CheckLevel.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeTraversal;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    Node rootNode = Node.newProgram(1);
    CheckGlobalThis checker = new CheckGlobalThis(new Compiler(), WARNING);

    Node functionNode = Node.newFunction("evil", 0, Node.NOP);
    functionNode.setJSDocInfo(new JSDocInfo("@this {evil: false}", null, functionNode));
    rootNode.addChild(functionNode);

    NodeTraversal traversal = new NodeTraversal(rootNode, new CheckGlobalThis(new Compiler(), WARNING));
    traversal.traverse();

    assertFalse("No warning expected for a function annotated with @this", checker.hasError());

    functionNode.setJSDocInfo(null);
    rootNode.traverse(checker);

    assertNotNull("Warning expected for a function without @this annotation", checker.getError());
  }

  // Add test cases for other methods and edge cases here

  @Test
  public void testCheckGlobalThis_EdgeCases() {
    // Test cases for edge cases and other scenarios
  }
}