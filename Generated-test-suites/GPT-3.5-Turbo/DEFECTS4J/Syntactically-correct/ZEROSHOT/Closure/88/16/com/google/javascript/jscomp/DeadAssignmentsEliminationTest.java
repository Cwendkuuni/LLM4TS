import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testProcess() {
    // create a compilation unit with some code
    Compiler compiler = new Compiler();
    compiler.initOptions(new CompilerOptions());
    Node externs = compiler.parseTestCode("");
    Node root = compiler.parseTestCode("");
    
    // create a dead assignments elimination instance
    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    
    // process the compilation unit
    elimination.process(externs, root);
    
    // verify the results
    // ...
  }
}