import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrowTypeTest {

    private ArrowType arrowType;

    @Before
    public void setUp() {
        arrowType = new ArrowType(new JSTypeRegistry(), null,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));
    }

    @Test
    public void testIsSubType_WithSameReturnType_ReturnsTrue() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertTrue(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubType_WithDifferentReturnType_ReturnsFalse() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null,
                new JSTypeRegistry().getNativeType(JSTypeNative.STRING_TYPE));

        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubType_WithSameParameters_ReturnsTrue() {
        Node parameters = new Node();

        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertTrue(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubType_WithDifferentParameters_ReturnsFalse() {
        Node parameters = new Node();

        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubType_WithMissingRequiredParameters_ReturnsFalse() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(Token.NAME));

        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertFalse(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubType_WithExtraOptionalParameters_ReturnsTrue() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(Token.NAME));
        parameters.getFirstChild().setIsOptionalArg(true);

        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertTrue(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubType_WithMissingOptionalParameters_ReturnsTrue() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(Token.NAME));
        parameters.getFirstChild().setIsOptionalArg(true);

        ArrowType other = new ArrowType(new JSTypeRegistry(), null,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertTrue(arrowType.isSubtype(other));
    }

    @Test
    public void testIsSubType_WithVarArgs_ReturnsTrue() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(Token.NAME));
        parameters.getFirstChild().setIsVarArgs(true);

        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertTrue(arrowType.isSubtype(other));
    }

    @Test
    public void testHasEqualParameters_WithEqualParameters_ReturnsTrue() {
        Node parameters = new Node();
        parameters.addChildToBack(new Node(Token.NAME));

        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertTrue(arrowType.hasEqualParameters(other));
    }

    @Test
    public void testHasEqualParameters_WithDifferentParameters_ReturnsFalse() {
        Node parametersA = new Node();
        parametersA.addChildToBack(new Node(Token.NAME));

        Node parametersB = new Node();
        parametersB.addChildToBack(new Node(Token.STRING));

        ArrowType other = new ArrowType(new JSTypeRegistry(), parametersB,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertFalse(arrowType.hasEqualParameters(other));
    }

    @Test
    public void testHashCode_WithSameReturnType_ReturnsSameHashCode() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertEquals(arrowType.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCode_WithDifferentReturnType_ReturnsDifferentHashCode() {
        ArrowType other = new ArrowType(new JSTypeRegistry(), null,
                new JSTypeRegistry().getNativeType(JSTypeNative.STRING_TYPE));

        assertNotEquals(arrowType.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCode_WithSameParameters_ReturnsSameHashCode() {
        Node parameters = new Node();

        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertEquals(arrowType.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCode_WithDifferentParameters_ReturnsDifferentHashCode() {
        Node parameters = new Node();

        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters,
                new JSTypeRegistry().getNativeType(JSTypeNative.NUMBER_TYPE));

        assertNotEquals(arrowType.hashCode(), other.hashCode());
    }

    @Test
    public void testHashCode_WithEqualParametersAndDifferentReturnType_ReturnsDifferentHashCode() {
        Node parameters = new Node();

        ArrowType other = new ArrowType(new JSTypeRegistry(), parameters,
                new JSTypeRegistry().getNativeType(JSTypeNative.STRING_TYPE));

        assertNotEquals(arrowType.hashCode(), other.hashCode());
    }

}