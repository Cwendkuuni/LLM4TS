package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

    private ObjectMapper objectMapper;
    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        SerializationConfig config = objectMapper.getSerializationConfig();
        SerializerProvider provider = objectMapper.getDefaultSerializerProvider();
        xmlSerializerProvider = new XmlSerializerProvider(config, provider);
    }

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider newInstance = xmlSerializerProvider.createInstance(objectMapper.getSerializationConfig(), objectMapper.getDefaultSerializerProvider());
        assertEquals(xmlSerializerProvider.getClass(), newInstance.getClass());
    }

    @Test
    public void testSerializeValueNull() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        xmlSerializerProvider.serializeValue(gen, null);
        Mockito.verify(gen, Mockito.times(1)).writeStartElement("null");
        Mockito.verify(gen, Mockito.times(1)).writeEndElement();
    }

    @Test
    public void testSerializeValueObject() throws IOException {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testClass>\n<intField>1</intField>\n<stringField>Hello World!</stringField>\n</testClass>";
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Object value = new TestClass(1, "Hello World!");
        Mockito.when(gen.getRootValueSeparator()).thenReturn("");
        Mockito.when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        Mockito.when(gen.writeStartObject()).thenAnswer(invocation -> {
            gen.writeStartElement("testClass");
            return null;
        });
        Mockito.when(gen.writeNumberField("intField", 1)).thenAnswer(invocation -> null);
        Mockito.when(gen.writeStringField("stringField", "Hello World!"))
                .thenAnswer(invocation -> null);
        Mockito.when(gen.writeEndObject()).thenAnswer(invocation -> null);

        xmlSerializerProvider.serializeValue(gen, value);

        Mockito.verify(gen, Mockito.times(1)).writeStartObject();
        Mockito.verify(gen, Mockito.times(1)).writeStartElement("testClass");
        Mockito.verify(gen, Mockito.times(1)).writeNumberField("intField", 1);
        Mockito.verify(gen, Mockito.times(1)).writeStringField("stringField", "Hello World!");
        Mockito.verify(gen, Mockito.times(1)).writeEndObject();

        String actual = new StringWriter().writeValueAsString(gen);
        assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeValueObjectWithException() throws IOException {
        String expectedMessage = "JsonMappingException: Root name 'testClass' is not allowed for non-root serialization";
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Object value = new TestClass(1, "Hello World!");
        Mockito.when(gen.getRootValueSeparator()).thenReturn("");
        Mockito.when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        Mockito.when(gen.writeStartObject()).thenAnswer(invocation -> {
            throw new IllegalStateException("Root name is not set");
        });

        xmlSerializerProvider.serializeValue(gen, value);

        Mockito.verify(gen, Mockito.never()).writeStartObject();
        Mockito.verify(gen, Mockito.never()).writeStartElement("testClass");
        Mockito.verify(gen, Mockito.never()).writeNumberField("intField", 1);
        Mockito.verify(gen, Mockito.never()).writeStringField("stringField", "Hello World!");
        Mockito.verify(gen, Mockito.never()).writeEndObject();

        Mockito.verify(gen, Mockito.times(1)).close();

        Throwable exception = Mockito.captureThrown();
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void testSerializeValueObjectWithRootType() throws IOException {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testClass>\n<intField>1</intField>\n<stringField>Hello World!</stringField>\n</testClass>";
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        JavaType rootType = objectMapper.getTypeFactory().constructType(TestClass.class);
        Mockito.when(gen.getRootValueSeparator()).thenReturn("");
        Mockito.when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        Mockito.when(gen.writeStartObject()).thenAnswer(invocation -> {
            gen.writeStartElement("testClass");
            return null;
        });
        Mockito.when(gen.writeNumberField("intField", 1)).thenAnswer(invocation -> null);
        Mockito.when(gen.writeStringField("stringField", "Hello World!"))
                .thenAnswer(invocation -> null);
        Mockito.when(gen.writeEndObject()).thenAnswer(invocation -> null);

        xmlSerializerProvider.serializeValue(gen, new TestClass(1, "Hello World!"), rootType);

        Mockito.verify(gen, Mockito.times(1)).writeStartObject();
        Mockito.verify(gen, Mockito.times(1)).writeStartElement("testClass");
        Mockito.verify(gen, Mockito.times(1)).writeNumberField("intField", 1);
        Mockito.verify(gen, Mockito.times(1)).writeStringField("stringField", "Hello World!");
        Mockito.verify(gen, Mockito.times(1)).writeEndObject();

        String actual = new StringWriter().writeValueAsString(gen);
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeValueObjectWithRootTypeAndSerializer() throws IOException {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testClass>\n<intField>1</intField>\n<stringField>Hello World!</stringField>\n</testClass>";
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        JavaType rootType = objectMapper.getTypeFactory().constructType(TestClass.class);
        StdSerializer<TestClass> serializer = new StdSerializer<TestClass>(TestClass.class) {
            @Override
            public void serialize(TestClass value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeStartObject();
                gen.writeFieldName("intField");
                gen.writeNumber(value.getIntField());
                gen.writeFieldName("stringField");
                gen.writeString(value.getStringField());
                gen.writeEndObject();
            }
        };
        Mockito.when(gen.getRootValueSeparator()).thenReturn("");
        Mockito.when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        Mockito.when(gen.writeStartObject()).thenAnswer(invocation -> null);

        xmlSerializerProvider.setSerializerInstance("com.fasterxml.jackson.dataformat.xml.ser.TestClass", serializer);

        xmlSerializerProvider.serializeValue(gen, new TestClass(1, "Hello World!"), rootType);

        Mockito.verify(gen, Mockito.times(1)).writeStartObject();
        Mockito.verify(gen, Mockito.never()).writeStartElement("testClass");
        Mockito.verify(gen, Mockito.never()).writeNumberField("intField", 1);
        Mockito.verify(gen, Mockito.never()).writeStringField("stringField", "Hello World!");
        Mockito.verify(gen, Mockito.never()).writeEndObject();

        String actual = new StringWriter().writeValueAsString(gen);
        assertEquals(expected, actual);
    }

    static class TestClass {
        private int intField;
        private String stringField;

        public TestClass(int intField, String stringField) {
            this.intField = intField;
            this.stringField = stringField;
        }

        public int getIntField() {
            return intField;
        }

        public String getStringField() {
            return stringField;
        }
    }
}

class StringWriter {
    private static final SimpleBeanPropertyFilter.SerializerFilter FILTER = SimpleBeanPropertyFilter.filterOutAllExcept("*");

    public String writeValueAsString(JsonGenerator gen) throws IOException {
        TokenBuffer tokenBuffer = new TokenBuffer(gen);
        ObjectWriter writer = objectMapper.writer()
                .withView(SimpleBeanPropertyView.StandardExcluding(FilterProvider.defaultFilterInstance()))
                .withSerializerFilter(FILTER);
        writer.writeValue(tokenBuffer, gen.getUnderlyingOutput(), null);
        return tokenBuffer.asString();
    }
}