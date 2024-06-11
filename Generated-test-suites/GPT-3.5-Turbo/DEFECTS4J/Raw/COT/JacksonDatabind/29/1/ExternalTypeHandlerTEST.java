
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.extTypedProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        builder.addExternal(property, typeDeserializer);
        ExternalTypeHandler handler = builder.build();

        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JsonParser jp = new JsonParser();
        jp.setText("typeId");

        try {
            boolean result = handler.handleTypePropertyValue(jp, ctxt, "typePropertyName", new Object());
            Assert.assertTrue(result);
        } catch (JsonParseException e) {
            Assert.fail("JsonParseException should not be thrown");
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }

    @Test
    public void testHandlePropertyValue() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        builder.addExternal(property, typeDeserializer);
        ExternalTypeHandler handler = builder.build();

        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JsonParser jp = new JsonParser();
        TokenBuffer tokens = new TokenBuffer(jp);
        tokens.writeString("value");
        handler.handlePropertyValue(jp, ctxt, "propertyName", new Object());
    }

    @Test
    public void testComplete() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        builder.addExternal(property, typeDeserializer);
        ExternalTypeHandler handler = builder.build();

        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JsonParser jp = new JsonParser();
        Object bean = new Object();

        try {
            handler.complete(jp, ctxt, bean);
        } catch (JsonParseException e) {
            Assert.fail("JsonParseException should not be thrown");
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }

    @Test
    public void testCompleteWithBuffer() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        builder.addExternal(property, typeDeserializer);
        ExternalTypeHandler handler = builder.build();

        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JsonParser jp = new JsonParser();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator();

        try {
            handler.complete(jp, ctxt, buffer, creator);
        } catch (JsonParseException e) {
            Assert.fail("JsonParseException should not be thrown");
        } catch (IOException e) {
            Assert.fail("IOException should not be thrown");
        }
    }
}
