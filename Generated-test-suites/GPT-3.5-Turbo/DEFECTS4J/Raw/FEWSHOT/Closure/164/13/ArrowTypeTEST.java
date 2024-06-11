import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrowTypeTest {
    @Test
    public void testIsSubtype_WithMatchingReturnType_ShouldReturnTrue() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType returnType1 = registry.createFindType(JSTypeNative.UNKNOWN_TYPE);
        JSType returnType2 = registry.createFindType(JSTypeNative.UNKNOWN_TYPE);

        ArrowType arrowType1 = new ArrowType(registry, null, returnType1);
        ArrowType arrowType2 = new ArrowType(registry, null, returnType2);

        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsSubtype_WithDifferentReturnType_ShouldReturnFalse() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType returnType1 = registry.createFindType(JSTypeNative.UNKNOWN_TYPE);
        JSType returnType2 = registry.createFindType(JSTypeNative.VOID_TYPE);

        ArrowType arrowType1 = new ArrowType(registry, null, returnType1);
        ArrowType arrowType2 = new ArrowType(registry, null, returnType2);

        assertFalse(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsSubtype_WithMatchingParameters_ShouldReturnTrue() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = registry.createParametersWithVarArgs(registry.createFindType(JSTypeNative.UNKNOWN_TYPE));
        Node parameters2 = registry.createParametersWithVarArgs(registry.createFindType(JSTypeNative.UNKNOWN_TYPE));

        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);

        assertTrue(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsSubtype_WithDifferentParameters_ShouldReturnFalse() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Node parameters1 = registry.createParametersWithVarArgs(registry.createFindType(JSTypeNative.UNKNOWN_TYPE));
        Node parameters2 = registry.createParametersWithVarArgs(registry.createFindType(JSTypeNative.BOOLEAN_TYPE));

        ArrowType arrowType1 = new ArrowType(registry, parameters1, null);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, null);

        assertFalse(arrowType1.isSubtype(arrowType2));
    }

    @Test
    public void testIsEquivalentTo_WithEqualArrowType_ShouldReturnTrue() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType returnType1 = registry.createFindType(JSTypeNative.UNKNOWN_TYPE);
        JSType returnType2 = registry.createFindType(JSTypeNative.UNKNOWN_TYPE);
        Node parameters1 = registry.createParametersWithVarArgs(registry.createFindType(JSTypeNative.UNKNOWN_TYPE));
        Node parameters2 = registry.createParametersWithVarArgs(registry.createFindType(JSTypeNative.UNKNOWN_TYPE));

        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

        assertTrue(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testIsEquivalentTo_WithDifferentReturnType_ShouldReturnFalse() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType returnType1 = registry.createFindType(JSTypeNative.UNKNOWN_TYPE);
        JSType returnType2 = registry.createFindType(JSTypeNative.VOID_TYPE);
        Node parameters1 = registry.createParametersWithVarArgs(registry.createFindType(JSTypeNative.UNKNOWN_TYPE));
        Node parameters2 = registry.createParametersWithVarArgs(registry.createFindType(JSTypeNative.UNKNOWN_TYPE));

        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

        assertFalse(arrowType1.isEquivalentTo(arrowType2));
    }

    @Test
    public void testIsEquivalentTo_WithDifferentParameters_ShouldReturnFalse() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType returnType1 = registry.createFindType(JSTypeNative.UNKNOWN_TYPE);
        JSType returnType2 = registry.createFindType(JSTypeNative.UNKNOWN_TYPE);
        Node parameters1 = registry.createParametersWithVarArgs(registry.createFindType(JSTypeNative.UNKNOWN_TYPE));
        Node parameters2 = registry.createParametersWithScope(registry.createFindType(JSTypeNative.UNKNOWN_TYPE));

        ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);
        ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

        assertFalse(arrowType1.isEquivalentTo(arrowType2));
    }
}