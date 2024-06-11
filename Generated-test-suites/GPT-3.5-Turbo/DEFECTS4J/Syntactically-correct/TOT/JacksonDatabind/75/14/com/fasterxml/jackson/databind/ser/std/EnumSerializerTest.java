package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.EnumValues;

@JacksonStdImpl
public class EnumSerializerTest {

    // Create a mock EnumValue object
    @Mock
    private EnumValues mockEnumValues;
    
    @Mock
    private JsonGenerator mockJsonGenerator;
    
    @Mock
    private SerializerProvider mockSerializerProvider;
    
    @Mock
    private BeanProperty mockBeanProperty;
    
    @Mock
    private JsonNode mockJsonNode;
    
    @Mock
    private JsonFormatVisitorWrapper mockVisitorWrapper;
    
    @Mock
    private JavaType mockJavaType;
    
    private EnumSerializer enumSerializer;
    
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Mockito.when(mockEnumValues.getEnumClass()).thenReturn(TestEnum.class);
        Mockito.when(mockBeanProperty.getType()).thenReturn(mockJavaType);
        Mockito.when(mockJavaType.isEnumType()).thenReturn(true);
        enumSerializer = new EnumSerializer(mockEnumValues, false);
    }
    
    @Test
    public void testSerialize_AsIndex() throws IOException {
        Mockito.when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX))
            .thenReturn(true);
        
        enumSerializer.serialize(TestEnum.TEST1, mockJsonGenerator, mockSerializerProvider);
        
        // Verify that writeNumber() was called with the correct ordinal value
        verify(mockJsonGenerator).writeNumber(TestEnum.TEST1.ordinal());
    }
    
    @Test
    public void testSerialize_AsString_UsingToString() throws IOException {
        Mockito.when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING))
            .thenReturn(true);
        
        enumSerializer.serialize(TestEnum.TEST1, mockJsonGenerator, mockSerializerProvider);
        
        // Verify that writeString() was called with the correct string representation of the enum
        verify(mockJsonGenerator).writeString(TestEnum.TEST1.toString());
    }
    
    @Test
    public void testSerialize_AsString_UsingValues() throws IOException {
        Mockito.when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING))
            .thenReturn(false);
        Mockito.when(mockEnumValues.serializedValueFor(TestEnum.TEST1)).thenReturn("Test1");
        
        enumSerializer.serialize(TestEnum.TEST1, mockJsonGenerator, mockSerializerProvider);
        
        // Verify that writeString() was called with the correct serialized value from EnumValues
        verify(mockJsonGenerator).writeString("Test1");
    }
    
    @Test
    public void testCreateContextual() throws JsonMappingException {
        // Create a new EnumSerializer with different serializeAsIndex value
        EnumSerializer newSerializer = new EnumSerializer(mockEnumValues, true);
        when(mockBeanProperty.getMember()).thenReturn(null);
        when(mockBeanProperty.getType()).thenReturn(mockJavaType);
        when(mockSerializerProvider.constructType(mockJavaType.getType())).thenReturn(mockJavaType);
        when(mockVisitorWrapper.getProvider()).thenReturn(mockSerializerProvider);
        when(mockVisitorWrapper.expectStringFormat(mockJavaType)).thenReturn(null);
        when(mockVisitorWrapper.expectArrayFormat(mockJavaType)).thenReturn(null);
        when(mockVisitorWrapper.expectBooleanFormat(mockJavaType)).thenReturn(null);
        when(mockVisitorWrapper.expectIntegerFormat(mockJavaType)).thenReturn(null);
        when(mockVisitorWrapper.expectNullFormat(mockJavaType)).thenReturn(null);
        when(mockVisitorWrapper.expectNumberFormat(mockJavaType)).thenReturn(null);
        when(mockVisitorWrapper.expectObjectFormat(mockJavaType)).thenReturn(null);

        ContextualSerializer newContextualSerializer = newSerializer.createContextual(mockSerializerProvider, mockBeanProperty);
        assertEquals(newSerializer, newContextualSerializer);
    }
    
    @Test
    public void testGetSchema_WithSerializeAsIndex() {
        Mockito.when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX))
            .thenReturn(true);
        
        JsonNode resultNode = enumSerializer.getSchema(mockSerializerProvider, null);
        
        // Verify that createSchemaNode() was called with the correct type and index set to true
        verify(mockSerializerProvider).createSchemaNode("integer", true);
        assertTrue(resultNode.isObject());
        assertTrue(resultNode.get("type").textValue().equals("integer"));
    }
    
    @Test
    public void testGetSchema_AsString_UsingValues() {
        Mockito.when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX))
            .thenReturn(false);
        Mockito.when(mockJavaType.isEnumType()).thenReturn(true);
        Mockito.when(mockEnumValues.values()).thenReturn(Arrays.asList(mockJsonNode));
        
        JsonNode resultNode = enumSerializer.getSchema(mockSerializerProvider, mockJavaType.getType());
        
        // Verify that createSchemaNode() was called with the correct type and index set to true
        verify(mockSerializerProvider).createSchemaNode("string", true);
        assertTrue(resultNode.isObject());
        assertTrue(resultNode.get("type").textValue().equals("string"));
        assertTrue(resultNode.get("enum").isArray());
        assertEquals(1, resultNode.get("enum").size());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        Mockito.when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING))
            .thenReturn(false);
        Mockito.when(mockVisitorWrapper.expectStringFormat(mockJavaType)).thenReturn(mockJsonStringFormatVisitor);
        when(mockVisitorWrapper.getProvider()).thenReturn(mockSerializerProvider);
        
        enumSerializer.acceptJsonFormatVisitor(mockVisitorWrapper, mockJavaType);
        
        verify(mockVisitorWrapper).expectStringFormat(mockJavaType);
    }
    
    private enum TestEnum {
        TEST1, TEST2, TEST3;
        
        @Override
        public String toString() {
            return "Test Enum";
        }
    }
}