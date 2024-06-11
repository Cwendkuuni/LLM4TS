import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "propName";
        Object bean = new Object();
        boolean result = handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "propName";
        Object bean = new Object();
        boolean result = handler.handlePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        Object result = handler.complete(p, ctxt, bean);
        assertEquals(bean, result);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = handler.complete(jp, ctxt, buffer, creator);
        assertNull(result);
    }

    @Test
    public void test_deserialize() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "typeId";
        try {
            handler._deserialize(p, ctxt, index, typeId);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void test_deserializeAndSet() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        int index = 0;
        String typeId = "typeId";
        try {
            handler._deserializeAndSet(p, ctxt, bean, index, typeId);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler handler = builder.build();
        assertNotNull(handler);
    }
}