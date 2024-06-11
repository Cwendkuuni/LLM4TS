package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

/**
 * Test cases for {@link ExternalTypeHandler} class.
 */
public class ExternalTypeHandlerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @BeforeClass
    public static void setup() {
        // Set up ObjectMapper for testing
        MAPPER.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                AddPropertiesBuilder builder = new AddPropertiesBuilder();
                builder.addExternal(new SimplePojoProperty("name"), new StringDeserializer(String.class));
                builder.addExternal(new SimplePojoProperty("typeId"), new IntDeserializer(Integer.class));
                builder.build();
            }
        });
    }

    @Test
    public void testExternalTypeHandlerStart() throws IOException {
        // Test that start method returns a new instance
        ExternalTypeHandler handler1 = ExternalTypeHandler.builder().build();
        ExternalTypeHandler handler2 = handler1.start();
        assertNotSame(handler1, handler2);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        // Test handling of type property value
        String json = "{\"name\": \"John\", \"typeId\": \"Integer\"}";
        ObjectPojo obj = MAPPER.readValue(new ByteArrayInputStream(json.getBytes()), ObjectPojo.class);
        ExternalTypeHandler handler = obj.getHandler();
        assertTrue(handler.handleTypePropertyValue(MAPPER.getFactory().createParser(json), MAPPER.getContext(), "typeId", obj));
        assertEquals("Integer", handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        // Test handling of property value
        String json = "{\"name\": \"John\", \"age\": 30}";
        ObjectPojo obj = MAPPER.readValue(new ByteArrayInputStream(json.getBytes()), ObjectPojo.class);
        ExternalTypeHandler handler = obj.getHandler();
        assertTrue(handler.handlePropertyValue(MAPPER.getFactory().createParser(json), MAPPER.getContext(), "age", obj));
        assertNull(handler._typeIds[0]);
        assertEquals(30, obj.getAge());
    }

    @Test
    public void testCompleteNullBean() throws IOException {
        // Test handling of null bean
        String json = "{\"name\": \"John\"}";
        ExternalTypeHandler handler = ExternalTypeHandler.builder().build().start();
        JsonParser p = MAPPER.getFactory().createParser(json);
        try {
            handler.complete(p, MAPPER.getContext(), null);
            fail("Expected MappingException");
        } catch (MappingException e) {
            // Expected
        }
    }

    @Test
    public void testCompleteWithoutAllProperties() throws IOException {
        // Test handling of incomplete JSON
        String json = "{\"name\": \"John\"}";
        ObjectPojo obj = MAPPER.readValue(new ByteArrayInputStream(json.getBytes()), ObjectPojo.class);
        ExternalTypeHandler handler = obj.getHandler();
        try {
            handler.complete(MAPPER.getFactory().createParser(json), MAPPER.getContext(), null);
            fail("Expected MappingException");
        } catch (MappingException e) {
            // Expected
        }
    }

    @Test
    public void testCompleteWithAllProperties() throws IOException {
        // Test handling of complete JSON
        String json = "{\"name\": \"John\", \"typeId\": \"Integer\", \"age\": 30}";
        ObjectPojo obj = MAPPER.readValue(new ByteArrayInputStream(json.getBytes()), ObjectPojo.class);
        ExternalTypeHandler handler = obj.getHandler();
        Object result = handler.complete(MAPPER.getFactory().createParser(json), MAPPER.getContext(), null);
        assertEquals("John", result.getClass().getDeclaredField("name").get(result));
        assertEquals(30, result.getClass().getDeclaredField("age").get(result));
        assertEquals("Integer", result.getClass().getDeclaredField("typeId").get(result));
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        // Test handling of complete JSON with creator properties
        String json = "{" +
                "  \"name\": \"John\"," +
                "  \"typeId\": {\"name\": \"StringType\", \"value\": \"StringType\"}," +
                "  \"age\": 30," +
                "  \"creator\": {" +
                "    \"creatorName\": \"CreatorClass\"," +
                "    \"creatorProperty\": [1]" +
                "  }" +
                "}";
        ObjectPojo obj = MAPPER.readValue(new ByteArrayInputStream(json.getBytes()), ObjectPojo.class);
        ExternalTypeHandler handler = obj.getHandler();
        Object result = handler.complete(MAPPER.getFactory().createParser(json), MAPPER.getContext(), null, null);
        assertEquals("John", result.getClass().getDeclaredField("name").get(result));
        assertEquals(30, result.getClass().getDeclaredField("age").get(result));
        assertEquals("StringType", result.getClass().getDeclaredField("typeId").get(result));
        assertEquals("CreatorClass", result.getClass().getDeclaredField("creator").getClass().getDeclaredField("name").get(result));
        assertEquals("StringType", result.getClass().getDeclaredField("creator").getClass().getDeclaredField("value").get(result));
    }

    /**
     * Simple POJO class with name and age properties.
     */
    public static class ObjectPojo {
        private String name;
        private Integer typeId;
        private int age;

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("typeId")
        public Integer getTypeId() {
            return typeId;
        }

        @JsonProperty("typeId")
        public void setTypeId(Integer typeId) {
            this.typeId = typeId;
        }

        @JsonProperty("age")
        public int getAge() {
            return age;
        }

        @JsonProperty("age")
        public void setAge(int age) {
            this.age = age;
        }

        @JsonProperty("handler")
        public ExternalTypeHandler getHandler() {
            return handler;
        }

        @JsonIgnore
        private ExternalTypeHandler handler;

        public ObjectPojo() {
            handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), null, null);
        }
    }

    /**
     * Simple builder for creating {@link ExtTypedProperty} instances.
     */
    public static class AddPropertiesBuilder {
        private final List<ExtTypedProperty> _properties = new ArrayList<ExtTypedProperty>();

        public void addExternal(SettableBeanProperty property, TypeDeserializer typeDeser) {
            _properties.add(new ExtTypedProperty(property, typeDeser));
        }

        public ExternalTypeHandler build() {
            return new ExternalTypeHandler(_properties.toArray(new ExtTypedProperty[_properties.size()]),
                    new HashMap<>(), null, null);
        }
    }

    public static class SimplePojoProperty {
        private final String _name;

        public SimplePojoProperty(String name) {
            _name = name;
        }

        public String getName() {
            return _name;
        }
    }

    @Test
    public void testTypeIdPropertyName() {
        SettableBeanProperty property = new SimplePojoProperty("typeId");
        TypeDeserializer typeDeserializer = new StringDeserializer(String.class);
        ExtTypedProperty extTypedProperty = new ExtTypedProperty(property, typeDeserializer);
        assertEquals("typeId", extTypedProperty.getTypePropertyName());
    }

    @Test
    public void testTypeIdDefaultType() {
        SettableBeanProperty property = new SimplePojoProperty("typeId");
        TypeDeserializer typeDeserializer = new StringDeserializer(String.class);
        ExtTypedProperty extTypedProperty = new ExtTypedProperty(property, typeDeserializer);
        assertTrue(extTypedProperty.hasDefaultType());
        assertEquals(String.class, extTypedProperty.getDefaultType());
    }
}