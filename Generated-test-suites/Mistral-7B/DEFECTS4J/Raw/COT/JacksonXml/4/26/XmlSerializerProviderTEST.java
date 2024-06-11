package com.fasterxml.jackson.dataformat.xml.ser; // Import the required packages

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

import org.junit.*;
import org.junit.platform.commons.util.StringUtils;

/**
 * Test cases for XmlSerializerProvider class.
 */
public class XmlSerializerProviderTest {
    // Test data
    private static final Object NULL_VALUE = null;
    private static final Object INT_VALUE = 123;
    private static final Object STRING_VALUE = "test";
    private static final Object LIST_VALUE = new Object[] { INT_VALUE, STRING_VALUE };

    private static final JavaType LIST_TYPE = ObjectMapper.getDefaultInstance().getTypeFactory()
            .constructCollectionType(List.class, Integer.class);

    private static final Object MAP_VALUE = new Object() {
        private final Map<String, Object> map = new HashMap<>();
        static {
            map.put("intKey", INT_VALUE);
            map.put("stringKey", STRING_VALUE);
        }

        @Override
        public Map<String, Object> getMap() {
            return map;
        }
    };

    private static final AnnotatedClass<XmlSerializerProvider> ANNOTATED_CLASS = AnnotationIntrospector.findClass(XmlSerializerProvider.class);
    private static final AnnotatedConstructor<XmlSerializerProvider> ANNOTATED_CONSTRUCTOR;
    private static final AnnotatedMethod<XmlSerializerProvider, SerializerProvider> ANNOTATED_SERIALIZER_PROVIDER;

    static {
        ANNOTATED_CONSTRUCTOR = ANNOTATED_CLASS.getConstructors().next();
        ANNOTATED_SERIALIZER_PROVIDER = ANNOTATED_CLASS.getMethod("getSerializerProvider");
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup(); // Create a test root name lookup
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup); // Call the constructor
        assertNotNull(xmlSerializerProvider); // Check if the instance is not null
    }

    /**
     * Test createInstance method.
     */
    @Test
    public void testCreateInstance() {
        XmlRootNameLookup rootNameLookup = new XmlRootNameLookup();
        SerializerProvider serializerProvider = new DefaultSerializerProvider();
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup, _config, serializerProvider);
        assertEquals(xmlSerializerProvider.getClass(), XmlSerializerProvider.class);
    }

    /**
     * Test serializeValue method with null value.
     */
    @Test
    public void testSerializeValue_Null() throws JsonProcessingException, XMLStreamException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        JsonGenerator gen = new ToXmlGenerator();
        xmlSerializerProvider.serializeValue(gen, NULL_VALUE); // Call the method
        String result = StaxUtil.toString(gen.getStaxWriter().getXMLStreamWriter()); // Get the result as a string
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?><null xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>", result);
    }

    /**
     * Test serializeValue method with int value.
     */
    @Test
    public void testSerializeValue_Int() throws JsonProcessingException, XMLStreamException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        JsonGenerator gen = new ToXmlGenerator();
        xmlSerializerProvider.serializeValue(gen, INT_VALUE); // Call the method
        String result = StaxUtil.toString(gen.getStaxWriter().getXMLStreamWriter()); // Get the result as a string
        assertEquals("<int xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">123</int>", result);
    }

    /**
     * Test serializeValue method with string value.
     */
    @Test
    public void testSerializeValue_String() throws JsonProcessingException, XMLStreamException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        JsonGenerator gen = new ToXmlGenerator();
        xmlSerializerProvider.serializeValue(gen, STRING_VALUE); // Call the method
        String result = StaxUtil.toString(gen.getStaxWriter().getXMLStreamWriter()); // Get the result as a string
        assertEquals("<string xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">test</string>", result);
    }

    /**
     * Test serializeValue method with list value.
     */
    @Test
    public void testSerializeValue_List() throws JsonProcessingException, XMLStreamException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        JsonGenerator gen = new ToXmlGenerator();
        xmlSerializerProvider.serializeValue(gen, LIST_VALUE); // Call the method
        String result = StaxUtil.toString(gen.getStaxWriter().getXMLStreamWriter()); // Get the result as a string
        assertEquals("<array xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "<item><int xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">123</int></item>" +
                "<item><string xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">test</string></item>" +
                "</array>", result);
    }

    /**
     * Test serializeValue method with map value.
     */
    @Test
    public void testSerializeValue_Map() throws JsonProcessingException, XMLStreamException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        JsonGenerator gen = new ToXmlGenerator();
        xmlSerializerProvider.serializeValue(gen, MAP_VALUE); // Call the method
        String result = StaxUtil.toString(gen.getStaxWriter().getXMLStreamWriter()); // Get the result as a string
        assertEquals("<map xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "<entry><key xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">intKey</key>" +
                "<value><int xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">123</int></value>" +
                "</entry>" +
                "<entry><key xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">stringKey</key>" +
                "<value><string xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">test</string></value>" +
                "</entry>" +
                "</map>", result);
    }

    /**
     * Test edge cases and exception handling scenarios.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionHandling() throws JsonMappingException {
        XmlSerializerProvider xmlSerializerProvider = new XmlSerializerProvider(new XmlRootNameLookup());
        JsonGenerator gen = null; // Null generator
        xmlSerializerProvider.serializeValue(gen, INT_VALUE); // Call the method

        gen = new ToXmlGenerator();
        xmlSerializerProvider.setSerializerProvider(null); // Null serializer provider
        xmlSerializerProvider.serializeValue(gen, INT_VALUE); // Call the method

        gen = new ToXmlGenerator();
        xmlSerializerProvider.serializeValue(gen, new Object()); // Unsupported object type
    }
}