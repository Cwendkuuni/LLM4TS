import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = mock(JavaType.class);
        Object valueHandler = mock(Object.class);
        Object typeHandler = mock(Object.class);

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        assertNotNull(referenceType);
        assertEquals(cls, referenceType._class);
        assertEquals(refType, referenceType._referencedType);
        assertEquals(valueHandler, referenceType._valueHandler);
        assertEquals(typeHandler, referenceType._typeHandler);
        assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithTypeHandler_SameHandler() {
        Object h = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, h, false);

        ReferenceType newType = referenceType.withTypeHandler(h);

        assertSame(referenceType, newType);
    }

    @Test
    public void testWithTypeHandler_DifferentHandler() {
        Class<?> cls = Object.class;
        JavaType refType = mock(JavaType.class);
        Object valueHandler = mock(Object.class);
        Object typeHandler = mock(Object.class);
        Object h = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType newType = referenceType.withTypeHandler(h);

        assertNotSame(referenceType, newType);
        assertEquals(referenceType._class, newType._class);
        assertEquals(referenceType._referencedType, newType._referencedType);
        assertEquals(referenceType._valueHandler, newType._valueHandler);
        assertEquals(h, newType._typeHandler);
        assertEquals(referenceType._asStatic, newType._asStatic);
    }

    @Test
    public void testWithContentTypeHandler_SameHandler() {
        Object h = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, h, false);

        ReferenceType newType = referenceType.withContentTypeHandler(h);

        assertSame(referenceType, newType);
    }

    @Test
    public void testWithContentTypeHandler_DifferentHandler() {
        Class<?> cls = Object.class;
        JavaType refType = mock(JavaType.class);
        Object valueHandler = mock(Object.class);
        Object typeHandler = mock(Object.class);
        Object h = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType newType = referenceType.withContentTypeHandler(h);

        assertNotSame(referenceType, newType);
        assertEquals(referenceType._class, newType._class);
        assertNotSame(referenceType._referencedType, newType._referencedType);
        assertEquals(referenceType._valueHandler, newType._valueHandler);
        assertEquals(referenceType._typeHandler, newType._typeHandler);
        assertEquals(referenceType._asStatic, newType._asStatic);
    }

    @Test
    public void testWithValueHandler_SameHandler() {
        Object h = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(null, null, h, null, false);

        ReferenceType newType = referenceType.withValueHandler(h);

        assertSame(referenceType, newType);
    }

    @Test
    public void testWithValueHandler_DifferentHandler() {
        Class<?> cls = Object.class;
        JavaType refType = mock(JavaType.class);
        Object valueHandler = mock(Object.class);
        Object typeHandler = mock(Object.class);
        Object h = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType newType = referenceType.withValueHandler(h);

        assertNotSame(referenceType, newType);
        assertEquals(referenceType._class, newType._class);
        assertEquals(referenceType._referencedType, newType._referencedType);
        assertEquals(h, newType._valueHandler);
        assertEquals(referenceType._typeHandler, newType._typeHandler);
        assertEquals(referenceType._asStatic, newType._asStatic);
    }

    @Test
    public void testWithContentValueHandler_SameHandler() {
        Object h = mock(Object.class);
        JavaType refType = mock(JavaType.class);
        when(refType.<Object>getValueHandler()).thenReturn(h);
        ReferenceType referenceType = new ReferenceType(null, refType, null, null, false);

        ReferenceType newType = referenceType.withContentValueHandler(h);

        assertSame(referenceType, newType);
    }

    @Test
    public void testWithContentValueHandler_DifferentHandler() {
        Class<?> cls = Object.class;
        Object valueHandler = mock(Object.class);
        Object typeHandler = mock(Object.class);
        Object h = mock(Object.class);
        JavaType refType = mock(JavaType.class);
        when(refType.<Object>getValueHandler()).thenReturn(mock(Object.class));
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType newType = referenceType.withContentValueHandler(h);

        assertNotSame(referenceType, newType);
        assertEquals(referenceType._class, newType._class);
        assertNotSame(referenceType._referencedType, newType._referencedType);
        assertEquals(referenceType._valueHandler, newType._valueHandler);
        assertEquals(referenceType._typeHandler, newType._typeHandler);
        assertEquals(referenceType._asStatic, newType._asStatic);
    }

    @Test
    public void testWithStaticTyping_True() {
        Class<?> cls = Object.class;
        JavaType refType = mock(JavaType.class);
        Object valueHandler = mock(Object.class);
        Object typeHandler = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);

        ReferenceType newType = referenceType.withStaticTyping();

        assertNotSame(referenceType, newType);
        assertEquals(referenceType._class, newType._class);
        assertEquals(referenceType._referencedType, newType._referencedType);
        assertEquals(referenceType._valueHandler, newType._valueHandler);
        assertEquals(referenceType._typeHandler, newType._typeHandler);
        assertTrue(newType._asStatic);
    }

    @Test
    public void testWithStaticTyping_False() {
        Object h = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, h, false);

        ReferenceType newType = referenceType.withStaticTyping();

        assertSame(referenceType, newType);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = Object.class;
        JavaType refType = mock(JavaType.class);
        when(refType.toCanonical()).thenReturn("T");
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        String canonicalName = referenceType.buildCanonicalName();

        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName());
        sb.append('<');
        sb.append(refType.toCanonical());
        String expectedCanonicalName = sb.toString();

        assertEquals(expectedCanonicalName, canonicalName);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = Object.class;
        JavaType refType = mock(JavaType.class);
        Object valueHandler = mock(Object.class);
        Object typeHandler = mock(Object.class);
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);

        JavaType narrowedType = referenceType._narrow(subclass);

        assertTrue(narrowedType instanceof ReferenceType);
        assertSame(subclass, narrowedType._class);
        assertSame(refType, narrowedType._referencedType);
        assertSame(valueHandler, narrowedType._valueHandler);
        assertSame(typeHandler, narrowedType._typeHandler);
        assertFalse(narrowedType._asStatic);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = mock(JavaType.class);
        ReferenceType referenceType = new ReferenceType(null, refType, null, null, false);

        JavaType referencedType = referenceType.getReferencedType();

        assertSame(refType, referencedType);
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);

        int count = referenceType.containedTypeCount();

        assertEquals(1, count);
    }

    @Test
    public void testContainedType() {
        JavaType refType = mock(JavaType.class);
        ReferenceType referenceType = new ReferenceType(null, refType, null, null, false);

        JavaType containedType = referenceType.containedType(0);

        assertSame(refType, containedType);
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(null, null, null, null, false);

        String containedTypeName = referenceType.containedTypeName(0);

        assertNull(containedTypeName);
    }

    @Test
    public void testGetParameterSource() {
        Class<?> cls = Object.class;
        ReferenceType referenceType = new ReferenceType(cls, null, null, null, false);

        Class<?> parameterSource = referenceType.getParameterSource();

        assertSame(cls, parameterSource);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = Object.class;
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, null, null, null, false);

        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);

        StringBuilder expectedErasedSignature = new StringBuilder();
        expectedErasedSignature = _classSignature(cls, expectedErasedSignature, true);

        assertEquals(expectedErasedSignature, erasedSignature);
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = Object.class;
        StringBuilder sb = new StringBuilder();
        JavaType refType = mock(JavaType.class);
        when(refType.getGenericSignature(sb)).thenReturn(sb);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        StringBuilder genericSignature = referenceType.getGenericSignature(sb);

        StringBuilder expectedGenericSignature = new StringBuilder();
        expectedGenericSignature = _classSignature(cls, expectedGenericSignature, false);
        expectedGenericSignature.append('<');
        expectedGenericSignature = refType.getGenericSignature(expectedGenericSignature);
        expectedGenericSignature.append(';');

        assertEquals(expectedGenericSignature, genericSignature);
    }

    @Test
    public void testToString() {
        Class<?> cls = Object.class;
        StringBuilder canonicalNameSb = new StringBuilder();
        JavaType refType = mock(JavaType.class);
        when(refType.toString()).thenReturn("refType");
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);

        String toString = referenceType.toString();

        StringBuilder expectedToString = new StringBuilder(40);
        expectedToString.append("[reference type, class ");
        expectedToString.append(referenceType.buildCanonicalName());
        expectedToString.append('<');
        expectedToString.append(refType);
        expectedToString.append('>');
        expectedToString.append(']');

        assertEquals(expectedToString.toString(), toString);
    }

    @Test
    public void testEquals() {
        Class<?> cls = Object.class;
        JavaType refType = mock(JavaType.class);
        ReferenceType referenceType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType sameType = new ReferenceType(cls, refType, null, null, false);
        ReferenceType differentClass = new ReferenceType(Object.class, refType, null, null, false);
        ReferenceType differentRefType = new ReferenceType(cls, mock(JavaType.class), null, null, false);
        ReferenceType nullRefType = new ReferenceType(cls, null, null, null, false);
        Object anotherType = new Object();

        assertTrue(referenceType.equals(referenceType));
        assertTrue(referenceType.equals(sameType));
        assertFalse(referenceType.equals(differentClass));
        assertFalse(referenceType.equals(differentRefType));
        assertFalse(referenceType.equals(nullRefType));
        assertFalse(referenceType.equals(null));
        assertFalse(referenceType.equals(anotherType));
    }
}