import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

 private AbstractCompiler compiler;
 private Callback checkGlobalThis;

 @Test
 public void testCheckGlobalThis() {
 checkGlobalThis = new CheckGlobalThis(mock(AbstractCompiler.class), WARNING);

 // Test constructor function
 Node constructorNode = Node.newFunctionNode("constructorFunction", null, null, new Node[0]);
 testCheckFunctionNode(constructorNode, true);

 // Test function without JSDoc or constructor annotation
 Node noAnnotationFunctionNode = Node.newFunctionNode("noAnnotationFunction", null, null, new Node[0]);
 testCheckFunctionNode(noAnnotationFunctionNode, false);

 // Test function with @this annotation
 Node thisAnnotationFunctionNode = Node.newFunctionNode("thisAnnotationFunction", null, new Node("@this"), new Node[0]);
 testCheckFunctionNode(thisAnnotationFunctionNode, true);

 // Test function with @constructor annotation
 Node constructorAnnotationFunctionNode = Node.newFunctionNode("constructorAnnotationFunction", null, new Node("@constructor"), new Node[0]);
 testCheckFunctionNode(constructorAnnotationFunctionNode, true);

 // Test function inside an object
 Node objectNode = Node.newObjectLiteralNode("obj", new Node("prop", Node.newFunctionNode("functionInsideObject", null, null, new Node[0])));
 testCheckFunctionNode(objectNode, false);

 // Test function inside a class
 Node classNode = Node.newClassNode("Class", null, new Node[0], new Node[0], new Node[0]);
 Node functionInsideClassNode = Node.newFunctionNode("functionInsideClass", null, null, new Node[0]);
 Node methodNode = Node.newMethodNode("method", null, null, null, functionInsideClassNode, new Node[0]);
 Node classPropertyNode = Node.newPropertyNode("prop", classNode, methodNode);
 testCheckFunctionNode(classPropertyNode, false);
 }

 private void testCheckFunctionNode(Node node, boolean expectedResult) {
 NodeTraversal traversal = mock(NodeTraversal.class);
 when(traversal.makeError(any(Node.class), eq(WARNING), any(DiagnosticType.class))).thenReturn(new Node[0]);
 checkGlobalThis.visit(traversal, node, null);
 Node parentNode = Node.newBlockNode(new Node[0]);
 when(node.getParent()).thenReturn(parentNode);
 when(parentNode.getType()).thenReturn(Token.SCRIPT);
 boolean actualResult = checkGlobalThis.shouldTraverse(traversal, node, null);
 org.junit.Assert.assertEquals(expectedResult, actualResult);
 }
}