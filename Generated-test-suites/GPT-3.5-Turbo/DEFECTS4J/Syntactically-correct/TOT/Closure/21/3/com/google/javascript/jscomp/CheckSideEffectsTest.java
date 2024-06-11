import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckSideEffectsTest {

   @Test
   public void testProcess_NoSideEffects_Success() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      boolean protectSideEffectFreeCode = true;
      CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

      // Act
      checkSideEffects.process(null, null);

      // Assert
      // No warnings should be reported
      assertTrue(compiler.getWarnings().isEmpty());
   }

   @Test
   public void testProcess_SideEffects_ReportWarnings() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      boolean protectSideEffectFreeCode = true;
      CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

      // Creating a side-effect code
      Node root = createSideEffectsCode();

      // Act
      checkSideEffects.process(null, root);

      // Assert
      // Warnings related to side-effects should be reported
      assertFalse(compiler.getWarnings().isEmpty());
   }

   private Node createSideEffectsCode() {
      // Create a side-effect code
      Node root = new Node(Token.BLOCK);

      // Line 1
      Node stringNode = IR.string("this string is");
      Node assignNode = IR.assign(IR.name("s"), stringNode);

      // Line 2
      Node continueStringNode = IR.string("continued on the next line but you forgot the +");
      assignNode.addChildToBack(continueStringNode);
      root.addChildToBack(assignNode);

      return root;
   }

   @Test
   public void testHotSwapScript_NoSideEffects_Success() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      boolean protectSideEffectFreeCode = true;
      CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

      // Act
      checkSideEffects.hotSwapScript(null, null);

      // Assert
      // No warnings should be reported
      assertTrue(compiler.getWarnings().isEmpty());
   }

   @Test
   public void testHotSwapScript_SideEffects_ReportWarnings() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckLevel level = CheckLevel.WARNING;
      boolean protectSideEffectFreeCode = true;
      CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

      // Creating a side-effect code
      Node scriptRoot = createSideEffectsCode();

      // Act
      checkSideEffects.hotSwapScript(scriptRoot, null);

      // Assert
      // Warnings related to side-effects should be reported
      assertFalse(compiler.getWarnings().isEmpty());
   }

   @Test
   public void testHotSwapScript_SideEffectsCode_HasNoMoreProtection_CallRemoved() {
      // Arrange
      AbstractCompiler compiler = new AbstractCompiler();
      CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);

      // Creating a side-effect code
      Node scriptRoot = createSideEffectsCodeWithProtection();

      // Act
      stripProtection.process(null, scriptRoot);

      // Assert
      // The protected side-effect code should be removed
      assertEquals(Token.IN, scriptRoot.getFirstChild().getNext().getType());
   }

   private Node createSideEffectsCodeWithProtection() {
      // Create a side-effect code with protection
      Node root = new Node(Token.BLOCK);

      // Line 1
      Node stringNode = IR.string("this string is");
      Node assignNode = IR.assign(IR.name("s"), stringNode);

      // Line 2 - Protection
      Node protectionNode = IR.call(IR.name("JSCOMPILER_PRESERVE"));
      protectionNode.addChildToBack(assignNode);

      root.addChildToBack(protectionNode);

      return root;
   }

}