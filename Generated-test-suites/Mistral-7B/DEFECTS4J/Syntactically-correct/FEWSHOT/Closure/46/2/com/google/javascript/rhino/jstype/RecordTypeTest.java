package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();
        RecordProperty prop1 = new RecordProperty("prop1", Integer.TYPE);
        RecordProperty prop2 = new RecordProperty("prop2", String.class);
        properties.put("prop1", prop1);
        properties.put("prop2", prop2);

        RecordType recordType = new RecordType(registry, properties);
        assertNotNull(recordType);
    }

    @Test
    public void testIsEquivalentTo() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop2", new RecordProperty("prop2", Double.TYPE));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsEquivalentToSameInstance() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();

        properties.put("prop1", new RecordProperty("prop1", Integer.TYPE));

        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = recordType1;

        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsEquivalentToSameProperties() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsEquivalentToWithSamePropertiesAndValues() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordProperty prop1 = properties1.get("prop1");
        RecordProperty prop1Copy = new RecordProperty("prop1", prop1.getType());
        properties2.put("prop1", prop1Copy);

        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsSubtype() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties1.put("prop2", new RecordProperty("prop2", String.class));

        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop2", new RecordProperty("prop2", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtypeWithIncompatiblePropertyType() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties1.put("prop2", new RecordProperty("prop2", String.class));

        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop2", new RecordProperty("prop2", Double.TYPE));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertFalse(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtypeWithMissingProperty() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties1.put("prop2", new RecordProperty("prop2", String.class));

        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertFalse(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtypeWithMissingPropertyButWithSubtypeProperty() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties1.put("prop2", new RecordProperty("prop2", String.class));

        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop3", new RecordProperty("prop3", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtypeWithMissingPropertyButWithSupertypeProperty() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties1.put("prop2", new RecordProperty("prop2", String.class));

        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop3", new RecordProperty("prop3", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testGetImplicitPrototype() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();

        RecordType recordType = new RecordType(registry, properties);

        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE),
            recordType.getImplicitPrototype());
    }

    @Test
    public void testDefineProperty() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();

        RecordType recordType = new RecordType(registry, properties);

        String propertyName = "propertyName";
        JSType propertyType = String.class;
        Node propertyNode = Node.NUL;

        assertTrue(recordType.defineProperty(propertyName, propertyType,
            false, propertyNode));

        assertTrue(recordType.hasProperty(propertyName));
        assertEquals(propertyType, recordType.getPropertyType(propertyName));
    }

    @Test(expected = IllegalStateException.class)
    public void testDefinePropertyWithNullPropertyType() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();

        RecordType recordType = new RecordType(registry, properties);

        String propertyName = "propertyName";
        Node propertyNode = Node.NUL;

        assertTrue(recordType.defineProperty(propertyName, null,
            false, propertyNode));

        fail();
    }

    @Test
    public void testGetLeastSupertype() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties1.put("prop2", new RecordProperty("prop2", String.class));

        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop3", new RecordProperty("prop3", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);

        assertEquals(recordType2, leastSupertype);
    }

    @Test
    public void testGetGreatestSubtype() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties1.put("prop2", new RecordProperty("prop2", String.class));

        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop3", new RecordProperty("prop3", String.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

        assertNull(greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeWithSameProperties() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

        assertEquals(recordType1, greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeWithCompatiblePropertyTypes() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties1.put("prop2", new RecordProperty("prop2", String.class));

        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop2", new RecordProperty("prop2", Number.class));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

        assertEquals(recordType2, greatestSubtype);
    }

    @Test
    public void testGetGreatestSubtypeWithIncompatiblePropertyTypes() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        Map<String, RecordProperty> properties2 = Maps.newHashMap();

        properties1.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties1.put("prop2", new RecordProperty("prop2", String.class));

        properties2.put("prop1", new RecordProperty("prop1", Integer.TYPE));
        properties2.put("prop2", new RecordProperty("prop2", Boolean.TYPE));

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

        assertNull(greatestSubtype);
    }

    @Test
    public void testResolveInternal() {
        RecordTypeRegistry registry = new RecordTypeRegistry();
        Map<String, RecordProperty> properties = Maps.newHashMap();

        properties.put("prop1", new RecordProperty("prop1", Integer.TYPE));

        RecordType recordType = new RecordType(registry, properties);
        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>(registry, errorReporter);

        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);

        assertNotNull(resolvedType);
    }
}