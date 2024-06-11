import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private JavaType baseType;
    private TypeIdResolver idResolver;
    private String typePropertyName;
    private boolean typeIdVisible;
    private Class<?> defaultImpl;

    @Before
    public void setUp() {
        baseType = mock(JavaType.class);
        idResolver = mock(TypeIdResolver.class);
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = null;
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("type", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertNotNull(typeDeserializerBase.getTypeIdResolver());
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void test_deserializeWithNativeTypeId_NullTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt);
        assertNotNull(result);
        assertTrue(result instanceof NullifyingDeserializer);
    }

    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void test_deserializeWithNativeTypeId_NullDefaultImplDeserializer() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, "test");
    }

    @Test
    public void test_deserializeWithNativeTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Map<String, JsonDeserializer<Object>> deserializers = new HashMap<>();
        typeDeserializerBase._deserializers = deserializers;
        JsonDeserializer<Object> deser = mock(JsonDeserializer.class);
        deserializers.put("test", deser);
        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, "test");
        assertEquals(deser, result);
    }

    @Test
    public void test_handleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String typeId = "unknown";
        String extraDesc = "known type ids = null";
        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
        } catch (com.fasterxml.jackson.databind.JsonMappingException e) {
            assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", e.getBaseType().toString());
            assertEquals(typeId, e.getTypeId());
            assertEquals(extraDesc, e.getMessage());
        }
    }

}