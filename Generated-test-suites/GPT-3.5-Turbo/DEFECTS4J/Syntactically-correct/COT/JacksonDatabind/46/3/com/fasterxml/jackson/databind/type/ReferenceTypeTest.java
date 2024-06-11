
import org.junit.Test;

public class ReferenceTypeTest {
    @Test
    public void testConstruct() {
        Class<?> cls = ReferenceType.class;
        JavaType refType = new SimpleType(null, null, false);
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType res = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        // assertion
        assertSame(cls, res._class);
        assertSame(refType, res._referencedType);
        assertSame(valueHandler, res._valueHandler);
        assertSame(typeHandler, res._typeHandler);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        ReferenceType res = origin.withTypeHandler(h);

        // assertion
        assertSame(origin._class, res._class);
        assertSame(origin._referencedType, res._referencedType);
        assertSame(origin._valueHandler, res._valueHandler);
        assertSame(h, res._typeHandler);
        assertSame(origin._asStatic, res._asStatic);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        ReferenceType res = origin.withContentTypeHandler(h);

        // assertion
        assertSame(origin._class, res._class);
        assertSame(origin._valueHandler, res._valueHandler);
        assertSame(origin._typeHandler, res._typeHandler);
        assertSame(origin._asStatic, res._asStatic);
        assertSame(origin._referencedType.<Object>getTypeHandler(), res._referencedType.<Object>getTypeHandler());
        assertSame(h, res._referencedType.<Object>getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        ReferenceType res = origin.withValueHandler(h);

        // assertion
        assertSame(origin._class, res._class);
        assertSame(origin._referencedType, res._referencedType);
        assertSame(h, res._valueHandler);
        assertSame(origin._typeHandler, res._typeHandler);
        assertSame(origin._asStatic, res._asStatic);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        ReferenceType res = origin.withContentValueHandler(h);

        // assertion
        assertSame(origin._class, res._class);
        assertSame(origin._valueHandler, res._valueHandler);
        assertSame(origin._typeHandler, res._typeHandler);
        assertSame(origin._asStatic, res._asStatic);
        assertSame(origin._referencedType.<Object>getValueHandler(), res._referencedType.<Object>getValueHandler());
        assertSame(h, res._referencedType.<Object>getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        ReferenceType res = origin.withStaticTyping();

        // assertion
        assertSame(origin._class, res._class);
        assertSame(origin._referencedType, res._referencedType);
        assertSame(origin._valueHandler, res._valueHandler);
        assertSame(origin._typeHandler, res._typeHandler);
        assertTrue(res._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = new Object().getClass();
        JavaType refType = new SimpleType(null, null, false);
        ReferenceType origin = new ReferenceType(cls, refType, null, null, false);

        String res = origin.buildCanonicalName();

        // assertion
        assertEquals(cls.getName() + "<" + refType.toCanonical() + ">", res);
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = new Object().getClass();
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        JavaType res = origin._narrow(subclass);

        // assertion
        assertSame(subclass, res._class);
        assertSame(origin._referencedType, res._referencedType);
        assertSame(origin._valueHandler, res._valueHandler);
        assertSame(origin._typeHandler, res._typeHandler);
        assertSame(origin._asStatic, res._asStatic);
    }

    @Test
    public void test_getReferencedType() {
        JavaType refType = new SimpleType(null, null, false);
        ReferenceType origin = new ReferenceType(null, refType, null, null, false);

        JavaType res = origin.getReferencedType();

        // assertion
        assertSame(refType, res);
    }

    @Test
    public void test_isReferenceType() {
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        boolean res = origin.isReferenceType();

        // assertion
        assertTrue(res);
    }

    @Test
    public void test_containedTypeCount() {
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        int res = origin.containedTypeCount();

        // assertion
        assertEquals(1, res);
    }

    @Test
    public void test_containedType() {
        JavaType refType = new SimpleType(null, null, false);
        ReferenceType origin = new ReferenceType(null, refType, null, null, false);

        JavaType res = origin.containedType(0);

        // assertion
        assertSame(refType, res);
    }

    @Test
    public void test_containedTypeName() {
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        String res = origin.containedTypeName(0);

        // assertion
        assertEquals("T", res);
    }

    @Test
    public void test_getParameterSource() {
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        Class<?> res = origin.getParameterSource();

        // assertion
        assertSame(origin._class, res);
    }

    @Test
    public void test_getErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        StringBuilder res = origin.getErasedSignature(sb);

        // assertion
        assertSame(sb, res);
    }

    @Test
    public void test_getGenericSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType origin = new ReferenceType(null, null, null, null, false);

        StringBuilder res = origin.getGenericSignature(sb);

        // assertion
        assertSame(sb, res);
    }

    @Test
    public void test_toString() {
        Class<?> cls = new Object().getClass();
        JavaType refType = new SimpleType(null, null, false);
        ReferenceType origin = new ReferenceType(cls, refType, null, null, false);

        String res = origin.toString();

        // assertion
        assertEquals("[reference type, class " + origin.buildCanonicalName() + "<" + refType + ">]", res);
    }

    @Test
    public void test_equals() {
        Class<?> cls = new Object().getClass();
        JavaType refType = new SimpleType(null, null, false);
        ReferenceType origin = new ReferenceType(cls, refType, null, null, false);
        ReferenceType same = new ReferenceType(cls, refType, null, null, false);
        ReferenceType notSameClass = new ReferenceType(null, refType, null, null, false);
        ReferenceType notSameRefType = new ReferenceType(cls, null, null, null, false);

        boolean res1 = origin.equals(same);
        boolean res2 = origin.equals(notSameClass);
        boolean res3 = origin.equals(notSameRefType);

        // assertion
        assertTrue(res1);
        assertFalse(res2);
        assertFalse(res3);
    }
}
