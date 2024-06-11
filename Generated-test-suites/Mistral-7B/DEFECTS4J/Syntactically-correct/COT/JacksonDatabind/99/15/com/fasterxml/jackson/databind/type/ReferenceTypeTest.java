import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructorWithArguments() {
        JavaType refType = new JavaType(String.class);
        JavaType anchorType = new JavaType(Integer.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, refType, anchorType, null, null, false);

        Assert.assertEquals(String.class, referenceType.getClass());
        Assert.assertEquals(refType, referenceType.getReferencedType());
        Assert.assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new JavaType(Integer.class);
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom(baseType, refType);

        Assert.assertEquals(String.class, referenceType.getClass());
        Assert.assertEquals(refType, referenceType.getReferencedType());
        Assert.assertEquals(baseType, referenceType.getBaseType());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, refType, null, null, null, false);

        Assert.assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, refType, null, null, null, false);

        Assert.assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testHasContentType() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, refType, null, null, null, false);

        Assert.assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, refType, null, null, null, false);

        StringBuilder expected = new StringBuilder("[class java.lang.String");
        StringBuilder actual = referenceType.getErasedSignature(new StringBuilder());

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new JavaType(String.class);
        ReferenceType referenceType = new ReferenceType(String.class, TypeBindings.emptyBindings(),
                null, null, refType, null, null, null, false);

        StringBuilder expected = new StringBuilder("[reference type, class java.lang.String<T>]");
        StringBuilder actual = referenceType.getGenericSignature(new StringBuilder());

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullRefType() {
        JavaType baseType = new JavaType(Integer.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType baseType = new JavaType(Double.class);
        JavaType refType = new JavaType(String.class);
        ReferenceType.upgradeFrom(baseType, refType);
    }
}