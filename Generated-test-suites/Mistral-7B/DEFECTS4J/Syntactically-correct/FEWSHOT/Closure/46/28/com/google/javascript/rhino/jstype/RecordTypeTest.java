package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.PrototypeObjectType;
import com.google.javascript.rhino.jstype.RecordProperty;
import org.junit.Test;

import java.util.Map;
import java.util.SortedMap;

public class RecordTypeTest {

    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
    }

    @Test
    public void testCreateRecordType() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));
        properties.put("prop2", new RecordProperty("prop2", registry.getTypeByName("string")));

        RecordType recordType = new RecordType(registry, properties);

        assertEquals("RecordType{properties=[prop1=int, prop2=string]}", recordType.toString());
    }

    @Test
    public void testIsEquivalentTo() {
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));
        properties1.put("prop2", new RecordProperty("prop2", registry.getTypeByName("string")));

        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));
        properties2.put("prop2", new RecordProperty("prop2", registry.getTypeByName("string")));
        properties2.put("prop3", new RecordProperty("prop3", registry.getTypeByName("string")));

        RecordType recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isEquivalentTo(recordType1));
        assertTrue(recordType1.isEquivalentTo(recordType2));
        assertFalse(recordType1.isEquivalentTo(registry.getTypeByName("int")));
        assertFalse(recordType1.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    }

    @Test
    public void testGetImplicitPrototype() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));
        RecordType recordType = new RecordType(registry, properties);

        assertNotNull(recordType.getImplicitPrototype());
        assertEquals("ObjectType{name=Object, isSealed=false, isEnum=false, isFunction=false, isConstructor=false, isPrototype=false, isObjectType=true, isErrorType=false}", recordType.getImplicitPrototype().toString());
    }

    @Test
    public void testDefineProperty() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));
        RecordType recordType = new RecordType(registry, properties);

        assertFalse(recordType.defineProperty("prop1", registry.getTypeByName("boolean"), false, null));
        assertFalse(recordType.defineProperty("prop2", registry.getTypeByName("int"), false, null));

        recordType.defineProperty("prop1", registry.getTypeByName("boolean"), true, null);
        assertTrue(recordType.hasProperty("prop1"));
        assertEquals("boolean", recordType.getPropertyType("prop1").getName());
    }

    @Test
    public void testGetLeastSupertype() {
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));
        properties1.put("prop2", new RecordProperty("prop2", registry.getTypeByName("string")));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));
        properties2.put("prop3", new RecordProperty("prop3", registry.getTypeByName("bool")));
        RecordType recordType2 = new RecordType(registry, properties2);

        RecordType leastSupertype = recordType1.getLeastSupertype(recordType2);
        assertEquals("RecordType{properties=[prop1=int, prop2=string]}", leastSupertype.toString());

        Map<String, RecordProperty> properties3 = Maps.newHashMap();
        properties3.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));
        RecordType recordType3 = new RecordType(registry, properties3);

        RecordType greatestSubtype = recordType2.getGreatestSubtype(recordType1);
        assertEquals("RecordType{properties=[prop1=int, prop2=string]}", greatestSubtype.toString());

        RecordType greatestSubtype2 = recordType3.getGreatestSubtype(recordType2);
        assertEquals("RecordType{properties=[]}", greatestSubtype2.toString());
    }

    @Test
    public void testToString() {
        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));
        properties.put("prop2", new RecordProperty("prop2", registry.getTypeByName("string")));
        RecordType recordType = new RecordType(registry, properties);

        assertEquals("RecordType{properties=[prop1=int, prop2=string]}", recordType.toString());
    }

    @Test
    public void testIsSubtype() {
        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));
        properties1.put("prop2", new RecordProperty("prop2", registry.getTypeByName("string")));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));
        properties2.put("prop3", new RecordProperty("prop3", registry.getTypeByName("bool")));
        RecordType recordType2 = new RecordType(registry, properties2);

        Map<String, RecordProperty> properties3 = Maps.newHashMap();
        properties3.put("prop1", new RecordProperty("prop1", registry.getTypeByName("int")));

        assertTrue(recordType1.isSubtype(recordType1));
        assertTrue(recordType1.isSubtype(recordType2));
        assertFalse(recordType1.isSubtype(registry.getTypeByName("int")));
        assertFalse(recordType1.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        assertTrue(recordType2.isSubtype(recordType1));
        assertTrue(recordType3.isSubtype(recordType1));
        assertTrue(recordType1.isSubtype(recordType3));
    }
}