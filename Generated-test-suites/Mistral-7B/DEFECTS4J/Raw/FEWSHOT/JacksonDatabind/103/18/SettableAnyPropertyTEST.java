package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.util.Map;

public class SettableAnyPropertyTest extends TestBase {

    @Test
    public void testSettableAnyProperty() throws IOException {
        BeanProperty property = createTestBeanProperty("testProperty");
        AnnotatedMember setter = createTestAnnotatedMember(property.getName(), AnnotatedField.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty.getSetter());
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertNull(settableAnyProperty.getValueTypeDeserializer());
        assertNull(settableAnyProperty.getKeyDeserializer());
        assertFalse(settableAnyProperty.isSetterIsField());

        SettableAnyProperty withValueDeserializer = settableAnyProperty.withValueDeserializer(createTestJsonDeserializer());
        assertEquals(settableAnyProperty, withValueDeserializer.getProperty());
        assertEquals(setter, withValueDeserializer.getSetter());
        assertEquals(type, withValueDeserializer.getType());
        assertNotNull(withValueDeserializer.getValueDeserializer());
        assertNull(withValueDeserializer.getValueTypeDeserializer());
        assertNull(withValueDeserializer.getKeyDeserializer());
        assertFalse(withValueDeserializer.isSetterIsField());
    }

    @Test
    public void testSettableAnyPropertyWithKeyDeserializer() throws IOException {
        BeanProperty property = createTestBeanProperty("testProperty");
        AnnotatedMember setter = createTestAnnotatedMember(property.getName(), AnnotatedField.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, createTestKeyDeserializer(), null, null);

        assertEquals(property, settableAnyProperty.getProperty());
        assertEquals(setter, settableAnyProperty.getSetter());
        assertEquals(type, settableAnyProperty.getType());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertNotNull(settableAnyProperty.getValueTypeDeserializer());
        assertNotNull(settableAnyProperty.getKeyDeserializer());
        assertFalse(settableAnyProperty.isSetterIsField());
    }

    @Test
    public void testSettableAnyPropertyFixAccess() throws IOException {
        BeanProperty property = createTestBeanProperty("testProperty");
        AnnotatedMember setter = createTestAnnotatedMember(property.getName(), AnnotatedField.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        DeserializationConfig config = createContext().builder().build();
        settableAnyProperty.fixAccess(config);

        AnnotatedMember setterWithAccess = setter;
        if (setter instanceof AnnotatedField) {
            setterWithAccess = ((AnnotatedField) setter).withVisibility(Modifier.PUBLIC);
        }

        assertEquals(setterWithAccess, settableAnyProperty.getSetter());
    }

    @Test
    public void testSettableAnyPropertyDeserializeAndSet() throws IOException {
        BeanProperty property = createTestBeanProperty("testProperty");
        AnnotatedMember setter = createTestAnnotatedMember(property.getName(), AnnotatedField.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        Object testInstance = createTestInstance();
        String propName = "testProperty";
        Object value = "testValue";

        JsonParser parser = createParser("{\"testProperty\":\"testValue\"}");
        settableAnyProperty.deserializeAndSet(parser, createContext(), testInstance, propName);

        if (settableAnyProperty.isSetterIsField()) {
            Map<Object, Object> map = ((AnnotatedField) settableAnyProperty.getSetter()).getValue(testInstance);
            assertEquals("testValue", map.get(propName));
        } else {
            ((AnnotatedMethod) settableAnyProperty.getSetter()).callOnWith(testInstance, propName, value);
            assertEquals(value, testInstance.getClass().getDeclaredField(property.getName()).get(testInstance));
        }
    }

    @Test
    public void testSettableAnyPropertyDeserializeWithType() throws IOException {
        BeanProperty property = createTestBeanProperty("testProperty");
        AnnotatedMember setter = createTestAnnotatedMember(property.getName(), AnnotatedField.class);
        JavaType type = createSimpleJavaType(String.class);
        JavaType valueType = createSimpleJavaType(Integer.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, createTypeDeserializer(valueType));

        Object testInstance = createTestInstance();
        String propName = "testProperty";
        Object value = 123;

        JsonParser parser = createParser("{\"testProperty\":123}");
        settableAnyProperty.deserializeAndSet(parser, createContext(), testInstance, propName);

        assertEquals(value, testInstance.getClass().getDeclaredField(property.getName()).get(testInstance));
    }

    @Test
    public void testSettableAnyPropertyDeserializeValueDeserializer() throws IOException {
        BeanProperty property = createTestBeanProperty("testProperty");
        AnnotatedMember setter = createTestAnnotatedMember(property.getName(), AnnotatedField.class);
        JavaType type = createSimpleJavaType(String.class);
        JsonDeserializer<Object> valueDeserializer = createTestJsonDeserializer();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, valueDeserializer, null);

        Object testInstance = createTestInstance();
        String propName = "testProperty";
        Object value = "testValue";

        JsonParser parser = createParser("{\"testProperty\":\"testValue\"}");
        settableAnyProperty.deserializeAndSet(parser, createContext(), testInstance, propName);

        assertEquals(value, testInstance.getClass().getDeclaredField(property.getName()).get(testInstance));
    }

    @Test
    public void testSettableAnyPropertyDeserializeWithTypeDeserializer() throws IOException {
        BeanProperty property = createTestBeanProperty("testProperty");
        AnnotatedMember setter = createTestAnnotatedMember(property.getName(), AnnotatedField.class);
        JavaType type = createSimpleJavaType(String.class);
        JavaType valueType = createSimpleJavaType(Integer.class);
        TypeDeserializer valueTypeDeserializer = createTypeDeserializer(valueType);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, valueTypeDeserializer);

        Object testInstance = createTestInstance();
        String propName = "testProperty";
        Object value = 123;

        JsonParser parser = createParser("{\"testProperty\":123}");
        settableAnyProperty.deserializeAndSet(parser, createContext(), testInstance, propName);

        assertEquals(value, testInstance.getClass().getDeclaredField(property.getName()).get(testInstance));
    }

