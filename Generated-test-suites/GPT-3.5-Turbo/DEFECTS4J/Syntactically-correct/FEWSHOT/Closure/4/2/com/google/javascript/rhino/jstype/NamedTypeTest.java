import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class NamedTypeTest {
    @Test
    public void testGetReferencedType() {
        NamedType namedType = new NamedType(null, "testType", "sourceName", 0, 0);
        assertNull(namedType.getReferencedType());
    }

    @Test
    public void testGetReferenceName() {
        NamedType namedType = new NamedType(null, "testType", "sourceName", 0, 0);
        assertEquals("testType", namedType.getReferenceName());
    }

    @Test
    public void testHashCode() {
        NamedType namedType = new NamedType(null, "testType", "sourceName", 0, 0);

        int expectedHashCode = 31 * namedType.getReferenceName().hashCode();
        assertEquals(expectedHashCode, namedType.hashCode());
    }

    @Test
    public void testIsNamedType() {
        NamedType namedType = new NamedType(null, "testType", "sourceName", 0, 0);

        assertTrue(namedType.isNamedType());
    }

    @Test
    public void testIsNominalType() {
        NamedType namedType = new NamedType(null, "testType", "sourceName", 0, 0);

        assertTrue(namedType.isNominalType());
    }
}