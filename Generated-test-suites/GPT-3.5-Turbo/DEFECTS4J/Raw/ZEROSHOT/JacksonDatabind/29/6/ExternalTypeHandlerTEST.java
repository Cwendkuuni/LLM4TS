import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() {
        // create test objects
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[]{
                new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer())
        },
                new HashMap<String, Integer>(),
                new String[1], new TokenBuffer[1]);
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        String propName = "propName";
        Object bean = new Object();

        // perform the test
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);

        // assert the result
        assertTrue(result);
    }

    @Test
    public void testHandlePropertyValue() {
        // create test objects
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[]{
                new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer())
        },
                new HashMap<String, Integer>(),
                new String[1], new TokenBuffer[1]);
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        String propName = "propName";
        Object bean = new Object();

        // perform the test
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);

        // assert the result
        assertTrue(result);
    }

    @Test
    public void testComplete() {
        // create test objects
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[]{
                new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer())
        },
                new HashMap<String, Integer>(),
                new String[1], new TokenBuffer[1]);
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();

        // perform the test
        Object result = externalTypeHandler.complete(p, ctxt, bean);

        // assert the result
        assertNotNull(result);
    }

    @Test
    public void testCompleteWithPropertyBuffering() {
        // create test objects
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[]{
                new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer())
        },
                new HashMap<String, Integer>(),
                new String[1], new TokenBuffer[1]);
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator();
        Object bean = new Object();

        // perform the test
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);

        // assert the result
        assertNotNull(result);
    }

}