package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":\"type1\"}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        assertTrue(handler.handleTypePropertyValue(jp, mockContext(), "property1", null));
        assertEquals("type1", handler.getExpectedTypeId(0));
        assertEquals("property1", handler.getPropertyName(0));
    }

    @Test
    public void testHandleTypePropertyValueMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"missingProperty\":\"type1\"}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        assertFalse(handler.handleTypePropertyValue(jp, mockContext(), "missingProperty", null));
        assertNull(handler.getExpectedTypeId(0));
        assertNull(handler.getPropertyName(0));
    }

    @Test
    public void testHandleTypePropertyValueMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":null}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        assertFalse(handler.handleTypePropertyValue(jp, mockContext(), "property1", null));
        assertNull(handler.getExpectedTypeId(0));
        assertNull(handler.getPropertyName(0));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":123}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        assertTrue(handler.handlePropertyValue(jp, mockContext(), "property1", null));

        assertEquals("123", handler.getExpectedTypeId(0));
        assertEquals("property1", handler.getPropertyName(0));

        JsonParser parsedValue = handler.getParsedValue(0);
        assertEquals(JsonToken.VALUE_NUMBER_INT, parsedValue.getCurrentToken().getId());
        assertEquals(123, parsedValue.getValueAsInt());
    }

    @Test
    public void testHandlePropertyValueNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":null}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        assertTrue(handler.handlePropertyValue(jp, mockContext(), "property1", null));

        assertNull(handler.getExpectedTypeId(0));
        assertNull(handler.getPropertyName(0));

        JsonParser parsedValue = handler.getParsedValue(0);
        assertEquals(JsonToken.START_OBJECT, parsedValue.getCurrentToken().getId());
    }

    @Test
    public void testHandlePropertyValueMissingProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"missingProperty\":123}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        assertFalse(handler.handlePropertyValue(jp, mockContext(), "missingProperty", null));

        assertNull(handler.getExpectedTypeId(0));
        assertNull(handler.getPropertyName(0));
    }

    @Test
    public void testHandlePropertyValueMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":\"\"}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        assertFalse(handler.handlePropertyValue(jp, mockContext(), "property1", null));

        assertNull(handler.getExpectedTypeId(0));
        assertNull(handler.getPropertyName(0));
    }

    @Test
    public void testComplete() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":\"value1\"}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        handler.start();

        handler.handleTypePropertyValue(jp, mockContext(), "foo", null);
        handler.handlePropertyValue(jp, mockContext(), "property1", null);

        Object result = handler.complete(jp, mockContext(), null);
        assertEquals(mapper.readValue(jp, Map.class), result);
    }

    @Test
    public void testCompleteMissingTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":\"value1\"}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        handler.start();

        handler.handleTypePropertyValue(jp, mockContext(), "foo", null);
        handler.handlePropertyValue(jp, mockContext(), "property1", null);
        handler.handlePropertyValue(jp, mockContext(), "missingProperty", null);

        try {
            handler.complete(jp, mockContext(), null);
            fail("Expected MappingException");
        } catch (JsonMappingException e) {
            assertEquals("Missing property 'missingProperty' for external type id 'null'", e.getMessage());
        }
    }

    @Test
    public void testCompleteMissingTypeIdProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":123}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        handler.start();

        handler.handlePropertyValue(jp, mockContext(), "property1", null);

        try {
            handler.complete(jp, mockContext(), null);
            fail("Expected MappingException");
        } catch (JsonMappingException e) {
            assertEquals("Missing property 'property1' for external type id 'null'", e.getMessage());
        }
    }

    @Test
    public void testCompleteWithNullTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":null}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        handler.start();

        handler.handlePropertyValue(jp, mockContext(), "property1", null);

        Object result = handler.complete(jp, mockContext(), null);
        assertNull(result);
    }

    @Test
    public void testCompleteWithNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":null}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        handler.start();

        handler.handleTypePropertyValue(jp, mockContext(), "foo", null);
        handler.handlePropertyValue(jp, mockContext(), "property1", null);

        Object result = handler.complete(jp, mockContext(), null);
        assertNull(result);
    }

    @Test
    public void testCompleteWithNullTypeIdAndValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        handler.start();

        Object result = handler.complete(jp, mockContext(), null);
        assertNull(result);
    }

    @Test
    public void testCompleteWithNullTypeIdPropertyAndValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{}");
        ExternalTypeHandler handler = createHandler(jp, mapper);
        handler.start();

        handler.handleTypePropertyValue(jp, mockContext(), "foo", null);
        handler.handlePropertyValue(jp, mockContext(), "property1", null);

        Object result = handler.complete(jp, mockContext(), null);
        assertNull(result);
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":null}");
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        ExternalTypeHandler handler = createHandler(jp, mapper);
        handler.start();

        handler.handleTypePropertyValue(jp, mockContext(), "foo", null);
        handler.handlePropertyValue(jp, mockContext(), "property1", null);

        Object result = handler.complete(jp, mockContext(), null, creator.mockBuilder().build());
        assertNotNull(result);
    }

    @Test
    public void testCompleteWithCreatorAndValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jp = mapper.getFactory().createParser("{\"foo\":\"StringType\", \"property1\":\"value1\"}");
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        ExternalTypeHandler handler = createHandler(jp, mapper);
        handler.start();

        handler.handleTypePropertyValue(jp, mockContext(), "foo", null);
        handler.handlePropertyValue(jp, mockContext(), "property1", "value1");

        Object result = handler.complete(jp, mockContext(), null, creator.mockBuilder().build());
        assertEquals("value1", creator.getPropertyValue(0));
        assertEquals("value1", result.getClass().getDeclaredField("property1").get(result));
    }

    private ExternalTypeHandler createHandler(JsonParser jp, ObjectMapper mapper) throws IOException {
        ObjectCodec codec = mapper.getObjectCodec();
        return new ExternalTypeHandler(codec.getDeserializationConfig().findBeanDeserializer(Object.class),
                new ArrayList<>(), null, null);
    }

    private DeserializationContext mockContext() {
        return mock(DeserializationContext.class);
    }

    private static Deserializer.PropertyName propertyName(String name) {
        return new Deserializer.PropertyName("test", name);
    }

    private static PropertyBasedCreator mockCreator(Class<?> clazz) {
        return mock(PropertyBasedCreator.class, new Mockito.MockitoSession().getMockFactory().createMock(PropertyBasedCreator.class, new Object[] { clazz }));
    }

    private static PropertyBasedCreator.Builder mockCreatorBuilder(Class<?> clazz) {
        return mock(PropertyBasedCreator.Builder.class, new Object[] { clazz });
    }
}