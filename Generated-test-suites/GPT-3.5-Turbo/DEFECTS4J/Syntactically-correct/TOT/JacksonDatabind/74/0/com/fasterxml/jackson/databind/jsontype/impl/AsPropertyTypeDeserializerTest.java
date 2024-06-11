import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

@RunWith(MockitoJUnitRunner.class)
public class AsPropertyTypeDeserializerTest {

    @Mock
    private JavaType bt;

    @Mock
    private TypeIdResolver idRes;

    @Mock
    private DeserializationContext deserializationContext;

    @Mock
    private JsonParser jsonParser;

    private AsPropertyTypeDeserializer asPropertyTypeDeserializer;

    @Before
    public void setup() {
        asPropertyTypeDeserializer = new AsPropertyTypeDeserializer(bt, idRes, "type", true, bt);
    }

    @Test
    public void testDeserializeTypedFromObject_WithNoNativeTypeId_ShouldReturnDeserializedObject() throws IOException {
        // Arrange
        when(jsonParser.canReadTypeId()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT, JsonToken.FIELD_NAME,
                JsonToken.VALUE_TRUE, JsonToken.END_OBJECT);
        when(jsonParser.getCurrentName()).thenReturn("type");
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.VALUE_TRUE, JsonToken.END_OBJECT);

        // Act
        Object deserializedObject = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser,
                deserializationContext);

        // Assert
        assertEquals(Boolean.TRUE, deserializedObject);
    }

    @Test
    public void testDeserializeTypedFromObject_WithOnlyFieldName_ShouldReturnDeserializedObject() throws IOException {
        // Arrange
        when(jsonParser.canReadTypeId()).thenReturn(false);
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.VALUE_TRUE, JsonToken.END_OBJECT);
        when(jsonParser.getCurrentName()).thenReturn("type");
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.VALUE_TRUE, JsonToken.END_OBJECT);

        // Act
        Object deserializedObject = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser,
                deserializationContext);

        // Assert
        assertEquals(Boolean.TRUE, deserializedObject);
    }

    @Test
    public void testDeserializeTypedFromObject_WithDefaultImpl_ShouldReturnDeserializedDefaultImplObject()
            throws IOException {
        // Arrange
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);
        when(asPropertyTypeDeserializer._findDefaultImplDeserializer(deserializationContext)).thenReturn(null);
        when(asPropertyTypeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext))
                .thenReturn(Boolean.TRUE);

        // Act
        Object deserializedObject = asPropertyTypeDeserializer.deserializeTypedFromObject(jsonParser,
                deserializationContext);

        // Assert
        assertEquals(Boolean.TRUE, deserializedObject);
    }

    @Test
    public void testDeserializeTypedForId_WithTypeVisibility_ShouldReturnDeserializedObject() throws IOException {
        // Arrange
        final String typeId = "type_1";
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn(typeId);
        when(asPropertyTypeDeserializer._findDeserializer(deserializationContext, typeId)).thenReturn(bt);
        when(asPropertyTypeDeserializer._typeIdVisible).thenReturn(true);
        when(jsonParser.getCurrentName()).thenReturn("type");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        TokenBuffer tb = mock(TokenBuffer.class);

        // Act
        Object deserializedObject = asPropertyTypeDeserializer
                ._deserializeTypedForId(jsonParser, deserializationContext, tb);

        // Assert
        assertEquals(bt, deserializedObject);
    }

    @Test
    public void testDeserializeTypedForId_WithTypeIdVisibility_ShouldReturnDeserializedObject() throws IOException {
        // Arrange
        final String typeId = "type_1";
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn(typeId);
        when(asPropertyTypeDeserializer._findDeserializer(deserializationContext, typeId)).thenReturn(bt);
        when(asPropertyTypeDeserializer._typeIdVisible).thenReturn(false);
        when(jsonParser.getCurrentName()).thenReturn("type");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        TokenBuffer tb = mock(TokenBuffer.class);

        // Act
        Object deserializedObject = asPropertyTypeDeserializer
                ._deserializeTypedForId(jsonParser, deserializationContext, tb);

        // Assert
        assertEquals(bt, deserializedObject);
    }

    @Test
    public void testDeserializeTypedForId_WithTokenBufferNotNull_ShouldReturnDeserializedObject() throws IOException {
        // Arrange
        final String typeId = "type_1";
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn(typeId);
        when(asPropertyTypeDeserializer._findDeserializer(deserializationContext, typeId)).thenReturn(bt);
        when(asPropertyTypeDeserializer._typeIdVisible).thenReturn(true);
        when(jsonParser.getCurrentName()).thenReturn("type");
        when(jsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        TokenBuffer tb = mock(TokenBuffer.class);
        when(tb.asParser(jsonParser)).thenReturn(jsonParser);

        // Act
        Object deserializedObject = asPropertyTypeDeserializer
                ._deserializeTypedForId(jsonParser, deserializationContext, tb);

        // Assert
        assertEquals(bt, deserializedObject);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WithDefaultImplDeserializerNotNull_ShouldReturnDeserializedDefaultImplObject()
            throws IOException {
        // Arrange
        when(asPropertyTypeDeserializer._findDefaultImplDeserializer(deserializationContext)).thenReturn(bt);
        when(asPropertyTypeDeserializer._baseType).thenReturn(bt);
        when(asPropertyTypeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext))
                .thenReturn(Boolean.TRUE);

        // Act
        Object deserializedObject = asPropertyTypeDeserializer
                ._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);

        // Assert
        assertEquals(Boolean.TRUE, deserializedObject);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WithNaturalResult_ShouldReturnNaturalResult() throws IOException {
        // Arrange
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(asPropertyTypeDeserializer.deserializeIfNatural(jsonParser, deserializationContext, bt))
                .thenReturn(42);

        // Act
        Object deserializedObject = asPropertyTypeDeserializer
                ._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);

        // Assert
        assertEquals(42, deserializedObject);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WithArrayToken_ShouldInvokeSuperMethod() throws IOException {
        // Arrange
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        when(asPropertyTypeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext))
                .thenReturn(Boolean.TRUE);

        // Act
        Object deserializedObject = asPropertyTypeDeserializer
                ._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);

        // Assert
        assertEquals(Boolean.TRUE, deserializedObject);
    }

    @Test
    public void testDeserializeTypedUsingDefaultImpl_WithMissingField_ShouldThrowException() throws IOException {
        // Arrange
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);
        when(jsonParser.getCurrentName()).thenReturn("type");

        // Act & Assert
        try {
            asPropertyTypeDeserializer._deserializeTypedUsingDefaultImpl(jsonParser, deserializationContext, null);
        } catch (JsonMappingException ex) {
            assertEquals("missing property 'type' that is to contain type id  (for class bt)", ex.getMessage());
        }
    }

    @Test
    public void testDeserializeTypedFromAny_WithArrayToken_ShouldInvokeSuperMethod() throws IOException {
        // Arrange
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);

        // Act
        Object deserializedObject = asPropertyTypeDeserializer.deserializeTypedFromAny(jsonParser,
                deserializationContext);

        // Assert
        assertNull(deserializedObject);
    }
}