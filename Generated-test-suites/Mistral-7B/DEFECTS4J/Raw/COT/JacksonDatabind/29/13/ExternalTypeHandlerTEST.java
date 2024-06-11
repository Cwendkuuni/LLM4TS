import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        handler.start();

        JsonParser jp = createTestParser();
        DeserializationContext ctxt = createTestContext();

        Object bean = new Object();

        handler.handleTypePropertyValue(jp, ctxt, "propName", bean);

        Assert.assertNotNull(handler._typeIds);
        Assert.assertEquals("propValue", handler._typeIds[0]);

        jp.nextToken(); // consume next token

        handler.complete(jp, ctxt, bean);

        Assert.assertEquals("propValue", handler.getPropertyValue(bean, 0));
    }

    @Test
    public void testHandlePropertyValueWithDeserializableValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        handler.start();

        JsonParser jp = createTestParser();
        DeserializationContext ctxt = createTestContext();

        Object bean = new TestBean();

        handler.handlePropertyValue(jp, ctxt, "propName", bean);

        jp.nextToken(); // consume next token

        handler.complete(jp, ctxt, bean);

        assertEquals("TestValue", handler.getPropertyValue(bean, 0));
    }

    @Test
    public void testHandlePropertyValueWithoutDeserializableValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        handler.start();

        JsonParser jp = createTestParser();
        DeserializationContext ctxt = createTestContext();

        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "propName", bean);

        jp.nextToken(); // consume next token

        handler.complete(jp, ctxt, bean);

        assertEquals(null, handler.getPropertyValue(bean, 0));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingPropertyForExternalTypeId() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        handler.start();

        JsonParser jp = createTestParser();
        DeserializationContext ctxt = createTestContext();

        Object bean = new Object();

        handler.handleTypePropertyValue(jp, ctxt, "nonExistentPropName", bean);

        handler.complete(jp, ctxt, bean);

        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingExternalTypeId() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        handler.start();

        JsonParser jp = createTestParser();
        DeserializationContext ctxt = createTestContext();

        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "propName", bean);

        handler.complete(jp, ctxt, bean);

        fail("Expected JsonMappingException");
    }

    @Test
    public void testCompleteWithNullValues() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        handler.start();

        JsonParser jp = createTestParser();
        DeserializationContext ctxt = createTestContext();

        Object bean = new Object();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
        handler.handlePropertyValue(jp, ctxt, "prop2", bean);

        handler.complete(jp, ctxt, bean);

        assertEquals(null, handler.getPropertyValue(bean, 0));
        assertEquals(null, handler.getPropertyValue(bean, 1));
    }

    @Test
    public void testCompleteWithValidValues() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        handler.start();

        JsonParser jp = createTestParser();
        DeserializationContext ctxt = createTestContext();

        Object bean = new Object();

        handler.handleTypePropertyValue(jp, ctxt, "prop1", bean);
        handler.handlePropertyValue(jp, ctxt, "prop2", bean);

        handler.complete(jp, ctxt, bean);

        assertEquals("value1", handler.getPropertyValue(bean, 0));
        assertEquals("value2", handler.getPropertyValue(bean, 1));
    }

    @Test
    public void testCompleteWithCreatorProperties() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(createPropertiesWithCreator(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        handler.start();

        JsonParser jp = createTestParser();
        DeserializationContext ctxt = createTestContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new TestCreator();

        handler.handlePropertyValue(jp, ctxt, "prop1", null);
        handler.handlePropertyValue(jp, ctxt, "prop2", null);

        handler.complete(jp, ctxt, buffer, creator);

        Object bean = creator.build(ctxt, buffer);

        assertEquals("value1", handler.getPropertyValue(bean, 0));
        assertEquals("value2", handler.getPropertyValue(bean, 1));
    }

    private JsonParser createTestParser() throws IOException {
        String testJson = "[{\"propName\":\"propValue1\", \"typeId\":\"Type1\"}, {\"propName\":\"propValue2\", \"typeId\":\"Type2\"}]";
        ObjectCodec objectMapper = new ObjectMapper();
        return objectMapper.getFactory().createParser(testJson);
    }

    private DeserializationContext createTestContext() {
        ObjectCodec objectMapper = new ObjectMapper();
        return objectMapper.getDeserializationContext();
    }

    private ExtTypedProperty[] createProperties() {
        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(new ExtTypedProperty(new TestProperty("prop1", TestType1.class), new TestTypeDeserializer1()));
        properties.add(new ExtTypedProperty(new TestProperty("prop2", TestType2.class), new TestTypeDeserializer2()));
        return properties.toArray(new ExtTypedProperty[properties.size()]);
    }

    private ExtTypedProperty[] createPropertiesWithCreator() {
        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(new ExtTypedProperty(new TestProperty("prop1", TestType1.class), new TestTypeDeserializer1()));
        properties.add(new ExtTypedProperty(new TestProperty("prop2", TestType2.class), new TestTypeDeserializer2()));
        properties.add(new ExtTypedProperty(new TestCreatorProperty("creatorProp"), TestCreator.class));
        return properties.toArray(new ExtTypedProperty[properties.size()]);
    }

    private Map<String, Integer> createNameToPropertyIndex() {
        Map<String, Integer> map = new HashMap<>();
        map.put("prop1", 0);
        map.put("prop2", 1);
        map.put("typeId", 2);
        return map;
    }

    private static class TestBean {
        Object prop1;
        Object prop2;

        Object getPropertyValue(int index) {
            return this.getClass().getDeclaredField("prop" + (index + 1)).get(this);
        }
    }

    private static class TestProperty {
        String name;
        Class<?> type;

        TestProperty(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }
    }

    private static class TestType1 {
        String value;

        String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Type1";
        }
    }

    private static class TestType2 {
        String value;

        String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Type2";
        }
    }

    private static class TestTypeDeserializer1 extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new TestType1();
        }
    }

    private static class TestTypeDeserializer2 extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new TestType2();
        }
    }

    private static class TestCreator {
        Object creatorProp;

        Object build(DeserializationContext ctxt, PropertyValueBuffer buffer) throws IOException {
            this.creatorProp = buffer.getParameter("creatorProp");
            return this;
        }
    }

    private static class TestCreatorProperty {
        String name;

        TestCreatorProperty(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}