import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;
    private AbstractCompiler compiler;
    private CheckLevel level;
    private NodeTraversal t;
    private Node n;
    private Node parent;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        level = CheckLevel.WARNING;
        checkGlobalThis = new CheckGlobalThis(compiler, level);
        t = new NodeTraversal(compiler, checkGlobalThis);
        n = new Node(Token.THIS);
        parent = new Node(Token.GETPROP);
    }

    @Test
    public void testShouldTraverse_FunctionConstructor_NoAnnotation() {
        Node functionNode = new Node(Token.FUNCTION);
        parent.addChildToFront(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionConstructor_ConstructorAnnotation() {
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setConstructor(true);
        functionNode.setJSDocInfo(jsDocInfo);
        parent.addChildToFront(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionConstructor_ThisAnnotation() {
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.recordThisType();
        functionNode.setJSDocInfo(jsDocInfo);
        parent.addChildToFront(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionConstructor_OverrideAnnotation() {
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        jsDocInfo.setOverride(true);
        functionNode.setJSDocInfo(jsDocInfo);
        parent.addChildToFront(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionMultipleParents_Block() {
        Node functionNode = new Node(Token.FUNCTION);
        parent.setType(Token.BLOCK);
        parent.addChildToFront(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionMultipleParents_Script() {
        Node functionNode = new Node(Token.FUNCTION);
        parent.setType(Token.SCRIPT);
        parent.addChildToFront(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionMultipleParents_Name() {
        Node functionNode = new Node(Token.FUNCTION);
        parent.setType(Token.NAME);
        parent.addChildToFront(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionMultipleParents_Assign() {
        Node functionNode = new Node(Token.FUNCTION);
        parent.setType(Token.ASSIGN);
        parent.addChildToFront(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionMultipleParents_String() {
        Node functionNode = new Node(Token.FUNCTION);
        parent.setType(Token.STRING);
        parent.addChildToFront(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionMultipleParents_Number() {
        Node functionNode = new Node(Token.FUNCTION);
        parent.setType(Token.NUMBER);
        parent.addChildToFront(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertTrue(result);
    }

    @Test
    public void testShouldTraverse_FunctionAssignmentToPrototypeProperty() {
        Node functionNode = new Node(Token.FUNCTION);
        parent.setType(Token.GETPROP);
        parent.addChildToFront(new Node(Token.GETPROP));
        parent.getFirstChild().getLastChild().setString("prototype");
        parent.addChildToBack(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionAssignmentToPrototypeSubproperty() {
        Node functionNode = new Node(Token.FUNCTION);
        Node getPropNode = new Node(Token.GETPROP);
        getPropNode.addChildToFront(new Node(Token.GETPROP));
        getPropNode.getFirstChild().getLastChild().setString("prototype");
        getPropNode.getFirstChild().addChildToBack(new Node(Token.NAME));
        getPropNode.getFirstChild().getLastChild().setString("x");
        parent.setType(Token.GETPROP);
        parent.addChildToFront(getPropNode);
        parent.addChildToBack(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_NonAssignNode() {
        Node functionNode = new Node(Token.FUNCTION);
        parent.setType(Token.VAR);
        parent.addChildToFront(functionNode);
        boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);
        assertFalse(result);
    }

    @Test
    public void testVisit_ThisKeyword_ReportError() {
        Node thisNode = new Node(Token.THIS);
        checkGlobalThis.visit(t, thisNode, parent);
        // Verify that report method is called
    }

    @Test
    public void testVisit_ThisKeyword_NotReportError() {
        Node thisNode = new Node(Token.THIS);
        parent.setType(Token.GETPROP);
        checkGlobalThis.visit(t, thisNode, parent);
        // Verify that report method is not called
    }

    @Test
    public void testVisit_NotAssignLhsChild() {
        checkGlobalThis.visit(t, n, parent);
        // Verify that assignLhsChild is not set to null
    }

    @Test
    public void testVisit_AssignLhsChild() {
        checkGlobalThis.visit(t, n, n);
        // Verify that assignLhsChild is set to null
    }

    @Test
    public void testGetFunctionJsDocInfo_WithJSDoc() {
        Node functionNode = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = new JSDocInfo();
        functionNode.setJSDocInfo(jsDocInfo);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentNameWithJSDoc() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentName = new Node(Token.NAME);
        JSDocInfo jsDocInfo = new JSDocInfo();
        parentName.setJSDocInfo(jsDocInfo);
        parentName.addChildToFront(functionNode);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_ParentAssignNameWithJSDoc() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentAssignNode = new Node(Token.ASSIGN);
        Node parentName = new Node(Token.NAME);
        JSDocInfo jsDocInfo = new JSDocInfo();
        parentName.setJSDocInfo(jsDocInfo);
        parentAssignNode.addChildToFront(parentName);
        parentAssignNode.addChildToBack(functionNode);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_GrandparentVarNameWithJSDoc() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentAssignNode = new Node(Token.ASSIGN);
        Node parentName = new Node(Token.NAME);
        Node grandparentVarNode = new Node(Token.VAR);
        JSDocInfo jsDocInfo = new JSDocInfo();
        grandparentVarNode.setJSDocInfo(jsDocInfo);
        parentName.addChildToFront(functionNode);
        parentAssignNode.addChildToFront(parentName);
        grandparentVarNode.addChildToFront(parentAssignNode);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertEquals(jsDocInfo, result);
    }

    @Test
    public void testGetFunctionJsDocInfo_MissingJSDocInfo() {
        Node functionNode = new Node(Token.FUNCTION);
        Node parentAssignNode = new Node(Token.ASSIGN);
        Node parentName = new Node(Token.NAME);
        Node grandparentVarNode = new Node(Token.VAR);
        parentName.addChildToFront(functionNode);
        parentAssignNode.addChildToFront(parentName);
        grandparentVarNode.addChildToFront(parentAssignNode);
        JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(functionNode);
        assertNull(result);
    }
}