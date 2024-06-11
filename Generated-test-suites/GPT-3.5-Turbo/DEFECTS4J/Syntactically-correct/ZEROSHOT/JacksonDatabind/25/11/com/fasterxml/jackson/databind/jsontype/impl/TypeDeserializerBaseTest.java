import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TypeDeserializerBaseTest {

    @Mock
    private JavaType baseType;
    @Mock
    private TypeIdResolver idResolver;
    @Mock
    private BeanProperty property;
    @Mock
    private JavaType defaultImpl;

    private TypeDeserializerBase typeDeserializerBase;

    public TypeDeserializerBaseTest() {
        MockitoAnnotations.initMocks(this);
        typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "typePropertyName", true, defaultImpl) {
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
        when(baseType.getRawClass()).thenReturn(Object.class);
        assertEquals("java.lang.Object", typeDeserializerBase.baseTypeName());
    }

    @Test
    public void testGetPropertyType() {
        assertNull(typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertNull(typeDeserializerBase.getDefaultImpl());
        when(defaultImpl.getRawClass()).thenReturn(Object.class);
        assertEquals(Object.class, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testToString() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase;" +
                " base-type:null; id-resolver: null]";
        assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        assertEquals(NullifyingDeserializer.instance, typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt));
    }

    @Test(expected = com.fasterxml.jackson.databind.JsonMappingException.class)
    public void testDeserializeWithNativeTypeId_ThrowsMappingException() throws IOException {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        typeDeserializerBase._deserializeWithNativeTypeId(jp, ctxt, "typeId");
    }

    @Test
    public void testFindDeserializer() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        Map<String,JsonDeserializer<Object>> deserializers = new ConcurrentHashMap<>();
        deserializers.put("typeId", expectedDeserializer);
        deserializers.put("anotherTypeId", null);
        when(ctxt.findContextualValueDeserializer(baseType, property)).thenReturn(expectedDeserializer);
        typeDeserializerBase._deserializers = deserializers;
        JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(ctxt, "typeId");
        assertEquals(expectedDeserializer, actualDeserializer);
    }

    @Test
    public void testFindDeserializer_DefaultImplDeserializer() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        JsonDeserializer<Object> expectedDefaultImplDeserializer = mock(JsonDeserializer.class);
        when(defaultImpl.getRawClass()).thenReturn(Object.class);
        when(ctxt.findContextualValueDeserializer(defaultImpl, property)).thenReturn(expectedDefaultImplDeserializer);
        JsonDeserializer<Object> actualDefaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertEquals(expectedDefaultImplDeserializer, actualDefaultImplDeserializer);
    }

    @Test
    public void testHandleUnknownTypeId() throws IOException {
        DeserializationContext ctxt = mock(DeserializationContext.class);
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        JavaType baseType = mock(JavaType.class);
        com.fasterxml.jackson.databind.JsonMappingException expectedException =
                new com.fasterxml.jackson.databind.JsonMappingException(baseType, "typeId", null);
        when(idResolver.typeFromId(ctxt, "typeId")).thenReturn(null);
        when(ctxt.mappingException(baseType, "typeId", null)).thenReturn(expectedException);

        try {
            typeDeserializerBase._handleUnknownTypeId(ctxt, "typeId", idResolver, baseType);
            fail("Exception is expected");
        } catch (com.fasterxml.jackson.databind.JsonMappingException ex) {
            assertEquals(expectedException, ex);
        }
    }
}