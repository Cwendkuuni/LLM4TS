package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlRootNameLookup;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue_NullValue_SerializeXmlNullCalled() throws IOException, XMLStreamException {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator generator = new ToXmlGenerator(new StringWriter(), new XmlMapper(),
                new StaxUtil.StaxWriterDelegate(null, null), null, new JsonFactory(),
                new SerializerFactoryConfig());
        provider.init(generator);
        GsonJsonSerializer serializer = new GsonJsonSerializer();
        JsonGenerator jsonGen = new TokenBuffer(provider, false, false).asJsonGenerator();
        provider.assignGenerator(jsonGen, generator);
        jsonGen.flush();
        Object value = null;

        // Act
        provider.serializeValue(jsonGen, value);

        // Assert
        assertEquals("serializeXmlNull should be called when value is null", 1, serializer.getSerializeXmlNullCallCount());
    }

    @Test
    public void testSerializeValue_NonNullValue_SerializeXmlNullNotCalled() throws IOException, XMLStreamException {
        // Arrange
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        ToXmlGenerator generator = new ToXmlGenerator(new StringWriter(), new XmlMapper(),
                new StaxUtil.StaxWriterDelegate(null, null), null, new JsonFactory(),
                new SerializerFactoryConfig());
        provider.init(generator);
        GsonJsonSerializer serializer = new GsonJsonSerializer();
        JsonGenerator jsonGen = new TokenBuffer(provider, false, false).asJsonGenerator();
        provider.assignGenerator(jsonGen, generator);
        jsonGen.flush();
        Object value = new Object();

        // Act
        provider.serializeValue(jsonGen, value);

        // Assert
        assertEquals("serializeXmlNull should not be called when value is not null", 0, serializer.getSerializeXmlNullCallCount());
    }

    private static class GsonJsonSerializer extends StdSerializer<Object> {

        private int serializeXmlNullCallCount;

        protected GsonJsonSerializer() {
            super(Object.class);
        }

        public int getSerializeXmlNullCallCount() {
            return serializeXmlNullCallCount;
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
        }

        @Override
        public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider,
                TypeSerializer typeSer) throws IOException {
        }

        @Override
        public void serializeTypedFields(Object value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
        }

        @Override
        public void serializeFields(Object value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
        }

        @Override
        public String toString() {
            return "GsonJsonSerializer";
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider,
                JsonSerializer<Object> ser) throws IOException {
        }

        @Override
        public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider,
                JsonSerializer<Object> ser, TypeSerializer typeSer) throws IOException {
        }

        @Override
        public boolean isEmpty(SerializerProvider provider, Object value) {
            return false;
        }

        @Override
        public boolean usesObjectId() {
            return false;
        }

        @Override
        public boolean handlesArray() {
            return false;
        }

        @Override
        public boolean handlesWriteAsObject() {
            return false;
        }

        @Override
        public void serializePolymorphic(Object value, JsonGenerator gen, SerializerProvider provider,
                TypeSerializer typeSer, JsonSerializer<Object> ser) throws IOException {
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                throws JsonMappingException {
        }

        @Override
        protected boolean _containsTypeId(SerializerProvider provider) {
            return false;
        }

        @Override
        protected Object _getReferenced(Object value, JsonGenerator gen, SerializerProvider provider) {
            return null;
        }

        @Override
        protected boolean _isValuePresent(SerializerProvider provider) {
            return false;
        }

        @Override
        protected void _serializeNull(Object value, JsonGenerator gen, SerializerProvider provider)
                throws IOException {
            serializeXmlNullCallCount++;
        }
    }

    // other test methods

}