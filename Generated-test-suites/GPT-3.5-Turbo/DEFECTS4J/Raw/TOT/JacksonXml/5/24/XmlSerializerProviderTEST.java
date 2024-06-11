package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerFactory;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.dataformat.xml.util.XmlRootNameLookup;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class XmlSerializerProviderTest {

    @Mock
    private JsonGenerator mockGenerator;
    
    @Mock
    private DefaultSerializerProvider mockProvider;
    
    @Mock
    private SerializationConfig mockConfig;
    
    @Mock
    private SerializerFactory mockFactory;
    
    @Mock
    private ToXmlGenerator mockToXmlGenerator;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testSerializeValue_NullValue_ShouldSerializeXmlNull() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(mockGenerator, null);
        verify(provider).serializeValue(mockGenerator, null);
    }
    
    @Test
    public void testSerializeValue_AsArray_ShouldStartRootArray() throws Exception {
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockGenerator.inRoot()).thenReturn(true);
        when(mockToXmlGenerator.setNextNameIfMissing(any())).thenReturn(false);
        when(mockToXmlGenerator.inRoot()).thenReturn(true);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(mockGenerator, "testValue");
        
        verify(mockToXmlGenerator).writeStartObject();
        verify(mockGenerator).writeFieldName("item");
        verify(mockGenerator).writeEndObject();
    }
    
    @Test
    public void testSerializeValue_AsXmlGenerator_ShouldInitGeneratorWithRootName() throws Exception {
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockToXmlGenerator.setNextNameIfMissing(any())).thenReturn(false);
        when(mockToXmlGenerator.inRoot()).thenReturn(false);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(mockGenerator, "testValue");
        
        verify(mockToXmlGenerator).initGenerator();
    }
    
    @Test
    public void testSerializeValue_AsXmlGenerator_ShouldForceDefaultNamespaceWhenRootNameHasNamespace() throws Exception {
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockToXmlGenerator.setNextNameIfMissing(any())).thenReturn(false);
        when(mockToXmlGenerator.inRoot()).thenReturn(false);
        when(mockToXmlGenerator.getStaxWriter().getDefaultNamespace()).thenReturn(null);
        when(mockToXmlGenerator.getStaxWriter().getNamespaceContext()).thenReturn(null);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(mockGenerator, "testValue");
        
        verify(mockToXmlGenerator).getStaxWriter().setDefaultNamespace(null);
    }
    
    @Test
    public void testSerializeValue_AsXmlGenerator_ShouldSetNextNameWithRootNameWhenRootNameNotSet() throws Exception {
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockToXmlGenerator.setNextNameIfMissing(any())).thenReturn(true);
        when(mockToXmlGenerator.inRoot()).thenReturn(false);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(mockGenerator, "testValue");
        
        verify(mockToXmlGenerator).setNextName(any());
    }
    
    @Test
    public void testSerializeValue_AsXmlGenerator_ShouldNotSetNextNameWithRootNameWhenRootNameSet() throws Exception {
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockToXmlGenerator.setNextNameIfMissing(any())).thenReturn(false);
        when(mockToXmlGenerator.inRoot()).thenReturn(false);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(mockGenerator, "testValue");
        
        verify(mockToXmlGenerator, never()).setNextName(any());
    }
    
    @Test
    public void testSerializeValue_NullSerializer_ShouldFindTypedValueSerializer() throws Exception {
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockToXmlGenerator.setNextNameIfMissing(any())).thenReturn(false);
        when(mockToXmlGenerator.inRoot()).thenReturn(false);
        
        JsonSerializer<Object> mockSerializer = mock(JsonSerializer.class);
        XmlSerializerProvider provider = spy(new XmlSerializerProvider(new XmlRootNameLookup()));
        doReturn(mockSerializer).when(provider).findTypedValueSerializer(any(), anyBoolean(), any());
        
        provider.serializeValue(mockGenerator, "testValue");
        
        verify(mockSerializer).serialize(eq("testValue"), eq(mockGenerator), any());
    }
    
    @Test
    public void testSerializeValue_AsArray_ShouldWriteEndObject() throws Exception {
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockToXmlGenerator.setNextNameIfMissing(any())).thenReturn(false);
        when(mockToXmlGenerator.inRoot()).thenReturn(false);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(mockGenerator, "testValue");
        provider.serializeValue(mockGenerator, new String[] {"test1", "test2"});
        
        verify(mockGenerator).writeEndObject();
    }
    
    @Test
    public void testSerializeValue_NullValueWithType_ShouldSerializeXmlNull() {
        XmlSerializerProvider provider = spy(new XmlSerializerProvider(new XmlRootNameLookup()));
        provider.serializeValue(mockGenerator, null, JavaType.wrapperType(String.class), null);
        verify(provider).serializeValue(mockGenerator, null);
    }
    
    @Test
    public void testSerializeValue_NullSerializer_ShouldFindTypedValueSerializerWithType() throws Exception {
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockToXmlGenerator.setNextNameIfMissing(any())).thenReturn(false);
        when(mockToXmlGenerator.inRoot()).thenReturn(false);
        
        JsonSerializer<Object> mockSerializer = mock(JsonSerializer.class);
        XmlSerializerProvider provider = spy(new XmlSerializerProvider(new XmlRootNameLookup()));
        doReturn(mockSerializer).when(provider).findTypedValueSerializer(any(), anyBoolean(), any());
        
        provider.serializeValue(mockGenerator, "testValue", JavaType.wrapperType(String.class), null);
        
        verify(mockSerializer).serialize(eq("testValue"), eq(mockGenerator), any());
    }

    @Test
    public void testSerializeValue_AsXmlGeneratorWithType_ShouldInitGeneratorWithRootName() throws Exception {
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockToXmlGenerator.setNextNameIfMissing(any())).thenReturn(false);
        when(mockToXmlGenerator.inRoot()).thenReturn(false);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider.serializeValue(mockGenerator, "testValue", JavaType.wrapperType(String.class), mock(JsonSerializer.class));
        
        verify(mockToXmlGenerator).initGenerator();
    }
    
    @Test
    public void testSerializeValue_NullValue_ShouldCallSuper() throws Exception {
        ToXmlGenerator mockToXmlGenerator = mock(ToXmlGenerator.class);
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockGenerator.inRoot()).thenReturn(true);
        when(mockProvider.findTypedValueSerializer(any(), anyBoolean(), any())).thenReturn(mock(JsonSerializer.class));
        
        XmlSerializerProvider provider = spy(new XmlSerializerProvider(mockProvider, mockConfig, mockFactory));
        doReturn(mockToXmlGenerator).when(provider)._asXmlGenerator(mockGenerator);
        
        provider.serializeValue(mockGenerator, null);
        
        verify(mockProvider).serializeValue(mockGenerator, null);
    }
    
    @Test
    public void testSerializeValue_AsXmlGenerator_ShouldDelegateSuperSerialize() throws Exception {
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockToXmlGenerator.setNextNameIfMissing(any())).thenReturn(false);
        when(mockToXmlGenerator.inRoot()).thenReturn(false);
        when(mockProvider.findTypedValueSerializer(any(), anyBoolean(), any())).thenReturn(mock(JsonSerializer.class));
        
        XmlSerializerProvider provider = spy(new XmlSerializerProvider(mockProvider, mockConfig, mockFactory));
        doReturn(mockToXmlGenerator).when(provider)._asXmlGenerator(mockGenerator);
        
        provider.serializeValue(mockGenerator, "testValue");
        
        verify(mockProvider).serializeValue(mockGenerator, "testValue");
    }
    
    @Test
    public void testSerializeValue_NullValueWithType_ShouldCallSuper() throws Exception {
        ToXmlGenerator mockToXmlGenerator = mock(ToXmlGenerator.class);
        when(mockGenerator instanceof ToXmlGenerator).thenReturn(true);
        when(mockGenerator.inRoot()).thenReturn(true);
        when(mockProvider.findTypedValueSerializer(any(), anyBoolean(), any())).thenReturn(mock(JsonSerializer.class));
        
        XmlSerializerProvider provider = spy(new XmlSerializerProvider(mockProvider, mockConfig, mockFactory));
        doReturn(mockToXmlGenerator).when(provider)._asXmlGenerator(mockGenerator);
        
        provider.serializeValue(mockGenerator, null, JavaType.wrapperType(String.class), null);
        
        verify(mockProvider).serializeValue(mockGenerator, null);
    }
    
    @Test
    public void testWriteException_ShouldWrapExceptionAsIOE() throws Exception {
        Exception mockException = mock(Exception.class);
        
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        provider._wrapAsIOE(mockGenerator, mockException);
        
        verify(mockGenerator).wrapedException(mockException);
    }
    
    @Test
    public void testRootNameFromConfig_WithNullName_ShouldReturnNull() {
        XmlSerializerProvider provider = new XmlSerializerProvider(new XmlRootNameLookup());
        QName rootName = provider._rootNameFromConfig();
        
        verify(mockConfig).getFullRootName();
        assert(rootName == null);
    }
}