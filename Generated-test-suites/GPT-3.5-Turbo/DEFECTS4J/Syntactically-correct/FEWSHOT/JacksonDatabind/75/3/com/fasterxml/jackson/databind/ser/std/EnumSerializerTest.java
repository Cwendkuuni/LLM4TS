package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.ContextualSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.util.EnumValues;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(MockitoJUnitRunner.class)
public class EnumSerializerTest {
    @Mock
    private EnumValues enumValues;

    @Mock
    private BeanProperty beanProperty;

    @Mock
    private JsonFormat.Value format;

    @Mock
    private Enum<?> en;

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider serializers;

    private EnumSerializer enumSerializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        enumSerializer = new EnumSerializer(enumValues, Boolean.TRUE);
    }

    @Test
    public void testConstruct() {
        EnumSerializer constructedSerializer = EnumSerializer.construct(Enum.class, mock(SerializationConfig.class),
                mock(BeanDescription.class), format);
        assertEquals(enumValues, constructedSerializer.getEnumValues());
    }

    @Test
    public void testCreateContextual() throws Exception {
        enumSerializer.createContextual(serializers, beanProperty);
        verify(beanProperty, times(2)).getType();
    }

    @Test
    public void testSerialize() throws Exception {
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(Boolean.FALSE);
        when(enumValues.serializedValueFor(en)).thenReturn("value");
        enumSerializer.serialize(en, gen, serializers);
        verify(enumValues).serializedValueFor(en);
        verify(gen).writeString("value");
    }

    @Test
    public void testSerializeAsIndex() throws Exception {
        when(serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)).thenReturn(Boolean.TRUE);
        enumSerializer.serialize(en, gen, serializers);
        verify(serializers).isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        verify(gen).writeNumber(en.ordinal());
    }

    @Test
    public void testGetSchema() throws Exception {
        assertEquals(ObjectNode.class, enumSerializer.getSchema(serializers, null).getClass());
    }

    @Test
    public void testGetSchemaWithEnumType() throws Exception {
        when(serializers.constructType(any(Type.class))).thenReturn(mock(JavaType.class));
        assertEquals(ObjectNode.class, enumSerializer.getSchema(serializers, mock(Type.class)).getClass());
    }

    @Test
    public void testGetSchemaWithSerializeAsIndex() throws Exception {
        enumSerializer = new EnumSerializer(enumValues, Boolean.TRUE);
        assertEquals(ObjectNode.class, enumSerializer.getSchema(serializers, null).getClass());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).getProvider();
        verify(serializers).isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        verify(visitor).expectStringFormat(typeHint);
    }
}