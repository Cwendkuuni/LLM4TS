import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CollectionDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    private CollectionDeserializer collectionDeserializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        collectionDeserializer = new CollectionDeserializer(JavaType.collectionType(CollectionDeserializer.class), null, null, null);
    }

    @Test
    public void testDeserializeWithEmptyString() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_STRING)).thenReturn(true);
        when(jsonParser.getText()).thenReturn("");

        Collection<Object> collection = collectionDeserializer.deserialize(jsonParser, deserializationContext);

        assertEquals(0, collection.size());
    }

    @Test
    public void testDeserializeWithNullValue() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        Collection<Object> expected = Arrays.asList(null);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithSingleValue() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(jsonParser.getNumberValue()).thenReturn(10);

        Collection<Object> result = collectionDeserializer.deserialize(jsonParser, deserializationContext);
        Collection<Object> expected = Arrays.asList(10);

        assertEquals(expected, result);
    }

    @Test
    public void testDeserializeWithUnresolvedReference() throws IOException {
        UnresolvedForwardReference unresolvedForwardReference = new UnresolvedForwardReference(null, "id", Collection.class, null);
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);
        when(deserializationContext.handleSecondaryContextualization(any(), any(), any())).thenReturn(null);

        CollectionDeserializer.CollectionReferringAccumulator referringAccumulator = spy(collectionDeserializer.new CollectionReferringAccumulator(Collection.class, null));
        doReturn(referringAccumulator).when(deserializationContext).findObjectId(any(), any());

        collectionDeserializer.deserialize(jsonParser, deserializationContext);

        verify(referringAccumulator, times(1)).add(any());
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        Object result = collectionDeserializer.deserializeWithType(jsonParser, deserializationContext, null);

        assertEquals(null, result);
    }

}