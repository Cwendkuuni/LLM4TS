    
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[] { "T" }, new JavaType[] { JavaType.STRING }, null);
        JavaType boundType = typeBindings.findBoundType("T");
        assertEquals(JavaType.STRING, boundType);
    }

    @Test
    public void testIsEmpty() {
        TypeBindings typeBindings = new TypeBindings(new String[0], new JavaType[0], null);
        assertTrue(typeBindings.isEmpty());
        
        TypeBindings nonEmptyTypeBindings = new TypeBindings(new String[] { "T" }, new JavaType[] { JavaType.STRING }, null);
        assertFalse(nonEmptyTypeBindings.isEmpty());
    }

    @Test
    public void testSize() {
        TypeBindings typeBindings = new TypeBindings(new String[0], new JavaType[0], null);
        assertEquals(0, typeBindings.size());
        
        TypeBindings nonEmptyTypeBindings = new TypeBindings(new String[] { "T" }, new JavaType[] { JavaType.STRING }, null);
        assertEquals(1, nonEmptyTypeBindings.size());
    }

    @Test
    public void testGetBoundName() {
        TypeBindings typeBindings = new TypeBindings(new String[] { "T" }, new JavaType[] { JavaType.STRING }, null);
        String boundName = typeBindings.getBoundName(0);
        assertEquals("T", boundName);
        
        String nullBoundName = typeBindings.getBoundName(1);
        assertNull(nullBoundName);
    }

    @Test
    public void testGetBoundType() {
        TypeBindings typeBindings = new TypeBindings(new String[] { "T" }, new JavaType[] { JavaType.STRING }, null);
        JavaType boundType = typeBindings.getBoundType(0);
        assertEquals(JavaType.STRING, boundType);
        
        JavaType nullBoundType = typeBindings.getBoundType(1);
        assertNull(nullBoundType);
    }

    @Test
    public void testGetTypeParameters() {
        TypeBindings typeBindings = new TypeBindings(new String[] { "T" }, new JavaType[] { JavaType.STRING }, null);
        List<JavaType> typeParameters = typeBindings.getTypeParameters();
        assertEquals(1, typeParameters.size());
        assertEquals(JavaType.STRING, typeParameters.get(0));
        
        TypeBindings emptyTypeBindings = new TypeBindings(new String[0], new JavaType[0], null);
        List<JavaType> emptyTypeParameters = emptyTypeBindings.getTypeParameters();
        assertTrue(emptyTypeParameters.isEmpty());
    }
}
