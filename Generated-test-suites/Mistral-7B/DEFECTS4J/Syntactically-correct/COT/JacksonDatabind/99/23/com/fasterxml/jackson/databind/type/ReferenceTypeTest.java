import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstructor() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(int[].class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        Assert.assertEquals(cls, referenceType.getRawClass());
        Assert.assertEquals(bindings, referenceType.getTypeBindings());
        Assert.assertEquals(superClass, referenceType.getSuperClass());
        Assert.assertArrayEquals(superInts, referenceType.getSuperInterfaces());
        Assert.assertEquals(refType, referenceType.getContentType());
        Assert.assertNull(referenceType.getAnchorType());
        Assert.assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testReferenceTypeUpgradeFrom() {
        Class<?> baseClass = String.class;
        JavaType baseType = JavaType.construct(baseClass);
        JavaType refType = JavaType.construct(int[].class);

        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        Assert.assertEquals(baseClass, referenceType.getRawClass());
        Assert.assertNotSame(baseType, referenceType.getContentType());
        Assert.assertEquals(refType, referenceType.getContentType());
        Assert.assertEquals(baseType, referenceType.getSuperClass());
        Assert.assertArrayEmpty(referenceType.getSuperInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeUpgradeFromNullBaseType() {
        JavaType refType = JavaType.construct(int[].class);
        ReferenceType.upgradeFrom(null, refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReferenceTypeUpgradeFromNullRefType() {
        JavaType baseType = JavaType.construct(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testReferenceTypeWithContentType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(int[].class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        JavaType newContentType = JavaType.construct(double[].class);
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);

        Assert.assertNotSame(referenceType, newReferenceType);
        Assert.assertEquals(newContentType, newReferenceType.getContentType());
    }

    @Test
    public void testReferenceTypeWithTypeHandler() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(int[].class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = new Object() {};
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        Object newTypeHandler = new Object() {};
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);

        Assert.assertNotSame(referenceType, newReferenceType);
        Assert.assertEquals(newTypeHandler, newReferenceType.getTypeHandler());
    }

    @Test(expected = IllegalStateException.class)
    public void testReferenceTypeWithTypeHandlerSameInstance() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(int[].class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = new Object() {};
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        referenceType.withTypeHandler(typeHandler);
        referenceType.withTypeHandler(typeHandler);
    }

    @Test
    public void testReferenceTypeIsReferenceType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(int[].class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testReferenceTypeHasContentType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(int[].class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testReferenceTypeGetContentType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(int[].class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testReferenceTypeGetReferencedType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(int[].class);
        JavaType anchorType = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testReferenceTypeGetAnchorType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(int[].class);
        JavaType anchorType = JavaType.construct(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testReferenceTypeIsAnchorType() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(int[].class);
        JavaType anchorType = JavaType.construct(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        assertFalse(referenceType.isAnchorType());

        ReferenceType anchorTypeReferenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, referenceType, valueHandler, typeHandler, asStatic);

        assertTrue(anchorTypeReferenceType.isAnchorType());
    }

    @Test
    public void testReferenceTypeToString() {
        Class<?> cls = Object.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType refType = JavaType.construct(int[].class);
        JavaType anchorType = JavaType.construct(String.class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, valueHandler, typeHandler, asStatic);

        String expected = "[reference type, class " + ReferenceType.class.getName() + "...]";

        assertEquals(expected, referenceType.toString());
    }

    @Test
    public void testReferenceTypeEquals() {
        Class<?> cls1 = Object.class;
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        JavaType superClass1 = null;
        JavaType[] superInts1 = null;
        JavaType refType1 = JavaType.construct(int[].class);
        JavaType anchorType1 = JavaType.construct(String.class);
        Object valueHandler1 = null;
        Object typeHandler1 = null;
        boolean asStatic1 = false;

        ReferenceType referenceType1 = new ReferenceType(cls1, bindings1, superClass1, superInts1, refType1, anchorType1, valueHandler1, typeHandler1, asStatic1);

        Class<?> cls2 = Object.class;
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        JavaType superClass2 = null;
        JavaType[] superInts2 = null;
        JavaType refType2 = JavaType.construct(int[].class);
        JavaType anchorType2 = JavaType.construct(String.class);
        Object valueHandler2 = null;
        Object typeHandler2 = null;
        boolean asStatic2 = false;

        ReferenceType referenceType2 = new ReferenceType(cls2, bindings2, superClass2, superInts2, refType2, anchorType2, valueHandler2, typeHandler2, asStatic2);

        assertTrue(referenceType1.equals(referenceType1));
        assertTrue(referenceType1.equals(referenceType1));

        assertTrue(referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(cls1, bindings1, superClass1, superInts1, refType1, anchorType1, valueHandler1, typeHandler1, true);

        assertFalse(referenceType1.equals(referenceType3));

        JavaType contentType1 = JavaType.construct(double[].class);

        ReferenceType referenceType4 = new ReferenceType(cls1, bindings1, superClass1, superInts1, contentType1, anchorType1, valueHandler1, typeHandler1, asStatic1);

        assertFalse(referenceType1.equals(referenceType4));

        JavaType referencedType1 = JavaType.construct(int[].class);

        ReferenceType referenceType5 = new ReferenceType(cls1, bindings1, superClass1, superInts1, refType1, anchorType1, valueHandler1, typeHandler1, asStatic1);

        JavaType newReferencedType = JavaType.construct(long[].class);

        ReferenceType referenceType6 = new ReferenceType(cls1, bindings1, superClass1, superInts1, referenceType5, anchorType1, valueHandler1, typeHandler1, asStatic1);

        ReferenceType referenceType7 = new ReferenceType(cls1, bindings1, superClass1, superInts1, referenceType5, anchorType1, valueHandler1, typeHandler1, asStatic1);

        referenceType6.setReferencedType(newReferencedType);

        assertFalse(referenceType5.equals(referenceType6));

        JavaType referencedType2 = JavaType.construct(short[].class);

        ReferenceType referenceType8 = new ReferenceType(cls1, bindings1, superClass1, superInts1, referenceType5, anchorType1, valueHandler1, typeHandler1, asStatic1);

        ReferenceType referenceType9 = new ReferenceType(cls1, bindings1, superClass1, superInts1, referenceType5, anchorType1, valueHandler1, typeHandler1, asStatic1);

        referenceType8.setAnchorType(referencedType2);

        assertFalse(referenceType5.equals(referenceType8));
    }
}