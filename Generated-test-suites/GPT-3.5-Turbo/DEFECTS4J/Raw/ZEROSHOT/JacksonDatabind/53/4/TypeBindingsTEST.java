import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBindingsTest {
    
    @Test
    public void testEmptyBindings() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertTrue(typeBindings.isEmpty());
        assertEquals(0, typeBindings.size());
    }
    
    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, null);
        JavaType boundType = typeBindings.findBoundType("T");
        assertNotNull(boundType);
        assertEquals(JavaType.INT_TYPE, boundType);
    }
    
    @Test
    public void testFindBoundTypeWithUnbound() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, new String[] {"U"});
        JavaType boundType = typeBindings.findBoundType("T");
        assertNotNull(boundType);
        assertEquals(JavaType.INT_TYPE, boundType);
    }
    
    @Test
    public void testGetBoundName() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, null);
        String boundName = typeBindings.getBoundName(0);
        assertEquals("T", boundName);
    }
    
    @Test
    public void testGetBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, null);
        JavaType boundType = typeBindings.getBoundType(0);
        assertEquals(JavaType.INT_TYPE, boundType);
    }
    
    @Test
    public void testGetTypeParameters() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, null);
        assertTrue(typeBindings.getTypeParameters().contains(JavaType.INT_TYPE));
    }
    
    @Test
    public void testHasUnbound() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, new String[] {"U"});
        assertTrue(typeBindings.hasUnbound("U"));
    }
    
    @Test
    public void testWithUnboundVariable() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, null);
        TypeBindings newTypeBindings = typeBindings.withUnboundVariable("U");
        assertTrue(newTypeBindings.hasUnbound("U"));
    }
    
    @Test
    public void testToString() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, null);
        String toString = typeBindings.toString();
        assertEquals("<int>", toString);
    }
    
    @Test
    public void testHashCode() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, null);
        int hashCode = typeBindings.hashCode();
        assertEquals(JavaType.INT_TYPE.hashCode() + 1, hashCode);
    }
    
    @Test
    public void testEquals() {
        TypeBindings typeBindings1 = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, null);
        TypeBindings typeBindings2 = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, null);
        assertTrue(typeBindings1.equals(typeBindings2));
    }
    
    @Test
    public void testTypeParameterArray() {
        TypeBindings typeBindings = new TypeBindings(new String[] {"T"}, new JavaType[] {JavaType.INT_TYPE}, null);
        JavaType[] typeParameterArray = typeBindings.typeParameterArray();
        assertEquals(JavaType.INT_TYPE, typeParameterArray[0]);
    }
}