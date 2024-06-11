package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Test;

public class XmlSerializerProviderTest {

    @Test
    public void test_serializeValue() {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);

        // Test null value
        JsonGenerator gen = new TokenBuffer();
        xmlSerializerProvider.serializeValue(gen, null);
        assertEquals("<null/>", gen.toString());

        // Test non-null value
        Object value = "test";
        gen = new TokenBuffer();
        jsonSerializerProvider.serializeValue(gen, value);
        assertEquals("<test/>", gen.toString());

        // Test non-null value with rootType and JsonSerializer
        JavaType rootType = new SimpleType("MyType");
        JsonSerializer<Object> jsonSerializer = new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString("serialized value");
            }
        };
        gen = new TokenBuffer();
        xmlSerializerProvider.serializeValue(gen, value, rootType, jsonSerializer);
        assertEquals("<MyType><test/></MyType>", gen.toString());

        // Test null value with rootType and JsonSerializer
        gen = new TokenBuffer();
        xmlSerializerProvider.serializeValue(gen, null, rootType, jsonSerializer);
        assertEquals("<MyType><null/></MyType>", gen.toString());
    }

    @Test
    public void test_rootNameFromConfig() {
        SerializationConfig config = new SerializationConfig();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());

        PropertyName rootName = config.getFullRootName();
        QName expectedRootName = rootName == null ? null : new QName(rootName.getNamespace(), rootName.getSimpleName());
        QName actualRootName = xmlSerializerProvider._rootNameFromConfig();

        assertEquals(expectedRootName, actualRootName);
    }

    @Test
    public void test_asXmlGenerator() throws JsonMappingException {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);

        JsonGenerator gen = new TokenBuffer();
        ToXmlGenerator expectedToXmlGenerator = new ToXmlGenerator() {};
        ToXmlGenerator actualToXmlGenerator = xmlSerializerProvider._asXmlGenerator(gen);

        assertEquals(expectedToXmlGenerator, actualToXmlGenerator);
    }
}