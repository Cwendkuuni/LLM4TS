import org.junit.*;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    private CheckGlobalThis checkGlobalThis;

    @Before
    public void setUp() {
        checkGlobalThis = new CheckGlobalThis();
    }

    @Test
    public void testShouldTraverse_FunctionHasConstructorAnnotation_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        parent.putProp("JSDocInfo", new JSDocInfo().setConstructor(true));
        n.putProp("parent", parent);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionHasThisAnnotation_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        parent.putProp("JSDocInfo", new JSDocInfo().setThisType(new Node()));
        n.putProp("parent", parent);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionHasOverrideAnnotation_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        parent.putProp("JSDocInfo", new JSDocInfo().setOverride(true));
        n.putProp("parent", parent);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionHasInterfaceAnnotation_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        parent.putProp("JSDocInfo", new JSDocInfo().setInterface(true));
        n.putProp("parent", parent);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_NonGlobalContext_ShouldReturnFalse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        parent.putType(Token.BLOCK);
        n.putProp("parent", parent);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertFalse(result);
    }

    @Test
    public void testShouldTraverse_FunctionNotLentToPrototype_ShouldReturnTrue() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        Node grandparent = new Node();
        parent.putType(Token.GETPROP);
        parent.putProp("lastChild", new Node().setString("prototype"));
        grandparent.putProp("jsdocInfo", new JSDocInfo());
        grandparent.putProp("lastChild", new Node().setString("prototype"));
        parent.putProp("parent", grandparent);
        n.putProp("parent", parent);

        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

        assertTrue(result);
    }

    // Add test cases for other methods

}