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
        JavaType refType = new JavaType(int.class);
        JavaType anchorType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, anchorType, null, null, false);

        Assert.assertEquals(int[].class, referenceType.getRawClass());
        Assert.assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        Assert.assertEquals(null, referenceType.getSuperClass());
        Assert.assertEquals(new JavaType[0], referenceType.getSuperInterfaces());
        Assert.assertEquals(refType, referenceType.getReferencedType());
        Assert.assertEquals(anchorType, referenceType.getAnchorType());
        Assert.assertFalse(referenceType.isAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType(int[].class);
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        Assert.assertEquals(int[].class, referenceType.getRawClass());
        Assert.assertEquals(TypeBindings.emptyBindings(), referenceType.getTypeBindings());
        Assert.assertEquals(null, referenceType.getSuperClass());
        Assert.assertEquals(new JavaType[0], referenceType.getSuperInterfaces());
        Assert.assertEquals(refType, referenceType.getReferencedType());
        Assert.assertEquals(referenceType, referenceType.getAnchorType());
        Assert.assertTrue(referenceType.isAnchorType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType baseType = new JavaType(int[].class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new JavaType(String.class);
        JavaType refType = new JavaType(int.class);
        ReferenceType.upgradeFrom(baseType, refType);
    }

    @Test
    public void testGetContentType() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);

        Assert.assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);

        Assert.assertEquals(refType, referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);

        Assert.assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);

        Assert.assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);

        StringBuilder expected = new StringBuilder();
        expected.append("[I");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);

        StringBuilder expected = new StringBuilder();
        expected.append("[Ljava/lang/Object;<T:Ljava/lang/Object;>;");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetAnchorType() {
        JavaType refType = new JavaType(int.class);
        JavaType anchorType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, anchorType, null, null, false);

        Assert.assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        JavaType refType = new JavaType(int.class);
        JavaType anchorType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, anchorType, null, null, false);

        Assert.assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        JavaType refType = new JavaType(int.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType, null, null, null, false);

        String expected = "[reference type, class [" + int[].class.getName() + "]"
                + "<T:Ljava/lang/Object;>, content type=" + refType + ']';
        String actual = referenceType.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new JavaType(int.class);
        JavaType refType2 = new JavaType(int.class);
        ReferenceType referenceType1 = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType1, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType2, null, null, null, false);

        Assert.assertTrue(referenceType1.equals(referenceType1));
        Assert.assertTrue(referenceType1.equals(referenceType2));
        Assert.assertTrue(referenceType2.equals(referenceType1));

        JavaType refType3 = new JavaType(long.class);
        ReferenceType referenceType3 = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, new JavaType[0], refType3, null, null, null, false);

        Assert.assertFalse(referenceType1.equals(referenceType3));
        Assert.assertFalse(referenceType3.equals(referenceType1));
    }
}