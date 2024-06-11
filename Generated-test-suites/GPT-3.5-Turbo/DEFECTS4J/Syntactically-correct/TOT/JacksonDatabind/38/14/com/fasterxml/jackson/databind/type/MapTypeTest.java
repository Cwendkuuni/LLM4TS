package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        // Typical use case: construct MapType with rawType, keyType, and valueType
        Class<?> rawType = HashMap.class;
        JavaType keyType = JacksonTypeFactory.constructType(String.class);
        JavaType valueType = JacksonTypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, keyType, valueType);

        Assert.assertEquals(rawType, mapType.getRawType());
        Assert.assertEquals(keyType, mapType.getKeyType());
        Assert.assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        // Typical use case: change the keyType of the existing MapType
        Class<?> rawType = HashMap.class;
        JavaType initialKeyType = JacksonTypeFactory.constructType(String.class);
        JavaType valueType = JacksonTypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, initialKeyType, valueType);

        JavaType newKeyType = JacksonTypeFactory.constructType(Integer.class);
        MapType updatedMapType = mapType.withKeyType(newKeyType);

        Assert.assertEquals(rawType, updatedMapType.getRawType());
        Assert.assertEquals(newKeyType, updatedMapType.getKeyType());
        Assert.assertEquals(valueType, updatedMapType.getValueType());
    }

    @Test
    public void testWithContentType() {
        // Typical use case: change the valueType of the existing MapType
        Class<?> rawType = HashMap.class;
        JavaType keyType = JacksonTypeFactory.constructType(String.class);
        JavaType initialValueType = JacksonTypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, keyType, initialValueType);

        JavaType newValueType = JacksonTypeFactory.constructType(Double.class);
        MapType updatedMapType = mapType.withContentType(newValueType);

        Assert.assertEquals(rawType, updatedMapType.getRawType());
        Assert.assertEquals(keyType, updatedMapType.getKeyType());
        Assert.assertEquals(newValueType, updatedMapType.getValueType());
    }
  
    @Test
    public void testWithStaticTyping() {
        // Typical use case: enable static typing for the existing MapType
        Class<?> rawType = HashMap.class;
        JavaType keyType = JacksonTypeFactory.constructType(String.class);
        JavaType valueType = JacksonTypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, keyType, valueType);
        MapType staticMapType = mapType.withStaticTyping();

        Assert.assertEquals(rawType, staticMapType.getRawType());
        Assert.assertTrue(staticMapType.isStaticTyping());
    }

    @Test
    public void testWithKeyTypeHandler() {
        // Typical use case: change the keyTypeHandler of the existing MapType
        Class<?> rawType = HashMap.class;
        JavaType keyType = JacksonTypeFactory.constructType(String.class);
        JavaType valueType = JacksonTypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, keyType, valueType);

        Object keyTypeHandler = new MyKeyTypeHandler();
        MapType updatedMapType = mapType.withKeyTypeHandler(keyTypeHandler);

        Assert.assertEquals(rawType, updatedMapType.getRawType());
        Assert.assertEquals(keyType.withTypeHandler(keyTypeHandler), updatedMapType.getKeyType());
        Assert.assertEquals(valueType, updatedMapType.getValueType());
    }

    @Test
    public void testWithContentValueHandler() {
        // Typical use case: change the valueHandler of the existing MapType
        Class<?> rawType = HashMap.class;
        JavaType keyType = JacksonTypeFactory.constructType(String.class);
        JavaType valueType = JacksonTypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, keyType, valueType);

        Object valueHandler = new MyValueHandler();
        MapType updatedMapType = mapType.withContentValueHandler(valueHandler);

        Assert.assertEquals(rawType, updatedMapType.getRawType());
        Assert.assertEquals(keyType, updatedMapType.getKeyType());
        Assert.assertEquals(valueType.withValueHandler(valueHandler), updatedMapType.getValueType());
    }

    @Test
    public void testRefine() {
        // Typical use case: refine the existing MapType with the given rawType, keyType, and valueType
        Class<?> rawType = HashMap.class;
        JavaType initialKeyType = JacksonTypeFactory.constructType(String.class);
        JavaType initialValueType = JacksonTypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(HashMap.class, initialKeyType, initialValueType);

        JavaType refinedKeyType = JacksonTypeFactory.constructType(String.class);
        JavaType refinedValueType = JacksonTypeFactory.constructType(Double.class);
        JavaType refinedMapType = mapType.refine(rawType, null, null, null, refinedKeyType, refinedValueType);

        Assert.assertEquals(rawType, refinedMapType.getRawType());
        Assert.assertEquals(refinedKeyType, refinedMapType.getKeyType());
        Assert.assertEquals(refinedValueType, refinedMapType.getValueType());
    }

    @Test
    public void testToString() {
        // Typical use case: convert MapType to a string representation
        Class<?> rawType = HashMap.class;
        JavaType keyType = JacksonTypeFactory.constructType(String.class);
        JavaType valueType = JacksonTypeFactory.constructType(Integer.class);

        MapType mapType = MapType.construct(rawType, keyType, valueType);

        String expectedString = "[map type; class " + rawType.getName() + ", " + keyType + " -> " + valueType + "]";
        Assert.assertEquals(expectedString, mapType.toString());
    }

    // Edge case and error scenarios can be added here

}