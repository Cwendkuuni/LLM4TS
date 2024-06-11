import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseType() {
        JavaType baseType = new TypeReference<String>() {}.getType();
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(baseType, mock(TypeIdResolver.class), "", true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals(baseType.getRawClass().getName(), deserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, "type", true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("type", deserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, idResolver, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals(idResolver, deserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = new TypeReference<String>() {}.getType();
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, null, true, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals(defaultImpl.getRawClass(), deserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        assertEquals(expected, deserializerBase.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object typeId = "typeId";

        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        try {
            deserializerBase._deserializeWithNativeTypeId(jp, ctxt, typeId);
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("No (native) type id found when one was expected for polymorphic type handling", e.getMessage());
        }
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "typeId";
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = null;

        TypeDeserializerBase deserializerBase = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        JavaType actual = deserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);

        assertNull(actual);
    }
}