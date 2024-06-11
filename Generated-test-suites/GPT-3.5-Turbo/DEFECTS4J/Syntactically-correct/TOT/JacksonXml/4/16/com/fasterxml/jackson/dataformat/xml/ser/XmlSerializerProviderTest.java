import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLOutputFactory;

public class XmlSerializerProviderTest {

    private XmlMapper xmlMapper;

    @Before
    public void setup() {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        
        xmlMapper = new XmlMapper(module);
    }

    @Test
    public void testSerializeValue_WhenValueIsNull_ShouldSerializeXmlNull() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = xmlMapper.getFactory().createGenerator(writer);
        SerializerProvider provider = xmlMapper.getSerializerProvider();

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(provider, xmlMapper.getSerializationConfig(),
                xmlMapper.getSerializerFactory());

        // Act
        xmlProvider.serializeValue(generator, null);

        // Assert
        generator.flush();
        Assert.assertEquals("<null/>", writer.toString());
    }

    @Test
    public void testSerializeValue_WhenValueIsNotNull_ShouldSerializeValue() throws IOException,
            XMLStreamException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = xmlMapper.getFactory().createGenerator(writer);
        SerializerProvider provider = xmlMapper.getSerializerProvider();

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(provider, xmlMapper.getSerializationConfig(),
                xmlMapper.getSerializerFactory());

        // Act
        xmlProvider.serializeValue(generator, new TestObject("John", 25));

        // Assert
        generator.flush();
        Assert.assertEquals("<TestObject><name>John</name><age>25</age></TestObject>", writer.toString());
    }

    @Test
    public void testSerializeValue_WhenValueIsNullAndGeneratorIsNotXml_ShouldSerializeXmlNull() throws IOException {
        // Arrange
        StringWriter writer = new StringWriter();
        JsonGenerator generator = createNonXmlGenerator(writer);
        SerializerProvider provider = xmlMapper.getSerializerProvider();

        XmlSerializerProvider xmlProvider = new XmlSerializerProvider(provider, xmlMapper.getSerializationConfig(),
                xmlMapper.getSerializerFactory());

        // Act
        xmlProvider.serializeValue(generator, null);

        // Assert
        generator.flush();
        Assert.assertEquals("null", writer.toString());
    }

    private JsonGenerator createNonXmlGenerator(StringWriter writer) throws IOException {
        SerializerFactory factory = BeanSerializerBase.buildDefaultSerializerFactory();
        DefaultSerializerProvider provider = new DefaultSerializerProvider.Impl(factory);
        ObjectWriter writer = provider.writer();

        JsonGenerator generator = writer.getFactory().createGenerator(writer);
        generator.setCodec(xmlMapper);

        return generator;
    }
}

class TestObject {
    public String name;
    public int age;

    public TestObject(String name, int age) {
        this.name = name;
        this.age = age;
    }
}