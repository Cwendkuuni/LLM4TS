
import static org.junit.Assert.*;
import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() throws Exception {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler();
        ExternalTypeHandler result = externalTypeHandler.start();
        assertNotNull(result);
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = null;
        Object bean = null;
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler();
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = null;
        Object bean = null;
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler();
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler();
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        assertNull(result);
    }

    @Test
    public void testComplete2() throws Exception {
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler();
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);
        assertNull(result);
    }

}

