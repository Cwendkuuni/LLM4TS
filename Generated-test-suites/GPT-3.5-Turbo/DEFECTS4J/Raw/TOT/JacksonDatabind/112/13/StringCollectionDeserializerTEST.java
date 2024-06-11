import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StringCollectionDeserializerTest {

    private StringCollectionDeserializer stringCollectionDeserializer;

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        stringCollectionDeserializer = new StringCollectionDeserializer();
    }

    @Test
    public void testDeserialize_WithValues_ShouldReturnCollectionOfStrings() throws Exception {
        // Arrange
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.nextTextValue()).thenReturn("value1", "value2", null);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);

        // Act
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        assertEquals(2, result.size());
        assertEquals("value1", ((List<String>) result).get(0));
        assertEquals("value2", ((List<String>) result).get(1));
    }

    @Test
    public void testDeserialize_WithEmptyValue_ShouldReturnEmptyCollection() throws Exception {
        // Arrange
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);

        // Act
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserialize_WithEmptyArray_ShouldReturnEmptyCollection() throws Exception {
        // Arrange
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);

        // Act
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserialize_WithNullValue_ShouldReturnNullCollection() throws Exception {
        // Arrange
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        // Act
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeWithType_WithValidData_ShouldReturnDeserializedObjectOfTypeCollectionOfString() throws Exception {
        // Arrange
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext)).thenReturn(new ArrayList<>());

        // Act
        Object result = stringCollectionDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);

        // Assert
        assertEquals(ArrayList.class, result.getClass());
    }

    @Test
    public void testDeserialize_WithType_DeserializeUsingCustom_ShouldReturnCollectionOfStrings() throws Exception {
        // Arrange
        Collection<String> result = new ArrayList<>();
        JsonDeserializer<String> stringJsonDeserializer = mock(JsonDeserializer.class);
        when(jsonParser.nextTextValue()).thenReturn("value1", "value2", null);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL, JsonToken.VALUE_STRING, JsonToken.END_ARRAY);
        when(stringJsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("value1", "value2");

        // Act
        result = stringCollectionDeserializer.deserializeUsingCustom(jsonParser, deserializationContext, result, stringJsonDeserializer);

        // Assert
        assertEquals(2, result.size());
        assertEquals("value1", ((List<String>) result).get(0));
        assertEquals("value2", ((List<String>) result).get(1));
    }

    @Test
    public void testDeserialize_WithType_HandleNonArray_ShouldReturnCollectionOfStrings() throws Exception {
        // Arrange
        Collection<String> result = new ArrayList<>();
        JsonDeserializer<String> stringJsonDeserializer = mock(JsonDeserializer.class);
        when(jsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(stringJsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("value1");

        // Act
        result = stringCollectionDeserializer.handleNonArray(jsonParser, deserializationContext, result);

        // Assert
        assertEquals(1, result.size());
        assertEquals("value1", ((List<String>) result).get(0));
    }
}