import org.junit.Test;

import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

    @Test
    public void testCopy() {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        XmlSerializerProvider copy = provider.copy();
        assertNotNull(copy);
        assertNotSame(provider, copy);
    }

    @Test
    public void testCreateInstance() {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        XmlSerializerProvider instance = provider.createInstance(null, null);
        assertNotNull(instance);
        assertNotSame(provider, instance);
    }

    @Test
    public void testSerializeValue() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        XmlSerializerProvider spyProvider = Mockito.spy(provider);
        
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Object value = new Object();
        
        spyProvider.serializeValue(generator, value);
        
        Mockito.verify(spyProvider, Mockito.times(1))._serializeXmlNull(generator);
    }

    @Test
    public void testSerializeValueWithRootTypeAndSerializer() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        XmlSerializerProvider spyProvider = Mockito.spy(provider);
        
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Object value = new Object();
        JavaType rootType = Mockito.mock(JavaType.class);
        JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class);
        
        spyProvider.serializeValue(generator, value, rootType, serializer);
        
        Mockito.verify(spyProvider, Mockito.times(1))._serializeXmlNull(generator);
    }

    @Test
    public void test_serializeXmlNull() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        
        provider._serializeXmlNull(generator);
        
        Mockito.verify(generator, Mockito.times(1)).writeNull();
    }

    @Test
    public void test_startRootArray() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        ToXmlGenerator generator = Mockito.mock(ToXmlGenerator.class);
        QName rootName = new QName("test");
        
        provider._startRootArray(generator, rootName);
        
        Mockito.verify(generator, Mockito.times(1)).writeStartObject();
        Mockito.verify(generator, Mockito.times(1)).writeFieldName("item");
    }

    @Test
    public void test_initWithRootName() throws IOException {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        ToXmlGenerator generator = Mockito.mock(ToXmlGenerator.class);
        QName rootName = new QName("test");
        
        provider._initWithRootName(generator, rootName);
        
        Mockito.verify(generator, Mockito.times(1)).setNextNameIfMissing(rootName);
        Mockito.verify(generator, Mockito.times(1)).initGenerator();
        Mockito.verify(generator, Mockito.times(1)).getStaxWriter();
    }

    @Test
    public void test_rootNameFromConfig() {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        provider.configureRootName("test", "namespace");
        
        QName rootName = provider._rootNameFromConfig();
        
        assertEquals("test", rootName.getLocalPart());
        assertEquals("namespace", rootName.getNamespaceURI());
    }

    @Test
    public void test_asXmlGenerator() {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        
        ToXmlGenerator generator = Mockito.mock(ToXmlGenerator.class);
        ToXmlGenerator result = provider._asXmlGenerator(generator);
        
        assertSame(generator, result);
    }

    @Test
    public void test_asXmlGenerator_WithTokenBuffer() {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        
        TokenBuffer buffer = Mockito.mock(TokenBuffer.class);
        
        try {
            provider._asXmlGenerator(buffer);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("XmlMapper does not with generators of type other than ToXmlGenerator; got: com.fasterxml.jackson.databind.util.TokenBuffer", e.getMessage());
        }
    }

    @Test
    public void test_wrapAsIOE() {
        XmlSerializerProvider provider = new XmlSerializerProvider();
        JsonGenerator generator = Mockito.mock(JsonGenerator.class);
        Exception exception = new Exception("Test exception");
        
        IOException result = provider._wrapAsIOE(generator, exception);
        
        assertNotSame(exception, result);
        assertEquals("Test exception", result.getMessage());
    }

}