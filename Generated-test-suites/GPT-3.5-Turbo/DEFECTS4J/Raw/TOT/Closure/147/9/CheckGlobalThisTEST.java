import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(null, null);
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForConstructorFunction() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        node.setJSDocInfo(new JSDocInfo().setConstructor(true));
        Node parent = new Node(Token.METHOD);
        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForThisAnnotationFunction() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        node.setJSDocInfo(new JSDocInfo().setThisType(new Node(Token.STRING, "MyObject")));
        Node parent = new Node(Token.METHOD);
        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForInterfaceFunction() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        node.setJSDocInfo(new JSDocInfo().setInterface(true));
        Node parent = new Node(Token.METHOD);
        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForOverrideFunction() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        node.setJSDocInfo(new JSDocInfo().setOverride(true));
        Node parent = new Node(Token.METHOD);
        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForNonGlobalFunction() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void shouldTraverse_shouldReturnTrueForGlobalFunction() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void shouldTraverse_shouldReturnTrueForNestedAssignment() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(node);
        parent.addChildToBack(new Node(Token.THIS));
        assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForAssignmentPrototypeProperty() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(node);
        node.addChildToBack(new Node(Token.NAME, "prototype"));
        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void shouldTraverse_shouldReturnFalseForAssignmentPrototypeSubproperty() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToBack(new Node(Token.GETPROP).addChildToBack(new Node(Token.NAME, "prototype")));
        parent.addChildToBack(node);
        node.addChildToBack(new Node(Token.NAME, "property"));
        assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void shouldTraverse_shouldReturnTrueForPropertyAccess() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.GETPROP);
        Node parent = new Node(Token.NAME);
        parent.addChildToBack(node);
        assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void visit_shouldReportGlobalThis() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.NAME);
        checkGlobalThis.visit(nodeTraversal, node, parent);
        assertTrue("Expected an error message for global 'this' usage", 
            nodeTraversal.getErrorCount() > 0);
    }

    @Test
    public void visit_shouldNotReportGlobalThis() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(node);
        checkGlobalThis.visit(nodeTraversal, node, parent);
        assertEquals("Expected no error message for global 'this' usage",
            0, nodeTraversal.getErrorCount());
    }

    @Test
    public void getFunctionJsDocInfo_shouldReturnFunctionJsDocInfo() {
        Node node = new Node(Token.FUNCTION);
        JSDocInfo expectedJSDocInfo = new JSDocInfo();
        expectedJSDocInfo.setOverride(true);
        node.setJSDocInfo(expectedJSDocInfo);
        JSDocInfo actualJSDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
        assertEquals("Expected the same JSDocInfo object", expectedJSDocInfo, actualJSDocInfo);
    }

    @Test
    public void getFunctionJsDocInfo_shouldReturnParentFunctionJsDocInfo() {
        Node node = new Node(Token.NAME);
        node.setJSDocInfo(new JSDocInfo().setConstructor(true));
        Node parent = new Node(Token.VAR);
        parent.addChildToBack(node);
        JSDocInfo expectedJSDocInfo = new JSDocInfo();
        expectedJSDocInfo.setOverride(true);
        parent.setJSDocInfo(expectedJSDocInfo);
        JSDocInfo actualJSDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
        assertEquals("Expected the JSDocInfo object from the parent node", expectedJSDocInfo, actualJSDocInfo);
    }

    @Test
    public void getFunctionJsDocInfo_shouldReturnGrandparentFunctionJsDocInfo() {
        Node node = new Node(Token.NAME);
        node.setJSDocInfo(new JSDocInfo().setConstructor(true));
        Node parent = new Node(Token.METHOD);
        parent.addChildToBack(node);
        Node grandparent = new Node(Token.VAR);
        grandparent.addChildToBack(parent);
        JSDocInfo expectedJSDocInfo = new JSDocInfo();
        expectedJSDocInfo.setOverride(true);
        grandparent.setJSDocInfo(expectedJSDocInfo);
        JSDocInfo actualJSDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);
        assertEquals("Expected the JSDocInfo object from the grandparent node", expectedJSDocInfo, actualJSDocInfo);
    }
}