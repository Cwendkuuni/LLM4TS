import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;

@RunWith(MockitoJUnitRunner.class)
public class StringCollectionDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeCustom() throws IOException {
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueDeserializer, null);
        Collection<String> result = deserializer.deserialize(jsonParser, deserializationContext);
        assertNotNull(result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Object result = deserializer.deserializeWithType(jsonParser, deserializationContext, null);
        assertNull(result);
    }

    @Test
    public void testHandleNonArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Collection<String> result = deserializer.handleNonArray(jsonParser, deserializationContext, null);
        assertNotNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeException() throws IOException {
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, valueDeserializer, null);
        doThrow(new IOException()).when(valueDeserializer).deserialize(jsonParser, deserializationContext);

        deserializer.deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testCreateWithResolved() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        StringCollectionDeserializer newDeserializer = deserializer.withResolved(null, null, null, true);

        assertNotSame(deserializer, newDeserializer);
    }

    @Test
    public void testIsCachable() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        assertTrue(deserializer.isCachable());
    }
}