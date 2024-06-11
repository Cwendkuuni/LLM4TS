import static org.junit.Assert.*;
import org.junit.Test;

public class TypeBindingsTest {
    
    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
    }
    
    @Test
    public void testCreateWithNoTypes() {
        TypeBindings bindings = TypeBindings.create(String.class, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }
    
    @Test
    public void testCreateWithNoTypeParameters() {
        TypeBindings bindings = TypeBindings.createIfNeeded(Integer.class, null);
        assertTrue(bindings.isEmpty());
        assertEquals(0, bindings.size());
    }
    
    @Test
    public void testFindBoundType() {
        TypeBindings bindings = TypeBindings.create(Integer.class, new JavaType[] { JavaType.INT });
        JavaType boundType = bindings.findBoundType("T");
        assertEquals(JavaType.INT, boundType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchingNamesAndTypes() {
        TypeBindings.create(String.class, new JavaType[] { JavaType.STRING, JavaType.INT });
    }
    
    @Test
    public void testWithTypeBindingsToString() {
        TypeBindings bindings = TypeBindings.create(String.class, new JavaType[] { JavaType.STRING });
        assertEquals("<Ljava.lang.String;>", bindings.toString());
    }
    
    @Test
    public void testEquals() {
        TypeBindings bindings1 = TypeBindings.create(Integer.class, new JavaType[] { JavaType.INT });
        TypeBindings bindings2 = TypeBindings.create(Integer.class, new JavaType[] { JavaType.INT });
        assertTrue(bindings1.equals(bindings2));
    }

}