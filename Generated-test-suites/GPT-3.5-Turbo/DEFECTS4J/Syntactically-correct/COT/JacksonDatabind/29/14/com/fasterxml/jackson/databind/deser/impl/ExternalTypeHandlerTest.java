import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler.ExtTypedProperty[] properties = {};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = {};
        TokenBuffer[] tokens = {};

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);

        Assert.assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler.ExtTypedProperty[] properties = {};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = {};
        TokenBuffer[] tokens = {};

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);

        Assert.assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler.ExtTypedProperty[] properties = {};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = {};
        TokenBuffer[] tokens = {};

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = externalTypeHandler.complete(p, ctxt, bean);

        Assert.assertNull(result);
    }

    @Test
    public void testComplete2() throws IOException {
        ExternalTypeHandler.ExtTypedProperty[] properties = {};
        HashMap<String, Integer> nameToPropertyIndex = new HashMap<>();
        String[] typeIds = {};
        TokenBuffer[] tokens = {};

        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);

        JsonParser jp = null;
        DeserializationContext ctxt = null;
        ExternalTypeHandler.PropertyValueBuffer buffer = null;
        ExternalTypeHandler.PropertyBasedCreator creator = null;
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);

        Assert.assertNull(result);
    }

    @Test
    public void testBuild() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();

        ExternalTypeHandler result = builder.build();

        Assert.assertNotNull(result);
    }

}