package com.fasterxml.jackson.dataformat.xml.ser;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider src = new XmlSerializerProvider();
        assertNotNull("Instance should not be null", src.createInstance());
    }

    @Test
    public void testSerializeValue() {
        XmlSerializerProvider src = new XmlSerializerProvider();
        assertNotNull("Serialized value should not be null", src.serializeValue());
    }

    @Test
    public void testSerializeValueWithRootType() {
        XmlSerializerProvider src = new XmlSerializerProvider();
        assertNotNull("Serialized value should not be null", src.serializeValueWithRootType());
    }

    @Test
    public void testSerializeValueWithRootTypeAndSerializer() {
        XmlSerializerProvider src = new XmlSerializerProvider();
        assertNotNull("Serialized value should not be null", src.serializeValueWithRootTypeAndSerializer());
    }
}