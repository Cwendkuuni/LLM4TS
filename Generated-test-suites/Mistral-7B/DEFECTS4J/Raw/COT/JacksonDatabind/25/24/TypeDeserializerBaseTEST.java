package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

public class TypeDeserializerBaseTest {

    private static final String CLASS_NAME = TypeDeserializerBase.class.getName();
    private static final String TYPE_PROPERTY_NAME = "typePropertyName";
    private static final String BASE_TYPE_NAME = "baseTypeName";

    private TypeDeserializerBase typeDeserializerBase;
    private DeserializationContext context;
    private JsonFactory jsonFactory;
    private TypeIdResolver typeIdResolver;
    private JavaType baseType;

    @Before
    public void setup() throws Exception {
        jsonFactory = new ObjectMapper().getFactory();
        typeIdResolver = new DefaultTypeIdResolver();
        baseType = javaTypeOf(String.class);
        context = new TestContext(jsonFactory);
        typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, TYPE_PROPERTY_NAME, false, null);
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty property = new BeanProperty("testProperty", String.class);
        TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
        assertNotNull(forProperty);
        assertEquals(String.class, forProperty.getType());
    }

    @Test
    public void testGotTypeInclusion() {
        JsonTypeInfo.As gotTypeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNull(gotTypeInclusion);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBaseTypeName() {
        typeDeserializerBase.baseTypeName();
    }

    @Test
    public void testGetPropertyName() {
        String gotPropertyName = typeDeserializerBase.getPropertyName();
        assertEquals(TYPE_PROPERTY_NAME, gotPropertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver gotTypeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertEquals(typeIdResolver, gotTypeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> gotDefaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(gotDefaultImpl);
    }

    @Test
    public void testToString() throws IOException {
        String gotString = typeDeserializerBase.toString();
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;" +
                " base-type:class java.lang.String;" +
                " id-resolver:class com.fasterxml.jackson.databind.type.TypeIdResolverImpl]";
        assertEquals(expected, gotString);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        String typeId = "testTypeId";
        JsonDeserializer<Object> gotDeserializer = typeDeserializerBase._findDeserializer(context, typeId);
        assertNull(gotDeserializer);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullContext() throws IOException {
        String typeId = "testTypeId";
        typeDeserializerBase._findDeserializer(null, typeId);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullTypeId() throws IOException {
        typeDeserializerBase._findDeserializer(context, null);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> gotDeserializer = typeDeserializerBase._findDefaultImplDeserializer(context);
        assertNull(gotDeserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        String typeId = "testTypeId";
        TypeIdResolver newTypeIdResolver = new DefaultTypeIdResolver() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String id) throws IOException {
                fail("Method should not be called");
                return null;
            }
        };
        TypeDeserializerBase newTypeDeserializerBase = new TypeDeserializerBase(baseType, newTypeIdResolver, TYPE_PROPERTY_NAME, false, null) {
            @Override
            protected JsonDeserializer<Object> _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return new StdConverter<Object, String>() {
                    @Override
                    public String convert(Object src) {
                        return "UNKNOWN";
                    }
                }.getDeserializer();
            }
        };
        Object result = newTypeDeserializerBase._deserializeWithNativeTypeId(jsonFactory.createParser("{}"), context, typeId);
        assertEquals("UNKNOWN", result);
    }
}