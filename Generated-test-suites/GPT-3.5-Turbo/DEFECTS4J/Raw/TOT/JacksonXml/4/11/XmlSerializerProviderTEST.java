
package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlSerializerProviderTest {
    
    @Test
    public void testSerializeValue_NullValue_SerializeXmlNull() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        DummyJsonGenerator dummyGenerator = new DummyJsonGenerator();
        Object value = null;

        // Act
        provider.serializeValue(dummyGenerator, value);

        // Assert
        assertEquals("<null>", dummyGenerator.getOutput());
    }

    @Test
    public void testSerializeValue_ClassValue_GeneratorCalledWithRootName() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        DummyJsonGenerator dummyGenerator = new DummyJsonGenerator();
        Object value = new MyClass();

        // Act
        provider.serializeValue(dummyGenerator, value);

        // Assert
        assertEquals("WithRootName", dummyGenerator.getOutput());
    }

    @Test
    public void testSerializeValue_NullValue_SerializeXmlNullWithType() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        DummyJsonGenerator dummyGenerator = new DummyJsonGenerator();
        Object value = null;

        // Act
        provider.serializeValue(dummyGenerator, value, MyClass.class);

        // Assert
        assertEquals("<null>", dummyGenerator.getOutput());
    }

    @Test
    public void testSerializeValue_ClassValue_GeneratorCalledWithRootNameAndType() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        DummyJsonGenerator dummyGenerator = new DummyJsonGenerator();
        Object value = new MyClass();

        // Act
        provider.serializeValue(dummyGenerator, value, MyClass.class);

        // Assert
        assertEquals("WithRootName", dummyGenerator.getOutput());
    }

    @Test
    public void testSerializeValue_NullValue_SerializeXmlNullWithTypeAndSerializer() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        DummyJsonGenerator dummyGenerator = new DummyJsonGenerator();
        Object value = null;
        DummyObjectSerializer serializer = new DummyObjectSerializer();

        // Act
        provider.serializeValue(dummyGenerator, value, MyClass.class, serializer);

        // Assert
        assertEquals("<null>", dummyGenerator.getOutput());
    }

    @Test
    public void testSerializeValue_ClassValue_GeneratorCalledWithRootNameAndTypeAndSerializer() {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        DummyJsonGenerator dummyGenerator = new DummyJsonGenerator();
        Object value = new MyClass();
        DummyObjectSerializer serializer = new DummyObjectSerializer();

        // Act
        provider.serializeValue(dummyGenerator, value, MyClass.class, serializer);

        // Assert
        assertEquals("WithRootName", dummyGenerator.getOutput());
    }

    private class MyClass {
    }

    private class DummyJsonGenerator extends JsonGenerator {
        private String output = "";

        public String getOutput() {
            return output;
        }

        @Override
        public void writeEndObject() throws IOException {}

        @Override
        public void writeFieldName(SerializableString name) throws IOException {}

        @Override
        public void writeFieldName(String name) throws IOException {}

        @Override
        public void writeObject(Object obj) throws IOException {
            output = obj.toString();
        }

        // Other overridden methods omitted for brevity
    }

    private class DummyObjectSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {}
    }
}

