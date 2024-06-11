import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() throws JsonProcessingException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        ExternalTypeHandler startedHandler = handler.start();
        assertNotNull(startedHandler);
    }

    @Test
    public void testHandleTypePropertyValueHappyPath() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"typeId\": \"StringType\"}");
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        boolean result = handler.handleTypePropertyValue(mockParser, mockContext, "typeId", mockBean);
        assertTrue(result);
    }

    @Test
    public void testHandleTypePropertyValueNullPropertyName() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"nullProp\": \"StringType\"}");
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        boolean result = handler.handleTypePropertyValue(mockParser, mockContext, "nullProp", mockBean);
        assertFalse(result);
    }

    @Test(expected = IOException.class)
    public void testHandleTypePropertyValueNullBean() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"typeId\": \"StringType\"}");
        DeserializationContext mockContext = createMockContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId", null);
    }

    @Test(expected = IOException.class)
    public void testHandleTypePropertyValueNullTokenBuffer() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"typeId\": \"StringType\"}");
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        handler._tokens = null;
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId", mockBean);
    }

    @Test
    public void testHandleTypePropertyValueWrongPropertyName() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"wrongProp\": \"StringType\"}");
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        boolean result = handler.handleTypePropertyValue(mockParser, mockContext, "wrongProp", mockBean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValueHappyPath() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"property\": \"value\"}");
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        boolean result = handler.handlePropertyValue(mockParser, mockContext, "property", mockBean);
        assertTrue(result);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueNullParser() throws JsonProcessingException {
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        handler.handlePropertyValue(null, mockContext, "property", mockBean);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueNullBean() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"property\": \"value\"}");
        DeserializationContext mockContext = createMockContext();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        handler.handlePropertyValue(mockParser, mockContext, "property", null);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueNullTypeId() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"property\": null}");
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        handler.handlePropertyValue(mockParser, mockContext, "property", mockBean);
    }

    @Test
    public void testCompleteHappyPath() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"typeId1\": \"StringType1\", \"typeId2\": \"StringType2\"}");
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId1", mockBean);
        handler.handlePropertyValue(mockParser, mockContext, "property1", mockBean);
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId2", mockBean);
        handler.handlePropertyValue(mockParser, mockContext, "property2", mockBean);
        Object result = handler.complete(mockParser, mockContext, mockBean);
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testCompleteMissingProperty() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"typeId\": \"StringType\"}");
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId", mockBean);
        handler.handlePropertyValue(mockParser, mockContext, "property", mockBean);
        handler.complete(mockParser, mockContext, mockBean);
        fail("Expected JsonMappingException to be thrown");
    }

    @Test(expected = JsonProcessingException.class)
    public void testCompleteMissingTypeId() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"property\": \"value\"}");
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId", mockBean);
        handler.handlePropertyValue(mockParser, mockContext, "property", mockBean);
        handler.complete(mockParser, mockContext, mockBean);
        fail("Expected JsonProcessingException to be thrown");
    }

    @Test
    public void testCompleteWithNullValues() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"typeId1\": null, \"typeId2\": null}");
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId1", mockBean);
        handler.handlePropertyValue(mockParser, mockContext, "property1", mockBean);
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId2", mockBean);
        handler.handlePropertyValue(mockParser, mockContext, "property2", mockBean);
        Object result = handler.complete(mockParser, mockContext, mockBean);
        assertNotNull(result);
    }

    @Test
    public void testCompleteWithNonCreatorProperties() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"typeId1\": \"StringType1\", \"typeId2\": \"StringType2\"}");
        DeserializationContext mockContext = createMockContext();
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId1", mockBean);
        handler.handlePropertyValue(mockParser, mockContext, "property1", mockBean);
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId2", mockBean);
        handler.handlePropertyValue(mockParser, mockContext, "property2", mockBean);
        Object result = handler.complete(mockParser, mockContext, mockBean);
        assertNotNull(result);
        SettableBeanProperty property1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty property2 = Mockito.mock(SettableBeanProperty.class);
        Mockito.when(property1.getName()).thenReturn("property1");
        Mockito.when(property2.getName()).thenReturn("property2");
        assertEquals(property1.getValue(result), Mockito.any());
        assertEquals(property2.getValue(result), Mockito.any());
    }

    @Test
    public void testCompleteWithCreatorProperties() throws JsonProcessingException {
        JsonParser mockParser = createMockParser("{\"typeId1\": \"StringType1\", \"typeId2\": \"StringType2\"}");
        DeserializationContext mockContext = createMockContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer pvb) throws IOException {
                Object bean = new Object();
                pvb.assignParameter(Mockito.mock(SettableBeanProperty.class), "value1");
                pvb.assignParameter(Mockito.mock(SettableBeanProperty.class), "value2");
                return bean;
            }
        };
        Object mockBean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder().build());
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId1", mockBean);
        handler.handlePropertyValue(mockParser, mockContext, "property1", mockBean);
        handler.handleTypePropertyValue(mockParser, mockContext, "typeId2", mockBean);
        handler.handlePropertyValue(mockParser, mockContext, "property2", mockBean);
        Object result = handler.complete(mockParser, mockContext, buffer, creator);
        assertNotNull(result);
        SettableBeanProperty property1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty property2 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty typeProperty1 = Mockito.mock(SettableBeanProperty.class);
        SettableBeanProperty typeProperty2 = Mockito.mock(SettableBeanProperty.class);
        Mockito.when(property1.getName()).thenReturn("property1");
        Mockito.when(property2.getName()).thenReturn("property2");
        Mockito.when(typeProperty1.getName()).thenReturn("typeId1");
        Mockito.when(typeProperty2.getName()).thenReturn("typeId2");
        assertEquals(property1.getValue(result), "value1");
        assertEquals(property2.getValue(result), "value2");
        assertEquals(typeProperty1.getValue(result), "StringType1");
        assertEquals(typeProperty2.getValue(result), "StringType2");
    }

    private JsonParser createMockParser(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(json.getBytes()));
    }

    private DeserializationContext createMockContext() {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getDeserializationContext();
    }
}