package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link TypeDeserializerBase}.
 */
public class TypeDeserializerBaseTest {

    private static final String TYPE_ID_ONE = "typeIdOne";
    private static final String TYPE_ID_TWO = "typeIdTwo";
    private static final String TYPE_ID_THREE = "typeIdThree";
    private static final String JSON_STR_ONE = "{\"typeId\":\"" + TYPE_ID_ONE + "\", \"value\":42}";
    private static final String JSON_STR_TWO = "{\"typeId\":\"" + TYPE_ID_TWO + "\", \"value\":43}";
    private static final String JSON_STR_THREE = "{\"typeId\":\"" + TYPE_ID_THREE + "\", \"value\":44}";

    private JavaType _baseType;
    private TypeIdResolver _idResolver;
    private TypeDeserializerBase _typeDeserializerBase;
    private BeanProperty _beanProperty;
    private ObjectMapper _objectMapper;

    @Before
    public void setup() throws Exception {
        JavaType javaTypeOne = TypeFactory.defaultInstance().constructType(int.class);
        JavaType javaTypeTwo = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType javaTypeThree = TypeFactory.defaultInstance().constructType(String.class);
        _baseType = javaTypeOne;
        _idResolver = new TypeIdResolverBase() {
            @Override
            public JavaType typeFromId(DeserializationContext ctxt, String typeId)
                    throws IOException {
                switch (typeId) {
                    case TYPE_ID_ONE:
                        return javaTypeOne;
                    case TYPE_ID_TWO:
                        return javaTypeTwo;
                    case TYPE_ID_THREE:
                        return javaTypeThree;
                    default:
                        return null;
                }
            }
        };
        _typeDeserializerBase = new TypeDeserializerBaseBase(_baseType, _idResolver, "typeProperty", true, null);
        _beanProperty = new BeanProperty.Std("typeProperty", int.class);
        _objectMapper = new ObjectMapper();
        _objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testForProperty() throws Exception {
        TypeDeserializerBase<Integer> typeDeserializerBase = _typeDeserializerBase.forProperty(_beanProperty);
        assertEquals(TypeDeserializerBase.class, typeDeserializerBase.getClass());
        assertEquals(_baseType, typeDeserializerBase.getType(null));
        assertEquals(Integer.class, typeDeserializerBase.getType(null).getRawClass());
        assertEquals(_idResolver, typeDeserializerBase.getTypeIdResolver());
        assertEquals("typeProperty", typeDeserializerBase.getPropertyName());
        assertEquals(true, typeDeserializerBase.isTypeIdVisible());
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        assertEquals(JsonTypeInfo.As.PROPERTY, _typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        assertEquals("int", _typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() throws Exception {
        assertEquals("typeProperty", _typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() throws Exception {
        assertEquals(_idResolver, _typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() throws Exception {
        assertNull(_typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() throws Exception {
        String expected = "[TypeDeserializerBase; base-type:int; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase@15db9742]";
        assertEquals(expected, _typeDeserializerBase.toString());
    }

    @Test
    public void testFindDeserializer() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(JSON_STR_ONE);
        jsonParser.nextToken();
        Object valueOne = _typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, _objectMapper.getDeserializationContext());
        assertEquals(42, valueOne);

        JsonParser jsonParserTwo = objectMapper.getFactory().createParser(JSON_STR_TWO);
        jsonParserTwo.nextToken();
        Object valueTwo = _typeDeserializerBase._deserializeWithNativeTypeId(jsonParserTwo, _objectMapper.getDeserializationContext());
        assertEquals(43, valueTwo);

        JsonParser jsonParserThree = objectMapper.getFactory().createParser(JSON_STR_THREE);
        jsonParserThree.nextToken();
        Object valueThree = _typeDeserializerBase._deserializeWithNativeTypeId(jsonParserThree, _objectMapper.getDeserializationContext());
        assertEquals("value", valueThree);

        JsonParser jsonParserUnknown = objectMapper.getFactory().createParser(JSON_STR_ONE);
        jsonParserUnknown.nextToken();
        try {
            _typeDeserializerBase._deserializeWithNativeTypeId(jsonParserUnknown, _objectMapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            assertEquals("No (native) type id found when one was expected for polymorphic type handling", e.getMessage());
        }
    }

    @Test
    public void testFindDefaultImplDeserializer() throws Exception {
        JsonParser jsonParser = _objectMapper.getFactory().createParser(JSON_STR_ONE);
        jsonParser.nextToken();
        JsonDeserializer<Object> defaultImplDeserializer = _typeDeserializerBase._findDefaultImplDeserializer(_objectMapper.getDeserializationContext());
        assertNull(defaultImplDeserializer);

        JsonParser jsonParserNull = _objectMapper.getFactory().createParser(JSON_STR_ONE);
        jsonParserNull.nextToken();
        JsonDeserializer<Object> nullifyingDeserializer = NullifyingDeserializer.instance;
        assertSame(nullifyingDeserializer, _typeDeserializerBase._findDefaultImplDeserializer(_objectMapper.getDeserializationContext()));
    }
}

class TypeDeserializerBaseBase extends TypeDeserializerBase {
    public TypeDeserializerBaseBase(JavaType baseType, TypeIdResolver idResolver,
            String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
        super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
    }

    @Override
    protected TypeDeserializerBase forProperty(BeanProperty prop) {
        return new TypeDeserializerBaseBase(super._baseType, super._idResolver, prop, super._typeIdVisible, super._defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return super.forProperty(prop);
            }
        };
    }
}