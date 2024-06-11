package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.Named;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static com.fasterxml.jackson.databind.jsontype.TypeSerializer.findRawType;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper;
    private ObjectIdReader objectIdReader;
    private PropertyName idPropertyName;

    @BeforeMethod
    public void setUp() {
        mapper = new ObjectMapper();
        objectIdReader = new ObjectIdReader() {
            @Override
            public PropertyName propertyName() {
                return idPropertyName;
            }

            @Override
            public Class<?> idType() {
                return Long.class;
            }

            @Override
            public JsonDeserializer<Object> idDeserializer() {
                return new StdDeserializer<Object>(Long.class) {};
            }

            @Override
            public PropertyMetadata metadata() {
                return new PropertyMetadata() {};
            }

            @Override
            public ObjectIdGenerator idGenerator() {
                return null;
            }

            @Override
            public ObjectIdResolver idResolver() {
                return null;
            }

            @Override
            public SettableBeanProperty idProperty() {
                return new StdSettableBeanProperty("id", Long.class, null, null);
            }
        };
        idPropertyName = PropertyName.construct("id");
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        property.deserializeAndSet(mapper.getFactory().createParser("null"), mapper.getDeserializationContext(), instance);
        assertNull(instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testDeserializeAndSetValue() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        ObjectNode node = mapper.createObjectNode();
        node.put("id", 1L);
        JsonParser parser = mapper.getFactory().createParser(node.toString());

        property.deserializeAndSet(parser, mapper.getDeserializationContext(), instance);
        assertEquals(instance.getClass().getDeclaredField("id").get(instance), 1L);
    }

    @Test
    public void testDeserializeAndSetValueDeserializer() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new MyJsonDeserializer());
        Object instance = new Object();
        ObjectNode node = mapper.createObjectNode();
        node.put("id", "123");
        JsonParser parser = mapper.getFactory().createParser(node.toString());

        property.deserializeAndSet(parser, mapper.getDeserializationContext(), instance);
        assertEquals(instance.getClass().getDeclaredField("id").get(instance), 123);
    }

    @Test
    public void testDeserializeAndSetValueDeserializerNullValue() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, new MyJsonDeserializer());
        Object instance = new Object();
        ObjectNode node = mapper.createObjectNode();
        JsonParser parser = mapper.getFactory().createParser(node.toString());

        property.deserializeAndSet(parser, mapper.getDeserializationContext(), instance);
        assertNull(instance.getClass().getDeclaredField("id").get(instance));
    }

    @Test
    public void testSetValue() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Long id = 1L;

        property.setAndReturn(instance, id);
        assertEquals(instance.getClass().getDeclaredField("id").get(instance), id);
    }

    @Test
    public void testSetValueNull() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();

        assertThrows(UnsupportedOperationException.class, () -> property.setAndReturn(instance, null));
    }

    @DataProvider
    public Object[][] dataProvider() {
        return new Object[][]{
                {Long.class, Long.TYPE, Long.class, Long.TYPE},
                {Integer.class, Integer.TYPE, Integer.class, Integer.TYPE},
                {String.class, String.class, String.class, String.class},
                {Double.class, Double.TYPE, Double.class, Double.TYPE},
                {Float.class, Float.TYPE, Float.class, Float.TYPE}
        };
    }

    @Test(dataProvider = "dataProvider")
    public void testSerialization(Class<?> type, Type serializedType, Class<?> deserializedType, Type deserializedType) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdValueProperty property = new ObjectIdValueProperty(objectIdReader, null);
        Object instance = new Object();
        Long id = 1L;
        BeanPropertyDefinition definition = mapper.findPropertyDefinition(PropertyName.construct("id"), Object.class);
        Converter<Long, String> converter = ClassUtil.constructConverter(Long.class, String.class, null);
        mapper.setPropertyMetadata(definition, new PropertyMetadata() {
            @Override
            public JsonSerializer<Object> getSerializer() {
                return new StdSerializer<Object>(Object.class) {
                    @Override
                    public void serializeValues(Object p, JsonGenerator g) throws IOException {
                        g.writeStringValue(converter.valueToString(p));
                    }
                };
            }
        });

        property.setAndReturn(instance, id);
        ObjectNode node = mapper.writeValueAsTree(instance);
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        JsonParser parser = mapper.getFactory().createParser(json);

        Object deserializedInstance = mapper.readValue(parser, type);
        assertEquals(deserializedInstance.getClass().getDeclaredField("id").get(deserializedInstance), id);
        assertEquals(type, deserializedType);
        assertEquals(serializedType, findRawType(deserializedType.getTypeParameters()[0]));
    }

    private static class MyJsonDeserializer extends JsonDeserializer<Object> {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "123";
        }
    }

    private static class StdSettableBeanProperty extends SettableBeanProperty {
        public StdSettableBeanProperty(String name, Class<?> type, Annotation[] anns, JsonDeserializer<?> valueDeserializer) {
            super(name, type, null, valueDeserializer);
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return null;
        }

        @Override
        public AnnotatedMember getMember() {
            return null;
        }

        @Override
        public Object setAndReturn(Object instance, Object value) throws IOException {
            throw new UnsupportedOperationException();
        }
    }
}