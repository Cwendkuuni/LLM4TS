import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
    
    @Test
    public void testGetContentType() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        JavaType contentType = referenceType.getContentType();
        assertNull(contentType);
    }
    
    @Test
    public void testGetReferencedType() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        JavaType referencedType = referenceType.getReferencedType();
        assertNull(referencedType);
    }
    
    @Test
    public void testHasContentType() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        boolean hasContentType = referenceType.hasContentType();
        assertTrue(hasContentType);
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        boolean isReferenceType = referenceType.isReferenceType();
        assertTrue(isReferenceType);
    }
    
    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        JavaType anchorType = referenceType.getAnchorType();
        assertNull(anchorType);
    }
    
    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        boolean isAnchorType = referenceType.isAnchorType();
        assertTrue(isAnchorType);
    }
    
    @Test
    public void testEquals() {
        ReferenceType referenceType1 = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        assertTrue(referenceType1.equals(referenceType2));
    }
}