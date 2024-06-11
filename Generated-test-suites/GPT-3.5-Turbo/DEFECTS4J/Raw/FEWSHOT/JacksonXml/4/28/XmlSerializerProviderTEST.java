import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {
    private XmlSerializerProvider provider;
    private DefaultSerializerProvider delegate;
    private XmlRootNameLookup rootNameLookup;
    private JsonGenerator gen;
    private Object value;
    private JavaType rootType;
    private JsonSerializer<Object> ser;

    @Before
    public void setUp() {
        rootNameLookup = mock(XmlRootNameLookup.class);
        delegate = mock(DefaultSerializerProvider.class);
        provider = new XmlSerializerProvider(rootNameLookup);
        gen = mock(JsonGenerator.class);
        value = mock(Object.class);
        rootType = mock(JavaType.class);
        ser = mock(JsonSerializer.class);
    }

    @Test
    public void test_createInstance() {
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory jsf = mock(SerializerFactory.class);
        XmlSerializerProvider result = provider.createInstance(config, jsf);
        assertEquals(XmlSerializerProvider.class, result.getClass());
        assertEquals(XmlSerializerProvider.class, result.getClass().getSuperclass());
    }

    @Test
    public void test_serializeValue() throws IOException {
        QName rootName = mock(QName.class);
        when(rootNameLookup.findRootName(value.getClass(), provider._config)).thenReturn(rootName);
        TypeUtil.isIndexedType(value.getClass());
        provider.serializeValue(gen, value);
        verify(gen).writeEndObject();
    }

    @Test
    public void test_serializeValue_withJavaType() throws IOException {
        QName rootName = mock(QName.class);
        when(rootNameLookup.findRootName(rootType, provider._config)).thenReturn(rootName);
        TypeUtil.isIndexedType(rootType);
        provider.serializeValue(gen, value, rootType);
        verify(gen).writeEndObject();
    }

    @Test
    public void test_serializeValue_withJavaTypeAndSerializer() throws IOException {
        QName rootName = mock(QName.class);
        when(rootNameLookup.findRootName(rootType, provider._config)).thenReturn(rootName);
        TypeUtil.isIndexedType(rootType);
        provider.serializeValue(gen, value, rootType, ser);
        verify(gen).writeEndObject();
    }

    @Test
    public void test__serializeXmlNull() throws IOException {
        when(gen instanceof ToXmlGenerator).thenReturn(true);
        provider._serializeXmlNull(gen);
        verify(gen).writeObject(null);
    }

    @Test
    public void test__startRootArray() throws IOException {
        QName rootName = mock(QName.class);
        provider._startRootArray(mock(ToXmlGenerator.class), rootName);
        verify(gen).writeEndObject();
    }

    @Test
    public void test__initWithRootName() throws IOException {
        QName rootName = mock(QName.class);
        provider._initWithRootName(mock(ToXmlGenerator.class), rootName);
        verify(gen).initGenerator();
    }

    @Test
    public void test__rootNameFromConfig() {
        PropertyName name = mock(PropertyName.class);
        provider._rootNameFromConfig();
        verify(rootNameLookup).findRootName(rootType, provider._config);
    }

    @Test
    public void test__asXmlGenerator() throws JsonMappingException {
        provider._asXmlGenerator(mock(TokenBuffer.class));
        verify(gen).getClass();
    }
}