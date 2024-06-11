package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private XmlRootNameLookup rootNameLookup;
    private JsonGenerator jsonGenerator;
    private Object value;
    private JavaType rootType;
    private JsonSerializer<Object> valueSerializer;

    @Before
    public void setUp() {
        rootNameLookup = Mockito.mock(XmlRootNameLookup.class);
        jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializationConfig config = Mockito.mock(SerializationConfig.class);
        SerializerFactory factory = Mockito.mock(SerializerFactory.class);

        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider src = new XmlSerializerProvider(xmlSerializerProvider, config, factory);
        xmlSerializerProvider = new XmlSerializerProvider(src);
    }

    @Test
    public void testSerializeValue() throws Exception {
        value = new Object();
        xmlSerializerProvider.serializeValue(jsonGenerator, value);
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeEndObject();
    }

    @Test
    public void testInheritedMethods() throws Exception {
        rootType = Mockito.mock(JavaType.class);
        valueSerializer = Mockito.mock(JsonSerializer.class);
        IOException exception = Mockito.mock(IOException.class);

        xmlSerializerProvider.serializeValue(jsonGenerator, value, rootType, valueSerializer);
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeEndObject();
        Mockito.verify(valueSerializer, Mockito.times(1)).serialize(value, jsonGenerator, xmlSerializerProvider);

        xmlSerializerProvider._serializeXmlNull(jsonGenerator);
        Mockito.verify(jsonGenerator, Mockito.times(1)).writeEndObject();

        xmlSerializerProvider._startRootArray(Mockito.mock(ToXmlGenerator.class), Mockito.mock(QName.class));
        xmlSerializerProvider._initWithRootName(Mockito.mock(ToXmlGenerator.class), Mockito.mock(QName.class));
        xmlSerializerProvider._rootNameFromConfig();

        Mockito.when(valueSerializer.serialize(Mockito.any(), Mockito.any(), Mockito.any())).thenThrow(exception);
        try {
            xmlSerializerProvider.serializeValue(jsonGenerator, value, rootType, valueSerializer);
            fail("An exception should have been thrown");
        } catch (Exception e) {
            assertSame(exception, e.getCause());
        }
    }

    @Test
    public void testSetDefaultNamespace() throws Exception {
        xmlSerializerProvider._initWithRootName(Mockito.mock(ToXmlGenerator.class), new QName("namespace", "localPart"));
    }
}