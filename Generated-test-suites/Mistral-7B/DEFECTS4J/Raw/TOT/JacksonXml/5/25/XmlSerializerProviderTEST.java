import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.TestUtil;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeNull() throws Exception {
        ObjectMapper mapper = new XmlMapper();
        XmlSerializerProvider xmlSerializerProvider = mapper.getXmlSerializerProvider();
        JsonGenerator gen = mock(JsonGenerator.class);

        when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        xmlSerializerProvider._serializeXmlNull(gen);

        String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<null/>";
        String actualXml = TestUtil.writeValueAsString(gen);
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeObject() throws Exception {
        ObjectMapper mapper = new XmlMapper();
        XmlSerializerProvider xmlSerializerProvider = mapper.getXmlSerializerProvider();
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();

        when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        SimpleBeanPropertyFilter propertyFilter = new SimpleBeanPropertyFilter("testProperty", null);
        SerializationConfig config = mapper.getSerializationConfig();
        config.setFilterProcessor(propertyFilter);

        xmlSerializerProvider.serializeValue(gen, value);

        String expectedXml = "<testProperty></testProperty>";
        String actualXml = TestUtil.writeValueAsString(gen);
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeArray() throws Exception {
        ObjectMapper mapper = new XmlMapper();
        XmlSerializerProvider xmlSerializerProvider = mapper.getXmlSerializerProvider();
        JsonGenerator gen = mock(JsonGenerator.class);
        Object[] array = new Object[3];
        for (int i = 0; i < array.length; i++) {
            array[i] = new Object();
        }

        when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        xmlSerializerProvider.serializeValue(gen, array, Object[].class, null);

        String expectedXml = "<item>\n<testProperty></testProperty>\n<item>\n<testProperty></testProperty>\n<item>\n<testProperty></testProperty>\n";
        String actualXml = TestUtil.writeValueAsString(gen);
        assertEquals(expectedXml, actualXml);
    }

    @Test
    public void testSerializeObjectWithRootName() throws Exception {
        ObjectMapper mapper = new XmlMapper();
        XmlSerializerProvider xmlSerializerProvider = mapper.getXmlSerializerProvider();
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        QName rootName = new QName("testRootName");

        when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        xmlSerializerProvider._initWithRootName((ToXmlGenerator) gen, rootName);
        xmlSerializerProvider.serializeValue(gen, value);

        String expectedXml = "<testRootName><testProperty></testProperty></testRootName>";
        String actualXml = TestUtil.writeValueAsString(gen);
        assertEquals(expectedXml, actualXml);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeObjectWithWrongRootName() throws Exception {
        ObjectMapper mapper = new XmlMapper();
        XmlSerializerProvider xmlSerializerProvider = mapper.getXmlSerializerProvider();
        JsonGenerator gen = mock(JsonGenerator.class);
        Object value = new Object();
        QName rootName = new QName("testRootName", "testNamespace");

        when(gen.getSerializerProvider()).thenReturn(xmlSerializerProvider);
        xmlSerializerProvider._initWithRootName((ToXmlGenerator) gen, rootName);
        xmlSerializerProvider.serializeValue(gen, value);

        // The test should fail with a JsonMappingException
    }
}