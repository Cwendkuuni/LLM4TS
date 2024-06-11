import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testConstructReferenceType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        
        // Test class and referenced type
        assertEquals(String.class, referenceType.getClass());
        assertEquals(null, referenceType.getReferencedType());
        
        // Test content type and anchor type
        assertEquals(null, referenceType.getContentType());
        assertEquals(referenceType, referenceType.getAnchorType());
        
        // Test has content type and is reference type
        assertEquals(false, referenceType.hasContentType());
        assertEquals(true, referenceType.isReferenceType());
    }

    @Test
    public void testWithContentType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        ReferenceType updatedType = referenceType.withContentType(Integer.class);
        
        // Test that the content type has been updated
        assertEquals(Integer.class, updatedType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        ReferenceType updatedType = referenceType.withTypeHandler(Object.class);
        
        // Test that the type handler has been updated
        assertEquals(Object.class, updatedType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        ReferenceType updatedType = referenceType.withContentTypeHandler(Object.class);
        
        // Test that the content type handler has been updated
        assertEquals(Object.class, updatedType.getContentValueHandler());
    }

    @Test
    public void testWithValueHandler() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        ReferenceType updatedType = referenceType.withValueHandler(Object.class);
        
        // Test that the value handler has been updated
        assertEquals(Object.class, updatedType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        ReferenceType updatedType = referenceType.withContentValueHandler(Object.class);
        
        // Test that the content value handler has been updated
        assertEquals(Object.class, updatedType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        ReferenceType updatedType = referenceType.withStaticTyping();
        
        // Test that static typing has been enabled
        assertEquals(true, updatedType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        ReferenceType refinedType = referenceType.refine(Object.class, null, null, null);
        
        // Test that the raw type has been refined
        assertEquals(Object.class, refinedType.getRawClass());
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        
        // Test that the anchor type is the reference type itself
        assertEquals(referenceType, referenceType.getAnchorType());
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        
        // Test that the reference type is the anchor type
        assertEquals(true, referenceType.isAnchorType());
    }

    @Test
    public void testToString() {
        ReferenceType referenceType = ReferenceType.construct(String.class, null);
        String toString = referenceType.toString();
        
        // Test that the string representation contains the class name and referenced type
        assertTrue(toString.contains("ReferenceType"));
        assertTrue(toString.contains("String"));
    }

    @Test
    public void testEquals() {
        ReferenceType type1 = ReferenceType.construct(String.class, null);
        ReferenceType type2 = ReferenceType.construct(Integer.class, null);
        
        // Test that type1 and type2 are not equal
        assertNotEquals(type1, type2);
    }
}