
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler.ExtTypedProperty[] properties;
    private HashMap<String, Integer> nameToPropertyIndex;
    private String[] typeIds;
    private TokenBuffer[] tokens;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        properties = new ExternalTypeHandler.ExtTypedProperty[2];
        properties[0] = new ExternalTypeHandler.ExtTypedProperty(null, null);
        properties[1] = new ExternalTypeHandler.ExtTypedProperty(null, null);
        nameToPropertyIndex = new HashMap<String, Integer>();
        nameToPropertyIndex.put("prop1", 0);
        nameToPropertyIndex.put("prop2", 1);
        typeIds = new String[2];
        tokens = new TokenBuffer[2];
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testStart() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        ExternalTypeHandler newHandler = externalTypeHandler.start();

        assertNotNull(newHandler);
        assertNotSame(externalTypeHandler, newHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{}");
        DeserializationContext context = objectMapper.getDeserializationContext();

        boolean handled = externalTypeHandler.handleTypePropertyValue(parser, context, "prop1", null);

        assertFalse(handled);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{}");
        DeserializationContext context = objectMapper.getDeserializationContext();

        boolean handled = externalTypeHandler.handlePropertyValue(parser, context, "prop1", null);

        assertFalse(handled);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{}");
        DeserializationContext context = objectMapper.getDeserializationContext();

        Object result = externalTypeHandler.complete(parser, context, null);

        assertNull(result);
    }

    @Test
    public void testCompleteWithPropertyBuffer() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createParser("{}");
        DeserializationContext context = objectMapper.getDeserializationContext();
        ExternalTypeHandler.PropertyValueBuffer buffer = new ExternalTypeHandler.PropertyValueBuffer(objectMapper, context, properties.length);
        ExternalTypeHandler.PropertyBasedCreator creator = new ExternalTypeHandler.PropertyBasedCreator();

        externalTypeHandler.complete(parser, context, buffer, creator);

        assertNotNull(buffer);
        assertNotNull(creator);
    }
}
