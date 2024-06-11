package com.fasterxml.jackson.dataformat.xml.ser.test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Test;
import org.mockito.Mockito;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

    @Test
    public void testDefaultSerializerProviderCreation() {
        XmlRootNameLookup lookup = Mockito.mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(lookup);
        assertSame(lookup, provider._rootNameLookup);
    }

    @Test
    public void testMockSerializerProviderCreation() {
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        SerializerFactory factory = Mockito.mock(SerializerFactory.class);
        XmlSerializerProvider srcProvider = Mockito.mock(XmlSerializerProvider.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(srcProvider, config, factory);
        assertSame(srcProvider, provider._rootNameLookup);
    }

    @Test
    public void testSerializeValueCallsSerializeXmlNullIfValueIsNull() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Object value = null;
        XmlSerializerProvider provider = Mockito.mock(XmlSerializerProvider.class);
        provider._serializeXmlNull(generator);
        Mockito.verify(provider, Mockito.times(1)).serializeValue(generator, value);
    }

    @Test(expected=IOException.class)
    public void testSerializeValueThrowsIOException() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Object value = new Object();
        XmlSerializerProvider provider = Mockito.mock(XmlSerializerProvider.class);
        provider._serializeXmlNull(generator);
        Mockito.verify(provider, Mockito.times(1)).serializeValue(generator, value);
        throw new IOException();
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator generator = Mockito.mock(ToXmlGenerator.class);
        QName rootName = Mockito.mock(QName.class);
        XmlSerializerProvider provider = Mockito.mock(XmlSerializerProvider.class);
        provider._startRootArray(generator, rootName);
        Mockito.verify(generator, Mockito.times(1)).writeStartObject();
        Mockito.verify(generator, Mockito.times(1)).writeFieldName("item");
    }

    @Test
    public void testInitWithRootNameSetsNameWhenNotSet() throws IOException {
        ToXmlGenerator generator = Mockito.mock(ToXmlGenerator.class);
        QName rootName = Mockito.mock(QName.class);
        XmlSerializerProvider provider = Mockito.mock(XmlSerializerProvider.class);
        provider._initWithRootName(generator, rootName);
        Mockito.verify(generator, Mockito.times(1)).setNextNameIfMissing(rootName);
        Mockito.verify(generator, Mockito.times(1)).initGenerator();
        verify(generator, never()).inRoot();
        Mockito.verify(generator, Mockito.times(1)).setDefaultNamespace(rootName.getNamespaceURI());
    }

    @Test
    public void testSerializeValueCallsSerializeXmlNullWhenValueIsNull() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Object value = null;
        JavaType rootType = Mockito.mock(JavaType.class);
        XmlSerializerProvider provider = Mockito.mock(XmlSerializerProvider.class);
        provider.serializeValue(generator, value, rootType);
        Mockito.verify(provider, Mockito.times(1))._serializeXmlNull(generator);
    }
    
    @Test(expected=IOException.class)
    public void testSerializeValueThrowsIOExceptionWhenValueIsNotNull() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = Mockito.mock(JavaType.class);
        XmlSerializerProvider provider = Mockito.mock(XmlSerializerProvider.class);
        provider.serializeValue(generator, value, rootType);
        Mockito.verify(provider, Mockito.times(1)).findTypedValueSerializer(rootType,true,null);
        Mockito.verify(provider, Mockito.times(1))._assertInstanceOfXmlGenerator(generator);
        throw new IOException();
    }
    @Test
    public void testSerializeValueCallsSerializeValueWhenSerIsNull() throws IOException {
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = Mockito.mock(JavaType.class);
        XmlSerializerProvider provider = Mockito.mock(XmlSerializerProvider.class);
        provider.serializeValue(generator, value);
        Mockito.verify(provider, Mockito.times(1)).serializeValue(generator, value, null, null);
    }

}