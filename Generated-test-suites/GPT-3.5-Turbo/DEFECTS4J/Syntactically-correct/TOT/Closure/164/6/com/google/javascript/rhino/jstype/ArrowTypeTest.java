import org.junit.Test;
import static org.junit.Assert.*;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        JSType other = registry.getNativeType(JSTypeNative.STRING_TYPE);
        assertTrue("isSubtype() should return true when other is the same as returnType", arrowType.isSubtype(other));
    }

    @Test
    public void testHasEqualParameters() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = null;
        JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

        Node parameters2 = null;
        JSType returnType2 = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

        assertTrue("hasEqualParameters() should return true when parameter lists are null in both ArrowTypes", arrowType1.hasEqualParameters(arrowType2));
    }

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = null;
        JSType returnType1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

        Node parameters2 = null;
        JSType returnType2 = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

        assertTrue("isEquivalentTo() should return true when returnType and parameter lists are equal", arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        int expectedHashCode = 31 + returnType.hashCode();
        assertEquals("hashCode() should return the hash code of the returnType", expectedHashCode, arrowType.hashCode());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        JSType other = registry.getNativeType(JSTypeNative.STRING_TYPE);
        arrowType.getLeastSupertype(other);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        JSType other = registry.getNativeType(JSTypeNative.STRING_TYPE);
        arrowType.getGreatestSubtype(other);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTestForEquality() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        JSType other = registry.getNativeType(JSTypeNative.STRING_TYPE);
        arrowType.testForEquality(other);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testVisit() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        arrowType.visit(null);
    }

    @Test
    public void testHasUnknownParamsOrReturn() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        assertFalse("hasUnknownParamsOrReturn() should return false when parameters and returnType are not unknown types", arrowType.hasUnknownParamsOrReturn());
    }

    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters = null;
        JSType returnType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        ArrowType arrowType = new ArrowType(registry, parameters, returnType);

        String expectedString = super.toString();
        assertEquals("toStringHelper() should return the result of calling super.toString()", expectedString, arrowType.toStringHelper(true));
    }

}