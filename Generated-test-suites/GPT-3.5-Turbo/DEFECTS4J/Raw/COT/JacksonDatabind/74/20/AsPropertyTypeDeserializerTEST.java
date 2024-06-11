import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import static org.mockito.Mockito.*;

public class AsPropertyTypeDeserializerTest {

    private AsPropertyTypeDeserializer deserializer;
    private DeserializationContext deserializationContext;
    private JsonParser jsonParser;
    private BeanProperty beanProperty;

    @Before
    public void setup() {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = mock(JavaType.class);
        deserializer = new AsPropertyTypeDeserializer(baseType, typeIdResolver, typePropertyName, typeIdVisible,
                defaultImpl);
        deserializationContext = mock(DeserializationContext.class);
        jsonParser = mock(JsonParser.class);
        beanProperty = mock(BeanProperty.class);
    }

    @Test
    public void testForProperty() {
        deserializer.forProperty(beanProperty);
        verify(beanProperty, times(1)).getValueType();
        verify(deserializer, times(1)).getTypeInclusion();
        verify(deserializer, times(1)).new AsPropertyTypeDeserializer(deserializer, beanProperty);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        // Assert the behavior when the type id is present in the JSON input
        when(jsonParser.canReadTypeId()).thenReturn(true);
        when(jsonParser.getTypeId()).thenReturn("someType");
        deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        // Verify that the correct deserialization method is called with the type id
        verify(deserializer, times(1))._deserializeWithNativeTypeId(jsonParser, deserializationContext, "someType");

        // Assert the behavior when type id is not present in the JSON input but the input starts with an object
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        // Verify that the correct deserialization method is called for the property with the expected type id
        verify(deserializer, times(1))._deserializeTypedForId(jsonParser, deserializationContext, null);

        // Assert the behavior when type id is not present in the JSON input and the input does not start with an object
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("someType");
        deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        // Verify that the correct deserialization method is called for the default implementation
        verify(deserializer, times(1))._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);
    }

    @Test
    public void testDeserializeTypedForId() throws IOException {
        // Assert the behavior when the type id is visible
        when(jsonParser.getCurrentName()).thenReturn("type");
        when(jsonParser.getText()).thenReturn("someType");
        when(deserializer.isTypeIdVisible()).thenReturn(true);
        when(deserializer._findDeserializer(deserializationContext, "someType")).thenReturn(mock(JsonDeserializer.class));
        deserializer._deserializeTypedForId(jsonParser, deserializationContext, null);
        // Verify that the correct deserialization method is called
        verify(deserializationContext, times(1)).handleUnexpectedToken(String.class, jsonParser);
        verify(deserializer, times(1))._findDeserializer(deserializationContext, "someType");

        // Assert the behavior when the type id is not visible
        when(deserializer.isTypeIdVisible()).thenReturn(false);
        when(jsonParser.getCurrentName()).thenReturn("type");
        when(jsonParser.getText()).thenReturn("someType");
        when(deserializer._findDeserializer(deserializationContext, "someType")).thenReturn(mock(JsonDeserializer.class));
        deserializer._deserializeTypedForId(jsonParser, deserializationContext, null);
        // Verify that the correct deserialization method is called without merging the id in the JSON input
        verify(deserializationContext, times(1)).handleUnexpectedToken(String.class, jsonParser);
        verify(deserializer, times(0))._findDeserializer(deserializationContext, "someType");
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl() throws IOException {
        // Assert the behavior when the default implementation deserializer is found
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);
        when(deserializer._findDefaultImplDeserializer(deserializationContext)).thenReturn(defaultImplDeserializer);
        deserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);
        // Verify that the correct deserialization method is called with the default implementation deserializer
        verify(defaultImplDeserializer, times(1)).deserialize(jsonParser, deserializationContext);

        // Assert the behavior when the input is a natural value
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("someValue");
        deserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);
        // Verify that the correct deserialization method is called if the input is a natural value
        verify(deserializer, times(1)).deserializeIfNatural(jsonParser, deserializationContext);

        // Assert the behavior when the input is not a natural value but an array
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        deserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);
        // Verify that the method to handle array wrapper is called
        verify(deserializer, times(1)).deserializeTypedFromAny(jsonParser, deserializationContext);

        // Assert the behavior when the input does not have the type property
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME);
        deserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);
        // Verify that the correct deserialization method is called when the input does not have the type property
        verify(deserializationContext, times(1)).handleWrongTokenException(jsonParser, JsonToken.FIELD_NAME,
                "missing property 'type' that is to contain type id");
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        // Assert the behavior when the input is an array
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        // Verify that the correct deserialization method is called for array wrapper
        verify(deserializer, times(1)).deserializeTypedFromArray(jsonParser, deserializationContext);

        // Assert the behavior when the input is not an array
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        // Verify that the correct deserialization method is called for any object
        verify(deserializer, times(1)).deserializeTypedFromObject(jsonParser, deserializationContext);
    }
}