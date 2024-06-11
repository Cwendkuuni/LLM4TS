import static org.junit.Assert.*;
import org.junit.Test;

public class ReferenceTypeTest {
  
    @Test
    public void testConstruct() {
        ReferenceType refType = ReferenceType.construct(String.class, JavaType.fromType(String.class), null, null);
        assertNotNull(refType);
        assertEquals(String.class, refType.getRawClass());
        assertEquals(0, refType.hashCode());
        assertNull(refType.getValueHandler());
        assertNull(refType.getTypeHandler());
        assertFalse(refType.isStaticTyping());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType refType = new ReferenceType(String.class, JavaType.fromType(String.class), null, null, false);
        ReferenceType withTypeHandler = refType.withTypeHandler(typeHandler);
        assertSame(typeHandler, withTypeHandler.getTypeHandler());
    }
    
    @Test
    public void testReferenceTypeEqualsWithDifferentReferencedType() {
        ReferenceType refType1 = new ReferenceType(String.class, JavaType.fromType(String.class), null, null, false);
        ReferenceType refType2 = new ReferenceType(Integer.class, JavaType.fromType(Integer.class), null, null, false);
        assertFalse(refType1.equals(refType2));
    }
    
    @Test
    public void testReferencedTypeWithStaticTyping() {
        ReferenceType refType = new ReferenceType(String.class, JavaType.fromType(String.class), null, null, false);
        ReferenceType withStaticTyping = refType.withStaticTyping();
        assertTrue(withStaticTyping.isStaticTyping());
    }
  
}