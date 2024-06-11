import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;

    @Before
    public void setUp() throws IOException {
        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(new ExtTypedProperty(new TestProperty(), new TestTypeDeserializer()));

        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), null, null);
        objectMapper = new ObjectMapper();
        jsonParser = objectMapper.getFactory().createParser("{}");
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String propName = "testProp";
        String typeId = "TestType";
        handler.handleTypePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName, null);
        Assert.assertEquals(typeId, handler._typeIds[0]);
    }

    @Test(expected = IOException.class)
    public void testHandleTypePropertyValueWithNullBean() throws IOException {
        String propName = "testProp";
        String typeId = "TestType";
        handler.handleTypePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName, null);
        handler.handlePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName, null);
        jsonParser.nextToken(); // consume START_OBJECT token
        handler.complete(jsonParser, new DeserializationContext(objectMapper), null);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        String propName = "testProp";
        String typeId = "TestType";
        handler.handlePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName, null);
        Assert.assertEquals(typeId, handler._typeIds[0]);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueWithNullBeanAndNoTokens() throws IOException {
        String propName = "testProp";
        handler.handlePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName, null);
        jsonParser.nextToken(); // consume START_OBJECT token
        handler.complete(jsonParser, new DeserializationContext(objectMapper), null);
    }

    @Test
    public void testComplete() throws IOException {
        String propName1 = "testProp1";
        String typeId1 = "TestType1";
        String propName2 = "testProp2";
        String typeId2 = "TestType2";
        handler.handleTypePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName1, null);
        handler.handlePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName1, null);
        handler.handleTypePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName2, null);
        handler.handlePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName2, null);
        jsonParser.nextToken(); // consume START_OBJECT token

        Object result = handler.complete(jsonParser, new DeserializationContext(objectMapper), null);
        Assert.assertNotNull(result);

        Assert.assertEquals(typeId1, handler._typeIds[0]);
        Assert.assertEquals(typeId2, handler._typeIds[1]);
    }

    @Test
    public void testCompleteWithPropertyBasedCreator() throws IOException {
        String propName1 = "testProp1";
        String typeId1 = "TestType1";
        String propName2 = "testProp2";
        String typeId2 = "TestType2";

        handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new TestProperty1(), new TestTypeDeserializer1())
                .addExternal(new TestProperty2(), new TestTypeDeserializer2())
                .build());

        handler.handleTypePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName1, null);
        handler.handlePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName1, null);
        handler.handleTypePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName2, null);
        handler.handlePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName2, null);
        jsonParser.nextToken(); // consume START_OBJECT token

        PropertyValueBuffer buffer = new PropertyValueBuffer();
        Object result = handler.complete(jsonParser, new DeserializationContext(objectMapper), buffer, new TestCreator());
        Assert.assertNotNull(result);

        Assert.assertEquals(typeId1, handler._typeIds[0]);
        Assert.assertEquals(typeId2, handler._typeIds[1]);

        Assert.assertEquals(typeId1, buffer.getParameterValue(0));
        Assert.assertEquals(typeId2, buffer.getParameterValue(1));
    }

    @Test(expected = JsonProcessingException.class)
    public void testCompleteMissingProperty() throws IOException {
        String propName = "testProp";
        String typeId = "TestType";

        handler.handleTypePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName, null);
        jsonParser.nextToken(); // consume START_OBJECT token

        handler.complete(jsonParser, new DeserializationContext(objectMapper), null);

        Assert.assertEquals(typeId, handler._typeIds[0]);

        // should throw JsonProcessingException as property is missing
        handler.complete(jsonParser, new DeserializationContext(objectMapper), null);
    }

    @Test(expected = JsonProcessingException.class)
    public void testCompleteMissingTypeId() throws IOException {
        String propName = "testProp";
        String typeId = "TestType";

        handler.handlePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName, null);
        jsonParser.nextToken(); // consume START_OBJECT token

        handler.complete(jsonParser, new DeserializationContext(objectMapper), null);

        // should throw JsonProcessingException as typeId is missing
        handler.complete(jsonParser, new DeserializationContext(objectMapper), null);
    }

    @Test
    public void testCompleteWithNullTypeIdAndNullToken() throws IOException {
        String propName = "testProp";

        handler.handlePropertyValue(jsonParser, new DeserializationContext(objectMapper), propName, null);
        jsonParser.nextToken(); // consume START_OBJECT token

        handler.complete(jsonParser, new DeserializationContext(objectMapper), null, null);

        Assert.assertNull(handler._typeIds[0]);
        Assert.assertNull(_tokens[0]);
    }

    static class TestProperty {
        // empty class
    }

    static class TestProperty1 {
        private String prop1;

        public String getProp1() {
            return prop1;
        }

        public void setProp1(String prop1) {
            this.prop1 = prop1;
        }
    }

    static class TestProperty2 {
        private String prop2;

        public String getProp2() {
            return prop2;
        }

        public void setProp2(String prop2) {
            this.prop2 = prop2;
        }
    }

    static class TestTypeDeserializer implements TypeDeserializer {
        private static final String PROPERTY_NAME = "testType";

        @Override
        public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            return jp.getValueAsString();
        }

        @Override
        public String getPropertyName() {
            return PROPERTY_NAME;
        }

        @Override
        public void serializeWithType(SerializerProvider sp, Object value, TypeSerializer ts) throws IOException {
            // empty method
        }

        @Override
        public void deserializeValueIfPossible(JsonParser jp, DeserializationContext ctxt, Object bean) throws IOException {
            // empty method
        }
    }

    static class TestTypeDeserializer1 extends TestTypeDeserializer {
        // empty class
    }

    static class TestTypeDeserializer2 extends TestTypeDeserializer {
        // empty class
    }

    static class TestCreator implements PropertyBasedCreator {
        @Override
        public Object createInstance(DeserializationContext ctxt) throws IOException {
            return new Object();
        }

        @Override
        public PropertyMapper getPropertyMapper(DeserializationContext ctxt) {
            return null;
        }

        @Override
        public void configure(DeserializationContext ctxt) throws IOException {
            // empty method
        }

        @Override
        public void initialize(DeserializationContext ctxt, BeanDescription beanDesc) throws IOException {
            // empty method
        }

        @Override
        public void initValueInstantiator(DeserializationContext ctxt, BeanDescription beanDesc) throws IOException {
            // empty method
        }

        @Override
        public void postProcessValueId(DeserializationContext ctxt, BeanProperty property) throws IOException {
            // empty method
        }

        @Override
        public void postProcessValue(DeserializationContext ctxt, PropertyValueBuffer valueIn) throws IOException {
            // empty method
        }

        @Override
        public void postProcessPropertyId(DeserializationContext ctxt, BeanProperty property) throws IOException {
            // empty method
        }

        @Override
        public void postProcessPropertyValue(DeserializationContext ctxt, PropertyValueBuffer valueIn) throws IOException {
            // empty method
        }

        @Override
        public void setContentHandler(DefaultContentHandler handler) {
            // empty method
        }

        @Override
        public void setAnnotationIntrospector(AnnotationIntrospector introspector) {
            // empty method
        }

        @Override
        public void setSerializerProvider(SerializerProvider provider) {
            // empty method
        }

        @Override
        public void setBeanDescription(BeanDescription beanDesc) {
            // empty method
        }

        @Override
        public void setActiveContext(ActiveContext ac) {
            // empty method
        }

        @Override
        public void setDefaultContextualHandler(DefaultContextualHandler handler) {
            // empty method
        }

        @Override
        public void setTypeResolver(TypeResolverBuilder typeResolverBuilder) {
            // empty method
        }

        @Override
        public void setValueFactory(ValueFactory valueFactory) {
            // empty method
        }
    }
}