import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        ExternalTypeHandler newHandler = handler.start();
        assertNotNull(newHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        assertFalse(handler.handleTypePropertyValue(null, null, "propName", null));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        assertFalse(handler.handlePropertyValue(null, null, "propName", null));
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        Object result = handler.complete(null, null, null);
        assertNull(result);
    }

    @Test
    public void testComplete_PropertyValueBuffer() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        Object result = handler.complete(null, null, buffer, creator);
        assertNull(result);
    }

}