import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Compiler;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.JSModule;

public class VarCheckTest {
  
  private VarCheck varCheck;
  private CompilerInput input;
  private Compiler compiler;
  private JSModuleGraph moduleGraph;
  
  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    varCheck = new VarCheck(compiler);
    input = new CompilerInput("TestInput.js");
    moduleGraph = compiler.getModuleGraph();
    Node externsRoot = new Node(Token.ROOT);
    Node codeRoot = new Node(Token.ROOT);
    input.setExterns(externsRoot);
    input.setAstRoot(codeRoot);
  }
  
  @Test
  public void testProcess() throws Exception {
    Node externs = new Node(Token.ROOT);
    Node root = new Node(Token.ROOT);
    varCheck.process(externs, root);
    // Test goes here
  }
  
  @Test
  public void testVisit() throws Exception {
    NodeTraversal traversal = new NodeTraversal(compiler, varCheck);
    Node n = new Node("Name");
    Node parent = new Node("Parent");
    traversal.visit(null, n, parent);
    // Test goes here
  }
  
  @Test
  public void testCreateSynthesizedExternVar() throws Exception {
    String varName = "testVar";
    varCheck.createSynthesizedExternVar(varName);
    // Test goes here
  }
  
  @Test 
  public void testGetSynthesizedExternsInput() throws Exception {
    CompilerInput input = varCheck.getSynthesizedExternsInput();
    // Test goes here
  }
  
  @Test 
  public void testGetSynthesizedExternsRoot() throws Exception {
    Node root = varCheck.getSynthesizedExternsRoot();
    // Test goes here
  }
}