import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis(null, null);
    }

    @After
    public void tearDown() {
        checkGlobalThis = null;
    }

    @Test
    public void testShouldTraverse_FunctionWithConstructorAndThisAnnotation_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        jsDoc.setConstructor(true);
        jsDoc.recordThisType();
        n.setJSDocInfo(jsDoc);
        parent.setFirstChild(n);
        assertFalse(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_FunctionWithoutConstructorAndThisAnnotation_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.NAME);
        JSDocInfo jsDoc = new JSDocInfo();
        n.setJSDocInfo(jsDoc);
        parent.setFirstChild(n);
        assertTrue(checkGlobalThis.shouldTraverse(t, n, parent));
    }

    @Test
    public void testShouldTraverse_LeftSideOfAssignment_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        parent.setFirstChild(lhs);
        parent.addChildToFront(n);
        n.setNext(lhs);
        checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(checkGlobalThis.shouldTraverse(t, lhs, parent));
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignment_PrototypeProperty_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        parent.setFirstChild(lhs);
        parent.addChildToFront(n);
        n.setNext(lhs);
        Node prototype = new Node(Token.NAME);
        prototype.setString("prototype");
        lhs.addChildToBack(prototype);
        assertFalse(checkGlobalThis.shouldTraverse(t, lhs, parent));
    }

    @Test
    public void testShouldTraverse_RightSideOfAssignment_PrototypeSubproperty_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.GETPROP);
        parent.setFirstChild(lhs);
        parent.addChildToFront(n);
        n.setNext(lhs);
        Node prototype = new Node(Token.NAME);
        prototype.setString("prototype");
        lhs.addChildToBack(prototype);
        Node subproperty = new Node(Token.NAME);
        subproperty.setString("subproperty");
        lhs.addChildToBack(subproperty);
        assertFalse(checkGlobalThis.shouldTraverse(t, lhs, parent));
    }

    @Test
    public void testVisit_NodeIsThisAndShouldReport_ReturnsTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        checkGlobalThis.shouldTraverse(t, n, parent);
        assertTrue(checkGlobalThis.shouldReportThis(n, parent));
    }

    @Test
    public void testVisit_NodeIsThisAndShouldNotReport_ReturnsFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.ASSIGN);
        Node lhs = new Node(Token.NAME);
        parent.setFirstChild(lhs);
        parent.addChildToFront(n);
        n.setNext(lhs);
        checkGlobalThis.shouldTraverse(t, n, parent);
        assertFalse(checkGlobalThis.shouldReportThis(n, parent));
    }
}