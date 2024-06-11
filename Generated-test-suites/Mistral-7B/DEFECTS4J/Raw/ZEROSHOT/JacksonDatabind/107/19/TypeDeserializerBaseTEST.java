package com.example.jackson.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    private TypeDeserializerBase typeDeserializer;
    private TypeIdResolver typeIdResolver;
    private DeserializationContext context;

    @Before
    public void setup() throws Exception {
        typeIdResolver = mock(TypeIdResolver.class);
        context = mock(DeserializationContext.class);
        when(context.findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class)))
                .thenReturn(mock(JsonDeserializer.class));
        when(context.getTypeFactory()).thenReturn(getTypeFactory());
        when(context.getConfig()).thenReturn(objectMapper.getConfig());
        typeDeserializer = new TypeDeserializerBase(getTypeFactory().constructType(String.class), typeIdResolver,
                "typePropertyName", true, getTypeFactory().constructType(Integer.class));
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty beanProperty = mock(BeanProperty.class);
        TypeDeserializerBase newTypeDeserializer = typeDeserializer.forProperty(beanProperty);
        assertEquals(Integer.class, newTypeDeserializer.baseType());
        assertEquals("typePropertyName", newTypeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeInclusion() {
        assertEquals(JsonTypeInfo.As.WRAP_IN_ARRAY, typeDeserializer.getTypeInclusion());
    }

    @Test
    public void testGetBaseTypeName() throws Exception {
        assertEquals("java.lang.String", typeDeserializer.baseTypeName());
    }

    @Test
    public void testDefaultImpl() throws Exception {
        assertEquals(Integer.class, typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        String typeId = "typeId1";
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        when(typeDeserializer._deserializers.get(typeId)).thenReturn(jsonDeserializer);

        JsonDeserializer<Object> deserializer = typeDeserializer._findDeserializer(context, typeId);
        assertEquals(jsonDeserializer, deserializer);
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JsonDeserializer<Object> jsonDeserializer = mock(JsonDeserializer.class);
        when(context.findContextualValueDeserializer(eq(Integer.class), any(BeanProperty.class)))
                .thenReturn(jsonDeserializer);

        JsonDeserializer<Object> deserializer = typeDeserializer._findDefaultImplDeserializer(context);
        assertEquals(jsonDeserializer, deserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws Exception {
        when(typeIdResolver.typeFromId(context, "unknownTypeId"))
                .thenReturn((JavaType) null);

        JavaType javaType = typeDeserializer._handleUnknownTypeId(context, "unknownTypeId");
        assertNull(javaType);
    }

    @Test(expected = JsonMappingException.class)
    public void testHandleMissingTypeId() throws Exception {
        when(typeIdResolver.typeFromId(context, "typeId1"))
                .thenThrow(new IOException("Missing TypeId"));

        typeDeserializer._handleMissingTypeId(context, "typeId1");
    }
}