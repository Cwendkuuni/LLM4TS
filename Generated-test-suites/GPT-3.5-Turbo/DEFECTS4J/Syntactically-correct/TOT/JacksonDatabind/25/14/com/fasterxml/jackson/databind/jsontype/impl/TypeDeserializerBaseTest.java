
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @BeforeEach
    public void setup() {
        typeDeserializerBase = new TypeDeserializerBaseTestClass();
    }

    @Test
    public void forProperty_shouldCreateCopyWithCorrectProperty() {
        BeanProperty property = mock(BeanProperty.class);
        TypeDeserializerBase newTypeDeserializer = typeDeserializerBase.forProperty(property);

        Assertions.assertEquals(property, newTypeDeserializer._property);
    }

    @Test
    public void getTypeInclusion_shouldReturnTypeInfoAsExpected() {
        TypeDeserializerBase newTypeDeserializer = new TypeDeserializerBaseTestClass();
        Assertions.assertEquals(JsonTypeInfo.As.PROPERTY, newTypeDeserializer.getTypeInclusion());
    }

    @Test
    public void getPropertyName_shouldReturnTypePropertyNameAsExpected() {
        Assertions.assertEquals("_typePropertyName", typeDeserializerBase.getPropertyName());
    }

    @Test
    public void getTypeIdResolver_shouldReturnTypeResolverAsExpected() {
        TypeIdResolver idResolver = mock(TypeIdResolver.class);
        TypeDeserializerBaseTestClass newTypeDeserializer = new TypeDeserializerBaseTestClass(idResolver);

        Assertions.assertEquals(idResolver, newTypeDeserializer.getTypeIdResolver());
    }

    @Test
    public void getDefaultImpl_shouldReturnDefaultImplementationAsExpected() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        Assertions.assertEquals(TypeDeserializerBaseTestClass.DEFAULT_IMPL, defaultImpl);
    }

    @Test
    public void _toString_shouldReturnExpectedStringRepresentation() {
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBaseTestClass; base-type:null; id-resolver: null]";
        Assertions.assertEquals(expected, typeDeserializerBase.toString());
    }

    @Test
    public void _findDeserializer_withExistingDeserializer_shouldReturnExpectedDeserializer() throws IOException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        String typeId = "mockTypeId";
        JsonDeserializer<Object> expected = new NullifyingDeserializer();
        typeDeserializerBase._deserializers.put(typeId, expected);

        JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(deserializationContext, typeId);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void _findDeserializer_withNonexistentDeserializer_shouldReturnExpectedDeserializer() throws IOException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        String typeId = "mockTypeId";
        JavaType type = mock(JavaType.class);
        BeanProperty property = mock(BeanProperty.class);
        ObjectMapper objectMapper = mock(ObjectMapper.class);
        JsonFactory jsonFactory = mock(JsonFactory.class);
        JsonParser jsonParser = mock(JsonParser.class);
        when(deserializationContext.getObjectMapper()).thenReturn(objectMapper);
        when(objectMapper.getFactory()).thenReturn(jsonFactory);
        when(jsonFactory.createParser(any())).thenReturn(jsonParser);
        when(jsonParser.getTypeId()).thenReturn(typeId);
        when(typeDeserializerBase._idResolver.typeFromId(deserializationContext, typeId)).thenReturn(type);
        when(deserializationContext.findContextualValueDeserializer(type, property)).thenReturn(new NullifyingDeserializer());

        JsonDeserializer<Object> actual = typeDeserializerBase._findDeserializer(deserializationContext, typeId);
        Assertions.assertTrue(actual instanceof NullifyingDeserializer);
    }

    @Test
    public void _findDefaultImplDeserializer_withNullDefaultImpl_shouldReturnNullifyingDeserializer() throws IOException {
        typeDeserializerBase._defaultImpl = null;

        JsonDeserializer<Object> actual = typeDeserializerBase._findDefaultImplDeserializer(mock(DeserializationContext.class));

        Assertions.assertTrue(actual instanceof NullifyingDeserializer);
    }

    @Test
    public void _findDefaultImplDeserializer_withInvalidRawClass_shouldReturnNullifyingDeserializer()
            throws IOException {
        typeDeserializerBase._defaultImpl = mock(JavaType.class);

        JsonDeserializer<Object> actual = typeDeserializerBase._findDefaultImplDeserializer(mock(DeserializationContext.class));

        Assertions.assertTrue(actual instanceof NullifyingDeserializer);
    }

    @Test
    public void _findDefaultImplDeserializer_withNonNullDefaultImpl_shouldReturnExpectedDeserializer()
            throws IOException {
        JavaType defaultImpl = mock(JavaType.class);
        when(defaultImpl.getRawClass()).thenReturn(String.class);
        typeDeserializerBase._defaultImpl = defaultImpl;

        JsonDeserializer<Object> actual = typeDeserializerBase._findDefaultImplDeserializer(mock(DeserializationContext.class));

        // Replace with your own assertion
        Assertions.assertNull(actual);
    }

    @Test
    public void _handleUnknownTypeId_withTypeIdResolverBase_shouldReturnExpectedDeserializer()
            throws IOException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        TypeIdResolver typeIdResolver = mock(TypeIdResolverBase.class);
        JavaType baseType = mock(JavaType.class);
        when(baseType.toString()).thenReturn("mockBaseType");
        typeDeserializerBase._idResolver = typeIdResolver;
        typeDeserializerBase._baseType = baseType;

        Exception exception = Assertions.assertThrows(IOException.class, () -> typeDeserializerBase
                ._handleUnknownTypeId(deserializationContext, "mockTypeId", typeIdResolver, baseType));

        // Replace with your own assertion
        Assertions.assertEquals("Unknown type id 'mockTypeId' (known type ids are not statically known)",
                exception.getMessage());
    }

    private static class TypeDeserializerBaseTestClass extends TypeDeserializerBase {

        private static final long serialVersionUID = 1;

        private static final Class<?> DEFAULT_IMPL = String.class;

        TypeDeserializerBaseTestClass() {
            super(null, null, "_typePropertyName", true, DEFAULT_IMPL);
        }

        TypeDeserializerBaseTestClass(TypeIdResolver idRes) {
            super(null, idRes, "_typePropertyName", true, DEFAULT_IMPL);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TypeDeserializerBaseTestClass(_idResolver);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }
    }
}

