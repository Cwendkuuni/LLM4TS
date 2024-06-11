import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(null, null, null, false, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(null, null, "type", false, null);
        assertEquals("type", typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver resolver = new MyTypeIdResolver();
        TypeDeserializerBase typeDeserializer = new TypeDeserializerBaseImpl(null, resolver, null, false, null);
        assertEquals(resolver, typeDeserializer.getTypeIdResolver());
    }

    private static class TypeDeserializerBaseImpl extends TypeDeserializerBase {
        public TypeDeserializerBaseImpl(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }
    }

    private static class MyTypeIdResolver implements TypeIdResolver {
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
    }
}