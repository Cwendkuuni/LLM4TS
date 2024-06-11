package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ObjectIdValuePropertyTest {

    private static final ObjectIdReader objectIdReader = mock(ObjectIdReader.class);

    @Test
    public void testBasic() throws IOException {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        assertEquals(objectIdReader.propertyName, property.getName());
        assertEquals(IdType.STR, property.getIdType());
        assertEquals(null, property.getMember());
        assertEquals(valueDeserializer, property.getValueDeserializer());
    }

    @Test
    public void testWithName() throws IOException {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        PropertyName newName = mock(PropertyName.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(new ObjectIdValueProperty(objectIdReader, valueDeserializer), newName);
        assertEquals(newName, property.getName());
    }

    @Test
    public void testWithValueDeserializer() throws IOException {
        JsonDeserializer<Object> newValueDeserializer = mock(JsonDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, valueDeserializer);
        ObjectIdValueProperty newProperty = new ObjectIdValueProperty(property, newValueDeserializer);
        assertEquals(newValueDeserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = "123";
        Object instance = mock(Object.class);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        ObjectIdReader reader = new ObjectIdReader("id", IdType.STR, null, null, null, null, null);

        when(objectIdReader.getName()).thenReturn(reader.propertyName);
        when(objectIdReader.getIdType()).thenReturn(reader.getIdType());
        when(objectIdReader.getIdProperty()).thenReturn(null);
        when(objectIdReader.getValueDeserializer()).thenReturn(valueDeserializer);
        when(valueDeserializer.deserialize(parser, context)).thenReturn(id);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        property.deserializeAndSet(parser, context, instance);

        ObjectId idProp = mock(ObjectId.class);
        when(context.findObjectId(id, reader.generator, reader.resolver)).thenReturn(idProp);
        when(idProp.bindItem(instance)).thenReturn(instance);

        Object idValue = property.getIdProperty().setAndReturn(instance, id);
        assertEquals(id, idValue);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = mock(Object.class);
        Object id = "123";
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object idValue = property.setAndReturn(instance, id);
        assertEquals(id, idValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        Object instance = mock(Object.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        property.set(instance, "123");
    }

    @Test
    public void testGetAnnotation() throws IOException {
        assertEquals(null, ObjectIdValueProperty.class.getDeclaredMethod("getAnnotation", Annotation.class).invoke(null, null));
    }

    @Test
    public void testGetMember() {
        assertEquals(null, ObjectIdValueProperty.class.getDeclaredMethod("getMember").invoke(null));
    }

    @Test
    public void testDeserializeAndSetNullId() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        ObjectIdReader reader = new ObjectIdReader("id", IdType.STR, null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);
        property.deserializeAndSet(parser, context, instance);
        assertNull(property.getIdProperty().get(instance));
    }

    @Test
    public void testSimpleBeanPropertyFilter() throws IOException {
        ObjectIdReader reader = new ObjectIdReader("id", IdType.STR, null, null, null, null, null);
        ObjectIdValueProperty property = new ObjectIdValueProperty(reader, null);
        SimpleBeanPropertyFilter filter = new SimpleBeanPropertyFilter(Object.class, ClassUtil.filterNullAndIsDefined(new String[]{"id"}));

        PropertyName pName = PropertyName.construct("id", "");
        ObjectIdValueProperty nameOnlyProperty = new ObjectIdValueProperty(reader, null);
        assertEquals(pName, nameOnlyProperty.getName());
        assertEquals(NameFilter.FILTER_NAME, filter.filterName(nameOnlyProperty));

        ObjectIdValueProperty filteredProperty = new ObjectIdValueProperty(property, filter);
        assertEquals(property, filteredProperty);
        assertEquals(pName, filteredProperty.getName());
    }
}