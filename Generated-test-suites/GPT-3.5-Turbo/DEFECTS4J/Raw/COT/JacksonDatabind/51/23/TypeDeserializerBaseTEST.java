
package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

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
        baseType = new ObjectMapper().getTypeFactory().constructType(Object.class);
        idResolver = new TypeIdResolver() {
            @Override
            public void init(JavaType baseType) {
            }

            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public String idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }

            @Override
            public String idFromBaseType() {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext context, String id) throws IOException {
                return null;
            }

            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        };
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = new ObjectMapper().getTypeFactory().constructType(Object.class);
        property = null;
        typeDeserializer = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializerBase forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("java.lang.Object", typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("type", typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(idResolver, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertEquals(Object.class, typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testToString() {
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:Object; id-resolver: null]", typeDeserializer.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonDeserializer<Object> deser = new NullifyingDeserializer();
        Object result = deser.deserialize(null, null);
        assertNull(result);
        result = typeDeserializer._deserializeWithNativeTypeId(null, null, null);
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleUnknownTypeId() throws IOException {
        typeDeserializer._handleUnknownTypeId(null, "unknown", idResolver, baseType);
    }
}
