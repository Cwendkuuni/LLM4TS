import org.junit.Assert;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = ReferenceType.construct(cls, refType, null, null);
        Assert.assertEquals(cls, referenceType._class);
        Assert.assertEquals(refType, referenceType._referencedType);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();
        ReferenceType newReferenceType = referenceType.withTypeHandler(h);
        Assert.assertEquals(h, newReferenceType._typeHandler);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = refType;
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(h);
        Assert.assertEquals(h, newReferenceType._referencedType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        ReferenceType referenceType = new ReferenceType();
        ReferenceType newReferenceType = referenceType.withValueHandler(h);
        Assert.assertEquals(h, newReferenceType._valueHandler);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = refType;
        ReferenceType newReferenceType = referenceType.withContentValueHandler(h);
        Assert.assertEquals(h, newReferenceType._referencedType.getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        Assert.assertTrue(newReferenceType._asStatic);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = cls;
        referenceType._referencedType = refType;
        String expectedCanonicalName = cls.getName() + '<' + refType.toCanonical() + '>';
        Assert.assertEquals(expectedCanonicalName, referenceType.buildCanonicalName());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Integer.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = refType;
        ReferenceType newReferenceType = referenceType._narrow(subclass);
        Assert.assertEquals(subclass, newReferenceType._class);
        Assert.assertEquals(refType, newReferenceType._referencedType);
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = refType;
        Assert.assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType();
        Assert.assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType();
        Assert.assertEquals(1, referenceType.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = refType;
        Assert.assertEquals(refType, referenceType.containedType(0));
        Assert.assertNull(referenceType.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType();
        Assert.assertEquals("T", referenceType.containedTypeName(0));
        Assert.assertNull(referenceType.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = cls;
        Assert.assertEquals(cls, referenceType.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = String.class;
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = cls;
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedSignature = new StringBuilder();
        expectedSignature.append(cls.getName());
        referenceType.getErasedSignature(sb);
        Assert.assertEquals(expectedSignature.toString(), sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = cls;
        referenceType._referencedType = refType;
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedSignature = new StringBuilder();
        expectedSignature.append(cls.getName()).append('<').append(refType.getGenericSignature(new StringBuilder())).append(';');
        referenceType.getGenericSignature(sb);
        Assert.assertEquals(expectedSignature.toString(), sb.toString());
    }
    
    @Test
    public void testToString() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = cls;
        referenceType._referencedType = refType;
        StringBuilder expectedToString = new StringBuilder(40);
        expectedToString.append("[reference type, class ")
                .append(referenceType.buildCanonicalName())
                .append('<')
                .append(refType)
                .append('>')
                .append(']');
        Assert.assertEquals(expectedToString.toString(), referenceType.toString());
    }
    
    @Test
    public void testEquals() {
        Class<?> cls = String.class;
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = cls;
        referenceType._referencedType = refType;

        ReferenceType sameReferenceType = new ReferenceType();
        sameReferenceType._class = cls;
        sameReferenceType._referencedType = refType;
        Assert.assertTrue(referenceType.equals(sameReferenceType));

        ReferenceType differentReferenceType = new ReferenceType();
        differentReferenceType._class = Integer.class;
        differentReferenceType._referencedType = refType;
        Assert.assertFalse(referenceType.equals(differentReferenceType));
    }
}