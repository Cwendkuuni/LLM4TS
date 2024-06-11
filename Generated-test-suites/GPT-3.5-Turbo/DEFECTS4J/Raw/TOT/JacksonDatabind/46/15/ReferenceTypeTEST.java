
import org.junit.Assert;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = new JavaType();
        Object valueHandler = new Object();
        Object typeHandler = new Object();

        ReferenceType referenceType = ReferenceType.construct(cls, refType, valueHandler, typeHandler);

        Assert.assertEquals(cls, referenceType._class);
        Assert.assertEquals(refType, referenceType._referencedType);
        Assert.assertEquals(valueHandler, referenceType._valueHandler);
        Assert.assertEquals(typeHandler, referenceType._typeHandler);
        Assert.assertFalse(referenceType._asStatic);
    }

    @Test
    public void testWithTypHandler_shouldReturnNewReferenceTypeInstance_withDifferentTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType originalReferenceType = new ReferenceType();
        originalReferenceType._typeHandler = new Object();

        ReferenceType updatedReferenceType = originalReferenceType.withTypeHandler(typeHandler);

        Assert.assertNotSame(originalReferenceType, updatedReferenceType);
        Assert.assertEquals(originalReferenceType._class, updatedReferenceType._class);
        Assert.assertEquals(originalReferenceType._referencedType, updatedReferenceType._referencedType);
        Assert.assertEquals(originalReferenceType._valueHandler, updatedReferenceType._valueHandler);
        Assert.assertEquals(typeHandler, updatedReferenceType._typeHandler);
        Assert.assertEquals(originalReferenceType._asStatic, updatedReferenceType._asStatic);
    }

    @Test
    public void testWithContentTypeHandler_shouldReturnNewReferenceTypeInstance_withDifferentContentTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType originalReferenceType = new ReferenceType();
        originalReferenceType._referencedType = new JavaType();

        ReferenceType updatedReferenceType = originalReferenceType.withContentTypeHandler(typeHandler);

        Assert.assertNotSame(originalReferenceType, updatedReferenceType);
        Assert.assertEquals(originalReferenceType._class, updatedReferenceType._class);
        Assert.assertEquals(originalReferenceType._valueHandler, updatedReferenceType._valueHandler);
        Assert.assertEquals(originalReferenceType._typeHandler, updatedReferenceType._typeHandler);
        Assert.assertEquals(typeHandler, updatedReferenceType._referencedType.getTypeHandler());
        Assert.assertEquals(originalReferenceType._asStatic, updatedReferenceType._asStatic);
    }

    @Test
    public void testWithValueHandler_shouldReturnNewReferenceTypeInstance_withDifferentValueHandler() {
        Object valueHandler = new Object();
        ReferenceType originalReferenceType = new ReferenceType();
        originalReferenceType._valueHandler = new Object();

        ReferenceType updatedReferenceType = originalReferenceType.withValueHandler(valueHandler);

        Assert.assertNotSame(originalReferenceType, updatedReferenceType);
        Assert.assertEquals(originalReferenceType._class, updatedReferenceType._class);
        Assert.assertEquals(originalReferenceType._referencedType, updatedReferenceType._referencedType);
        Assert.assertEquals(valueHandler, updatedReferenceType._valueHandler);
        Assert.assertEquals(originalReferenceType._typeHandler, updatedReferenceType._typeHandler);
        Assert.assertEquals(originalReferenceType._asStatic, updatedReferenceType._asStatic);
    }

    @Test
    public void testWithContentValueHandler_shouldReturnNewReferenceTypeInstance_withDifferentContentValueHandler() {
        Object valueHandler = new Object();
        ReferenceType originalReferenceType = new ReferenceType();
        originalReferenceType._referencedType = new JavaType();

        ReferenceType updatedReferenceType = originalReferenceType.withContentValueHandler(valueHandler);

        Assert.assertNotSame(originalReferenceType, updatedReferenceType);
        Assert.assertEquals(originalReferenceType._class, updatedReferenceType._class);
        Assert.assertEquals(originalReferenceType._typeHandler, updatedReferenceType._typeHandler);
        Assert.assertEquals(originalReferenceType._valueHandler, updatedReferenceType._valueHandler);
        Assert.assertEquals(valueHandler, updatedReferenceType._referencedType.getValueHandler());
        Assert.assertEquals(originalReferenceType._asStatic, updatedReferenceType._asStatic);
    }

    @Test
    public void testWithStaticTyping_shouldReturnNewReferenceTypeInstance_withStaticTypingEnabled() {
        ReferenceType originalReferenceType = new ReferenceType();
        originalReferenceType._asStatic = false;

        ReferenceType updatedReferenceType = originalReferenceType.withStaticTyping();

        Assert.assertNotSame(originalReferenceType, updatedReferenceType);
        Assert.assertEquals(originalReferenceType._class, updatedReferenceType._class);
        Assert.assertEquals(originalReferenceType._referencedType, updatedReferenceType._referencedType);
        Assert.assertEquals(originalReferenceType._valueHandler, updatedReferenceType._valueHandler);
        Assert.assertEquals(originalReferenceType._typeHandler, updatedReferenceType._typeHandler);
        Assert.assertTrue(updatedReferenceType._asStatic);
    }

    @Test
    public void testBuildCanonicalName_shouldReturnCanonicalName() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = Object.class;
        referenceType._referencedType = new JavaType();

        String expected = "java.lang.Object<T>";
        String canonicalName = referenceType.buildCanonicalName();

        Assert.assertEquals(expected, canonicalName);
    }

    @Test
    public void testNarrow_shouldReturnNewReferenceTypeInstance_withSubClass() {
        ReferenceType originalReferenceType = new ReferenceType();
        originalReferenceType._referencedType = new JavaType();
        originalReferenceType._valueHandler = new Object();
        originalReferenceType._typeHandler = new Object();
        originalReferenceType._asStatic = true;

        Class<?> subClass = SubClass.class;
        ReferenceType updatedReferenceType = originalReferenceType._narrow(subClass);

        Assert.assertNotSame(originalReferenceType, updatedReferenceType);
        Assert.assertEquals(subClass, updatedReferenceType._class);
        Assert.assertEquals(originalReferenceType._referencedType, updatedReferenceType._referencedType);
        Assert.assertEquals(originalReferenceType._valueHandler, updatedReferenceType._valueHandler);
        Assert.assertEquals(originalReferenceType._typeHandler, updatedReferenceType._typeHandler);
        Assert.assertEquals(originalReferenceType._asStatic, updatedReferenceType._asStatic);
    }

    @Test
    public void testGetReferencedType_shouldReturnReferencedType() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = refType;

        JavaType referencedType = referenceType.getReferencedType();

        Assert.assertEquals(refType, referencedType);
    }

    @Test
    public void testIsReferenceType_shouldReturnTrue() {
        ReferenceType referenceType = new ReferenceType();
        boolean isReferenceType = referenceType.isReferenceType();
        Assert.assertTrue(isReferenceType);
    }

    @Test
    public void testContainedTypeCount_shouldReturnOne() {
        ReferenceType referenceType = new ReferenceType();
        int containedTypeCount = referenceType.containedTypeCount();

        Assert.assertEquals(1, containedTypeCount);
    }

    @Test
    public void testContainedType_shouldReturnReferencedType() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType();
        referenceType._referencedType = refType;

        JavaType containedType = referenceType.containedType(0);

        Assert.assertEquals(refType, containedType);
    }

    @Test
    public void testContainedTypeName_shouldReturnT_whenIndexIsZero() {
        ReferenceType referenceType = new ReferenceType();
        String containedTypeName = referenceType.containedTypeName(0);

        Assert.assertEquals("T", containedTypeName);
    }

    @Test
    public void testGetParameterSource_shouldReturnRawType() {
        Class<?> cls = Object.class;
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = cls;

        Class<?> parameterSource = referenceType.getParameterSource();

        Assert.assertEquals(cls, parameterSource);
    }

    @Test
    public void testGetErasedSignature_shouldReturnErasedSignature() {
        StringBuilder sb = new StringBuilder();
        Class<?> cls = Object.class;
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = cls;
        
        StringBuilder resultSb = referenceType.getErasedSignature(sb);

        Assert.assertSame(sb, resultSb);
        Assert.assertEquals(cls.getName(), sb.toString());
    }

    @Test
    public void testGetGenericSignature_shouldReturnGenericSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = Object.class;
        referenceType._referencedType = new JavaType();

        StringBuilder resultSb = referenceType.getGenericSignature(sb);

        Assert.assertSame(sb, resultSb);
        Assert.assertTrue(sb.length() > 0);
        Assert.assertTrue(sb.toString().contains(referenceType._referencedType.getClass().getSimpleName()));
    }

    @Test
    public void testToString_shouldReturnStringRepresentation() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = Object.class;
        referenceType._referencedType = new JavaType();

        String expected = "[reference type, class java.lang.Object<java.util.ArrayList>]<java.util.ArrayList>";
        String toString = referenceType.toString();

        Assert.assertEquals(expected, toString);
    }

    @Test
    public void testEquals_shouldReturnTrue_whenSameObject() {
        ReferenceType referenceType = new ReferenceType();

        boolean equals = referenceType.equals(referenceType);

        Assert.assertTrue(equals);
    }

    @Test
    public void testEquals_shouldReturnFalse_whenObjectIsNull() {
        ReferenceType referenceType = new ReferenceType();

        boolean equals = referenceType.equals(null);

        Assert.assertFalse(equals);
    }

    @Test
    public void testEquals_shouldReturnFalse_whenNotSameClass() {
        ReferenceType referenceType = new ReferenceType();
        Object object = new Object();

        boolean equals = referenceType.equals(object);

        Assert.assertFalse(equals);
    }

    @Test
    public void testEquals_shouldReturnFalse_whenClassIsDifferent() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType other = new ReferenceType();
        other._class = DifferentClass.class;

        boolean equals = referenceType.equals(other);

        Assert.assertFalse(equals);
    }

    @Test
    public void testEquals_shouldReturnFalse_whenReferencedTypesAreDifferent() {
        ReferenceType referenceType = new ReferenceType();
        ReferenceType other = new ReferenceType();
        referenceType._referencedType = new JavaType();
        other._referencedType = new JavaType();

        boolean equals = referenceType.equals(other);

        Assert.assertFalse(equals);
    }

    @Test
    public void testEquals_shouldReturnTrue_whenAllFieldsAreEqual() {
        ReferenceType referenceType = new ReferenceType();
        referenceType._class = SubClass.class;
        referenceType._referencedType = new JavaType();
        referenceType._valueHandler = new Object();
        referenceType._typeHandler = new Object();
        referenceType._asStatic = true;

        ReferenceType other = new ReferenceType();
        other._class = SubClass.class;
        other._referencedType = new JavaType();
        other._valueHandler = new Object();
        other._typeHandler = new Object();
        other._asStatic = true;
        
        boolean equals = referenceType.equals(other);

        Assert.assertTrue(equals);
    }
}

class SubClass {}
