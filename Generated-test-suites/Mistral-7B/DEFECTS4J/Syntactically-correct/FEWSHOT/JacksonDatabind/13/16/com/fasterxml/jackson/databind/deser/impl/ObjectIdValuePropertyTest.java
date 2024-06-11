package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtil.constructParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotate.JsonDeserialize;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.ObjectIdReader.IdType;
import com.fasterxml.jackson.databind.deser.ValueDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class ObjectIdValuePropertyTest {

    @JsonDeserialize(using = ObjectIdValueProperty.class)
    static class TestPojo {
        private Object id;
        private String name;

        @JsonDeserialize(using = IdDeserializer.class)
        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class IdDeserializer extends StdSerializer<Object> {
        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            Object id = jp.getValueAsLong();
            return id;
        }
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader idReader = mock(ObjectIdReader.class);
        when(idReader.propertyName).thenReturn("id");
        when(idReader.getIdType()).thenReturn(IdType.LONG);
        when(idReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(idReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        ObjectIdValueProperty property = new ObjectIdValueProperty(idReader, null);

        JsonParser jp = constructParser(mapper, "[1]");
        TestPojo instance = new TestPojo();
        property.deserializeAndSet(jp, mapper.getDeserializationContext(), instance);
        assertEquals(1L, instance.getId());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader idReader = mock(ObjectIdReader.class);
        when(idReader.propertyName).thenReturn("id");
        when(idReader.getIdType()).thenReturn(IdType.LONG);
        when(idReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(idReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        ValueDeserializer valueDeserializer = mock(ValueDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(idReader, valueDeserializer);

        JsonParser jp = constructParser(mapper, "[1]");
        TestPojo instance = new TestPojo();
        Object result = property.deserializeSetAndReturn(jp, mapper.getDeserializationContext(), instance);
        assertEquals(instance, result);
        assertEquals(1L, instance.getId());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader idReader = mock(ObjectIdReader.class);
        when(idReader.propertyName).thenReturn("id");
        when(idReader.getIdType()).thenReturn(IdType.LONG);
        when(idReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(idReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        ValueDeserializer valueDeserializer = mock(ValueDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(idReader, valueDeserializer);

        TestPojo instance = new TestPojo();
        Object id = 1L;
        Object result = property.setAndReturn(instance, id);
        assertEquals(instance, result);
        assertEquals(id, instance.getId());
    }

    @Test
    public void testGetAnnotation() {
        ObjectIdReader idReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(idReader, null);
        assertNull(property.getAnnotation(JsonDeserializer.class));
    }

    @Test
    public void testGetMember() {
        ObjectIdReader idReader = mock(ObjectIdReader.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(idReader, null);
        assertNull(property.getMember());
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader idReader = mock(ObjectIdReader.class);
        when(idReader.propertyName).thenReturn("id");
        when(idReader.getIdType()).thenReturn(IdType.LONG);
        when(idReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(idReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        ValueDeserializer valueDeserializer = mock(ValueDeserializer.class);
        ObjectIdValueProperty property = new ObjectIdValueProperty(idReader, valueDeserializer);

        TestPojo instance = new TestPojo();
        instance.setId(1L);

        ObjectWriter writer = mapper.writer();
        JsonNode node = writer.writeValueAsTree(property.getName(), instance);
        assertEquals("1", node.get(property.getName().getShortName()).asText());
    }
}