import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler compiler;
    private CheckLevel level;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = new CheckLevel();
        checkGlobalThis = new CheckGlobalThis(compiler, level);
    }
    
    @Test
    public void testShouldTraverse_GlobalThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_ConstructorAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.NAME);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_ThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.NAME);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_NoLogicalPlace() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(Token.NAME);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_ParentNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = null;
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_AssignLhsChildNotNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        n = new Node(Token.VAR);
        parent = new Node(Token.NAME);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testVisit_GlobalThis() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        t.setCurrentNode(n);
        checkGlobalThis.visit(t, n, parent);
        
        assertEquals(1, compiler.getReportedErrors().size());
    }
    
    @Test
    public void testVisit_AssignLhsChildNotNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.visit(t, n, parent);
        parent = new Node(Token.ASSIGN);
        t.setCurrentNode(parent);
        n = new Node(Token.THIS);
        
        checkGlobalThis.visit(t, n, parent);
        
        assertNull(checkGlobalThis.assignLhsChild);
    }
}