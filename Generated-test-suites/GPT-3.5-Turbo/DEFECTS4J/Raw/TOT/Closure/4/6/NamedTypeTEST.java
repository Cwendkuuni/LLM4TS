import org.junit.Assert;
import org.junit.Test;

public class NamedTypeTest {

    @Test
    public void testGetReferenceName() {
        NamedType namedType = new NamedType(null, "Test", "Source", 1, 2);
        String referenceName = namedType.getReferenceName();
        Assert.assertEquals("Test", referenceName);
    }

    @Test
    public void testIsNamedType() {
        NamedType namedType = new NamedType(null, "Test", "Source", 1, 2);
        boolean isNamedType = namedType.isNamedType();
        Assert.assertTrue(isNamedType);
    }

    @Test
    public void testIsNominalType() {
        NamedType namedType = new NamedType(null, "Test", "Source", 1, 2);
        boolean isNominalType = namedType.isNominalType();
        Assert.assertTrue(isNominalType);
    }

    @Test
    public void testHashCode() {
        NamedType namedType = new NamedType(null, "Test", "Source", 1, 2);
        int hashCode = namedType.hashCode();
        Assert.assertNotEquals(0, hashCode);
    }
}