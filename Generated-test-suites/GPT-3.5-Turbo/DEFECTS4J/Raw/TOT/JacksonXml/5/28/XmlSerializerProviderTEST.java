package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Before;
import org.junit.Test;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setup() {
        XmlRootNameLookup xmlRootNameLookup = new XmlRootNameLookup();
        xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
    }

    @Test
    public void testCopy() {
        DefaultSerializerProvider result = xmlSerializerProvider.copy();
        assertNotNull(result);
        assertTrue(result instanceof XmlSerializerProvider);
    }

    @Test
    public void testCreateInstance() {
        DefaultSerializerProvider result = xmlSerializerProvider.createInstance(null, null);
        assertNotNull(result);
        assertTrue(result instanceof XmlSerializerProvider);
    }

    @Test
    public void testSerializeValue_NullValue() throws IOException {
        JsonGenerator generator = new XmlGenerator(null, 0, null, null, null);
        try {
            xmlSerializerProvider.serializeValue(generator, null);
        } catch (Exception ex) {
            assertEquals(JsonMappingException.class, ex.getClass());
        }
    }

    @Test
    public void testSerializeValue_AsArray() throws IOException {
        JsonGenerator generator = new XmlGenerator(null, 0, null, null, null);
        try {
            xmlSerializerProvider.serializeValue(generator, new Object[]{});
        } catch (Exception ex) {
            assertEquals(JsonMappingException.class, ex.getClass());
        }
    }

    @Test
    public void testSerializeValue() throws IOException {
        JsonGenerator generator = new XmlGenerator(null, 0, null, null, null);
        try {
            xmlSerializerProvider.serializeValue(generator, new Object());
        } catch (Exception ex) {
            assertEquals(JsonMappingException.class, ex.getClass());
        }
    }

    @Test
    public void testSerializeValueWithRootType_NullValue() throws IOException {
        JsonGenerator generator = new XmlGenerator(null, 0, null, null, null);
        try {
            xmlSerializerProvider.serializeValue(generator, null, null, null);
        } catch (Exception ex) {
            assertEquals(JsonMappingException.class, ex.getClass());
        }
    }

    @Test
    public void testSerializeValueWithRootType_AsArray() throws IOException {
        JsonGenerator generator = new XmlGenerator(null, 0, null, null, null);
        try {
            xmlSerializerProvider.serializeValue(generator, new Object[]{}, null, null);
        } catch (Exception ex) {
            assertEquals(JsonMappingException.class, ex.getClass());
        }
    }

    @Test
    public void testSerializeValueWithRootType() throws IOException {
        JsonGenerator generator = new XmlGenerator(null, 0, null, null, null);
        try {
            xmlSerializerProvider.serializeValue(generator, new Object(), null, null);
        } catch (Exception ex) {
            assertEquals(JsonMappingException.class, ex.getClass());
        }
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        JsonGenerator generator = new XmlGenerator(null, 0, null, null, null);
        try {
            xmlSerializerProvider._serializeXmlNull(generator);
        } catch (Exception ex) {
            assertEquals(JsonMappingException.class, ex.getClass());
        }
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator toXmlGenerator = new ToXmlGenerator(null, 0, null, null, null, null);
        QName rootName = new QName("test");
        try {
            xmlSerializerProvider._startRootArray(toXmlGenerator, rootName);
        } catch (Exception ex) {
            assertEquals(JsonMappingException.class, ex.getClass());
        }
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator toXmlGenerator = new ToXmlGenerator(null, 0, null, null, null, null);
        QName rootName = new QName("test");
        try {
            xmlSerializerProvider._initWithRootName(toXmlGenerator, rootName);
        } catch (Exception ex) {
            assertEquals(JsonMappingException.class, ex.getClass());
        }
    }

    @Test
    public void testRootNameFromConfig() throws IOException {
        PropertyName propertyName = new PropertyName("test");
        xmlSerializerProvider._config = new SerializationConfig(null, null, null, null, propertyName, null, null, null, null, null, null, null, null, null);
        try {
            xmlSerializerProvider._rootNameFromConfig();
        } catch (Exception ex) {
            assertEquals(JsonMappingException.class, ex.getClass());
        }
    }

    @Test
    public void testAsXmlGenerator() throws IOException {
        JsonGenerator jsonGenerator = new JsonGenerator(null, 0, null, null, null, null, null);
        try {
            xmlSerializerProvider._asXmlGenerator(jsonGenerator);
        } catch (Exception ex) {
            assertEquals(JsonMappingException.class, ex.getClass());
        }
    }

    @Test
    public void testWrapAsIOE() throws IOException {
        JsonGenerator generator = new XmlGenerator(null, 0, null, null, null);
        Exception exception = new Exception();
        try {
            xmlSerializerProvider._wrapAsIOE(generator, exception);
        } catch (Exception ex) {
            assertEquals(IOException.class, ex.getClass());
        }
    }

}