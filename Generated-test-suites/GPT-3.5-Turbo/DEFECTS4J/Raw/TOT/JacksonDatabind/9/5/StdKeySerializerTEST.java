
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class StdKeySerializerTest {

    @Test
    public void testSerialize_DateValue_ShouldSerializeDateKey() {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        MockJsonGenerator jgen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        Date dateValue = new Date();

        // Act
        serializer.serialize(dateValue, jgen, provider);

        // Assert
        assertEquals(dateValue.toString(), jgen.getFieldName());
        assertEquals(0, jgen.getWrittenFieldsCount());
    }

    @Test
    public void testSerialize_NonDateValue_ShouldSerializeStringKey() {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        MockJsonGenerator jgen = new MockJsonGenerator();
        MockSerializerProvider provider = new MockSerializerProvider();
        Object value = new Object();

        // Act
        serializer.serialize(value, jgen, provider);

        // Assert
        assertEquals(value.toString(), jgen.getFieldName());
        assertEquals(0, jgen.getWrittenFieldsCount());
    }

    @Test
    public void testGetSchema_ShouldReturnStringSchemaNode() {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        MockSerializerProvider provider = new MockSerializerProvider();
        Type typeHint = Object.class;

        // Act
        JsonNode result = serializer.getSchema(provider, typeHint);

        // Assert
        assertNotNull(result);
        assertEquals("string", result.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_ShouldExpectStringFormat() {
        // Arrange
        StdKeySerializer serializer = new StdKeySerializer();
        MockJsonFormatVisitorWrapper visitor = new MockJsonFormatVisitorWrapper();
        JavaType typeHint = JavaType.builder().build();

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        assertTrue(visitor.isExpectingStringFormat());
    }
}

class MockJsonGenerator extends JsonGenerator {
    
    private String fieldName;
    private int writtenFieldsCount;

    // Constructors and other overridden methods omitted for brevity

    @Override
    public void writeFieldName(String name) throws IOException {
        fieldName = name;
        writtenFieldsCount++;
    }
    
    public String getFieldName() {
        return fieldName;
    }

    public int getWrittenFieldsCount() {
        return writtenFieldsCount;
    }
}

class MockSerializerProvider extends SerializerProvider {
    
    // Constructors and other overridden methods omitted for brevity
}

class MockJsonFormatVisitorWrapper extends JsonFormatVisitorWrapper {
    
    private boolean expectingStringFormat;

    // Constructors and other overridden methods omitted for brevity

    @Override
    public void expectStringFormat(JavaType javaType) throws JsonMappingException {
        expectingStringFormat = true;
    }
    
    public boolean isExpectingStringFormat() {
        return expectingStringFormat;
    }
}
