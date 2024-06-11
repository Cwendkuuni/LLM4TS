package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ArrowTypeTest {

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        
        // Creating ArrowType instances for testing
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        arrowType1.hasEqualParameters(arrowType1, true); // Test hasEqualParameters
        
        ArrowType arrowType2 = new ArrowType(registry, null, null);
        arrowType2.hasEqualParameters(arrowType2, true); // Test hasEqualParameters
        
        // Test for arrow types with same parameter and return types
        assertTrue(arrowType1.isSubtype(arrowType2));
        assertTrue(arrowType2.isSubtype(arrowType1));
        
        // Test for arrow types with different parameter type
        arrowType1 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.STRING_TYPE));
        arrowType2 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        assertFalse(arrowType1.isSubtype(arrowType2));
        assertFalse(arrowType2.isSubtype(arrowType1));
        
        // Test for arrow types with different return type
        arrowType1 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.STRING_TYPE));
        arrowType2 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.STRING_TYPE));
        assertFalse(arrowType1.isSubtype(arrowType2));
        assertFalse(arrowType2.isSubtype(arrowType1));
    }

    @Test
    public void testHashCode() {
        JSTypeRegistry registry = new JSTypeRegistry();

        // Creating ArrowType instances for testing
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        int hashCode1 = arrowType1.hashCode();

        ArrowType arrowType2 = new ArrowType(registry, null, null);
        int hashCode2 = arrowType2.hashCode();

        // Test for equal hashCodes
        assertEquals(hashCode1, hashCode2);

        arrowType1 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.STRING_TYPE));
        hashCode1 = arrowType1.hashCode();

        arrowType2 = new ArrowType(registry, null, registry.getNativeType(JSTypeNative.STRING_TYPE));
        hashCode2 = arrowType2.hashCode();

        // Test for equal hashCodes
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testGetPossibleToBooleanOutcomes() {
        JSTypeRegistry registry = new JSTypeRegistry();

        // Creating ArrowType instance for testing
        ArrowType arrowType = new ArrowType(registry, null, null);
        BooleanLiteralSet outcomes = arrowType.getPossibleToBooleanOutcomes();

        // Test for TRUE outcome
        assertTrue(outcomes.contains(BooleanLiteral.TRUE));

        // Test for FALSE outcome
        assertFalse(outcomes.contains(BooleanLiteral.FALSE));
    }

    @Test
    public void testToStringHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();

        // Creating ArrowType instance for testing
        ArrowType arrowType = new ArrowType(registry, null, null);
        String output = arrowType.toStringHelper(true);

        // Test for expected output
        assertEquals("[ArrowType]", output);
    }

    @Test
    public void testHasAnyTemplateInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();

        // Creating ArrowType instances for testing
        ArrowType arrowType1 = new ArrowType(registry, null, null);
        arrowType1.hasAnyTemplateInternal(); // Test hasAnyTemplateInternal

        ArrowType arrowType2 = new ArrowType(registry, null, null);
        arrowType2.hasAnyTemplateInternal(); // Test hasAnyTemplateInternal

        // Test for arrow types without any template
        assertFalse(arrowType1.hasAnyTemplateInternal());
        assertFalse(arrowType2.hasAnyTemplateInternal());

        // Test for arrow types with templates in parameter type
        arrowType1 = new ArrowType(registry, null, new TemplateType(registry, "T"));
        assertTrue(arrowType1.hasAnyTemplateInternal());
        assertFalse(arrowType2.hasAnyTemplateInternal());

        // Test for arrow types with templates in return type
        arrowType1 = new ArrowType(registry, null, null);
        arrowType2 = new ArrowType(registry, null, new TemplateType(registry, "T"));
        assertFalse(arrowType1.hasAnyTemplateInternal());
        assertTrue(arrowType2.hasAnyTemplateInternal());
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();

        // Creating ArrowType instance for testing
        ArrowType arrowType = new ArrowType(registry, null, null);
        arrowType.resolveInternal(null, null);
        assertNull(arrowType.returnType);
        assertNull(arrowType.parameters);

        // Creating ArrowType instances for testing
        ArrowType arrowType1 = new ArrowType(registry, null, new TemplateType(registry, "T"));
        arrowType1.resolveInternal(null, null);
        assertNull(arrowType1.returnType);
        assertNull(arrowType1.parameters);

        ArrowType arrowType2 = new ArrowType(registry, new Node(1), new TemplateType(registry, "T"));
        arrowType2.resolveInternal(null, null);
        assertNull(arrowType2.returnType);
        assertNull(arrowType2.parameters);
    }
}