package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.Test;

public class XmlSerializerProviderTest {
    @Test
    public void test_serializeValue() throws Exception {
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        ObjectReader reader = new ObjectMapper().readerForUpdating(String.class);
        String xmlString = "<root><str>Hello</str></root>";
        String jsonString = "{\"root\":{\"str\":\"Hello\"}}";

        // Test with null value
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>", gen.toString());
        gen.close();

        // Test with non-null value (String)
        Object value = reader.readValue(xmlString);
        JsonSerializer<String> stringSerializer = xmlProvider.findValueSerializer(String.class, null);
        gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, value);
        assertEquals(jsonString, gen.toString());
        gen.close();

        // Test with non-null value (Custom Object)
        QName rootName = new QName("http://test.com", "MyObject");
        JavaType rootType = new TypeFactory().constructType(MyObject.class);
        MyObject customObject = new MyObject();
        gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, customObject, rootType);
        String expectedXml = "<MyObject xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><field1>Value1</field1><field2>Value2</field2></MyObject>";
        assertEquals(expectedXml, gen.toString());
        gen.close();

        // Test with non-null value (Custom Object with RootName in Config)
        MyObjectWithRootName customObjectWithRootName = new MyObjectWithRootName();
        SerializationConfig config = new SerializationConfig();
        config.setRootElementName("rootElement");
        xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup(), config, null);
        gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, customObjectWithRootName);
        String expectedXmlWithRootName = "<rootElement xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><MyObject xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><field1>Value1</field1><field2>Value2</field2></MyObject></rootElement>";
        assertEquals(expectedXmlWithRootName, gen.toString());
        gen.close();
    }

    @Test
    public void test_serializeValueWithSerializer() throws Exception {
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        ObjectReader reader = new ObjectMapper().readerForUpdating(String.class);
        String xmlString = "<root><str>Hello</str></root>";
        String jsonString = "{\"root\":{\"str\":\"Hello\"}}";

        // Test with null value
        JsonGenerator gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, null, null, null);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>", gen.toString());
        gen.close();

        // Test with non-null value (String)
        Object value = reader.readValue(xmlString);
        JsonSerializer<String> stringSerializer = xmlProvider.findValueSerializer(String.class, null);
        gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, value, null, stringSerializer);
        assertEquals(jsonString, gen.toString());
        gen.close();
    }

    @Test
    public void test_serializeValueWithCustomRootName() throws Exception {
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        ObjectReader reader = new ObjectMapper().readerForUpdating(String.class);
        String xmlString = "<MyObject xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><field1>Value1</field1><field2>Value2</field2></MyObject>";
        String jsonString = "{\"MyObject\":{\"field1\":\"Value1\",\"field2\":\"Value2\"}}";

        JavaType rootType = new TypeFactory().constructType(MyObject.class);
        gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, null, rootType, null);
        assertEquals("<MyObject xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>", gen.toString());
        gen.close();

        Object value = reader.readValue(xmlString);
        xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        gen = new TokenBuffer();
        xmlProvider.serializeValue(gen, value, rootType, null);
        assertEquals(jsonString, gen.toString());
        gen.close();
    }
}

class MyObject {
    String field1;
    String field2;

    @Override
    public String toString() {
        return "MyObject{" +
                "field1='" + field1 + '\'' +
                ", field2='" + field2 + '\'' +
                '}';
    }
}

class MyObjectWithRootName {
    MyObject myObject;

    public MyObject getMyObject() {
        return myObject;
    }

    public void setMyObject(MyObject myObject) {
        this.myObject = myObject;
    }
}