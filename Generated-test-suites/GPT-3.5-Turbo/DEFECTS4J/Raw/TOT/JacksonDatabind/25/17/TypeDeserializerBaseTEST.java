import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

    @Test
    public void test_getPropertyName() {
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, null, "typeProperty", true, null) {
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
        assertNotNull(propertyName);
        assertEquals("typeProperty", propertyName);
    }
    
    @Test
    public void test_getTypeName_whenTypeIdIsNotFoundAndDefaultImplExists() {
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
                return "unknownTypeId";
            }
            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }
            @Override
            public JavaType typeFromId(DatabindContext context, String id) {
                return null;
            }
            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        };
        
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, "typeProperty", true, String.class) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNotNull(defaultImpl);
        assertEquals(String.class, defaultImpl);
    }
    
    @Test
    public void test_getTypeName_whenTypeIdIsNotFoundAndDefaultImplDoesNotExist() {
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
                return "unknownTypeId";
            }
            @Override
            public String getDescForKnownTypeIds() {
                return null;
            }
            @Override
            public JavaType typeFromId(DatabindContext context, String id) {
                return null;
            }
            @Override
            public JsonTypeInfo.Id getMechanism() {
                return null;
            }
        };
        
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(null, idResolver, "typeProperty", true, null) {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }
            @Override
            public JsonTypeInfo.As getTypeInclusion() {
                return null;
            }
        };
        
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNull(defaultImpl);
    }
    
}