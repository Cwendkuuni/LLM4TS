package com.example.junit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class TypeDeserializerBaseTest extends TestBase {

    private TypeDeserializerBase testee;
    private TypeIdResolver typeIdResolver;
    private DeserializationContext context;
    private BeanProperty property;

    @Before
    public void setUp() throws Exception {
        typeIdResolver = mock(TypeIdResolver.class);
        context = mock(DeserializationContext.class);
        property = mock(BeanProperty.class);

        testee = new TypeDeserializerBase(mock(JavaType.class), typeIdResolver, "typePropertyName", true, mock(JavaType.class)) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                this.property = prop;
                return null;
            }
        };
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializer typeDeserializer = testee.forProperty(property);
        assertSame(testee, typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        // Abstract method, cannot test
    }

    @Test
    public void testBaseTypeName() {
        String baseTypeName = testee.baseTypeName();
        assertNotNull(baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        String propertyName = testee.getPropertyName();
        assertEquals("typePropertyName", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = testee.getTypeIdResolver();
        assertSame(typeIdResolver, idResolver);
    }

    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = testee.getDefaultImpl();
        assertNotNull(defaultImpl);
    }

    @Test
    public void testBaseType() {
        JavaType baseType = testee.baseType();
        assertNotNull(baseType);
    }

    @Test
    public void testToString() {
        String toString = testee.toString();
        assertNotNull(toString);
    }

    @Test
    public void testFindDeserializer() throws IOException {
        when(typeIdResolver.typeFromId(context, "someTypeId")).thenReturn(mock(JavaType.class));
        JsonDeserializer<Object> deserializer = testee._findDeserializer(context, "someTypeId");
        assertNotNull(deserializer);

        verify(context).findContextualValueDeserializer(mock(JavaType.class), property);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws IOException {
        JsonDeserializer<Object> defaultImplDeserializer = testee._findDefaultImplDeserializer(context);
        assertNull(defaultImplDeserializer);

        verify(context).findContextualValueDeserializer(_defaultImpl, property);
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);

        when(jsonParser.getTypeId()).thenReturn("someTypeId");

        Object result = testee._deserializeWithNativeTypeId(jsonParser, context);
        assertNull(result);

        verify(context).findContextualValueDeserializer(mock(JavaType.class), property);
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdWithNullTypeId() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);

        Object result = testee._deserializeWithNativeTypeId(jsonParser, context, null);
        fail("Expected IOException, but got " + result);

        verify(context).handleMissingTypeId(_baseType, "type ids are not statically known (for POJO property '_property')");
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        when(typeIdResolver.getDescForKnownTypeIds()).thenReturn("knownTypeIds");

        JavaType result = testee._handleUnknownTypeId(context, "unknownTypeId");
        assertNull(result);

        verify(context).handleMissingTypeId(_baseType, "knownTypeIds (for POJO property '_property')");
    }

    @Test
    public void testHandleMissingTypeId() throws IOException {
        JavaType result = testee._handleMissingTypeId(context, "extraDesc");
        assertNull(result);

        verify(context).handleMissingTypeId(_baseType, "extraDesc");
    }
}