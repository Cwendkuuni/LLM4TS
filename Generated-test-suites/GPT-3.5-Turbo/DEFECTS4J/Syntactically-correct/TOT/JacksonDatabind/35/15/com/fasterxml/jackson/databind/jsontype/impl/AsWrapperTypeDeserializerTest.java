import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AsWrapperTypeDeserializerTest {

    private AsWrapperTypeDeserializer deserializer;
    private JavaType baseType;
    private TypeIdResolver typeIdResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;

    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        typeIdResolver = mock(TypeIdResolver.class);
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = Object.class;
        deserializer = new AsWrapperTypeDeserializer(baseType, typeIdResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testDeserializeTypedFromObject_WithTypeId_ShouldReturnDeserializedObject() throws IOException {
        // Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn("mockType");
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.END_OBJECT);
        when(jp.getText()).thenReturn("mockType");
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "mockType")).thenReturn(deser);
        Object value = new Object();
        when(deser.deserialize(jp, ctxt)).thenReturn(value);

        // Act
        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);

        // Assert
        Assert.assertEquals(value, result);
        verify(jp, times(1)).canReadTypeId();
        verify(jp, times(1)).getTypeId();
        verify(jp, times(1)).getCurrentToken();
        verify(jp, times(2)).nextToken();
        verify(jp, times(1)).getText();
        verify(deserializer, times(1))._findDeserializer(ctxt, "mockType");
        verify(deser, times(1)).deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromArray_WithTypeId_ShouldReturnDeserializedObject() throws IOException {
        // Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn("mockType");
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.END_OBJECT);
        when(jp.getText()).thenReturn("mockType");
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "mockType")).thenReturn(deser);
        Object value = new Object();
        when(deser.deserialize(jp, ctxt)).thenReturn(value);

        // Act
        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);

        // Assert
        Assert.assertEquals(value, result);
        verify(jp, times(1)).canReadTypeId();
        verify(jp, times(1)).getTypeId();
        verify(jp, times(1)).getCurrentToken();
        verify(jp, times(2)).nextToken();
        verify(jp, times(1)).getText();
        verify(deserializer, times(1))._findDeserializer(ctxt, "mockType");
        verify(deser, times(1)).deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromScalar_WithTypeId_ShouldReturnDeserializedObject() throws IOException {
        // Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn("mockType");
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.END_OBJECT);
        when(jp.getText()).thenReturn("mockType");
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "mockType")).thenReturn(deser);
        Object value = new Object();
        when(deser.deserialize(jp, ctxt)).thenReturn(value);

        // Act
        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);

        // Assert
        Assert.assertEquals(value, result);
        verify(jp, times(1)).canReadTypeId();
        verify(jp, times(1)).getTypeId();
        verify(jp, times(1)).getCurrentToken();
        verify(jp, times(2)).nextToken();
        verify(jp, times(1)).getText();
        verify(deserializer, times(1))._findDeserializer(ctxt, "mockType");
        verify(deser, times(1)).deserialize(jp, ctxt);
    }

    @Test
    public void testDeserializeTypedFromAny_WithTypeId_ShouldReturnDeserializedObject() throws IOException {
        // Arrange
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn("mockType");
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME, JsonToken.END_OBJECT);
        when(jp.getText()).thenReturn("mockType");
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "mockType")).thenReturn(deser);
        Object value = new Object();
        when(deser.deserialize(jp, ctxt)).thenReturn(value);

        // Act
        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);

        // Assert
        Assert.assertEquals(value, result);
        verify(jp, times(1)).canReadTypeId();
        verify(jp, times(1)).getTypeId();
        verify(jp, times(1)).getCurrentToken();
        verify(jp, times(2)).nextToken();
        verify(jp, times(1)).getText();
        verify(deserializer, times(1))._findDeserializer(ctxt, "mockType");
        verify(deser, times(1)).deserialize(jp, ctxt);
    }
}