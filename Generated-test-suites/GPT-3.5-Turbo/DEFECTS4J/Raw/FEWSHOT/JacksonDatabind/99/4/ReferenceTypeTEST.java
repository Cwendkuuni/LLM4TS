import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testGetContentType() {
        ReferenceType type = new ReferenceType(Integer.class, null, null, null, null, null, null, null, false);
        assertEquals(Integer.class, type.getContentType());
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType type = new ReferenceType(Integer.class, null, null, null, null, null, null, null, false);
        assertEquals(Integer.class, type.getReferencedType());
    }

    @Test
    public void testHasContentType() {
        ReferenceType type = new ReferenceType(Integer.class, null, null, null, null, null, null, null, false);
        assertTrue(type.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType type = new ReferenceType(Integer.class, null, null, null, null, null, null, null, false);
        assertTrue(type.isReferenceType());
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType type = new ReferenceType(Integer.class, null, null, null, null, null, null, null, false);
        assertEquals(type, type.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType type = new ReferenceType(Integer.class, null, null, null, null, null, null, null, false);
        assertTrue(type.isAnchorType());
    }

    @Test
    public void testEquals() {
        ReferenceType type1 = new ReferenceType(Integer.class, null, null, null, null, null, null, null, false);
        ReferenceType type2 = new ReferenceType(Integer.class, null, null, null, null, null, null, null, false);
        assertTrue(type1.equals(type2));
    }

    @Test
    public void testToString() {
        ReferenceType type = new ReferenceType(Integer.class, null, null, null, null, null, null, null, false);
        assertNotNull(type.toString());
    }
}