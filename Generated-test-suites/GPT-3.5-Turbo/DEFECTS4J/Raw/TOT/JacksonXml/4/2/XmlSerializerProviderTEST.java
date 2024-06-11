import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerCache;
import com.fasterxml.jackson.databind.ser.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class XmlSerializerProviderTest {
    
    private XmlSerializerProvider provider;
    private JsonSerializer<Object> valueSerializer;
    private JsonSerializer<Object> nullSerializer;
    private ToXmlGenerator generator;
    private JsonGenerator jsonGenerator;
    private Object value;
    private SerializationConfig config;
    private SerializerFactoryConfig factoryConfig;
    private SerializerFactory factory;

    @Before
    public void setup() throws IOException {
        generator = mock(ToXmlGenerator.class);
        jsonGenerator = mock(JsonGenerator.class);
        valueSerializer = mock(JsonSerializer.class);
        nullSerializer = mock(JsonSerializer.class);
        value = new Object();
        config = mock(SerializationConfig.class);
        factoryConfig = mock(SerializerFactoryConfig.class);
        factory = mock(SerializerFactory.class);
        
        when(generator.setNextNameIfMissing(new QName("null"))).thenReturn(true);
        when(generator.inRoot()).thenReturn(true);
        
        provider = new XmlSerializerProvider(null);
        provider.setGenerator(generator);
        provider.setNullValueSerializer(nullSerializer);
        provider.setConfig(config);
        provider.setFactoryConfig(factoryConfig);
        provider.setSerializerFactory(factory);
        provider.setCache(new SerializerCache());
    }
    
    @Test
    public void testSerializeValue_Null() throws IOException {
        provider.serializeValue(jsonGenerator, null);
        
        verify(nullSerializer, times(1)).serialize(null, jsonGenerator, provider);
    }
    
    @Test
    public void testSerializeValue_Class() throws IOException {
        Class<?> clazz = value.getClass();
        when(config.getFullRootName()).thenReturn(null);
        when(generator.isXml12()).thenReturn(false);
        when(factoryConfig.getSerializerCache()).thenReturn(new SerializerCache());
        when(config.withSerializerCache(factoryConfig.getSerializerCache())).thenReturn(config);
        when(provider.findTypedValueSerializer(clazz, true, null)).thenReturn(valueSerializer);
        provider.serializeValue(jsonGenerator, value);
        
        verify(generator, times(1)).initWithRootName(generator, new QName("null"));
        verify(generator, times(1)).initGenerator();
        verify(generator, times(1)).isXml12();
        verify(factoryConfig, times(1)).getSerializerCache();
        verify(config, times(1)).withSerializerCache(factoryConfig.getSerializerCache());
        verify(provider, times(1)).findTypedValueSerializer(clazz, true, null);
        verify(valueSerializer, times(1)).serialize(value, jsonGenerator, provider);
    }
    
    @Test
    public void testSerializeValue_JavaType() throws IOException {
        JavaType type = mock(JavaType.class);
        when(config.getFullRootName()).thenReturn(null);
        when(generator.isXml12()).thenReturn(false);
        when(factoryConfig.getSerializerCache()).thenReturn(new SerializerCache());
        when(config.withSerializerCache(factoryConfig.getSerializerCache())).thenReturn(config);
        when(provider.findTypedValueSerializer(type, true, null)).thenReturn(valueSerializer);
        provider.serializeValue(jsonGenerator, value, type);
        
        verify(generator, times(1)).initWithRootName(generator, new QName("null"));
        verify(generator, times(1)).initGenerator();
        verify(generator, times(1)).isXml12();
        verify(factoryConfig, times(1)).getSerializerCache();
        verify(config, times(1)).withSerializerCache(factoryConfig.getSerializerCache());
        verify(provider, times(1)).findTypedValueSerializer(type, true, null);
        verify(valueSerializer, times(1)).serialize(value, jsonGenerator, provider);
    }
    
    @Test
    public void testSerializeValue_JavaType_Serializer() throws IOException {
        JavaType type = mock(JavaType.class);
        when(config.getFullRootName()).thenReturn(null);
        when(generator.isXml12()).thenReturn(false);
        provider.serializeValue(jsonGenerator, value, type, valueSerializer);
        
        verify(generator, times(1)).initWithRootName(generator, new QName("null"));
        verify(generator, times(1)).initGenerator();
        verify(generator, times(1)).isXml12();
        verify(valueSerializer, times(1)).serialize(value, jsonGenerator, provider);
    }
    
    @Test
    public void testSerializeValue_AsArray() throws IOException {
        when(generator.isXml12()).thenReturn(false);
        when(factoryConfig.getSerializerCache()).thenReturn(new SerializerCache());
        when(config.withSerializerCache(factoryConfig.getSerializerCache())).thenReturn(config);
        when(provider.findTypedValueSerializer(value.getClass(), true, null)).thenReturn(valueSerializer);
        
        provider.serializeValue(jsonGenerator, value, value.getClass(), valueSerializer);
        
        verify(generator, times(1)).initWithRootName(generator, new QName("null"));
        verify(generator, times(1)).initGenerator();
        verify(generator, times(1)).isXml12();
        verify(factoryConfig, times(1)).getSerializerCache();
        verify(config, times(1)).withSerializerCache(factoryConfig.getSerializerCache());
        verify(provider, times(1)).findTypedValueSerializer(value.getClass(), true, null);
        verify(valueSerializer, times(1)).serialize(value, jsonGenerator, provider);
        verify(jsonGenerator, times(1)).writeEndObject();
    }
    
    @Test
    public void testSerializeValue_AsArray_NullSerializer() throws IOException {
        when(jsonGenerator.writeObjectFieldStart("item")).thenReturn(jsonGenerator);
        when(generator.isXml12()).thenReturn(false);
        when(factoryConfig.getSerializerCache()).thenReturn(new SerializerCache());
        when(config.withSerializerCache(factoryConfig.getSerializerCache())).thenReturn(config);
        when(provider.findTypedValueSerializer(value.getClass(), true, null)).thenReturn(valueSerializer);
        provider.serializeValue(jsonGenerator, value, value.getClass(), null);
        
        verify(generator, times(1)).initWithRootName(generator, new QName("null"));
        verify(generator, times(1)).initGenerator();
        verify(generator, times(1)).isXml12();
        verify(factoryConfig, times(1)).getSerializerCache();
        verify(config, times(1)).withSerializerCache(factoryConfig.getSerializerCache());
        verify(provider, times(1)).findTypedValueSerializer(value.getClass(), true, null);
        verify(valueSerializer, times(1)).serialize(value, jsonGenerator, provider);
        verify(jsonGenerator, times(1)).writeEndObject();
    }
    
    @Test
    public void testStartRootArray() throws IOException {
        QName rootName = new QName("root");
        provider.startRootArray(generator, rootName);
        
        verify(generator, times(1)).writeStartObject();
        verify(generator, times(1)).writeFieldName("item");
    }
    
    @Test
    public void testInitWithRootName_InRoot() throws IOException {
        QName rootName = new QName("root");
        when(generator.isXml12()).thenReturn(false);
        when(rootName.getNamespaceURI()).thenReturn("http://example.com");
        provider.initWithRootName(generator, rootName);
        
        verify(generator, times(1)).setNextNameIfMissing(rootName);
        verify(generator, times(1)).initWithRootName(generator, rootName);
        verify(generator, times(1)).initGenerator();
        verify(rootName, times(1)).getNamespaceURI();
        verify(generator.getStaxWriter(), times(1)).setDefaultNamespace("http://example.com");
    }
    
    @Test
    public void testInitWithRootName_NotInRoot() throws IOException {
        QName rootName = new QName("root");
        when(generator.isXml12()).thenReturn(false);
        when(rootName.getNamespaceURI()).thenReturn("");
        provider.initWithRootName(generator, rootName);
        
        verify(generator, times(1)).setNextNameIfMissing(rootName);
        verify(generator, times(1)).inRoot();
        verify(rootName, times(1)).getNamespaceURI();
        verify(provider, times(0)).findTypedValueSerializer(value.getClass(), true, null);
    }
    
    @Test
    public void testRootNameFromConfig() {
        PropertyName name = new PropertyName("root");
        when(config.getFullRootName()).thenReturn(name);
        QName rootName = provider.rootNameFromConfig();
        
        assertEquals(rootName, new QName("root"));
    }
}