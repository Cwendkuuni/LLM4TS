import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {

    private XmlSerializerProvider xmlSerializerProvider;

    @Mock
    private XmlRootNameLookup xmlRootNameLookupMock;

    @Mock
    private DefaultSerializerProvider defaultSerializerProviderMock;

    @Mock
    private ToXmlGenerator toXmlGeneratorMock;

    @Mock
    private JsonGenerator jsonGeneratorMock;

    private QName rootNameMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        rootNameMock = new QName("mock");
        xmlSerializerProvider = new XmlSerializerProvider(xmlRootNameLookupMock);
    }

    @Test
    public void testSerializeValueWithNull() throws IOException {
        xmlSerializerProvider.serializeValue(jsonGeneratorMock, null);
        verify(jsonGeneratorMock).writeStartObject();
        verify(xmlSerializerProvider).serializeValue(jsonGeneratorMock, null);
        verify(jsonGeneratorMock, times(1)).writeEndObject();
    }

    @Test
    public void testSerializeValueWithObjectAndRootNameFromConfig() throws IOException {
        when(toXmlGeneratorMock.inRoot()).thenReturn(true);
        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootNameMock);
        when(xmlSerializerProvider._asXmlGenerator(jsonGeneratorMock)).thenReturn(toXmlGeneratorMock);

        xmlSerializerProvider.serializeValue(jsonGeneratorMock, new Object());

        verify(toXmlGeneratorMock).setNextNameIfMissing(rootNameMock);
        verify(toXmlGeneratorMock, times(1)).initGenerator();
        verify(toXmlGeneratorMock).getStaxWriter();
        verify(toXmlGeneratorMock).writeStartObject();
        verify(toXmlGeneratorMock).writeFieldName(anyString());
        verify(toXmlGeneratorMock, never()).setDefaultNamespace(anyString());

    }

    @Test
    public void testSerializeValueWithObjectAndNullRootNameFromConfig() throws IOException {
        when(toXmlGeneratorMock.inRoot()).thenReturn(true);
        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(null);
        when(xmlSerializerProvider._asXmlGenerator(jsonGeneratorMock)).thenReturn(toXmlGeneratorMock);

        xmlSerializerProvider.serializeValue(jsonGeneratorMock, new Object());

        verify(toXmlGeneratorMock).setNextNameIfMissing(rootNameMock);
        verify(toXmlGeneratorMock, times(1)).initGenerator();
        verify(toXmlGeneratorMock).getStaxWriter();
        verify(toXmlGeneratorMock).writeStartObject();
        verify(toXmlGeneratorMock).writeFieldName(anyString());
        verify(toXmlGeneratorMock, never()).setDefaultNamespace(anyString());

    }

    @Test
    public void testSerializeValueWithObjectAndRootNameFromLookup() throws IOException {
        when(toXmlGeneratorMock.inRoot()).thenReturn(true);
        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(null);
        when(xmlSerializerProvider._asXmlGenerator(jsonGeneratorMock)).thenReturn(toXmlGeneratorMock);
        when(xmlRootNameLookupMock.findRootName(any(Class.class), any(SerializationConfig.class))).thenReturn(rootNameMock);

        xmlSerializerProvider.serializeValue(jsonGeneratorMock, new Object());

        verify(toXmlGeneratorMock).setNextNameIfMissing(rootNameMock);
        verify(toXmlGeneratorMock, times(1)).initGenerator();
        verify(toXmlGeneratorMock).getStaxWriter();
        verify(toXmlGeneratorMock).writeStartObject();
        verify(toXmlGeneratorMock).writeFieldName(anyString());
        verify(toXmlGeneratorMock, never()).setDefaultNamespace(anyString());

    }

    @Test
    public void testSerializeValueWithObjectAndArray() throws IOException {
        when(toXmlGeneratorMock.inRoot()).thenReturn(false);
        when(toXmlGeneratorMock.setNextNameIfMissing(rootNameMock)).thenReturn(false);
        when(toXmlGeneratorMock.setNextName(rootNameMock)).thenReturn(true);
        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootNameMock);
        when(xmlSerializerProvider._asXmlGenerator(jsonGeneratorMock)).thenReturn(toXmlGeneratorMock);
        when(xmlSerializerProvider.findTypedValueSerializer(Mockito.<Class<?>> any(), anyBoolean(), isNull())).thenReturn(mock(JsonSerializer.class));

        xmlSerializerProvider.serializeValue(jsonGeneratorMock, new Object[] {});

        verify(toXmlGeneratorMock, never()).setNextNameIfMissing(rootNameMock);
        verify(toXmlGeneratorMock).initGenerator();
        verify(toXmlGeneratorMock, times(1)).getStaxWriter();
        verify(toXmlGeneratorMock).writeStartObject();
        verify(toXmlGeneratorMock).writeFieldName("item");
        verify(toXmlGeneratorMock).writeEndObject();

    }

    @Test
    public void testSerializeValueWithObjectAndRootType() throws IOException {
        when(toXmlGeneratorMock.inRoot()).thenReturn(false);
        when(toXmlGeneratorMock.setNextNameIfMissing(rootNameMock)).thenReturn(false);
        when(toXmlGeneratorMock.setNextName(rootNameMock)).thenReturn(true);
        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootNameMock);
        when(xmlSerializerProvider._asXmlGenerator(jsonGeneratorMock)).thenReturn(toXmlGeneratorMock);
        when(xmlSerializerProvider.findTypedValueSerializer(Mockito.<JavaType> any(), anyBoolean(), isNull())).thenReturn(mock(JsonSerializer.class));

        xmlSerializerProvider.serializeValue(jsonGeneratorMock, new Object(), mock(JavaType.class), mock(JsonSerializer.class));

        verify(toXmlGeneratorMock, never()).setNextNameIfMissing(rootNameMock);
        verify(toXmlGeneratorMock).initGenerator();
        verify(toXmlGeneratorMock, times(1)).getStaxWriter();
        verify(toXmlGeneratorMock).writeStartObject();
        verify(toXmlGeneratorMock).writeFieldName("item");
        verify(toXmlGeneratorMock).writeEndObject();

    }

    @Test
    public void testSerializeValueWithSerializationException() throws IOException {
        when(xmlSerializerProvider.findTypedValueSerializer(Mockito.<JavaType> any(), anyBoolean(), isNull())).thenThrow(RuntimeException.class);

        xmlSerializerProvider.serializeValue(jsonGeneratorMock, new Object(), mock(JavaType.class), mock(JsonSerializer.class));

        verify(jsonGeneratorMock).writeStartObject();
        verify(jsonGeneratorMock, never()).writeFieldName(anyString());
        verify(jsonGeneratorMock, times(1)).writeEndObject();

    }

    @Test
    public void testSerializeXmlNullWithRootNameFromConfig() throws IOException {
        when(xmlSerializerProvider._rootNameFromConfig()).thenReturn(rootNameMock);
        when(xmlSerializerProvider._asXmlGenerator(jsonGeneratorMock)).thenReturn(toXmlGeneratorMock);

        xmlSerializerProvider._serializeXmlNull(jsonGeneratorMock);

        verify(toXmlGeneratorMock).setNextNameIfMissing(rootNameMock);
        verify(toXmlGeneratorMock).initGenerator();
        verify(jsonGeneratorMock).writeStartObject();
        verify(jsonGeneratorMock).writeFieldName(anyString());
        verify(jsonGeneratorMock, never()).writeEndObject();

    }

    @Test
    public void testSerializeXmlNullWithNullRootNameFromConfig() throws IOException {
        xmlSerializerProvider._serializeXmlNull(jsonGeneratorMock);

        verify(toXmlGeneratorMock, never()).setNextNameIfMissing(rootNameMock);
        verify(toXmlGeneratorMock).initGenerator();
        verify(jsonGeneratorMock).writeStartObject();
        verify(jsonGeneratorMock).writeFieldName(anyString());
        verify(jsonGeneratorMock, never()).writeEndObject();

    }

    @Test
    public void testStartRootArray() throws IOException {
        xmlSerializerProvider._startRootArray(toXmlGeneratorMock, rootNameMock);

        verify(toXmlGeneratorMock).writeStartObject();
        verify(toXmlGeneratorMock).writeFieldName("item");

    }

    @Test
    public void testInitWithRootName() throws IOException {
        xmlSerializerProvider._initWithRootName(toXmlGeneratorMock, rootNameMock);

        verify(toXmlGeneratorMock).setNextNameIfMissing(rootNameMock);
        verify(toXmlGeneratorMock, times(1)).initGenerator();
        verify(toXmlGeneratorMock).getStaxWriter();

    }

    @Test
    public void testRootNameFromConfigWithNullPropertyName() {
        assertEquals(null, xmlSerializerProvider._rootNameFromConfig());
    }

    @Test
    public void testRootNameFromConfigWithEmptyNamespace() {
        assertEquals(new QName("mock"), xmlSerializerProvider._rootNameFromConfig());
    }

    @Test
    public void testRootNameFromConfigWithNamespace() {
        xmlSerializerProvider._config = mock(SerializationConfig.class);
        when(xmlSerializerProvider._config.getFullRootName()).thenReturn(mock(PropertyName.class));
        assertEquals(new QName("mock", "mock"), xmlSerializerProvider._rootNameFromConfig());
        verify(xmlSerializerProvider._config.getFullRootName(), times(1)).getNamespace();

    }

    @Test(expected = JsonMappingException.class)
    public void testASXmlGeneratorWithTokenBuffer() throws JsonMappingException {
        xmlSerializerProvider._asXmlGenerator(mock(TokenBuffer.class));
    }

    @Test
    public void testWrapAsIOEWithIOException() throws IOException {
        IOException exception = mock(IOException.class);
        assertEquals(exception, xmlSerializerProvider._wrapAsIOE(jsonGeneratorMock, exception));
    }

    @Test
    public void testWrapAsIOEWithException() throws IOException {
        Exception exception = mock(Exception.class);
        when(exception.getMessage()).thenReturn("message");
        assertEquals(IOException.class, xmlSerializerProvider._wrapAsIOE(jsonGeneratorMock, exception).getClass());
    }
}