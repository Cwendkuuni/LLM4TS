import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

   private VarCheck varCheck;

   @Before
   public void setUp() {
      varCheck = new VarCheck(new AbstractCompiler());
   }

   @Test
   public void testProcess_WhenVarUndefined_ShouldReportError() {
      // Arrange
      String varName = "undefinedVar";
      Node externs = new Node(Token.EXTERN, new Node(Token.VAR, new Node(Token.NAME, varName)));
      Node root = new Node(Token.ROOT);
      Node code = new Node(Token.SCRIPT, new Node(Token.NAME, "testCode"));
      root.addChildToBack(externs);
      root.addChildToBack(code);

      // Act
      varCheck.process(externs, root);

      // Assert
      assertTrue(varCheck.getErrorLevel(VarCheck.UNDEFINED_VAR_ERROR) == CheckLevel.ERROR);
      assertNotNull(varCheck.getWarningLevel(VarCheck.UNDEFINED_VAR_ERROR));
      assertEquals(1, varCheck.getCompiler().getErrorCount());
   }

   @Test
   public void testProcess_WhenVarDefinedInDifferentModule_ShouldReportError() {
      // Arrange
      String varName = "varA";
      Node externs = new Node(Token.EXTERN, new Node(Token.VAR, new Node(Token.NAME, varName)));
      Node root = new Node(Token.ROOT);
      Node moduleA = new Node(Token.MODULE, new Node(Token.NAME, "moduleA"));
      Node moduleB = new Node(Token.MODULE, new Node(Token.NAME, "moduleB"));
      Node codeA = new Node(Token.SCRIPT, new Node(Token.NAME, "testCodeA"));
      Node codeB = new Node(Token.SCRIPT, new Node(Token.NAME, "testCodeB"));
      codeA.addChildToBack(new Node(Token.EXPR_RESULT, new Node(Token.NAME, varName)));
      moduleA.addChildToBack(codeA);
      codeB.addChildToBack(new Node(Token.EXPR_RESULT, new Node(Token.NAME, varName)));
      moduleB.addChildToBack(codeB);
      root.addChildToBack(externs);
      root.addChildToBack(moduleA);
      root.addChildToBack(moduleB);

      // Act
      varCheck.process(externs, root);

      // Assert
      assertTrue(varCheck.getErrorLevel(VarCheck.VIOLATED_MODULE_DEP_ERROR) == CheckLevel.ERROR);
      assertNotNull(varCheck.getWarningLevel(VarCheck.VIOLATED_MODULE_DEP_ERROR));
      assertEquals(1, varCheck.getCompiler().getErrorCount());
   }

   @Test
   public void testProcess_WhenVarUndefinedInExterns_ShouldReportWarning() {
      // Arrange
      String varName = "undefinedVar";
      Node externs = new Node(Token.EXTERN, new Node(Token.VAR, new Node(Token.NAME, varName)));
      Node root = new Node(Token.ROOT);
      Node code = new Node(Token.SCRIPT, new Node(Token.NAME, "testCode"));
      code.addChildToBack(new Node(Token.EXPR_RESULT, new Node(Token.NAME, varName)));
      root.addChildToBack(externs);
      root.addChildToBack(code);

      // Act
      varCheck.process(externs, root);

      // Assert
      assertTrue(varCheck.getWarningLevel(VarCheck.UNDEFINED_VAR_ERROR) == CheckLevel.WARNING);
      assertNotNull(varCheck.getErrorLevel(VarCheck.UNDEFINED_VAR_ERROR));
      assertEquals(1, varCheck.getCompiler().getWarningCount());
   }

   @Test
   public void testProcess_WhenVarDefinedInSameFile_ShouldNotReportErrors() {
      // Arrange
      String varName = "varA";
      Node externs = new Node(Token.EXTERN, new Node(Token.VAR, new Node(Token.NAME, varName)));
      Node root = new Node(Token.ROOT);
      Node module = new Node(Token.MODULE, new Node(Token.NAME, "moduleA"));
      Node code = new Node(Token.SCRIPT, new Node(Token.NAME, "testCodeA"));
      code.addChildToBack(new Node(Token.EXPR_RESULT, new Node(Token.NAME, varName)));
      module.addChildToBack(code);
      root.addChildToBack(externs);
      root.addChildToBack(module);

      // Act
      varCheck.process(externs, root);

      // Assert
      assertNull(varCheck.getErrorLevel(VarCheck.UNDEFINED_VAR_ERROR));
      assertNull(varCheck.getWarningLevel(VarCheck.UNDEFINED_VAR_ERROR));
      assertEquals(0, varCheck.getCompiler().getErrorCount());
      assertEquals(0, varCheck.getCompiler().getWarningCount());
   }
}