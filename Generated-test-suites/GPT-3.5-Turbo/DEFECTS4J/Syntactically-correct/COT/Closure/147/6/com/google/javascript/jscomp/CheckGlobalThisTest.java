
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test when node type is not a function
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
        
        // Test when node type is function with @constructor annotation
        n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
        
        // Test when node type is function with @this annotation
        n = new Node(Token.FUNCTION);
        jsDoc.setThisType(new Node(Token.STRING, "thisType"));
        n.setJSDocInfo(jsDoc);
        result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
        
        // Test when node type is function with no logical place to put a @this annotation
        n = new Node(Token.FUNCTION);
        parent.setType(Token.BLOCK);
        Node grandparent = new Node(Token.NAME);
        parent.setParent(grandparent);
        grandparent.setFirstChild(parent);
        result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
        
        // Test when node type is function with a logical place to put a @this annotation
        n = new Node(Token.FUNCTION);
        parent.setType(Token.BLOCK);
        result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
        
        // Test when parent node type is ASSIGN and current node is left side of assignment
        n = new Node(Token.NAME);
        parent = new Node(Token.ASSIGN);
        parent.setFirstChild(n);
        Node rhs = new Node(Token.ARRAYLIT);
        parent.setSecondChild(rhs);
        result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(true, result);
        
        // Test when parent node type is ASSIGN and current node is right side of assignment
        n = new Node(Token.NAME);
        parent = new Node(Token.ASSIGN);
        parent.setSecondChild(n);
        n.setJSDocInfo(null);
        parent.setFirstChild(rhs);
        Node lhs = new Node(Token.NAME);
        lhs.setJSDocInfo(null);
        rhs.setFirstChild(lhs);
        lhs.setFirstChild(new Node(Token.NAME, "prototype"));
        n = new Node(Token.NAME, "this");
        rhs.addChildToBack(n);
        result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertEquals(false, result);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test when node type is THIS and shouldReportThis returns false
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node();
        boolean result = checkGlobalThis.shouldReportThis(n, parent);
        assertEquals(false, result);
        
        // Test when node type is THIS and shouldReportThis returns true
        parent = new Node(Token.GETPROP);
        result = checkGlobalThis.shouldReportThis(n, parent);
        assertEquals(true, result);
        
        // Test when node is assignLhsChild
        checkGlobalThis.assignLhsChild = n;
        result = checkGlobalThis.shouldReportThis(n, parent);
        assertEquals(true, result);
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = new CheckLevel();
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        
        // Test when JSDocInfo is available directly in the node
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
        
        // Test when JSDocInfo is in the parent node
        n = new Node(Token.NAME);
        Node parent = new Node(Token.FUNCTION);
        jsDoc.setConstructor(true);
        parent.setJSDocInfo(jsDoc);
        n.setParent(parent);
        result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
        
        // Test when JSDocInfo is in the grandparent node
        Node grandparent = new Node(Token.VAR);
        jsDoc = new JSDocInfo();
        grandparent.setJSDocInfo(jsDoc);
        parent.setParent(grandparent);
        result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }
}
