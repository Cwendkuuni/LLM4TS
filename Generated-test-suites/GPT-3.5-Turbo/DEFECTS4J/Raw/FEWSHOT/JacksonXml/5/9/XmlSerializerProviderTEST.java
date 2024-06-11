package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class XmlSerializerProviderTest {
    @Test
    public void testSerializeValue() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        assertEquals("foo", provider.serializeValue(null, "foo"));
        assertNull(provider.serializeValue(null, null));
    }

    @Test
    public void testSerializeValueWithType() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        assertEquals("bar", provider.serializeValue(null, "bar", String.class, null));
        assertNull(provider.serializeValue(null, null, null, null));
    }

    @Test
    public void testSerializeXmlNull() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        assertNull(provider.serializeXmlNull(null));
    }

    @Test
    public void testStartRootArray() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        provider.startRootArray(null, null);
    }

    @Test
    public void testInitWithRootName() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        provider.initWithRootName(null, null);
    }

    @Test
    public void testRootNameFromConfig() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        assertNull(provider.rootNameFromConfig());
    }

    @Test
    public void testAsXmlGenerator() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        assertNull(provider.asXmlGenerator(null));
    }

    @Test
    public void testWrapAsIOE() throws Exception {
        XmlSerializerProvider provider = new XmlSerializerProvider(null);
        assertNull(provider.wrapAsIOE(null, null));
    }
}