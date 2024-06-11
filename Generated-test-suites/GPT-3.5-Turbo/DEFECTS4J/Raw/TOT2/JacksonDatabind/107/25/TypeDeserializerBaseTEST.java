import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.IOException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public class TypeDeserializerBaseTest {

    TypeDeserializerBase typeDeserializer;
    
    @Mock
    DeserializationContext deserializationContext;
    @Mock
    BeanProperty beanProperty;
    @Mock
    JavaType javaType;
    @Mock
    TypeIdResolver typeIdResolver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        typeDeserializer = new TypeDeserializerBaseStub(javaType, typeIdResolver, "type", true, javaType);
    }

    @Test
    public void testBaseTypeName() {
        assertEquals("com.fasterxml.jackson.databind.JavaType", typeDeserializer.baseTypeName());
    }

    @Test
    public void testGetPropertyName() {
        assertEquals("type", typeDeserializer.getPropertyName());
    }

    @Test
    public void testGetTypeIdResolver() {
        assertEquals(typeIdResolver, typeDeserializer.getTypeIdResolver());
    }

    @Test
    public void testGetDefaultImpl() {
        assertEquals(javaType.getRawClass(), typeDeserializer.getDefaultImpl());
    }

    @Test
    public void testBaseType() {
        assertEquals(javaType, typeDeserializer.baseType());
    }

    @Test
    public void testToString() {
        assertTrue(typeDeserializer.toString().contains("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase"));
    }

    private class TypeDeserializerBaseStub extends TypeDeserializerBase {
        public TypeDeserializerBaseStub(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
            super(baseType, idRes, typePropertyName, typeIdVisible, defaultImpl);
        }

        @Override
        public JsonTypeInfo.As getTypeInclusion() {
            return null;
        }

        @Override
        public TypeDeserializer forProperty(BeanProperty prop) {
            return null;
        }
    }
}