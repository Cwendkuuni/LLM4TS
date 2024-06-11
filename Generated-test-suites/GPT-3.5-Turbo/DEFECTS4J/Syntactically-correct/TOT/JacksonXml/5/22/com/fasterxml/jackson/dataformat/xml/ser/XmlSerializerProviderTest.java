
package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

    @Test
    public void testCopy() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        XmlSerializerProvider copy = provider.copy();

        assertNotNull(copy);
        assertNotSame(provider, copy);
        assertSame(provider._rootNameLookup, copy._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);
        SerializationConfig config = new SerializationConfig(null, null, null);
        SerializerFactory serializerFactory = new SerializerFactory();

        XmlSerializerProvider instance = provider.createInstance(config, serializerFactory);

        assertNotNull(instance);
        assertNotSame(provider, instance);
        assertSame(provider._rootNameLookup, instance._rootNameLookup);
    }

    @Test
    public void testSerializeValueWithNull() throws IOException {
        ToXmlGenerator generator = mock(ToXmlGenerator.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider._serializeXmlNull(generator);

        //Verify that serializeValue with null value calls serializeValue with null generator
        verify(provider, times(1)).serializeValue(eq(generator), isNull());
    }

    @Test
    public void testRootNameFromConfig() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());

        PropertyName name = new PropertyName("root");
        provider._config.setFullRootName(name);
        QName rootName = provider._rootNameFromConfig();

        assertEquals("root", rootName.getLocalPart());
        assertNull(rootName.getNamespaceURI());

        name = new PropertyName("root", "namespace");
        provider._config.setFullRootName(name);
        rootName = provider._rootNameFromConfig();

        assertEquals("root", rootName.getLocalPart());
        assertEquals("namespace", rootName.getNamespaceURI());
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWithInvalidGenerator() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        JsonGenerator generator = mock(JsonGenerator.class);

        provider._asXmlGenerator(generator);
    }

    @Test
    public void testAsXmlGeneratorWithTokenBufferGenerator() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        TokenBuffer buffer = mock(TokenBuffer.class);

        ToXmlGenerator result = provider._asXmlGenerator(buffer);

        assertNull(result);
    }

    //... continue with more test methods for other public methods in the class ...

}
