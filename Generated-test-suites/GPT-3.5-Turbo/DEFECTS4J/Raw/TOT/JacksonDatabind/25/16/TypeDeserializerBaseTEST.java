import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setup() {
        typeDeserializerBase = new TypeDeserializerBase(
                SomeBaseType.class,
                new SomeTypeIdResolver(),
                "type",
                true,
                SomeDefaultImpl.class
        ) {
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
    public void testFindDeserializer_withValidType_shouldReturnDeserializer() {
        SomeJsonDeserializer deserializer = new SomeJsonDeserializer();
        typeDeserializerBase._deserializers.put("TestType", deserializer);

        SomeJsonDeserializer result = (SomeJsonDeserializer) typeDeserializerBase._findDeserializer(mock(DeserializationContext.class), "TestType");

        assertNotNull(result);
        assertEquals(deserializer, result);
    }

    @Test
    public void testFindDeserializer_withInvalidType_shouldReturnDefaultImplDeserializer() {
        typeDeserializerBase._defaultImplDeserializer = new SomeDefaultImplDeserializer();
        DeserializationContext context = mock(DeserializationContext.class);

        SomeDefaultImplDeserializer result = (SomeDefaultImplDeserializer) typeDeserializerBase._findDeserializer(context, "InvalidType");

        assertNotNull(result);
        assertEquals(typeDeserializerBase._defaultImplDeserializer, result);
        verify(context, never()).findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class));
    }

    @Test
    public void testFindDeserializer_withMissingType_shouldHandleUnknownTypeId() {
        DeserializationContext context = mock(DeserializationContext.class);

        try {
            typeDeserializerBase._findDeserializer(context, "MissingType");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Unknown type id"));
        }
    }

    @Test
    public void testFindDefaultImplDeserializer_withEnabledFailOnInvalidSubtype_shouldReturnNullifyingDeserializer() {
        typeDeserializerBase._defaultImpl = null;

        DeserializationContext context = mock(DeserializationContext.class);
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(true);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);

        assertNotNull(result);
        assertEquals(NullifyingDeserializer.instance, result);
        verify(context, never()).findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class));
    }

    @Test
    public void testFindDefaultImplDeserializer_withDisabledFailOnInvalidSubtype_shouldReturnNullifyingDeserializer() {
        typeDeserializerBase._defaultImpl = null;

        DeserializationContext context = mock(DeserializationContext.class);
        when(context.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)).thenReturn(false);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);

        assertNotNull(result);
        assertEquals(NullifyingDeserializer.instance, result);
        verify(context, never()).findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class));
    }

    @Test
    public void testFindDefaultImplDeserializer_withBogusDefaultImpl_shouldReturnNullifyingDeserializer() {
        typeDeserializerBase._defaultImpl = new BogusDefaultImpl();

        DeserializationContext context = mock(DeserializationContext.class);

        JsonDeserializer<Object> result = typeDeserializerBase._findDefaultImplDeserializer(context);

        assertNotNull(result);
        assertEquals(NullifyingDeserializer.instance, result);
        verify(context, never()).findContextualValueDeserializer(any(JavaType.class), any(BeanProperty.class));
    }

    @Test
    public void testToString_shouldReturnStringRepresentation() {
        String expectedResult = "[TypeDeserializerBase; base-type:SomeBaseType; id-resolver: SomeTypeIdResolver]";
        String result = typeDeserializerBase.toString();
        assertEquals(expectedResult, result);
    }
}