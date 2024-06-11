import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse() {
        // Create a CheckGlobalThis object
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test case 1: FUNCTION node with @constructor annotation - should return false
        NodeTraversal t1 = new NodeTraversal();
        Node n1 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfo();
        jsDoc1.setConstructor(true);
        n1.setJSDocInfo(jsDoc1);
        boolean result1 = checkGlobalThis.shouldTraverse(t1, n1, null);
        assertEquals(false, result1);
        
        // Test case 2: FUNCTION node with @this annotation - should return false
        NodeTraversal t2 = new NodeTraversal();
        Node n2 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc2 = new JSDocInfo();
        jsDoc2.recordThisType();
        n2.setJSDocInfo(jsDoc2);
        boolean result2 = checkGlobalThis.shouldTraverse(t2, n2, null);
        assertEquals(false, result2);
        
        // Test case 3: FUNCTION node with no annotations - should return true
        NodeTraversal t3 = new NodeTraversal();
        Node n3 = new Node(Token.FUNCTION);
        boolean result3 = checkGlobalThis.shouldTraverse(t3, n3, null);
        assertEquals(true, result3);
        
        // Test case 4: Not a FUNCTION node - should return true
        NodeTraversal t4 = new NodeTraversal();
        Node n4 = new Node(Token.NAME);
        boolean result4 = checkGlobalThis.shouldTraverse(t4, n4, null);
        assertEquals(true, result4);
    }
    
    @Test
    public void testVisit() {
        // Create a CheckGlobalThis object
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test case 1: Node is not THIS - should not report an error
        NodeTraversal t1 = new NodeTraversal();
        Node n1 = new Node(Token.NAME);
        Node parent1 = new Node(Token.GETPROP);
        checkGlobalThis.visit(t1, n1, parent1);
        assertEquals(0, compiler.getErrors().size());
        
        // Test case 2: Node is THIS but shouldReportThis returns false - should not report an error
        NodeTraversal t2 = new NodeTraversal();
        Node n2 = new Node(Token.THIS);
        Node parent2 = new Node(Token.GETPROP);
        checkGlobalThis.visit(t2, n2, parent2);
        assertEquals(0, compiler.getErrors().size());
        
        // Test case 3: Node is THIS and shouldReportThis returns true - should report an error
        NodeTraversal t3 = new NodeTraversal();
        Node n3 = new Node(Token.THIS);
        Node parent3 = null;
        checkGlobalThis.visit(t3, n3, parent3);
        assertEquals(1, compiler.getErrors().size());
        
        // Test case 4: n is assignLhsChild but shouldReportThis returns false - should not report an error
        NodeTraversal t4 = new NodeTraversal();
        Node n4 = new Node(Token.NAME);
        Node parent4 = new Node(Token.ASSIGN);
        checkGlobalThis.visit(t4, n4, parent4);
        assertEquals(0, compiler.getErrors().size());
        
        // Test case 5: n is assignLhsChild and shouldReportThis returns true - should report an error
        NodeTraversal t5 = new NodeTraversal();
        Node n5 = new Node(Token.THIS);
        Node parent5 = new Node(Token.ASSIGN);
        checkGlobalThis.visit(t5, n5, parent5);
        assertEquals(1, compiler.getErrors().size());
    }
    
    @Test
    public void testGetFunctionJsDocInfo() {
        // Create a CheckGlobalThis object
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test case 1: Function node has JSDocInfo - should return the JSDocInfo
        Node n1 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfo();
        n1.setJSDocInfo(jsDoc1);
        JSDocInfo result1 = checkGlobalThis.getFunctionJsDocInfo(n1);
        assertEquals(jsDoc1, result1);
        
        // Test case 2: Function node does not have JSDocInfo but parent is NAME node with JSDocInfo - should return parent's JSDocInfo
        Node n2 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc2 = new JSDocInfo();
        Node parent2 = new Node(Token.NAME);
        parent2.setJSDocInfo(jsDoc2);
        n2.setParent(parent2);
        JSDocInfo result2 = checkGlobalThis.getFunctionJsDocInfo(n2);
        assertEquals(jsDoc2, result2);
        
        // Test case 3: Function node does not have JSDocInfo and parent is NAME node without JSDocInfo - should return grandparent's JSDocInfo
        Node n3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.NAME);
        Node grandparent3 = new Node(Token.VAR);
        JSDocInfo jsDoc3 = new JSDocInfo();
        grandparent3.setJSDocInfo(jsDoc3);
        parent3.setParent(grandparent3);
        n3.setParent(parent3);
        JSDocInfo result3 = checkGlobalThis.getFunctionJsDocInfo(n3);
        assertEquals(jsDoc3, result3);
        
        // Test case 4: No JSDocInfo found - should return null
        Node n4 = new Node(Token.FUNCTION);
        JSDocInfo result4 = checkGlobalThis.getFunctionJsDocInfo(n4);
        assertNull(result4);
    }
}