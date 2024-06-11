
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {
    
    private TypeDeserializerBase typeDeserializer;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private JavaType defaultImpl;
    private BeanProperty property;
    
    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = mock(JavaType.class);
        property = mock(BeanProperty.class);
        
        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
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
    }
    
    @Test
    public void testBaseTypeName() {
        assertEquals(baseType.getRawClass().getName(), typeDeserializer.baseTypeName());
    }
    
    @Test
    public void testGetPropertyName() {
        assertEquals(typePropertyName, typeDeserializer.getPropertyName());
    }
    
    @Test
    public void testGetTypeIdResolver() {
        assertEquals(idResolver, typeDeserializer.getTypeIdResolver());
    }
    
    @Test
    public void testGetDefaultImpl() {
        assertEquals(ClassUtil.rawClass(defaultImpl), typeDeserializer.getDefaultImpl());
    }
    
    @Test
    public void testBaseType() {
        assertEquals(baseType, typeDeserializer.baseType());
    }
    
    @Test
    public void testToString() {
        String expected = "[" + typeDeserializer.getClass().getName();
        expected += "; base-type:" + baseType;
        expected += "; id-resolver: " + idResolver;
        expected += "]";
        
        assertEquals(expected, typeDeserializer.toString());
    }
    
    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> result = typeDeserializer._findDefaultImplDeserializer(ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithNativeTypeId() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        
        Object result = typeDeserializer._deserializeWithNativeTypeId(jp, ctxt);
        
        assertNull(result);
    }
    
    @Test
    public void testHandleUnknownTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "123";
        
        JavaType result = typeDeserializer._handleUnknownTypeId(ctxt, typeId);
        
        assertNull(result);
    }
    
    @Test
    public void testHandleMissingTypeId() throws Exception {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String extraDesc = "Extra description";
        
        JavaType result = typeDeserializer._handleMissingTypeId(ctxt, extraDesc);
        
        assertNull(result);
    }

}
