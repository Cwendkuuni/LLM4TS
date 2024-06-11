import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Before;

public class CheckGlobalThisTest {

    private CheckGlobalThis testClass;

    @Before
    public void setUp() {
        testClass = new CheckGlobalThis(null, null);
    }

    @Test
    public void testShouldTraverse_FunctionIsConstructor_ReturnFalse() {
        Node node = new Node(Token.FUNCTION);
        node.putProp(Node.JSDOC_INFO_PROP, "{\"constructor\":true}");
        assertFalse(testClass.shouldTraverse(null, node, null));
    }

    @Test
    public void testShouldTraverse_FunctionHasThisTypeAnnotation_ReturnFalse() {
        Node node = new Node(Token.FUNCTION);
        node.putProp(Node.JSDOC_INFO_PROP, "{\"hasThisType\":true}");
        assertFalse(testClass.shouldTraverse(null, node, null));
    }

    @Test
    public void testShouldTraverse_FunctionIsAssignmentToPrototypeProperty_ReturnFalse() {
        Node parentNode = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "obj"), new Node(Token.STRING, "prototype"));
        parentNode.addChildToFront(lhs);
        Node rhs = new Node(Token.NAME, "value");
        parentNode.addChildToBack(rhs);
        assertFalse(testClass.shouldTraverse(null, rhs, parentNode));
    }

    @Test
    public void testVisit_GlobalThisEncountered_ShouldReport() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        parent.addChildToBack(new Node(Token.NAME, "obj"));
        parent.addChildToBack(new Node(Token.STRING, "prop"));
        testClass.visit(t, n, parent);
        assertTrue("Expected error reported for global this usage", testClass.errorReported());
    }

    @Test
    public void testVisit_GlobalThisOnLeftSideOfAssignment_ShouldReport() {
        NodeTraversal t = new NodeTraversal(null, null);
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        parent.addChildToFront(new Node(Token.THIS));
        parent.addChildToBack(new Node(Token.NAME, "varName"));
        testClass.visit(t, n, parent);
        assertTrue("Expected error reported for global this on left side of assignment", testClass.errorReported());
    }

}