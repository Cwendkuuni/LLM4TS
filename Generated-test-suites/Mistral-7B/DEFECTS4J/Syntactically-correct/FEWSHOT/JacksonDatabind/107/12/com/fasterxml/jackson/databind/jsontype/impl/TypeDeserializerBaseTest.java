package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TypeDeserializerBaseTestBase;

public class TypeDeserializerBaseTest extends TypeDeserializerBaseTestBase {

    @Test
    public void testUtil_stripLeadingHyphens() {
        TypeDeserializerBase base = new TypeDeserializerBase(
            new JavaType(String.class), mock(TypeIdResolver.class),
            "typePropertyName", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };

        assertSame("f", base.stripLeadingHyphens("-f"));
        assertSame("foo", base.stripLeadingHyphens("--foo"));
        assertSame("foo", base.stripLeadingHyphens("foo-"));
        assertNull(base.stripLeadingHyphens(null));
    }

    @Test
    public void testUtil_stripLeadingAndTrailingQuotes() {
        TypeDeserializerBase base = new TypeDeserializerBase(
            new JavaType(String.class), mock(TypeIdResolver.class),
            "typePropertyName", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };

        assertEquals("foo", base.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo bar", base.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("foo\" and bar\"", base.stripLeadingAndTrailingQuotes("\"foo\" and bar\""));
        assertEquals("", base.stripLeadingAndTrailingQuotes("\"\""));
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase base = new TypeDeserializerBase(
            new JavaType(String.class), mock(TypeIdResolver.class),
            "typePropertyName", false, null);

        assertEquals("java.lang.String", base.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase base = new TypeDeserializerBase(
            new JavaType(String.class), mock(TypeIdResolver.class),
            "typePropertyName", false, null);

        assertEquals("typePropertyName", base.getPropertyName());
    }

    @Test
    public void testTypeIdResolver() {
        TypeDeserializerBase base = new TypeDeserializerBase(
            new JavaType(String.class), mock(TypeIdResolver.class),
            "typePropertyName", false, null);

        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(any(DeserializationContext.class), anyString())).thenReturn(null);
        base._idResolver = idResolver;

        assertNull(base.getTypeIdResolver().typeFromId(mock(DeserializationContext.class), "expectedId"));
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithNullTypeId() throws IOException {
        TypeDeserializerBase base = new TypeDeserializerBase(
            new JavaType(String.class), mock(TypeIdResolver.class),
            "typePropertyName", false, null) {
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String extraDesc)
                    throws IOException {
                return null;
            }
        };

        base.handleUnknownTypeId(mock(DeserializationContext.class), null);
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeId() throws IOException {
        TypeDeserializerBase base = new TypeDeserializerBase(
            new JavaType(String.class), mock(TypeIdResolver.class),
            "typePropertyName", false, null) {
            @Override
            protected JavaType _handleMissingTypeId(DeserializationContext ctxt, String extraDesc)
                    throws IOException {
                return null;
            }
        };

        base.handleMissingTypeId(mock(DeserializationContext.class), "extraDesc");
    }

    @Test
    public void testFindDeserializer() {
        TypeDeserializerBase base = new TypeDeserializerBase(
            new JavaType(String.class), mock(TypeIdResolver.class),
            "typePropertyName", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return mock(JsonDeserializer.class);
            }
        };

        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(base._deserializers.get(anyString())).thenReturn(deser);

        assertEquals(deser, base._findDeserializer(ctxt, "typeId"));
    }

    @Test
    public void testFindDefaultImplDeserializer() {
        TypeDeserializerBase base = new TypeDeserializerBase(
            new JavaType(String.class), mock(TypeIdResolver.class),
            "typePropertyName", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }
        };

        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        when(base._defaultImplDeserializer).thenReturn(deser);

        assertSame(deser, base._findDefaultImplDeserializer(ctxt));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        TypeDeserializerBase base = new TypeDeserializerBase(
            new JavaType(String.class), mock(TypeIdResolver.class),
            "typePropertyName", false, null) {
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String extraDesc)
                    throws IOException {
                return null;
            }
        };

        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.getTypeFactory().constructSpecializedType(any(JavaType.class), any(Class.class)))
                .thenReturn(mock(JavaType.class));

        JavaType result = base._handleUnknownTypeId(ctxt, "extraDesc");
        assertNull(result);
    }
}