package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

public class RecordTypeTest {
    @Test
    public void testRecordTypeBasic() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, Maps.newHashMap());
        assertNull(recordType.getImplicitPrototype());
        assertTrue(recordType.isSynthetic());
    }

    @Test
    public void testRecordTypeProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propertiesMap = Maps.newHashMap();
        propertiesMap.put("foo", new RecordProperty(registry.getIntType(), null));
        propertiesMap.put("bar", new RecordProperty(registry.getStringType(), null));

        RecordType recordType = new RecordType(registry, propertiesMap, true);

        // Check properties exist
        assertEquals(2, recordType.properties.size());
        assertTrue(recordType.hasProperty("foo"));
        assertTrue(recordType.hasProperty("bar"));

        // Check property types
        assertEquals(registry.getIntType(), recordType.getPropertyType("foo"));
        assertEquals(registry.getStringType(), recordType.getPropertyType("bar"));

        // Check defining properties
        assertFalse(recordType.defineProperty("baz", registry.getIntType(), false, null));
        assertTrue(recordType.defineProperty("baz", registry.getIntType(), true, null));
        assertTrue(recordType.hasProperty("baz"));
        assertEquals(registry.getIntType(), recordType.getPropertyType("baz"));

        // Check freezing record type
        recordType.isFrozen = true;
        assertFalse(recordType.defineProperty("qux", registry.getIntType(), false, null));
    }

    @Test
    public void testRecordTypeEquivalence() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> propMap1 = Maps.newHashMap();
        propMap1.put("foo", new RecordProperty(registry.getIntType(), null));
        Map<String, RecordProperty> propMap2 = Maps.newHashMap();
        propMap2.put("foo", new RecordProperty(registry.getIntType(), null));
        propMap2.put("bar", new RecordProperty(registry.getStringType(), null));

        RecordType recordType1 = new RecordType(registry, propMap1, true);
        RecordType recordType2 = new RecordType(registry, propMap2, true);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));

        // Change one property type and check for inequality
        RecordType recordType3 = new RecordType(registry, propMap2, true);
        RecordProperty fooProp = recordType1.getPropertyDefinition("foo");
        JSType fooType1 = fooProp.getType();
        RecordProperty barProp = recordType2.getPropertyDefinition("bar");
        JSType barType1 = barProp.getType();
        JSType fooType2 = registry.getIntType();
        JSType barType2 = registry.getStringType();
        recordType3.properties.put("foo", new RecordProperty(fooType2, null));
        recordType3.properties.put("bar", new RecordProperty(barType2, null));

        assertFalse(recordType1.checkRecordEquivalenceHelper(recordType3, false));

        // Test with tolerating unknowns
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType3, true));
    }

    @Test
    public void testRecordTypeSubtyping() {
        JSTypeRegistry registry = new JSTypeRegistry();

        // Record with property "x" of type Int
        RecordType typeA = new RecordType(registry, Maps.newHashMap());
        RecordProperty xProp = new RecordProperty(registry.getIntType(), null);
        typeA.defineProperty("x", xProp.getType(), true, null);

        // Record with properties "x" of type Int and "y" of type String
        RecordType typeB = new RecordType(registry, Maps.newHashMap());
        typeB.defineProperty("x", registry.getIntType(), true, null);
        typeB.defineProperty("y", registry.getStringType(), true, null);

        // Check A is subtype of B
        assertTrue(typeA.isSubtype(typeB));

        // Check B is not subtype of A
        assertFalse(typeB.isSubtype(typeA));

        // Record with property "x" of type String
        RecordType typeC = new RecordType(registry, Maps.newHashMap());
        typeC.defineProperty("x", registry.getStringType(), true, null);

        // Check A is not subtype of C
        assertFalse(typeA.isSubtype(typeC));

        // Check C is not subtype of A
        assertFalse(typeC.isSubtype(typeA));

        // Record with properties "x" of type Int and "y" of type Int
        RecordType typeD = new RecordType(registry, Maps.newHashMap());
        typeD.defineProperty("x", registry.getIntType(), true, null);
        typeD.defineProperty("y", registry.getIntType(), true, null);

        // Check A is subtype of D
        assertTrue(typeA.isSubtype(typeD));

        // Check D is subtype of A
        assertTrue(typeD.isSubtype(typeA));

        // Record with properties "x" of type Int and "y" of type Object
        RecordType typeE = new RecordType(registry, Maps.newHashMap());
        typeE.defineProperty("x", registry.getIntType(), true, null);
        typeE.defineProperty("y", registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), true, null);

        // Check A is not subtype of E
        assertFalse(typeA.isSubtype(typeE));

        // Check E is not subtype of A
        assertFalse(typeE.isSubtype(typeA));
    }
}