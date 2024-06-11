package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

public class XmlSerializerProviderTest {

    private XmlRootNameLookup mockRootNames;
    private SerializationConfig mockConfig;
    private SerializerFactory mockFactory;
    private ToXmlGenerator mockXmlGenerator;
    private JavaType mockRootType;
    private JsonGenerator mockGenerator;
    private StringWriter writer;
    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setup() {
        mockRootNames = mock(XmlRootNameLookup.class);
        mockConfig = mock(SerializationConfig.class);
        mockFactory = mock(SerializerFactory.class);
        mockXmlGenerator = mock(ToXmlGenerator.class);
        mockRootType = mock(JavaType.class);
        mockGenerator = mock(JsonGenerator.class);
        writer = new StringWriter();
        xmlSerializerProvider = new XmlSerializerProvider(mockRootNames);
    }

    @Test
    public void testCreateInstance() {
        SerializerFactory jsf = mock(SerializerFactory.class);
        DefaultSerializerProvider result = xmlSerializerProvider.createInstance(mockConfig, jsf);
        assertEquals(XmlSerializerProvider.class, result.getClass());
        assertEquals(XmlSerializerProvider.class, result.getClass().getSuperclass());
    }

    @Test
    public void testSerializeValueWhenValueIsNull() throws IOException {
        QName rootName = new QName("null");
        when(mockXmlGenerator.setNextNameIfMissing(rootName)).thenReturn(false);
        when(mockXmlGenerator.inRoot()).thenReturn(true);
        when(mockRootNames.findRootName(String.class, mockConfig)).thenReturn(rootName);

        xmlSerializerProvider.serializeValue(mockGenerator, null);

        //Asserts
        //...
    }

    @Test
    public void testSerializeXmlNull() throws IOException {
        QName rootName = new QName("null");
        when(mockXmlGenerator.setNextNameIfMissing(rootName)).thenReturn(true);

        DefaultSerializerProvider spiMock = mock(DefaultSerializerProvider.class);

        xmlSerializerProvider.serializeXmlNull(mockXmlGenerator, spiMock);

        //Asserts
        //...
    }

    @Test
    public void testSerializeValueWhenAsArrayIsTrue() throws IOException {
        //Setup
        QName rootName = new QName("root");
        when(mockXmlGenerator.setNextNameIfMissing(rootName)).thenReturn(true);
        when(mockXmlGenerator.inRoot()).thenReturn(true);
        when(mockRootNames.findRootName(String.class, mockConfig)).thenReturn(rootName);
        when(TypeUtil.isIndexedType(String.class)).thenReturn(true);

        //Test
        xmlSerializerProvider.serializeValue(mockGenerator, "value");

        //Asserts
        //...
    }

    @Test
    public void testSerializeValueWhenAsArrayIsFalse() throws IOException {
        //Setup
        QName rootName = new QName("root");
        when(mockXmlGenerator.setNextNameIfMissing(rootName)).thenReturn(true);
        when(mockXmlGenerator.inRoot()).thenReturn(true);
        when(mockRootNames.findRootName(String.class, mockConfig)).thenReturn(rootName);
        when(TypeUtil.isIndexedType(String.class)).thenReturn(false);

        //Test
        xmlSerializerProvider.serializeValue(mockGenerator, "value");

        //Asserts
        //...
    }

}