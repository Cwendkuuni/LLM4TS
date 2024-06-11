
package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;

    @Before
    public void setUp() {
        XmlRootNameLookup xmlRootNameLookup = new XmlRootNameLookup();
        xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
    }

    @Test
    public void createInstanceTest() throws NoSuchFieldException, IllegalAccessException {
        DefaultSerializerProvider defaultSerializerProvider = (DefaultSerializerProvider) xmlSerializerProvider.createInstance(null, null);
        Field field = DefaultSerializerProvider.class.getDeclaredField("_rootNameLookup");
        field.setAccessible(true);
        XmlRootNameLookup rootNameLookup = (XmlRootNameLookup) field.get(defaultSerializerProvider);
        Assert.assertEquals(xmlSerializerProvider._rootNameLookup, rootNameLookup);
    }

    @Test
    public void serializeValueTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        XmlGeneratorStub xmlGeneratorStub = new XmlGeneratorStub();
        xmlSerializerProvider.serializeValue(xmlGeneratorStub, new Object());
        Method method = XmlGeneratorStub.class.getDeclaredMethod("writeStartObject");
        method.setAccessible(true);
        Assert.assertTrue((Boolean) method.invoke(xmlGeneratorStub));
    }

    @Test
    public void serializeValueTest_withRootType() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        XmlGeneratorStub xmlGeneratorStub = new XmlGeneratorStub();
        xmlSerializerProvider.serializeValue(xmlGeneratorStub, new Object(), new JavaTypeStub());
        Method method = XmlGeneratorStub.class.getDeclaredMethod("writeStartObject");
        method.setAccessible(true);
        Assert.assertTrue((Boolean) method.invoke(xmlGeneratorStub));
    }

    @Test
    public void serializeValueTest_withSerializer() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        XmlGeneratorStub xmlGeneratorStub = new XmlGeneratorStub();
        xmlSerializerProvider.serializeValue(xmlGeneratorStub, new Object(), new JavaTypeStub(), new JsonSerializerStub());
        Method method = XmlGeneratorStub.class.getDeclaredMethod("writeStartObject");
        method.setAccessible(true);
        Assert.assertTrue((Boolean) method.invoke(xmlGeneratorStub));
    }

    @Test
    public void serializeValueTest_withNullValue() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        XmlGeneratorStub xmlGeneratorStub = new XmlGeneratorStub();
        xmlSerializerProvider.serializeValue(xmlGeneratorStub, null);
        Method method = XmlGeneratorStub.class.getDeclaredMethod("writeStartObject");
        method.setAccessible(true);
        Assert.assertTrue((Boolean) method.invoke(xmlGeneratorStub));
    }

    @Test
    public void startRootArrayTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        XmlGeneratorStub xmlGeneratorStub = new XmlGeneratorStub();
        xmlSerializerProvider._startRootArray(xmlGeneratorStub, new QName("root"));
        Method method = XmlGeneratorStub.class.getDeclaredMethod("writeStartObject");
        method.setAccessible(true);
        Assert.assertTrue((Boolean) method.invoke(xmlGeneratorStub));
    }

    @Test
    public void initWithRootNameTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, NoSuchFieldException {
        XmlGeneratorStub xmlGeneratorStub = new XmlGeneratorStub();
        xmlSerializerProvider._initWithRootName(xmlGeneratorStub, new QName("root"));
        Method method = XmlGeneratorStub.class.getDeclaredMethod("initGenerator");
        method.setAccessible(true);
        Assert.assertTrue((Boolean) method.invoke(xmlGeneratorStub));
        Field field = XmlGeneratorStub.class.getDeclaredField("defaultNamespace");
        field.setAccessible(true);
        Assert.assertEquals("root", field.get(xmlGeneratorStub));
    }

    @Test
    public void rootNameFromConfigTest() throws NoSuchFieldException, IllegalAccessException {
        PropertyName propertyName = new PropertyName("property");
        Field field = XmlSerializerProvider.class.getDeclaredField("_config");
        field.setAccessible(true);
        SerializationConfig serializationConfig = new SerializationConfig(null);
        field.set(xmlSerializerProvider, serializationConfig);
        serializationConfig.setFullRootName(propertyName);
        QName rootName = (QName) invokePrivateMethod(xmlSerializerProvider, "_rootNameFromConfig");

        Assert.assertEquals(propertyName.getSimpleName(), rootName.getLocalPart());
        Assert.assertNull(rootName.getNamespaceURI());

        propertyName = new PropertyName("property", "http://example.com");
        serializationConfig.setFullRootName(propertyName);
        rootName = (QName) invokePrivateMethod(xmlSerializerProvider, "_rootNameFromConfig");

        Assert.assertEquals(propertyName.getSimpleName(), rootName.getLocalPart());
        Assert.assertEquals(propertyName.getNamespace(), rootName.getNamespaceURI());
    }

    @Test
    public void asXmlGeneratorTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        XmlGeneratorStub xmlGeneratorStub = new XmlGeneratorStub();
        ToXmlGenerator toXmlGenerator = xmlSerializerProvider._asXmlGenerator(xmlGeneratorStub);
        Assert.assertEquals(xmlGeneratorStub, toXmlGenerator);

        TokenBufferStub tokenBufferStub = new TokenBufferStub();
        toXmlGenerator = xmlSerializerProvider._asXmlGenerator(tokenBufferStub);
        Assert.assertNull(toXmlGenerator);

        try {
            xmlSerializerProvider._asXmlGenerator(new JsonGeneratorStub());
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            Assert.assertEquals(e.getMessage(), "XmlMapper does not with generators of type other than ToXmlGenerator; got: " + JsonGeneratorStub.class.getName());
        }
    }

    private Object invokePrivateMethod(Object object, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = object.getClass().getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(object);
    }
}

class XmlGeneratorStub extends ToXmlGenerator {

    public XmlGeneratorStub() throws IOException {
        super(null, 0, null, null);
    }

    @Override
    public void writeStartObject() {
    }

    @Override
    public void writeEndObject() {
    }

    @Override
    public void writeName(String name) {
    }

    @Override
    public void close() throws IOException {
    }
}

class JavaTypeStub extends JavaType {

    public JavaTypeStub() {
        super(null, 0, null, null);
    }
}

class JsonSerializerStub extends JsonSerializer<Object> {

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
    }
}

class TokenBufferStub extends TokenBuffer {

    public TokenBufferStub() {
        super(null, false);
    }
}

class JsonGeneratorStub extends JsonGenerator {

    @Override
    public void writeStartObject() {
    }

    @Override
    public void writeEndObject() {
    }

    @Override
    public void writeName(String name) {
    }

    @Override
    public void close() throws IOException {
    }
}
