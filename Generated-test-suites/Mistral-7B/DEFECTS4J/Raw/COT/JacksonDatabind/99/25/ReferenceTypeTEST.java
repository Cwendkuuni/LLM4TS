import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructorWithArguments() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = new SimpleType(String.class);
        JavaType anchorType = new SimpleType(Object.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);

        Assert.assertEquals(cls, referenceType.getRawClass());
        Assert.assertEquals(bindings, referenceType.getTypeBindings());
        Assert.assertEquals(superClass, referenceType.getSuperClass());
        Assert.assertArrayEquals(superInts, referenceType.getSuperInterfaces());
        Assert.assertEquals(refType, referenceType.getContentType());
        Assert.assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(String.class);
        JavaType refdType = new SimpleType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refdType);

        Assert.assertNotNull(referenceType);
        Assert.assertSame(baseType, referenceType.getBaseType());
        Assert.assertEquals(refdType, referenceType.getReferencedType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNoReferencedType() {
        JavaType baseType = new SimpleType(String.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new TypeReference<SimpleType>() {};
        JavaType refdType = new SimpleType(Integer.class);
        ReferenceType.upgradeFrom(baseType, refdType);
    }

    @Test
    public void testConstruct() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = new SimpleType(String.class);

        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, refType);

        Assert.assertEquals(cls, referenceType.getRawClass());
        Assert.assertEquals(bindings, referenceType.getTypeBindings());
        Assert.assertEquals(superClass, referenceType.getSuperClass());
        Assert.assertArrayEquals(superInts, referenceType.getSuperInterfaces());
        Assert.assertEquals(refType, referenceType.getContentType());
        Assert.assertNull(referenceType.getAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructNoReferencedType() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        ReferenceType referenceType = ReferenceType.construct(cls, bindings, superClass, superInts, null);
    }

    @Test
    public void testWithContentType() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = new SimpleType(String.class);
        JavaType newContentType = new SimpleType(Float.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        JavaType result = referenceType.withContentType(newContentType);

        Assert.assertNotSame(referenceType, result);
        Assert.assertEquals(newContentType, result.getContentType());
        Assert.assertEquals(refType, result.getReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = new SimpleType(String.class);
        Object handler = new Object() {};

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        JavaType result = referenceType.withTypeHandler(handler);

        Assert.assertNotSame(referenceType, result);
        Assert.assertEquals(refType, result.getReferencedType());
        Assert.assertEquals(handler, result.getTypeHandler());
    }

    @Test
    public void testGetContentType() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = new SimpleType(String.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);
        JavaType contentType = referenceType.getContentType();

        Assert.assertEquals(refType, contentType);
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = new SimpleType(String.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

        Assert.assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = new SimpleType(String.class);

        StringBuilder expectedSb = new StringBuilder("<T>");
        StringBuilder actualSb = new ReferenceType(cls, bindings, superClass, superInts, refType).getGenericSignature(expectedSb);

        String expected = "[Ljava/lang/Object;<T>";
        String actual = actualSb.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = new SimpleType(String.class);

        StringBuilder expectedSb = new StringBuilder("[Ljava/lang/Object;");
        StringBuilder actualSb = new ReferenceType(cls, bindings, superClass, superInts, refType).getErasedSignature(expectedSb);

        String expected = "[Ljava/lang/Object;";
        String actual = actualSb.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetAnchorType() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = new SimpleType(String.class);
        JavaType anchorType = new SimpleType(Object.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);
        JavaType result = referenceType.getAnchorType();

        Assert.assertEquals(anchorType, result);
    }

    @Test
    public void testIsAnchorType() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = new SimpleType(String.class);
        JavaType anchorType = new SimpleType(ReferenceType.class);

        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, anchorType, null, null, false);

        Assert.assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        Class<?> cls = new Class<Integer>() {};
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = new SimpleType(int[].class);
        JavaType[] superInts = new JavaType[0];
        JavaType refType = new SimpleType(String.class);

        StringBuilder expectedSb = new StringBuilder("[reference type, class ");
        StringBuilder actualSb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(cls, bindings, superClass, superInts, refType, null, null, null, false);

        actualSb.append(referenceType.toString());

        String expected = "[reference type, class " + cls.getName() + "<String>]";
        String actual = actualSb.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<?> cls1 = new Class<Integer>() {};
        TypeBindings bindings1 = TypeBindings.emptyBindings();
        JavaType superClass1 = new SimpleType(int[].class);
        JavaType[] superInts1 = new JavaType[0];
        JavaType refType1 = new SimpleType(String.class);

        Class<?> cls2 = new Class<Integer>() {};
        TypeBindings bindings2 = TypeBindings.emptyBindings();
        JavaType superClass2 = new SimpleType(int[].class);
        JavaType[] superInts2 = new JavaType[0];
        JavaType refType2 = new SimpleType(String.class);

        ReferenceType referenceType1 = new ReferenceType(cls1, bindings1, superClass1, superInts1, refType1, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(cls2, bindings2, superClass2, superInts2, refType2, null, null, null, false);

        Assert.assertFalse(referenceType1.equals(referenceType2));

        ReferenceType referenceType3 = new ReferenceType(cls1, bindings1, superClass1, superInts1, refType1, null, null, null, false);
        Assert.assertTrue(referenceType1.equals(referenceType3));
    }
}