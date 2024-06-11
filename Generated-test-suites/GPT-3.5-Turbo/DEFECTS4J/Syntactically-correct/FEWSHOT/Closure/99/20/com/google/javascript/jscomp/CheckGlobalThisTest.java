import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.apache.commons.cli.CheckLevel;
import org.apache.commons.cli.NodeTraversal;
import org.junit.Test;

public class CheckGlobalThisTest {
    
    @Test
    public void testShouldTraverse_constructorFunction() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_functionWithThisAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_functionWithOverrideAnnotation() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_functionInsideLogicalBlock() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.LOGICALBLOCK);
        
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_functionInsideAssign() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_functionInsideInvalidParent() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.PLUS);
        
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }
    
    @Test
    public void testShouldTraverse_assignLhsChildIsNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        Node assignLhsChild = null;
        
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldTraverse_assignLhsChildIsNotNull() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        Node assignLhsChild = new Node(Token.THIS);
        
        boolean result = CheckGlobalThis.shouldTraverse(t, n, parent);
        
        assertEquals(false, result);
    }

    @Test
    public void testVisit_globalThisInAssignLhs() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node assignLhsChild = new Node(Token.THIS);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.ERROR);
        checkGlobalThis.setAssignLhsChild(assignLhsChild);
        
        checkGlobalThis.visit(t, n, parent);
        
        assertEquals(null, checkGlobalThis.getAssignLhsChild());
    }
    
    @Test
    public void testVisit_globalThisWithPropertyAccess() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.ERROR);
        
        checkGlobalThis.visit(t, n, parent);
        
        // Assert that the appropriate error is reported
    }
    
    @Test
    public void testShouldReportThis_assignLhsChildIsNull() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        Node assignLhsChild = null;
        
        boolean result = CheckGlobalThis.shouldReportThis(n, parent, assignLhsChild);
        
        assertEquals(true, result);
    }
    
    @Test
    public void testShouldReportThis_assignLhsChildIsNotNull() {
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        Node assignLhsChild = new Node(Token.THIS);
        
        boolean result = CheckGlobalThis.shouldReportThis(n, parent, assignLhsChild);
        
        assertEquals(false, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_withJsDocInfoInNode() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_withJsDocInfoInParentNameNode() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        n.setParent(parent);
        
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_withJsDocInfoInParentAssignNode() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        n.setParent(parent);
        
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(jsDoc, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_withoutJsDocInfoInNode() {
        Node n = new Node(Token.FUNCTION);
        
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_withoutJsDocInfoInParent() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        n.setParent(parent);
        
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(null, result);
    }
    
    @Test
    public void testGetFunctionJsDocInfo_withoutJsDocInfoInParentAssignNode() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        n.setParent(parent);
        
        JSDocInfo result = CheckGlobalThis.getFunctionJsDocInfo(n);
        
        assertEquals(null, result);
    }
}