import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

@RunWith(MockitoJUnitRunner.class)
public class XmlSerializerProviderTest {

    @Mock
    private JsonGenerator mockJsonGenerator;
    @Mock
    private DefaultSerializerProvider mockDefaultSerializerProvider;
    @Mock
    private SerializationConfig mockSerializationConfig;
    @Mock
    private SerializerFactory mockSerializerFactory;
    @Mock
    private JavaType mockJavaType;
    @Mock
    private JsonSerializer<Object> mockJsonSerializer;
    @Mock
    private QName mockQName;
    @Mock
    private PropertyName mockPropertyName;
    @Mock
    private ToXmlGenerator mockToXmlGenerator;

    private XmlSerializerProvider xmlSerializerProvider;
    private XmlRootNameLookup xmlRootNameLookup;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        xmlRootNameLookup = new XmlRootNameLookup();
        xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookup);
    }

    @Test
    public void testConstructor() {
        assertEquals(xmlRootNameLookup, xmlSerializerProvider._rootNameLookup);
    }

    @Test
    public void testCopy() {
        XmlSerializerProvider copy = xmlSerializerProvider.copy();
        assertEquals(xmlSerializerProvider._rootNameLookup, copy._rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider instance = xmlSerializerProvider.createInstance(mockSerializationConfig,
                mockSerializerFactory);
        assertEquals(xmlSerializerProvider._rootNameLookup, instance._rootNameLookup);
    }

    @Test
    public void test_serializeValue_withNullObject() throws Exception {
        when(mockJsonGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockJsonSerializer.serialize(any(), any(JsonGenerator.class), any(XmlSerializerProvider.class)))
                .thenThrow(new NullPointerException());
        xmlSerializerProvider.serializeValue(mockJsonGenerator, null);
        // Verify method calls
        assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, xmlRootNameLookup.findRootName(mockJavaType,
                mockSerializationConfig));
        assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, mockQName);
        assertNull(mockToXmlGenerator.setNextNameIfMissing(mockQName));
        assertNull(mockToXmlGenerator.getStaxWriter());
    }

    @Test
    public void test_serializeValue_withNonNullObject() throws Exception {
        when(mockJsonGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockJsonSerializer.serialize(any(), any(JsonGenerator.class), any(XmlSerializerProvider.class)))
                .thenThrow(new NullPointerException());
        xmlSerializerProvider.serializeValue(mockJsonGenerator, new Object());
        // Verify method calls
        assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, xmlRootNameLookup.findRootName(mockJavaType,
                mockSerializationConfig));
        assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, mockQName);
        assertNull(mockToXmlGenerator.setNextNameIfMissing(mockQName));
        assertNull(mockToXmlGenerator.getStaxWriter());
    }

    @Test
    public void test_serializeValue_withRootTypeAndSerializer() throws Exception {
        when(mockJsonGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockJsonSerializer.serialize(any(), any(JsonGenerator.class), any(XmlSerializerProvider.class)))
                .thenThrow(new NullPointerException());
        xmlSerializerProvider.serializeValue(mockJsonGenerator, new Object(), mockJavaType, mockJsonSerializer);
        // Verify method calls
        assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, xmlRootNameLookup.findRootName(mockJavaType,
                mockSerializationConfig));
        assertEquals(XmlSerializerProvider.ROOT_NAME_FOR_NULL, mockQName);
        assertNull(mockToXmlGenerator.setNextNameIfMissing(mockQName));
        assertNull(mockToXmlGenerator.getStaxWriter());
    }

    @Test(expected = JsonMappingException.class)
    public void test_serializeValue_withException() throws Exception {
        when(mockJsonGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockJsonSerializer.serialize(any(), any(JsonGenerator.class), any(XmlSerializerProvider.class)))
                .thenThrow(new IllegalArgumentException());
        xmlSerializerProvider.serializeValue(mockJsonGenerator, new Object());
    }

    @Test
    public void test_startRootArray() throws Exception {
        xmlSerializerProvider._startRootArray(mockToXmlGenerator, mockQName);
        // Verify method calls
        assertEquals(mockToXmlGenerator, mockToXmlGenerator.writeStartObject());
        assertEquals(mockToXmlGenerator, mockToXmlGenerator.writeFieldName("item"));
    }

    @Test
    public void test_initWithRootName() throws Exception {
        xmlSerializerProvider._initWithRootName(mockToXmlGenerator, mockQName);
        // Verify method calls
        assertEquals(mockToXmlGenerator, mockToXmlGenerator.initGenerator());
        assertEquals(mockToXmlGenerator, mockToXmlGenerator.setNextNameIfMissing(mockQName));
        assertEquals(mockToXmlGenerator.getStaxWriter(), mockToXmlGenerator.getStaxWriter());
    }

    @Test
    public void test_rootNameFromConfig_withNullName() {
        assertNull(xmlSerializerProvider._rootNameFromConfig());
    }

    @Test
    public void test_rootNameFromConfig_withInvalidName() {
        when(mockPropertyName.getNamespace()).thenReturn(null);
        when(mockPropertyName.getSimpleName()).thenReturn("name");
        when(mockSerializationConfig.getFullRootName()).thenReturn(mockPropertyName);
        assertEquals(new QName("name"), xmlSerializerProvider._rootNameFromConfig());
    }

    @Test
    public void test_rootNameFromConfig_withNamespace() {
        when(mockPropertyName.getNamespace()).thenReturn("namespace");
        when(mockPropertyName.getSimpleName()).thenReturn("name");
        when(mockSerializationConfig.getFullRootName()).thenReturn(mockPropertyName);
        assertEquals(new QName("namespace", "name"), xmlSerializerProvider._rootNameFromConfig());
    }

    @Test(expected = JsonMappingException.class)
    public void test_asXmlGenerator_withInvalidGenerator() throws Exception {
        xmlSerializerProvider._asXmlGenerator(mockJsonGenerator);
    }

    @Test
    public void test_asXmlGenerator_withValidGenerator() throws Exception {
        ToXmlGenerator generator = xmlSerializerProvider._asXmlGenerator(mockToXmlGenerator);
        assertEquals(mockToXmlGenerator, generator);
    }

    @Test
    public void test_wrapAsIOE_withIOException() throws Exception {
        IOException exception = new IOException();
        assertEquals(exception, xmlSerializerProvider._wrapAsIOE(mockJsonGenerator, exception));
    }

    @Test
    public void test_wrapAsIOE_withException() throws Exception {
        Exception exception = new Exception();
        assertEquals(JsonMappingException.class, xmlSerializerProvider._wrapAsIOE(mockJsonGenerator, exception).getClass());
    }
}