    @Test(expected = JsonMappingException.class)
    public void testSettableAnyPropertyDeserializeWithNullValueDeserializer() throws IOException {
        BeanProperty property = createTestBeanProperty("testProperty");
        AnnotatedMember setter = createTestAnnotatedMember(property.getName(), AnnotatedField.class);
        JavaType type = createSimpleJavaType(String.class);
        JsonDeserializer<Object> valueDeserializer = createNullJsonDeserializer();
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, valueDeserializer, null);

        Object testInstance = createTestInstance();
        String propName = "testProperty";
        Object value = null;

        JsonParser parser = createParser("{\"testProperty\":null}");
        settableAnyProperty.deserializeAndSet(parser, createContext(), testInstance, propName);

        fail("Expected JsonMappingException but it didn't occur.");
    }

    @Test
    public void testSettableAnyPropertyWithUnresolvedForwardReference() throws IOException {
        BeanProperty property = createTestBeanProperty("testProperty");
        AnnotatedMember setter = createTestAnnotatedMember(property.getName(), AnnotatedField.class);
        JavaType type = createSimpleJavaType(String.class);
        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        Object testInstance = createTestInstance();
        String propName = "testProperty";
        Object value = "testValue";

        JsonParser parser = createParser("{\"testProperty\":null}");
        settableAnyProperty.deserializeAndSet(parser, createContext(), testInstance, propName);

        SettableAnyProperty.AnySetterReferring referring = (SettableAnyProperty.AnySetterReferring) settableAnyProperty.getSetter().getAnnotated().getValue(testInstance);
        Object id = referring.getReferencingId();
        assertEquals(TestBase.UNRESOLVED_FORWARD_REFERENCE, id);
    }

    private AnnotatedMember createTestAnnotatedMember(String name, Class<?> type) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        Object instance = createTestInstance();
        Object field = instance.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return AnnotatedMember.Stored.create(field, createTestAnnotation(), type);
    }

    private AnnotatedMethod createTestAnnotatedMethod(String name, Class<?> returnType, Class<?>[] parameterTypes) {
        return AnnotatedMethod.Parameterized.forExecutable(createTestInstance().getClass().getMethod(name, parameterTypes), createTestAnnotation());
    }

    private JsonDeserializer<Object> createTestJsonDeserializer() {
        return new TestJsonDeserializer();
    }

    private KeyDeserializer createTestKeyDeserializer() {
        return new TestKeyDeserializer();
    }

    private TypeDeserializer createTypeDeserializer(JavaType type) {
        return new TestTypeDeserializer();
    }

    private JsonParser createParser(String content) throws IOException {
        return new JsonFactory().createParser(new StringReader(content));
    }

    private DeserializationContext createContext() {
        return new TestJsonDeserializer.TestContext();
    }

    private Object createTestInstance() throws InstantiationException, IllegalAccessException {
        return TestBase.createTestInstance();
    }

    private BeanProperty createTestBeanProperty(String name) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        return new TestBeanProperty(createTestInstance().getClass(), name);
    }
}

class TestJsonDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}

class TestKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        return key;
    }
}

class TestTypeDeserializer extends TypeDeserializer {
    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, JavaType type) throws IOException {
        return p.getValueAsString();
    }
}

class TestBeanProperty extends BeanPropertyDefinition {
    public TestBeanProperty(Class<?> beanType, String propertyName) {
        super(beanType, propertyName);
    }
}

class TestInstance {
    private Map<Object, Object> map;
}