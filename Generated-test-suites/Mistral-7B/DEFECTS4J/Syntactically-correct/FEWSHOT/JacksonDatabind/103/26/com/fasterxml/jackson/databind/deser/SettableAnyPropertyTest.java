package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyProperty() {
        BeanProperty property = new BeanProperty(null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMethod(null, null, null);
        JavaType type = new SimpleType(Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<>();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals(property, settableAnyProperty.getProperty());
        assertTrue(settableAnyProperty.hasValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        BeanProperty property = new BeanProperty(null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMethod(null, null, null);
        JavaType type = new SimpleType(Object.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<>();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        SettableAnyProperty settableAnyPropertyWithValueDeserializer = settableAnyProperty.withValueDeserializer(new StdDeserializer<>());

        assertNotSame(valueDeserializer, settableAnyPropertyWithValueDeserializer.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        String json = "{\"propName\":{\"value\":\"value\"}}";
        JsonParser jsonParser = jsonFactory.createParser(json);

        BeanProperty property = new BeanProperty(null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setValue", new Class[]{Object.class, Object.class});
        JavaType type = new SimpleType(Map.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<>();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new HashMap<>();
        String propName = "propName";

        settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance, propName);

        Map<Object, Object> map = (Map<Object, Object>) instance;
        assertEquals("value", map.get(propName));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeAndSetWithNullValue() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        String json = "{\"propName\":null}";
        JsonParser jsonParser = jsonFactory.createParser(json);

        BeanProperty property = new BeanProperty(null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setValue", new Class[]{Object.class, Object.class});
        JavaType type = new SimpleType(Map.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<>();
        TypeDeserializer valueTypeDeserializer = null;

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new HashMap<>();
        String propName = "propName";

        settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance, propName);

        Map<Object, Object> map = (Map<Object, Object>) instance;
        assertNull(map.get(propName));
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        String json = "{\"propName\":1}";
        JsonParser jsonParser = jsonFactory.createParser(json);

        BeanProperty property = new BeanProperty(null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setValue", new Class[]{Object.class, Object.class});
        JavaType type = new SimpleType(Integer.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<>();
        TypeDeserializer valueTypeDeserializer = new SimpleTypeDeserializer(type);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new HashMap<>();
        String propName = "propName";

        settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance, propName);

        Map<Object, Object> map = (Map<Object, Object>) instance;
        assertEquals(1, map.get(propName));
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithTypeMismatch() throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        String json = "{\"propName\":\"value\"}";
        JsonParser jsonParser = jsonFactory.createParser(json);

        BeanProperty property = new BeanProperty(null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setValue", new Class[]{Object.class, Object.class});
        JavaType type = new SimpleType(Integer.class);
        KeyDeserializer keyDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = new StandardDeserializer<>();
        TypeDeserializer valueTypeDeserializer = new SimpleTypeDeserializer(type);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        Object instance = new HashMap<>();
        String propName = "propName";

        settableAnyProperty.deserializeAndSet(jsonParser, objectMapper.getDeserializationContext(), instance, propName);

        Map<Object, Object> map = (Map<Object, Object>) instance;
        assertNull(map.get(propName));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new HashMap<>();
        String propName = "propName";
        Object value = "value";

        BeanProperty property = new BeanProperty(null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setValue", new Class[]{Object.class, Object.class});
        JavaType type = new SimpleType(Map.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        settableAnyProperty.set(instance, propName, value);

        Map<Object, Object> map = (Map<Object, Object>) instance;
        assertEquals(value, map.get(propName));
    }

    @Test(expected = IOException.class)
    public void testSetWithNullInstance() throws IOException {
        String propName = "propName";
        Object value = "value";

        BeanProperty property = new BeanProperty(null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setValue", new Class[]{Object.class, Object.class});
        JavaType type = new SimpleType(Map.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        settableAnyProperty.set(null, propName, value);

        fail("Should throw an IOException");
    }

    @Test(expected = IOException.class)
    public void testSetWithNullPropName() throws IOException {
        Object instance = new HashMap<>();
        Object value = "value";

        BeanProperty property = new BeanProperty(null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setValue", new Class[]{Object.class, Object.class});
        JavaType type = new SimpleType(Map.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        settableAnyProperty.set(instance, null, value);

        fail("Should throw an IOException");
    }

    @Test(expected = IOException.class)
    public void testSetWithNullValue() throws IOException {
        Object instance = new HashMap<>();
        String propName = "propName";

        BeanProperty property = new BeanProperty(null, null, null, null, null, null);
        AnnotatedMember setter = new AnnotatedMethod(null, "setValue", new Class[]{Object.class, Object.class});
        JavaType type = new SimpleType(Map.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        settableAnyProperty.set(instance, propName, null);

        fail("Should throw an IOException");
    }

    @Test
    public void testThrowAsIOE() throws IOException {
        Exception e = new IOException("Test IOException");
        Object propName = "propName";
        Object value = "value";

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(null, null, null, null, null, null);

        settableAnyProperty._throwAsIOE(e, propName, value);

        fail("Should not reach this point");
    }
}