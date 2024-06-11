import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {
    private ObjectMapper objectMapper;
    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        typeDeserializerBase = new TestTypeDeserializerBase();
    }

    @Test
    public void testForProperty() throws Exception {
        BeanProperty beanProperty = mock(BeanProperty.class);
        typeDeserializerBase = new TypeDeserializerBase(objectMapper.getTypeFactory().constructType(String.class),
                mock(TypeIdResolver.class), "", true, null);
        TypeDeserializer actual = typeDeserializerBase.forProperty(beanProperty);
        assertEquals(String.class, actual.getType(null));
    }

    @Test
    public void testGetTypeInclusion() throws Exception {
        JsonTypeInfo.As expectedTypeInclusion = JsonTypeInfo.As.EXTERNAL_START_WITH_ANNOTATION;
        typeDeserializerBase = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return expectedTypeInclusion;
            }
        };
        assertEquals(expectedTypeInclusion, typeDeserializerBase.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() throws Exception {
        typeDeserializerBase = new TypeDeserializerBase(null, null, "", true, null);
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testToString() throws Exception {
        typeDeserializerBase = new TypeDeserializerBase(null, null, "", true, null);
        String expectedToString = "[TypeDeserializerBase; base-type:java.lang.Object; id-resolver:";
        assertEquals(expectedToString, typeDeserializerBase.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testForPropertyNullProperty() throws Exception {
        BeanProperty beanProperty = null;
        typeDeserializerBase = new TypeDeserializerBase(null, null, "", true, null);
        typeDeserializerBase.forProperty(beanProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeIdResolverNull() throws Exception {
        typeDeserializerBase = new TypeDeserializerBase(null, null, "", true, null);
        typeDeserializerBase.getTypeIdResolver();
    }

    @Test(expected = NullPointerException.class)
    public void testFindDeserializerNullContext() throws Exception {
        typeDeserializerBase = new TypeDeserializerBase(null, null, "", true, null);
        typeDeserializerBase._findDeserializer(null, "");
    }

    @Test(expected = IOException.class)
    public void testFindDeserializerIOException() throws Exception {
        DeserializationContext context = mock(DeserializationContext.class);
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);
        when(context.findContextualValueDeserializer(null, null)).thenThrow(new IOException());
        typeDeserializerBase = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
                return super._findDeserializer(ctxt, typeId);
            }
        };
        typeDeserializerBase._findDeserializer(context, "");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullParser() throws Exception {
        JsonParser jsonParser = null;
        DeserializationContext context = mock(DeserializationContext.class);
        typeDeserializerBase = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
                    throws IOException {
                return super._deserializeWithNativeTypeId(jsonParser, ctxt, typeId);
            }
        };
        typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHandleUnknownTypeIdNullContext() throws Exception {
        typeDeserializerBase = new TypeDeserializerBase(null, null, "", true, null) {
            @Override
            JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId,
                    TypeIdResolver idResolver, JavaType baseType) throws IOException {
                return super._handleUnknownTypeId(null, typeId, idResolver, baseType);
            }
        };
        typeDeserializerBase._handleUnknownTypeId(null, "", null, null);
    }

    static class TestTypeDeserializerBase extends TypeDeserializerBase {
    }
}