import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class AsWrapperTypeDeserializerTest {
    
    private AsWrapperTypeDeserializer deserializer;
    private JsonParser parser;
    private DeserializationContext context;
    private TokenBuffer buffer;
    
    @Before
    public void setup() throws IOException {
        JavaType baseType = mock(JavaType.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = Object.class;
        
        deserializer = new AsWrapperTypeDeserializer(baseType, idResolver,
                typePropertyName, typeIdVisible, defaultImpl);
        
        JsonFactory factory = new JsonFactory();
        parser = factory.createParser("{}");
        context = mock(DeserializationContext.class);
        buffer = new TokenBuffer(null, false);
    }
    
    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("value");
        
        deserializer._findDeserializer = () -> deserializerMock;
        Object result = deserializer.deserializeTypedFromObject(parser, context);
        assertEquals("value", result);
        
        verify(parser, times(2)).getCurrentToken();
        verify(parser, times(2)).nextToken();
        verify(deserializerMock).deserialize(parser, context);
    }
    
    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("value");
        
        deserializer._findDeserializer = () -> deserializerMock;
        Object result = deserializer.deserializeTypedFromArray(parser, context);
        assertEquals("value", result);
        
        verify(parser, times(2)).getCurrentToken();
        verify(parser, times(2)).nextToken();
        verify(deserializerMock).deserialize(parser, context);
    }
    
    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("value");
        
        deserializer._findDeserializer = () -> deserializerMock;
        Object result = deserializer.deserializeTypedFromScalar(parser, context);
        assertEquals("value", result);
        
        verify(parser, times(2)).getCurrentToken();
        verify(parser, times(2)).nextToken();
        verify(deserializerMock).deserialize(parser, context);
    }
    
    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("value");
        
        deserializer._findDeserializer = () -> deserializerMock;
        Object result = deserializer.deserializeTypedFromAny(parser, context);
        assertEquals("value", result);
        
        verify(parser, times(2)).getCurrentToken();
        verify(parser, times(2)).nextToken();
        verify(deserializerMock).deserialize(parser, context);
    }
    
    @Test
    public void test_deserialize_with_type_id() throws IOException {
        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.canReadTypeId()).thenReturn(true);
        when(parserMock.getTypeId()).thenReturn("typeId");
        
        JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("value");
        
        deserializer._findDeserializer = () -> deserializerMock;
        Object result = deserializer._deserialize(parserMock, context);
        assertEquals("value", result);
        
        verify(parserMock, times(2)).canReadTypeId();
        verify(parserMock).getTypeId();
        verify(deserializerMock).deserialize(parserMock, context);
    }
    
    @Test
    public void test_deserialize_with_type_id_null() throws IOException {
        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.canReadTypeId()).thenReturn(true);
        when(parserMock.getTypeId()).thenReturn(null);
        
        JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("value");
        
        deserializer._findDeserializer = () -> deserializerMock;
        Object result = deserializer._deserialize(parserMock, context);
        assertEquals("value", result);
        
        verify(parserMock, times(2)).canReadTypeId();
        verify(parserMock).getTypeId();
        verify(deserializerMock).deserialize(parserMock, context);
    }
    
    @Test
    public void test_deserialize_with_native_type_ids() throws IOException {
        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.canReadTypeId()).thenReturn(false);
        
        JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("value");
        
        deserializer._findDeserializer = () -> deserializerMock;
        Object result = deserializer._deserialize(parserMock, context);
        assertEquals("value", result);
        
        verify(parserMock).canReadTypeId();
        verify(deserializerMock).deserialize(parserMock, context);
    }
    
    @Test
    public void test_deserialize_with_type_id_and_type_id_visible() throws IOException {
        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.canReadTypeId()).thenReturn(true);
        when(parserMock.getTypeId()).thenReturn("typeId");
        when(parserMock.nextToken()).thenReturn(JsonToken.START_OBJECT);
        
        JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("value");
        
        deserializer._findDeserializer = () -> deserializerMock;
        Object result = deserializer._deserialize(parserMock, context);
        assertEquals("value", result);
        
        verify(parserMock, times(2)).canReadTypeId();
        verify(parserMock).getTypeId();
        verify(parserMock, times(2)).nextToken();
        verify(parserMock).writeFieldName("type");
        verify(parserMock).writeString("typeId");
        verify(parserMock).asParser();
        verify(parserMock, times(2)).nextToken();
        verify(deserializerMock).deserialize(any(JsonParser.class), any(DeserializationContext.class));
    }
    
    @Test
    public void test_deserialize_with_type_id_and_not_type_id_visible() throws IOException {
        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.canReadTypeId()).thenReturn(true);
        when(parserMock.getTypeId()).thenReturn("typeId");
        when(parserMock.nextToken()).thenReturn(JsonToken.END_OBJECT);
        
        JsonDeserializer<Object> deserializerMock = mock(JsonDeserializer.class);
        when(deserializerMock.deserialize(any(JsonParser.class), any(DeserializationContext.class)))
                .thenReturn("value");
        
        deserializer._findDeserializer = () -> deserializerMock;
        Object result = deserializer._deserialize(parserMock, context);
        assertEquals("value", result);
        
        verify(parserMock, times(2)).canReadTypeId();
        verify(parserMock).getTypeId();
        verify(parserMock, times(2)).nextToken();
        verify(deserializerMock).deserialize(any(JsonParser.class), any(DeserializationContext.class));
    }
    
    @Test
    public void test_deserialize_with_wrong_start_token() throws IOException {
        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        
        try {
            deserializer._deserialize(parserMock, context);
        } catch (IOException e) {
            assertEquals("expected token START_OBJECT but got VALUE_STRING", e.getMessage());
        }
        
        verify(parserMock).getCurrentToken();
    }
    
    @Test
    public void test_deserialize_with_wrong_field_name_token() throws IOException {
        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(parserMock.nextToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        
        try {
            deserializer._deserialize(parserMock, context);
        } catch (IOException e) {
            assertEquals("expected token FIELD_NAME but got VALUE_NUMBER_INT", e.getMessage());
        }
        
        verify(parserMock, times(2)).getCurrentToken();
        verify(parserMock).nextToken();
    }
    
    @Test
    public void test_deserialize_with_not_end_object_token() throws IOException {
        JsonParser parserMock = mock(JsonParser.class);
        when(parserMock.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(parserMock.nextToken())
                .thenReturn(JsonToken.FIELD_NAME)
                .thenReturn(JsonToken.VALUE_STRING);
        
        try {
            deserializer._deserialize(parserMock, context);
        } catch (IOException e) {
            assertEquals("expected token END_OBJECT but got FIELD_NAME", e.getMessage());
        }
        
        verify(parserMock, times(3)).getCurrentToken();
        verify(parserMock, times(2)).nextToken();
    }
    
    @Test
    public void test_forProperty_with_same_property() {
        BeanProperty propertyMock = mock(BeanProperty.class);
        
        TypeDeserializer deserializer = this.deserializer.forProperty(propertyMock);
        
        assertEquals(this.deserializer, deserializer);
    }
    
    @Test
    public void test_forProperty_with_different_property() {
        BeanProperty propertyMock = mock(BeanProperty.class);
        TypeDeserializer deserializer = mock(TypeDeserializer.class);
        
        TypeDeserializer newDeserializer = this.deserializer.forProperty(propertyMock);
        
        assertEquals(AsWrapperTypeDeserializer.class, newDeserializer.getClass());
        assertEquals(propertyMock, newDeserializer.getProperty());
    }
    
    @Test
    public void test_getTypeInclusion() {
        assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
    }
    
    @Test
    public void test_baseTypeName() {
        JavaType baseTypeMock = mock(JavaType.class);
        when(baseTypeMock.toString()).thenReturn("BaseType");
        
        deserializer._baseType = baseTypeMock;
        
        assertEquals("BaseType", deserializer.baseTypeName());
        
        verify(baseTypeMock).toString();
    }

}