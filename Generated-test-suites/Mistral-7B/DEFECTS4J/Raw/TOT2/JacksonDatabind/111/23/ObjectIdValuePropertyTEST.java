import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(
                new ObjectIdReader("idReader"), new PropertyMetadata());
        JsonParser parser = new JsonFactory().createParser("{\"id\":1}");
        Object instance = new Object();
        objectIdValueProperty.deserializeAndSet(parser, new DeserializationContext(), instance);
        assertEquals(1, objectIdValueProperty.get(instance));
        parser.close();
    }
}