package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.UnionType;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        RecordType record1 = createRecordType("{a: int, b: int}");
        RecordType record2 = createRecordType("{a: int, b: int}");
        RecordType record3 = createRecordType("{a: int, c: int}");
        RecordType record4 = createRecordType("{b: int, a: int}");
        RecordType record5 = createRecordType("{c: int, a: int}");

        assertTrue(record1.isEquivalentTo(record2));
        assertFalse(record1.isEquivalentTo(record3));
        assertFalse(record1.isEquivalentTo(record4));
        assertFalse(record1.isEquivalentTo(record5));
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordType record = createRecordType("{a: int}");
        ObjectType implicitPrototype = record.getImplicitPrototype();
        assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype.getName());
    }

    @Test
    public void testDefineProperty() {
        RecordType record = createRecordType("{}");
        boolean result = record.defineProperty("a", createIntType(), true, null);
        assertTrue(result);
    }

    @Test
    public void testDefineProperty_Frozen() {
        RecordType record = createRecordType("{}");
        boolean result = record.defineProperty("a", createIntType(), true, null);
        assertTrue(result);

        record.isFrozen = true;
        result = record.defineProperty("b", createIntType(), true, null);
        assertFalse(result);
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType record1 = createRecordType("{a: int, b: int}");
        RecordType record2 = createRecordType("{a: int, b: string, c: int}");
        RecordType record3 = createRecordType("{c: int, a: int}");

        UnionType result = record1.getGreatestSubtypeHelper(record2);
        assertEquals(UnionType.class, result.getClass());

        assertTrue(result.isTypeOf(record1));
        assertTrue(result.isTypeOf(record2));
        assertTrue(result.isTypeOf(record3));

        result = record1.getGreatestSubtypeHelper(record3);
        assertTrue(result.isTypeOf(record1));
        assertTrue(result.isTypeOf(record3));

        result = record1.getGreatestSubtypeHelper(createIntType());
        assertFalse(result.isTypeOf(record1));
        assertFalse(result.isTypeOf(createIntType()));
    }

    @Test
    public void testIsSubtype() {
        RecordType record1 = createRecordType("{a: int, b: int}");
        RecordType record2 = createRecordType("{a: int, b: string}");
        RecordType record3 = createRecordType("{a: int, c: int}");
        RecordType record4 = createRecordType("{b: int, a: int}");
        RecordType record5 = createIntType();

        assertTrue(record1.isSubtype(record1));
        assertTrue(record1.isSubtype(record3));
        assertTrue(record1.isSubtype(record4));
        assertFalse(record1.isSubtype(record2));
        assertFalse(record1.isSubtype(record5));
    }

    @Test
    public void testResolveInternal() {
        RecordType record = createRecordType("{a: {b: int}}");
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>();

        record.resolveInternal(errorReporter, scope);

        Map<String, JSType> properties = record.properties;
        assertEquals("int", properties.get("a").getName());
        JSType propertyB = properties.get("a").getPropertyType("b");
        assertEquals("int", propertyB.getName());
    }

    private RecordType createRecordType(String recordDefinition) {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> recordProperties = Maps.newHashMap();
        RecordProperty property;
        String[] propertyDefinitionParts = recordDefinition.substring(1, recordDefinition.length() - 1)
                .split(", ");
        for (String propertyDefinition : propertyDefinitionParts) {
            String[] propertyParts = propertyDefinition.split(": ");
            property = new RecordProperty(propertyParts[1].trim(), createIntType());
            recordProperties.put(propertyParts[0].trim(), property);
        }
        return new RecordType(registry, recordProperties);
    }

    private JSType createIntType() {
        return registry.getIntType();
    }

    private JSTypeRegistry registry = new JSTypeRegistry();
}