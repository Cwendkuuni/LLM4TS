import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(MyTestClass.class, null);
    }

    @Test
    public void testSetReference() {
        JavaType javaType = new JavaType();
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType javaType1 = new JavaType();
        JavaType javaType2 = new JavaType();

        resolvedRecursiveType.setReference(javaType1);
        resolvedRecursiveType.setReference(javaType2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType javaType = new JavaType();
        
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType.getGenericSignature(sb), resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType javaType = new JavaType();
        
        resolvedRecursiveType.setReference(javaType);
        assertEquals(javaType.getErasedSignature(sb), resolvedRecursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
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
    public void testNarrow() {
        Class<?> subclass = MyTestSubclass.class;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = MyTestClass.class;
        TypeBindings bindings = null;
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];

        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, resolvedRecursiveType.toString());

        JavaType javaType = new JavaType(MyTestClass.class);
        resolvedRecursiveType.setReference(javaType);
        expected = "[recursive type; " + MyTestClass.class.getName() + "]";
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(MyTestClass.class, null);

        assertFalse(resolvedRecursiveType.equals(null));
        assertFalse(resolvedRecursiveType.equals(new Object()));
        assertFalse(resolvedRecursiveType.equals(other));

        resolvedRecursiveType.setReference(new JavaType(MyTestClass.class));
        other.setReference(new JavaType(MyTestSubclass.class));

        assertTrue(resolvedRecursiveType.equals(other));
    }

    private static class MyTestClass {
    }

    private static class MyTestSubclass extends MyTestClass {
    }
    
}