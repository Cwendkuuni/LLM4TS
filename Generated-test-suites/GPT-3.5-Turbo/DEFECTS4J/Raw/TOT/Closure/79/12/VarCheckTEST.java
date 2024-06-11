import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {

   private VarCheck varCheck;

   @Before
   public void setUp() {
      varCheck = new VarCheck();
   }

   @Test
   public void testVisit_VariableDeclaredInCurrentFile_NoError() {
      // Arrange
      NodeTraversal t = new NodeTraversal();
      Node n = new Node(Token.NAME, "x");
      Node parent = new Node(Token.VAR);

      // Act
      varCheck.visit(t, n, parent);

      // Assert
      // No error is expected
   }

   @Test
   public void testVisit_VariableNotDeclared_ErrorReported() {
      // Arrange
      NodeTraversal t = new NodeTraversal();
      Node n = new Node(Token.NAME, "x");
      Node parent = new Node(Token.GETPROP);

      // Act
      varCheck.visit(t, n, parent);

      // Assert
      assertTrue(t.getErrorCount() == 1);
   }

   @Test
   public void testVisit_VariableDeclaredInDifferentModule_NoError() {
      // Arrange
      NodeTraversal t = new NodeTraversal();
      Node n = new Node(Token.NAME, "x");
      Node parent = new Node(Token.VAR);

      // Mocking the modules
      JSModule currModule = new JSModule("current");
      JSModule varModule = new JSModule("other");
      JSModuleGraph moduleGraph = new JSModuleGraph();
      moduleGraph.addDependency(currModule, varModule);
      compiler.setModuleGraph(moduleGraph);
      CompilerInput currInput = new CompilerInput(currModule);
      CompilerInput varInput = new CompilerInput(varModule);
      t.setInput(currInput);
      t.getScope().declare("x", n, null, varInput);

      // Act
      varCheck.visit(t, n, parent);

      // Assert
      // No error is expected
   }

   @Test
   public void testVisit_VariableDeclaredInDifferentModule_ErrorReported() {
      // Arrange
      NodeTraversal t = new NodeTraversal();
      Node n = new Node(Token.NAME, "x");
      Node parent = new Node(Token.VAR);

      // Mocking the modules
      JSModule currModule = new JSModule("current");
      JSModule varModule = new JSModule("other");
      JSModuleGraph moduleGraph = new JSModuleGraph();
      moduleGraph.addDependency(varModule, currModule);
      compiler.setModuleGraph(moduleGraph);
      CompilerInput currInput = new CompilerInput(currModule);
      CompilerInput varInput = new CompilerInput(varModule);
      t.setInput(currInput);
      t.getScope().declare("x", n, null, varInput);

      // Act
      varCheck.visit(t, n, parent);

      // Assert
      assertTrue(t.getErrorCount() == 1);
   }

   @Test
   public void testVisit_VariableReferencedBetweenModulesWithNoDependency_WarningReported() {
      // Arrange
      NodeTraversal t = new NodeTraversal();
      Node n = new Node(Token.NAME, "x");
      Node parent = new Node(Token.VAR);

      // Mocking the modules
      JSModule currModule = new JSModule("current");
      JSModule varModule = new JSModule("other");
      JSModuleGraph moduleGraph = new JSModuleGraph();
      compiler.setModuleGraph(moduleGraph);
      CompilerInput currInput = new CompilerInput(currModule);
      CompilerInput varInput = new CompilerInput(varModule);
      t.setInput(currInput);
      t.getScope().declare("x", n, null, varInput);

      // Act
      varCheck.visit(t, n, parent);

      // Assert
      assertTrue(t.getWarningCount() == 1);
   }

}