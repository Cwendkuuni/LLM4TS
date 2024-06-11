import org.junit.Test;
import static org.junit.Assert.*;

public class FieldPropertyTest {

    @Test
    public void testDeserializeAndSet_NullValueWithSkipNulls() throws Exception {
        // Arrange
        FieldProperty objUnderTest = createFieldProperty();
        JsonParser mockJsonParser = createMockJsonParser(JsonToken.VALUE_NULL);
        DeserializationContext mockDeserializationContext = createMockDeserializationContext();
        Object instance = new Object();
        
        // Act
        objUnderTest.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);
        
        // Assert
        verify(mockJsonParser).hasToken(JsonToken.VALUE_NULL);
        verifyNoMoreInteractions(mockJsonParser);
        verify(mockDeserializationContext).isEnabled(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES);
        verifyNoMoreInteractions(mockDeserializationContext);
        assertEquals(null, objUnderTest.getNullProvider().getNullValue(mockDeserializationContext));
        verify(mockConversions, never()).findConverter(any(), any(), any());
        verify(mockValueDeserializer, never()).deserialize(any(), any());
        
        // Verify
        verify(mockField).set(instance, null);
        verifyNoMoreInteractions(mockField);
    }

    @Test
    public void testDeserializeAndSet_NullValueWithoutSkipNulls() throws Exception {
        // Arrange
        FieldProperty objUnderTest = createFieldProperty();
        JsonParser mockJsonParser = createMockJsonParser(JsonToken.VALUE_NULL);
        DeserializationContext mockDeserializationContext = createMockDeserializationContext();
        Object instance = new Object();
        
        // Act
        objUnderTest.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);
        
        // Assert
        verify(mockJsonParser).hasToken(JsonToken.VALUE_NULL);
        verifyNoMoreInteractions(mockJsonParser);
        verify(mockDeserializationContext).isEnabled(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES);
        verifyNoMoreInteractions(mockDeserializationContext);
        assertEquals(null, objUnderTest.getNullProvider().getNullValue(mockDeserializationContext));
        verify(mockConversions, never()).findConverter(any(), any(), any());
        verify(mockValueDeserializer, never()).deserialize(any(), any());
        
        // Verify
        verify(mockField).set(instance, null);
        verifyNoMoreInteractions(mockField);
    }

    @Test
    public void testDeserializeAndSet_NonNullValue() throws Exception {
        // Arrange
        FieldProperty objUnderTest = createFieldProperty();
        JsonParser mockJsonParser = createMockJsonParser(JsonToken.VALUE_STRING);
        DeserializationContext mockDeserializationContext = createMockDeserializationContext();
        Object instance = new Object();
        String value = "test value";
        when(mockStringValueDeserializer.deserialize(mockJsonParser, mockDeserializationContext)).thenReturn(value);
        
        // Act
        objUnderTest.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);
        
        // Assert
        verify(mockJsonParser).hasToken(JsonToken.VALUE_NULL);
        verifyNoMoreInteractions(mockJsonParser);
        verify(mockDeserializationContext).isEnabled(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES);
        verifyNoMoreInteractions(mockDeserializationContext);
        verify(mockStringValueDeserializer).deserialize(mockJsonParser, mockDeserializationContext);
        
        // Verify
        verify(mockField).set(instance, value);
        verifyNoMoreInteractions(mockField);
    }
  
    @Test
    public void testDeserializeSetAndReturn_NullValueWithSkipNulls() throws Exception {
        // Arrange
        FieldProperty objUnderTest = createFieldProperty();
        JsonParser mockJsonParser = createMockJsonParser(JsonToken.VALUE_NULL);
        DeserializationContext mockDeserializationContext = createMockDeserializationContext();
        Object instance = new Object();
        
        // Act
        Object result = objUnderTest.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);
        
        // Assert
        verify(mockJsonParser).hasToken(JsonToken.VALUE_NULL);
        verifyNoMoreInteractions(mockJsonParser);
        verify(mockDeserializationContext).isEnabled(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES);
        verifyNoMoreInteractions(mockDeserializationContext);
        assertEquals(null, objUnderTest.getNullProvider().getNullValue(mockDeserializationContext));
        verify(mockConversions, never()).findConverter(any(), any(), any());
        verify(mockValueDeserializer, never()).deserialize(any(), any());
        
        // Verify
        verify(mockField).set(instance, null);
        verifyNoMoreInteractions(mockField);
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn_NullValueWithoutSkipNulls() throws Exception {
        // Arrange
        FieldProperty objUnderTest = createFieldProperty();
        JsonParser mockJsonParser = createMockJsonParser(JsonToken.VALUE_NULL);
        DeserializationContext mockDeserializationContext = createMockDeserializationContext();
        Object instance = new Object();
        
        // Act
        Object result = objUnderTest.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);
        
        // Assert
        verify(mockJsonParser).hasToken(JsonToken.VALUE_NULL);
        verifyNoMoreInteractions(mockJsonParser);
        verify(mockDeserializationContext).isEnabled(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES);
        verifyNoMoreInteractions(mockDeserializationContext);
        assertEquals(null, objUnderTest.getNullProvider().getNullValue(mockDeserializationContext));
        verify(mockConversions, never()).findConverter(any(), any(), any());
        verify(mockValueDeserializer, never()).deserialize(any(), any());
        
        // Verify
        verify(mockField).set(instance, null);
        verifyNoMoreInteractions(mockField);
        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn_NonNullValue() throws Exception {
        // Arrange
        FieldProperty objUnderTest = createFieldProperty();
        JsonParser mockJsonParser = createMockJsonParser(JsonToken.VALUE_STRING);
        DeserializationContext mockDeserializationContext = createMockDeserializationContext();
        Object instance = new Object();
        String value = "test value";
        when(mockStringValueDeserializer.deserialize(mockJsonParser, mockDeserializationContext)).thenReturn(value);
        
        // Act
        Object result = objUnderTest.deserializeAndSet(mockJsonParser, mockDeserializationContext, instance);
        
        // Assert
        verify(mockJsonParser).hasToken(JsonToken.VALUE_NULL);
        verifyNoMoreInteractions(mockJsonParser);
        verify(mockDeserializationContext).isEnabled(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES);
        verifyNoMoreInteractions(mockDeserializationContext);
        verify(mockStringValueDeserializer).deserialize(mockJsonParser, mockDeserializationContext);
        
        // Verify
        verify(mockField).set(instance, value);
        verifyNoMoreInteractions(mockField);
        assertEquals(instance, result);
    }
    
    @Test
    public void testSet() throws Exception {
        // Arrange
        FieldProperty objUnderTest = createFieldProperty();
        Object instance = new Object();
        Object value = new Object();
        
        // Act
        objUnderTest.set(instance, value);
        
        // Verify
        verify(mockField).set(instance, value);
        verifyNoMoreInteractions(mockField);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        // Arrange
        FieldProperty objUnderTest = createFieldProperty();
        Object instance = new Object();
        Object value = new Object();
        
        // Act
        Object result = objUnderTest.setAndReturn(instance, value);
        
        // Verify
        verify(mockField).set(instance, value);
        verifyNoMoreInteractions(mockField);
        assertEquals(instance, result);
    }

    private FieldProperty createFieldProperty() throws Exception {
        BeanPropertyDefinition mockBeanPropertyDefinition = mock(BeanPropertyDefinition.class);
        JavaType mockJavaType = mock(JavaType.class);
        TypeDeserializer mockTypeDeserializer = mock(TypeDeserializer.class);
        Annotations mockAnnotations = mock(Annotations.class);
        AnnotatedField mockAnnotatedField = mock(AnnotatedField.class);
        Field mockField = mock(Field.class);
        
        return new FieldProperty(mockBeanPropertyDefinition, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
    }

    private JsonParser createMockJsonParser(JsonToken jsonToken) throws Exception {
        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.hasToken(jsonToken)).thenReturn(true);
        return mockJsonParser;
    }

    private DeserializationContext createMockDeserializationContext() {
        DeserializationConfig mockDeserializationConfig = mock(DeserializationConfig.class);
        DeserializationContext mockDeserializationContext = mock(DeserializationContext.class);
        
        when(mockDeserializationConfig.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        when(mockDeserializationContext.getConfig()).thenReturn(mockDeserializationConfig);
        
        return mockDeserializationContext;
    }
    
    private Field createMockField() {
        Field mockField = mock(Field.class);
        when(mockAnnotatedField.getAnnotated()).thenReturn(mockField);
        return mockField;
    }
}