package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);

        ObjectMapper mapper = new ObjectMapper(xmlProvider);

        // Test null value
        String xml = mapper.writeValueAsString(null);
        assertEquals("<null/>", xml);

        // Test serialization of simple types
        JavaType javaTypeInt = mapper.getTypeFactory().constructType(int.class);
        int i = 1;
        xml = mapper.writeValueAsString(i);
        assertEquals("<int>1</int>", xml);

        JavaType javaTypeString = mapper.getTypeFactory().constructType(String.class);
        String str = "Hello, World!";
        xml = mapper.writeValueAsString(str);
        assertEquals("<string>Hello, World!</string>", xml);

        // Test serialization of custom classes
        String className = "com.example.MyClass";
        MyClass myClass = new MyClass();
        xml = mapper.writeValueAsString(myClass);
        // Replace this with the expected XML for MyClass
        assertEquals("<MyClass><field1>value1</field1><field2>value2</field2></MyClass>", xml);

        // Test serialization with explicit root name
        PropertyName rootNameProp = PropertyName.construct("rootName");
        config.setRootValueSeparator(new PropertyName[0]);
        config.setRootName(rootNameProp);
        xmlProvider = new XmlSerializerProvider(rootNameLookup, config);
        xml = mapper.writeValueAsString(i);
        assertEquals("<rootName><int>1</int></rootName>", xml);
    }

    @Test
    public void testSerializeValueWithRootType() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);

        ObjectMapper mapper = new ObjectMapper(xmlProvider);

        // Test null value
        String xml = mapper.writeValueAsString(null, JavaType.construct(String[].class), null);
        assertEquals("<null/><item xsi:type=\"x:Array\"/>", xml);

        // Test serialization of simple types
        JavaType javaTypeIntArray = mapper.getTypeFactory().constructType(int[].class);
        int[] ints = {1, 2, 3};
        xml = mapper.writeValueAsString(ints, JavaType.construct(int[].class), null);
        assertEquals("<intArray><int>1</int><int>2</int><int>3</int></intArray>", xml);

        JavaType javaTypeStringArray = mapper.getTypeFactory().constructType(String[].class);
        String[] strings = {"Hello", "World"};
        xml = mapper.writeValueAsString(strings, JavaType.construct(String[].class), null);
        assertEquals("<stringArray><string>Hello</string><string>World</string></stringArray>", xml);

        // Test serialization of custom classes
        JavaType javaTypeMyClassArray = mapper.getTypeFactory().constructType(MyClass[].class);
        MyClass[] myClasses = {new MyClass(), new MyClass()};
        xml = mapper.writeValueAsString(myClasses, javaTypeMyClassArray, null);
        // Replace this with the expected XML for MyClass array
        assertEquals("<MyClassArray><MyClass><field1>value1</field1><field2>value2</field2></MyClass><MyClass><field1>value3</field1><field2>value4</field2></MyClass></MyClassArray>", xml);
    }

    @Test
    public void testSerializeValueWithCustomSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup);
        ObjectMapper mapper = new ObjectMapper(xmlProvider);

        // Create a custom serializer for MyClass
        JsonSerializer<MyClass> myClassSerializer = new JsonSerializer<MyClass>() {
            @Override
            public void serialize(MyClass myClass, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeStartObject();
                gen.writeFieldName("customField");
                gen.writeValue(myClass.customField);
                gen.writeEndObject();
            }
        };

        // Register the custom serializer with ObjectMapper
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("customField");
        ObjectWriter writer = mapper.writer()
                .withFilter(filter)
                .withSerializer(MyClass.class, myClassSerializer);

        MyClass myClass = new MyClass("Test");
        String xml = writer.writeValueAsString(myClass);
        assertEquals("<MyClass><customField>Test</customField></MyClass>", xml);
    }

    @Test
    public void testInitWithRootName() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup());

        ToXmlGenerator xgen = xmlProvider._asXmlGenerator(jsonGenerator);
        xmlProvider._initWithRootName(xgen, new QName("MyRootElement"));

        // Check if the root name was set
        assertEquals("MyRootElement", xgen.getNextName());
    }

    @Test
    public void testStartRootArray() throws Exception {
        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator jsonGenerator = jsonFactory.createGenerator(new ByteArrayOutputStream(), new DefaultSerializerProvider());
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(new XmlRootNameLookup());

        ToXmlGenerator xgen = xmlProvider._asXmlGenerator(jsonGenerator);
        xmlProvider._startRootArray(xgen, new QName("MyRootArray"));

        // Check if the root array was started
        assertEquals("MyRootArray", xgen.getName());
        assertEquals("item", xgen.getCurrentName());
    }

    @Test
    public void testSerializeValueWithRootNameFromConfig() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config);

        ObjectMapper mapper = new ObjectMapper(xmlProvider);

        int i = 1;
        String xml = mapper.writeValueAsString(i);
        assertEquals("<rootName><int>1</int></rootName>", xml);

        PropertyName rootNameProp = PropertyName.construct("rootName2");
        config.setRootValueSeparator(new PropertyName[0]);
        config.setRootName(rootNameProp);
        xmlProvider = new XmlSerializerProvider(rootNameLookup, config);
        xml = mapper.writeValueAsString(i);
        assertEquals("<rootName2><int>1</int></rootName2>", xml);
    }

    @Test
    public void testSerializeValueWithRootNameFromLookup() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config);

        ObjectMapper mapper = new ObjectMapper(xmlProvider);

        int i = 1;
        String xml = mapper.writeValueAsString(i);
        assertEquals("<MyClass><int>1</int></MyClass>", xml);

        rootNameLookup.putRootName("MyClass", new QName("MyRootName"));
        xml = mapper.writeValueAsString(i);
        assertEquals("<MyRootName><int>1</int></MyRootName>", xml);
    }

    @Test
    public void testSerializeValueWithRootNameFromClass() throws Exception {
        SerializationConfig config = new SerializationConfig();
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(rootNameLookup, config);

        ObjectMapper mapper = new ObjectMapper(xmlProvider);

        int i = 1;
        JavaType javaType = mapper.getTypeFactory().constructType(MyRootName.class);
        String xml = mapper.writeValueAsString(i, javaType, null);
        assertEquals("<MyRootName><int>1</int></MyRootName>", xml);
    }

    static class MyClass {
        String field1;
        String field2;
        String customField;

        public MyClass() {
            this("Default Value");
        }

        public MyClass(String customField) {
            this.customField = customField;
        }
    }

    static class MyRootName {
        // Empty class used for testing root name lookup
    }
}