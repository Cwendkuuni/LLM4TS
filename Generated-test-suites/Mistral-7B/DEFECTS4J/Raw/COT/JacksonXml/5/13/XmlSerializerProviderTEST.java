package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.QName;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Test;

/**
 * Unit tests for {@link XmlSerializerProvider} class.
 */
public class XmlSerializerProviderTest {

    @Test
    public void testXmlSerializerProvider_constructor() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        assertNotNull(xmlSerializerProvider);
    }

    @Test
    public void testXmlSerializerProvider_copy() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider originalXmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        SerializedStringProperty nameProp = new SerializedStringProperty();
        JsonSerializer<Object> serializer = new JsonSerializer<Object>(nameProp) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
                gen.writeString(value.toString());
            }
        };
        originalXmlSerializerProvider.setSerializerInstance("test.StringSerializer", serializer);

        XmlSerializerProvider copiedXmlSerializerProvider = new XmlSerializerProvider(originalXmlSerializerProvider);
        JsonSerializer<Object> copiedSerializer = copiedXmlSerializerProvider.getSerializerInstance("test.StringSerializer");
        assertEquals(serializer, copiedSerializer);
    }

    @Test
    public void testSerializeValue_nullValue() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        TokenBuffer tokenBuffer = new TokenBuffer(StaxUtil.createWriter());
        xmlSerializerProvider.serializeValue(tokenBuffer, null);
        String xml = tokenBuffer.getPrettyValue();
        assertEquals("<null/>", xml);
    }

    @Test
    public void testSerializeValue_objectValue() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        String value = "testValue";
        TokenBuffer tokenBuffer = new TokenBuffer(StaxUtil.createWriter());
        xmlSerializerProvider.serializeValue(tokenBuffer, value);
        String xml = tokenBuffer.getPrettyValue();
        assertEquals("<testValue/>", xml);
    }

    @Test
    public void testSerializeValue_objectValueWithRootType() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JavaType rootType = TypeFactory.defaultInstance().constructType(String[].class);
        TokenBuffer tokenBuffer = new TokenBuffer(StaxUtil.createWriter());
        xmlSerializerProvider.serializeValue(tokenBuffer, "testValue", rootType, null);
        String xml = tokenBuffer.getPrettyValue();
        assertEquals("<item><testValue/></item>", xml);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValue_unsupportedSerializer() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        Object value = new Object();
        TokenBuffer tokenBuffer = new TokenBuffer(StaxUtil.createWriter());
        xmlSerializerProvider.serializeValue(tokenBuffer, value, null, null);
        fail("Expected JsonMappingException");
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValue_unsupportedType() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        int value = 42;
        TokenBuffer tokenBuffer = new TokenBuffer(StaxUtil.createWriter());
        xmlSerializerProvider.serializeValue(tokenBuffer, value, null, null);
        fail("Expected JsonMappingException");
    }

    @Test(expected = IOException.class)
    public void testSerializeValue_ioException() throws IOException {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        String value = "testValue";
        TokenBuffer tokenBuffer = new TokenBuffer(StaxUtil.createWriterWithException()) {
            @Override
            public void writeStartObject() throws XMLStreamException {
                throw new XMLStreamException("Test exception.");
            }
        };
        xmlSerializerProvider.serializeValue(tokenBuffer, value);
        fail("Expected IOException");
    }

}