import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

public class ObjectIdValuePropertyTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testDeserializeAndSet() throws IOException {
        SettableBeanProperty property = new ObjectIdValueProperty(
                new ObjectIdReader(new PropertyName("test"), HashMap.class, null),
                null);
        Object instance = new HashMap<>();

        JsonParser parser = objectMapper.getFactory().createParser("null");

        property.deserializeAndSet(parser, DeserializationContext.emptyContext(), instance);

        Assert.assertEquals(null, instance.get("test"));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SettableBeanProperty property = new ObjectIdValueProperty(
                new ObjectIdReader(new PropertyName("test"), HashMap.class, null),
                null);
        Object instance = new HashMap<>();

        JsonParser parser = objectMapper.getFactory().createParser("\"value\"");

        Object result = property.deserializeSetAndReturn(parser, DeserializationContext.emptyContext(), instance);

        Assert.assertEquals("value", instance.get("test"));
        Assert.assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        SettableBeanProperty property = new ObjectIdValueProperty(
                new ObjectIdReader(new PropertyName("test"), HashMap.class, null),
                null);
        Object instance = new HashMap<>();

        JsonParser parser = objectMapper.getFactory().createParser("\"value\"");

        try {
            property.set(instance, "newValue");
            Assert.fail("UnsupportedOperationException should be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testSetAndGetMember() throws IOException {
        SettableBeanProperty property = new ObjectIdValueProperty(
                new ObjectIdReader(new PropertyName("test"), HashMap.class, null),
                null);

        Assert.assertNull(property.getMember());
    }
}