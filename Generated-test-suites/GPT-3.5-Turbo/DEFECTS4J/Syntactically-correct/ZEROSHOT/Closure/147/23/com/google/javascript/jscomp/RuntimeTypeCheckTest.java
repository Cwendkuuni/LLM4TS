import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class RuntimeTypeCheckTest {
  
  private static final String LOG_FUNCTION = "function(warning, expr) {}";
  
  private AbstractCompiler compiler;
  
  @Test
  public void testProcess() {
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    compiler = new AbstractCompiler();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, LOG_FUNCTION);
    runtimeTypeCheck.process(externs, root);
    // Verify that the code changes have been reported correctly
    assertEquals(true, compiler.hasCodeChanged());
  }
  
  @Test
  public void testAddMarkers() {
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.Callback addMarkers = new RuntimeTypeCheck.AddMarkers(compiler);
    NodeTraversal.traverse(compiler, traversal, addMarkers);
    // Verify that the code changes have been reported correctly
    assertEquals(true, compiler.hasCodeChanged());
  }
  
  @Test
  public void testAddChecks() {
    NodeTraversal traversal = new NodeTraversal();
    NodeTraversal.Callback addChecks = new RuntimeTypeCheck.AddChecks(compiler);
    NodeTraversal.traverse(compiler, traversal, addChecks);
    // Verify that the code changes have been reported correctly
    assertEquals(true, compiler.hasCodeChanged());
  }
  
  @Test
  public void testCreateCheckTypeCallNode() {
    JSType type = compiler.createType("string");
    Node expr = new Node(Token.STRING, "Hello");
    Node checkNode = runtimeTypeCheck.createCheckTypeCallNode(type, expr);
    // Verify that the checkNode is not null
    assertEquals(true, checkNode != null);
  }
  
  @Test
  public void testCreateCheckerNode() {
    JSType type = compiler.createType("string");
    Node checkerNode = runtimeTypeCheck.createCheckerNode(type);
    // Verify that the checkerNode is not null
    assertEquals(true, checkerNode != null);
  }
  
  @Test
  public void testGetBoilerplateCode() {
    Node boilerplateCode = runtimeTypeCheck.getBoilerplateCode(compiler, LOG_FUNCTION);
    // Verify that the boilerplateCode is not null
    assertEquals(true, boilerplateCode != null);
  }
}