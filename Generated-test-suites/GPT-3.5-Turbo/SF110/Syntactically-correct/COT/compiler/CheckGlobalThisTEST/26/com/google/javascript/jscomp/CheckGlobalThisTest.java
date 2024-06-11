
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        // Initialize CheckGlobalThis object for testing
        checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
    }

    @Test
    public void testShouldTraverse_FunctionNotConstructor_NoThisAnnotation_ShouldReturnTrue() {
        // Test shouldTraverse method with a function that is not a constructor and has no @this annotation
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == true);
    }

    @Test
    public void testShouldTraverse_FunctionIsConstructor_NoThisAnnotation_ShouldReturnFalse() {
        // Test shouldTraverse method with a function that is a constructor and has no @this annotation
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = JSDocInfo.builder().setConstructor(true).build();
        node.setJSDocInfo(jsDocInfo);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == false);
    }

    @Test
    public void testShouldTraverse_FunctionNotConstructor_ThisAnnotation_ShouldReturnFalse() {
        // Test shouldTraverse method with a function that is not a constructor and has @this annotation
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = JSDocInfo.builder().addType(new JSTypeExpression(new Node(Token.STRING))).build();
        node.setJSDocInfo(jsDocInfo);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == false);
    }

    @Test
    public void testShouldTraverse_FunctionNotConstructor_UndefinedThisAnnotation_ShouldReturnFalse() {
        // Test shouldTraverse method with a function that is not a constructor and has @this annotation with undefined type
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = JSDocInfo.builder().addType(new JSTypeExpression(new Node(Token.VOID))).build();
        node.setJSDocInfo(jsDocInfo);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == false);
    }

    @Test
    public void testShouldTraverse_FunctionNotConstructor_InterfaceAnnotation_ShouldReturnFalse() {
        // Test shouldTraverse method with a function that is not a constructor and has an @interface annotation
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = JSDocInfo.builder().setInterface(true).build();
        node.setJSDocInfo(jsDocInfo);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == false);
    }

    @Test
    public void testShouldTraverse_FunctionNotConstructor_OverrideAnnotation_ShouldReturnFalse() {
        // Test shouldTraverse method with a function that is not a constructor and has an @override annotation
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        JSDocInfo jsDocInfo = JSDocInfo.builder().setOverride(true).build();
        node.setJSDocInfo(jsDocInfo);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == false);
    }

    @Test
    public void testShouldTraverse_FunctionInsideBlock_ShouldReturnFalse() {
        // Test shouldTraverse method with a function inside a block
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.BLOCK);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == false);
    }

    @Test
    public void testShouldTraverse_FunctionInsideScript_ShouldReturnTrue() {
        // Test shouldTraverse method with a function inside a script
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == true);
    }

    @Test
    public void testShouldTraverse_FunctionInsideName_ShouldReturnTrue() {
        // Test shouldTraverse method with a function inside a name
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == true);
    }

    @Test
    public void testShouldTraverse_FunctionInsideAssign_ShouldReturnTrue() {
        // Test shouldTraverse method with a function inside an assign
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == true);
    }

    @Test
    public void testShouldTraverse_FunctionInsideStringKey_ShouldReturnTrue() {
        // Test shouldTraverse method with a function inside a string key
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.STRING_KEY);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == true);
    }

    @Test
    public void testShouldTraverse_LendsToPrototypeProperty_ShouldReturnFalse() {
        // Test shouldTraverse method with a function that is lent to a prototype property
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        Node grandparent = new Node(Token.GETPROP, new Node(Token.NAME, "x"), new Node(Token.STRING, "prototype"));
        grandparent.setJSDocInfo(JSDocInfo.builder().setLendsName("x.prototype").build());
        parent.addChildToFront(grandparent);
        boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
        assert(result == false);
    }

    @Test
    public void testVisit_ThisInAssignment_ShouldReportGlobalThis() {
        // Test visit method with a THIS node that is on the left side of an assignment
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.THIS);
        checkGlobalThis.assignLhsChild = node;  // Simulate a THIS on the left side of an assignment
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.visit(nodeTraversal, node, parent);
        // Assert that a diagnostic with the global this warning is reported
        assertTrue(nodeTraversal.hasDiagnosedWarning("dangerous use of the global 'this' object"));
    }

    @Test
    public void testVisit_ThisAsPropertyAccess_ShouldReportGlobalThis() {
        // Test visit method with a THIS node that is a property access
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(node);  // Simulate a THIS as part of a property access
        checkGlobalThis.visit(nodeTraversal, node, parent);
        // Assert that a diagnostic with the global this warning is reported
        assertTrue(nodeTraversal.hasDiagnosedWarning("dangerous use of the global 'this' object"));
    }

    @Test
    public void testVisit_ThisNotOnAssignOrPropertyAccess_ShouldNotReportGlobalThis() {
        // Test visit method with a THIS node that is not on the left side of an assignment or a property access
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node(Token.SCRIPT);
        checkGlobalThis.visit(nodeTraversal, node, parent);
        // Assert that no diagnostic is reported
        assertFalse(nodeTraversal.hasDiagnosedWarning("dangerous use of the global 'this' object"));
    }

    @Test
    public void testVisit_ThisNotOnAssignOrPropertyAccess_AssignLhsChildNotNull_ShouldNotReportGlobalThis() {
        // Test visit method with a THIS node that is not on the left side of an assignment or a property access,
        // but assignLhsChild is not null
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.THIS);
        checkGlobalThis.assignLhsChild = new Node(Token.NAME);  // Simulate assignLhsChild not being null
        Node parent = new Node(Token.SCRIPT);
        checkGlobalThis.visit(nodeTraversal, node, parent);
        // Assert that no diagnostic is reported
        assertFalse(nodeTraversal.hasDiagnosedWarning("dangerous use of the global 'this' object"));
    }
}
