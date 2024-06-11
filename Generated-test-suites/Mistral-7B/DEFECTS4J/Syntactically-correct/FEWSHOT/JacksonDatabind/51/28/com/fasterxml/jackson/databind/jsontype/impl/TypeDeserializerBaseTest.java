package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ObjectReader reader = mock(ObjectReader.class);
        when(ctxt.findContextualValueDeserializer(AnyType.class, null)).thenReturn(mock(JsonDeserializer.class));
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return ctxt.findContextualValueDeserializer(reader.getType(typeId), null);
            }
        };

        String typeId = "TypeId";
        JsonDeserializer<Object> deserializer = base._findDeserializer(ctxt, typeId);
        assertSame(deserializer, ctxt.findContextualValueDeserializer(reader.getType(typeId), null));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ObjectReader reader = mock(ObjectReader.class);
        when(ctxt.findContextualValueDeserializer(reader.getType(JavaType.getType(Void.TYPE)), null)).thenReturn(NullifyingDeserializer.instance);
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return ctxt.findContextualValueDeserializer(reader.getType(JavaType.getType(Void.TYPE)), null);
            }
        };

        assertSame(NullifyingDeserializer.instance, base._findDefaultImplDeserializer(ctxt));
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        ObjectReader reader = mock(ObjectReader.class);
        when(ctxt.findContextualValueDeserializer(null, null)).thenReturn(mock(JsonDeserializer.class));
        TypeDeserializerBase base = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return ctxt.findContextualValueDeserializer(null, null);
            }
        };

        JsonDeserializer<Object> deserializer = base._findDeserializer(ctxt, null);
        assertSame(deserializer, ctxt.findContextualValueDeserializer(null, null));
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(idResolver.typeFromId(ctxt, "typeId")).thenReturn(null);
        when(ctxt.handleUnknownTypeId(TypeDeserializerBase.class.getDeclaredField("_baseType")
                .get(new TypeDeserializerBase(null, null, null, false, null)), "typeId", idResolver, null))
                .thenReturn(JavaType.constructType(String.class));

        TypeDeserializerBase base = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return (JavaType) ctxt.handleUnknownTypeId(TypeDeserializerBase.class.getDeclaredField("_baseType")
                        .get(this), typeId, idResolver, null);
            }
        };

        JavaType type = base._handleUnknownTypeId(ctxt, "typeId", idResolver, null);
        assertSame(type, JavaType.constructType(String.class));
    }

    @Test
    public void testHandleUnknownTypeIdWithNullIdResolver() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.handleUnknownTypeId(TypeDeserializerBase.class.getDeclaredField("_baseType")
                .get(new TypeDeserializerBase(null, null, null, false, null)), "typeId", null, null))
                .thenThrow(IOException.class);

        TypeDeserializerBase base = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return (JavaType) ctxt.handleUnknownTypeId(TypeDeserializerBase.class.getDeclaredField("_baseType")
                        .get(this), typeId, null, null);
            }
        };

        try {
            base._handleUnknownTypeId(ctxt, "typeId", null, null);
            fail();
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void testHandleUnknownTypeIdWithNullBaseType() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(ctxt.handleUnknownTypeId(null, "typeId", idResolver, null)).thenReturn(JavaType.constructType(Object.class));

        TypeDeserializerBase base = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return ctxt.handleUnknownTypeId(null, typeId, idResolver, null);
            }
        };

        JavaType type = base._handleUnknownTypeId(ctxt, "typeId", idResolver, null);
        assertSame(type, JavaType.constructType(Object.class));
    }

    @Test
    public void testHandleUnknownTypeIdWithNullTypeId() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(ctxt.handleUnknownTypeId(TypeDeserializerBase.class.getDeclaredField("_baseType")
                .get(new TypeDeserializerBase(null, null, null, false, null)), null, idResolver, null))
                .thenThrow(IOException.class);

        TypeDeserializerBase base = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return (JavaType) ctxt.handleUnknownTypeId(TypeDeserializerBase.class.getDeclaredField("_baseType")
                        .get(this), null, idResolver, null);
            }
        };

        try {
            base._handleUnknownTypeId(ctxt, null, idResolver, null);
            fail();
        } catch (IOException expected) {
            // expected
        }
    }

    @Test
    public void testHandleUnknownTypeIdWithNullIdResolverAndBaseType() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.handleUnknownTypeId(null, null, null, null)).thenReturn(JavaType.constructType(Object.class));

        TypeDeserializerBase base = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return ctxt.handleUnknownTypeId(null, null, null, null);
            }
        };

        JavaType type = base._handleUnknownTypeId(ctxt, null, null, null);
        assertSame(type, JavaType.constructType(Object.class));
    }

    @Test
    public void testToString() throws IOException {
        TypeDeserializerBase base = new TypeDeserializerBase(
                JavaType.constructType(String.class), null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return this;
            }
        };

        assertEquals("[TypeDeserializerBase; base-type:class java.lang.String; id-resolver: null]", base.toString());
    }
}