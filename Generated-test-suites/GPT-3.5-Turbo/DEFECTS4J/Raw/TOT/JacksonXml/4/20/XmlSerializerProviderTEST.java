package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider serializerProvider;
    private XmlRootNameLookup rootNameLookup;
    private TestXmlGenerator xmlGenerator;
    
    private class TestXmlGenerator extends ToXmlGenerator {
        
        private QName rootName;

        public TestXmlGenerator(JsonGenerator g, int feat, XmlRootNameLookup rootNames) {
            super(g, feat, null);
        }

        @Override
        public void setNextName(QName name) throws IOException {
            this.rootName = name;
        }
        
        public QName getRootName() {
            return rootName;
        }
        
        public void reset() {
            rootName = null;
        }
    }

    @Before
    public void setUp() throws Exception {
        rootNameLookup = new XmlRootNameLookup();
        serializerProvider = new XmlSerializerProvider(rootNameLookup);
        xmlGenerator = new TestXmlGenerator(null, 0, rootNameLookup);
    }

    @Test
    public void testSerializeValue_withNullValue_calls_serializeXmlNull() throws IOException {
        xmlGenerator.reset();
        serializerProvider.serializeValue(xmlGenerator, null);
        assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, xmlGenerator.getRootName());
    }
    
    @Test
    public void testSerializeValue_withNonNullValue_calls_serializeValue() throws IOException {
        xmlGenerator.reset();
        Object value = new Object();
        JsonSerializer<Object> ser = new JsonSerializer<Object>() {
            
            @Override
            public void serialize(Object value, JsonGenerator gen,
                    SerializerProvider serializers) throws IOException,
                    JsonGenerationException {
                gen.writeString(value.toString());
            }
        };
        serializerProvider.serializeValue(xmlGenerator, value, JavaType.clazz, ser);
        
        // custom serialization logic will serialize the object to string and write to generator
        // so no root name will be set
        assertNull(xmlGenerator.getRootName());
    }
    
    @Test
    public void testSerializeValue_withNonNullRootType_calls_serializeValueWithRootType() throws IOException {
        xmlGenerator.reset();
        Object value = new Object();
        JavaType rootType = JavaType.clazz;
        serializerProvider.serializeValue(xmlGenerator, value, rootType);
        
        // rootNameLookup should be called to find the root name for the rootType
        QName rootName = rootNameLookup.findRootName(rootType, null);
        assertEquals(rootName, xmlGenerator.getRootName());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSerializeValue_withToXmlGenerator_throwsIllegalStateException() throws IOException {
        xmlGenerator = new TestXmlGenerator(xmlGenerator, 0, rootNameLookup);
        serializerProvider.serializeValue(xmlGenerator, null);
    }
    
    @Test
    public void testSerializeValue_withToXmlGenerator_andRootType_throwsIllegalStateException() throws IOException {
        xmlGenerator = new TestXmlGenerator(xmlGenerator, 0, rootNameLookup);
        JavaType rootType = JavaType.clazz;
        try {
            serializerProvider.serializeValue(xmlGenerator, null, rootType);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }
    
    @Test
    public void testSerializeValue_withToXmlGenerator_andRootType_andJsonSerializer_throwsIllegalStateException()
            throws IOException {
        xmlGenerator = new TestXmlGenerator(xmlGenerator, 0, rootNameLookup);
        JavaType rootType = JavaType.clazz;
        JsonSerializer<Object> ser = new JsonSerializer<Object>() {
            
            @Override
            public void serialize(Object value, JsonGenerator gen,
                    SerializerProvider serializers) throws IOException,
                    JsonGenerationException {
                gen.writeString(value.toString());
            }
        };
        
        try {
            serializerProvider.serializeValue(xmlGenerator, null, rootType, ser);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }
}