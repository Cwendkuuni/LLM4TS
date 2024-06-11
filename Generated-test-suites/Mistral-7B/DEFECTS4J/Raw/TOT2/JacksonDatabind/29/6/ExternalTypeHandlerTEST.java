import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.StandardDeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.ExtTypedProperty;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExternalTypeHandlerTest {

    private ObjectNode rootNode;
    private JsonParser parser;
    private StandardDeserializationContext ctxt;

    @BeforeAll
    public void init() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        rootNode = mapper.createObjectNode();
        parser = mapper.getFactory().getParser(rootNode);
        ctxt = new StandardDeserializationContext();
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("typeProperty"),
                        SimpleTypeDeserializer.instance())
                .build());

        handler.start();
        handler.handleTypePropertyValue(parser, ctxt, "typeProperty", null);
        handler.complete(parser, ctxt, null);

        assertEquals("expectedType", handler.getBean(), rootNode.get("bean"));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("property"),
                        SimpleTypeDeserializer.instance())
                .build());

        handler.start();
        handler.handlePropertyValue(parser, ctxt, "property", null);
        handler.complete(parser, ctxt, null);

        assertEquals("expectedValue", handler.getBean(), rootNode.get("bean"));
    }

    @Test
    public void testHandleMissingTypeProperty() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("typeProperty"),
                        SimpleTypeDeserializer.instance())
                .build());

        handler.start();
        handler.handleTypePropertyValue(parser, ctxt, "missingProperty", null);
        handler.complete(parser, ctxt, null);

        assertThrows(MappingException.class, () -> handler.getBean());
    }

    @Test
    public void testHandleMissingProperty() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("property"),
                        SimpleTypeDeserializer.instance())
                .build());

        handler.start();
        handler.handlePropertyValue(parser, ctxt, "typeProperty", null);
        handler.complete(parser, ctxt, null);

        assertThrows(MappingException.class, () -> handler.getBean());
    }

    @Test
    public void testDeserializeNull() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new SimplePojo().getClass().getDeclaredField("property"),
                        SimpleTypeDeserializer.instance())
                .build());

        handler.start();
        handler.handlePropertyValue(parser, ctxt, "property", null);
        handler.complete(parser, ctxt, null);

        handler.handlePropertyValue(parser, ctxt, "property", null);
        handler.complete(parser, ctxt, null);

        assertEquals("expectedValue", handler.getBean(), rootNode.get("bean"));
    }

    @Test
    public void testDeserializeArray() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new ArrayPojo().getClass().getDeclaredField("property"),
                        ArrayTypeDeserializer.instance())
                .build());

        handler.start();
        handler.handlePropertyValue(parser, ctxt, "property", null);
        handler.complete(parser, ctxt, null);

        Object[] expected = {1, 2, 3};
        ObjectNode node = (ObjectNode) rootNode.get("bean");
        Object actual = node.get(0).get("value");
        assertArrayEquals(expected, (Object[]) actual);
    }

    @Test
    public void testDeserializeCreator() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(SimpleCreatorPojo.class.getDeclaredField("property"),
                        SimpleCreatorTypeDeserializer.instance())
                .build());

        handler.start();
        handler.handlePropertyValue(parser, ctxt, "property", null);
        handler.complete(parser, ctxt, null);

        assertEquals("expectedCreatorPojo", handler.getBean(), rootNode.get("bean"));
    }

    @Test
    public void testDeserializeCreatorWithMissingProperty() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(SimpleCreatorPojo.class.getDeclaredField("property"),
                        SimpleCreatorTypeDeserializer.instance())
                .build());

        ObjectNode node = (ObjectNode) rootNode.putPOJO("bean", new SimpleCreatorPojo());
        node.putNull("property");

        handler.start();
        handler.handlePropertyValue(parser, ctxt, "property", rootNode);
        handler.complete(parser, ctxt, null);

        assertThrows(MappingException.class, () -> handler.getBean());
    }

    @Test
    public void testDeserializeCreatorWithMissingType() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(SimpleCreatorPojo.class.getDeclaredField("property"),
                        SimpleCreatorTypeDeserializer.instance())
                .build());

        ObjectNode node = (ObjectNode) rootNode.putPOJO("bean", new SimpleCreatorPojo());
        node.put("property", ObjectMapper.getDefaultInstance().valueToTree(new int[] {1, 2}));

        handler.start();
        handler.handlePropertyValue(parser, ctxt, "property", rootNode);
        handler.complete(parser, ctxt, null);

        assertEquals("expectedCreatorPojo", handler.getBean(), rootNode.get("bean"));
    }

    private Object getBean() {
        return ((ExternalTypeHandler) parser.getCurrentValue()).getBean();
    }

    @AfterAll
    public void cleanup() throws IOException {
        parser.close();
        rootNode = null;
    }

    private static class SimplePojo {
        private String typeProperty;

        public String getTypeProperty() {
            return typeProperty;
        }

        public void setTypeProperty(String typeProperty) {
            this.typeProperty = typeProperty;
        }
    }

    private static class SimpleTypeDeserializer extends StdDeserializer<String> {
        public static final SimpleTypeDeserializer instance = new SimpleTypeDeserializer();

        public SimpleTypeDeserializer() {
            this(null);
        }

        public SimpleTypeDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getText();
        }

        @Override
        public PropertyDescriptor findPropertyDescriptor(ObjectBeans.PropertyName propertyName,
                ObjectBean base) throws IOException {
            return new StandardPropertyDescriptor("typeProperty", String.class, null);
        }
    }

    private static class ArrayPojo {
        private int[] property;

        public int[] getProperty() {
            return property;
        }

        public void setProperty(int[] property) {
            this.property = property;
        }
    }

    private static class ArrayTypeDeserializer extends StdDeserializer<int[]> {

        @Override
        public int[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken t = p.nextToken();
            if (t != JsonToken.START_ARRAY) {
                throw ctxt.mappingException("Expected START_ARRAY but found " + t);
            }
            JsonToken token = null;
            int[] result = new int[1];
            while ((token = p.nextToken()) != JsonToken.END_ARRAY) {
                if (token.isScalarValue()) {
                    result = Arrays.copyOf(result, result.length + 1);
                    result[result.length - 1] = Integer.parseInt(p.getText());
                } else {
                    throw ctxt.mappingException("Expected VALUE but found " + token);
                }
            }
            return Arrays.copyOf(result, result.length - 1);
        }

        @Override
        public PropertyDescriptor findPropertyDescriptor(ObjectBeans.PropertyName propertyName,
                ObjectBean base) throws IOException {
            return new StandardPropertyDescriptor("property", int[].class, null);
        }
    }

    private static class SimpleCreatorPojo {
        private int property;

        public int getProperty() {
            return property;
        }

        public void setProperty(int property) {
            this.property = property;
        }
    }

    private static class SimpleCreatorTypeDeserializer extends StdDeserializer<SimpleCreatorPojo> {

        @Override
        public SimpleCreatorPojo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken t = p.nextToken();
            if (t != JsonToken.START_OBJECT) {
                throw ctxt.mappingException("Expected START_OBJECT but found " + t);
            }
            ObjectNode node = (ObjectNode) p.readValueAsTree();
            SimpleCreatorPojo pojo = new SimpleCreatorPojo();
            pojo.setProperty(node.get("property").asInt());
            return pojo;
        }

        @Override
        public PropertyDescriptor findPropertyDescriptor(ObjectBeans.PropertyName propertyName,
                ObjectBean base) throws IOException {
            return new StandardPropertyDescriptor("property", SimpleCreatorPojo.class, null);
        }
    }
}