package com.fasterxml.jackson.databind.type.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.TypeResolverBuilder;

public class MapLikeTypeTest {

    @Test
    public void testMapLikeTypeCreation() {
        JavaType mapType = Map.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), mapLikeType._bindings);
        assertEquals(Map.class, mapLikeType._class);
        assertEquals(keyType, mapLikeType._keyType);
        assertEquals(valueType, mapLikeType._valueType);
        assertEquals(false, mapLikeType._asStatic);

        TypeVariable<?>[] typeParameters = Map.class.getTypeParameters();
        TypeBindings bindings = TypeBindings.create(Map.class, keyType, valueType);
        MapLikeType mapLikeType2 = new MapLikeType(mapType, bindings, null, null, keyType, valueType, null, null, false);
        assertEquals(mapType, mapLikeType2.getRawType());
        assertEquals(bindings, mapLikeType2._bindings);
        assertEquals(mapType, mapLikeType2._class);
        assertEquals(keyType, mapLikeType2._keyType);
        assertEquals(valueType, mapLikeType2._valueType);
        assertEquals(false, mapLikeType2._asStatic);
    }

    @Test
    public void testMapLikeTypeUpgrade() {
        JavaType baseType = ArrayList.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(baseType, keyType, valueType);
        JavaType mapType = new MapLikeType.MapLikeFactory().constructMapType(Map.class, keyType, valueType);
        MapLikeType upgradedType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(mapType, upgradedType.getRawType());
        assertEquals(keyType, upgradedType._keyType);
        assertEquals(valueType, upgradedType._valueType);
    }

    @Test
    public void testMapLikeTypeStaticTyping() {
        JavaType mapType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType mapLikeTypeStatic = mapLikeType.withStaticTyping();

        assertTrue(mapLikeTypeStatic._asStatic);
        assertFalse(mapLikeType._asStatic);
    }

    @Test
    public void testMapLikeTypeWithKeyType() {
        JavaType mapType = HashMap.class;
        JavaType keyType1 = String.class;
        JavaType keyType2 = Integer.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType1, valueType, null, null, false);
        MapLikeType mapLikeType2 = mapLikeType.withKeyType(keyType2);

        assertEquals(mapType, mapLikeType2.getRawType());
        assertEquals(keyType2, mapLikeType2._keyType);
        assertEquals(valueType, mapLikeType2._valueType);
    }

    @Test
    public void testMapLikeTypeWithContentType() {
        JavaType mapType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType1 = Integer.class;
        JavaType valueType2 = Float.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType1, null, null, false);
        MapLikeType mapLikeType2 = mapLikeType.withContentType(valueType2);

        assertEquals(mapType, mapLikeType2.getRawType());
        assertEquals(keyType, mapLikeType2._keyType);
        assertEquals(valueType2, mapLikeType2._valueType);
    }

    @Test
    public void testMapLikeTypeWithTypeHandler() {
        JavaType mapType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        TypeDeserializer valueHandler = new TypeDeserializer() {};

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType mapLikeType2 = mapLikeType.withTypeHandler(valueHandler);

        assertEquals(mapType, mapLikeType2.getRawType());
        assertEquals(keyType, mapLikeType2._keyType);
        assertEquals(valueType, mapLikeType2._valueType);
        assertEquals(valueHandler, mapLikeType2._typeHandler);
        assertNull(mapLikeType2._valueHandler);
    }

    @Test
    public void testMapLikeTypeWithContentTypeHandler() {
        JavaType mapType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        TypeDeserializer valueHandler = new TypeDeserializer() {};

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType mapLikeType2 = mapLikeType.withContentTypeHandler(valueHandler);

        assertEquals(mapType, mapLikeType2.getRawType());
        assertEquals(keyType, mapLikeType2._keyType);
        assertEquals(valueType, mapLikeType2._valueType);
        assertNull(mapLikeType2._typeHandler);
        assertEquals(valueHandler, mapLikeType2._valueHandler);
    }

    @Test
    public void testMapLikeTypeWithValueHandler() {
        JavaType mapType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        TypeDeserializer valueHandler = new TypeDeserializer() {};

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType mapLikeType2 = mapLikeType.withValueHandler(valueHandler);

        assertEquals(mapType, mapLikeType2.getRawType());
        assertEquals(keyType, mapLikeType2._keyType);
        assertEquals(valueType, mapLikeType2._valueType);
        assertEquals(valueHandler, mapLikeType2._valueHandler);
        assertNull(mapLikeType2._typeHandler);
    }

    @Test
    public void testMapLikeTypeWithContentValueHandler() {
        JavaType mapType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        TypeDeserializer valueHandler = new TypeDeserializer() {};

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType mapLikeType2 = mapLikeType.withContentValueHandler(valueHandler);

        assertEquals(mapType, mapLikeType2.getRawType());
        assertEquals(keyType, mapLikeType2._keyType);
        assertEquals(valueType, mapLikeType2._valueType);
        assertNull(mapLikeType2._typeHandler);
        assertNull(mapLikeType2._valueHandler);
        assertEquals(valueHandler, mapLikeType2._contentValueHandler);
    }

    @Test
    public void testMapLikeTypeIsContainerType() {
        JavaType mapType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testMapLikeTypeIsTrueMapType() {
        JavaType mapType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testMapLikeTypeRefine() {
        JavaType mapType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        TypeBindings bindings = TypeBindings.create(mapType, keyType, valueType);
        TypeDeserializer valueHandler = new TypeDeserializer() {};
        MapLikeType mapLikeType = new MapLikeType(mapType, bindings, null, null, keyType, valueType, null, valueHandler, false);

        JavaType refinedType = mapLikeType.refine(HashMap.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(mapType, refinedType.getRawType());
        assertEquals(keyType, refinedType._keyType);
        assertEquals(valueType, refinedType._valueType);
        assertNull(refinedType._typeHandler);
        assertNull(refinedType._valueHandler);
        assertNull(refinedType._valueHandler);
    }

    @Test
    public void testMapLikeTypeToString() {
        JavaType mapType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        String toString = mapLikeType.toString();
        assertTrue(toString.startsWith("[map-like type; class "));
        assertTrue(toString.endsWith(";]"));
    }

    @Test
    public void testMapLikeTypeEquals() {
        JavaType mapType = HashMap.class;
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapLikeType mapLikeType1 = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(mapType, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);
        MapLikeType mapLikeType3 = new MapLikeType(ArrayList.class, TypeBindings.emptyBindings(), null, null, keyType, valueType, null, null, false);

        assertTrue(mapLikeType1.equals(mapLikeType2));
        assertFalse(mapLikeType1.equals(mapLikeType3));
        assertFalse(mapLikeType2.equals(mapLikeType3));
        assertFalse(mapLikeType1.equals(null));
    }

}