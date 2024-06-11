package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    private ObjectMapper objectMapper;

    private BeanProperty beanProperty;

    @Before
    public void setUp() {
        JavaType baseType = new ObjectMapper().constructType(TypeDeserializerBase.class);
        TypeIdResolver idResolver = new ClassNameIdResolver(baseType, null);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = null;
        typeDeserializerBase = new TypeDeserializerBaseMock(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);

        objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, "type");
    }

    @Test
    public void forPropertyTest() {
        TypeDeserializer copy = typeDeserializerBase.forProperty(beanProperty);
        assertNotNull(copy);
    }

    @Test
    public void getTypeInclusionTest() {
        assertEquals(JsonTypeInfo.As.PROPERTY, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void baseTypeNameTest() {
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void getPropertyNameTest() {
        assertEquals("type", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void getTypeIdResolverTest() {
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void getDefaultImplTest() {
        assertNotNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void baseTypeTest() {
        assertNotNull(typeDeserializerBase.baseType());
    }

    @Test
    public void _deserializeWithNativeTypeIdTest() throws Exception {
        String json = "{\"type\":\"java.util.HashMap\",\"value\":{\"key\":\"value\"}}";
        Object object = objectMapper.readValue(json, Object.class);
        assertNotNull(object);
    }

    @Test(expected = JsonMappingException.class)
    public void _deserializeWithNativeTypeIdTest2() throws Exception {
        String json = "{\"type\":\"unknown\",\"value\":{\"key\":\"value\"}}";
        objectMapper.readValue(json, Object.class);
    }
}

class TypeDeserializerBaseMock extends TypeDeserializerBase {

	protected TypeDeserializerBaseMock(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
		super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
	}

	@Override
	public TypeDeserializer forProperty(BeanProperty prop) {
		return new TypeDeserializerBaseMock(_baseType, _idResolver, _typePropertyName, _typeIdVisible, _defaultImpl);
	}
	
	@Override
    public JsonTypeInfo.As getTypeInclusion() {
        return JsonTypeInfo.As.PROPERTY;
    }
	
	@Override
    protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId) throws IOException {
        return ctxt.handleUnknownTypeId(_baseType, typeId, _idResolver, "Unknown type id");
    }
}