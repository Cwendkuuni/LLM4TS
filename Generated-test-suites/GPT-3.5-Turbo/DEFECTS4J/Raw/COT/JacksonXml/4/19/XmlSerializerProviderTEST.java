
package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue_NullValue() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        TestXmlGenerator generator = new TestXmlGenerator();
        
        // Act
        provider.serializeValue(generator, null);
        
        // Assert
        assertEquals("<null>", generator.getOutput());
    }
    
    @Test
    public void testSerializeValue_WithRootName() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        TestXmlGenerator generator = new TestXmlGenerator();
        provider._rootNameLookup = new XmlRootNameLookup();
        provider._rootNameLookup.addRootName(String.class, new QName("http://example.com", "root"));
        
        // Act
        provider.serializeValue(generator, "test");
        
        // Assert
        assertEquals("<root xmlns=\"http://example.com\">test</root>", generator.getOutput());
    }
    
    @Test
    public void testSerializeValue_WithRootNameAndArray() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        TestXmlGenerator generator = new TestXmlGenerator();
        provider._rootNameLookup = new XmlRootNameLookup();
        provider._rootNameLookup.addRootName(String[].class, new QName("http://example.com", "array"));
        
        // Act
        provider.serializeValue(generator, new String[]{"test1", "test2"});
        
        // Assert
        assertEquals("<array xmlns=\"http://example.com\"><item>test1</item><item>test2</item></array>", generator.getOutput());
    }
    
    @Test
    public void testSerializeValue_WithRootNameAndNullArray() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        TestXmlGenerator generator = new TestXmlGenerator();
        provider._rootNameLookup = new XmlRootNameLookup();
        provider._rootNameLookup.addRootName(String[].class, new QName("http://example.com", "array"));
        
        // Act
        provider.serializeValue(generator, null);
        
        // Assert
        assertEquals("<null>", generator.getOutput());
    }
    
    @Test
    public void testSerializeValue_WithRootType() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        TestXmlGenerator generator = new TestXmlGenerator();
        provider._rootNameLookup = new XmlRootNameLookup();
        provider._rootNameLookup.addRootName(String.class, new QName("http://example.com", "root"));
        
        // Act
        provider.serializeValue(generator, "test", String.class);
        
        // Assert
        assertEquals("<root xmlns=\"http://example.com\">test</root>", generator.getOutput());
    }
    
    @Test
    public void testSerializeValue_WithRootTypeAndArray() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        TestXmlGenerator generator = new TestXmlGenerator();
        provider._rootNameLookup = new XmlRootNameLookup();
        provider._rootNameLookup.addRootName(String[].class, new QName("http://example.com", "array"));
        
        // Act
        provider.serializeValue(generator, new String[]{"test1", "test2"}, String[].class);
        
        // Assert
        assertEquals("<array xmlns=\"http://example.com\"><item>test1</item><item>test2</item></array>", generator.getOutput());
    }
    
    @Test
    public void testSerializeValue_WithRootTypeAndNullArray() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        TestXmlGenerator generator = new TestXmlGenerator();
        provider._rootNameLookup = new XmlRootNameLookup();
        provider._rootNameLookup.addRootName(String[].class, new QName("http://example.com", "array"));
        
        // Act
        provider.serializeValue(generator, null, String[].class);
        
        // Assert
        assertEquals("<null>", generator.getOutput());
    }
    
    @Test
    public void testSerializeValue_WithRootTypeAndCustomSerializer() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        TestXmlGenerator generator = new TestXmlGenerator();
        provider._rootNameLookup = new XmlRootNameLookup();
        provider._rootNameLookup.addRootName(String.class, new QName("http://example.com", "root"));
        
        // Act
        provider.serializeValue(generator, "test", String.class, new CustomSerializer());
        
        // Assert
        assertEquals("<root xmlns=\"http://example.com\">custom_test</root>", generator.getOutput());
    }
    
    private class TestXmlGenerator extends com.fasterxml.jackson.dataformat.xml.util.ToXmlGenerator {
        
        private StringBuilder output;

        public TestXmlGenerator() {
            super(new com.fasterxml.jackson.core.io.IOContext(null, false, null));
            output = new StringBuilder();
        }
        
        @Override
        public void writeStartObject() {
            output.append("{");
        }
        
        @Override
        public void writeEndObject() {
            output.append("}");
        }
        
        @Override
        public void writeFieldName(String name) {
            output.append("\"").append(name).append("\":");
        }
        
        @Override
        public void writeString(String text) {
            output.append("\"").append(text).append("\"");
        }
        
        @Override
        public void writeStartArray() {
            output.append("[");
        }
        
        @Override
        public void writeEndArray() {
            output.append("]");
        }
        
        public String getOutput() {
            return output.toString();
        }
    }
    
    private class CustomSerializer extends com.fasterxml.jackson.databind.JsonSerializer<String> {
        
        @Override
        public void serialize(String value, com.fasterxml.jackson.core.JsonGenerator gen, com.fasterxml.jackson.databind.SerializerProvider serializers) throws java.io.IOException {
            gen.writeString("custom_" + value);
        }
    }
}
