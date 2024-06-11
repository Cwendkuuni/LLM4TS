package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.test.BaseTest;
import com.fasterxml.jackson.test.MockAnnotationIntrospector;

public class ObjectIdValuePropertyTest extends BaseTest {

    private static final ObjectIdReader STRING_ID_READER = new ObjectIdReaderForString();

    @Test
    public void testDeserialization() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(null);
        when(objectIdReader.resolver).thenReturn(null);
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser jp = createParser("{\"id\": \"123\"}");
        Object instance = new Object();

        property.deserializeAndSet(jp, mock(DeserializationContext.class), instance);

        assertSame(instance, jp.readValueAs(Object.class));
        verify(objectIdReader).getIdType();

        Object id = jp.getValueAsToken().getValueAsString();
        assertSame("123", id);

        Object idValue = property.get(instance);
        assertSame("123", idValue);
    }

    @Test
    public void testDeserializationWithNullId() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(null);
        when(objectIdReader.resolver).thenReturn(null);
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser jp = createParser("{\"id\": null}");
        Object instance = new Object();

        property.deserializeAndSet(jp, mock(DeserializationContext.class), instance);

        assertNull(instance.getClass().getField("id").get(instance));
    }

    @Test
    public void testDeserializationWithJsonParserFailure() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(null);
        when(objectIdReader.resolver).thenReturn(null);
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser jp = createParser("{\"id\": xxx}");
        Object instance = new Object();

        try {
            property.deserializeAndSet(jp, mock(DeserializationContext.class), instance);
            fail();
        } catch (JsonMappingException expected) {
            // expected
        }

        assertNull(instance.getClass().getField("id").get(instance));
    }

    @Test
    public void testDeserializationWithCustomObjectIdReader() throws IOException, JsonProcessingException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(Integer.class);
        when(objectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(objectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        JsonParser jp = createParser("{\"id\": 123}");
        Object instance = new Object();

        property.deserializeAndSet(jp, mock(DeserializationContext.class), instance);

        assertSame(instance, jp.readValueAs(Object.class));
        verify(objectIdReader).getIdType();

        Object id = jp.getValueAsToken().getValueAsInt();
        assertSame(123, id);

        Object idValue = property.get(instance);
        assertSame(123, idValue);
    }

    @Test
    public void testSerializationWithToStringSerializer() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(null);
        when(objectIdReader.resolver).thenReturn(null);
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(FAIL_ON_EMPTY_BEANS);
        mapper.setSerializerFactory(new DefaultSerializerFactory(new MockAnnotationIntrospector()));
        mapper.registerSerializer(Object.class, new ToStringSerializer(Object.class));

        Object instance = new Object();
        Object id = "123";
        property.set(instance, id);

        String json = mapper.writeValueAsString(instance);

        JsonParser jp = mapper.readTree(json);
        Object idValue = jp.getValueAsToken().getValueAsString();

        assertSame("123", idValue);
    }

    @Test
    public void testSerializationWithNullId() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(null);
        when(objectIdReader.resolver).thenReturn(null);
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.enable(FAIL_ON_EMPTY_BEANS);
        mapper.setSerializerFactory(new DefaultSerializerFactory(new MockAnnotationIntrospector()));

        Object instance = new Object();

        JsonNode json = mapper.writeValueAsTree(instance);
        Object idValue = json.get("id");

        assertNull(idValue);
    }

    @Test
    public void testToString() throws IOException {
        ObjectIdReader objectIdReader = mock(ObjectIdReader.class);
        when(objectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(objectIdReader.getIdType()).thenReturn(String.class);
        when(objectIdReader.generator).thenReturn(null);
        when(objectIdReader.resolver).thenReturn(null);
        when(objectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new PropertyMetadata());

        Object instance = new Object();
        Object id = "123";
        property.set(instance, id);

        String toString = property.toString();
        assertEquals("ObjectIdValueProperty(id=123)", toString);

        property.setSerializedName("foo");
        toString = property.toString();
        assertEquals("ObjectIdValueProperty(id=123, serializedName=foo)", toString);

        ObjectIdValueProperty property2 = new ObjectIdValueProperty(property, new PropertyName("bar"));
        toString = property2.toString();
        assertEquals("ObjectIdValueProperty(serializedName=bar)", toString);

        ObjectIdValueProperty property3 = new ObjectIdValueProperty(property, "bar");
        toString = property3.toString();
        assertEquals("ObjectIdValueProperty(serializedName=bar)", toString);

        ObjectIdValueProperty property4 = new ObjectIdValueProperty(property, new PropertyName("bar"), null);
        toString = property4.toString();
        assertEquals("ObjectIdValueProperty(serializedName=bar, null)", toString);

        ObjectIdValueProperty property5 = new ObjectIdValueProperty(property, "bar", null);
        toString = property5.toString();
        assertEquals("ObjectIdValueProperty(serializedName=bar, null)", toString);

        property5.setSerializer(new StdSerializer<>(String.class, null));
        toString = property5.toString();
        assertEquals("ObjectIdValueProperty(serializedName=bar, StdSerializer)", toString);
    }
}