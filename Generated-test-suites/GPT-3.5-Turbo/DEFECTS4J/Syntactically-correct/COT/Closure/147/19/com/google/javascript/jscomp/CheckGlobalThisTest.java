import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        // Test shouldTraverse when function is a constructor
        NodeTraversal t1 = new NodeTraversal(null);
        Node n1 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfo();
        jsDoc1.setConstructor(true);
        n1.setJSDocInfo(jsDoc1);
        assertFalse(checkGlobalThis.shouldTraverse(t1, n1, null));
        
        // Test shouldTraverse when function has @this annotation
        NodeTraversal t2 = new NodeTraversal(null);
        Node n2 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc2 = new JSDocInfo();
        jsDoc2.recordThisType("MyClass");
        n2.setJSDocInfo(jsDoc2);
        assertFalse(checkGlobalThis.shouldTraverse(t2, n2, null));
        
        // Test shouldTraverse when function has @override annotation
        NodeTraversal t3 = new NodeTraversal(null);
        Node n3 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc3 = new JSDocInfo();
        jsDoc3.setOverride(true);
        n3.setJSDocInfo(jsDoc3);
        assertFalse(checkGlobalThis.shouldTraverse(t3, n3, null));
        
        // Test shouldTraverse when parent is not a valid context
        NodeTraversal t4 = new NodeTraversal(null);
        Node n4 = new Node(Token.FUNCTION);
        Node parent4 = new Node(Token.ADD);
        assertFalse(checkGlobalThis.shouldTraverse(t4, n4, parent4));
        
        // Test shouldTraverse when parent is a valid context
        NodeTraversal t5 = new NodeTraversal(null);
        Node n5 = new Node(Token.FUNCTION);
        Node parent5 = new Node(Token.BLOCK);
        assertTrue(checkGlobalThis.shouldTraverse(t5, n5, parent5));
    }
    
    @Test
    public void testVisit() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = new NodeTraversal(null);
        
        // Test visit when n is not a THIS node
        Node n1 = new Node(Token.NAME);
        Node parent1 = new Node(Token.GETPROP);
        Node grandparent1 = new Node(Token.GETPROP);
        checkGlobalThis.visit(t, n1, parent1);
        assertNull(checkGlobalThis.assignLhsChild);
        
        // Test visit when n is a THIS node and shouldReportThis is true
        Node n2 = new Node(Token.THIS);
        Node parent2 = new Node(Token.GETPROP);
        checkGlobalThis.assignLhsChild = n2;
        checkGlobalThis.visit(t, n2, parent2);
        assertNull(checkGlobalThis.assignLhsChild);
        
        // Test visit when n is a THIS node and shouldReportThis is false
        Node n3 = new Node(Token.THIS);
        Node parent3 = new Node(Token.NAME);
        checkGlobalThis.visit(t, n3, parent3);
        assertNotNull(checkGlobalThis.assignLhsChild);
    }
    
    @Test
    public void testGetFunctionJsDoc() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        
        // Test getFunctionJsDocInfo when function has JSDoc
        Node n1 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfo();
        jsDoc1.recordThisType("MyClass");
        n1.setJSDocInfo(jsDoc1);
        JSDocInfo result1 = checkGlobalThis.getFunctionJsDocInfo(n1);
        assertEquals(jsDoc1, result1);
        
        // Test getFunctionJsDocInfo when function does not have JSDoc and parent is NAME
        Node n2 = new Node(Token.FUNCTION);
        Node parent2 = new Node(Token.NAME);
        JSDocInfo jsDoc2 = new JSDocInfo();
        jsDoc2.recordThisType("MyClass");
        parent2.setJSDocInfo(jsDoc2);
        JSDocInfo result2 = checkGlobalThis.getFunctionJsDocInfo(n2);
        assertEquals(jsDoc2, result2);
        
        // Test getFunctionJsDocInfo when function does not have JSDoc and parent is ASSIGN
        Node n3 = new Node(Token.FUNCTION);
        Node parent3 = new Node(Token.ASSIGN);
        JSDocInfo jsDoc3 = new JSDocInfo();
        jsDoc3.recordThisType("MyClass");
        parent3.setJSDocInfo(jsDoc3);
        Node grandparent3 = new Node(Token.VAR);
        grandparent3.addChildToBack(parent3);
        JSDocInfo result3 = checkGlobalThis.getFunctionJsDocInfo(n3);
        assertEquals(jsDoc3, result3);
        
        // Test getFunctionJsDocInfo when function does not have JSDoc and parent is not a valid context
        Node n4 = new Node(Token.FUNCTION);
        Node parent4 = new Node(Token.ADD);
        JSDocInfo result4 = checkGlobalThis.getFunctionJsDocInfo(n4);
        assertNull(result4);
    }
}