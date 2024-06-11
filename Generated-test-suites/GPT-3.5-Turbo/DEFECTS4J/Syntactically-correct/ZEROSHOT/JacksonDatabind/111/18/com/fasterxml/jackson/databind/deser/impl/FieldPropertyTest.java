import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

public class FieldPropertyTest {

    @Test
    public void testConstructor() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedField field = Mockito.mock(AnnotatedField.class);

        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);

        assertNotNull(fieldProperty);
        assertEquals(propDef, fieldProperty.getPropertyDefinition());
        assertEquals(type, fieldProperty.getType());
        assertEquals(typeDeser, fieldProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, fieldProperty.getContextAnnotations());
        assertEquals(field, fieldProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Object value = new Object();
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);

        Field field = Mockito.mock(Field.class);
        Mockito.when(field.get(Mockito.any())).thenReturn(value);

        AnnotatedField annotatedField = Mockito.mock(AnnotatedField.class);
        Mockito.when(annotatedField.getAnnotated()).thenReturn(field);

        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
        fieldProperty.withValueDeserializer(deserializer);

        fieldProperty.deserializeAndSet(jsonParser, ctxt, instance);

        Mockito.verifyZeroInteractions(jsonParser);
        Mockito.verifyZeroInteractions(ctxt);
        Mockito.verify(field).set(instance, value);
        Mockito.verifyZeroInteractions(deserializer);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        Object instance = new Object();

        Object value = new Object();
        JsonDeserializer<?> deserializer = Mockito.mock(JsonDeserializer.class);

        Field field = Mockito.mock(Field.class);
        Mockito.when(field.get(Mockito.any())).thenReturn(value);

        AnnotatedField annotatedField = Mockito.mock(AnnotatedField.class);
        Mockito.when(annotatedField.getAnnotated()).thenReturn(field);

        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);
        fieldProperty.withValueDeserializer(deserializer);

        Object result = fieldProperty.deserializeSetAndReturn(jsonParser, ctxt, instance);

        Mockito.verifyZeroInteractions(jsonParser);
        Mockito.verifyZeroInteractions(ctxt);
        Mockito.verify(field).set(instance, value);
        Mockito.verifyZeroInteractions(deserializer);

        assertEquals(instance, result);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        Field field = Mockito.mock(Field.class);
        AnnotatedField annotatedField = Mockito.mock(AnnotatedField.class);
        Mockito.when(annotatedField.getAnnotated()).thenReturn(field);

        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);

        fieldProperty.set(instance, value);

        Mockito.verify(field).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        Field field = Mockito.mock(Field.class);
        AnnotatedField annotatedField = Mockito.mock(AnnotatedField.class);
        Mockito.when(annotatedField.getAnnotated()).thenReturn(field);

        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, annotatedField);

        Object result = fieldProperty.setAndReturn(instance, value);

        Mockito.verify(field).set(instance, value);

        assertEquals(instance, result);
    }

}