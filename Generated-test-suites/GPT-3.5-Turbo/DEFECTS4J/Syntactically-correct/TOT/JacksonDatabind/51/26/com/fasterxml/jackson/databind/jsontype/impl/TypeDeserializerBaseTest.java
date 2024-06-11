import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypeDeserializerBaseTest {

    private static ObjectMapper objectMapper;
    private static JavaType baseType;
    private static TypeIdResolver idResolver;
    private static String typePropertyName;
    private static boolean typeIdVisible;
    private static JavaType defaultImpl;

    @BeforeClass
    public static void setup() {
        objectMapper = new ObjectMapper();
        baseType = objectMapper.constructType(String.class);
        idResolver = new TestTypeIdResolver();
        typePropertyName = "type";
        typeIdVisible = true;
        defaultImpl = objectMapper.constructType(Integer.class);
    }

    @Test
    public void testForProperty() {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        BeanProperty prop = new TestBeanProperty();
        TypeDeserializer newDeserializer = deserializer.forProperty(prop);
        assertNotNull(newDeserializer);
    }

    @Test
    public void testGetTypeInclusion() {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        JsonTypeInfo.As typeInclusion = deserializer.getTypeInclusion();
        assertNotNull(typeInclusion);
    }

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        String baseTypeName = deserializer.baseTypeName();
        assertEquals("java.lang.String", baseTypeName);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        String propertyName = deserializer.getPropertyName();
        assertEquals("type", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        TypeIdResolver typeIdResolver = deserializer.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        Class<?> defaultImplClass = deserializer.getDefaultImpl();
        assertEquals(Integer.class, defaultImplClass);
    }

    @Test
    public void testToString() {
        TypeDeserializerBase deserializer = new TestTypeDeserializer(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
        String toString = deserializer.toString();
        assertEquals("[TestTypeDeserializer; base-type:String; id-resolver: com.fasterxml.jackson.databind.jsontype.impl.TestTypeIdResolver]", toString);
    }

    private static class TestTypeDeserializer extends TypeDeserializerBase {

        public TestTypeDeserializer(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return new TestTypeDeserializer(_baseType, _idResolver, _typePropertyName, _typeIdVisible, _defaultImpl);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return JsonTypeInfo.As.PROPERTY;
        }
    }

    private static class TestTypeIdResolver implements TypeIdResolver {

        @Override
        public void init(JavaType baseType) {
        }

        @Override
        public String idFromValue(Object value) {
            return null;
        }

        @Override
        public String idFromBaseType() {
            return null;
        }

        @Override
        public String idFromValueAndType(Object value, Class<?> suggestedType) {
            return null;
        }

        @Override
        public JavaType typeFromId(DatabindContext context, String id) throws IOException {
            return null;
        }

        @Override
        public String getDescForKnownTypeIds() {
            return null;
        }

        @Override
        public JsonTypeInfo.Id getMechanism() {
            return JsonTypeInfo.Id.NAME;
        }
    }

    private static class TestBeanProperty implements BeanProperty {

        @Override
        public String getName() {
            return null;
        }

        @Override
        public JavaType getType() {
            return null;
        }

        @Override
        public PropertyName getWrapperName() {
            return null;
        }

        @Override
        public AnnotatedMember getMember() {
            return null;
        }

        @Override
        public boolean isRequired() {
            return false;
        }

        @Override
        public PropertyMetadata getMetadata() {
            return null;
        }

        @Override
        public <A extends Annotation> A getAnnotation(Class<A> acls) {
            return null;
        }

        @Override
        public <A extends Annotation> A getContextAnnotation(Class<A> acls) {
            return null;
        }
    }
}