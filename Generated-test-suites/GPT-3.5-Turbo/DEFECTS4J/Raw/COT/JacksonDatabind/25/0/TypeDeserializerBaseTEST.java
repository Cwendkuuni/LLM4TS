import org.junit.Test;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class TypeDeserializerBaseTest {
    
    @Test
    public void testBaseTypeName() throws IOException{
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType, TypeIdResolver, "typePropertyName", true, Object.class);
        String baseTypeName = typeDeserializerBase.baseTypeName();
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase", baseTypeName);
    }

    @Test
    public void testGetPropertyName() throws IOException{
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType, TypeIdResolver, "typePropertyName", true, Object.class);
        String propertyName = typeDeserializerBase.getPropertyName();
        assertEquals("typePropertyName", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() throws IOException{
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType, TypeIdResolver, "typePropertyName", true, Object.class);
        TypeIdResolver typeIdResolver = typeDeserializerBase.getTypeIdResolver();
        assertNotNull(typeIdResolver);
    }

    @Test
    public void testGetDefaultImpl() throws IOException{
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType, TypeIdResolver, "typePropertyName", true, Object.class);
        Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
        assertEquals(Object.class, defaultImpl);
    }

    @Test
    public void testToString() throws IOException{
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType, TypeIdResolver, "typePropertyName", true, Object.class);
        String toString = typeDeserializerBase.toString();
        assertEquals("[java.math.BigDecimal; base-type: null; id-resolver: null]", toString);
    }

    @Test
    public void testForProperty() throws IOException{
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType, TypeIdResolver, "typePropertyName", true, Object.class);
        TypeDeserializer typeDeserializer = typeDeserializerBase.forProperty(BeanProperty);
        assertNotNull(typeDeserializer);
    }

    @Test
    public void testGetTypeInclusion() throws IOException{
        TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(JavaType, TypeIdResolver, "typePropertyName", true, Object.class);
        JsonTypeInfo.As typeInclusion = typeDeserializerBase.getTypeInclusion();
        assertNotNull(typeInclusion);
    }

}