import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = TestClass.class;
        JavaType refType = new SimpleType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);
        assertEquals(cls, referenceType.getParameterSource());
        assertEquals(refType, referenceType.getReferencedType());
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();
        ReferenceType withTypeHandler = referenceType.withTypeHandler(h);
        assertEquals(h, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();
        ReferenceType withContentTypeHandler = referenceType.withContentTypeHandler(h);
        assertEquals(h, withContentTypeHandler.getReferencedType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();
        ReferenceType withValueHandler = referenceType.withValueHandler(h);
        assertEquals(h, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();
        ReferenceType withContentValueHandler = referenceType.withContentValueHandler(h);
        assertEquals(h, withContentValueHandler.getReferencedType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType withStaticTyping = referenceType.withStaticTyping();
        assertTrue(withStaticTyping.isStaticTyping());
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = TestClass.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(cls, refType);
        String expected = cls.getName() + "<" + refType.toCanonical() + ">";
        assertEquals(expected, referenceType.toCanonical());
    }

    @Test
    public void testNarrow() {
        Class<?> cls = TestClass.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(cls, refType);
        Class<?> subClass = SubTestClass.class;
        ReferenceType narrowedType = (ReferenceType) referenceType._narrow(subClass);
        assertEquals(subClass, narrowedType.getParameterSource());
        assertEquals(refType, narrowedType.getReferencedType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType.setReferencedType(refType);
        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType();
        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType();
        assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType.setReferencedType(refType);
        assertEquals(refType, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType();
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        Class<?> cls = TestClass.class;
        ReferenceType referenceType = new ReferenceType(cls, null);
        assertEquals(cls, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType();
        StringBuilder erasedSignature = referenceType.getErasedSignature(sb);
        assertEquals("Erased Signature", sb.toString(), erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType.setReferencedType(refType);
        StringBuilder genericSignature = referenceType.getGenericSignature(sb);
        assertEquals("Generic Signature", sb.toString(), genericSignature.toString());
    }

    @Test
    public void testToString() {
        Class<?> cls = TestClass.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(cls, refType);
        String expected = "[reference type, class " + referenceType.toCanonical() + "]";
        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testEquals() {
        Class<?> cls = TestClass.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType1 = new ReferenceType(cls, refType);
        ReferenceType referenceType2 = new ReferenceType(cls, refType);
        ReferenceType referenceType3 = new ReferenceType();
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType1));
        assertFalse(referenceType1.equals(referenceType3));
    }

}