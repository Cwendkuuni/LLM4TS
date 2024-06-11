import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        ExternalTypeHandler result = externalTypeHandler.start();
        assertNotNull(result);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        assertFalse(externalTypeHandler.handleTypePropertyValue(jsonParser, deserializationContext, "propName", null));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        assertFalse(externalTypeHandler.handlePropertyValue(jsonParser, deserializationContext, "propName", null));
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        assertNull(externalTypeHandler.complete(jsonParser, deserializationContext, null));
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        PropertyValueBuffer buffer = Mockito.mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = Mockito.mock(PropertyBasedCreator.class);
        assertNull(externalTypeHandler.complete(jsonParser, deserializationContext, buffer, creator));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        TokenBuffer tokenBuffer = Mockito.mock(TokenBuffer.class);
        externalTypeHandler.deserializeAndSet(jsonParser, deserializationContext, "bean", 0, "typeId");
    }

    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        assertNull(externalTypeHandler.deserialize(jsonParser, deserializationContext, 0, "typeId"));
    }

}