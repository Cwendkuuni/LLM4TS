import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;
    private XmlRootNameLookup rootNameLookup;
    private SerializationConfig config;
    private SerializerFactory factory;
    private JsonGenerator jsonGenerator;
    private Object value;
    private JavaType rootType;
    private JsonSerializer<Object> ser;

    @Before
    public void setUp() {
        rootNameLookup = mock(XmlRootNameLookup.class);
        config = mock(SerializationConfig.class);
        factory = mock(SerializerFactory.class);
        jsonGenerator = mock(JsonGenerator.class);
        value = new Object();
        rootType = mock(JavaType.class);
        ser = mock(JsonSerializer.class);

        xmlSerializerProvider = new XmlSerializerProvider(rootNameLookup);
    }

    @Test
    public void testCreateInstance() {
        DefaultSerializerProvider instance = xmlSerializerProvider.createInstance(config, factory);
        assertNotNull(instance);
        assertNotSame(xmlSerializerProvider, instance);
        assertSame(xmlSerializerProvider._rootNameLookup, instance._rootNameLookup);
        assertSame(config, instance.getConfig());
        assertSame(factory, instance.getFactory());
    }

    @Test
    public void testSerializeValueWithNullValue() throws IOException {
        xmlSerializerProvider.serializeValue(jsonGenerator, null);

        verify(jsonGenerator).writeStartObject();
        verify(jsonGenerator).writeFieldName("null");

        verify(jsonGenerator).writeEndObject();
        verifyNoMoreInteractions(jsonGenerator);
    }

    @Test
    public void testSerializeValueWithNonNullValue() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(toXmlGenerator);
        when(rootNameLookup.findRootName(Object.class, config)).thenReturn(null);

        xmlSerializerProvider.serializeValue(jsonGenerator, value);

        verify(toXmlGenerator, never()).setNextNameIfMissing(any());
        verify(toXmlGenerator, never()).inRoot();
        verify(toXmlGenerator).initGenerator();
        verify(toXmlGenerator, never()).getStaxWriter().setDefaultNamespace(any());
        verify(jsonGenerator).writeEndObject();
        verifyNoMoreInteractions(jsonGenerator);
    }

    @Test
    public void testSerializeValueWithRootName() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(toXmlGenerator);
        when(rootNameLookup.findRootName(Object.class, config)).thenReturn(new QName("root"));

        xmlSerializerProvider.serializeValue(jsonGenerator, value);

        verify(toXmlGenerator).setNextNameIfMissing(any());
        verify(toXmlGenerator).inRoot();
        verify(toXmlGenerator).setNextName(any());
        verify(toXmlGenerator).initGenerator();
        verify(toXmlGenerator, never()).getStaxWriter().setDefaultNamespace(any());
        verify(jsonGenerator).writeEndObject();
        verifyNoMoreInteractions(jsonGenerator);
    }

    @Test
    public void testSerializeValueWithArrayValue() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(toXmlGenerator);
        when(rootNameLookup.findRootName(Object.class, config)).thenReturn(new QName("root"));
        when(rootType.isContainerType()).thenReturn(true);

        xmlSerializerProvider.serializeValue(jsonGenerator, value, rootType);

        verify(toXmlGenerator).setNextNameIfMissing(any());
        verify(toXmlGenerator).inRoot();
        verify(toXmlGenerator).setNextName(any());
        verify(toXmlGenerator).initGenerator();
        verify(toXmlGenerator, never()).getStaxWriter().setDefaultNamespace(any());
        verify(jsonGenerator).writeEndObject();
        verifyNoMoreInteractions(jsonGenerator);
    }

    @Test
    public void testSerializeValueWithNonNullValueAndRootType() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(xmlSerializerProvider._asXmlGenerator(jsonGenerator)).thenReturn(toXmlGenerator);
        when(rootNameLookup.findRootName(rootType, config)).thenReturn(new QName("root"));

        xmlSerializerProvider.serializeValue(jsonGenerator, value, rootType);

        verify(toXmlGenerator).setNextNameIfMissing(any());
        verify(toXmlGenerator).inRoot();
        verify(toXmlGenerator).setNextName(any());
        verify(toXmlGenerator).initGenerator();
        verify(toXmlGenerator, never()).getStaxWriter().setDefaultNamespace(any());
        verify(jsonGenerator).writeEndObject();
        verifyNoMoreInteractions(jsonGenerator);
    }

    @Test
    public void testSerializeValueWithSerializedValue() throws IOException {
        xmlSerializerProvider.serializeValue(jsonGenerator, value, rootType, ser);

        verify(jsonGenerator).writeEndObject();
        verifyNoMoreInteractions(jsonGenerator);
        verify(ser).serialize(eq(value), eq(jsonGenerator), eq(xmlSerializerProvider));
        verifyNoMoreInteractions(ser);
    }

    @Test
    public void testStartRootArray() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        QName rootName = new QName("root");

        xmlSerializerProvider._startRootArray(toXmlGenerator, rootName);

        verify(toXmlGenerator).writeStartObject();
        verify(toXmlGenerator).writeFieldName("item");
        verifyNoMoreInteractions(toXmlGenerator);
    }

    @Test
    public void testInitWithRootName() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        QName rootName = new QName("root");

        xmlSerializerProvider._initWithRootName(toXmlGenerator, rootName);

        verify(toXmlGenerator).setNextNameIfMissing(any());
        verify(toXmlGenerator).inRoot();
        verify(toXmlGenerator).setNextName(any());
        verify(toXmlGenerator).initGenerator();
        verify(toXmlGenerator, never()).getStaxWriter().setDefaultNamespace(any());
        verifyNoMoreInteractions(toXmlGenerator);
    }

    @Test
    public void testRootNameFromConfigWithNullConfig() {
        QName rootName = xmlSerializerProvider._rootNameFromConfig();
        assertNull(rootName);
    }

    @Test
    public void testRootNameFromConfigWithEmptyNamespace() {
        PropertyName propertyName = mock(PropertyName.class);
        when(config.getFullRootName()).thenReturn(propertyName);
        when(propertyName.getNamespace()).thenReturn("");

        QName rootName = xmlSerializerProvider._rootNameFromConfig();

        assertEquals(propertyName.getSimpleName(), rootName.getLocalPart());
        assertNull(rootName.getNamespaceURI());
    }

    @Test
    public void testRootNameFromConfig() {
        PropertyName propertyName = mock(PropertyName.class);
        when(config.getFullRootName()).thenReturn(propertyName);
        when(propertyName.getNamespace()).thenReturn("http://example.com");
        when(propertyName.getSimpleName()).thenReturn("root");

        QName rootName = xmlSerializerProvider._rootNameFromConfig();

        assertEquals(propertyName.getSimpleName(), rootName.getLocalPart());
        assertEquals(propertyName.getNamespace(), rootName.getNamespaceURI());
    }

    @Test(expected = JsonMappingException.class)
    public void testAsXmlGeneratorWithInvalidGenerator() throws JsonMappingException {
        JsonGenerator generator = mock(JsonGenerator.class);
        xmlSerializerProvider._asXmlGenerator(generator);
    }
}