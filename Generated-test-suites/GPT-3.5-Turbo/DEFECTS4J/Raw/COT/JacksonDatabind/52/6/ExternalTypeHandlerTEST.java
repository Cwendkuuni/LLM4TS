import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        ExternalTypeHandler result = handler.start();

        assertNotNull(result);
        assertNotSame(handler, result);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        String propName = "type";
        Object bean = new Object();

        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, bean);

        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        String propName = "name";
        Object bean = new Object();

        boolean result = handler.handlePropertyValue(p, ctxt, propName, bean);

        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object bean = new Object();

        Object result = handler.complete(p, ctxt, bean);

        assertNotNull(result);
        assertSame(bean, result);
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer(ctxt);
        PropertyBasedCreator creator = null;

        Object result = handler.complete(p, ctxt, buffer, creator);

        assertNotNull(result);
    }

    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        int index = 0;
        String typeId = "type";

        Object result = handler._deserialize(p, ctxt, index, typeId);

        assertNotNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("{}");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object bean = new Object();
        int index = 0;
        String typeId = "type";

        handler._deserializeAndSet(p, ctxt, bean, index, typeId);

        // assertion
    }

    @Test
    public void testBuilderAddExternal() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;

        builder.addExternal(property, typeDeser);
    }
}