package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.VoidType;
import com.google.javascript.rhino.jstype.types.ObjectType;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> props = Maps.newHashMap();
        RecordProperty property1 = new RecordProperty("prop1", String.class);
        RecordProperty property2 = new RecordProperty("prop2", Integer.class);
        props.put("prop1", property1);
        props.put("prop2", property2);

        RecordType recordType = new RecordType(registry, props);

        assertEquals("{prop1: string, prop2: number}", recordType.toString());
    }

    @Test
    public void testRecordTypeIsEquivalentToSelf() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> props = Maps.newHashMap();
        RecordProperty property = new RecordProperty("prop", String.class);
        props.put("prop", property);

        RecordType recordType = new RecordType(registry, props);

        assertTrue(recordType.isEquivalentTo(recordType));
    }

    @Test
    public void testRecordTypeIsNotEquivalentToNull() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> props = Maps.newHashMap();
        RecordProperty property = new RecordProperty("prop", String.class);
        props.put("prop", property);

        RecordType recordType = new RecordType(registry, props);

        assertFalse(recordType.isEquivalentTo(null));
    }

    @Test
    public void testRecordTypeIsNotEquivalentToDifferentTypes() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propsA = Maps.newHashMap();
        RecordProperty propertyA = new RecordProperty("prop", String.class);
        propsA.put("prop", propertyA);

        Map<String, RecordProperty> propsB = Maps.newHashMap();
        RecordProperty propertyB = new RecordProperty("prop", Integer.class);
        propsB.put("prop", propertyB);

        RecordType recordTypeA = new RecordType(registry, propsA);
        RecordType recordTypeB = new RecordType(registry, propsB);

        assertFalse(recordTypeA.isEquivalentTo(recordTypeB));
    }

    @Test
    public void testRecordTypeGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> props = Maps.newHashMap();
        RecordProperty property = new RecordProperty("prop", String.class);
        props.put("prop", property);

        RecordType recordType = new RecordType(registry, props);

        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), implicitPrototype);
    }

    @Test
    public void testRecordTypeDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> props = Maps.newHashMap();
        RecordProperty property = new RecordProperty("prop", String.class);
        props.put("prop", property);

        RecordType recordType = new RecordType(registry, props);

        recordType.defineProperty("prop", String.class, false, null);

        assertTrue(recordType.hasProperty("prop"));
        assertEquals(String.class, recordType.getPropertyType("prop"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRecordTypeDefinePropertyWithNullRecordProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> props = Maps.newHashMap();

        RecordType recordType = new RecordType(registry, props);

        recordType.defineProperty("prop", null, false, null);
    }

    @Test
    public void testRecordTypeGetGreatestSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propsA = Maps.newHashMap();
        RecordProperty propertyA1 = new RecordProperty("prop1", String.class);
        RecordProperty propertyA2 = new RecordProperty("prop2", Integer.class);
        propsA.put("prop1", propertyA1);
        propsA.put("prop2", propertyA2);

        RecordType recordTypeA = new RecordType(registry, propsA);

        Map<String, RecordProperty> propsB = Maps.newHashMap();
        RecordProperty propertyB = new RecordProperty("prop", String.class);
        propsB.put("prop", propertyB);

        RecordType recordTypeB = new RecordType(registry, propsB);

        Map<String, RecordProperty> propsC = Maps.newHashMap();
        RecordProperty propertyC = new RecordProperty("prop", String.class);
        propsC.put("prop", propertyC);

        RecordType recordTypeC = new RecordType(registry, propsC);

        UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        unionTypeBuilder.addAlternate(objectType);

        UnionType greatestSubtypeAB = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
        assertEquals(unionTypeBuilder.build(), greatestSubtypeAB);

        UnionType greatestSubtypeAC = recordTypeA.getGreatestSubtypeHelper(recordTypeC);
        assertEquals(recordTypeA, greatestSubtypeAC);

        UnionType greatestSubtypeBC = recordTypeB.getGreatestSubtypeHelper(recordTypeC);
        assertEquals(recordTypeC, greatestSubtypeBC);

        UnionType greatestSubtypeBA = recordTypeB.getGreatestSubtypeHelper(recordTypeA);
        assertEquals(recordTypeA, greatestSubtypeBA);

        UnionType greatestSubtypeBB = recordTypeB.getGreatestSubtypeHelper(recordTypeB);
        assertEquals(recordTypeB, greatestSubtypeBB);

        VoidType greatestSubtypeAEmpty = recordTypeA.getGreatestSubtypeHelper(new VoidType(registry));
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtypeAEmpty);
    }

    @Test
    public void testRecordTypeToMaybeRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> props = Maps.newHashMap();
        RecordProperty property = new RecordProperty("prop", String.class);
        props.put("prop", property);

        RecordType recordType = new RecordType(registry, props);

        assertEquals(recordType, recordType.toMaybeRecordType());
    }

    @Test
    public void testRecordTypeIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propsA = Maps.newHashMap();
        RecordProperty propertyA = new RecordProperty("prop", String.class);
        propsA.put("prop", propertyA);

        Map<String, RecordProperty> propsB = Maps.newHashMap();
        RecordProperty propertyB = new RecordProperty("prop", String.class);
        propsB.put("prop", propertyB);

        RecordType recordTypeA = new RecordType(registry, propsA);
        RecordType recordTypeB = new RecordType(registry, propsB);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
    }

    @Test
    public void testRecordTypeIsNotSubtypeDifferentRecordTypes() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propsA = Maps.newHashMap();
        RecordProperty propertyA = new RecordProperty("prop", String.class);
        propsA.put("prop", propertyA);

        Map<String, RecordProperty> propsB = Maps.newHashMap();
        RecordProperty propertyB = new RecordProperty("prop2", String.class);
        propsB.put("prop2", propertyB);

        RecordType recordTypeA = new RecordType(registry, propsA);
        RecordType recordTypeB = new RecordType(registry, propsB);

        assertFalse(recordTypeA.isSubtype(recordTypeB));
    }

    @Test
    public void testRecordTypeIsNotSubtypeObjectToRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> props = Maps.newHashMap();
        RecordProperty property = new RecordProperty("prop", String.class);
        props.put("prop", property);

        RecordType recordType = new RecordType(registry, props);

        assertFalse(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    }

    @Test
    public void testRecordTypeIsSubtypeRecordToObject() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> props = Maps.newHashMap();
        RecordProperty property = new RecordProperty("prop", String.class);
        props.put("prop", property);

        RecordType recordType = new RecordType(registry, props);

        assertTrue(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
    }

    @Test
    public void testRecordTypeIsSubtypeRecordToRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propsA = Maps.newHashMap();
        RecordProperty propertyA = new RecordProperty("prop", String.class);
        propsA.put("prop", propertyA);

        Map<String, RecordProperty> propsB = Maps.newHashMap();
        RecordProperty propertyB = new RecordProperty("prop", String.class);
        propsB.put("prop", propertyB);

        RecordType recordTypeA = new RecordType(registry, propsA);
        RecordType recordTypeB = new RecordType(registry, propsB);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
    }

    @Test
    public void testRecordTypeIsSubtypeRecordToNarrowerRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propsA = Maps.newHashMap();
        RecordProperty propertyA = new RecordProperty("prop1", String.class);
        RecordProperty propertyB = new RecordProperty("prop2", Integer.class);
        propsA.put("prop1", propertyA);
        propsA.put("prop2", propertyB);

        Map<String, RecordProperty> propsB = Maps.newHashMap();
        RecordProperty propertyC = new RecordProperty("prop2", Integer.class);
        propsB.put("prop2", propertyC);

        RecordType recordTypeA = new RecordType(registry, propsA);
        RecordType recordTypeB = new RecordType(registry, propsB);

        assertFalse(recordTypeA.isSubtype(recordTypeB));
    }

    @Test
    public void testRecordTypeIsSubtypeRecordToWiderRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();

        Map<String, RecordProperty> propsA = Maps.newHashMap();
        RecordProperty propertyA = new RecordProperty("prop1", String.class);
        RecordProperty propertyB = new RecordProperty("prop2", Integer.class);
        propsA.put("prop1", propertyA);
        propsA.put("prop2", propertyB);

        Map<String, RecordProperty> propsB = Maps.newHashMap();
        RecordProperty propertyC = new RecordProperty("prop1", String.class);
        RecordProperty propertyD = new RecordProperty("prop2", Integer.class);
        RecordProperty propertyE = new RecordProperty("prop3", String.class);
        propsB.put("prop1", propertyC);
        propsB.put("prop2", propertyD);
        propsB.put("prop3", propertyE);

        RecordType recordTypeA = new RecordType(registry, propsA);
        RecordType recordTypeB = new RecordType(registry, propsB);

        assertTrue(recordTypeA.isSubtype(recordTypeB));
    }
}