package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import javax.xml.stream.XMLStreamException;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import javax.xml.namespace.QName;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.core.JsonGenerator;
import java.io.IOException;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {
    @Test
    public void testCopy() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        DefaultSerializerProvider defaultSerializerProvider = new DefaultSerializerProvider(xmlSerializerProvider);
        DefaultSerializerProvider defaultSerializerProviderCopy = defaultSerializerProvider.copy();
        assertNotNull(defaultSerializerProviderCopy);
        assertNotSame(defaultSerializerProvider, defaultSerializerProviderCopy);
    }

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        DefaultSerializerProvider defaultSerializerProvider = new DefaultSerializerProvider();
        SerializerFactory serializerFactory = new SerializerFactory();
        SerializationConfig serializationConfig = new SerializationConfig();
        DefaultSerializerProvider defaultSerializerProviderInstance = defaultSerializerProvider.createInstance(serializationConfig, serializerFactory);
        assertNotNull(defaultSerializerProviderInstance);
    }

    @Test
    public void testSerializeValue() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        TokenBuffer tokenBuffer = new TokenBuffer(xmlSerializerProvider);
        assertNull(tokenBuffer.getCodec());
    }

    @Test
    public void testSerializeValueWithType() throws IOException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        TokenBuffer tokenBuffer = new TokenBuffer(xmlSerializerProvider);
        assertNull(tokenBuffer.getCodec());
    }
}