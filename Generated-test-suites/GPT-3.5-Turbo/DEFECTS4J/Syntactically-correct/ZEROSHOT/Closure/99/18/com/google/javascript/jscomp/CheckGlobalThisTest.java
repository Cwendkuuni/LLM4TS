import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_FunctionConstructorWithThisAnnotation_ReturnFalse() {
        // Test if shouldTraverse returns false for function with @this annotation
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        Node parent = new Node(null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithNoAnnotations_ReturnTrue() {
        // Test if shouldTraverse returns true for function with no annotations
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        Node parent = new Node(null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_NonFunction_ReturnFalse() {
        // Test if shouldTraverse returns false for non-function nodes
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        n.setType(Token.NUMBER);
        Node parent = new Node(null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnFalse() {
        // Test if shouldTraverse returns false for function with @constructor annotation
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithThisAnnotation_ReturnFalse() {
        // Test if shouldTraverse returns false for function with @this annotation
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnFalse() {
        // Test if shouldTraverse returns false for function with @override annotation
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setOverride(true);
        n.setJSDocInfo(jsDoc);
        Node parent = new Node(null);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_Block_ReturnTrue() {
        // Test if shouldTraverse returns true for block node
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        Node parent = new Node(null);
        parent.setType(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_Script_ReturnTrue() {
        // Test if shouldTraverse returns true for script node
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        Node parent = new Node(null);
        parent.setType(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_Name_ReturnTrue() {
        // Test if shouldTraverse returns true for name node
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        Node parent = new Node(null);
        parent.setType(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_Assign_ReturnTrue() {
        // Test if shouldTraverse returns true for assign node
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        Node parent = new Node(null);
        parent.setType(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testVisit_ThisOnLeftSideOfAssign_LevelWarning() {
        // Test if visit reports a warning for THIS on the left side of assign
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        NodeTraversal t = null;
        Node n = new Node(null);
        n.setType(Token.THIS);
        Node parent = new Node(null);
        boolean result = checkGlobalThis.visit(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testVisit_ThisOnRightSideOfAssignWithoutPrototype_ReturnTrue() {
        // Test if visit returns true for THIS on the right side of assign without prototype
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        n.setType(Token.THIS);
        Node parent = new Node(null);
        parent.setType(Token.GETPROP);
        parent.getLastChild().setString("property");
        boolean result = checkGlobalThis.visit(t, n, parent);
        assertTrue(result);
    }

    @Test
    public void testVisit_ThisOnRightSideOfAssignWithPrototype_ReturnFalse() {
        // Test if visit returns false for THIS on the right side of assign with prototype
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        NodeTraversal t = null;
        Node n = new Node(null);
        n.setType(Token.THIS);
        Node parent = new Node(null);
        parent.setType(Token.GETPROP);
        Node lhs = new Node(null);
        lhs.setType(Token.GETPROP);
        lhs.getLastChild().setString("prototype");
        parent.addChildToFront(lhs);
        boolean result = checkGlobalThis.visit(t, n, parent);
        assertFalse(result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithJSDoc_ReturnJSDocInfo() {
        // Test if getFunctionJsDocInfo returns JSDocInfo for function with JSDoc
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(null);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(jsDoc, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithParentName_ReturnParentJSDocInfo() {
        // Test if getFunctionJsDocInfo returns parent JSDocInfo for function with parent name node
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(null);
        Node parent = new Node(null);
        parent.setType(Token.NAME);
        parent.setJSDocInfo(new JSDocInfo());
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(parent.getJSDocInfo(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithParentAssign_ReturnParentJSDocInfo() {
        // Test if getFunctionJsDocInfo returns parent JSDocInfo for function with parent assign node
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(null);
        Node parent = new Node(null);
        parent.setType(Token.ASSIGN);
        parent.setJSDocInfo(new JSDocInfo());
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(parent.getJSDocInfo(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithParentVar_ReturnParentJSDocInfo() {
        // Test if getFunctionJsDocInfo returns grandparent JSDocInfo for function with parent var node
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(null);
        Node parent = new Node(null);
        parent.setType(Token.NAME);
        Node gramps = new Node(null);
        gramps.setType(Token.VAR);
        gramps.setJSDocInfo(new JSDocInfo());
        parent.setParent(gramps);
        parent.setJSDocInfo(new JSDocInfo());
        n.setParent(parent);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertEquals(gramps.getJSDocInfo(), result);
    }

    @Test
    public void testGetFunctionJsDocInfo_FunctionWithNoJSDoc_ReturnNull() {
        // Test if getFunctionJsDocInfo returns null for function with no JSDoc
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
        Node n = new Node(null);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
        assertNull(result);
    }

}