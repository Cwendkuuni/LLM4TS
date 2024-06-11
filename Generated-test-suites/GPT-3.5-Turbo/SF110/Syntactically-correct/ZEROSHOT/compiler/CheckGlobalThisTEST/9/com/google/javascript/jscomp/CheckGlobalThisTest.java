import org.junit.*;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionConstructor_DoNotTraverse() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.FUNCTION);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        boolean expResult = false;
        boolean result = instance.shouldTraverse(t, n, parent);
        assertEquals(expResult, result);
    }

    @Test
    public void testShouldTraverse_FunctionNoAnnotations_Traverse() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        boolean expResult = true;
        boolean result = instance.shouldTraverse(t, n, parent);
        assertEquals(expResult, result);
    }

    @Test
    public void testShouldTraverse_Assignment_LeftSide_Traverse() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        boolean expResult = true;
        boolean result = instance.shouldTraverse(t, n, parent);
        assertEquals(expResult, result);
    }

    @Test
    public void testShouldTraverse_Assignment_RightSide_NotPrototypeProperty_DoNotTraverse() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        boolean expResult = false;
        boolean result = instance.shouldTraverse(t, n, parent);
        assertEquals(expResult, result);
    }

    @Test
    public void testShouldTraverse_Assignment_RightSide_PrototypeProperty_DoNotTraverse() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        n.addChildToFront(new Node(Token.STRING));
        n.addChildToFront(new Node(Token.THIS));
        parent.addChildToFront(n);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        boolean expResult = false;
        boolean result = instance.shouldTraverse(t, n, parent);
        assertEquals(expResult, result);
    }

    @Test
    public void testVisit_ThisAndShouldReportThis_ReturnTrue() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        instance.assignLhsChild = null;
        boolean expResult = true;
        boolean result = instance.shouldReportThis(n);
        assertEquals(expResult, result);
    }

    @Test
    public void testVisit_ThisAndShouldReportThis_ReturnFalse() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        instance.assignLhsChild = new Node(Token.THIS);
        boolean expResult = false;
        boolean result = instance.shouldReportThis(n);
        assertEquals(expResult, result);
    }

    @Test
    public void testShouldReportThis_AssignLhsChildNull_ReturnTrue() {
        Node n = new Node(Token.THIS);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        instance.assignLhsChild = null;
        boolean expResult = true;
        boolean result = instance.shouldReportThis(n);
        assertEquals(expResult, result);
    }

    @Test
    public void testShouldReportThis_AssignLhsChildNotNull_ReturnFalse() {
        Node n = new Node(Token.THIS);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        instance.assignLhsChild = new Node(Token.THIS);
        boolean expResult = false;
        boolean result = instance.shouldReportThis(n);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJsDocInfo_ReturnNull() {
        Node n = new Node(Token.FUNCTION);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        JSDocInfo expResult = null;
        JSDocInfo result = instance.getFunctionJsDocInfo(n);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_JsDocInfoInNode_ReturnJsDocInfo() {
        Node n = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        JSDocInfo expResult = jsDoc;
        JSDocInfo result = instance.getFunctionJsDocInfo(n);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJsDocInfoInNode_ParentIsName_ReturnJsDocInfoFromParent() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        n.setParent(parent);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        JSDocInfo expResult = jsDoc;
        JSDocInfo result = instance.getFunctionJsDocInfo(n);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJsDocInfoInNode_ParentIsAssign_ReturnJsDocInfoFromParent() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        JSDocInfo jsDoc = new JSDocInfo();
        parent.setJSDocInfo(jsDoc);
        n.setParent(parent);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        JSDocInfo expResult = jsDoc;
        JSDocInfo result = instance.getFunctionJsDocInfo(n);
        assertEquals(expResult, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_NoJsDocInfoInNode_ParentIsVar_ReturnJsDocInfoFromGrandparent() {
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.VAR);
        Node gramps = new Node(Token.FUNCTION);
        JSDocInfo jsDoc = new JSDocInfo();
        gramps.setJSDocInfo(jsDoc);
        parent.setParent(gramps);
        n.setParent(parent);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        JSDocInfo expResult = jsDoc;
        JSDocInfo result = instance.getFunctionJsDocInfo(n);
        assertEquals(expResult, result);
    }

    @Test
    public void testVisit_AssignLhsChildNull_AssignLhsChildNull() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.THIS);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        instance.assignLhsChild = null;
        instance.visit(t, n, parent);
        assertNull(instance.assignLhsChild);
    }

    @Test
    public void testVisit_AssignLhsChildNotNull_AssignLhsChildNull() {
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.THIS);
        CheckGlobalThis instance = new CheckGlobalThis(null);
        instance.assignLhsChild = new Node(Token.THIS);
        instance.visit(t, n, parent);
        assertNull(instance.assignLhsChild);
    }

    @Test
    public void testVisit_ThisAndShouldReportThis_ReportError() {
        AbstractCompiler compiler = new AbstractCompiler();
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        CheckGlobalThis instance = new CheckGlobalThis(compiler);
        instance.assignLhsChild = null;
        instance.visit(t, n, parent);
        assertEquals(1, compiler.getErrorCount());
    }

    @Test
    public void testVisit_ThisAndShouldReportThis_DoNotReportError() {
        AbstractCompiler compiler = new AbstractCompiler();
        NodeTraversal t = new NodeTraversal(null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ADD);
        CheckGlobalThis instance = new CheckGlobalThis(compiler);
        instance.assignLhsChild = new Node(Token.THIS);
        instance.visit(t, n, parent);
        assertEquals(0, compiler.getErrorCount());
    }
}