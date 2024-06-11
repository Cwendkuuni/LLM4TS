import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.dataformat.xml.ser.XmlSerializerProvider;
import com.fasterxml.jackson.dataformat.xml.util.StaxUtil;
import com.fasterxml.jackson.dataformat.xml.util.TypeUtil;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;

public class XmlSerializerProviderTest {
    
    private XmlSerializerProvider provider;
    private JsonGenerator mockGenerator;
    
    @Before
    public void setup() {
        XmlRootNameLookup rootNames = mock(XmlRootNameLookup.class);
        provider = new XmlSerializerProvider(rootNames);
        mockGenerator = mock(JsonGenerator.class);
    }
    
    @Test
    public void testCreateInstance() {
        SerializationConfig config = mock(SerializationConfig.class);
        SerializerFactory factory = mock(SerializerFactory.class);
        DefaultSerializerProvider instance = provider.createInstance(config, factory);
        
        assertNotNull(instance);
    }
    
    @Test
    public void testSerializeValue_withNullValue() throws IOException {
        XmlSerializerProvider spyProvider = spy(provider);
        doNothing().when(spyProvider).serializeValue(any(JsonGenerator.class), any(Object.class));
        
        spyProvider.serializeValue(mockGenerator, null);
        
        verify(spyProvider, times(1)).serializeValue(any(JsonGenerator.class), any(Object.class));
    }
    
    @Test
    public void testSerializeValue_withNonArrayValue() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(provider._asXmlGenerator(any(JsonGenerator.class))).thenReturn(toXmlGenerator);
        when(toXmlGenerator.inRoot()).thenReturn(true);
        
        JsonSerializer<Object> mockSerializer = mock(JsonSerializer.class);
        when(provider.findTypedValueSerializer(any(Class.class), anyBoolean(), any(SerializerProvider.class))).thenReturn(mockSerializer);
        
        provider.serializeValue(mockGenerator, "test");
        
        verify(provider, times(1))._initWithRootName(any(ToXmlGenerator.class), any(QName.class));
        verify(provider, times(1)).findTypedValueSerializer(any(Class.class), anyBoolean(), any(SerializerProvider.class));
        verify(mockSerializer, times(1)).serialize(eq("test"), eq(mockGenerator), eq(provider));
    }
    
    @Test
    public void testSerializeValue_withArrayValue() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(provider._asXmlGenerator(any(JsonGenerator.class))).thenReturn(toXmlGenerator);
        when(toXmlGenerator.inRoot()).thenReturn(true);
        when(TypeUtil.isIndexedType(any(Class.class))).thenReturn(true);
        
        QName rootName = new QName("test");
        when(provider._rootNameFromConfig()).thenReturn(rootName);
        
        JsonSerializer<Object> mockSerializer = mock(JsonSerializer.class);
        when(provider.findTypedValueSerializer(any(Class.class), anyBoolean(), any(SerializerProvider.class))).thenReturn(mockSerializer);
        
        provider.serializeValue(mockGenerator, new String[] {"test1", "test2"});
        
        verify(toXmlGenerator, times(1)).writeStartObject();
        verify(provider, times(1))._startRootArray(any(ToXmlGenerator.class), eq(rootName));
        verify(mockSerializer, times(2)).serialize(any(String.class), eq(mockGenerator), eq(provider));
        verify(mockGenerator, times(1)).writeEndObject();
    }
    
    @Test
    public void testSerializeValue_withNonArrayRootType() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(provider._asXmlGenerator(any(JsonGenerator.class))).thenReturn(toXmlGenerator);
        when(toXmlGenerator.inRoot()).thenReturn(true);
        
        JavaType rootType = mock(JavaType.class);
        
        JsonSerializer<Object> mockSerializer = mock(JsonSerializer.class);
        when(provider.findTypedValueSerializer(any(JavaType.class), anyBoolean(), any(SerializerProvider.class))).thenReturn(mockSerializer);
        
        provider.serializeValue(mockGenerator, "test", rootType);
        
        verify(provider, times(1))._initWithRootName(any(ToXmlGenerator.class), any(QName.class));
        verify(provider, times(1)).findTypedValueSerializer(eq(rootType), anyBoolean(), any(SerializerProvider.class));
        verify(mockSerializer, times(1)).serialize(eq("test"), eq(mockGenerator), eq(provider));
    }
    
    @Test
    public void testSerializeValue_withArrayRootType() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(provider._asXmlGenerator(any(JsonGenerator.class))).thenReturn(toXmlGenerator);
        when(toXmlGenerator.inRoot()).thenReturn(true);
        when(TypeUtil.isIndexedType(any(JavaType.class))).thenReturn(true);
        
        QName rootName = new QName("test");
        when(provider._rootNameFromConfig()).thenReturn(rootName);
        
        JavaType rootType = mock(JavaType.class);
        
        JsonSerializer<Object> mockSerializer = mock(JsonSerializer.class);
        when(provider.findTypedValueSerializer(any(JavaType.class), anyBoolean(), any(SerializerProvider.class))).thenReturn(mockSerializer);
        
        provider.serializeValue(mockGenerator, new String[] {"test1", "test2"}, rootType);
        
        verify(toXmlGenerator, times(1)).writeStartObject();
        verify(provider, times(1))._startRootArray(any(ToXmlGenerator.class), eq(rootName));
        verify(mockSerializer, times(2)).serialize(any(String.class), eq(mockGenerator), eq(provider));
        verify(mockGenerator, times(1)).writeEndObject();
    }
    
    @Test
    public void testSerializeValue_withArrayRootTypeAndCustomSerializer() throws IOException {
        ToXmlGenerator toXmlGenerator = mock(ToXmlGenerator.class);
        when(provider._asXmlGenerator(any(JsonGenerator.class))).thenReturn(toXmlGenerator);
        when(toXmlGenerator.inRoot()).thenReturn(true);
        when(TypeUtil.isIndexedType(any(JavaType.class))).thenReturn(true);
        
        QName rootName = new QName("test");
        when(provider._rootNameFromConfig()).thenReturn(rootName);
        
        JavaType rootType = mock(JavaType.class);
        
        JsonSerializer<Object> mockSerializer = mock(JsonSerializer.class);
        
        provider.serializeValue(mockGenerator, new String[] {"test1", "test2"}, rootType, mockSerializer);
        
        verify(toXmlGenerator, times(1)).writeStartObject();
        verify(provider, times(1))._startRootArray(any(ToXmlGenerator.class), eq(rootName));
        verify(mockSerializer, times(2)).serialize(any(String.class), eq(mockGenerator), eq(provider));
        verify(mockGenerator, times(1)).writeEndObject();
    }

}