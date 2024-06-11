import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Test;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Integer.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler, asStatic);

        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertEquals(asStatic, referenceType._asStatic);
    }

    @Test
    public void testWithTypeHandler() {
        Object newTypeHandler = new Object();

        ReferenceType referenceType = createTestReferenceType();

        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);

        assertNotEquals(referenceType, newReferenceType);
        assertEquals(referenceType._class, newReferenceType._class);
        assertEquals(referenceType._referencedType, newReferenceType._referencedType);
        assertEquals(referenceType._valueHandler, newReferenceType._valueHandler);
        assertEquals(newTypeHandler, newReferenceType._typeHandler);
        assertEquals(referenceType._asStatic, newReferenceType._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object newContentTypeHandler = new Object();

        ReferenceType referenceType = createTestReferenceType();

        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);

        assertNotEquals(referenceType, newReferenceType);
        assertEquals(referenceType._class, newReferenceType._class);
        assertNotEquals(referenceType._referencedType, newReferenceType._referencedType);
        assertEquals(referenceType._valueHandler, newReferenceType._valueHandler);
        assertEquals(referenceType._typeHandler, newReferenceType._typeHandler);
        assertEquals(referenceType._asStatic, newReferenceType._asStatic);
    }

    @Test
    public void testWithValueHandler() {
        Object newValueHandler = new Object();

        ReferenceType referenceType = createTestReferenceType();

        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);

        assertNotEquals(referenceType, newReferenceType);
        assertEquals(referenceType._class, newReferenceType._class);
        assertEquals(referenceType._referencedType, newReferenceType._referencedType);
        assertEquals(newValueHandler, newReferenceType._valueHandler);
        assertEquals(referenceType._typeHandler, newReferenceType._typeHandler);
        assertEquals(referenceType._asStatic, newReferenceType._asStatic);
    }

    @Test
    public void testWithContentValueHandler() {
        Object newContentValueHandler = new Object();

        ReferenceType referenceType = createTestReferenceType();

        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);

        assertNotEquals(referenceType, newReferenceType);
        assertEquals(referenceType._class, newReferenceType._class);
        assertEquals(referenceType._referencedType, newReferenceType._referencedType);
        assertEquals(newContentValueHandler, newReferenceType._valueHandler);
        assertEquals(referenceType._typeHandler, newReferenceType._typeHandler);
        assertEquals(referenceType._asStatic, newReferenceType._asStatic);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = createTestReferenceType();

        ReferenceType newReferenceType = referenceType.withStaticTyping();

        assertNotEquals(referenceType, newReferenceType);
        assertEquals(referenceType._class, newReferenceType._class);
        assertEquals(referenceType._referencedType, newReferenceType._referencedType);
        assertEquals(referenceType._valueHandler, newReferenceType._valueHandler);
        assertEquals(referenceType._typeHandler, newReferenceType._typeHandler);
        assertTrue(newReferenceType._asStatic);
    }

    private ReferenceType createTestReferenceType() {
        Class<?> cls = Integer.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        return new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
    }
    
    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = createTestReferenceType();

        JavaType referencedType = referenceType.getReferencedType();

        assertEquals(referenceType._referencedType, referencedType);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = createTestReferenceType();

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = createTestReferenceType();

        int count = referenceType.containedTypeCount();

        assertEquals(1, count);
    }

    @Test
    public void testContainedType() {
        ReferenceType referenceType = createTestReferenceType();

        JavaType containedType = referenceType.containedType(0);

        assertEquals(referenceType._referencedType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = createTestReferenceType();

        String typeName = referenceType.containedTypeName(0);

        assertEquals("T", typeName);
    }

    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = createTestReferenceType();

        Class<?> parameterSource = referenceType.getParameterSource();

        assertEquals(referenceType._class, parameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        ReferenceType referenceType = createTestReferenceType();

        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);

        assertEquals(sb, erasedSignature);
    }

    @Test
    public void testGetGenericSignature() {
        ReferenceType referenceType = createTestReferenceType();

        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = referenceType.getGenericSignature(sb);

        assertEquals(sb, genericSignature);
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = createTestReferenceType();

        String toStringValue = referenceType.toString();

        assertTrue(toStringValue.contains(referenceType.buildCanonicalName()));
        assertTrue(toStringValue.contains(referenceType._referencedType.toString()));
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType1 = createTestReferenceType();
        ReferenceType referenceType2 = createTestReferenceType();

        assertEquals(referenceType1, referenceType2);
    }

    private Object[] getDifferentObjects() {
        return new Object[]{
                new Object(),
                new Object(),
                new Object()
        };
    }

    @Test
    public void testNotEqualsDifferentClass() {
        ReferenceType referenceType = createTestReferenceType();
        Object[] differentObjects = getDifferentObjects();

        for (Object obj : differentObjects) {
            assertNotEquals(referenceType, obj);
        }
    }

    @Test
    public void testNotEqualsDifferentReferencedType() {
        ReferenceType referenceType = createTestReferenceType();
        ReferenceType newReferenceType = createTestReferenceType();
        newReferenceType._referencedType = new JavaType();

        assertNotEquals(referenceType, newReferenceType);
    }

    private ReferenceType createTestReferenceType() {
        Class<?> cls = Integer.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        return new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);
    }
}