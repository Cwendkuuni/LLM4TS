package com.fasterxml.jackson.databind.jsontype.impl;

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
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.test.BaseTest;

/**
 * Test cases for {@link TypeDeserializerBase} class.
 */
public class TypeDeserializerBaseTest extends BaseTest {

    private static final TypeDeserializerBase TESTEE;

    static {
        // Create a test instance for testing
        JavaType baseType = TypeFactory.defaultInstance().constructType(String.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        TESTEE = new TypeDeserializerBase(baseType, idResolver, null, false, null) {
            @Override
            protected TypeDeserializer forProperty(BeanProperty prop) {
                // Override to return a dummy implementation
                return new TypeDeserializer() {
                    @Override
                    public JsonTypeInfo.As getTypeInclusion() {
                        return JsonTypeInfo.As.EXISTING_PROPERTY;
                    }
                };
            }
        };
    }

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        String typeId = "foo";

        // Test finding a deserializer by type id
        JsonDeserializer<Object> deser = TESTEE._findDeserializer(ctxt, typeId);
        assertNull(deser);

        when(ctxt.findContextualValueDeserializer(eq(String.class), any())).thenReturn(mock(JsonDeserializer.class));
        deser = TESTEE._findDeserializer(ctxt, typeId);
        assertNotNull(deser);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());

        // Test finding the default implementation deserializer
        JsonDeserializer<Object> deser = TESTEE._findDefaultImplDeserializer(ctxt);
        assertNull(deser);

        when(ctxt.findContextualValueDeserializer(eq(String.class), any())).thenReturn(NullifyingDeserializer.instance);
        deser = TESTEE._findDefaultImplDeserializer(ctxt);
        assertSame(NullifyingDeserializer.instance, deser);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        String typeId = "foo";

        when(TESTEE.getTypeIdResolver()).thenReturn(mock(TypeIdResolver.class));

        // Test handling an unknown type id
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(TESTEE.getTypeIdResolver()).thenReturn(idResolver);
        Object result = TESTEE._handleUnknownTypeId(ctxt, typeId, idResolver, TESTEE._baseType);
        fail("Expected an exception to be thrown");
    }

    @Test
    public void testHandleUnknownTypeIdWithCustomTypeIdResolver() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        String typeId = "foo";

        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        when(TESTEE.getTypeIdResolver()).thenReturn(idResolver);

        Object result = TESTEE._handleUnknownTypeId(ctxt, typeId, idResolver, TESTEE._baseType);
        fail("Expected an exception to be thrown");
    }

    @Test
    public void testHandleUnknownTypeIdWithNullTypeIdResolver() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        String typeId = null;

        Object result = TESTEE._handleUnknownTypeId(ctxt, typeId, null, TESTEE._baseType);
        fail("Expected an exception to be thrown");
    }

    @Test
    public void testHandleUnknownTypeIdWithDefaultType() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        String typeId = null;

        Object result = TESTEE._handleUnknownTypeId(ctxt, typeId, TESTEE._idResolver, TESTEE._baseType);
        assertSame(null, result);
    }

    @Test
    public void testFindDeserializerWithTypeIdResolver() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());
        String typeId = "foo";

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(TESTEE._idResolver.typeFromId(ctxt, typeId)).thenReturn(TypeFactory.defaultInstance().constructType(Integer.class));
        when(TESTEE._idResolver.typeFromId(ctxt, typeId)).thenReturn(typeDeserializer);

        JsonDeserializer<Object> deser = TESTEE._findDeserializer(ctxt, typeId);
        assertNotNull(deser);
        assertSame(typeDeserializer, deser);
    }

    @Test
    public void testFindDeserializerWithNullTypeId() throws IOException {
        DeserializationContext ctxt = new DeserializationContext(new ObjectMapper(), new StandardDeserializationContext());

        JsonDeserializer<Object> deser = TESTEE._findDeserializer(ctxt, null);
        assertNull(deser);
    }
}