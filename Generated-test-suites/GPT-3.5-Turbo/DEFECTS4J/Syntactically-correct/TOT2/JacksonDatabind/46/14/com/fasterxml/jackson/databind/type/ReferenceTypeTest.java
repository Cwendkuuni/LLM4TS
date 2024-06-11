import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
    
    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.referenceType(Object.class));
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(Object.class, referenceType.getReferencedType().getRawClass());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.referenceType(Integer.class), null, null, false);
        ReferenceType withTypeHandler = referenceType.withTypeHandler(typeHandler);
        assertNotNull(withTypeHandler);
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.referenceType(Integer.class), null, null, false);
        ReferenceType withStaticTyping = referenceType.withStaticTyping();
        assertNotNull(withStaticTyping);
        assertTrue(withStaticTyping.isStaticTyping());
    }
    
    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.referenceType(Integer.class), null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }
    
    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.referenceType(Integer.class), null, null, false);
        assertEquals(Integer.class, referenceType.containedType(0).getRawClass());
        assertNull(referenceType.containedType(1)); // Should return null for invalid index
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.referenceType(Integer.class), null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1)); // Should return null for invalid index
    }
    
    @Test
    public void testGetCanonicalName() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.referenceType(Integer.class), null, null, false);
        assertEquals("java.lang.String<java.lang.Integer>", referenceType.getCanonicalName());
    }
    
    @Test
    public void testEquals() {
        ReferenceType referenceType1 = new ReferenceType(String.class, JavaType.referenceType(Integer.class), null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, JavaType.referenceType(Integer.class), null, null, false);
        assertEquals(referenceType1, referenceType2);
    }
}