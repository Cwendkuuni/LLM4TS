package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * Test cases for {@link TypeDeserializerBase} class.
 *
 * @author John Doe
 */
public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private TypeIdResolver typeIdResolver;
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        typeIdResolver = new TypeIdResolverBase();
        typeDeserializerBase = new TypeDeserializerBaseBase(TypeFactory.defaultInstance, typeIdResolver,
                "typePropertyName", false, TypeFactory.defaultInstance.constructType(String.class));
        objectMapper = new ObjectMapper();
        objectMapper.setTypeIdResolver(typeIdResolver);
    }

    @Test
    public void testForProperty() throws JsonMappingException, JsonProcessingException {
        BeanProperty property = new BeanProperty("propertyName", String.class);
        TypeDeserializerBase newTypeDeserializerBase = typeDeserializerBase.forProperty(property);
        assertEquals(String.class, newTypeDeserializerBase.baseType());
        assertEquals("typePropertyName", newTypeDeserializerBase.getPropertyName());
        assertNotNull(newTypeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetTypeInclusion() {
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() throws JsonMappingException, JsonProcessingException {
        String json = "{\"typePropertyName\":\"abc\"}";
        JsonParser jsonParser = new JsonFactory().createParser(json);
        Object value = typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, objectMapper.getDeserializationContext());
        assertEquals("java.lang.String", typeDeserializerBase.baseTypeName());
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullContext() throws JsonMappingException, JsonProcessingException {
        typeDeserializerBase._findDeserializer(null, "typeId");
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithNullTypeId() throws JsonMappingException, JsonProcessingException {
        DeserializationContext context = objectMapper.getDeserializationContext();
        typeDeserializerBase._findDeserializer(context, null);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerWithUnknownTypeId() throws JsonMappingException, JsonProcessingException {
        DeserializationContext context = objectMapper.getDeserializationContext();
        typeDeserializerBase._findDeserializer(context, "unknownTypeId");
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDefaultImplDeserializerWithNullContext() throws JsonMappingException, JsonProcessingException {
        typeDeserializerBase._findDefaultImplDeserializer(null);
    }

    @Test(expected = JsonMappingException.class)
    public void testFindDefaultImplDeserializerWithDefaultImplIsNull() throws JsonMappingException, JsonProcessingException {
        typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
    }

    @Test
    public void testFindDefaultImplDeserializerWithDefaultImplPresent()
            throws JsonMappingException, JsonProcessingException {
        typeDeserializerBase = new TypeDeserializerBaseBase(TypeFactory.defaultInstance, typeIdResolver,
                "typePropertyName", false, TypeFactory.defaultInstance.constructType(Number.class));
        JsonDeserializer<Object> deserializer = new StdDeserializer<>();
        typeDeserializerBase._defaultImplDeserializer = deserializer;
        assertEquals(deserializer, typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext()));
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithNullContext() throws JsonMappingException, JsonProcessingException {
        typeDeserializerBase._handleUnknownTypeId(null, "extraDesc");
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithEmptyExtraDesc()
            throws JsonMappingException, JsonProcessingException {
        typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "");
    }

    @Test(expected = IOException.class)
    public void testHandleUnknownTypeIdWithExtraDesc()
            throws JsonMappingException, JsonProcessingException {
        typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "extraDesc");
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeIdWithNullContext() throws JsonMappingException, JsonProcessingException {
        typeDeserializerBase._handleMissingTypeId(null, "extraDesc");
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeIdWithEmptyExtraDesc()
            throws JsonMappingException, JsonProcessingException {
        typeDeserializerBase._handleMissingTypeId(objectMapper.getDeserializationContext(), "");
    }

    @Test(expected = IOException.class)
    public void testHandleMissingTypeIdWithExtraDesc()
            throws JsonMappingException, JsonProcessingException {
        typeDeserializerBase._handleMissingTypeId(objectMapper.getDeserializationContext(), "extraDesc");
    }
}

class TypeDeserializerBaseBase extends TypeDeserializerBase {

    public TypeDeserializerBaseBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
        super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }
}

class StdDeserializer<T> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }
}

class TypeIdResolverBase implements TypeIdResolver {

    @Override
    public JavaType typeFromId(DeserializationContext ctxt, String id) throws IOException {
        return null;
    }

    @Override
    public String idFromValue(DeserializationContext ctxt, Object value) throws IOException {
        return null;
    }

    @Override
    public void initValueTypeResolver(TypeResolverBuilder builder) throws JsonMappingException {

    }
}