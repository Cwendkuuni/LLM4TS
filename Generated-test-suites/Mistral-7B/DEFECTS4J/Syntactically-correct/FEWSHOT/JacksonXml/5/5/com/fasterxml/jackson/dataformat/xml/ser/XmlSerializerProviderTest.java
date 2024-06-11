package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializationWithRootName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNames);
        config.setRootValueSeparator(new PropertyName("item"));
        config.setSerializationInclusion(Emempties.EMPTY_ARRAY);
        config.setRootName("root");

        String xml = mapper.writeValueAsString(new Object());
        JsonGenerator gen = new TokenBuffer(mapper.getFactory().createXmlGenerator(true));
        serializerProvider.serializeValue(gen, new Object(), null, null);
        String serializedXml = gen.getAsString();

        assertTrue(serializedXml.startsWith("<root>"));
        assertTrue(xml.startsWith("<root>"));
        assertNull(serializerProvider._rootNameLookup.findRootName(Object.class, config));
    }

    @Test
    public void testSerializationWithRootNameFromConfig() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNames);
        config.setRootName("customRootName");

        String xml = mapper.writeValueAsString(new Object());
        JsonGenerator gen = new TokenBuffer(mapper.getFactory().createXmlGenerator(true));
        serializerProvider.serializeValue(gen, new Object(), null, null);
        String serializedXml = gen.getAsString();

        assertTrue(serializedXml.startsWith("<customRootName>"));
        assertTrue(xml.startsWith("<customRootName>"));
    }

    @Test
    public void testSerializationWithNullRootName() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNames);

        String xml = mapper.writeValueAsString(null);
        JsonGenerator gen = new TokenBuffer(mapper.getFactory().createXmlGenerator(true));
        serializerProvider.serializeValue(gen, null, null, null);
        String serializedXml = gen.getAsString();

        assertTrue(serializedXml.startsWith("<null>"));
        assertTrue(xml.startsWith("<null>"));
    }

    @Test
    public void testSerializationWithArray() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNames);
        config.setSerializationInclusion(Emempties.EMPTY_ARRAY);

        String xml = mapper.writeValueAsString(new int[0]);
        JsonGenerator gen = new TokenBuffer(mapper.getFactory().createXmlGenerator(true));
        serializerProvider.serializeValue(gen, new int[0], null, null);
        String serializedXml = gen.getAsString();

        assertTrue(serializedXml.startsWith("<item>"));
        assertTrue(xml.startsWith("<item>"));
    }

    @Test
    public void testSerializationWithRootNameFromRootNameLookup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNames);

        String xml = mapper.writeValueAsString(new Object());
        JsonGenerator gen = new TokenBuffer(mapper.getFactory().createXmlGenerator(true));
        serializerProvider.serializeValue(gen, new Object(), null, null);
        String serializedXml = gen.getAsString();

        QName rootName = serializerProvider._rootNameLookup.findRootName(Object.class, config);
        assertEquals("root", rootName.getLocalPart());
        assertTrue(serializedXml.startsWith("<root>"));
        assertTrue(xml.startsWith("<root>"));
    }

    @Test
    public void testSerializationWithRootNameFromConfigAndRootNameLookup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializationConfig config = mapper.getSerializationConfig();
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(rootNames);
        config.setRootName("customRootName");

        String xml = mapper.writeValueAsString(new Object());
        JsonGenerator gen = new TokenBuffer(mapper.getFactory().createXmlGenerator(true));
        serializerProvider.serializeValue(gen, new Object(), null, null);
        String serializedXml = gen.getAsString();

        QName rootName = serializerProvider._rootNameLookup.findRootName(Object.class, config);
        assertEquals("customRootName", rootName.getLocalPart());
        assertTrue(serializedXml.startsWith("<customRootName>"));
        assertTrue(xml.startsWith("<customRootName>"));
    }
}