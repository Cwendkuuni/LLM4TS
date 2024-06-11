import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;

public class ObjectIdValuePropertyTest {

    private ObjectMapper mapper = new ObjectMapper();
    private JsonFactory jsonFactory = new JsonFactory();

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        JsonParser parser = jsonFactory.createParser("null");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();

        assertNull(property.deserializeAndSet(parser, ctxt, instance));
    }

    @Test
    public void testDeserializeAndSet_NonNullValue() throws IOException {
        JsonParser parser = jsonFactory.createParser("\"test\"");
        DeserializationContext ctxt = mapper.getDeserializationContext();
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();

        assertEquals("test", property.deserializeAndSet(parser, ctxt, instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn_NoIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();

        property.setAndReturn(instance, "test");
    }

    @Test
    public void testSetAndReturn_HasIdProperty() throws IOException {
        ObjectIdValueProperty property = new ObjectIdValueProperty(null, null);
        Object instance = new Object();
        property.setObjectIdReader(new ObjectIdReader());

        assertEquals("test", property.setAndReturn(instance, "test"));
    }
}