package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtil.convertObjectId;
import static com.fasterxml.jackson.core.util.TestUtil.createObjectIdReader;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

public class ObjectIdValuePropertyTest extends TestBase {

    private ObjectIdReader objectIdReader;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        JsonDeserializer<ObjectId> objectIdDeserializer = new StdDeserializer<ObjectId>() {
            @Override
            public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return convertObjectId(p.getValueAsString());
            }
        };
        ObjectIdReader idReader = createObjectIdReader("idPropertyName", ObjectId.class, objectIdDeserializer);
        this.objectIdReader = idReader;
    }

    @Test
    public void testConstructor() {
        JsonDeserializer<String> stringDeserializer = new StdDeserializer<String>() {};
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertEquals("idPropertyName", property.getName());
        assertEquals(ObjectId.class, property.getType());
        assertNull(property.getAnnotation(JsonId.class));
        assertNull(property.getDeserializer());
        assertNull(property.getIdProperty());
        assertEquals(stringDeserializer, property.getValueDeserializer());
        assertEquals(property.getClass(), property.getClass());
    }

    @Test
    public void testCopyConstructor() {
        ObjectIdValueProperty source = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty copy = new ObjectIdValueProperty(source, new JsonDeserializer<String>() {});
        assertEquals(source, copy);
        assertEquals(source.hashCode(), copy.hashCode());
    }

    @Test
    public void testDeprecatedConstructor() {
        ObjectIdValueProperty source = new ObjectIdValueProperty(objectIdReader, new PropertyName("idPropertyName"));
        ObjectIdValueProperty copy = new ObjectIdValueProperty(source, new PropertyName("newName"));
        assertEquals(source, copy);
        assertEquals(source.hashCode(), copy.hashCode());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
        assertEquals("newName", newProperty.getName());
        assertEquals(property, newProperty.getBackRef());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty newProperty = property.withValueDeserializer(new JsonDeserializer<String>() {});
        assertEquals(property.getValueDeserializer(), newProperty.getValueDeserializer());
        assertEquals(property, newProperty.getBackRef());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getAnnotation(JsonId.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object id = new ObjectId("123");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = mock(Object.class);

        String idString = "123";
        when(property.getValueDeserializer()).thenReturn(new StdDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return idString;
            }
        });

        when(objectIdReader.getIdType()).thenReturn(ObjectId.class);
        when(objectIdReader.getDeserializer()).thenReturn(new StdDeserializer<ObjectId>() {
            @Override
            public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return id;
            }
        });

        ObjectIdValueProperty.deserializeAndSet(new JsonParser(mapper.createParser("{ \"id\": \"" + idString + "\" }")),
                mapper.getDeserializationContext(), instance);

        AnnotatedMember member = mock(AnnotatedMember.class);
        ObjectIdValueProperty idProperty = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata(), member);
        Object returned = idProperty.deserializeAndSet(new JsonParser(mapper.createParser("{ \"id\": \"" + idString + "\" }")),
                mapper.getDeserializationContext(), instance);
        assertEquals(instance, returned);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object id = new ObjectId("123");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = mock(Object.class);

        String idString = "123";
        when(property.getValueDeserializer()).thenReturn(new StdDeserializer<String>() {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return idString;
            }
        });

        when(objectIdReader.getIdType()).thenReturn(ObjectId.class);
        when(objectIdReader.getDeserializer()).thenReturn(new StdDeserializer<ObjectId>() {
            @Override
            public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return id;
            }
        });

        ObjectIdValueProperty.DeserializationContext context = mapper.getDeserializationContext();
        Object returned = property.deserializeSetAndReturn(new JsonParser(mapper.createParser("{ \"id\": \"" + idString + "\" }")),
                context, instance);
        assertEquals(instance, returned);
    }

    @Test
    public void testSet() throws IOException {
        Object id = new ObjectId("123");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = mock(Object.class);

        when(objectIdReader.getIdType()).thenReturn(ObjectId.class);
        when(objectIdReader.getDeserializer()).thenReturn(new StdDeserializer<ObjectId>() {
            @Override
            public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return id;
            }
        });

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        property.set(instance, id);
        assertEquals(instance, property.getBackRef().get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object id = new ObjectId("123");
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = mock(Object.class);

        when(objectIdReader.getIdType()).thenReturn(ObjectId.class);
        when(objectIdReader.getDeserializer()).thenReturn(new StdDeserializer<ObjectId>() {
            @Override
            public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return id;
            }
        });

        SettableBeanProperty idProperty = mock(SettableBeanProperty.class);
        when(idProperty.setAndReturn(instance, id)).thenReturn(instance);

        Object returned = property.setAndReturn(instance, id);
        assertEquals(instance, returned);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        Object instance = new Object() {
            @JsonId
            ObjectId id;
        };
        property.set(instance, new ObjectId("123"));
    }

    @Test
    public void testToStringSerializer() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
        ObjectIdValueProperty propertyWithSerializer = new ObjectIdValueProperty(property, new ToStringSerializer<ObjectId>(ObjectId.class));
        assertEquals(property, propertyWithSerializer.getBackRef());
        assertEquals(ToStringSerializer.class, propertyWithSerializer.getValueDeserializer().getClass());
    }
}