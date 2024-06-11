import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws JsonProcessingException {
        String json = "{\"typeId\": \"StringType\", \"property\": \"value\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{"StringType"}, new TokenBuffer[1]);
        JsonParser jp = createParser(json);
        DeserializationContext ctxt = createContext();
        Object bean = new Object();

        assertTrue(handler.handleTypePropertyValue(jp, ctxt, "typeId", bean));
        assertEquals("StringType", handler.getTypeIds()[0]);

        jp.nextToken(); // move to the next token after "typeId"

        // test handleTypePropertyValue when type id property is missing
        assertFalse(handler.handleTypePropertyValue(jp, ctxt, "property", bean));
    }

    @Test
    public void testHandlePropertyValue() throws JsonProcessingException {
        String json = "{\"property\": \"value\"}";
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[1], new TokenBuffer[1]);
        JsonParser jp = createParser(json);
        DeserializationContext ctxt = createContext();
        Object bean = new Object();

        assertTrue(handler.handlePropertyValue(jp, ctxt, "property", bean));

        // test handlePropertyValue when type property name is not matched
        JsonParser jp2 = createParser(new StringBuilder(json.substring(0, json.lastIndexOf(":"))).append("\"unmatchedPropertyName\":\"value\"").toString());
        assertFalse(handler.handlePropertyValue(jp2, ctxt, "property", bean));
    }

    @Test
    public void testComplete() throws JsonProcessingException {
        String json = "{\"property1\": null, \"property2\": null, \"property3\": {}}";
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), null, null);
        handler.start();

        JsonParser jp = createParser(json);
        DeserializationContext ctxt = createContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "property1", bean);
        handler.handlePropertyValue(jp, ctxt, "property2", bean);

        try {
            handler.complete(jp, ctxt, bean);
            fail("Expected MappingException");
        } catch (Exception e) {
            assertEquals("Missing property 'property3' for external type id 'null'", e.getMessage());
        }

        handler.handleTypePropertyValue(jp, ctxt, "property3", bean);

        Object result = handler.complete(jp, ctxt, bean);
        assertNotNull(result);
    }

    @Test(expected = JsonProcessingException.class)
    public void testCompleteWithMissingTypeId() throws JsonProcessingException {
        String json = "{\"property1\": null, \"property2\": null, \"property3\": null}";
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), null, null);
        handler.start();

        JsonParser jp = createParser(json);
        DeserializationContext ctxt = createContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "property1", bean);
        handler.handlePropertyValue(jp, ctxt, "property2", bean);

        handler.complete(jp, ctxt, bean);
    }

    @Test
    public void testCompleteWithNullValue() throws JsonProcessingException {
        String json = "{\"property1\": null, \"property2\": null, \"property3\": null}";
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), null, null);
        handler.start();

        JsonParser jp = createParser(json);
        DeserializationContext ctxt = createContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "property1", bean);
        handler.handlePropertyValue(jp, ctxt, "property2", bean);

        handler.handleTypePropertyValue(jp, ctxt, "property3", bean);
        handler.handlePropertyValue(jp, ctxt, "property3", bean);

        Object result = handler.complete(jp, ctxt, bean);
        assertNotNull(result);
    }

    @Test
    public void testCompleteWithNaturalType() throws JsonProcessingException {
        String json = "{\"property\": null}";
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), null, null);
        handler.start();

        JsonParser jp = createParser(json);
        DeserializationContext ctxt = createContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "property", bean);

        String typeId = handler.getTypeIds()[0];
        assertEquals(Integer.class.getName(), typeId);

        Object result = handler.complete(jp, ctxt, bean);
        assertNotNull(result);
    }

    @Test
    public void testCompleteWithCreator() throws JsonProcessingException {
        String json = "[{\"creatorProperty\": \"creatorValue\"}, {\"property\": \"value\"}]";
        ExternalTypeHandler handler = new ExternalTypeHandler(createPropertiesWithCreator(), createNameToPropertyIndexWithCreator(), null, null);
        handler.start();

        JsonParser jp = createParser(json);
        DeserializationContext ctxt = createContext();
        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "creatorProperty", bean);

        Object creatorResult = handler.complete(jp, ctxt, null, new PropertyBasedCreator() {
            @Override
            public Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
                Object creatorValue = buffer.getParameter("creatorProperty");
                return creatorValue;
            }
        });
        assertNotNull(creatorResult);

        handler.handleTypePropertyValue(jp, ctxt, "property", bean);
        Object propertyResult = handler.complete(jp, ctxt, bean);
        assertNotNull(propertyResult);
    }

    private ExternalTypeHandler createHandler() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        return handler;
    }

    private ObjectMapper createMapper() {
        return new ObjectMapper();
    }

    private ExtTypedProperty[] createProperties() {
        ExtTypedProperty[] properties = new ExtTypedProperty[3];
        SettableBeanProperty prop1 = new SettableBeanProperty(null, "property1", String.class);
        SettableBeanProperty prop2 = new SettableBeanProperty(null, "property2", Integer.class);
        SettableBeanProperty prop3 = new SettableBeanProperty(null, "property3", Double.class);
        properties[0] = new ExtTypedProperty(prop1, null);
        properties[1] = new ExtTypedProperty(prop2, new SimpleTypeDeserializer(Integer.class));
        properties[2] = new ExtTypedProperty(prop3, new SimpleTypeDeserializer(Double.class));
        return properties;
    }

    private ExtTypedProperty[] createPropertiesWithCreator() {
        ExtTypedProperty[] properties = new ExtTypedProperty[3];
        SettableBeanProperty creatorProp = new SettableBeanProperty(null, "creatorProperty", String.class);
        SettableBeanProperty prop = new SettableBeanProperty(null, "property", Integer.class);
        properties[0] = new ExtTypedProperty(creatorProp, null);
        properties[1] = new ExtTypedProperty(prop, new SimpleTypeDeserializer(Integer.class));
        return properties;
    }

    private HashMap<String, Integer> createNameToPropertyIndex() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("property1", 0);
        map.put("property2", 1);
        map.put("property3", 2);
        return map;
    }

    private HashMap<String, Integer> createNameToPropertyIndexWithCreator() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("creatorProperty", 0);
        map.put("property", 1);
        return map;
    }

    private JsonParser createParser(String json) throws IOException {
        return createMapper().getFactory().createParser(json);
    }

    private DeserializationContext createContext() {
        return new TestDeserializationContext();
    }

    private static class TestDeserializationContext extends DeserializationContextBase {
        @Override
        public BeanDeserializerBase getActiveInitializer() {
            return null;
        }

        @Override
        public Object getInputValue() {
            return null;
        }

        @Override
        public Object getInputValueReference() {
            return null;
        }

        @Override
        public Object getRootValueInstance() {
            return null;
        }

        @Override
        public ObjectPointer getCurrentName() {
            return null;
        }

        @Override
        public ObjectPointer getCurrentPropertyId() {
            return null;
        }

        @Override
        public ObjectPointer getCurrentValue() {
            return null;
        }

        @Override
        public ObjectPointer findValueId(Object id) {
            return null;
        }

        @Override
        public Object getValueIfPresent(Object id) {
            return null;
        }
    }

    private static class SimpleTypeDeserializer extends TypeDeserializer {
        private final Class<Object> _defaultImpl;

        public SimpleTypeDeserializer(Class<Object> defaultImpl) {
            _defaultImpl = defaultImpl;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return ctxt.findValueHandler(getDefaultImplClass()).deserialize(p, ctxt);
        }

        @Override
        public String getPropertyName() {
            return "_default";
        }

        @Override
        public String getTypeId() {
            return null;
        }

        @Override
        public String getTypeIdString() {
            return null;
        }

        @Override
        public Class<?> getType(DeserializationContext ctxt) {
            return _defaultImpl;
        }

        @Override
        public JsonDeserializer<?> getContentDeserializer() {
            return null;
        }
    }
}