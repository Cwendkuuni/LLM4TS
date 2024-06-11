import static org.junit.Assert.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String baseTypeName = typeDeserializerBase.baseTypeName();

        assertNull(baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "type", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String propertyName = typeDeserializerBase.getPropertyName();

        assertEquals("type", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver typeIdResolver = new TypeIdResolver() {
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

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, typeIdResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        TypeIdResolver resolvedTypeIdResolver = typeDeserializerBase.getTypeIdResolver();

        assertNotNull(resolvedTypeIdResolver);
        assertEquals(typeIdResolver, resolvedTypeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = JavaType.builder().build();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, null, false, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        Class<?> resolvedDefaultImpl = typeDeserializerBase.getDefaultImpl();

        assertNotNull(resolvedDefaultImpl);
        assertEquals(Object.class, resolvedDefaultImpl);
    }

    @Test
    public void testBaseType() {
        JavaType baseType = JavaType.builder().build();

        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        JavaType resolvedBaseType = typeDeserializerBase.baseType();

        assertNotNull(resolvedBaseType);
        assertEquals(baseType, resolvedBaseType);
    }
}