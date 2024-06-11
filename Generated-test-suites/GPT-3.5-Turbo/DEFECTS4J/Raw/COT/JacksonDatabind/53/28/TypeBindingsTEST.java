
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void findBoundType() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertNull(typeBindings.findBoundType("name"));
    }

    @Test
    public void isEmpty() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertTrue(typeBindings.isEmpty());
    }

    @Test
    public void size() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void getBoundName() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertNull(typeBindings.getBoundName(0));
    }

    @Test
    public void getBoundType() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertNull(typeBindings.getBoundType(0));
    }

    @Test
    public void getTypeParameters() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertEquals(0, typeBindings.getTypeParameters().size());
    }

    @Test
    public void hasUnbound() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertFalse(typeBindings.hasUnbound("name"));
    }

    @Test
    public void hashCodeTest() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertEquals(1, typeBindings.hashCode());
    }

    @Test
    public void equalsTest() {
        TypeBindings typeBindings1 = TypeBindings.emptyBindings();
        TypeBindings typeBindings2 = TypeBindings.emptyBindings();
        assertTrue(typeBindings1.equals(typeBindings1));
        assertTrue(typeBindings1.equals(typeBindings2));
        assertFalse(typeBindings1.equals(null));
        assertFalse(typeBindings1.equals(new Object()));
    }

    @Test
    public void typeParameterArray() {
        TypeBindings typeBindings = TypeBindings.emptyBindings();
        assertArrayEquals(new JavaType[0], typeBindings.typeParameterArray());
    }
    
}
