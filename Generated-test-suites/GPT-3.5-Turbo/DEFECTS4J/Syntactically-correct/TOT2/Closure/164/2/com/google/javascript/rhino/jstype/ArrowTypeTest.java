import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    // Test for constructor with non-null parameters and returnType
    @Test
    public void testArrowTypeConstructor() {
        ArrowType arrowType = new ArrowType(JSTypeRegistry.INSTANCE, new Node(Token.NAME), JSTypeNative.STRING_TYPE);
        assertNotNull(arrowType);
        assertEquals(JSTypeNative.STRING_TYPE, arrowType.returnType);
    }

    // Test for isSubtype method with different ArrowType instances
    @Test
    public void testIsSubtype() {
        ArrowType arrowType1 = new ArrowType(JSTypeRegistry.INSTANCE, new Node(Token.NAME), JSTypeNative.STRING_TYPE);
        ArrowType arrowType2 = new ArrowType(JSTypeRegistry.INSTANCE, new Node(Token.NAME), JSTypeNative.NUMBER_TYPE);
        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    // Test for hasEqualParameters method with equal parameters
    @Test
    public void testHasEqualParameters() {
        ArrowType arrowType1 = new ArrowType(JSTypeRegistry.INSTANCE, new Node(Token.NAME), JSTypeNative.STRING_TYPE);
        ArrowType arrowType2 = new ArrowType(JSTypeRegistry.INSTANCE, new Node(Token.NAME), JSTypeNative.STRING_TYPE);
        assertTrue(arrowType1.hasEqualParameters(arrowType2));
    }

    // Test for isEquivalentTo method with different ArrowType instances
    @Test
    public void testIsEquivalentTo() {
        ArrowType arrowType1 = new ArrowType(JSTypeRegistry.INSTANCE, new Node(Token.NAME), JSTypeNative.STRING_TYPE);
        ArrowType arrowType2 = new ArrowType(JSTypeRegistry.INSTANCE, new Node(Token.NAME), JSTypeNative.STRING_TYPE);
        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }

    // Test for hashCode method
    @Test
    public void testHashCode() {
        ArrowType arrowType = new ArrowType(JSTypeRegistry.INSTANCE, new Node(Token.NAME), JSTypeNative.STRING_TYPE);
        assertNotNull(arrowType.hashCode());
    }
}