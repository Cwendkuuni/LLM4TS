import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
        SerializationConfig config = new SerializationConfig(xmlSerializerProvider);
        SerializerFactory jsf = null;
        DefaultSerializerProvider instance = xmlSerializerProvider.createInstance(config, jsf);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testSerializeValue() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
        JsonGenerator gen = null;
        
        try {
            xmlSerializerProvider.serializeValue(gen, null);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testSerializeValueWithRootType() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
        JsonGenerator gen = null;
        JavaType rootType = null;
        
        try {
            xmlSerializerProvider.serializeValue(gen, null, rootType);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testSerializeValueWithRootTypeAndSerializer() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
        JsonGenerator gen = null;
        JavaType rootType = null;
        JsonSerializer<Object> ser = null;
        
        try {
            xmlSerializerProvider.serializeValue(gen, null, rootType, ser);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void test_serializeXmlNull() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
        JsonGenerator jgen = null;
        
        try {
            xmlSerializerProvider._serializeXmlNull(jgen);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void test_startRootArray() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
        ToXmlGenerator xgen = null;
        QName rootName = null;
        
        try {
            xmlSerializerProvider._startRootArray(xgen, rootName);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void test_initWithRootName() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
        ToXmlGenerator xgen = null;
        QName rootName = null;
        
        try {
            xmlSerializerProvider._initWithRootName(xgen, rootName);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void test_rootNameFromConfig() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
        
        QName qName = xmlSerializerProvider._rootNameFromConfig();
        
        assertNull(qName);
    }
    
    @Test
    public void test_asXmlGenerator() {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider();
        JsonGenerator gen = null;
        
        try {
            xmlSerializerProvider._asXmlGenerator(gen);
        } catch (JsonMappingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}