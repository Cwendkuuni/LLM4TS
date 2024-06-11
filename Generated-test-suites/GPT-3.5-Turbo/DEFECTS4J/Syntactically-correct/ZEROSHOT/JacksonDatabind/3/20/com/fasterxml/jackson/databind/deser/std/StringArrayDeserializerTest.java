import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

@JacksonStdImpl
public class StringArrayDeserializerTest {

    private final StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer();

    @Test
    public void testDeserialize() throws IOException {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        String[] expected = {"value1", "value2", "value3"};

        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserializeCustomElementDeserializer() throws IOException {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        String[] expected = {"value1", "value2", "value3"};

        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();

        String[] result = stringArrayDeserializer._deserializeCustom(jsonParser, deserializationContext);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        String json = "\"value\"";
        String[] expected = {"value"};

        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();

        String[] result = stringArrayDeserializer.handleNonArray(jsonParser, deserializationContext);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testCreateContextual() throws IOException {
        DeserializationContext deserializationContext = new DeserializationContext();
        BeanProperty property = new BeanProperty();

        JsonDeserializer<?> result = stringArrayDeserializer.createContextual(deserializationContext, property);

        Assert.assertNull(result);
    }

}