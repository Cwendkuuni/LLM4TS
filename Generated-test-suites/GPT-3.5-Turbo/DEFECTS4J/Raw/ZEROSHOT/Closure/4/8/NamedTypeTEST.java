import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

    private NamedType namedType;

    @Before
    public void setup() {
        namedType = new NamedType(new JSTypeRegistry(), "test", "test", 1, 1);
    }

    @Test
    public void testGetReferencedType() {
        assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
    }

    @Test
    public void testGetReferenceName() {
        assertEquals("test", namedType.getReferenceName());
    }

    @Test
    public void testToStringHelper() {
        assertEquals("test", namedType.toStringHelper(true));
    }

    @Test
    public void testHasReferenceName() {
        assertTrue(namedType.hasReferenceName());
    }

    @Test
    public void testIsNamedType() {
        assertTrue(namedType.isNamedType());
    }

    @Test
    public void testIsNominalType() {
        assertTrue(namedType.isNominalType());
    }

    @Test
    public void testHashCode() {
        assertEquals("test".hashCode(), namedType.hashCode());
    }
}