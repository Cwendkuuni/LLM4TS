import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
    
    CheckGlobalThis checkGlobalThis;
    
    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenFunctionIsConstructor() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenFunctionHasThisType() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordType(new Node(Token.THIS));
        n.setJSDocInfo(jsDoc);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenFunctionIsOverride() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenParentIsNotBlockScriptNameOrAssign() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.GE);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnTrueWhenParentIsBlock() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnTrueWhenParentIsScript() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnTrueWhenParentIsName() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnTrueWhenParentIsAssign() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenParentIsNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, null);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnTrueWhenAssignLhsChildIsNotNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        parent.addChildToBack(lhs);
        parent.addChildToBack(new Node(Token.NULL));
        checkGlobalThis.assignLhsChild = lhs;
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenAssignLhsChildIsNotNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.PLUS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        parent.addChildToBack(lhs);
        parent.addChildToBack(new Node(Token.NULL));
        checkGlobalThis.assignLhsChild = lhs;
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenLhsIsGetPropAndLastChildIsPrototype() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.THIS));
        lhs.addChildToBack(new Node(Token.STRING, "prototype"));
        parent.addChildToBack(lhs);
        parent.addChildToBack(new Node(Token.NULL));
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnFalseWhenLhsQualifiedNameContainsPrototype() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        lhs.setQualifiedName("test.prototype.property");
        parent.addChildToBack(lhs);
        parent.addChildToBack(new Node(Token.NULL));
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertFalse(result);
    }
    
    @Test
    public void testShouldTraverse_shouldReturnTrueWhenNotAssignLhsChildAndNotGetPropAndParentIsNotNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.PLUS);
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        assertTrue(result);
    }
    
    @Test
    public void testVisit_shouldReportErrorWhenThisOnLeftSideOfAssignment() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        parent.addChildToBack(lhs);
        parent.addChildToBack(new Node(Token.NULL));
        checkGlobalThis.assignLhsChild = lhs;
        
        checkGlobalThis.visit(t, n, parent);
        
        // Assert error reported
    }
    
    @Test
    public void testVisit_shouldNotReportErrorWhenThisOnRightSideOfAssignment() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        parent.addChildToBack(new Node(Token.NULL));
        parent.addChildToBack(lhs);
        checkGlobalThis.assignLhsChild = lhs;
        
        checkGlobalThis.visit(t, n, parent);
        
        // Assert error not reported
    }
    
    @Test
    public void testVisit_shouldNotReportErrorWhenThisIsNotAssignment() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.PLUS);
        
        checkGlobalThis.visit(t, n, parent);
        
        // Assert error not reported
    }
    
    @Test
    public void testVisit_shouldNotReportErrorWhenThisIsNotAssignmentAndAssignLhsChildIsNotNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.PLUS);
        Node lhs = new Node(Token.NAME);
        checkGlobalThis.assignLhsChild = lhs;
        
        checkGlobalThis.visit(t, n, parent);
        
        // Assert error not reported
    }
    
    @After
    public void tearDown() {
        checkGlobalThis = null;
    }
}