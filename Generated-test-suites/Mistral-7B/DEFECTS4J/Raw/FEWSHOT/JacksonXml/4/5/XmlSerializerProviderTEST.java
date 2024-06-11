package com.fasterxml.jackson.dataformat.xml.ser;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.SerializerProviderDecorator;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertySerializerBase;
import com.fasterxml.jackson.databind.ser.impl.ValueSerializerBase;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import com.fasterxml.jackson.databind.util.XmlSerializerUtil;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue() throws Exception {
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        SerializerFactory factory = new SerializerFactory();
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = mock(JsonGenerator.class);

        Object value = new Object();
        Class<?> cls = value.getClass();
        JsonSerializer<Object> ser = new SimpleBeanPropertySerializerBase<>(config, factory, null, null, null, null);

        xmlSerializerProvider.serializeValue(gen, value);

        verify(gen).writeStartObject();
        verify(gen).writeFieldName("value");
        ser.serialize(value, gen, new DefaultSerializerProvider());
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeValueWithRootName() throws Exception {
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        SerializerFactory factory = new SerializerFactory();
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        when(rootNameLookup.findRootName(Mockito.any(Class.class), Mockito.any(SerializationConfig.class))).thenReturn(new QName("root"));
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = mock(JsonGenerator.class);

        Object value = new Object();
        Class<?> cls = value.getClass();
        JsonSerializer<Object> ser = new SimpleBeanPropertySerializerBase<>(config, factory, null, null, null, null);

        xmlSerializerProvider.serializeValue(gen, value);

        verify(gen).writeStartObject("root");
        verify(gen).writeFieldName("value");
        ser.serialize(value, gen, new DefaultSerializerProvider());
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeValueWithRootType() throws Exception {
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        SerializerFactory factory = new SerializerFactory();
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        when(rootNameLookup.findRootName(Mockito.any(JavaType.class), Mockito.any(SerializationConfig.class))).thenReturn(new QName("root"));
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = mock(JsonGenerator.class);
        JavaType rootType = new SimpleType("java.lang.Object");

        Object value = new Object();
        JsonSerializer<Object> ser = new SimpleBeanPropertySerializerBase<>(config, factory, null, null, null, null);

        xmlSerializerProvider.serializeValue(gen, value, rootType);

        verify(gen).writeStartObject("root");
        verify(gen).writeFieldName("value");
        ser.serialize(value, gen, new DefaultSerializerProvider());
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeValueWithRootTypeAndSerializer() throws Exception {
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        SerializerFactory factory = new SerializerFactory();
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        when(rootNameLookup.findRootName(Mockito.any(JavaType.class), Mockito.any(SerializationConfig.class))).thenReturn(new QName("root"));
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = mock(JsonGenerator.class);
        JavaType rootType = new SimpleType("java.lang.Object");
        JsonSerializer<Object> ser = new ValueSerializerBase<>(config, factory, null);

        Object value = new Object();

        xmlSerializerProvider.serializeValue(gen, value, rootType, ser);

        verify(gen).writeStartObject("root");
        ser.serialize(value, gen, new DefaultSerializerProvider());
        verify(gen).writeEndObject();
    }

    @Test
    public void testSerializeNullValue() throws Exception {
        SerializationConfig config = new SerializationConfig(new AnnotationIntrospector());
        SerializerFactory factory = new SerializerFactory();
        XmlRootNameLookup rootNameLookup = mock(XmlRootNameLookup.class);
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
        JsonGenerator gen = mock(JsonGenerator.class);

        xmlSerializerProvider.serializeValue(gen, null);

        verify(gen).writeStartObject(XmlSerializerProvider.ROOT_NAME_FOR_NULL.getLocalPart());
        verify(gen).writeNull();
        verify(gen).writeEndObject();
    }
}