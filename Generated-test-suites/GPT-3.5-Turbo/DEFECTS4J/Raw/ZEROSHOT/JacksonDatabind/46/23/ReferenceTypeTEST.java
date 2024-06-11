import org.junit.*;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertEquals(cls, referenceType.getClass());
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object newTypeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);

        assertEquals(cls, newReferenceType.getClass());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(valueHandler, newReferenceType.withTypeHandler(typeHandler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object newContentValueHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentValueHandler);

        assertEquals(cls, newReferenceType.getClass());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(valueHandler, newReferenceType.withContentTypeHandler(typeHandler));
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object newValueHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);

        assertEquals(cls, newReferenceType.getClass());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(typeHandler, newReferenceType.withValueHandler(valueHandler));
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Object newContentValueHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);

        assertEquals(cls, newReferenceType.getClass());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(typeHandler, newReferenceType.withContentValueHandler(valueHandler));
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType newReferenceType = referenceType.withStaticTyping();

        assertEquals(cls, newReferenceType.getClass());
        assertEquals(refType, newReferenceType.getReferencedType());
        assertEquals(valueHandler, newReferenceType.withStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        String canonicalName = referenceType.buildCanonicalName();

        assertEquals(cls.getName() + '<' + refType.toCanonical() + '>', canonicalName);
    }

    @Test
    public void testNarrow() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        Class<?> subclass = ReferenceTypeTest.class;

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType newReferenceType = referenceType._narrow(subclass);

        assertEquals(subclass, newReferenceType.getClass());
        assertEquals(refType, newReferenceType.getReferencedType());
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        JavaType referencedType = referenceType.getReferencedType();

        assertEquals(refType, referencedType);
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        int containedTypeCount = referenceType.containedTypeCount();

        assertEquals(1, containedTypeCount);
    }

    @Test
    public void testContainedType() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        int index = 0;

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        JavaType containedType = referenceType.containedType(index);

        assertEquals(refType, containedType);
        assertNull(referenceType.containedType(index + 1));
    }

    @Test
    public void testContainedTypeName() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        int index = 0;

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        String containedTypeName = referenceType.containedTypeName(index);

        assertEquals("T", containedTypeName);
        assertNull(referenceType.containedTypeName(index + 1));
    }

    @Test
    public void testGetParameterSource() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        Class<?> parameterSource = referenceType.getParameterSource();

        assertEquals(cls, parameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        StringBuilder sb = new StringBuilder();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);

        assertEquals(cls.getName(), erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        StringBuilder sb = new StringBuilder();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        StringBuilder genericSignature = referenceType.getGenericSignature(sb);

        assertEquals(cls.getName() + '<' + refType.toString() + '>', genericSignature.toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        String string = referenceType.toString();

        assertEquals("[reference type, class " + referenceType.buildCanonicalName() + ']', string);
    }

    @Test
    public void testEquals() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = JavaType.class;
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType other = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        ReferenceType different = ReferenceType.construct(ReferenceTypeTest.class, refType, valueHandler, typeHandler);

        assertTrue(referenceType.equals(other));
        assertTrue(referenceType.equals(referenceType));
        assertFalse(referenceType.equals(null));
        assertFalse(referenceType.equals(new Object()));
        assertFalse(referenceType.equals(different));
    }
}