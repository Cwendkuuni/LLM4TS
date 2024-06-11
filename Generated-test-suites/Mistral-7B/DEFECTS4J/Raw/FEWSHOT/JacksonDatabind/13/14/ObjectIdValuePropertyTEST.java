package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtil.constructParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ResolverRegistry;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedObjectIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class ObjectIdValuePropertyTest {

    @SuppressWarnings("unchecked")
    private static final ObjectIdReader mockedObjectIdReader = mock(ObjectIdReader.class);
    static {
        when(mockedObjectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(mockedObjectIdReader.getIdType()).thenReturn(String.class);
        when(mockedObjectIdReader.generator).thenReturn(new NameTransformer() {
            @Override public String transform(String name, NameTransformer transformer) {
                return name;
            }
        });
        when(mockedObjectIdReader.resolver).thenReturn(new NestedObjectIdResolver());
        when(mockedObjectIdReader.idProperty).thenReturn(new MockProperty());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata()) {
            @Override public JsonDeserializer<?> getValueDeserializer() {
                return deserializer;
            }
        };

        Object id = "idValue";
        String jsonString = "{\"id\":\"" + id + "\"}";

        JsonParser jp = constructParser(jsonString);
        Object target = new Object();

        when(deserializer.deserialize(jp, argThat(isInstanceOf(DeserializationContext.class)))).thenReturn(id);

        property.deserializeAndSet(jp, mock(DeserializationContext.class), target);
        assertSame(target, target);
        assertSame(id, _getFieldValue(target, "id"));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<String> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata()) {
            @Override public JsonDeserializer<?> getValueDeserializer() {
                return deserializer;
            }
        };

        Object id = "idValue";
        String jsonString = "{\"id\":\"" + id + "\"}";

        JsonParser jp = constructParser(jsonString);
        Object target = new Object();

        when(deserializer.deserialize(jp, argThat(isInstanceOf(DeserializationContext.class)))).thenReturn(id);

        Object result = property.deserializeSetAndReturn(jp, mock(DeserializationContext.class), target);
        assertSame(target, result);
        assertSame(id, _getFieldValue(target, "id"));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata()) {
            @Override public JsonDeserializer<?> getValueDeserializer() {
                return null;
            }
        };

        Object id = "idValue";
        Object target = new Object();
        _setFieldValue(target, "id", id);

        Object result = property.setAndReturn(target, id);
        assertSame(target, result);
        assertSame(id, _getFieldValue(target, "id"));
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata());
        assertNull(property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdValueProperty property = new ObjectIdValueProperty(mockedObjectIdReader, new PropertyMetadata());

        Object id = "idValue";
        String jsonString = "{\"id\":\"" + id + "\"}";

        Object target = new Object();
        when(_objectIdReader.idProperty.get(target)).thenReturn(id);

        JsonParser jp = constructParser(jsonString);

        Object result = property.deserialize(jp, mock(DeserializationContext.class), target);
        assertSame(target, result);
        assertSame(id, _getFieldValue(target, "id"));
    }

    private static Object getFieldValue(Object instance, String fieldName) throws IOException {
        BeanPropertyDefinition def = BeanUtil.findProperty(BeanUtil.findClass(instance.getClass()), fieldName);
        return def.getBeanProperty().get(instance);
    }

    private static void setFieldValue(Object instance, String fieldName, Object value) {
        BeanPropertyDefinition def = BeanUtil.findProperty(BeanUtil.findClass(instance.getClass()), fieldName);
        SettableBeanProperty property = def.getBeanProperty();
        property.set(instance, value);
    }

    private static Object _getFieldValue(Object instance, String fieldName) throws IOException {
        Object value;
        try {
            value = getFieldValue(instance, fieldName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    private static void _setFieldValue(Object instance, String fieldName, Object value) {
        try {
            setFieldValue(instance, fieldName, value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Matcher<Object> argThat(final Class<? extends Annotation> clazz) {
        return new ArgumentMatcher<Object>() {
            @Override public boolean matches(Object argument) {
                return argument != null && argument.getClass().isAnnotationPresent(clazz);
            }
        };
    }

    private static class MockProperty extends SettableBeanProperty {
        @Override public String toString() {
            return "MockProperty{}";
        }
    }
}