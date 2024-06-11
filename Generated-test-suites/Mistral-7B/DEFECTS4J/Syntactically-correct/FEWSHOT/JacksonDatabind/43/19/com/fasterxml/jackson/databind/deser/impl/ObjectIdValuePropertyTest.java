package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtil.constructTestObject;
import static com.fasterxml.jackson.databind.DeserializationTest.mapper;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.ToStringBeanProperty;
import com.fasterxml.jackson.databind.util.BeanUtil;

/**
 * Test cases for {@link ObjectIdValueProperty} class.
 */
public class ObjectIdValuePropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testObjectIdValueProperty() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(ObjectIdType.Long.name());
        when(objectIdReader.getDeserializer()).thenReturn(mock(JsonDeserializer.class));
        when(objectIdReader.idProperty).thenReturn(new ToStringBeanProperty.Std("idValue"));
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        assertSame(property.getClass(), ObjectIdValueProperty.class);
        assertSame(property.getName(), "id");
        assertSame(property.getIdType(), ObjectIdType.Long);
        assertEquals(property.getMetadata(), PropertyMetadata.empty());
        assertSame(property.getDeserializer(), mock(JsonDeserializer.class));
        assertSame(property._objectIdReader, objectIdReader);

        // test withName
        PropertyName newName = new PropertyName("newName");
        ObjectIdValueProperty propertyWithNewName = property.withName(newName);
        assertEquals(propertyWithNewName.getName(), "newName");
        assertEquals(propertyWithNewName.getClass(), ObjectIdValueProperty.class);

        // test withValueDeserializer
        JsonDeserializer<Long> deserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty propertyWithDeserializer = property.withValueDeserializer(deserializer);
        assertSame(propertyWithDeserializer._valueDeserializer, deserializer);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyName propertyName = new PropertyName("id");
        JsonDeserializer<Long> idDeserializer = mock(JsonDeserializer.class);
        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(ObjectIdType.Long.name());
        when(objectIdReader.getDeserializer()).thenReturn(idDeserializer);
        when(objectIdReader.idProperty).thenReturn(null);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        JsonParser p = mapper().getFactory().createParser("[1]");
        Object instance = constructTestObject(new TestObject());

        property.deserializeAndSet(p, mapper().getDeserializationContext(), instance);

        assertEquals(1, instance.getId());

        p.close();
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyName propertyName = new PropertyName("id");
        JsonDeserializer<Long> idDeserializer = mock(JsonDeserializer.class);
        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(ObjectIdType.Long.name());
        when(objectIdReader.getDeserializer()).thenReturn(idDeserializer);
        when(objectIdReader.idProperty).thenReturn(new ToStringBeanProperty.Std("idValue"));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        JsonParser p = mapper().getFactory().createParser("[1]");
        Object instance = constructTestObject(new TestObject());

        Object result = property.deserializeSetAndReturn(p, mapper().getDeserializationContext(), instance);

        assertEquals(instance, result);
        assertEquals(1, instance.getId());

        p.close();
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyName propertyName = new PropertyName("id");
        JsonDeserializer<Long> idDeserializer = mock(JsonDeserializer.class);
        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(ObjectIdType.Long.name());
        when(objectIdReader.getDeserializer()).thenReturn(idDeserializer);
        when(objectIdReader.idProperty).thenReturn(new ToStringBeanProperty.Std("idValue"));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        TestObject instance = new TestObject();
        Long id = 1L;
        property.setAndReturn(instance, id);

        assertEquals(id, instance.getId());
    }

    @Test
    public void testSetNull() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        PropertyName propertyName = new PropertyName("id");
        JsonDeserializer<Long> idDeserializer = mock(JsonDeserializer.class);
        when(objectIdReader.propertyName).thenReturn(propertyName);
        when(objectIdReader.getIdType()).thenReturn(ObjectIdType.Long.name());
        when(objectIdReader.getDeserializer()).thenReturn(idDeserializer);
        when(objectIdReader.idProperty).thenReturn(null);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, PropertyMetadata.empty());

        TestObject instance = new TestObject();
        property.setAndReturn(instance, null);

        assertNull(instance.getId());
    }

    static class TestObject {
        @JsonIdentityInfo(generator = ObjectIdGenerators.IntArrayGenerator.class)
        @JsonProperty("idValue")
        @JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
        private Long id;

        public Long getId() {
            return id;
        }
    }
}