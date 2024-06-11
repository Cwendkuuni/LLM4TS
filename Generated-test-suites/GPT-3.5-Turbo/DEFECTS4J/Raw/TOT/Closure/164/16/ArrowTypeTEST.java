import static org.junit.Assert.*;

import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        // Positive test case
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType1 = new ArrowType(registry, null, registry.getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType2 = new ArrowType(registry, null, registry.getNativeType(UNKNOWN_TYPE));
        assertTrue(arrowType1.isSubtype(arrowType2));

        // Negative test case
        ArrowType arrowType3 = new ArrowType(registry, null, registry.getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType4 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
        assertFalse(arrowType3.isSubtype(arrowType4));
    }

    @Test
    public void testHasEqualParameters() {
        // Positive test case
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.LP, new Node(Token.EMPTY));
        Node parameters2 = new Node(Token.LP, new Node(Token.EMPTY));
        ArrowType arrowType1 = new ArrowType(registry, parameters1, registry.getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType2 = new ArrowType(registry, parameters2, registry.getNativeType(UNKNOWN_TYPE));
        assertTrue(arrowType1.hasEqualParameters(arrowType2));

        // Negative test case
        Node parameters3 = new Node(Token.LP, new Node(Token.EMPTY));
        Node parameters4 = new Node(Token.LP, new Node(Token.EMPTY).putJSType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE)));
        ArrowType arrowType3 = new ArrowType(registry, parameters3, registry.getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType4 = new ArrowType(registry, parameters4, registry.getNativeType(UNKNOWN_TYPE));
        assertFalse(arrowType3.hasEqualParameters(arrowType4));
    }

    @Test
    public void testIsEquivalentTo() {
        // Positive test case
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.LP, new Node(Token.EMPTY));
        ArrowType arrowType1 = new ArrowType(registry, parameters1, registry.getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType2 = new ArrowType(registry, parameters1, registry.getNativeType(UNKNOWN_TYPE));
        assertTrue(arrowType1.isEquivalentTo(arrowType2));

        // Negative test case
        Node parameters3 = new Node(Token.LP, new Node(Token.EMPTY));
        Node parameters4 = new Node(Token.LP, new Node(Token.EMPTY).putJSType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE)));
        ArrowType arrowType3 = new ArrowType(registry, parameters3, registry.getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType4 = new ArrowType(registry, parameters4, registry.getNativeType(UNKNOWN_TYPE));
        assertFalse(arrowType3.isEquivalentTo(arrowType4));
    }

    @Test
    public void testHashCode() {
        // Positive test case
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.LP, new Node(Token.EMPTY).putJSType(registry.getNativeType(JSTypeNative.STRING_TYPE)));
        ArrowType arrowType1 = new ArrowType(registry, parameters1, registry.getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType2 = new ArrowType(registry, parameters1, registry.getNativeType(UNKNOWN_TYPE));
        assertEquals(arrowType1.hashCode(), arrowType2.hashCode());

        // Negative test case
        Node parameters3 = new Node(Token.LP, new Node(Token.EMPTY));
        Node parameters4 = new Node(Token.LP, new Node(Token.EMPTY).putJSType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE)));
        ArrowType arrowType3 = new ArrowType(registry, parameters3, registry.getNativeType(UNKNOWN_TYPE));
        ArrowType arrowType4 = new ArrowType(registry, parameters4, registry.getNativeType(UNKNOWN_TYPE));
        assertNotEquals(arrowType3.hashCode(), arrowType4.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, registry.getNativeType(UNKNOWN_TYPE));
        arrowType.getLeastSupertype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, registry.getNativeType(UNKNOWN_TYPE));
        arrowType.getGreatestSubtype(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, registry.getNativeType(UNKNOWN_TYPE));
        arrowType.testForEquality(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, registry.getNativeType(UNKNOWN_TYPE));
        arrowType.visit(null);
    }

    @Test
    public void testResolveInternal() {
        // Positive test case
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, null);
        assertTrue(arrowType.resolveInternal(null, null).isResolved());

        // Negative test case
        JSTypeRegistry registry2 = new JSTypeRegistry();
        ArrowType arrowType2 = new ArrowType(registry2, null, null);
        assertFalse(arrowType2.resolveInternal(null, null).isResolved());
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        // Positive test case
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = new Node(Token.LP, new Node(Token.EMPTY).putJSType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE)));
        ArrowType arrowType1 = new ArrowType(registry, parameters1, registry.getNativeType(UNKNOWN_TYPE));
        assertTrue(arrowType1.hasUnknownParamsOrReturn());

        // Negative test case
        Node parameters2 = new Node(Token.LP, new Node(Token.EMPTY).putJSType(registry.getNativeType(JSTypeNative.NUMBER_TYPE)));
        ArrowType arrowType2 = new ArrowType(registry, parameters2, registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
        assertFalse(arrowType2.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        // Positive test case
        JSTypeRegistry registry = new JSTypeRegistry();
        ArrowType arrowType = new ArrowType(registry, null, registry.getNativeType(UNKNOWN_TYPE));
        assertNotNull(arrowType.toStringHelper(false));

        // Negative test case
        JSTypeRegistry registry2 = new JSTypeRegistry();
        ArrowType arrowType2 = new ArrowType(registry2, null, registry.getNativeType(UNKNOWN_TYPE));
        assertNull(arrowType2.toStringHelper(true));
    }
}