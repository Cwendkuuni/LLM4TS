import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String expected = "";
        String actual = deserializer.baseTypeName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, "type", true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String expected = "type";
        String actual = deserializer.getPropertyName();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver resolver = new CustomTypeIdResolver();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, resolver, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        TypeIdResolver expected = resolver;
        TypeIdResolver actual = deserializer.getTypeIdResolver();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = new JavaType();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(null, null, null, true, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        Class<?> expected = defaultImpl.getRawClass();
        Class<?> actual = deserializer.getDefaultImpl();

        assertEquals(expected, actual);
    }

    @Test
    public void testBaseType() {
        JavaType baseType = new JavaType();
        TypeDeserializerBase deserializer = new TypeDeserializerBase(baseType, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        JavaType expected = baseType;
        JavaType actual = deserializer.baseType();

        assertEquals(expected, actual);
    }

    private static class CustomTypeIdResolver implements TypeIdResolver {

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
        public String idFromValueAndType(DatabindContext context, Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }

        @Override
        public JavaType getBaseType() {
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return null;
        }
    }

}