
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import com.fasterxml.jackson.core.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

@JacksonStdImpl
public class EnumSerializerTest {
    
    @Test
    public void testSerialize() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), true);
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(System.out);
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        try {
            enumSerializer.serialize(TestEnum.FIRST, jsonGenerator, serializers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testCreateContextual() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), true);
        BeanDescription beanDescription = new ObjectMapper().getSerializationConfig().introspect(TestEnum.class);
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        try {
            enumSerializer.createContextual(serializers, beanDescription.findProperty("name"));
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testGetSchema() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), true);
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        try {
            enumSerializer.getSchema(serializers, TestEnum.class);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), true);
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializerProvider().getProvider().getFormatVisitor(TestEnum.class, null);
        try {
            enumSerializer.acceptJsonFormatVisitor(visitor, TestEnum.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }
    
    public enum TestEnum {
        FIRST,
        SECOND,
        THIRD
    }
}

