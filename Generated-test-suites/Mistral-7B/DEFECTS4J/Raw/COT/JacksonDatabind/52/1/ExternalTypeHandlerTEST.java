import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser parser;
    private DeserializationContext ctxt;

    @Before
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        parser = mapper.getFactory().createParser("{}");
        ctxt = mapper.getDeserializationContext();
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(null, null)},
                new HashMap<>(), null, null);
        handler.start();
        String typeId = "StringType";
        String propName = "typeProperty";

        boolean result = handler.handleTypePropertyValue(parser, ctxt, propName, null);
        Assert.assertTrue(result);
        Assert.assertEquals(typeId, handler._typeIds[0]);
    }

    @Test(expected = IOException.class)
    public void testHandleTypePropertyValue_InvalidPropertyName() throws Exception {
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(null, null)},
                new HashMap<>(), null, null);
        handler.start();
        String typeId = "StringType";
        String propName = "invalidPropertyName";

        handler.handleTypePropertyValue(parser, ctxt, propName, null);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(null, null)},
                new HashMap<>(), null, null);
        handler.start();
        String propName = "property";
        Object bean = new Object();

        String result = handler.handlePropertyValue(parser, ctxt, propName, bean).get(0).toString();
        Assert.assertEquals(typeId, result);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValue_MissingProperty() throws Exception {
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(null, null)},
                new HashMap<>(), null, null);
        handler.start();
        String propName = "missingProperty";
        Object bean = new Object();

        handler.handlePropertyValue(parser, ctxt, propName, bean);
    }

    @Test
    public void testComplete() throws Exception {
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(null, null)},
                new HashMap<>(), null, null);
        handler.start();
        String typeId1 = "StringType1";
        String typeId2 = "StringType2";
        String propName1 = "property1";
        String propName2 = "property2";

        handler.handleTypePropertyValue(parser, ctxt, propName1, null);
        handler.handlePropertyValue(parser, ctxt, propName1, new Object());
        handler.handlePropertyValue(parser, ctxt, propName2, new Object());

        Object result = handler.complete(parser, ctxt, null);
        Assert.assertNotNull(result);

        Object bean = new Object();
        handler.handleTypePropertyValue(parser, ctxt, propName1, bean);
        handler.handlePropertyValue(parser, ctxt, propName2, bean);
        handler.handlePropertyValue(parser, ctxt, propName1, null);
        handler.handlePropertyValue(parser, ctxt, propName2, null);

        result = handler.complete(parser, ctxt, bean);
        Assert.assertNotNull(result);
    }

    @Test(expected = IOException.class)
    public void testComplete_MissingTypeId() throws Exception {
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(null, null)},
                new HashMap<>(), null, null);
        handler.start();
        String propName1 = "property1";
        String propName2 = "property2";

        handler.handleTypePropertyValue(parser, ctxt, propName1, null);
        handler.handlePropertyValue(parser, ctxt, propName1, new Object());
        handler.handlePropertyValue(parser, ctxt, propName2, new Object());

        handler.complete(parser, ctxt, null);
    }

    @Test(expected = IOException.class)
    public void testComplete_MissingProperty() throws Exception {
        handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(null, null)},
                new HashMap<>(), null, null);
        handler.start();
        String typeId = "StringType";
        String propName1 = "property1";
        String propName2 = "property2";

        handler.handleTypePropertyValue(parser, ctxt, propName1, null);
        handler._typeIds[0] = typeId;
        handler.handlePropertyValue(parser, ctxt, propName2, new Object());

        handler.complete(parser, ctxt, null);
    }

    @Test
    public void testComplete_WithCreatorProperties() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"creatorProperty\":[1,\"StringType1\"],\"property\":\"StringType2\"}");
        JsonParser parser = mapper.getFactory().getParser(node.traverse());

        handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new SettableBeanProperty(new SimpleBean(), "creatorProperty"), null),
                new ExtTypedProperty(new SettableBeanProperty(new SimpleBean(), "property"), null)
        }, new HashMap<>(), null, null);
        handler.start();

        Object result = handler.complete(parser, ctxt, null);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof SimpleBean);

        Object bean = (SimpleBean) result;
        Assert.assertEquals(1, bean.creatorProperty);
        Assert.assertEquals("StringType1", bean.typeId);
        Assert.assertEquals("StringType2", bean.property);
    }

    @Test(expected = IOException.class)
    public void testComplete_WithCreatorProperties_MissingTypeId() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"creatorProperty\":[1],\"property\":\"StringType\"}");
        JsonParser parser = mapper.getFactory().getParser(node.traverse());

        handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new SettableBeanProperty(new SimpleBean(), "creatorProperty"), null),
                new ExtTypedProperty(new SettableBeanProperty(new SimpleBean(), "property"), null)
        }, new HashMap<>(), null, null);
        handler.start();

        handler.complete(parser, ctxt, null);
    }

    @Test(expected = IOException.class)
    public void testComplete_WithCreatorProperties_MissingProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree("{\"property\":\"StringType\"}");
        JsonParser parser = mapper.getFactory().getParser(node.traverse());

        handler = new ExternalTypeHandler(new ExtTypedProperty[]{
                new ExtTypedProperty(new SettableBeanProperty(new SimpleBean(), "creatorProperty"), null),
                new ExtTypedProperty(new SettableBeanProperty(new SimpleBean(), "property"), null)
        }, new HashMap<>(), null, null);
        handler.start();

        handler.handleTypePropertyValue(parser, ctxt, "creatorProperty", null);
        handler.handlePropertyValue(parser, ctxt, "property", new Object());

        handler.complete(parser, ctxt, null);
    }
}

class SimpleBean {
    int creatorProperty;
    @JsonTypeInfo(use = As.EXTERNAL_PROPERTY, property = "typeId")
    String property;
}