import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;

public class StringCollectionDeserializerTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private ValueInstantiator valueInstantiator;

    @Mock
    private JsonDeserializer<Object> delegateDeserializer;

    private Collection<String> expectedResult;

    private StringCollectionDeserializer stringCollectionDeserializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        expectedResult = new ArrayList<String>();
        stringCollectionDeserializer = new StringCollectionDeserializer(JavaType.collectionType(String.class),
                mock(JsonDeserializer.class), valueInstantiator);
    }

    @Test
    public void deserialize_SuccessfulDeserialization_ReturnsDeserializedCollection() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextTextValue()).thenReturn("value1", "value2", null);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING,
                JsonToken.VALUE_NULL, JsonToken.END_ARRAY);

        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);

        expectedResult.addAll(Arrays.asList("value1", "value2", null));
        assertEquals(expectedResult, result);
    }

    @Test
    public void deserialize_CustomDeserializerProvided_SuccessfulDeserialization_ReturnsDeserializedCollection()
            throws IOException {
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        StringCollectionDeserializer customDeserializer = new StringCollectionDeserializer(
                JavaType.collectionType(String.class), valueInstantiator, delegateDeserializer, valueDeserializer,
                mock(NullValueProvider.class), false);

        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextTextValue()).thenReturn("value1", "value2");
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING, JsonToken.VALUE_STRING,
                JsonToken.END_ARRAY);

        Collection<String> result = customDeserializer.deserialize(jsonParser, deserializationContext);

        expectedResult.addAll(Arrays.asList("value1", "value2"));
        assertEquals(expectedResult, result);
    }

    @Test
    public void deserialize_NonArrayTokenAndSingleValueWrappingEnabled_ReturnsCollectionWithSingleValue()
            throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("value");

        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);

        expectedResult.add("value");
        assertEquals(expectedResult, result);
    }

    @Test
    public void deserialize_NonArrayTokenAndSingleValueWrappingDisabled_ThrowsException() throws IOException {
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("value");

        try {
            stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertTrue(e instanceof JsonMappingException);
        }
    }

}