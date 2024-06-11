import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler externalTypeHandler;

    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper();
        SettableBeanProperty property = objectMapper
                .reader()
                .forType(Integer.class)
                .readValue("property", DeserializationContext.class);
        TypeDeserializer typeDeserializer = objectMapper
                .reader()
                .forType(Integer.class)
                .readValue("typeDeserializer", DeserializationContext.class);
        SettableBeanProperty[] properties = new SettableBeanProperty[]{property};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<String, Integer>();
        nameToPropertyIndex.put("property", 0);
        String[] typeIds = new String[]{"typeId"};
        TokenBuffer[] tokens = new TokenBuffer[]{new TokenBuffer(new JsonFactory())};

        externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testHandleTypePropertyValue() throws JsonParseException, JsonMappingException, IOException {
        JsonParser jp = new JsonFactory().createJsonParser("\"typeId\"");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        String propName = "property";
        Object bean = null;

        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);

        assertTrue(result);
    }

    @Test
    public void testHandlePropertyValue() throws JsonParseException, JsonMappingException, IOException {
        JsonParser jp = new JsonFactory().createJsonParser("\"typeId\"");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        String propName = "property";
        Object bean = null;

        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);

        assertTrue(result);
    }

    @Test
    public void testComplete() throws JsonParseException, JsonMappingException, IOException {
        JsonParser p = new JsonFactory().createJsonParser("\"typeId\"");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object bean = null;

        Object result = externalTypeHandler.complete(p, ctxt, bean);

        assertNull(result);
    }

    @Test
    public void testCompleteWithBuffer() throws JsonParseException, JsonMappingException, IOException {
        JsonParser jp = new ObjectMapper().reader().createJsonParser("\"typeId\"");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;

        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);

        assertNull(result);
    }

    @Test
    public void testDeserialize() throws JsonParseException, JsonMappingException, IOException {
        JsonParser p = new JsonFactory().createJsonParser("\"typeId\"");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        int index = 0;
        String typeId = "typeId";

        Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);

        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws JsonParseException, JsonMappingException, IOException {
        JsonParser p = new JsonFactory().createJsonParser("\"typeId\"");
        DeserializationContext ctxt = new ObjectMapper().getDeserializationContext();
        Object bean = null;
        int index = 0;
        String typeId = "typeId";

        externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);

        // Add assertion here
    }

}