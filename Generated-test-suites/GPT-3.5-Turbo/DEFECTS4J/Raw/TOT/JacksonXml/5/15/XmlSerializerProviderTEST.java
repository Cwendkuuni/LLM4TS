
package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;


public class XmlSerializerProviderTest {

    private XmlSerializerProvider serializerProvider;

    @Before
    public void setup() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        serializerProvider = new XmlSerializerProvider(rootNames);
    }

    @Test
    public void testConstructor() {
        assertNotNull(serializerProvider);
    }

    @Test
    public void testCopy() {
        DefaultSerializerProvider copyProvider = serializerProvider.copy();
        assertNotNull(copyProvider);
        assertTrue(serializerProvider != copyProvider);
        assertEquals(serializerProvider.getClass(), copyProvider.getClass());
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = new SerializationConfig(null, null, null, null, null, null, null);
        SerializerFactory factory = new SerializerFactory();
        DefaultSerializerProvider instanceProvider = serializerProvider.createInstance(config, factory);
        assertNotNull(instanceProvider);
        assertTrue(serializerProvider != instanceProvider);
        assertEquals(serializerProvider.getClass(), instanceProvider.getClass());
    }

    @Test
    public void testSerializeValue_NullValue() {
        try {
            JsonGenerator generator = new JsonGenerator();
            serializerProvider.serializeValue(generator, null);
            // Validate the serialized value is "null"
            assertEquals("null", generator.getValue());
        } catch (IOException e) {
            fail("Unexpected IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeValue() {
        try {
            // Test with non-array value
            Object nonArrayValue = "testValue";
            JsonGenerator nonArrayGenerator = new JsonGenerator();
            serializerProvider.serializeValue(nonArrayGenerator, nonArrayValue);
            // Validate the serialized value is equal to nonArrayValue
            assertEquals(nonArrayValue, nonArrayGenerator.getValue());

            // Test with array value
            List<String> arrayValue = new ArrayList<>();
            arrayValue.add("item1");
            arrayValue.add("item2");
            JsonGenerator arrayGenerator = new JsonGenerator();
            serializerProvider.serializeValue(arrayGenerator, arrayValue);
            // Validate the serialized value is equal to arrayValue
            assertEquals(arrayValue.toString(), arrayGenerator.getValue());
        } catch (IOException e) {
            fail("Unexpected IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSerializeValue_WithRootType() {
        try {
            // Test with non-array value
            Object nonArrayValue = "testValue";
            JavaType nonArrayType = new JavaType();
            JsonSerializer<Object> serializer = new JsonSerializer<>();
            JsonGenerator nonArrayGenerator = new JsonGenerator();
            serializerProvider.serializeValue(nonArrayGenerator, nonArrayValue, nonArrayType, serializer);
            // Validate the serialized value is equal to nonArrayValue
            assertEquals(nonArrayValue, nonArrayGenerator.getValue());

            // Test with array value
            List<String> arrayValue = new ArrayList<>();
            arrayValue.add("item1");
            arrayValue.add("item2");
            JavaType arrayType = new JavaType();
            JsonGenerator arrayGenerator = new JsonGenerator();
            serializerProvider.serializeValue(arrayGenerator, arrayValue, arrayType, serializer);
            // Validate the serialized value is equal to arrayValue
            assertEquals(arrayValue.toString(), arrayGenerator.getValue());
        } catch (IOException e) {
            fail("Unexpected IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testStartRootArray() {
        try {
            ToXmlGenerator xgen = new ToXmlGenerator();
            QName rootName = new QName("root");
            serializerProvider.startRootArray(xgen, rootName);
            // Validate that the root array is properly started
            assertEquals("<root>", xgen.getStartTag());
            // Validate that the field name is "item"
            assertEquals("item", xgen.getFieldName());
        } catch (IOException e) {
            fail("Unexpected IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInitWithRootName() {
        try {
            ToXmlGenerator xgen = new ToXmlGenerator();
            QName rootName = new QName("root");
            serializerProvider.initWithRootName(xgen, rootName);
            // Validate that the root name is properly initialized
            assertEquals(rootName, xgen.getRootName());
        } catch (IOException e) {
            fail("Unexpected IOException thrown: " + e.getMessage());
        }
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName propertyName = new PropertyName("testName");
        assertNotNull(serializerProvider);
        assertEquals(propertyName.getSimpleName(), serializerProvider.rootNameFromConfig().getLocalPart());
    }

    @Test
    public void testAsXmlGenerator() {
        ToXmlGenerator toXmlGenerator = new ToXmlGenerator();
        JsonGenerator jsonGenerator = new TokenBuffer();
        assertNotNull(serializerProvider);
        assertTrue(serializerProvider.asXmlGenerator(toXmlGenerator) != null);
        assertTrue(serializerProvider.asXmlGenerator(jsonGenerator) == null);
    }

    @Test
    public void testWrapAsIOE() {
        try {
            JsonGenerator generator = new JsonGenerator();
            Exception exception = new Exception("Test exception");
            JsonMappingException jsonMappingException = serializerProvider.wrapAsIOE(generator, exception);
            assertNotNull(jsonMappingException);
            assertEquals(exception.getMessage(), jsonMappingException.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException thrown: " + e.getMessage());
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGenerator_InvalidGenerator() throws JsonMappingException {
        JsonGenerator jsonGenerator = new JsonGenerator();
        serializerProvider.asXmlGenerator(jsonGenerator);
    }

    @Test(expected = IOException.class)
    public void testWrapAsIOE_IOException() throws IOException {
        JsonGenerator generator = new JsonGenerator();
        IOException ioException = new IOException("Test IOException");
        throw serializerProvider.wrapAsIOE(generator, ioException);
    }
}
