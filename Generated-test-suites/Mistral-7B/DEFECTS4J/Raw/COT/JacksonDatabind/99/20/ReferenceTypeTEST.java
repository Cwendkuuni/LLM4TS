import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testConstructorWithRefTypeAndAnchorType() {
        JavaType refType = new TypeBase() {}.getType(String.class);
        JavaType anchorType = new TypeBase() {}.getType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, null, null, null, refType, anchorType);

        Assert.assertEquals(Integer.class, referenceType.getClass());
        Assert.assertEquals(refType, referenceType.getReferencedType());
        Assert.assertEquals(anchorType, referenceType.getAnchorType());
    }

    @Test
    public void testConstructorWithSuperClassAndRefType() {
        JavaType refType = new TypeBase() {}.getType(String.class);
        JavaType superClass = new TypeBase() {}.getType(Integer.class);
        ReferenceType referenceType = ReferenceType.construct(Integer.class, null, superClass, null, refType);

        Assert.assertEquals(Integer.class, referenceType.getClass());
        Assert.assertEquals(refType, referenceType.getReferencedType());
        Assert.assertNull(referenceType.getAnchorType());
        Assert.assertEquals(superClass, referenceType.getSuperclass());
    }

    @Test
    public void testConstructorWithBaseTypeAndRefType() {
        JavaType refType = new TypeBase() {}.getType(String.class);
        TypeBase baseType = new TypeBase() {}.getType(Integer.class);
        ReferenceType referenceType = ReferenceType.upgradeFrom((JavaType) baseType, refType);

        Assert.assertEquals(Integer.class, referenceType.getClass());
        Assert.assertEquals(refType, referenceType.getReferencedType());
        Assert.assertEquals(baseType, referenceType.getBaseType());
    }

    @Test
    public void testGetContentType() {
        JavaType refType = new TypeBase() {}.getType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, null, null, null, refType, null);

        Assert.assertEquals(refType, referenceType.getContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType = new TypeBase() {}.getType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, null, null, null, refType, null);

        Assert.assertTrue(referenceType.isReferenceType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullBaseType() {
        JavaType refType = new TypeBase() {}.getType(String.class);
        ReferenceType.upgradeFrom(null, refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        JavaType refType = new TypeBase() {}.getType(String.class);
        ReferenceType.upgradeFrom(new Object().getClass(), refType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromNullReferencedType() {
        JavaType baseType = new TypeBase() {}.getType(Integer.class);
        ReferenceType.upgradeFrom(baseType, null);
    }

    @Test
    public void testBuildCanonicalName() {
        JavaType refType = new TypeBase() {}.getType(String.class);
        ReferenceType referenceType = new ReferenceType(Integer.class, null, null, null, refType, null);

        Assert.assertEquals("[reference type, class [I<java.lang.String>]", referenceType.buildCanonicalName());
    }

    @Test
    public void testEquals() {
        JavaType refType1 = new TypeBase() {}.getType(String.class);
        JavaType refType2 = new TypeBase() {}.getType(String.class);
        ReferenceType referenceType1 = new ReferenceType(Integer.class, null, null, null, refType1, null);
        ReferenceType referenceType2 = new ReferenceType(Integer.class, null, null, null, refType2, null);

        Assert.assertEquals(referenceType1, referenceType1);
        Assert.assertEquals(referenceType1, referenceType2);
        Assert.assertNotSame(referenceType1, new Object());
    }

}