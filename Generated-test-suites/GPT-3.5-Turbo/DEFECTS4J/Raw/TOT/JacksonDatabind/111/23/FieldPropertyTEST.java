
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class FieldPropertyTest {

    @Mock
    private BeanPropertyDefinition mockBeanPropertyDef;

    @Mock
    private JavaType mockJavaType;

    @Mock
    private TypeDeserializer mockTypeDeserializer;

    @Mock
    private Annotations mockAnnotations;

    @Mock
    private AnnotatedField mockAnnotatedField;

    @Mock
    private Field mockField;

    @Mock
    private JsonDeserializer<?> mockDeserializer;

    @Mock
    private NullValueProvider mockNullValueProvider;

    @Mock
    private JsonParser mockJsonParser;

    @Mock
    private DeserializationContext mockDeserializationContext;

    @Test
    public void testConstructor() throws Exception {
        MockitoAnnotations.initMocks(this);

        FieldProperty fieldProperty = new FieldProperty(mockBeanPropertyDef, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);

        assertEquals(mockBeanPropertyDef, fieldProperty.getMember());
        assertEquals(mockJavaType, fieldProperty.getType());
        assertEquals(mockTypeDeserializer, fieldProperty.getTypeDeserializer());
        assertEquals(mockAnnotations, fieldProperty.getMember().getAnnotations());
        assertEquals(mockAnnotatedField, fieldProperty.getMember());
        assertEquals(mockField, fieldProperty.getMember().getAnnotated());
    }

    @Test
    public void testWithNullProvider() throws Exception {
        MockitoAnnotations.initMocks(this);

        FieldProperty fieldProperty = new FieldProperty(mockBeanPropertyDef, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
        SettableBeanProperty result = fieldProperty.withNullProvider(mockNullValueProvider);

        assertEquals(mockNullValueProvider, result.getNullValueProviderInstance());
        assertEquals(mockNullValueProvider.getNullValue(mockDeserializationContext), result.getNullValue(mockDeserializationContext));
    }

    @Test
    public void testFixAccess() throws Exception {
        MockitoAnnotations.initMocks(this);

        FieldProperty fieldProperty = new FieldProperty(mockBeanPropertyDef, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
        fieldProperty.fixAccess(mockDeserializationConfig);

        verify(mockField, times(1)).setAccessible(true);
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws Exception {
        MockitoAnnotations.initMocks(this);

        FieldProperty fieldProperty = new FieldProperty(mockBeanPropertyDef, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
        fieldProperty.withNullProvider(mockNullValueProvider);

        when(mockJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        fieldProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, null);

        verify(mockNullValueProvider, times(1)).getNullValue(mockDeserializationContext);
    }

    @Test
    public void testDeserializeAndSet_ValueDeserializer() throws Exception {
        MockitoAnnotations.initMocks(this);

        FieldProperty fieldProperty = new FieldProperty(mockBeanPropertyDef, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
        fieldProperty.withNullProvider(mockNullValueProvider);
        fieldProperty.withValueDeserializer(mockDeserializer);

        when(mockJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(mockDeserializer.deserialize(mockJsonParser, mockDeserializationContext)).thenReturn("test");
        when(mockDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer)).thenReturn("test");
        fieldProperty.deserializeAndSet(mockJsonParser, mockDeserializationContext, null);

        verify(mockField, times(1)).set(any(), eq("test"));
    }

    @Test
    public void testDeserializeSetAndReturn_NullValue() throws Exception {
        MockitoAnnotations.initMocks(this);

        FieldProperty fieldProperty = new FieldProperty(mockBeanPropertyDef, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
        fieldProperty.withNullProvider(mockNullValueProvider);

        when(mockJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object result = fieldProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, null);

        verify(mockNullValueProvider, times(1)).getNullValue(mockDeserializationContext);
        assertEquals(null, result);
    }

    @Test
    public void testDeserializeSetAndReturn_ValueDeserializer() throws Exception {
        MockitoAnnotations.initMocks(this);

        FieldProperty fieldProperty = new FieldProperty(mockBeanPropertyDef, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
        fieldProperty.withNullProvider(mockNullValueProvider);
        fieldProperty.withValueDeserializer(mockDeserializer);

        when(mockJsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(mockDeserializer.deserialize(mockJsonParser, mockDeserializationContext)).thenReturn("test");
        when(mockDeserializer.deserializeWithType(mockJsonParser, mockDeserializationContext, mockTypeDeserializer)).thenReturn("test");

        Object result = fieldProperty.deserializeSetAndReturn(mockJsonParser, mockDeserializationContext, null);

        verify(mockField, times(1)).set(any(), eq("test"));
        assertEquals(null, result);
    }

    @Test
    public void testSet() throws Exception {
        MockitoAnnotations.initMocks(this);

        FieldProperty fieldProperty = new FieldProperty(mockBeanPropertyDef, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
        fieldProperty.set(new Object(), "test");

        verify(mockField, times(1)).set(any(), eq("test"));
    }

    @Test
    public void testSetAndReturn() throws Exception {
        MockitoAnnotations.initMocks(this);

        FieldProperty fieldProperty = new FieldProperty(mockBeanPropertyDef, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);

        Object instance = new Object();
        Object result = fieldProperty.setAndReturn(instance, "test");

        verify(mockField, times(1)).set(any(), eq("test"));
        assertEquals(instance, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_MissingField() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(mockAnnotatedField.getAnnotated()).thenReturn(null);

        new FieldProperty(mockBeanPropertyDef, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
    }

    @Test
    public void testReadResolve() throws Exception {
        MockitoAnnotations.initMocks(this);

        FieldProperty fieldProperty = new FieldProperty(mockBeanPropertyDef, mockJavaType, mockTypeDeserializer, mockAnnotations, mockAnnotatedField);
        FieldProperty result = (FieldProperty) fieldProperty.readResolve();

        assertEquals(fieldProperty.getMember(), result.getMember());
    }
}

