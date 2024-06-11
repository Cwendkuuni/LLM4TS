
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

public class EnumSerializerTest {
    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = JsonMapper.builder(new JsonFactory())
                .serializerProvider(new DefaultSerializerProvider.Impl())
                .build();
    }

    @Test
    public void testSerializeAsIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(EnumType.class), true);
        EnumType value = EnumType.FIRST;
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("0", json);
    }
    
    @Test
    public void testSerializeAsString() throws Exception {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(EnumType.class), false);
        EnumType value = EnumType.FIRST;
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("\"FIRST\"", json);
    }
    
    @Test
    public void testCreateContextual() throws Exception {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(EnumType.class), false);
        EnumSerializer contextualSerializer = (EnumSerializer) serializer.createContextual(null, null);
        Assert.assertSame(serializer, contextualSerializer);
    }
    
    @Test
    public void testSerializeEnumWithValueAsString() throws Exception {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(EnumTypeWithCustomValues.class), false);
        EnumTypeWithCustomValues value = EnumTypeWithCustomValues.FIRST;
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("\"A\"", json);
    }

    @Test
    public void testSerializeEnumWithValueAsIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(EnumTypeWithCustomValues.class), true);
        EnumTypeWithCustomValues value = EnumTypeWithCustomValues.FIRST;
        String json = mapper.writeValueAsString(value);
        Assert.assertEquals("0", json);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(EnumType.class), false);
        serializer.acceptJsonFormatVisitor(null, null);
        // Assertion for acceptJsonFormatVisitor
    }
    
    @Test
    public void testGetEnumValues() throws Exception {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(EnumType.class), false);
        EnumValues enumValues = serializer.getEnumValues();
        Assert.assertSame(enumValues, serializer._values);
    }
    
    @Test
    public void testSerializeNull() throws Exception {
        EnumSerializer serializer = new EnumSerializer(new EnumValues(EnumType.class), false);
        String json = mapper.writeValueAsString(null);
        Assert.assertEquals("null", json);
    }
    
    // Add tests for potential edge cases and exception handling scenarios here

}
