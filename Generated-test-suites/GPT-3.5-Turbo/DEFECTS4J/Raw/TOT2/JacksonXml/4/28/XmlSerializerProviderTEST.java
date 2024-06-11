import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import javax.xml.namespace.QName;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider provider;

    @Before
    public void setup() {
        provider = new XmlSerializerProvider(new XmlSerializerProvider(new XmlRootNameLookup()));
    }

    @Test
    public void testCreateInstance() {
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new SerializerFactory();
        XmlSerializerProvider newProvider = provider.createInstance(config, factory);
        assertNotNull(newProvider);
    }

    @Test
    public void testSerializeValue() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonGenerator gen = new ToXmlGenerator(outputStream);
            Object value = "testValue";
            provider.serializeValue(gen, value);
            gen.close();
            String xmlOutput = new String(outputStream.toByteArray(), "UTF-8");
            assertTrue(xmlOutput.contains("<String>testValue</String>"));
        } catch (IOException e) {
            fail("Exception thrown during serialization");
        }
    }

    @Test
    public void testSerializeValueWithException() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator gen = new ToXmlGenerator(outputStream);
        Object value = new Object();
        try {
            provider.serializeValue(gen, value, JavaType.construct(Object.class));
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("Unexpected value for class " + Object.class.getName(), e.getMessage());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    public void testSerializeValueNull() {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JsonGenerator gen = new ToXmlGenerator(outputStream);
            Object value = null;
            provider.serializeValue(gen, value);
            gen.close();
            String xmlOutput = new String(outputStream.toByteArray(), "UTF-8");
            assertTrue(xmlOutput.contains("<null/>"));
        } catch (IOException e) {
            fail("Exception thrown during serialization");
        }
    }

    @Test
    public void testRootNameFromConfig() {
        QName rootName = provider._rootNameFromConfig();
        assertNull(rootName);
    }

    @Test
    public void testAsXmlGenerator() throws JsonMappingException {
        JsonGenerator gen = new ToXmlGenerator(new TokenBuffer());
        ToXmlGenerator xmlGen = provider._asXmlGenerator(gen);
        assertNotNull(xmlGen);
    }

}