import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class MethodCompilerPassTest {

   @Test
   public void testExternMethods() {
       MethodCompilerPass methodCompilerPass = Mockito.mock(MethodCompilerPass.class);
       
       methodCompilerPass.externMethods.add("testMethod");
       assertTrue(methodCompilerPass.externMethods.contains("testMethod"));
   }
   
   @Test
   public void testNonMethodProperties() {
       MethodCompilerPass methodCompilerPass = Mockito.mock(MethodCompilerPass.class);
       
       methodCompilerPass.nonMethodProperties.add("testProperty");
       assertTrue(methodCompilerPass.nonMethodProperties.contains("testProperty"));
   }
   
   @Test
   public void testMethodDefinitions() {
       MethodCompilerPass methodCompilerPass = Mockito.mock(MethodCompilerPass.class);
       
       methodCompilerPass.methodDefinitions.put("testMethod", new Node(Token.FUNCTION));
       assertTrue(methodCompilerPass.methodDefinitions.containsKey("testMethod"));
   }
   
   @Test
   public void testAddPossibleSignature() {
       MethodCompilerPass methodCompilerPass = Mockito.mock(MethodCompilerPass.class);
       Node node = new Node(Token.FUNCTION);
       NodeTraversal nodeTraversal = Mockito.mock(NodeTraversal.class);

       methodCompilerPass.addPossibleSignature("testMethod", node, nodeTraversal);
       assertTrue(methodCompilerPass.methodDefinitions.containsKey("testMethod"));
   }

   @Test
   public void testProcess() {
       MethodCompilerPass methodCompilerPass = Mockito.mock(MethodCompilerPass.class);
       Node externs = new Node(Token.EXTERN);
       Node root = new Node(Token.ROOT);

       methodCompilerPass.process(externs, root);
       assertTrue(methodCompilerPass.externMethods.isEmpty());
       assertTrue(methodCompilerPass.externMethodsWithoutSignatures.isEmpty());
   }
   
}