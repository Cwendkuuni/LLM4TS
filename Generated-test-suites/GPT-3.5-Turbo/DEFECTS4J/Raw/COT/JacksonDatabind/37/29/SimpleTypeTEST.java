
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        // Test SimpleType.constructUnsafe(Class<?> raw)
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assert(simpleType.getRawClass().equals(String.class));
    }
    
    @Test
    public void testConstruct() {
        // Test SimpleType.construct(Class<?> cls)
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assert(simpleType.getRawClass().equals(Integer.class));
    }
    
    @Test
    public void testWithContentType() {
        // Test SimpleType.withContentType(JavaType contentType)
        SimpleType baseType = SimpleType.construct(List.class);
        SimpleType withContentTypeType = baseType.withContentType(SimpleType.construct(String.class));
        assert(withContentTypeType.getContentType().getRawClass().equals(String.class));
    }
    
    @Test
    public void testWithTypeHandler() {
        // Test SimpleType.withTypeHandler(Object h)
        SimpleType baseType = SimpleType.construct(String.class);
        SimpleType withTypeHandlerType = baseType.withTypeHandler(new Object());
        assert(withTypeHandlerType.getTypeHandler().getClass().equals(Object.class));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        // Test SimpleType.withContentTypeHandler(Object h)
        SimpleType baseType = SimpleType.construct(List.class);
        SimpleType withContentTypeHandlerType = baseType.withContentTypeHandler(new Object());
        assert(withContentTypeHandlerType.getContentTypeHandler().getClass().equals(Object.class));
    }
    
    @Test
    public void testWithStaticTyping() {
        // Test SimpleType.withStaticTyping()
        SimpleType baseType = SimpleType.construct(String.class);
        SimpleType withStaticTypingType = baseType.withStaticTyping();
        assert(withStaticTypingType.isStatic());
    }
    
    @Test
    public void testRefine() {
        // Test SimpleType.refine(Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
        SimpleType baseType = SimpleType.construct(String.class);
        JavaType refinedType = baseType.refine(Object.class, null, null, null);
        assert(refinedType == null);
    }
    
}
