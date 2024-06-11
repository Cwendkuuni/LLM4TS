package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

    @Mock
    private XmlRootNameLookup _rootNameLookup;
    @Mock
    private ObjectMapper _objectMapper;
    @Mock
    private SerializationConfig _configuration;
    @Mock
    private SerializerFactory _serializerFactory;
    @Mock
    private TokenBuffer _tokenBuffer;
    @InjectMocks
    private XmlSerializerProvider xmlSerializerProvider;
    private JsonSerializer<Object> jsonSerializer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(_rootNameLookup.findRootName(any(Class.class), any(SerializationConfig.class))).willReturn(new QName("testRootName"));
        given(_objectMapper.getSerializationConfig()).willReturn(_configuration);
        given(_objectMapper.findValueSerializer(any(Class.class), anyBoolean())).willReturn(jsonSerializer);
    }

    @Test
    public void testSerializeValueWithRootTypeNull() throws Exception {
        given(_objectMapper.getSerializerProvider()).willReturn(xmlSerializerProvider);
        given(xmlSerializerProvider.findValueSerializer(eq(Object.class), anyBoolean(), any())).willCallRealMethod();

        JsonGenerator gen = mock(JsonGenerator.class);
        xmlSerializerProvider.serializeValue(gen, null);
        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("null");
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testSerializeValue() throws Exception {
        given(_objectMapper.getSerializationConfig()).willReturn(_configuration);
        given(_objectMapper.findValueSerializer(eq(String.class), anyBoolean())).willReturn(jsonSerializer);
        given(xmlSerializerProvider.findValueSerializer(eq(String.class), anyBoolean(), any())).willCallRealMethod();
        given(_rootNameLookup.findRootName(eq(String.class), any(SerializationConfig.class))).willReturn(new QName("testRootName"));

        Object testObject = "testString";
        JsonGenerator gen = mock(JsonGenerator.class);
        given(gen.getSerializerProvider()).willReturn(xmlSerializerProvider);
        given(xmlSerializerProvider.serializeValue(gen, testObject)).willCallRealMethod();

        given(xmlSerializerProvider.findValueSerializer(eq(String.class), anyBoolean(), any())).willReturn(jsonSerializer);
        given(jsonSerializer.serialize(testObject, gen, xmlSerializerProvider)).willCallRealMethod();

        xmlSerializerProvider.serializeValue(gen, testObject);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("testRootName");
        verify(gen, times(1)).writeFieldName("item");
        verify(gen, times(1)).writeValue(testObject);
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testSerializeValueWithRootType() throws Exception {
        given(_objectMapper.getSerializerProvider()).willReturn(xmlSerializerProvider);
        given(xmlSerializerProvider.findValueSerializer(eq(Object.class), anyBoolean(), any())).willCallRealMethod();

        JsonGenerator gen = mock(JsonGenerator.class);
        JavaType rootType = TypeFactory.defaultInstance().constructType(TestObject.class);
        xmlSerializerProvider.serializeValue(gen, new TestObject(), rootType);
        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("testRootName");
        verify(gen, times(1)).writeFieldName("testField");
        verify(gen, times(1)).writeValue(new TestObject().getTestField());
        verify(gen, times(1)).writeEndObject();
    }

    @Test
    public void testSerializeValueWithRootTypeAndSerializer() throws Exception {
        given(_objectMapper.getSerializationConfig()).willReturn(_configuration);
        given(_objectMapper.findValueSerializer(eq(TestObject.class), anyBoolean())).willReturn(jsonSerializer);
        given(_objectMapper.getSerializerProvider()).willReturn(xmlSerializerProvider);
        given(xmlSerializerProvider.findValueSerializer(eq(TestObject.class), anyBoolean(), any())).willCallRealMethod();
        given(_rootNameLookup.findRootName(eq(TestObject.class), any(SerializationConfig.class))).willReturn(new QName("testRootName"));

        Object testObject = new TestObject();
        JsonGenerator gen = mock(JsonGenerator.class);
        given(gen.getSerializerProvider()).willReturn(xmlSerializerProvider);
        given(xmlSerializerProvider.serializeValue(gen, testObject, new TypeReference<TestObject>() {}))
                .willCallRealMethod();

        given(xmlSerializerProvider.findValueSerializer(eq(TestObject.class), anyBoolean(), any()))
                .willReturn(spy(jsonSerializer));
        given(jsonSerializer.serialize(testObject, gen, xmlSerializerProvider)).willCallRealMethod();

        xmlSerializerProvider.serializeValue(gen, testObject, new TypeReference<TestObject>() {});

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("testRootName");
        verify(gen, times(1)).writeFieldName("testField");
        verify(gen, times(1)).writeValue(new TestObject().getTestField());
        verify(jsonSerializer).serialize(testObject, gen, xmlSerializerProvider);
    }

    @Test
    public void testSerializeValueWithSerializer() throws Exception {
        given(_objectMapper.getSerializationConfig()).willReturn(_configuration);
        given(_objectMapper.findValueSerializer(eq(TestObject.class), anyBoolean())).willReturn(jsonSerializer);
        given(_objectMapper.getSerializerProvider()).willReturn(xmlSerializerProvider);
        given(xmlSerializerProvider.findValueSerializer(any(Class.class), anyBoolean(), any())).willCallRealMethod();

        Object testObject = new TestObject();
        JsonGenerator gen = mock(JsonGenerator.class);
        given(gen.getSerializerProvider()).willReturn(xmlSerializerProvider);
        given(xmlSerializerProvider.serializeValue(gen, testObject, jsonSerializer))
                .willCallRealMethod();

        given(xmlSerializerProvider.findValueSerializer(eq(TestObject.class), anyBoolean(), any()))
                .willReturn(jsonSerializer);
        given(jsonSerializer.serialize(testObject, gen, xmlSerializerProvider)).willCallRealMethod();

        xmlSerializerProvider.serializeValue(gen, testObject, jsonSerializer);

        verify(gen, times(1)).writeStartObject();
        verify(gen, times(1)).writeFieldName("testRootName");
        verify(gen, times(1)).writeFieldName("testField");
        verify(gen, times(1)).writeValue(new TestObject().getTestField());
        verify(jsonSerializer).serialize(testObject, gen, xmlSerializerProvider);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueWithInvalidRootType() throws Exception {
        given(_objectMapper.getSerializerProvider()).willReturn(xmlSerializerProvider);
        given(xmlSerializerProvider.findValueSerializer(eq(Object.class), anyBoolean(), any())).willCallRealMethod();
        given(xmlSerializerProvider.findValueSerializer(eq(String[].class), anyBoolean(), any()))
                .willThrow(new JsonMappingException("Invalid root type: " + String[].class));

        JsonGenerator gen = mock(JsonGenerator.class);
        String[] testArray = new String[]{"testString"};
        xmlSerializerProvider.serializeValue(gen, testArray);
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = mock(SerializerFactory.class);
        SerializerProvider src = mock(SerializerProvider.class);
        ObjectMapper mapper = new ObjectMapper();
        XmlSerializerProvider serializerProvider = new XmlSerializerProvider(src, config, factory);

        assertEquals(XmlSerializerProvider.class, serializerProvider.getClass());
        assertEquals(src, serializerProvider.getParent());
        assertEquals(config, serializerProvider.getConfig());
        assertEquals(factory, serializerProvider.getFactory());
        assertEquals(_rootNameLookup, serializerProvider._rootNameLookup);

        XmlSerializerProvider clone = xmlSerializerProvider.copy();
        assertEquals(XmlSerializerProvider.class, clone.getClass());
        assertEquals(src, clone.getParent());
        assertEquals(config, clone.getConfig());
        assertEquals(factory, clone.getFactory());
        assertEquals(_rootNameLookup, clone._rootNameLookup);

        XmlSerializerProvider newInstance = XmlSerializerProvider.createInstance(config, factory);
        assertEquals(XmlSerializerProvider.class, newInstance.getClass());
        assertEquals(config, newInstance.getConfig());
        assertEquals(factory, newInstance.getFactory());
        assertEquals(_rootNameLookup, newInstance._rootNameLookup);
    }

    private static class TestObject {
        private String testField;

        public String getTestField() {
            return testField;
        }

        public void setTestField(String testField) {
            this.testField = testField;
        }
    }
}