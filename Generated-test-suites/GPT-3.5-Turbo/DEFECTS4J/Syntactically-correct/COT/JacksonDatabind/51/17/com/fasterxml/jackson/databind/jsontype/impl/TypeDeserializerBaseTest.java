import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {

    private TypeDeserializerBase typeDeserializerBase;

    @Before
    public void setUp() {
        JavaType baseType = Mockito.mock(JavaType.class);
        TypeIdResolver idRes = Mockito.mock(TypeIdResolver.class);
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        JavaType defaultImpl = Mockito.mock(JavaType.class);
        typeDeserializerBase = new TypeDeserializerBase(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl) {
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
    public void test_baseTypeName() {
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", baseTypeName);
    }

    @Test
    public void test_getPropertyName() {
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("type", propertyName);
    }

    @Test
    public void test_getTypeIdResolver() {
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void test_getDefaultImpl() {
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertNotNull(defaultImpl);
    }

    @Test
    public void test_toString() {
        String toString = typeDeserializerBase.toString();
        assertEquals("[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]", toString);
    }

    @Test
    public void test__findDefaultImplDeserializer() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        JsonDeserializer<Object> deserializer = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
        assertNotNull(deserializer);
    }
}