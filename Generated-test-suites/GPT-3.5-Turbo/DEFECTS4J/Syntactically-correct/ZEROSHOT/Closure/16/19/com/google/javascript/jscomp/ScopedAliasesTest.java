import org.junit.Assert;
import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testProcess() {
    // Create a test compiler and set up the necessary input data
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = new Node(jscode);
    Node root = new Node(jscode);
    
    // Create an instance of the ScopedAliases class
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName, SourcePosition<AliasTransformation> position) {
        return null;
      }
    });
    
    // Call the process method and check that there are no errors
    scopedAliases.process(externs, root);
    Assert.assertFalse(scopedAliases.hasErrors());
  }
  
  @Test
  public void testHotSwapScript() {
    // Create a test compiler and set up the necessary input data
    AbstractCompiler compiler = new AbstractCompiler();
    Node root = new Node(jscode);
    Node originalRoot = new Node(jscode);
    
    // Create an instance of the ScopedAliases class
    ScopedAliases scopedAliases = new ScopedAliases(compiler, null, new AliasTransformationHandler() {
      @Override
      public AliasTransformation logAliasTransformation(String fileName, SourcePosition<AliasTransformation> position) {
        return null;
      }
    });
    
    // Call the hotSwapScript method and check that there are no errors
    scopedAliases.hotSwapScript(root, originalRoot);
    Assert.assertFalse(scopedAliases.hasErrors());
  }
}