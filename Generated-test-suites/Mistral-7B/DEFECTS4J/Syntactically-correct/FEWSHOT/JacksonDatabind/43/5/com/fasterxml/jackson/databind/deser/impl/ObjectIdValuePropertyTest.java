package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.FieldVisibility.ANY;
import static com.fasterxml.jackson.core.Version.VERSION_2_11;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.BeanPropertyDeserializer;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Test cases for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonAutoDetect.Visibility.ANY);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String id = "123";
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(new BeanPropertyDefinition("idProp", String.class, ANY));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        ObjectReader reader = mapper.readerForUpdating(String.class, property);
        JsonParser p = mapper.getFactory().createParser("{\"id\":\"" + id + "\"}");
        Object instance = mapper.readValue(p, String.class);

        property.deserializeAndSet(p, mock(DeserializationContext.class), instance);

        assertEquals(id, instance);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        String id = "123";
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(new BeanPropertyDefinition("idProp", String.class, ANY));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();

        Object id = property.setAndReturn(instance, id);

        assertEquals(id, instance);
    }

    @Test
    public void testSetAndReturnNull() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(new BeanPropertyDefinition("idProp", String.class, ANY));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();

        Object id = property.setAndReturn(instance, null);

        assertNull(id);
    }

    @Test
    public void testDeserializeWithIdProperty() throws Exception {
        String id = "123";
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        BeanPropertyDeserializer idPropertyDeserializer = mock(BeanPropertyDeserializer.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(new BeanPropertyDefinition("idProp", String.class, ANY));
        when(objectIdReader.idProperty.getName()).thenReturn("idProp");
        when(objectIdReader.idProperty.getMember()).thenReturn(mock(AnnotatedMember.class));
        when(objectIdReader.idProperty.getAnnotation(JsonAutoDetect.class)).thenReturn(new JsonAutoDetect());
        when(idPropertyDeserializer.createContextualDeserializer(mock(DeserializationContext.class), mock(BeanPropertyDefinition.class))).thenReturn(mock(JsonDeserializer.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, idPropertyDeserializer);

        ObjectReader reader = mapper.readerForUpdating(String.class, property);
        JsonParser p = mapper.getFactory().createParser("{\"idProp\":\"" + id + "\"}");
        Object instance = mapper.readValue(p, String.class);

        property.deserializeAndSet(p, mock(DeserializationContext.class), instance);

        assertEquals(id, instance);
    }

    @Test
    public void testDeserializeWithTypeDeserializer() throws Exception {
        String id = "123";
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        TypeDeserializer idTypeDeserializer = mock(TypeDeserializer.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(null);
        when(objectIdReader.getIdTypeDeserializer()).thenReturn(idTypeDeserializer);

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        ObjectReader reader = mapper.readerForUpdating(String.class, property);
        JsonParser p = mapper.getFactory().createParser("{\"id\":\"" + id + "\"}");
        Object instance = mapper.readValue(p, String.class);

        property.deserializeAndSet(p, mock(DeserializationContext.class), instance);

        assertEquals(id, instance);
    }

    @Test
    public void testDeserializeSetAndReturnNull() throws Exception {
        String id = "123";
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(null);
        when(objectIdReader.getIdTypeDeserializer()).thenReturn(mock(TypeDeserializer.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();

        Object id = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);

        assertNull(id);
    }

    @Test
    public void testDeserializeSetAndReturnEmptyString() throws Exception {
        String id = "";
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(null);
        when(objectIdReader.getIdTypeDeserializer()).thenReturn(mock(TypeDeserializer.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();

        Object id = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);

        assertEquals(id, instance);
        assertEquals("", id);
    }

    @Test
    public void testDeserializeSetAndReturnNullId() throws Exception {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(null);
        when(objectIdReader.getIdTypeDeserializer()).thenReturn(mock(TypeDeserializer.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);

        Object instance = new Object();

        Object id = property.deserializeSetAndReturn(mock(JsonParser.class), mock(DeserializationContext.class), instance);

        assertNull(id);
    }
}