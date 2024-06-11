import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    

    @Test
    public void testProtectedConstructor() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstructMethod() {
        Class<?> cls = String.class;
        SimpleType.construct(cls);
    }

    @Test
    public void testCopyConstructor() {
        Class<?> cls = String.class;
        SimpleType original = new SimpleType(cls);
        SimpleType copy = new SimpleType(original);
        assertEquals(original, copy);
    }

    @Test
    public void testConstructUnsafe() {
        Class<?> cls = String.class;
        SimpleType simpleType = SimpleType.constructUnsafe(cls);
        assertNotNull(simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeprecatedConstructMethodWithArray() {
        Class<?> cls = int[].class;
        SimpleType.construct(cls);
    }

    @Test
    public void testNarrowMethod() {
        Class<?> superclass = Object.class;
        Class<?> subclass = String.class;
        SimpleType superType = new SimpleType(superclass);
        SimpleType subType = new SimpleType(subclass);
        JavaType narrowed = superType._narrow(subclass);
        assertEquals(subType, narrowed);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowMethodWithInvalidSubclass() {
        Class<?> superclass = Object.class;
        Class<?> subclass = Integer.class;
        SimpleType superType = new SimpleType(superclass);
        JavaType narrowed = superType._narrow(subclass);
        assertNull(narrowed);
    }

    @Test
    public void testWithContentTypeMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType contentType = JavaType.constructCollectionType(List.class, Integer.class);
        assertNull(simpleType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandlerMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        Object handler = new Object();
        JavaType typeHandler = new SimpleType(String.class);
        JavaType result = simpleType.withTypeHandler(typeHandler);
        assertEquals(simpleType, result);
    }

    @Test
    public void testWithStaticTypingMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, new TypeBindings(), null, null, null, null, false);
        JavaType result = simpleType.withStaticTyping();
        assertEquals(simpleType, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithStaticTypingMethodWithFalse() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls, new TypeBindings(), null, null, null, null, true);
        JavaType result = simpleType.withStaticTyping();
        assertNotNull(result); // should not throw exception
        assertFalse(result._asStatic);
    }

    @Test
    public void testRefineMethod() {
        Class<?> rawType = String.class;
        SimpleType baseType = new SimpleType(rawType);
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = JavaType.constructClassType(List.class);
        JavaType[] superInterfaces = new JavaType[] { JavaType.constructClassType(Map.class) };
        JavaType result = baseType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testIsContainerTypeMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignatureMethod() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }

    @Test
    public void testGetGenericSignatureMethod() {
        Class<?> cls = List.class;
        SimpleType simpleType = new SimpleType(cls);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/util/List;<TL;>;", sb.toString());
    }

    
}