import static org.junit.Assert.*;
import org.junit.Test;

public class ArrowTypeTest {

    // Test case for ArrowType constructor with non-null parameters and returnType
    @Test
    public void testArrowTypeConstructorNonNull() {
        ArrowType arrowType = new ArrowType(new JSTypeRegistry(), new Node(Node.VAR, new Node(Token.NAME, "param1")), JSType.BOOLEAN);
        assertNotNull(arrowType);
    }

    // Test case for ArrowType constructor with null parameters
    @Test
    public void testArrowTypeConstructorNullParameters() {
        ArrowType arrowType = new ArrowType(new JSTypeRegistry(), null, JSType.BOOLEAN);
        assertNotNull(arrowType);
    }

    // Test case for ArrowType constructor with null returnType
    @Test
    public void testArrowTypeConstructorNullReturnType() {
        ArrowType arrowType = new ArrowType(new JSTypeRegistry(), new Node(Node.VAR, new Node(Token.NAME, "param1")), null);
        assertNotNull(arrowType);
    }

    // Test case for isSubtype method with two equal ArrowType instances
    @Test
    public void testIsSubtypeEqual() {
        ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(Node.VAR, new Node(Token.NAME, "param1")), JSType.BOOLEAN);
        ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), new Node(Node.VAR, new Node(Token.NAME, "param1")), JSType.BOOLEAN);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    // Test case for isSubtype method with non-equal ArrowType instances
    @Test
    public void testIsSubtypeNotEqual() {
        ArrowType arrowType1 = new ArrowType(new JSTypeRegistry(), new Node(Node.VAR, new Node(Token.NAME, "param1")), JSType.BOOLEAN);
        ArrowType arrowType2 = new ArrowType(new JSTypeRegistry(), new Node(Node.VAR, new Node(Token.NAME, "param1")), JSType.NUMBER);
        assertFalse(arrowType1.isSubtype(arrowType2));
    }

    // Add more test cases based on edge cases and exception scenarios...
}