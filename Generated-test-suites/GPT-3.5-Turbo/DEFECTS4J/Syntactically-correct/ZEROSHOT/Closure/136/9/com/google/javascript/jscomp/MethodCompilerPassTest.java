import static org.junit.Assert.*;
import org.junit.Test;

public class MethodCompilerPassTest {

   @Test
   public void testProcess() {
      //Create AbstractCompiler object
      AbstractCompiler compiler = new AbstractCompiler();

      //Create MethodCompilerPass object
      MethodCompilerPass methodCompilerPass = new MethodCompilerPass(compiler) {
         public Callback getActingCallback() {
            return new NodeTraversal.Callback() {
               public void visit(NodeTraversal t, Node n, Node parent) {
                  //Code to perform during traversal
               }
            };
         }

         public MethodCompilerPass.SignatureStore getSignatureStore() {
            return null;
         }
      };

      //Create Node objects
      Node externs = new Node();
      Node root = new Node();

      //Call process method
      methodCompilerPass.process(externs, root);

      //Assertions
      assertEquals(compiler, methodCompilerPass.compiler);
      assertEquals(0, methodCompilerPass.externMethods.size());
      assertEquals(0, methodCompilerPass.externMethodsWithoutSignatures.size());
      assertEquals(0, methodCompilerPass.nonMethodProperties.size());
      assertNotNull(methodCompilerPass.methodDefinitions);
   }
}