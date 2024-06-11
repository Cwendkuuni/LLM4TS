import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializer td = new TypeDeserializerBase(null, null, null, true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String baseTypeName = td.baseTypeName();
        assertEquals(null, baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializer td = new TypeDeserializerBase(null, null, "type", false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        String propertyName = td.getPropertyName();
        assertEquals("type", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = new TypeIdResolver() {
            @Override
            public void init(JavaType bt) {}

            @Override
            public String idFromValue(Object value) { return null; }

            @Override
            public String idFromBaseType() { return null; }

            @Override
            public String idFromValueAndType(Object value, Class<?> type) { return null; }

            @Override
            public JavaType typeFromId(DatabindContext context, String id) { return null; }

            @Override
            public JavaType typeFromId(DatabindContext context, String id,
                    BeanProperty property) {
                return null;
            }

            @Override
            public String getDescForKnownTypeIds() { return null; }
        };

        TypeDeserializer td = new TypeDeserializerBase(null, idResolver, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        TypeIdResolver typeIdResolver = td.getTypeIdResolver();
        assertEquals(idResolver, typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        JavaType defaultImpl = new JavaType() {};
        TypeDeserializer td = new TypeDeserializerBase(null, null, null, false, defaultImpl) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        Class<?> defaultImplClass = td.getDefaultImpl();
        assertEquals(JavaType.class, defaultImplClass);
    }

    @Test
    public void testBaseType() {
        JavaType baseType = new JavaType() {};
        TypeDeserializer td = new TypeDeserializerBase(baseType, null, null, false, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };

        JavaType actualBaseType = td.baseType();
        assertEquals(baseType, actualBaseType);
    }

}