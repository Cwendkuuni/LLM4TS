import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrowTypeTest {

    private ArrowType arrowType;

    @Before
    public void setUp() {
        arrowType = new ArrowType(new JSTypeRegistry(), null, null);
    }

    // Tests for isSubtype method
    @Test
    public void testIsSubtype_sameArrowType_returnsTrue() {
        ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), null, null);
        assertTrue(arrowType.isSubtype(otherArrowType));
    }

    @Test
    public void testIsSubtype_differentReturnType_returnsFalse() {
        JSType returnType = new JSType(new JSTypeRegistry(), JSTypeNative.STRING_TYPE);
        ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), null, returnType);
        assertFalse(arrowType.isSubtype(otherArrowType));
    }

    @Test
    public void testIsSubtype_differentParameters_returnsFalse() {
        Node parameters = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(
                Token.STRING, "param1")), new Node(Token.NAME, new Node(
                Token.STRING, "param2")));
        ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), parameters, null);
        assertFalse(arrowType.isSubtype(otherArrowType));
    }

    @Test
    public void testIsSubtype_missingRequiredParameter_returnsFalse() {
        Node parameters = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(
                Token.STRING, "param1")));
        ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), parameters, null);
        assertFalse(arrowType.isSubtype(otherArrowType));
    }

    // Tests for hasEqualParameters method
    @Test
    public void testHasEqualParameters_sameParameters_returnsTrue() {
        Node parameters = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(
                Token.STRING, "param1")), new Node(Token.NAME, new Node(
                Token.STRING, "param2")));
        ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), parameters, null);
        assertTrue(arrowType.hasEqualParameters(otherArrowType));
    }

    @Test
    public void testHasEqualParameters_differentParameters_returnsFalse() {
        Node parameters = new Node(Token.FUNCTION, new Node(Token.NAME, new Node(
                Token.STRING, "param1")));
        ArrowType otherArrowType = new ArrowType(new JSTypeRegistry(), parameters, null);
        assertFalse(arrowType.hasEqualParameters(otherArrowType));
    }

    // More test cases for other methods can be added here

}