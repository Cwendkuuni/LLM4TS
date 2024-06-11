package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.*;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType(MyClass.class);
        resolvedRecursiveType.setReference(ref);

        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_multipleCalls_throwsIllegalStateException() {
        JavaType ref1 = new JavaType(MyClass.class);
        JavaType ref2 = new JavaType(YourClass.class);

        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();

        // Test when _referencedType is null
        assertEquals(expected.toString(), resolvedRecursiveType.getGenericSignature(sb).toString());

        // Test when _referencedType is not null
        JavaType ref = new JavaType(MyClass.class);
        resolvedRecursiveType.setReference(ref);
        expected.append(ref.getGenericSignature(new StringBuilder()).toString());

        assertEquals(expected.toString(), resolvedRecursiveType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();

        // Test when _referencedType is null
        assertEquals(expected.toString(), resolvedRecursiveType.getErasedSignature(sb).toString());

        // Test when _referencedType is not null
        JavaType ref = new JavaType(MyClass.class);
        resolvedRecursiveType.setReference(ref);
        expected.append(ref.getErasedSignature(new StringBuilder()).toString());

        assertEquals(expected.toString(), resolvedRecursiveType.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(MyClass.class);

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();

        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = MyClass.class;

        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = MyClass.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(MySuperClass.class);
        JavaType[] superInterfaces = {new JavaType(YourInterface.class)};

        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        StringBuilder expected = new StringBuilder(40).append("[recursive type; UNRESOLVED");
        assertEquals(expected.toString(), resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEquals_sameValues() {
        JavaType ref = new JavaType(MyClass.class);
        resolvedRecursiveType.setReference(ref);

        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, null);
        other.setReference(ref);

        assertTrue(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEquals_differentValues() {
        JavaType ref1 = new JavaType(MyClass.class);
        JavaType ref2 = new JavaType(YourClass.class);

        resolvedRecursiveType.setReference(ref1);

        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, null);
        other.setReference(ref2);

        assertFalse(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEquals_differentClasses() {
        assertFalse(resolvedRecursiveType.equals(new OtherClass()));
    }

    @After
    public void tearDown() {
        resolvedRecursiveType = null;
    }
}

class MyClass {

}

class MySuperClass {

}

interface YourInterface {

}

class YourClass {

}

class OtherClass {

}