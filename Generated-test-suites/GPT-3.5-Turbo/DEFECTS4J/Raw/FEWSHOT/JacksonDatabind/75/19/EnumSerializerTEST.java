package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

import org.junit.Test;

@JacksonStdImpl
public class EnumSerializerTest {
    @Test
    public void testConstructor() {
        EnumValues enumValues = new EnumValues();
        Boolean serializeAsIndex = true;
        EnumSerializer serializer = new EnumSerializer(enumValues, serializeAsIndex);

        assertEquals(enumValues, serializer.getEnumValues());
        assertEquals(serializeAsIndex, serializer._serializeAsIndex);
    }

    @Test
    public void testConstruct() {
        Class<?> enumClass = Enum.class;
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        BeanDescription beanDesc = new BeanDescription() {
            @Override
            public Class<?> getBeanClass() {
                return enumClass;
            }
        };
        JsonFormat.Value format = null;
        EnumSerializer serializer = EnumSerializer.construct(enumClass, config, beanDesc, format);

        assertTrue(serializer instanceof EnumSerializer);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        BeanProperty property = null;
        EnumSerializer serializer = new EnumSerializer(new EnumValues(), true);
        JsonSerializer<?> newSerializer = serializer.createContextual(serializers, property);

        assertEquals(serializer, newSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(), true);
        JsonGenerator gen = null;
        SerializerProvider serializers = null;

        // TODO: Add test cases for serialize method
    }

    @Test
    public void testGetSchema() {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(), true);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();
        Type typeHint = null;

        // TODO: Add test cases for getSchema method
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(), true);
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializerProvider().visitor();
        JavaType typeHint = null;
        
        // TODO: Add test casess for acceptJsonFormatVisitor method
    }

    @Test
    public void testSerializeAsIndex() {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(), true);
        SerializerProvider serializers = null;

        // TODO: Add test cases for _serializeAsIndex method
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_isShapeWrittenUsingIndex_ThrowsIllegalArgumentException() {
        Class<?> enumClass = Enum.class;
        JsonFormat.Value format = new JsonFormat.Value();
        boolean fromClass = true;

        EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, fromClass);
    }
}