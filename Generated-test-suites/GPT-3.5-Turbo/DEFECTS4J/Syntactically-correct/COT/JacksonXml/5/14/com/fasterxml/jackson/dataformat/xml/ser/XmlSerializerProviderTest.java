
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {
    
    @Test
    public void testConstructor() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        assertNotNull(provider);
        assertEquals(rootNames, provider._rootNameLookup);
    }
    
    @Test
    public void testCopy() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        DefaultSerializerProvider copy = provider.copy();
        assertNotNull(copy);
        assertTrue(copy instanceof XmlSerializerProvider);
    }
    
    @Test
    public void testCreateInstance() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider src = new XmlSerializerProvider(rootNames);
        SerializationConfig config = new SerializationConfig();
        SerializerFactory factory = new SerializerFactory();
        XmlSerializerProvider provider = new XmlSerializerProvider(src, config, factory);
        assertNotNull(provider);
        assertEquals(rootNames, provider._rootNameLookup);
    }
    
    @Test
    public void testSerializeValue() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        try {
            provider.serializeValue(gen, value);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void testSerializeValueWithRootType() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = new JsonGenerator();
        Object value = new Object();
        JavaType rootType = new JavaType();
        JsonSerializer<Object> ser = new JsonSerializer<>();
        try {
            provider.serializeValue(gen, value, rootType, ser);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void test_serializeXmlNull() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        JsonGenerator gen = new JsonGenerator();
        try {
            provider._serializeXmlNull(gen);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void test_startRootArray() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("root");
        try {
            provider._startRootArray(xgen, rootName);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void test_initWithRootName() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        ToXmlGenerator xgen = new ToXmlGenerator();
        QName rootName = new QName("root");
        try {
            provider._initWithRootName(xgen, rootName);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }
    
    @Test
    public void test_rootNameFromConfig() {
        XmlRootNameLookup rootNames = new XmlRootNameLookup();
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNames);
        PropertyName name = new PropertyName("root");
        provider._config = new SerializationConfig();
        provider._config.setFullRootName(name);
        QName rootName = provider._rootNameFromConfig();
        assertNotNull(rootName);
        assertEquals("root", rootName.getLocalPart());
    }
    
    @Test
    public void test_wrapAsIOE() {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        JsonGenerator gen = new JsonGenerator();
        Exception e = new Exception();
        try {
            provider._wrapAsIOE(gen, e);
        } catch (IOException ex) {
            fail("Unexpected IOException");
        }
    }
}
