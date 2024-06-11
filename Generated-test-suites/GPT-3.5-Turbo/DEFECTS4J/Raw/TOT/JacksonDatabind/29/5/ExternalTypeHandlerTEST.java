import org.junit.Test;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        ExternalTypeHandler newHandler = externalTypeHandler.start();
        assertNotNull(newHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "type";
        Object bean = new Object();
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "value";
        Object bean = new Object();
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        assertEquals(bean, result);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);
        assertNotNull(result);
    }

    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "1";
        Object result = externalTypeHandler.deserialize(p, ctxt, index, typeId);
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        int index = 0;
        String typeId = "1";
        externalTypeHandler.deserializeAndSet(p, ctxt, bean, index, typeId);
    }

    @Test
    public void testBuilderAddExternal() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeserializer = null;
        builder.addExternal(property, typeDeserializer);
    }

}