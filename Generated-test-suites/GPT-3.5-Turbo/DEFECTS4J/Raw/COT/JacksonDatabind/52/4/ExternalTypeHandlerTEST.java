import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();

        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(
                new SettableBeanProperty(null, null, null, null, null),
                new TypeDeserializer(null, null, null, null, null)
        );
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(
                new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty},
                new HashMap<String, Integer>(),
                null,
                new TokenBuffer[] {}
        );

        boolean result = externalTypeHandler.handleTypePropertyValue(jsonParser, deserializationContext, "propName", null);

        Assert.assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();

        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(
                new SettableBeanProperty(null, null, null, null, null),
                new TypeDeserializer(null, null, null, null, null)
        );
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(
                new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty},
                new HashMap<String, Integer>(),
                null,
                new TokenBuffer[] {}
        );

        boolean result = externalTypeHandler.handlePropertyValue(jsonParser, deserializationContext, "propName", null);

        Assert.assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();

        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(
                new SettableBeanProperty(null, null, null, null, null),
                new TypeDeserializer(null, null, null, null, null)
        );
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(
                new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty},
                new HashMap<String, Integer>(),
                null,
                new TokenBuffer[] {}
        );

        Object result = externalTypeHandler.complete(jsonParser, deserializationContext, null);

        Assert.assertNull(result);
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = objectMapper.getFactory();
        JsonParser jsonParser = jsonFactory.createParser("");
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();

        ExternalTypeHandler.Builder externalTypeHandlerBuilder = new ExternalTypeHandler.Builder();
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(
                new ExternalTypeHandler.ExtTypedProperty[] {},
                new HashMap<String, Integer>(),
                null,
                new TokenBuffer[] {}
        );

        Object result = externalTypeHandler.complete(jsonParser, deserializationContext, null, null);

        Assert.assertNull(result);
    }
}