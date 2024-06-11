
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import java.lang.reflect.Field;

public class FieldPropertyTest {

    @Test
    public void testDeserializeAndSet_WithNullValue_SkipNullsTrue() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);
        
        Field field = getField(instance.getClass(), "_field");
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);
        
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        AnnotationMap annotationMap = mock(AnnotationMap.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getFullName()).thenReturn(new PropertyName("_annotated"));
        
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeserializer, annotationMap, annotatedField);
        fieldProperty._skipNulls = true;
        fieldProperty.deserializeAndSet(parser, deserializationContext, instance);
        
        verify(field, never()).set(eq(instance), any());
        verify(nullValueProvider, never()).getNullValue(deserializationContext);
    }

    @Test
    public void testDeserializeAndSet_WithNullValue_SkipNullsFalse() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);
        
        Field field = getField(instance.getClass(), "_field");
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);
        
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        AnnotationMap annotationMap = mock(AnnotationMap.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getFullName()).thenReturn(new PropertyName("_annotated"));
        
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeserializer, annotationMap, annotatedField);
        fieldProperty._skipNulls = false;
        fieldProperty.deserializeAndSet(parser, deserializationContext, instance);
        
        verify(field, never()).set(eq(instance), any());
        verify(nullValueProvider).getNullValue(deserializationContext);
    }

    @Test
    public void testDeserializeAndSet_WithNotNullValue() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        
        Field field = getField(instance.getClass(), "_field");
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);
        
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        AnnotationMap annotationMap = mock(AnnotationMap.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getFullName()).thenReturn(new PropertyName("_annotated"));
        
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeserializer, annotationMap, annotatedField);
        fieldProperty._valueDeserializer = valueDeserializer;
        fieldProperty.deserializeAndSet(parser, deserializationContext, instance);
        
        verify(valueDeserializer).deserialize(parser, deserializationContext);
        verify(field).set(eq(instance), eq(value));
        verify(nullValueProvider, never()).getNullValue(deserializationContext);
    }

    @Test
    public void testDeserializeSetAndReturn_WithNullValue_SkipNullsTrue() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);
        
        Field field = getField(instance.getClass(), "_field");
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);
        
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        AnnotationMap annotationMap = mock(AnnotationMap.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getFullName()).thenReturn(new PropertyName("_annotated"));
        
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeserializer, annotationMap, annotatedField);
        fieldProperty._skipNulls = true;
        Object result = fieldProperty.deserializeSetAndReturn(parser, deserializationContext, instance);
        
        assertEquals(instance, result);
        verify(field, never()).set(eq(instance), any());
        verify(nullValueProvider, never()).getNullValue(deserializationContext);
    }

    @Test
    public void testDeserializeSetAndReturn_WithNullValue_SkipNullsFalse() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);
        
        Field field = getField(instance.getClass(), "_field");
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);
        
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        AnnotationMap annotationMap = mock(AnnotationMap.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getFullName()).thenReturn(new PropertyName("_annotated"));
        
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeserializer, annotationMap, annotatedField);
        fieldProperty._skipNulls = false;
        Object result = fieldProperty.deserializeSetAndReturn(parser, deserializationContext, instance);
        
        assertEquals(instance, result);
        verify(field, never()).set(eq(instance), any());
        verify(nullValueProvider).getNullValue(deserializationContext);
    }

    @Test
    public void testDeserializeSetAndReturn_WithNotNullValue() throws Exception {
        JsonParser parser = mock(JsonParser.class);
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        
        Field field = getField(instance.getClass(), "_field");
        AnnotatedField annotatedField = mock(AnnotatedField.class);
        when(annotatedField.getAnnotated()).thenReturn(field);
        
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        AnnotationMap annotationMap = mock(AnnotationMap.class);
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        when(propDef.getFullName()).thenReturn(new PropertyName("_annotated"));
        
        FieldProperty fieldProperty = new FieldProperty(propDef, type, typeDeserializer, annotationMap, annotatedField);
        fieldProperty._valueDeserializer = valueDeserializer;
        Object result = fieldProperty.deserializeSetAndReturn(parser, deserializationContext, instance);
        
        assertEquals(instance, result);
        verify(valueDeserializer).deserialize(parser, deserializationContext);
        verify(field).set(eq(instance), eq(value));
        verify(nullValueProvider, never()).getNullValue(deserializationContext);
    }

    @Test
    public void testSet() throws Exception {
        Field field = mock(Field.class);
        Object instance = new Object();
        Object value = new Object();
        
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        fieldProperty._field = field;
        fieldProperty.set(instance, value);
        
        verify(field).set(eq(instance), eq(value));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Field field = mock(Field.class);
        Object instance = new Object();
        Object value = new Object();
        
        FieldProperty fieldProperty = new FieldProperty(null, null, null, null, null);
        fieldProperty._field = field;
        Object result = fieldProperty.setAndReturn(instance, value);
        
        assertEquals(instance, result);
        verify(field).set(eq(instance), eq(value));
    }

    private Field getField(Class<?> clazz, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            fail("Failed to get field: " + fieldName);
        }
        return field;
    }
}
