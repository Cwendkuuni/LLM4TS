import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Before;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {
    private AsWrapperTypeDeserializer deserializer;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;

    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        typePropertyName = "type";
        deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, typePropertyName, true, null);
    }

    @Test
    public void testForProperty() {
        BeanProperty prop = mock(BeanProperty.class);
        assertEquals(deserializer, deserializer.forProperty(prop));
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("type");

        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "type")).thenReturn(deser);
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");
        when(jp.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);

        assertEquals("value", result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("type");

        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "type")).thenReturn(deser);
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");
        when(jp.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);

        assertEquals("value", result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("type");

        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "type")).thenReturn(deser);
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");
        when(jp.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);

        assertEquals("value", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("type");

        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "type")).thenReturn(deser);
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");
        when(jp.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);

        assertEquals("value", result);
    }

    @Test
    public void testDeserializeTypedFromObjectWithTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn("type");
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("type");

        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "type")).thenReturn(deser);
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");
        when(jp.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromObject(jp, ctxt);

        assertEquals("value", result);
    }

    @Test
    public void testDeserializeTypedFromArrayWithTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn("type");
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("type");

        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "type")).thenReturn(deser);
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");
        when(jp.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromArray(jp, ctxt);

        assertEquals("value", result);
    }

    @Test
    public void testDeserializeTypedFromScalarWithTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn("type");
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("type");

        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "type")).thenReturn(deser);
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");
        when(jp.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromScalar(jp, ctxt);

        assertEquals("value", result);
    }

    @Test
    public void testDeserializeTypedFromAnyWithTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(true);
        when(jp.getTypeId()).thenReturn("type");
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("type");

        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(ctxt, "type")).thenReturn(deser);
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");
        when(jp.nextToken()).thenReturn(JsonToken.END_OBJECT);

        Object result = deserializer.deserializeTypedFromAny(jp, ctxt);

        assertEquals("value", result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObjectWithWrongToken() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.END_OBJECT);

        deserializer.deserializeTypedFromObject(jp, ctxt);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromArrayWithWrongToken() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.END_OBJECT);

        deserializer.deserializeTypedFromArray(jp, ctxt);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromScalarWithWrongToken() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.END_OBJECT);

        deserializer.deserializeTypedFromScalar(jp, ctxt);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromAnyWithWrongToken() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.END_OBJECT);

        deserializer.deserializeTypedFromAny(jp, ctxt);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromObjectWithWrongField() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("wrong");

        deserializer.deserializeTypedFromObject(jp, ctxt);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromArrayWithWrongField() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("wrong");

        deserializer.deserializeTypedFromArray(jp, ctxt);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromScalarWithWrongField() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("wrong");

        deserializer.deserializeTypedFromScalar(jp, ctxt);
    }

    @Test(expected = IOException.class)
    public void testDeserializeTypedFromAnyWithWrongField() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        when(jp.canReadTypeId()).thenReturn(false);
        when(jp.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("wrong");

        deserializer.deserializeTypedFromAny(jp, ctxt);
    }
}