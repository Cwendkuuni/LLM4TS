package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedPropertyIdentifier;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;

/**
 * Test cases for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

    private DeserializationContext ctxt;
    private ObjectMapper mapper;
    private AnnotationIntrospector introspector;
    private JsonParser parser;
    private ObjectIdReader objectIdReader;
    private ObjectMapper testMapper;
    private BeanPropertyDefinition idPropDef;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        ctxt = mapper.getDeserializationContext();
        introspector = mapper.getAnnotationIntrospector();
        parser = mock(JsonParser.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("idValue");
        objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.getDeserializer()).thenReturn(valueDeserializer);
        when(objectIdReader.generator).thenReturn(new ObjectIdGenerator());
        when(objectIdReader.resolver).thenReturn(new ObjectIdResolver());
        idPropDef = introspect(TestObject.class, "id");
        testMapper = new ObjectMapper();
    }

    @Test
    public void testConstructor() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertSame(objectIdReader.propertyName, property.getName());
        assertSame(Integer.class, property.getType());
        assertNull(property.getDeserializer());
        assertNull(property.getMember());
        assertNull(property.getIdProperty());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        TestObject testObject = new TestObject();
        when(parser.getValueAsString()).thenReturn("123");

        property.deserializeAndSet(parser, ctxt, testObject);

        assertSame(123, testObject.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeserializeAndSetWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        TestObject testObject = new TestObject();

        property.deserializeAndSet(parser, ctxt, testObject);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        TestObject testObject = new TestObject();
        TestObject idObject = new TestObject();
        idObject.setId(123);

        property.set(testObject, idObject);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        TestObject testObject = new TestObject();
        TestObject idObject = new TestObject();
        idObject.setId(123);

        property.setAndReturn(testObject, idObject);

        assertSame(123, testObject.getId());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturnWithNullId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        TestObject testObject = new TestObject();

        property.setAndReturn(testObject, null);
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty newProperty = property.withName(newName);

        assertSame(newName, newProperty.getName());
        assertSame(property, newProperty.getBackReference());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonDeserializer<String> newDeserializer = mock(JsonDeserializer.class);

        ObjectIdValueProperty newProperty = property.withValueDeserializer(newDeserializer);

        assertSame(newDeserializer, newProperty.getDeserializer());
        assertSame(property, newProperty.getBackReference());
    }

    @Test
    public void testWithNameAndValueDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        PropertyName newName = new PropertyName("newName");
        JsonDeserializer<String> newDeserializer = mock(JsonDeserializer.class);

        ObjectIdValueProperty newProperty = property.withName(newName).withValueDeserializer(newDeserializer);

        assertSame(newName, newProperty.getName());
        assertSame(newName, newProperty.getBackReference().getName());
        assertSame(newDeserializer, newProperty.getDeserializer());
        assertSame(property, newProperty.getBackReference());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        JsonId annotation = new JsonId();

        assertNull(property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object result = property.deserializeSetAndReturn(parser, ctxt, null);

        assertNull(result);
    }

    @Test
    public void testSet() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        TestObject testObject = new TestObject();
        TestObject idObject = new TestObject();
        idObject.setId(123);

        property.set(testObject, idObject);

        assertSame(123, testObject.getId());
    }

    @Test
    public void testSetWithNullId() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        TestObject testObject = new TestObject();

        property.set(testObject, null);

        assertNull(testObject.getId());
    }

    private BeanPropertyDefinition introspect(Class<?> clazz, String propertyName) throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        BeanProperty propertyBean = introspector.findProperty(clazz, propertyName);
        return introspector.findProperty(clazz, propertyBean.getName());
    }

    private static class TestObject {
        private Integer id;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    private static class ObjectIdGenerator extends StdKeySerializer {
        @Override
        public void serialize(Object id, JsonGenerator jg, SerializerProvider sp) throws IOException {
            jg.writeNumber(id.hashCode());
        }
    }

    private static class ObjectIdResolver extends StdKeyDeserializer {
        @Override
        public Object deserializeKey(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getValueAsNumber().longValue();
        }
    }
}