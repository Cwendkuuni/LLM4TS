import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FieldPropertyTest {

    // Test setup
    private FieldProperty fieldProperty;

    @Before
    public void setUp() {
        // Mock the annotations and field for testing
        Annotation mockAnnotation = new Annotation() {};
        AnnotatedField annotatedField = new AnnotatedField(mockAnnotation, mockField);
        fieldProperty = new FieldProperty(mockBeanPropertyDefinition, mockJavaType, mockTypeDeserializer, mockAnnotations, annotatedField);
    }

    @Test
    public void testDeserializeAndSet_withNullValue_skipNullsEnabled_shouldNotSetField() {
        // Setup
        JsonParser mockParser = new JsonParser();
        DeserializationContext mockContext = new DeserializationContext();
        Object instance = new Object();

        // Configure mock objects
        when(mockParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(fieldProperty.isSkipNulls()).thenReturn(true);

        // Call method under test
        fieldProperty.deserializeAndSet(mockParser, mockContext, instance);

        // Verify that the field is not set
        assertNull(field.get(instance));
    }

    @Test
    public void testDeserializeAndSet_withNullValue_skipNullsDisabled_shouldSetFieldToNullProviderValue() {
        // Setup
        JsonParser mockParser = new JsonParser();
        DeserializationContext mockContext = new DeserializationContext();
        Object instance = new Object();

        // Configure mock objects
        when(mockParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(fieldProperty.isSkipNulls()).thenReturn(false);
        when(fieldProperty.getNullProvider()).thenReturn(mockNullValueProvider);
        when(mockNullValueProvider.getNullValue(mockContext)).thenReturn(mockNullValue);

        // Call method under test
        fieldProperty.deserializeAndSet(mockParser, mockContext, instance);

        // Verify that the field is set to the null provider value
        assertEquals(mockNullValue, field.get(instance));
    }

    @Test
    public void testDeserializeAndSet_withNonNullValueAndNoTypeDeserializer_shouldDeserializeAndSetField() {
        // Setup
        JsonParser mockParser = new JsonParser();
        DeserializationContext mockContext = new DeserializationContext();
        Object instance = new Object();
        Object mockValue = new Object();

        // Configure mock objects
        when(mockParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(mockParser.getValueAsString()).thenReturn(mockStringValue);
        when(fieldProperty.getTypeDeserializer()).thenReturn(null);
        when(fieldProperty.getValueDeserializer()).thenReturn(mockDeserializer);
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn(mockValue);

        // Call method under test
        fieldProperty.deserializeAndSet(mockParser, mockContext, instance);

        // Verify that the field is set to the deserialized value
        assertEquals(mockValue, field.get(instance));
    }

    @Test
    public void testDeserializeAndSet_withNonNullValueAndTypeDeserializer_shouldDeserializeAndSetField() {
        // Setup
        JsonParser mockParser = new JsonParser();
        DeserializationContext mockContext = new DeserializationContext();
        Object instance = new Object();
        Object mockValue = new Object();

        // Configure mock objects
        when(mockParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(mockParser.getValueAsString()).thenReturn(mockStringValue);
        when(fieldProperty.getTypeDeserializer()).thenReturn(mockTypeDeserializer);
        when(mockTypeDeserializer.deserializeWithType(mockParser, mockContext)).thenReturn(mockValue);

        // Call method under test
        fieldProperty.deserializeAndSet(mockParser, mockContext, instance);

        // Verify that the field is set to the deserialized value
        assertEquals(mockValue, field.get(instance));
    }

    @Test
    public void testDeserializeSetAndReturn_withNullValue_skipNullsEnabled_shouldNotSetFieldAndReturnSameInstance() {
        // Setup
        JsonParser mockParser = new JsonParser();
        DeserializationContext mockContext = new DeserializationContext();
        Object instance = new Object();

        // Configure mock objects
        when(mockParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(fieldProperty.isSkipNulls()).thenReturn(true);

        // Call method under test
        Object result = fieldProperty.deserializeSetAndReturn(mockParser, mockContext, instance);

        // Verify that the field is not set and the same instance is returned
        assertNull(field.get(instance));
        assertSame(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn_withNullValue_skipNullsDisabled_shouldSetFieldToNullProviderValueAndReturnSameInstance() {
        // Setup
        JsonParser mockParser = new JsonParser();
        DeserializationContext mockContext = new DeserializationContext();
        Object instance = new Object();

        // Configure mock objects
        when(mockParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(fieldProperty.isSkipNulls()).thenReturn(false);
        when(fieldProperty.getNullProvider()).thenReturn(mockNullValueProvider);
        when(mockNullValueProvider.getNullValue(mockContext)).thenReturn(mockNullValue);

        // Call method under test
        Object result = fieldProperty.deserializeSetAndReturn(mockParser, mockContext, instance);

        // Verify that the field is set to the null provider value and the same instance is returned
        assertEquals(mockNullValue, field.get(instance));
        assertSame(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn_withNonNullValueAndNoTypeDeserializer_shouldDeserializeAndSetFieldAndReturnSameInstance() {
        // Setup
        JsonParser mockParser = new JsonParser();
        DeserializationContext mockContext = new DeserializationContext();
        Object instance = new Object();
        Object mockValue = new Object();

        // Configure mock objects
        when(mockParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(mockParser.getValueAsString()).thenReturn(mockStringValue);
        when(fieldProperty.getTypeDeserializer()).thenReturn(null);
        when(fieldProperty.getValueDeserializer()).thenReturn(mockDeserializer);
        when(mockDeserializer.deserialize(mockParser, mockContext)).thenReturn(mockValue);

        // Call method under test
        Object result = fieldProperty.deserializeSetAndReturn(mockParser, mockContext, instance);

        // Verify that the field is set to the deserialized value and the same instance is returned
        assertEquals(mockValue, field.get(instance));
        assertSame(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn_withNonNullValueAndTypeDeserializer_shouldDeserializeAndSetFieldAndReturnSameInstance() {
        // Setup
        JsonParser mockParser = new JsonParser();
        DeserializationContext mockContext = new DeserializationContext();
        Object instance = new Object();
        Object mockValue = new Object();

        // Configure mock objects
        when(mockParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(mockParser.getValueAsString()).thenReturn(mockStringValue);
        when(fieldProperty.getTypeDeserializer()).thenReturn(mockTypeDeserializer);
        when(mockTypeDeserializer.deserializeWithType(mockParser, mockContext)).thenReturn(mockValue);

        // Call method under test
        Object result = fieldProperty.deserializeSetAndReturn(mockParser, mockContext, instance);

        // Verify that the field is set to the deserialized value and the same instance is returned
        assertEquals(mockValue, field.get(instance));
        assertSame(instance, result);
    }

    @Test
    public void testSet_shouldSetField() {
        // Setup
        Object instance = new Object();
        Object mockValue = new Object();

        // Call method under test
        fieldProperty.set(instance, mockValue);

        // Verify that the field is set to the value
        assertEquals(mockValue, field.get(instance));
    }

    @Test
    public void testSetAndReturn_shouldSetFieldAndReturnSameInstance() {
        // Setup
        Object instance = new Object();
        Object mockValue = new Object();

        // Call method under test
        Object result = fieldProperty.setAndReturn(instance, mockValue);

        // Verify that the field is set to the value and the same instance is returned
        assertEquals(mockValue, field.get(instance));
        assertSame(instance, result);
    }

    // Additional tests for BeanProperty implementation

    @Test
    public void testGetAnnotation_whenAnnotatedFieldNotNull_shouldReturnAnnotation() {
        // Setup
        Annotation mockAnnotation = new Annotation() {};
        AnnotatedField annotatedField = new AnnotatedField(mockAnnotation, mockField);
        fieldProperty = new FieldProperty(mockBeanPropertyDefinition, mockJavaType, mockTypeDeserializer, mockAnnotations, annotatedField);
        
        // Call method under test
        Annotation result = fieldProperty.getAnnotation(Annotation.class);
        
        // Verify that the annotation is returned
        assertSame(mockAnnotation, result);
    }
    
    @Test
    public void testGetAnnotation_whenAnnotatedFieldNull_shouldReturnNull() {
        // Call method under test
        Annotation result = fieldProperty.getAnnotation(Annotation.class);

        // Verify that null is returned
        assertNull(result);
    }

    @Test
    public void testGetMember_shouldReturnAnnotatedField() {
        // Call method under test
        AnnotatedMember result = fieldProperty.getMember();

        // Verify that the annotated field is returned
        assertSame(mockAnnotatedField, result);
    }
}