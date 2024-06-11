import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test with a function that is a constructor
        NodeTraversal t1 = new NodeTraversal();
        Node n1 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfo();
        jsDoc1.setConstructor(true);
        n1.setJSDocInfo(jsDoc1);
        assertFalse(checkGlobalThis.shouldTraverse(t1, n1, null));
        
        // Test with a function that has the @this annotation
        NodeTraversal t2 = new NodeTraversal();
        Node n2 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc2 = new JSDocInfo();
        jsDoc2.recordThisType();
        n2.setJSDocInfo(jsDoc2);
        assertFalse(checkGlobalThis.shouldTraverse(t2, n2, null));
        
        // Test with a function that cannot have a @this annotation
        NodeTraversal t3 = new NodeTraversal();
        Node n3 = new Node(Token.FUNCTION);
        assertFalse(checkGlobalThis.shouldTraverse(t3, n3, null));
        
        // Test with a function assigned to a variable
        NodeTraversal t4 = new NodeTraversal();
        Node parent4 = new Node(Token.NAME);
        parent4.setJSDocInfo(null);
        Node n4 = new Node(Token.FUNCTION);
        boolean result4 = checkGlobalThis.shouldTraverse(t4, n4, parent4);
        assertTrue(result4);
        assertFalse(checkGlobalThis.shouldTraverse(t4, parent4, null));
        
        // Test with a function assigned to a variable using var keyword
        NodeTraversal t5 = new NodeTraversal();
        Node grandparent5 = new Node(Token.VAR);
        grandparent5.setJSDocInfo(null);
        Node parent5 = new Node(Token.NAME);
        parent5.setJSDocInfo(null);
        Node n5 = new Node(Token.FUNCTION);
        boolean result5 = checkGlobalThis.shouldTraverse(t5, n5, parent5);
        assertTrue(result5);
        assertFalse(checkGlobalThis.shouldTraverse(t5, parent5, grandparent5));
    }
    
    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        
        // Test when assignLhsChild is null
        checkGlobalThis.visit(t, n, parent);
        assertNull(checkGlobalThis.assignLhsChild);
        
        // Test when assignLhsChild is not null
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, null);
        assertNull(checkGlobalThis.assignLhsChild);
    }
    
    @Test
    public void testShouldReportThis() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        
        // Test with assignLhsChild not null
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);
        boolean result1 = checkGlobalThis.shouldReportThis(n, parent);
        assertTrue(result1);
        
        // Test with assignLhsChild null
        checkGlobalThis.assignLhsChild = null;
        boolean result2 = checkGlobalThis.shouldReportThis(n, parent);
        assertFalse(result2);
    }
    
    @Test
    public void testGetFunctionJsDocInfo() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
        
        // Test with function that has JSDoc info
        Node n1 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfo();
        n1.setJSDocInfo(jsDoc1);
        JSDocInfo result1 = checkGlobalThis.getFunctionJsDocInfo(n1);
        assertEquals(jsDoc1, result1);
        
        // Test with function that does not have JSDoc info but parent has
        Node parent2 = new Node(Token.NAME);
        parent2.setJSDocInfo(null);
        Node n2 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc2 = new JSDocInfo();
        parent2.setJSDocInfo(jsDoc2);
        JSDocInfo result2 = checkGlobalThis.getFunctionJsDocInfo(n2);
        assertEquals(jsDoc2, result2);
        
        // Test with function that does not have JSDoc info and parent does not have JSDoc info
        Node parent3 = new Node(Token.NAME);
        parent3.setJSDocInfo(null);
        Node n3 = new Node(Token.FUNCTION);
        JSDocInfo result3 = checkGlobalThis.getFunctionJsDocInfo(n3);
        assertNull(result3);
    }
}