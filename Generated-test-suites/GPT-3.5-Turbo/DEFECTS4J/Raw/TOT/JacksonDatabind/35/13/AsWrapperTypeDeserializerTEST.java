import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    
    private AsWrapperTypeDeserializer deserializer;
    private JsonParser jsonParser;
    private DeserializationContext deserializationContext;
    private TokenBuffer tokenBuffer;
    
    @Before
    public void setUp() throws IOException {
        JavaType bt = mock(JavaType.class);
        TypeIdResolver idRes = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        
        deserializer = new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
        jsonParser = mock(JsonParser.class);
        deserializationContext = mock(DeserializationContext.class);
        tokenBuffer = mock(TokenBuffer.class);
    }

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        // Arrange
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getText()).thenReturn("type");
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(deserializationContext, "type")).thenReturn(deser);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        
        // Act
        Object result = deserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        
        // Assert
        verify(jsonParser).canReadTypeId();
        verify(jsonParser).getTypeId();
        verify(deserializer)._deserialize(jsonParser, deserializationContext);
        verify(deserializer)._typeIdVisible;
        verify(jsonParser).getCurrentToken();
        verify(deserializer)._findDeserializer(deserializationContext, "type");
        verify(deser).deserialize(jsonParser, deserializationContext);
        verify(jsonParser).nextToken();
        assertEquals(null, result);
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        // Arrange
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getText()).thenReturn("type");
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(deserializationContext, "type")).thenReturn(deser);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        
        // Act
        Object result = deserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
        
        // Assert
        verify(jsonParser).canReadTypeId();
        verify(jsonParser).getTypeId();
        verify(deserializer)._deserialize(jsonParser, deserializationContext);
        verify(deserializer)._typeIdVisible;
        verify(jsonParser).getCurrentToken();
        verify(deserializer)._findDeserializer(deserializationContext, "type");
        verify(deser).deserialize(jsonParser, deserializationContext);
        verify(jsonParser).nextToken();
        assertEquals(null, result);
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        // Arrange
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getText()).thenReturn("type");
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(deserializationContext, "type")).thenReturn(deser);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        
        // Act
        Object result = deserializer.deserializeTypedFromScalar(jsonParser, deserializationContext);
        
        // Assert
        verify(jsonParser).canReadTypeId();
        verify(jsonParser).getTypeId();
        verify(deserializer)._deserialize(jsonParser, deserializationContext);
        verify(deserializer)._typeIdVisible;
        verify(jsonParser).getCurrentToken();
        verify(deserializer)._findDeserializer(deserializationContext, "type");
        verify(deser).deserialize(jsonParser, deserializationContext);
        verify(jsonParser).nextToken();
        assertEquals(null, result);
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        // Arrange
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(jsonParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jsonParser.getText()).thenReturn("type");
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(deserializer._findDeserializer(deserializationContext, "type")).thenReturn(deser);
        when(jsonParser.nextToken()).thenReturn(JsonToken.END_OBJECT);
        
        // Act
        Object result = deserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
        
        // Assert
        verify(jsonParser).canReadTypeId();
        verify(jsonParser).getTypeId();
        verify(deserializer)._deserialize(jsonParser, deserializationContext);
        verify(deserializer)._typeIdVisible;
        verify(jsonParser).getCurrentToken();
        verify(deserializer)._findDeserializer(deserializationContext, "type");
        verify(deser).deserialize(jsonParser, deserializationContext);
        verify(jsonParser).nextToken();
        assertEquals(null, result);
    }
}