import com.google.javascript.jscomp.MethodCompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodCompilerPassTest {

 private static final String TEST_SOURCE = "testMethodCompilerPass.js";

 @Test
 public void testMethodCompilerPass() {
 MethodCompilerPass methodCompilerPass = new MethodCompilerPass(null);
 Node rootNode = null; // initialize with your test source code
 Node externsNode = null; // initialize with your test externs code
 methodCompilerPass.process(externsNode, rootNode);

 // Test for normal method declaration and addition to methodDefinitions
 String methodName = "testMethod";
 Node methodNode = new Node(Token.FUNCTION, null);
 methodNode.setLine(1);
 methodNode.setColumn(1);
 methodCompilerPass.processNode(methodNode);
 assertEquals(1, methodCompilerPass.methodDefinitions.size());
 assertTrue(methodCompilerPass.methodDefinitions.containsKey(methodName));

 // Test for method declaration in externs with no signature
 String methodNameWithoutSignature = "testMethodWithoutSignature";
 Node methodNodeWithoutSignature = new Node(Token.NAME, methodNameWithoutSignature);
 methodNodeWithoutSignature.setLine(1);
 methodNodeWithoutSignature.setColumn(1);
 methodCompilerPass.externMethods.add(methodNameWithoutSignature);
 methodCompilerPass.processNode(methodNodeWithoutSignature);
 assertEquals(1, methodCompilerPass.externMethods.size());
 assertFalse(methodCompilerPass.methodDefinitions.containsKey(methodNameWithoutSignature));

 // Test for method declaration in externs with signature
 String methodNameWithSignature = "testMethodWithSignature";
 Node methodNodeWithSignature = new Node(Token.FUNCTION, null);
 methodNodeWithSignature.setLine(1);
 methodNodeWithSignature.setColumn(1);
 methodCompilerPass.externMethods.add(methodNameWithSignature);
 Node methodSignatureNode = new Node(Token.FUNCTION, null);
 methodSignatureNode.setLine(1);
 methodSignatureNode.setColumn(1);
 methodCompilerPass.getSignatureStore().addSignature(methodNameWithSignature, methodNodeWithSignature, TEST_SOURCE);
 methodCompilerPass.processNode(methodNodeWithSignature);
 assertEquals(1, methodCompilerPass.methodDefinitions.size());
 assertTrue(methodCompilerPass.methodDefinitions.containsKey(methodNameWithSignature));

 // Test for non-method property
 String nonMethodProperty = "testProperty";
 Node nonMethodPropertyNode = new Node(Token.NAME, nonMethodProperty);
 nonMethodPropertyNode.setLine(1);
 nonMethodPropertyNode.setColumn(1);
 methodCompilerPass.nonMethodProperties.add(nonMethodProperty);
 methodCompilerPass.processNode(nonMethodPropertyNode);
 assertFalse(methodCompilerPass.methodDefinitions.containsKey(nonMethodProperty));

 // Test for exception when encountering undefined function
 String undefinedFunction = "testUndefinedFunction";
 Node undefinedFunctionNode = new Node(Token.NAME, undefinedFunction);
 undefinedFunctionNode.setLine(1);
 undefinedFunctionNode.setColumn(1);
 try {
 methodCompilerPass.processNode(undefinedFunctionNode);
 fail("Expected Exception");
 } catch (IllegalStateException e) {
 // Expected
 }
 assertEquals(0, methodCompilerPass.methodDefinitions.size());
 }
}