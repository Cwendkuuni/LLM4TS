import org.junit.Test;

import static org.junit.Assert.*;

public class TypeBindingsTest {
    
    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, null);
        JavaType boundType = typeBindings.findBoundType("T");
        assertEquals(JavaType.INT_TYPE, boundType);
    }
    
    @Test
    public void testIsEmpty() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertTrue(typeBindings.isEmpty());
    }
    
    @Test
    public void testSize() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T", "U"}, new JavaType[] {JavaType.INT_TYPE, JavaType.LONG_TYPE}, null);
        assertEquals(2, typeBindings.size());
    }
    
    @Test
    public void testGetBoundName() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T", "U"}, new JavaType[] {JavaType.INT_TYPE, JavaType.LONG_TYPE}, null);
        assertEquals("T", typeBindings.getBoundName(0));
        assertEquals("U", typeBindings.getBoundName(1));
        assertNull(typeBindings.getBoundName(2));
    }
    
    @Test
    public void testGetBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T", "U"}, new JavaType[] {JavaType.INT_TYPE, JavaType.LONG_TYPE}, null);
        assertEquals(JavaType.INT_TYPE, typeBindings.getBoundType(0));
        assertEquals(JavaType.LONG_TYPE, typeBindings.getBoundType(1));
        assertNull(typeBindings.getBoundType(2));
    }
    
    @Test
    public void testGetTypeParameters() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T", "U"}, new JavaType[] {JavaType.INT_TYPE, JavaType.LONG_TYPE}, null);
        assertEquals(2, typeBindings.getTypeParameters().size());
        assertTrue(typeBindings.getTypeParameters().contains(JavaType.INT_TYPE));
        assertTrue(typeBindings.getTypeParameters().contains(JavaType.LONG_TYPE));
    }
    
    @Test
    public void testHasUnbound() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, new String[] {"U"});
        assertTrue(typeBindings.hasUnbound("U"));
        assertFalse(typeBindings.hasUnbound("V"));
    }
    
    @Test
    public void testToString() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T", "U"}, new JavaType[] {JavaType.INT_TYPE, JavaType.LONG_TYPE}, null);
        assertEquals("<int,long>", typeBindings.toString());
    }
    
    @Test
    public void testHashCode() {
        TypeBindings typeBindings1 = new TypeBindings(new String[] {"T", "U"}, new JavaType[] {JavaType.INT_TYPE, JavaType.LONG_TYPE}, null);
        TypeBindings typeBindings2 = new TypeBindings(new String[] {"T", "U"}, new JavaType[] {JavaType.INT_TYPE, JavaType.LONG_TYPE}, null);
        assertEquals(typeBindings1.hashCode(), typeBindings2.hashCode());
    }
    
    @Test
    public void testEquals() {
        TypeBindings typeBindings1 = new TypeBindings(new String[] {"T", "U"}, new JavaType[] {JavaType.INT_TYPE, JavaType.LONG_TYPE}, null);
        TypeBindings typeBindings2 = new TypeBindings(new String[] {"T", "U"}, new JavaType[] {JavaType.INT_TYPE, JavaType.LONG_TYPE}, null);
        assertTrue(typeBindings1.equals(typeBindings2));
    }
}