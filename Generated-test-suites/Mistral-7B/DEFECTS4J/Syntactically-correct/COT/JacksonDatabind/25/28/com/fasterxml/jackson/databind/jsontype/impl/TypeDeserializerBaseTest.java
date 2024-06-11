package com.example.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;

@RunWith(BlockJUnit4ClassRunner.class)
public class TypeDeserializerBaseTest {

    

    @Test
    public void testForProperty() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        BeanProperty property = new BeanProperty.Std("propertyName", String.class);
        TypeDeserializerBase forProperty = typeDeserializerBase.forProperty(property);
        assertNotNull(forProperty);
    }

    @Test(expected = NullPointerException.class)
    public void testForPropertyNullProperty() {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNull(typeDeserializerBase.forProperty(null));
    }

    @Test
    public void testGetTypeInclusion() {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNotNull(typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNotNull(typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNotNull(typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNotNull(typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializerNullContext() {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        String typeId = "testTypeId";
        assertNull(typeDeserializerBase._findDeserializer(null, typeId));
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerNullTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNull(typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), null));
    }

    @Test
    public void testFindDeserializerEmptyIdResolver() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolverBase idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typeId = "testTypeId";
        assertNull(typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), typeId));
    }

    @Test
    public void testFindDeserializerWithDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolverBase idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return mapper.getTypeFactory().constructType(String.class);
            }
        };
        String typeId = "java.lang.String";
        JsonDeserializer<Object> deserializer = mapper.findValueDeserializer(mapper.getFactory().constructType(String.class));
        typeDeserializerBase._deserializers.put(typeId, deserializer);
        JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDeserializer(mapper.getDeserializationContext(), typeId);
        assertNotNull(foundDeserializer);
        assertNotNull(foundDeserializer.deserialize(mapper.getFactory().createParser("test"), mapper.getDeserializationContext()));
    }

    @Test
    public void testFindDefaultImplDeserializerNullContext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(null));
    }

    @Test
    public void testFindDefaultImplDeserializerNullDefaultImpl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNull(typeDeserializerBase._findDefaultImplDeserializer(mapper.getDeserializationContext()));
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImpl() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        Class<?> defaultImpl = Object.class;
        typeDeserializerBase._defaultImpl = mapper.getTypeFactory().constructType(defaultImpl);
        JsonDeserializer<Object> defaultImplDeserializer = mapper.findValueDeserializer(mapper.getFactory().constructType(defaultImpl));
        typeDeserializerBase._defaultImplDeserializer = defaultImplDeserializer;
        JsonDeserializer<Object> foundDeserializer = typeDeserializerBase._findDefaultImplDeserializer(mapper.getDeserializationContext());
        assertNotNull(foundDeserializer);
        assertNotNull(foundDeserializer.deserialize(mapper.getFactory().createParser("test"), mapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeWithNativeTypeIdNullContext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(null, mapper.getDeserializationContext()));
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeIdNullTypeId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(mapper.getDeserializationContext().createParser("test"), mapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeWithNativeTypeIdEmptyIdResolver() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolverBase idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        assertNull(typeDeserializerBase._deserializeWithNativeTypeId(mapper.getDeserializationContext().createParser("test"), mapper.getDeserializationContext()));
    }

    @Test
    public void testDeserializeWithNativeTypeIdWithDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolverBase idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                if ("testTypeId".equals(typeId)) {
                    return mapper.getTypeFactory().constructType(String.class);
                }
                return null;
            }
        };
        String expected = "test";
        mapper.setTypeIdResolver(idResolver);
        String json = "{\"typeId\":\"testTypeId\",\"value\":\"" + expected + "\"}";
        String result = mapper.readValue(new ByteArrayInputStream(json.getBytes()), new TypeReference<String>() {});
        assertNotNull(result);
        assertEquals(expected, result);
    }

    @Test
    public void testHandleUnknownTypeIdNullContext() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolverBase idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        String typeId = "testTypeId";
        assertNull(typeDeserializerBase._handleUnknownTypeId(mapper.getDeserializationContext(), typeId, idResolver, baseType));
    }

    @Test
    public void testHandleUnknownTypeIdWithDeserializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializerBase typeDeserializerBase = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        TypeIdResolverBase idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
                return null;
            }
        };
        JavaType baseType = mapper.getTypeFactory().constructType(TypeDeserializerBase.class);
        assertNotNull(typeDeserializerBase._handleUnknownTypeId(mapper.getDeserializationContext(), "testTypeId", idResolver, baseType));
    }

    
}