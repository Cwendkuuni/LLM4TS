
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private DeserializationContext deserializationContext;
    private JavaType javaType;
    private TypeIdResolver typeIdResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;

    @Before
    public void setUp() {
        javaType = null;
        typeIdResolver = null;
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = null;
        typeDeserializerBase = new TypeDeserializerBaseTestImpl(javaType, typeIdResovler, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Test
    public void testForProperty() {
        // Prepare test data
        BeanProperty prop = null;

        // Perform the test
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(prop);

        // Verify the results
        assertNotNull(typeDeserializer);
        assertEquals(TypeDeserializerBaseTestImpl.class, typeDeserializer.getClass());
        assertEquals(javaType, ((TypeDeserializerBaseTestImpl) typeDeserializer).baseType);
        assertEquals(typeIdResolver, ((TypeDeserializerBaseTestImpl) typeDeserializer)._idResolver);
        assertEquals(typePropertyName, ((TypeDeserializerBaseTestImpl) typeDeserializer)._typePropertyName);
        assertEquals(typeIdVisible, ((TypeDeserializerBaseTestImpl) typeDeserializer)._typeIdVisible);
        assertEquals(defaultImpl, ((TypeDeserializerBaseTestImpl) typeDeserializer)._defaultImpl);
        assertNull(((TypeDeserializerBaseTestImpl) typeDeserializer)._property);
        assertNull(((TypeDeserializerBaseTestImpl) typeDeserializer)._defaultImplDeserializer);
        assertEquals(typeDeserializerBase._deserializers, ((TypeDeserializerBaseTestImpl) typeDeserializer)._deserializers);
    }

    @Test
    public void testGetTypeInclusion() {
        // Perform the test
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        // Perform the test
        String baseTypeName = typeDeserializerBase.baseTypeName();

        // Verify the results
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        // Perform the test
        String propertyName = typeDeserializerBase.getPropertyName();

        // Verify the results
        assertEquals("type", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        // Perform the test
        TypeIdResolver idResolver = typeDeserializerBase.getTypeIdResolver();

        // Verify the results
        assertNull(idResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        // Perform the test
        Class<?> defaultImplClass = typeDeserializerBase.getDefaultImpl();

        // Verify the results
        assertNull(defaultImplClass);
    }

    @Test
    public void testToString() {
        // Perform the test
        String toString = typeDeserializerBase.toString();

        // Verify the results
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTestImpl; base-type:null; id-resolver: null]", toString);
    }

    @Test
    public void test_deserializeWithNativeTypeId() throws Exception {
        // Prepare test data
        JsonParser jp = null;

        // Perform the test
        Object deserializeWithNativeTypeId = typeDeserializerBase._deserializeWithNativeTypeId(jp, deserializationContext);
        
        // Verify the results
        assertNull(deserializeWithNativeTypeId);
    }

    @Test(expected=JsonMappingException.class)
    public void test_handleUnknownTypeId() throws Exception {
        // Perform the test
        typeDeserializerBase._handleUnknownTypeId(deserializationContext, "unknown", typeIdResolver, javaType);
    }
    

    private static class TypeDeserializerBaseTestImpl extends TypeDeserializerBase {
        public static final long serialVersionUID = 1L;

        public TypeDeserializerBaseTestImpl(JavaType baseType, TypeIdResolver idRes,
                String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        public TypeDeserializerBaseTestImpl(TypeDeserializerBase src, BeanProperty property) {
            super(src, property);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseTestImpl(this, prop);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.EXTERNAL_PROPERTY;
        }

        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}
