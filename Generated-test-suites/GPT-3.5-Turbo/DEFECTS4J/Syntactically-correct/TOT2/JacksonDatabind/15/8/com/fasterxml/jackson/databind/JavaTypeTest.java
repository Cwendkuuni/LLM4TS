import static org.junit.Assert.*;
import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testWithTypeHandler() {
        // Test with valid type handler
        Object typeHandler = new Object();
        JavaType javaType = new ConcreteJavaType(TestClass.class, 0, null, null, false);
        JavaType newJavaType = javaType.withTypeHandler(typeHandler);
        
        assertEquals(typeHandler, newJavaType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        // Test with valid content type handler
        Object contentTypeHandler = new Object();
        JavaType javaType = new ConcreteJavaType(TestClass.class, 0, null, null, false);
        JavaType newJavaType = javaType.withContentTypeHandler(contentTypeHandler);
        
        assertEquals(contentTypeHandler, newJavaType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        // Test with valid value handler
        Object valueHandler = new Object();
        JavaType javaType = new ConcreteJavaType(TestClass.class, 0, null, null, false);
        JavaType newJavaType = javaType.withValueHandler(valueHandler);
        
        assertEquals(valueHandler, newJavaType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        // Test with valid content value handler
        Object contentValueHandler = new Object();
        JavaType javaType = new ConcreteJavaType(TestClass.class, 0, null, null, false);
        JavaType newJavaType = javaType.withContentValueHandler(contentValueHandler);
        
        assertEquals(contentValueHandler, newJavaType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType javaType = new ConcreteJavaType(TestClass.class, 0, null, null, false);
        JavaType newJavaType = javaType.withStaticTyping();
        
        assertTrue(newJavaType.useStaticType());
    }
}

class ConcreteJavaType extends JavaType {
    public ConcreteJavaType(Class<?> raw, int additionalHash, Object valueHandler, Object typeHandler, boolean asStatic) {
        super(raw, additionalHash, valueHandler, typeHandler, asStatic);
    }

    @Override
    public JavaType withTypeHandler(Object h) {
        return new ConcreteJavaType(_class, _hash, _valueHandler, h, _asStatic);
    }

    @Override
    public JavaType withContentTypeHandler(Object h) {
        return new ConcreteJavaType(_class, _hash, _valueHandler, h, _asStatic);
    }

    @Override
    public JavaType withValueHandler(Object h) {
        return new ConcreteJavaType(_class, _hash, h, _typeHandler, _asStatic);
    }

    @Override
    public JavaType withContentValueHandler(Object h) {
        return new ConcreteJavaType(_class, _hash, h, _typeHandler, _asStatic);
    }

    @Override
    public JavaType withStaticTyping() {
        return new ConcreteJavaType(_class, _hash, _valueHandler, _typeHandler, true);
    }

    @Override
    protected JavaType _narrow(Class<?> subclass) {
        return null;
    }

    @Override
    public Class<?> getParameterSource() {
        return null;
    }

    @Override
    public String toString() {
        return "ConcreteJavaType";
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ConcreteJavaType;
    }

}

class TestClass {}