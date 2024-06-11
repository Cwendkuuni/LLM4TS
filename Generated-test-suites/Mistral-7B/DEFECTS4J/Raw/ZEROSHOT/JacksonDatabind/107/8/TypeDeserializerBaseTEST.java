package com.example.jackson.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class TypeDeserializerBaseTest extends TestBase {

    @Test
    public void testTypeDeserializerBase() throws IOException {
        // Create a mock TypeIdResolver
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        // Create a mock JavaType for base type
        JavaType baseType = mock(JavaType.class);
        when(baseType.getRawClass()).thenReturn(String.class);

        // Create a mock JsonDeserializer for default impl
        JsonDeserializer<Object> defaultImplDeserializer = mock(JsonDeserializer.class);

        // Create an instance of TypeDeserializerBase under test
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(
                baseType, idResolver, "typePropertyName", true, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
                    throws IOException {
                return defaultImplDeserializer;
            }
        };

        // Test that getters return expected values
        assertEquals(baseType, typeDeserializerBase.baseType());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals("typePropertyName", typeDeserializerBase.getPropertyName());
        assertTrue(typeDeserializerBase.isTypeIdVisible());
        assertEquals(TypeDeserializerBase.class, typeDeserializerBase.getClass());

        // Test that methods throw expected exceptions
        try {
            typeDeserializerBase.forProperty(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            typeDeserializerBase.forProperty(new BeanProperty(null, ""));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            typeDeserializerBase.forProperty(new BeanProperty("", null));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            typeDeserializerBase.forProperty(new BeanProperty("", ""));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            typeDeserializerBase.forProperty(new BeanProperty("", "", new TypeReference<Integer>() {}));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test that findDeserializer method returns expected JsonDeserializer
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(ctxt.findContextualValueDeserializer(eq(String[].class), anyObject())).thenReturn(new JsonDeserializer<Object>() {});
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(ctxt, "String[].class");
        assertNotNull(deserializer);

        // Test that findDefaultImplDeserializer method returns expected JsonDeserializer
        when(ctxt.findContextualValueDeserializer(eq(String.class), anyObject())).thenReturn(defaultImplDeserializer);
        JsonDeserializer<Object> defaultImplDeserializer2 = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(defaultImplDeserializer, defaultImplDeserializer2);

        // Test that deserializeWithNativeTypeId method returns expected Object
        JsonParser jsonParser = createParser("[]");
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, ctxt, "String[].class");
        assertNotNull(result);
    }
}