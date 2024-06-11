package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.types.JSTypeNative;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordProperty prop1 = new RecordProperty("prop1", registry.getType(int.class));
        RecordProperty prop2 = new RecordProperty("prop2", registry.getType(int.class));

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop1", prop1);
        properties.put("prop2", prop2);

        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);

        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordProperty prop1 = new RecordProperty("prop1", registry.getType(int.class));
        RecordProperty prop2 = new RecordProperty("prop2", registry.getType(int.class));

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop1", prop1);
        properties.put("prop2", prop2);

        RecordType recordType = new RecordType(registry, properties);

        ObjectType implicitPrototype = recordType.getImplicitPrototype();
        assertTrue(implicitPrototype.isInstanceOfType(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordProperty prop1 = new RecordProperty("prop1", registry.getType(int.class));
        RecordProperty prop2 = new RecordProperty("prop2", registry.getType(int.class));

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop1", prop1);
        properties.put("prop2", prop2);

        RecordType recordType = new RecordType(registry, properties);

        // Test frozen record type
        assertFalse(recordType.defineProperty("prop1", registry.getType(int.class), false, null));
        assertFalse(recordType.defineProperty("prop2", registry.getType(int.class), false, null));

        // Test unfrozen record type
        RecordType unfrozenRecordType = new RecordType(registry, properties) {
            @Override
            boolean isFrozen() {
                return false;
            }
        };

        assertTrue(unfrozenRecordType.defineProperty("prop1", registry.getType(int.class), false, null));
        assertTrue(unfrozenRecordType.defineProperty("prop2", registry.getType(int.class), false, null));
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordProperty prop1 = new RecordProperty("prop1", registry.getType(int.class));
        RecordProperty prop2 = new RecordProperty("prop2", registry.getType(int.class));
        RecordProperty prop3 = new RecordProperty("prop3", registry.getType(int.class));

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("prop1", prop1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("prop1", prop1);
        properties2.put("prop2", prop2);

        Map<String, RecordProperty> properties3 = Maps.newHashMap();
        properties3.put("prop1", prop1);
        properties3.put("prop2", prop2);
        properties3.put("prop3", prop3);

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);
        RecordType recordType3 = new RecordType(registry, properties3);

        JSType leastSupertype = recordType1.getLeastSupertype(recordType2);
        assertTrue(leastSupertype.isInstanceOfType(recordType1));
        assertTrue(leastSupertype.isInstanceOfType(recordType2));

        leastSupertype = recordType2.getLeastSupertype(recordType3);
        assertTrue(leastSupertype.isInstanceOfType(recordType2));
        assertTrue(leastSupertype.isInstanceOfType(recordType3));

        leastSupertype = recordType3.getLeastSupertype(recordType1);
        assertTrue(leastSupertype.isInstanceOfType(recordType3));
        assertTrue(leastSupertype.isInstanceOfType(recordType1));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordProperty prop1 = new RecordProperty("prop1", registry.getType(int.class));
        RecordProperty prop2 = new RecordProperty("prop2", registry.getType(int.class));
        RecordProperty prop3 = new RecordProperty("prop3", registry.getType(int.class));

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("prop1", prop1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("prop1", prop1);
        properties2.put("prop2", prop2);

        Map<String, RecordProperty> properties3 = Maps.newHashMap();
        properties3.put("prop1", prop1);
        properties3.put("prop2", prop2);
        properties3.put("prop3", prop3);

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);
        RecordType recordType3 = new RecordType(registry, properties3);

        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType3);
        assertTrue(greatestSubtype.isInstanceOfType(recordType3));
        assertTrue(greatestSubtype.isInstanceOfType(recordType1));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordProperty prop1 = new RecordProperty("prop1", registry.getType(int.class));
        RecordProperty prop2 = new RecordProperty("prop2", registry.getType(int.class));
        RecordProperty prop3 = new RecordProperty("prop3", registry.getType(int.class));

        Map<String, RecordProperty> properties1 = Maps.newHashMap();
        properties1.put("prop1", prop1);

        Map<String, RecordProperty> properties2 = Maps.newHashMap();
        properties2.put("prop1", prop1);
        properties2.put("prop2", prop2);

        Map<String, RecordProperty> properties3 = Maps.newHashMap();
        properties3.put("prop1", prop1);
        properties3.put("prop2", prop2);
        properties3.put("prop3", prop3);

        RecordType recordType1 = new RecordType(registry, properties1);
        RecordType recordType2 = new RecordType(registry, properties2);
        RecordType recordType3 = new RecordType(registry, properties3);

        assertTrue(recordType1.isSubtype(recordType1));
        assertTrue(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType1));
        assertTrue(recordType1.isSubtype(recordType3));
        assertTrue(recordType3.isSubtype(recordType1));

        RecordType recordType4 = new RecordType(registry, Maps.<String, RecordProperty> newHashMap());
        assertFalse(recordType1.isSubtype(recordType4));
        assertFalse(recordType4.isSubtype(recordType1));

        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(objectType.isSubtype(recordType1));
        assertTrue(recordType1.isSubtype(objectType));
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();

        RecordProperty prop1 = new RecordProperty("prop1", registry.getType(int.class));
        RecordProperty prop2 = new RecordProperty("prop2", registry.getType(int.class));

        Map<String, RecordProperty> properties = Maps.newHashMap();
        properties.put("prop1", prop1);
        properties.put("prop2", prop2);

        RecordType recordType = new RecordType(registry, properties);

        ErrorReporter errorReporter = new ErrorReporter();
        StaticScope<JSType> scope = new StaticScope<>(registry);

        // Test resolving static properties
        scope.put("REGISTRY", registry);
        JSType staticPropertyType = registry.getType(int[].class);
        recordType.resolveInternal(errorReporter, scope);
        JSType resolvedType = recordType.getPropertyType("prop1");
        assertTrue(resolvedType.isInstanceOfType(staticPropertyType));

        // Test resolving dynamic properties
        Node node = registry.createName("window");
        scope.put("WINDOW", registry.resolveGlobalScope(node));
        resolvedType = recordType.getPropertyType("prop2");
        JSType dynamicPropertyType = registry.getType(String.class);
        assertTrue(resolvedType.isInstanceOfType(dynamicPropertyType));

        // Test resolving dynamic properties with union types
        UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
        unionTypeBuilder.addAlternate(registry.getType(int.class));
        unionTypeBuilder.addAlternate(registry.getType(String.class));
        JSType unionType = unionTypeBuilder.build();
        scope.put("PROPERTY", unionType);
        resolvedType = recordType.getPropertyType("prop2");
        assertTrue(resolvedType.isInstanceOfType(unionType));
    }
}