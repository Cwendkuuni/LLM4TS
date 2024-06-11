import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();

        boolean result = new CheckGlobalThis(nodeTraversal, CheckLevel.ERROR).shouldTraverse(nodeTraversal, node, parent);
        assertEquals(true, result);
    }

    @Test
    public void testShouldTraverseFunctionWithConstructorAnnotation() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        node.setJSDocInfo(jsDoc);

        boolean result = new CheckGlobalThis(nodeTraversal, CheckLevel.ERROR).shouldTraverse(nodeTraversal, node, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverseFunctionWithThisAnnotation() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.FUNCTION);
        Node parent = new Node();
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.recordThisType();
        node.setJSDocInfo(jsDoc);

        boolean result = new CheckGlobalThis(nodeTraversal, CheckLevel.ERROR).shouldTraverse(nodeTraversal, node, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverseNestedAssignment() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        Node assignLhsChild = new Node();
        nodeTraversal.assignLhsChild = assignLhsChild;

        boolean result = new CheckGlobalThis(nodeTraversal, CheckLevel.ERROR).shouldTraverse(nodeTraversal, node, null);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverseRightSideAssignmentToPrototypeProperty() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "prototype"));
        parent.addChildToBack(lhs);

        boolean result = new CheckGlobalThis(nodeTraversal, CheckLevel.ERROR).shouldTraverse(nodeTraversal, node, parent);
        assertEquals(false, result);
    }

    @Test
    public void testShouldTraverseRightSideAssignmentToSubproperty() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node();
        Node parent = new Node();
        Node lhs = new Node(Token.GETPROP);
        lhs.addChildToBack(new Node(Token.NAME, "someObject"));
        lhs.addChildToBack(new Node(Token.NAME, "prototype"));
        parent.addChildToBack(lhs);

        boolean result = new CheckGlobalThis(nodeTraversal, CheckLevel.ERROR).shouldTraverse(nodeTraversal, node, parent);
        assertEquals(false, result);
    }

    @Test
    public void testVisit() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node();
        DiagnosticType diagnosticType = new DiagnosticType();

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(nodeTraversal, CheckLevel.ERROR);
        checkGlobalThis.visit(nodeTraversal, node, parent);

        // Assert that the diagnostic was reported
        assertEquals(1, nodeTraversal.numErrors);
        assertEquals(diagnosticType, nodeTraversal.errorType);
    }

    @Test
    public void testVisitWithAssignLhsChild() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.THIS);
        Node parent = new Node();
        nodeTraversal.assignLhsChild = new Node();

        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(nodeTraversal, CheckLevel.ERROR);
        checkGlobalThis.visit(nodeTraversal, node, parent);

        // Assert that the diagnostic was reported
        assertEquals(1, nodeTraversal.numErrors);
        assertEquals(diagnosticType, nodeTraversal.errorType);
    }
}