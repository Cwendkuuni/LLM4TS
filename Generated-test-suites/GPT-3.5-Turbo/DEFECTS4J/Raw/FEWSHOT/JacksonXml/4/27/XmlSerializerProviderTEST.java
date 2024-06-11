package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonGenerator;

public class XmlSerializerProviderTest {
    @Test
    public void testSerializeValueNull() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        ToXmlGenerator generator = new ToXmlGenerator();
        provider.serializeValue(generator, null);
        // Assert that the generator has been initialized
        assertNotNull(generator);
        // Assert that the generator has been set with the root name for null
        assertEquals(generator.getCurrentName(), "null");
    }

    @Test
    public void testSerializeValueClass() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        ToXmlGenerator generator = new ToXmlGenerator();
        provider.serializeValue(generator, new MyClass());
        // Assert that the generator has been initialized
        assertNotNull(generator);
        // Assert that the generator has been set with the root name
        assertEquals(generator.getCurrentName(), "MyClass");
    }

    @Test
    public void testSerializeValueArray() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        ToXmlGenerator generator = new ToXmlGenerator();
        provider.serializeValue(generator, new MyClass[] {new MyClass()});
        // Assert that the generator has been initialized
        assertNotNull(generator);
        // Assert that the generator has been set with the root name
        assertEquals(generator.getCurrentName(), "MyClass");
        // Assert that the generator has started writing the root array
        assertEquals(generator.getCurrentValue(), "item");
    }

    private class MyClass {}
}