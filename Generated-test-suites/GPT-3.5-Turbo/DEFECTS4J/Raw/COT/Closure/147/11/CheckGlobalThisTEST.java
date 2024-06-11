
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_functionNotConstructor_noAnnotations_shouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node node = new Node(Token.FUNCTION);
        boolean result = checkGlobalThis.shouldTraverse(traversal, node, null);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_functionConstructor_noAnnotations_shouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        node.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(traversal, node, null);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_functionNotConstructor_thisAnnotation_shouldNotTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType(null);
        node.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(traversal, node, null);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_functionInterfaceAnnotation_shouldNotTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setInterface(true);
        node.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(traversal, node, null);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_functionOverrideAnnotation_shouldNotTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        node.setJSDocInfo(jsDoc);
        boolean result = checkGlobalThis.shouldTraverse(traversal, node, null);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_innerFunction_noParent_shouldNotTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node parent = new Node(Token.FUNCTION);
        Node node = new Node(Token.FUNCTION);
        
        boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
        
        Assert.assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_innerFunction_inBlock_shouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node parent = new Node(Token.BLOCK);
        Node node = new Node(Token.FUNCTION);
        
        boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_innerFunction_inScript_shouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node parent = new Node(Token.SCRIPT);
        Node node = new Node(Token.FUNCTION);
        
        boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_innerFunction_inName_shouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node parent = new Node(Token.NAME);
        Node node = new Node(Token.FUNCTION);
        
        boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_innerFunction_inAssign_shouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node parent = new Node(Token.ASSIGN);
        Node node = new Node(Token.FUNCTION);
        
        boolean result = checkGlobalThis.shouldTraverse(traversal, node, parent);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testVisit_thisNode_shouldReport() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        
        checkGlobalThis.visit(traversal, node, parent);
        
        Assert.assertEquals(1, checkGlobalThis.compiler.getErrors().size());
        Assert.assertEquals(1, checkGlobalThis.compiler.getWarnings().size());
    }
    
    @Test
    public void testVisit_thisNode_assignLhsChild_notNull_shouldReport() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.shouldTraverse(null, null, parent);
        
        checkGlobalThis.visit(traversal, node, parent);
        
        Assert.assertEquals(1, checkGlobalThis.compiler.getErrors().size());
        Assert.assertEquals(1, checkGlobalThis.compiler.getWarnings().size());
    }
    
    @Test
    public void testVisit_thisNode_assignLhsChild_null_shouldNotReport() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal traversal = new NodeTraversal(null, checkGlobalThis);
        
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        
        checkGlobalThis.visit(traversal, node, parent);
        
        Assert.assertEquals(0, checkGlobalThis.compiler.getErrors().size());
        Assert.assertEquals(0, checkGlobalThis.compiler.getWarnings().size());
    }
}
