import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {
    
    @Test
    public void testForProperty() {
        TypeDeserializerBase typeDeser = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertNull(typeDeser.forProperty(null));
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase typeDeser = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return JsonTypeInfo.As.EXISTING_PROPERTY;
            }
        };

        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeser.getTypeInclusion());
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeser = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("java.lang.Object", typeDeser.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeser = new TypeDeserializerBase(null, null, "type", true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("type", typeDeser.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = new TypeIdResolver() {
            @Override
            public void init(JavaType baseType) {
                
            }

            @Override
            public String idFromValue(Object value) {
                return null;
            }

            @Override
            public String idFromValueAndType(Object value, Class<?> suggestedType) {
                return null;
            }

            @Override
            public String idFromBaseType() {
                return null;
            }

            @Override
            public JavaType typeFromId(DatabindContext context, String id) {
                return null;
            }

            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }

            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        };

        TypeDeserializerBase typeDeser = new TypeDeserializerBase(null, idResolver, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertSame(idResolver, typeDeser.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return Object.class;
            }
        };

        TypeDeserializerBase typeDeser = new TypeDeserializerBase(null, null, null, true, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertSame(Object.class, typeDeser.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        JavaType baseType = new JavaType() {
            @Override
            public Class<?> getRawClass() {
                return Object.class;
            }
        };

        TypeDeserializerBase typeDeser = new TypeDeserializerBase(baseType, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertSame(baseType, typeDeser.baseType());
    }

    @Test
    public void testToString() {
        TypeDeserializerBase typeDeser = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        assertEquals("[TypeDeserializerBase; base-type:null; id-resolver: null]", typeDeser.toString());
    }

}