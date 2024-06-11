
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private BeanProperty mockBeanProperty;
    private TypeIdResolver mockTypeIdResolver;
    private JavaType mockJavaType;
    private DeserializationContext mockDeserializationContext;
    private JsonDeserializer<Object> mockJsonDeserializer;
    private Map<String, JsonDeserializer<Object>> mockDeserializers;
    private ObjectMapper mockObjectMapper;
    private DeserializationContext mockDeserializationContext2;
    private JsonParser mockJsonParser;
    
    @Before
    public void setUp() {
        mockBeanProperty = mock(BeanProperty.class);
        mockTypeIdResolver = mock(TypeIdResolver .class);
        mockJavaType = mock(JavaType.class);
        mockDeserializationContext = mock(DeserializationContext.class);
        mockJsonDeserializer = mock(JsonDeserializer.class);
        mockDeserializers = mock(Map.class);
        mockObjectMapper = mock(ObjectMapper.class);
        mockDeserializationContext2 = mock(DeserializationContext.class);
        mockJsonParser = mock(JsonParser.class);
        
        typeDeserializerBase = new TypeDeserializerBase(mockJavaType, mockTypeIdResolver, "", true, mockJavaType) {
            private static final long serialVersionUID = 1L;

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public As getTypeInclusion() {
                return null;
            }
        };
        
        typeDeserializerBase._property = mockBeanProperty;
        typeDeserializerBase._deserializers = mockDeserializers;
        typeDeserializerBase._defaultImplDeserializer = mockJsonDeserializer;
        
        when(mockTypeIdResolver.typeFromId(any(DeserializationContext.class), any(String.class))).thenReturn(mockJavaType);
        when(mockJavaType.getRawClass()).thenReturn(Object.class);
        when(mockDeserializers.get(any(String.class))).thenReturn(null);
        when(mockObjectMapper.getTypeFactory()).thenReturn(mockJavaType);
        when(mockObjectMapper.constructSpecializedType(mockJavaType, Object.class)).thenReturn(mockJavaType);
        when(mockDeserializationContext2.findContextualValueDeserializer(mockJavaType, mockBeanProperty)).thenReturn(mockJsonDeserializer);
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(mockTypeIdResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertEquals(Object.class, typeDeserializerBase.getDefaultImpl());
    }
    
    @Test
    public void testDeserializeWithNativeTypeId_WhenTypeIsNullAndJsonFeatureIsNotEnabled_ReturnsNullifyingDeserializerInstance()
            throws IOException {
        assertEquals(JsonDeserializer.class, typeDeserializerBase._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext)._dummy().getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithNativeTypeId_WhenTypeIsNullAndJsonFeatureIsEnabled_ThrowsJsonMappingException()
            throws IOException {
        when(mockDeserializationContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        
        typeDeserializerBase._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext);
    }

    @Test
    public void testDeserializeWithNativeTypeId_WhenTypeIsNullAndJsonFeatureIsEnabledAndDefaultImplIsNull_ReturnsNull()
            throws IOException {
        when(mockDeserializationContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        when(typeDeserializerBase._defaultImpl.getRawClass()).thenReturn(Void.class);
        
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext));
    }

    @Test
    public void testDeserializeWithNativeTypeId_WhenTypeIsNullAndJsonFeatureIsEnabledAndDefaultImplIsVoid_ReturnsNullifyingDeserializerInstance() throws IOException {
        when(mockDeserializationContext.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);
        when(typeDeserializerBase._defaultImpl.getRawClass()).thenReturn(Void.class);

        assertEquals(JsonDeserializer.class, typeDeserializerBase._deserializeWithNativeTypeId(mockJsonParser, mockDeserializationContext)._dummy().getClass());
    }

}

