package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue_NullValue() throws IOException {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());

        // Act
        String result = provider.serializeValue(null);

        // Assert
        assertEquals("<null></null>", result);
    }
    
    @Test
    public void testSerializeValue_SerializeObject() throws IOException {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        TestObject object = new TestObject("test", 10);
        
        // Act
        String result = provider.serializeValue(object);
        
        // Assert
        assertEquals("<TestObject><name>test</name><value>10</value></TestObject>", result);
    }
    
    @Test
    public void testSerializeValue_SerializeArray() throws IOException {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        TestObject[] array = { new TestObject("test1", 10), new TestObject("test2", 20) };
        
        // Act
        String result = provider.serializeValue(array);
        
        // Assert
        assertEquals("<TestObject><name>test1</name><value>10</value></TestObject><TestObject><name>test2</name><value>20</value></TestObject>", result);
    }
    
    @Test
    public void testSerializeValue_EmptyArray() throws IOException {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        TestObject[] array = new TestObject[0];
        
        // Act
        String result = provider.serializeValue(array);
        
        // Assert
        assertEquals("", result);
    }
    
    @Test
    public void testSerializeValue_SerializeNestedArray() throws IOException {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        TestObject[][] nestedArray = { { new TestObject("test1", 10) }, { new TestObject("test2", 20) } };
        
        // Act
        String result = provider.serializeValue(nestedArray);
        
        // Assert
        assertEquals("<TestObject><name>test1</name><value>10</value></TestObject><TestObject><name>test2</name><value>20</value></TestObject>", result);
    }
}