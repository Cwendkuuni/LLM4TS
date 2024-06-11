package com.fasterxml.jackson.dataformat.xml.ser;

import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class XmlSerializerProviderTest {

    @Mock
    private JsonGenerator gen;
    @Mock
    private JsonSerializer<Object> ser;
    @Mock
    private Object value;
    @Mock
    private ToXmlGenerator xgen;
    @Mock
    private ToXmlGenerator xgen2;
    @Mock
    private SerializationConfig config;
    @Mock
    private SerializerFactory f;
    @Mock
    private DefaultSerializerProvider dsp;
    @Mock
    private JsonMappingException e;
    @Mock
    private JavaType rootType;

    private XmlSerializerProvider provider;
    private QName rootName;
    private QName nullRootName;

    @Before
    public void setup() {
        provider = new XmlSerializerProvider(new XmlRootNameLookup());
        MockitoAnnotations.initMocks(this);
        rootName = new QName("root");
        nullRootName = new QName("null");
    }

    @Test
    public void testCreateInstance() {
        Assert.assertNotNull(provider.createInstance(config, f));
    }

    @Test
    public void testSerializeValue_NullValue() throws IOException {
        provider.serializeValue(gen, null);
        //verify here;
    }

    @Test
    public void testSerializeValue_NoXGen() throws IOException {
        provider.serializeValue(gen2, value);
        //verify here;
    }

    @Test
    public void testSerializeValue_SuperClassImplementationException() throws IOException {
        provider.serializeValue(gen, value);
        //verify here;
    }

    @Test
    public void testSerializeField_NoXGen() throws IOException {
        provider.serializeValue(gen, value, rootType);
        //verify here;
    }

    @Test
    public void testSerializeField_SuperClassImplementationException() throws IOException {
        provider.serializeValue(gen, value, rootType);
        //verify here;
    }

    @Test
    public void testSerializeValueWithSerializer_NoXGen() throws IOException {
        provider.serializeValue(gen, value, rootType, ser);
        //verify here;
    }

    @Test
    public void testSerializeValueWithSerializer_SuperClassImplementationException() throws IOException {
        provider.serializeValue(gen, value, rootType, ser);
        //verify here;
    }

    @Test
    public void testSerializeXmlNull_WithXGen() throws IOException {
        provider._serializeXmlNull(xgen);
        //verify here;
    }

    @Test
    public void testSerializeXmlNull_NoXGen() throws IOException {
        provider._serializeXmlNull(gen);
        //verify here;
    }

    @Test
    public void testStartRootArray() throws IOException {
        provider._startRootArray(xgen, rootName);
        //verify here;
    }

    @Test
    public void testDeserializeArrayWithRootNameException() throws IOException {
        provider._startRootArray(xgen, rootName);
        //verify here;
    }

    @Test
    public void testSerializeXmlNull_SuperClassImplementationException() throws IOException {
        provider.serializeValue(gen, null);
        //verify here;
    }

    private ToXmlGenerator gen2 = new ToXmlGenerator(Configuration.empty(), null, null, null, null, null, null,
            false, null) {
        @Override
        public ToXmlGenerator setNextNameIfMissing(QName name) throws IOException {
            return this;
        }

        @Override
        public boolean inRoot() {
            return false;
        }
    };
}