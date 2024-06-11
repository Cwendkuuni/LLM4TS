
import org.junit.Assert;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testGetContentType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        Assert.assertNull(referenceType.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        Assert.assertNull(referenceType.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        Assert.assertTrue(referenceType.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        Assert.assertTrue(referenceType.isReferenceType());
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        Assert.assertNull(referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        Assert.assertTrue(referenceType.isAnchorType());
    }

    @Test
    public void testEquals() {
        ReferenceType referenceType1 = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(Class.class, null, null, null, null, null, null, null, false);
        Assert.assertEquals(referenceType1, referenceType2);
    }

}
