import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.deser.impl.NullsConstantProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class FieldPropertyTest {
    
    private FieldProperty fieldProperty;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedField field;
    private Field annotated;
    
    @Before
    public void setup() {
        propDef = mock(BeanPropertyDefinition.class);
        type = mock(JavaType.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        field = mock(AnnotatedField.class);
        annotated = mock(Field.class);
        
        fieldProperty = new FieldProperty(propDef, type, typeDeser, contextAnnotations, field);
    }
    
    @Test
    public void testConstructorAndGetters() {
        assertEquals(propDef, fieldProperty.getMember());
        assertEquals(field, fieldProperty.getMember());
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        fieldProperty.deserializeAndSet(p, ctxt, instance);
        Mockito.verify(p, Mockito.times(1)).hasToken(Mockito.any(JsonToken.class));
        Mockito.verify(p, Mockito.times(1)).hasToken(JsonToken.VALUE_NULL);
        Mockito.verify(p, Mockito.times(0)).isNull();
        Mockito.verify(ctxt, Mockito.times(0)).isEnabled(Mockito.any());
        Mockito.verify(ctxt, Mockito.times(0)).reader();
        Mockito.verify(ctxt, Mockito.times(0)).readString();
        Mockito.verify(ctxt, Mockito.times(0)).readValue(p, Object.class);
        Mockito.verify(p, Mockito.times(0)).skipChildren();
        Mockito.verify(p, Mockito.times(0)).getCurrentToken();
        Mockito.verify(p, Mockito.times(0)).nextToken();
        Mockito.verify(field, Mockito.times(1)).set(instance, value);
        Mockito.verify(field, Mockito.times(0)).getDeclaringClass();
        Mockito.verify(field, Mockito.times(0)).getName();
        Mockito.verify(field, Mockito.times(0)).getType();
        Mockito.verify(field, Mockito.times(0)).toGenericString();
        Mockito.verify(field, Mockito.times(0)).hashCode();
    }
    
    @Test
    public void testDeserializeAndSetWithNullValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Mockito.when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        fieldProperty.deserializeAndSet(p, ctxt, instance);
        Mockito.verify(p, Mockito.times(1)).hasToken(Mockito.any(JsonToken.class));
        Mockito.verify(p, Mockito.times(1)).hasToken(JsonToken.VALUE_NULL);
        Mockito.verify(field, Mockito.times(0)).set(Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        fieldProperty.setValueDeserializer(mock(JsonDeserializer.class));
        Mockito.when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        Mockito.when(field.getDeclaringClass()).thenReturn(Object.class);
        Mockito.when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(ctxt.readValue(p, Object.class)).thenReturn(value);
        fieldProperty.deserializeAndSet(p, ctxt, instance);
        Mockito.verify(p, Mockito.times(1)).hasToken(Mockito.any(JsonToken.class));
        Mockito.verify(p, Mockito.times(1)).hasToken(JsonToken.VALUE_NULL);
        Mockito.verify(field, Mockito.times(1)).set(instance, value);
    }
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        Object result = fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(instance, result);
        Mockito.verify(p, Mockito.times(1)).hasToken(Mockito.any(JsonToken.class));
        Mockito.verify(p, Mockito.times(1)).hasToken(JsonToken.VALUE_NULL);
        Mockito.verify(p, Mockito.times(0)).isNull();
        Mockito.verify(ctxt, Mockito.times(0)).isEnabled(Mockito.any());
        Mockito.verify(ctxt, Mockito.times(0)).reader();
        Mockito.verify(ctxt, Mockito.times(0)).readString();
        Mockito.verify(ctxt, Mockito.times(0)).readValue(p, Object.class);
        Mockito.verify(p, Mockito.times(0)).skipChildren();
        Mockito.verify(p, Mockito.times(0)).getCurrentToken();
        Mockito.verify(p, Mockito.times(0)).nextToken();
        Mockito.verify(field, Mockito.times(1)).set(instance, value);
    }
    
    @Test
    public void testDeserializeSetAndReturnWithNullValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Mockito.when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        Object result = fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(instance, result);
        Mockito.verify(p, Mockito.times(1)).hasToken(Mockito.any(JsonToken.class));
        Mockito.verify(p, Mockito.times(1)).hasToken(JsonToken.VALUE_NULL);
        Mockito.verify(field, Mockito.times(0)).set(Mockito.any(), Mockito.any());
    }
    
    @Test
    public void testDeserializeSetAndReturnWithTypeDeserializer() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        fieldProperty.setValueDeserializer(mock(JsonDeserializer.class));
        Mockito.when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        Mockito.when(field.getDeclaringClass()).thenReturn(Object.class);
        Mockito.when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(ctxt.readValue(p, Object.class)).thenReturn(value);
        Object result = fieldProperty.deserializeSetAndReturn(p, ctxt, instance);
        assertEquals(instance, result);
        Mockito.verify(p, Mockito.times(1)).hasToken(Mockito.any(JsonToken.class));
        Mockito.verify(p, Mockito.times(1)).hasToken(JsonToken.VALUE_NULL);
        Mockito.verify(field, Mockito.times(1)).set(instance, value);
    }
    
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        fieldProperty.set(instance, value);
        Mockito.verify(field, Mockito.times(1)).set(instance, value);
    }
    
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = fieldProperty.setAndReturn(instance, value);
        assertEquals(instance, result);
        Mockito.verify(field, Mockito.times(1)).set(instance, value);
    }

}