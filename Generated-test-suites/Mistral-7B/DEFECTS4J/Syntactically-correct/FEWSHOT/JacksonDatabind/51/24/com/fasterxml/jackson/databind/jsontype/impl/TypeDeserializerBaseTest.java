package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private static final TypeIdResolver TEST_ID_RESOLVER = mock(TypeIdResolver.class);
    private static final JsonDeserializer<Object> TEST_DESERIALIZER = mock(JsonDeserializer.class);

    @Test
    public void testFindDeserializer() throws IOException {
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), anyString())).thenReturn(null);
        when(TEST_ID_RESOLVER.isEnabled(anyInt())).thenReturn(true);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, TEST_ID_RESOLVER, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return TEST_DESERIALIZER;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return null;
            }
        };

        assertEquals(TEST_DESERIALIZER, typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), "testTypeId"));

        verify(TEST_ID_RESOLVER, times(1)).typeFromId(any(DeserializationContext.class), eq("testTypeId"));
        verify(TEST_ID_RESOLVER, times(1)).isEnabled(anyInt());
    }

    @Test
    public void testFindDeserializerWithNullIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return TEST_DESERIALIZER;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return null;
            }
        };

        assertNull(typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), "testTypeId"));
    }

    @Test
    public void testFindDeserializerWithNoDefaultImplementation() throws IOException {
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), anyString())).thenReturn(null);
        when(TEST_ID_RESOLVER.isEnabled(anyInt())).thenReturn(true);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, TEST_ID_RESOLVER, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return null;
            }
        };

        assertNull(typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), "testTypeId"));
    }

    @Test
    public void testFindDeserializerWithEmptyTypeId() throws IOException {
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), anyString())).thenReturn(null);
        when(TEST_ID_RESOLVER.isEnabled(anyInt())).thenReturn(true);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, TEST_ID_RESOLVER, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return TEST_DESERIALIZER;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return null;
            }
        };

        assertNull(typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), ""));
    }

    @Test
    public void testFindDeserializerWithTypeIdResolvedToNullType() throws IOException {
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), anyString())).thenReturn(null);
        when(TEST_ID_RESOLVER.isEnabled(anyInt())).thenReturn(true);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, TEST_ID_RESOLVER, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return TEST_DESERIALIZER;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return null;
            }
        };

        assertNull(typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), "testTypeId"));
    }

    @Test
    public void testFindDeserializerWithTypeIdResolvedToCustomDeserializer() throws IOException {
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), anyString())).thenReturn(createType());
        when(TEST_ID_RESOLVER.isEnabled(anyInt())).thenReturn(true);
        when(TEST_ID_RESOLVER.findValueDeserializer(any(DeserializationContext.class), eq(createType()))).thenReturn(TEST_DESERIALIZER);

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, TEST_ID_RESOLVER, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return TEST_DESERIALIZER;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return createType();
            }
        };

        assertSame(TEST_DESERIALIZER, typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), "testTypeId"));
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, TEST_ID_RESOLVER, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return TEST_DESERIALIZER;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return null;
            }
        };

        assertSame(TEST_DESERIALIZER, typeDeserializerBase._findDefaultImplDeserializer(mock(DeserializationContext.class)));
    }

    @Test
    public void testHandleUnknownTypeIdWithCustomTypeIdResolver() throws IOException {
        when(TEST_ID_RESOLVER.typeFromId(any(DeserializationContext.class), anyString())).thenReturn(createType());

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, TEST_ID_RESOLVER, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return null;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return createType();
            }
        };

        JavaType javaType = typeDeserializerBase._handleUnknownTypeId(mock(DeserializationContext.class), "testTypeId", TEST_ID_RESOLVER, "");
        assertSame(createType(), javaType);
    }

    @Test
    public void testHandleUnknownTypeIdWithNullTypeIdResolver() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return TEST_DESERIALIZER;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return null;
            }
        };

        assertNull(typeDeserializerBase._handleUnknownTypeId(mock(DeserializationContext.class), "testTypeId", null, ""));
    }

    @Test(expected = IllegalStateException.class)
    public void testHandleUnknownTypeIdWithNullTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, TEST_ID_RESOLVER, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return TEST_DESERIALIZER;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return null;
            }
        };

        typeDeserializerBase._handleUnknownTypeId(mock(DeserializationContext.class), null, TEST_ID_RESOLVER, "");
    }

    @Test
    public void testHandleUnknownTypeIdWithEmptyTypeId() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, TEST_ID_RESOLVER, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return TEST_DESERIALIZER;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return null;
            }
        };

        JavaType javaType = typeDeserializerBase._handleUnknownTypeId(mock(DeserializationContext.class), "", TEST_ID_RESOLVER, "");
        assertNull(javaType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleUnknownTypeIdWithInvalidBaseType() throws IOException {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, TEST_ID_RESOLVER, "", false, null) {
            @Override
            protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
                return TEST_DESERIALIZER;
            }

            @Override
            protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, String extraDesc) throws IOException {
                return null;
            }
        };

        JavaType javaType = typeDeserializerBase._handleUnknownTypeId(mock(DeserializationContext.class), "testTypeId", TEST_ID_RESOLVER, "");
        assertNull(javaType);

        verify(TEST_ID_RESOLVER, never()).typeFromId(any(DeserializationContext.class), anyString());
    }

    private JavaType createType() {
        return mock(JavaType.class);
    }
}