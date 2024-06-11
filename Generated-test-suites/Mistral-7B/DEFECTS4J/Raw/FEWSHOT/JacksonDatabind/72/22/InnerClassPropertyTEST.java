package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDescriptor;
import com.fasterxml.jackson.databind.json.JsonDeserializer.None;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.junit.Test;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassProperty() throws IOException {
        InnerClassProperty property = new InnerClassProperty(null, null);
        assertNull(property._delegate);
        assertNull(property._annotated);
        assertNull(property._creator);

        Class innerClass = new ClassInnerClass();
        Constructor<InnerClassInner> constructor = innerClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        BeanPropertyDefinition def = new BeanPropertyDefinition(null, "inner", new JavaBeanPropertyType(InnerClassInner.class));
        AnnotatedConstructor annotated = new AnnotatedConstructor(mock(Annotation[].class), constructor);
        SettableBeanProperty delegate = new SettableBeanProperty(null, "delegate", new JavaBeanPropertyType(String.class), null, null, null, null, null, null, null, null, null, null);

        InnerClassProperty propertyWithDelegate = new InnerClassProperty(delegate, annotated);
        assertNotNull(propertyWithDelegate._delegate);
        assertNotNull(propertyWithDelegate._annotated);
        assertNotNull(propertyWithDelegate._creator);

        assertEquals("delegate", propertyWithDelegate.getName());
        assertEquals(InnerClassInner.class, propertyWithDelegate.getType());
        assertNotNull(propertyWithDelegate.getValueDeserializer());

        Object innerObject = propertyWithDelegate.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), new Object());
        assertEquals("delegateValue", innerObject);

        assertEquals("delegateValue", propertyWithDelegate.get(new Object()));
        propertyWithDelegate.set(new Object(), "newValue");
        assertEquals("newValue", propertyWithDelegate.get(new Object()));

        Object innerObjectFromSetAndReturn = propertyWithDelegate.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), new Object());
        assertEquals("delegateValue", innerObjectFromSetAndReturn);
    }

    @Test
    public void testSerializableInnerClassProperty() throws IOException {
        InnerClassProperty property = new InnerClassProperty(null, null);

        Class innerClass = new ClassInnerClass();
        Constructor<InnerClassInner> constructor = innerClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        AnnotatedConstructor annotated = new AnnotatedConstructor(mock(Annotation[].class), constructor);
        SettableBeanProperty delegate = new SettableBeanProperty(null, "delegate", new JavaBeanPropertyType(String.class), null, null, null, null, null, null, null, null, null, null);

        InnerClassProperty propertyWithDelegate = new InnerClassProperty(delegate, annotated);

        // Serialize
        ObjectWriter writer = new ObjectMapper().writerForTree(new ObjectMapper.DefaultSerializerProvider());
        String json = writer.writeValueAsString(propertyWithDelegate);
        Object readValue = new ObjectMapper().readValue(json, InnerClassProperty.class);

        // Deserialize
        ObjectMapper mapper = new ObjectMapper();
        InnerClassProperty deserializedProperty = mapper.readValue(json, InnerClassProperty.class);

        assertEquals(propertyWithDelegate, deserializedProperty);
        assertEquals(propertyWithDelegate._delegate, deserializedProperty._delegate);
        assertEquals(propertyWithDelegate._annotated, deserializedProperty._annotated);
        assertEquals(propertyWithDelegate._creator, deserializedProperty._creator);
    }

    @Test
    public void testAnnotatedInnerClassProperty() throws IOException {
        Class innerClass = new ClassInnerClass();
        Constructor<InnerClassInner> constructor = innerClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        JsonDeserializer<InnerClassInner> deserializer = new JsonDeserializer<InnerClassInner>() {
            @Override
            public InnerClassInner deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                return new InnerClassInner(value);
            }
        };

        AnnotatedConstructor annotated = new AnnotatedConstructor(new JsonTypeName("InnerClassInner"), constructor, deserializer);

        SettableBeanProperty delegate = new SettableBeanProperty(null, "delegate", new JavaBeanPropertyType(String.class), null, null, null, null, null, null, null, null, null, null);

        InnerClassProperty propertyWithDelegate = new InnerClassProperty(delegate, annotated);

        Object innerObject = propertyWithDelegate.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), new Object());
        assertEquals("delegateValue", innerObject);

        assertEquals("delegateValue", propertyWithDelegate.get(new Object()));
        propertyWithDelegate.set(new Object(), "newValue");
        assertEquals("newValue", propertyWithDelegate.get(new Object()));

        Object innerObjectFromSetAndReturn = propertyWithDelegate.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), new Object());
        assertEquals("delegateValue", innerObjectFromSetAndReturn);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerForTree(mapper.getDefaultSerializerProvider());
        String json = writer.writeValueAsString(propertyWithDelegate);

        Object readValue = mapper.readValue(json, InnerClassProperty.class);
        assertEquals(propertyWithDelegate, readValue);
    }

    @Test
    public void testInnerClassPropertyWithValueDeserializer() throws IOException {
        InnerClassProperty property = new InnerClassProperty(null, null);

        Class innerClass = new ClassInnerClass();
        Constructor<InnerClassInner> constructor = innerClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        AnnotatedConstructor annotated = new AnnotatedConstructor(mock(Annotation[].class), constructor);
        SettableBeanProperty delegate = new SettableBeanProperty(null, "delegate", new JavaBeanPropertyType(String.class), null, null, null, null, null, null, null, null, null, null);

        JsonDeserializer<InnerClassInner> deserializer = new JsonDeserializer<InnerClassInner>() {
            @Override
            public InnerClassInner deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                String value = p.getValueAsString();
                return new InnerClassInner(value);
            }
        };

        InnerClassProperty propertyWithValueDeserializer = new InnerClassProperty(delegate, annotated, deserializer);

        Object innerObject = propertyWithValueDeserializer.deserializeAndSet(mock(JsonParser.class), mock(DeserializationContext.class), new Object());
        assertEquals("delegateValue", innerObject);

        assertEquals("delegateValue", propertyWithValueDeserializer.get(new Object()));
        propertyWithValueDeserializer.set(new Object(), "newValue");
        assertEquals("newValue", propertyWithValueDeserializer.get(new Object()));

        Object innerObjectFromSetAndReturn = propertyWithValueDeserializer.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), new Object());
        assertEquals("delegateValue", innerObjectFromSetAndReturn);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerForTree(mapper.getDefaultSerializerProvider());
        String json = writer.writeValueAsString(propertyWithValueDeserializer);

        Object readValue = mapper.readValue(json, InnerClassProperty.class);
        assertEquals(propertyWithValueDeserializer, readValue);
    }

    @Test
    public void testInnerClassPropertyWithName() throws IOException {
        Class innerClass = new ClassInnerClass();
        Constructor<InnerClassInner> constructor = innerClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);

        AnnotatedConstructor annotated = new AnnotatedConstructor(mock(Annotation[].class), constructor);
        SettableBeanProperty delegate = new SettableBeanProperty(null, "delegate", new JavaBeanPropertyType(String.class), null, null, null, null, null, null, null, null, null, null);

        InnerClassProperty propertyWithDelegate = new InnerClassProperty(delegate, annotated);

        String newName = "newName";
        InnerClassProperty propertyWithName = new InnerClassProperty(propertyWithDelegate, new PropertyName(newName));

        assertEquals(newName, propertyWithName.getName());
        assertEquals("delegate", propertyWithName._delegate.getName());
        assertEquals("delegate", propertyWithName._delegate.getName());

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerForTree(mapper.getDefaultSerializerProvider());
        String json = writer.writeValueAsString(propertyWithName);

        Object readValue = mapper.readValue(json, InnerClassProperty.class);
        assertEquals(propertyWithName, readValue);
    }

    // Classes for testing
    public static class ClassInnerClass {
        @JsonAutoDetect
        public static class InnerClassInner {
            @JsonProperty("value")
            private String value;

            @JsonCreator
            public InnerClassInner(@JsonProperty("value") String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }

    public static class ConverterTest {
        @JsonProperty("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @JsonDeserialize(using = StdConverter.StdConverterDeserializer.class)
        public static class ValueDeserializer extends StdConverter<ConverterTest, String> {
            @Override
            public String convert(ConverterTest source) {
                return source.getValue();
            }
        }
    }
}

class StdConverterDeserializer extends JsonDeserializer<Converter<?, ?>> {
    @Override
    public Converter<?, ?> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectReader reader = ctxt.getFactory().getConstructorValueReader(jp.getCurrentName(), jp.getCurrentToken().getValueType());
        Converter<?, ?> converter = reader.readValueAs(Converter.class);
        return converter;
    }
}