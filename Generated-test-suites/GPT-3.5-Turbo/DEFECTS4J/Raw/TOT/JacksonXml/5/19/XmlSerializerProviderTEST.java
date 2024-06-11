
package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import java.io.IOException;
import javax.xml.namespace.QName;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setUp() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        xmlSerializerProvider = new XmlSerializerProvider(rootNames);
    }

    @Test
    public void testSerializeValue_WhenValueIsNull_ShouldSerializeXmlNull() throws IOException {
        // Arrange
        StubJsonGenerator stubJsonGenerator = new StubJsonGenerator();
        
        // Act
        xmlSerializerProvider.serializeValue(stubJsonGenerator, null);
        
        // Assert
        assertEquals("null", stubJsonGenerator.getXmlResult());
    }

    @Test
    public void testSerializeValue_WhenValueIsNotNull_ShouldSerializeValueAsXml() throws IOException {
        // Arrange
        StubJsonGenerator stubJsonGenerator = new StubJsonGenerator();
        StubSerializer stubSerializer = new StubSerializer();
        
        // Act
        xmlSerializerProvider.serializeValue(stubJsonGenerator, "test", null, stubSerializer);
        
        // Assert
        assertEquals("<root>test</root>", stubJsonGenerator.getXmlResult());
    }

    @Test
    public void testSerializeValue_WhenConfigHasRootName_ShouldUseRootNameFromConfig() throws IOException {
        // Arrange
        StubJsonGenerator stubJsonGenerator = new StubJsonGenerator();
        StubSerializer stubSerializer = new StubSerializer();
        xmlSerializerProvider.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        
        // Act
        xmlSerializerProvider.serializeValue(stubJsonGenerator, "test", null, stubSerializer);
        
        // Assert
        assertEquals("<test>test</test>", stubJsonGenerator.getXmlResult());
    }

    @Test
    public void testSerializeValue_WhenCalledByConvertValue_ShouldNotUseRootName() throws IOException {
        // Arrange
        StubJsonGenerator stubJsonGenerator = new StubJsonGenerator();
        xmlSerializerProvider.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        
        // Act
        xmlSerializerProvider.serializeValue(stubJsonGenerator, "test");
        
        // Assert
        assertEquals("<null>test</null>", stubJsonGenerator.getXmlResult());
    }

    @Test
    public void testSerializeValue_WhenValueIsArray_ShouldSerializeArrayValueAsXml() throws IOException {
        // Arrange
        StubJsonGenerator stubJsonGenerator = new StubJsonGenerator();
        StubSerializer stubSerializer = new StubSerializer();
        
        // Act
        xmlSerializerProvider.serializeValue(stubJsonGenerator, new String[] { "test1", "test2" }, null, stubSerializer);
        
        // Assert
        assertEquals("<root><item>test1</item><item>test2</item></root>", stubJsonGenerator.getXmlResult());
    }

    private static class StubJsonGenerator extends JsonGenerator {
        private StringBuilder xmlResult = new StringBuilder();

        public String getXmlResult() {
            return xmlResult.toString();
        }

        @Override
        public void writeStartObject() throws IOException {}

        @Override
        public void writeEndObject() throws IOException {}

        @Override
        public void writeFieldName(String name) throws IOException {}

        @Override
        public void writeEndArray() throws IOException {}

        @Override
        public void writeBoolean(boolean value) throws IOException {}

        @Override
        public void writeNull() throws IOException {}

        @Override
        public void writeString(String text) throws IOException {
            xmlResult.append(text);
        }

        // Other overridden methods
    }
    
    private static class StubSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString("root");
            gen.writeString(value.toString());
        }
    }
}
