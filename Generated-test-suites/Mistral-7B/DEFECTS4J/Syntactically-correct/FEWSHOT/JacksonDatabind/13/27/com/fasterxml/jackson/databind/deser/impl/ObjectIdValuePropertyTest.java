package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class ObjectIdValuePropertyTest extends TestBase {

    private ObjectIdValueProperty property;
    private ObjectIdReader objectIdReader;
    private JsonDeserializer<Object> idDeserializer;
    private JsonDeserializer<String> stringDeserializer;
    private JsonParser jp;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        objectIdReader = mock(ObjectIdReader.class);
        idDeserializer = mock(JsonDeserializer.class);
        stringDeserializer = new StdSerializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                return p.getValueAsString();
            }
        };
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.getIdProperty()).thenReturn(null);
        when(objectIdReader.getDeserializer()).thenReturn(idDeserializer);
        jp = constructParser();
        property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());
    }

    @Test
    public void testWithName() {
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName"));
        assertEquals("newName", newProperty.getName().getSimpleName());
    }

    @Test
    public void testWithValueDeserializer() {
        ObjectIdValueProperty newProperty = property.withValueDeserializer(stringDeserializer);
        assertEquals(stringDeserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testGetAnnotation() {
        assertNull(property.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testGetMember() {
        assertNull(property.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String id = "idValue";
        objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.getIdProperty()).thenReturn(property);
        when(objectIdReader.getDeserializer()).thenReturn(stringDeserializer);
        property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        jp.setValue("idValue");
        Object instance = new Object();
        property.deserializeAndSet(jp, context(), instance);
        assertEquals(id, instance.getClass().getDeclaredField("_id").get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        String id = "idValue";
        Object instance = new Object();
        when(idDeserializer.deserialize(jp, context())).thenReturn(id);

        Object result = property.deserializeSetAndReturn(jp, context(), instance);
        assertEquals(id, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        String id = "idValue";

        when(objectIdReader.getIdProperty()).thenReturn(new SettableBeanProperty(new PropertyName("_id"), String.class, null) {
            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                this.instance = instance;
                this.value = value;
                return null;
            }
        });

        property.set(instance, id);
        assertEquals(id, instance.getClass().getDeclaredField("_id").get(instance));
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        String id = "idValue";

        when(objectIdReader.getIdProperty()).thenReturn(new SettableBeanProperty(new PropertyName("_id"), String.class, null) {
            @Override
            public Object setAndReturn(Object instance, Object value) throws IOException {
                this.instance = instance;
                this.value = value;
                return null;
            }
        });

        Object result = property.setAndReturn(instance, id);
        assertEquals(id, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetNull() throws IOException {
        Object instance = new Object();
        property.set(instance, null);
    }

    @Test
    public void testSetAndReturnNull() throws IOException {
        Object instance = new Object();
        property.setAndReturn(instance, null);
    }

    @Test
    public void testSetValueDeserializer() {
        ObjectIdValueProperty newProperty = new ObjectIdValueProperty(property, stringDeserializer);
        assertEquals(stringDeserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testClone() {
        ObjectIdValueProperty newProperty = property.clone();
        assertSame(property, newProperty);
    }

    @Test
    public void testCloneWithValueDeserializer() {
        ObjectIdValueProperty newProperty = property.withValueDeserializer(stringDeserializer).clone();
        assertNotSame(property, newProperty);
        assertEquals(stringDeserializer, newProperty.getValueDeserializer());
    }

    @Test
    public void testCloneWithName() {
        ObjectIdValueProperty newProperty = property.withName(new PropertyName("newName")).clone();
        assertNotSame(property, newProperty);
        assertEquals("newName", newProperty.getName().getSimpleName());
    }

    @Test
    public void testCloneWithDeserializer() {
        ObjectIdValueProperty newProperty = property.withDeserializer(stringDeserializer).clone();
        assertNotSame(property, newProperty);
        assertEquals(stringDeserializer, newProperty.getDeserializer());
    }

    @Test
    public void testCloneWithIdProperty() {
        ObjectIdValueProperty newProperty = property.withIdProperty(new PropertyName("newIdProperty")).clone();
        assertNotSame(property, newProperty);
        assertEquals("newIdProperty", newProperty.getName().getSimpleName());
    }

    @Test
    public void testCloneWithIdReader() {
        ObjectIdValueProperty newProperty = property.withIdReader(new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return new PropertyName("newName");
            }

            @Override
            public ObjectIdType idType() {
                return ObjectIdType.INT;
            }

            @Override
            public JsonDeserializer<Object> getIdDeserializer() {
                return null;
            }

            @Override
            public ObjectIdGenerator<?> generator() {
                return null;
            }

            @Override
            public ObjectIdResolver<?> resolver() {
                return null;
            }
        }).clone();
        assertNotSame(property, newProperty);
        assertEquals("newName", newProperty.getName().getSimpleName());
    }
}