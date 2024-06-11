import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser parser;
    private DeserializationContext context;
    private Object bean;

    @Before
    public void setup() {
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        bean = new Object();

        ExtTypedProperty property1 = new ExtTypedProperty(new SettableBeanProperty("prop1", String.class), null);
        ExtTypedProperty property2 = new ExtTypedProperty(new SettableBeanProperty("prop2", Integer.class), new IntDeserializer());
        ExtTypedProperty property3 = new ExtTypedProperty(new SettableBeanProperty("prop3", List.class), new CollectionTypeDeserializer(context, ArrayList.class, Map.class));

        List<ExtTypedProperty> properties = Arrays.asList(property1, property2, property3);
        Map<String, Integer> nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("prop1", 0);
        nameToPropertyIndex.put("prop2", 1);
        nameToPropertyIndex.put("prop3", 2);

        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, null, null);
    }

    @Test
    public void testStart() throws IOException {
        ExternalTypeHandler startHandler = handler.start();
        Assert.assertNotNull(startHandler);
    }

    @Test(expected = IllegalStateException.class)
    public void testHandleTypePropertyValueWithNullBean() throws IOException {
        handler.handleTypePropertyValue(parser, context, "prop1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleTypePropertyValueWithInvalidPropName() throws IOException {
        when(parser.getText()).thenReturn("invalidPropName");
        handler.handleTypePropertyValue(parser, context, "prop1", bean);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        when(parser.getText()).thenReturn("StringTypeId");
        handler.handleTypePropertyValue(parser, context, "prop1", bean);
        // verify that the typeId has been stored
        Assert.assertEquals("StringTypeId", handler._typeIds[0]);
    }

    @Test
    public void testHandlePropertyValueWithNullBean() throws IOException {
        handler.handlePropertyValue(parser, context, "prop1", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandlePropertyValueWithInvalidPropName() throws IOException {
        handler.handlePropertyValue(parser, context, "invalidPropName", bean);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        when(parser.getText()).thenReturn("StringValue");
        handler.handlePropertyValue(parser, context, "prop1", bean);
        // verify that the value has been buffered
        Assert.assertNotNull(handler._tokens[0]);
    }

    @Test
    public void testCompleteWithNullTypeId() throws IOException {
        handler.complete(parser, context, bean);
        Assert.assertNull(handler._typeIds[0]);
        Assert.assertNull(handler._tokens[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompleteWithNullBean() throws IOException {
        handler.complete(parser, context, null);
    }

    @Test
    public void testComplete() throws IOException {
        when(parser.getText()).thenReturn("StringTypeId");
        handler.handleTypePropertyValue(parser, context, "prop1", bean);
        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.complete(parser, context, bean);
        // verify that the typeId has been deserialized
        String typeId = handler.getProperty(bean, "prop1").getType().getTypeName();
        Assert.assertEquals("StringTypeId", typeId);
    }

    @Test
    public void testCompleteWithCreatorProperties() throws IOException {
        when(parser.getText()).thenReturn("StringTypeId");
        handler.handleTypePropertyValue(parser, context, "prop1", bean);
        handler.handlePropertyValue(parser, context, "prop1", bean);
        handler.complete(parser, context, new PropertyValueBuffer(), new SimpleCreator());
        // verify that the typeId has been passed to the creator
        Assert.assertEquals("StringTypeId", ((SimpleCreator) bean).getTypeId());
    }

    private static class SimpleCreator extends BaseJsonDeserializer<Object> {
        private String typeId;

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            this.typeId = p.getText();
            return null;
        }

        public String getTypeId() { return typeId; }
    }

    private static class ExtTypedProperty {
        private final SettableBeanProperty property;
        private final TypeDeserializer typeDeserializer;
        private String typePropertyName;
        private SettableBeanProperty typeProperty;

        public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeserializer) {
            this.property = property;
            this.typeDeserializer = typeDeserializer;
            this.typePropertyName = typeDeserializer.getPropertyName();
        }

        public void linkTypeProperty(SettableBeanProperty typeProperty) {
            this.typeProperty = typeProperty;
        }

        public boolean hasTypePropertyName(String n) {
            return n.equals(typePropertyName);
        }

        public boolean hasDefaultType() {
            return typeDeserializer.getDefaultImpl() != null;
        }

        public String getDefaultTypeId() {
            Class<?> defaultType = typeDeserializer.getDefaultImpl();
            if (defaultType == null) {
                return null;
            }
            return typeDeserializer.getTypeIdResolver().idFromValueAndType(null, defaultType);
        }

        public String getTypePropertyName() { return typePropertyName; }

        public SettableBeanProperty getProperty() {
            return property;
        }

        public SettableBeanProperty getTypeProperty() {
            return typeProperty;
        }
    }

    private static class ExtTypedPropertyBuilder {
        private final List<ExtTypedProperty> properties;

        public ExtTypedPropertyBuilder() {
            this.properties = new ArrayList<>();
        }

        public void addExternal(SettableBeanProperty property, TypeDeserializer typeDeserializer) {
            properties.add(new ExtTypedProperty(property, typeDeserializer));
        }

        public ExternalTypeHandler build() {
            return new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), null, null, null);
        }
    }
}