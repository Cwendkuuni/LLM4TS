import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;

public class CollectionDeserializerTest {

    private JsonDeserializer<Object> valueDeserializerMock;
    private CollectionDeserializer collectionDeserializer;

    @Before
    public void setup() {
        valueDeserializerMock = mock(JsonDeserializer.class);
        collectionDeserializer = new CollectionDeserializer(null, valueDeserializerMock, null, null);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser parserMock = mock(JsonParser.class);
        DeserializationContext contextMock = mock(DeserializationContext.class);
        Collection<Object> result = collectionDeserializer.deserialize(parserMock, contextMock);

        assertNotNull(result);
        verify(parserMock, times(1)).isExpectedStartArrayToken();
    }

    @Test
    public void testDeserializeNonArray() throws IOException {
        JsonParser parserMock = mock(JsonParser.class);
        DeserializationContext contextMock = mock(DeserializationContext.class);
        BeanProperty propertyMock = mock(BeanProperty.class);
        Collection<Object> result = collectionDeserializer.deserialize(parserMock, contextMock, null);

        assertNotNull(result);
        verify(parserMock, times(1)).getCurrentToken();
        verify(propertyMock, never()).getType();
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeNonArrayFail() throws IOException {
        JsonParser parserMock = mock(JsonParser.class);
        DeserializationContext contextMock = mock(DeserializationContext.class);
        when(parserMock.getCurrentToken()).thenReturn(null);

        collectionDeserializer.deserialize(parserMock, contextMock, null);
    }
}