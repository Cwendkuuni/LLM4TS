import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeadAssignmentsEliminationTest {
  
  private static final String TEST_CODE = "" +
      "var a = 1;\n" +
      "var b = 2;\n" +
      "a = 3;\n" +
      "var c = a + b;\n" +
      "b = 4;\n" +
      "c = a + b;\n" +
      "var d = 5;\n" +
      "a = d;\n" +
      "b = d;\n" +
      "c = d;\n" +
      "d = 6;\n" +
      "var e = d;\n" +
      "e = 7;\n";
  
  
  private Compiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;
  
  @Before
  public void setUp() {
    compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    compiler.initOptions(options);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }
  
  @Test
  public void testDeadAssignmentsElimination() {
    Node root = compiler.parse(TEST_CODE);
    Node externs = new Node(Token.ROOT);
    
    deadAssignmentsElimination.process(externs, root);
    
    root = compiler.getRoot();
    
    // Verify that the dead assignments are removed
    
    Node expectedRoot = compiler.parse("" +
        "var a = 3;\n" +
        "var b = 2;\n" +
        "var c = a + b;\n" +
        "b = 4;\n" +
        "c = a + b;\n" +
        "var d = 5;\n" +
        "a = d;\n" +
        "b = d;\n" +
        "c = d;\n" +
        "d = 6;\n" +
        "var e = d;\n" +
        "e = 7;\n");
        
    expectedRoot.detachFromParent();
    
    assertTrue(NodeUtil.isEquivalentTo(root, expectedRoot));
  }
}