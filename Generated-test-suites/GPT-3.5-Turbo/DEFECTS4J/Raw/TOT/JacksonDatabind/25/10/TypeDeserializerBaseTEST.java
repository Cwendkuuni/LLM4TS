import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;
    private JavaType baseTypeMock;
    private TypeIdResolver typeIdResolverMock;
    private JsonParser jsonParserMock;
    private DeserializationContext deserializationContextMock;

    @Before
    public void setup() {
        baseTypeMock = mock(JavaType.class);
        typeIdResolverMock = mock(TypeIdResolver.class);
        jsonParserMock = mock(JsonParser.class);
        deserializationContextMock = mock(DeserializationContext.class);

        typeDeserializerBase = new TypeDeserializerBase(baseTypeMock, typeIdResolverMock, "type", true, Object.class) {
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }

            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
        };
    }

    @Test
    public void testProperty_getPropertyName_ReturnsTypePropertyName() {
        assertEquals("type", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void testGetProperty_getTypeIdResolver_ReturnsTypeIdResolver() {
        assertEquals(typeIdResolverMock, typeDeserializerBase.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl_getDefaultImpl_ReturnsDefaultImplRawClass() {
        assertEquals(Object.class, typeDeserializerBase.getDefaultImpl());
    }

    @Test
    public void testFindDeserializer_TypeIdExists_ReturnsDeserializer() throws Exception {
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        when(typeIdResolverMock.typeFromId(deserializationContextMock, "id")).thenReturn(baseTypeMock);
        when(deserializationContextMock.findContextualValueDeserializer(baseTypeMock, null)).thenReturn(expectedDeserializer);

        JsonDeserializer<Object> deser = typeDeserializerBase._findDeserializer(deserializationContextMock, "id");
        assertEquals(expectedDeserializer, deser);
    }

    @Test
    public void testFindDeserializer_TypeIdNotExists_ReturnsDefaultImplDeserializer() throws Exception {
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        typeDeserializerBase._defaultImplDeserializer = expectedDeserializer;

        JsonDeserializer<Object> deser = typeDeserializerBase._findDeserializer(deserializationContextMock, "id");
        assertEquals(expectedDeserializer, deser);
    }

    @Test(expected = IOException.class)
    public void testFindDeserializer_TypeIdNotExistsAndDefaultImplNull_ThrowsIOException() throws Exception {
        typeDeserializerBase._defaultImpl = null;
        typeDeserializerBase._findDeserializer(deserializationContextMock, "id");
    }

    @Test
    public void testFindDefaultImplDeserializer_DefaultImplNull_ReturnsNullifyingDeserializer() throws Exception {
        typeDeserializerBase._defaultImpl = null;

        JsonDeserializer<Object> deser = typeDeserializerBase._findDefaultImplDeserializer(deserializationContextMock);
        assertEquals(NullifyingDeserializer.instance, deser);
    }

    @Test
    public void testFindDefaultImplDeserializer_DefaultImplVoid_ReturnsNullifyingDeserializer() throws Exception {
        typeDeserializerBase._defaultImpl = mock(JavaType.class);
        when(typeDeserializerBase._defaultImpl.getRawClass()).thenReturn(Void.class);

        JsonDeserializer<Object> deser = typeDeserializerBase._findDefaultImplDeserializer(deserializationContextMock);
        assertEquals(NullifyingDeserializer.instance, deser);
    }

    @Test
    public void testFindDefaultImplDeserializer_DefaultImplBogusClass_ReturnsNullifyingDeserializer() throws Exception {
        typeDeserializerBase._defaultImpl = mock(JavaType.class);
        when(typeDeserializerBase._defaultImpl.getRawClass()).thenReturn(Object.class);
        when(ClassUtil.isBogusClass(Object.class)).thenReturn(true);

        JsonDeserializer<Object> deser = typeDeserializerBase._findDefaultImplDeserializer(deserializationContextMock);
        assertEquals(NullifyingDeserializer.instance, deser);
    }

    @Test
    public void testDeserializeWithNativeTypeId_TypeIdNull_ReturnsDefaultImplDeserializer() throws Exception {
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        typeDeserializerBase._defaultImplDeserializer = expectedDeserializer;

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock, null);
        assertEquals(expectedDeserializer, result);
        verify(jsonParserMock, times(0)).getTypeId();
    }

    @Test
    public void testDeserializeWithNativeTypeId_TypeIdNotNull_ReturnsDeserializerWithTypeId() throws Exception {
        JsonDeserializer<Object> expectedDeserializer = mock(JsonDeserializer.class);
        when(jsonParserMock.getTypeId()).thenReturn("id");
        when(typeDeserializerBase._findDeserializer(deserializationContextMock, "id")).thenReturn(expectedDeserializer);

        Object result = typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock);
        assertEquals(expectedDeserializer, result);
        verify(jsonParserMock, times(1)).getTypeId();
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNativeTypeId_TypeIdNotNullAndDeserializeFailed_ThrowsIOException() throws Exception {
        when(jsonParserMock.getTypeId()).thenReturn("id");
        when(typeDeserializerBase._findDeserializer(deserializationContextMock, "id")).thenThrow(new IOException());

        typeDeserializerBase._deserializeWithNativeTypeId(jsonParserMock, deserializationContextMock);
        verify(jsonParserMock, times(1)).getTypeId();
    }

    @Test
    public void testHandleUnknownTypeId_IdResolverIsTypeIdResolverBase_ThrowsUnknownTypeException() throws Exception {
        TypeIdResolverBase idResolverBase = mock(TypeIdResolverBase.class);
        when(idResolverBase.getDescForKnownTypeIds()).thenReturn("knownTypeIds");

        IOException exception = new IOException();
        when(deserializationContextMock.unknownTypeException(baseTypeMock, "id", "known type ids = knownTypeIds")).thenReturn(exception);

        typeDeserializerBase._idResolver = idResolverBase;

        try {
            typeDeserializerBase._handleUnknownTypeId(deserializationContextMock, "id", idResolverBase, baseTypeMock);
            fail("IOException expected to be thrown");
        } catch (IOException e) {
            assertEquals(exception, e);
        }
    }

    @Test
    public void testHandleUnknownTypeId_IdResolverIsNotTypeIdResolverBase_DefaultMessage() throws Exception {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);

        IOException exception = new IOException();
        when(deserializationContextMock.unknownTypeException(baseTypeMock, "id", null)).thenReturn(exception);

        typeDeserializerBase._idResolver = idResolver;

        try {
            typeDeserializerBase._handleUnknownTypeId(deserializationContextMock, "id", idResolver, baseTypeMock);
            fail("IOException expected to be thrown");
        } catch (IOException e) {
            assertEquals(exception, e);
        }
    }
}