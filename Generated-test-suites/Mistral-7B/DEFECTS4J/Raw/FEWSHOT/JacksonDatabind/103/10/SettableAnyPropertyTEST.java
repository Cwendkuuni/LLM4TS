package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyPropertyConstruction() {
        BeanProperty property = new BeanProperty(new AnnotatedField(MyPojo.class, "anyProperty"));
        AnnotatedMember setter = new AnnotatedMethod(MyPojo.class, "setAnyProperty", JsonDeserializer.Null.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);
        assertEquals(MyPojo.class, settableAnyProperty.getProperty().getDeclaringClass());
        assertFalse(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty._valueDeserializer);
        assertNull(settableAnyProperty._valueTypeDeserializer);
        assertNull(settableAnyProperty._keyDeserializer);
        assertTrue(settableAnyProperty._setterIsField);
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        BeanProperty property = new BeanProperty(new AnnotatedField(MyPojo.class, "anyProperty"));
        AnnotatedMember setter = new AnnotatedMethod(MyPojo.class, "setAnyProperty", JsonDeserializer.Null.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StubJsonDeserializer();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, valueDeserializer, null);
        assertEquals(MyPojo.class, settableAnyProperty.getProperty().getDeclaringClass());
        assertTrue(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
        assertEquals(valueDeserializer, settableAnyProperty._valueDeserializer);
        assertNull(settableAnyProperty._valueTypeDeserializer);
        assertNull(settableAnyProperty._keyDeserializer);
        assertTrue(settableAnyProperty._setterIsField);
    }

    @Test
    public void testSettableAnyPropertyWithKeyDeserializer() {
        BeanProperty property = new BeanProperty(new AnnotatedField(MyPojo.class, "anyProperty"));
        AnnotatedMember setter = new AnnotatedMethod(MyPojo.class, "setAnyProperty", JsonDeserializer.Null.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class);
        KeyDeserializer keyDeserializer = new StubKeyDeserializer();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, null, null);
        assertEquals(MyPojo.class, settableAnyProperty.getProperty().getDeclaringClass());
        assertFalse(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty._valueDeserializer);
        assertEquals(keyDeserializer, settableAnyProperty._keyDeserializer);
        assertTrue(settableAnyProperty._setterIsField);
    }

    @Test
    public void testSettableAnyPropertyWithValueTypeDeserializer() {
        BeanProperty property = new BeanProperty(new AnnotatedField(MyPojo.class, "anyProperty"));
        AnnotatedMember setter = new AnnotatedMethod(MyPojo.class, "setAnyProperty", JsonDeserializer.Null.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class);
        JavaType valueType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(MyValue.class);
        TypeDeserializer valueTypeDeserializer = new StubTypeDeserializer();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, valueTypeDeserializer);
        assertEquals(MyPojo.class, settableAnyProperty.getProperty().getDeclaringClass());
        assertFalse(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty._valueDeserializer);
        assertEquals(valueTypeDeserializer, settableAnyProperty._valueTypeDeserializer);
        assertNull(settableAnyProperty._keyDeserializer);
        assertTrue(settableAnyProperty._setterIsField);
    }

    @Test
    public void testSettableAnyPropertySetValue() throws IOException {
        BeanProperty property = new BeanProperty(new AnnotatedField(MyPojo.class, "anyProperty"));
        AnnotatedMember setter = new AnnotatedMethod(MyPojo.class, "setAnyProperty", JsonDeserializer.Null.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        MyPojo instance = new MyPojo();
        String testValue = "test value";
        Object propName = "test name";

        settableAnyProperty.deserializeAndSet(JsonParserFactory.getEmptyParser(), null, instance, propName.toString());

        Map<Object, Object> map = (Map<Object, Object>) instance.getClass().getField("anyProperty").get(instance);
        assertEquals(testValue, map.get(propName));
    }

    @Test
    public void testSettableAnyPropertyForwardReference() throws IOException {
        BeanProperty property = new BeanProperty(new AnnotatedField(MyPojo.class, "anyProperty"));
        AnnotatedMember setter = new AnnotatedMethod(MyPojo.class, "setAnyProperty", JsonDeserializer.Null.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StubJsonDeserializer();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, valueDeserializer, null);

        MyPojo instance = new MyPojo();
        String testValue = "test value";
        Object propName = "test name";
        UnresolvedForwardReference reference = new UnresolvedForwardReference();

        settableAnyProperty.deserializeAndSet(JsonParserFactory.getEmptyParser(), null, instance, propName.toString());

        Assertions.assertThat(instance.getClass().getField("anyProperty").get(instance), instanceOf(LinkedHashMap.class));
        Map<Object, Object> map = (Map<Object, Object>) instance.getClass().getField("anyProperty").get(instance);
        assertNull(map.get(propName));

        settableAnyProperty.deserializeAndSet(JsonParserFactory.getEmptyParser(), null, instance, propName.toString(), reference);

        map = (Map<Object, Object>) instance.getClass().getField("anyProperty").get(instance);
        assertEquals(testValue, map.get(propName));
    }

    @Test
    public void testSettableAnyPropertyValueDeserialization() throws IOException {
        BeanProperty property = new BeanProperty(new AnnotatedField(MyPojo.class, "anyProperty"));
        AnnotatedMember setter = new AnnotatedMethod(MyPojo.class, "setAnyProperty", JsonDeserializer.Null.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class);
        JsonDeserializer<Object> valueDeserializer = new StubJsonDeserializer();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, valueDeserializer, null);

        String testValue = "test value";
        Object propName = "test name";

        settableAnyProperty.deserializeAndSet(createParser(testValue), null, new MyPojo(), propName.toString());

        MyPojo instance = new MyPojo();
        Map<Object, Object> map = (Map<Object, Object>) instance.getClass().getField("anyProperty").get(instance);
        assertEquals(testValue, map.get(propName));
    }

    @Test
    public void testSettableAnyPropertyValueTypeDeserialization() throws IOException {
        BeanProperty property = new BeanProperty(new AnnotatedField(MyPojo.class, "anyProperty"));
        AnnotatedMember setter = new AnnotatedMethod(MyPojo.class, "setAnyProperty", JsonDeserializer.Null.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class);
        JavaType valueType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(MyValue.class);
        TypeDeserializer valueTypeDeserializer = new StubTypeDeserializer();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, valueTypeDeserializer);

        String testValue = "test value";
        Object propName = "test name";

        settableAnyProperty.deserializeAndSet(createParser(testValue), null, new MyPojo(), propName.toString());

        MyPojo instance = new MyPojo();
        Map<Object, Object> map = (Map<Object, Object>) instance.getClass().getField("anyProperty").get(instance);
        MyValue expectedValue = new MyValue();
        expectedValue.setValue(testValue);
        assertEquals(expectedValue, map.get(propName));
    }

    @Test
    public void testSettableAnyPropertyKeyDeserialization() throws IOException {
        BeanProperty property = new BeanProperty(new AnnotatedField(MyPojo.class, "anyProperty"));
        AnnotatedMember setter = new AnnotatedMethod(MyPojo.class, "setAnyProperty", JsonDeserializer.Null.class);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructCollectionType(List.class, String.class);
        KeyDeserializer keyDeserializer = new StubKeyDeserializer();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, null, null);

        String testKey = "test key";
        String testValue = "test value";
        Object propName = "test name";

        settableAnyProperty.deserializeAndSet(createParser(testKey, testValue), null, new MyPojo(), propName.toString());

        MyPojo instance = new MyPojo();
        Map<Object, Object> map = (Map<Object, Object>) instance.getClass().getField("anyProperty").get(instance);
        assertEquals(testKey, map.keySet().iterator().next());
        assertEquals(testValue, map.get(testKey));
    }

    private JsonParser createParser(String value) throws IOException {
        ObjectMapper mapper = ObjectMapperFactory.getJsonMapper();
        return mapper.getFactory().createParser(JsonFactory.valueToBytes(value));
    }

    private static class MyPojo {
        private Map<Object, Object> anyProperty = new HashMap<>();

        public void setAnyProperty(Object key, Object value) {
            anyProperty.put(key, value);
        }
    }

    private static class MyValue {
        private String value;

        public MyValue() {
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static class StubJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "test value";
        }
    }

    private static class StubKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return key;
        }
    }

    private static class StubTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyValue();
        }

        @Override
        public JsonTypeDeserializer<Object> getNullValueTypeDeserializer() {
            return null;
        }
    }
}