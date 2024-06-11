import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProviders;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link XmlSerializerProvider} class.
 */
public class XmlSerializerProviderTest {

    private ObjectMapper jsonMapper;
    private XmlMapper xmlMapper;
    private SerializerProvider jsonProvider;
    private XmlSerializerProvider xmlProvider;

    @Before
    public void setup() {
        final JsonFactory jsonFactory = new JsonFactory();
        jsonMapper = new ObjectMapper(jsonFactory);
        jsonProvider = jsonMapper.getSerializerProvider();

        final XmlFactory xmlFactory = new XmlFactory();
        xmlMapper = new XmlMapper(xmlFactory);
        xmlProvider = xmlMapper.getSerializerProvider();
    }

    @After
    public void teardown() throws IOException {
        jsonMapper.close();
        xmlMapper.close();
    }

    /**
     * Tests {@link XmlSerializerProvider#createInstance(SerializationConfig, SerializerFactory)} method.
     */
    @Test
    public void testCreateInstance() {
        final SerializationConfig config = new SerializationConfig(jsonMapper.getConfig());
        final SerializerFactory factory = jsonProvider.getFactory();
        final XmlSerializerProvider provider = xmlProvider.copy();
        provider.setRootNameLookup(new XmlRootNameLookup(xmlMapper));
        final SerializerProvider newProvider = provider.createInstance(config, factory);

        assertEquals("Should be an instance of XmlSerializerProvider", XmlSerializerProvider.class, newProvider.getClass());
    }

    /**
     * Tests {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object)} method when the value is null.
     */
    @Test
    public void testSerializeNull() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final JsonGenerator jsonGenerator = jsonProvider.getGenerator(outputStream, null);
        final XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper));

        provider.serializeValue(jsonGenerator, null);
        jsonGenerator.flush();

        final String xml = outputStream.toString();
        assertEquals("Expected <null>", "<null>", xml);
    }

    /**
     * Tests {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object)} method when the value is an empty array.
     */
    @Test
    public void testSerializeEmptyArray() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final JsonGenerator jsonGenerator = jsonProvider.getGenerator(outputStream, null);
        final XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper));

        provider.serializeValue(jsonGenerator, new Object[0]);
        jsonGenerator.flush();

        final String xml = outputStream.toString();
        assertEquals("Expected <arrayName><></arrayName>", "<arrayName></arrayName>", xml);
    }

    /**
     * Tests {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object)} method when the value is an array with one element.
     */
    @Test
    public void testSerializeArrayWithOneElement() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final JsonGenerator jsonGenerator = jsonProvider.getGenerator(outputStream, null);
        final XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper));

        final int[] array = new int[]{1};
        provider.serializeValue(jsonGenerator, array);
        jsonGenerator.flush();

        final String xml = outputStream.toString();
        assertEquals("Expected <arrayName><int>1</int></arrayName>", "<arrayName><int>1</int></arrayName>", xml);
    }

    /**
     * Tests {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object)} method when the value is an instance of a custom class.
     */
    @Test
    public void testSerializeCustomClass() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final JsonGenerator jsonGenerator = jsonProvider.getGenerator(outputStream, null);
        final XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper));

        final CustomClass customClass = new CustomClass();
        provider.serializeValue(jsonGenerator, customClass);
        jsonGenerator.flush();

        final String xml = outputStream.toString();
        assertEquals("Expected <customClass><field1>defaultValue</field1></customClass>", "<customClass><field1>defaultValue</field1></customClass>", xml);
    }

    /**
     * Tests {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType)} method when the value is null.
     */
    @Test
    public void testSerializeNullWithJavaType() throws IOException {
        final StringWriter outputStream = new StringWriter();
        final JsonGenerator jsonGenerator = xmlProvider.getGenerator(outputStream, JavaType.construct(int[].class));
        final XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper));

        provider.serializeValue(jsonGenerator, null);
        jsonGenerator.flush();

        assertNull("Expected null", outputStream.toString());
    }

    /**
     * Tests {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType, JsonSerializer)} method when the value is null.
     */
    @Test
    public void testSerializeNullWithJavaTypeAndJsonSerializer() throws IOException {
        final StringWriter outputStream = new StringWriter();
        final JsonGenerator jsonGenerator = xmlProvider.getGenerator(outputStream, JavaType.construct(int[].class), null);
        final XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper));

        provider.serializeValue(jsonGenerator, null, null);
        jsonGenerator.flush();

        assertNull("Expected null", outputStream.toString());
    }

    /**
     * Tests {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType)} method when the value is an empty array.
     */
    @Test
    public void testSerializeEmptyArrayWithJavaType() throws IOException {
        final StringWriter outputStream = new StringWriter();
        final JsonGenerator jsonGenerator = xmlProvider.getGenerator(outputStream, JavaType.construct(int[].class));
        final XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper));

        provider.serializeValue(jsonGenerator, new Object[0]);
        jsonGenerator.flush();

        assertEquals("Expected <arrayName></arrayName>", "<arrayName></arrayName>", outputStream.toString());
    }

    /**
     * Tests {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType, JsonSerializer)} method when the value is an array with one element.
     */
    @Test
    public void testSerializeArrayWithOneElementWithJavaType() throws IOException {
        final StringWriter outputStream = new StringWriter();
        final JsonGenerator jsonGenerator = xmlProvider.getGenerator(outputStream, JavaType.construct(int[].class));
        final XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper));

        final int[] array = new int[]{1};
        provider.serializeValue(jsonGenerator, array);
        jsonGenerator.flush();

        assertEquals("Expected <arrayName><int>1</int></arrayName>", "<arrayName><int>1</int></arrayName>", outputStream.toString());
    }

    /**
     * Tests {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType, JsonSerializer)} method when the value is an instance of a custom class.
     */
    @Test
    public void testSerializeCustomClassWithJavaType() throws IOException {
        final StringWriter outputStream = new StringWriter();
        final JsonGenerator jsonGenerator = xmlProvider.getGenerator(outputStream, JavaType.construct(CustomClass.class));
        final XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper));

        final CustomClass customClass = new CustomClass();
        provider.serializeValue(jsonGenerator, customClass);
        jsonGenerator.flush();

        final String xml = outputStream.toString();
        assertEquals("Expected <customClass><field1>defaultValue</field1></customClass>", "<customClass><field1>defaultValue</field1></customClass>", xml);
    }

    /**
     * Tests {@link XmlSerializerProvider#serializeValue(JsonGenerator, Object, JavaType, JsonSerializer)} method when the JsonSerializer is provided.
     */
    @Test
    public void testSerializeCustomClassWithJsonSerializer() throws IOException {
        final StringWriter outputStream = new StringWriter();
        final JsonGenerator jsonGenerator = xmlProvider.getGenerator(outputStream, JavaType.construct(CustomClass.class), new CustomSerializer());
        final XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup(xmlMapper));

        final CustomClass customClass = new CustomClass();
        provider.serializeValue(jsonGenerator, customClass);
        jsonGenerator.flush();

        final String xml = outputStream.toString();
        assertEquals("Expected <customClass><field1>customValue</field1></customClass>", "<customClass><field1>customValue</field1></customClass>", xml);
    }

    private static class CustomClass {
        private String field1 = "defaultValue";

        public CustomClass() {
        }

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }
    }

    private static class CustomSerializer extends JsonSerializer<CustomClass> {
        @Override
        public void serialize(CustomClass customClass, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeFieldName("field1");
            jsonGenerator.writeValue("customValue");
            jsonGenerator.writeEndObject();
        }
    }
}