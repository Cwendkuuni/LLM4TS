import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    @Test
    public void testFindBoundType() {
        TypeBindings typeBindings = TypeBindings.create(Car.class, new JavaType[]{JavaType.INT_TYPE, JavaType.STRING_TYPE});
        JavaType actual = typeBindings.findBoundType("T");
        assertEquals(JavaType.INT_TYPE, actual);
    }

    @Test
    public void testIsEmpty() {
        TypeBindings typeBindings = TypeBindings.create(Car.class, new JavaType[]{});
        boolean actual = typeBindings.isEmpty();
        assertTrue(actual);
    }

    @Test
    public void testSize() {
        TypeBindings typeBindings = TypeBindings.create(Car.class, new JavaType[]{JavaType.INT_TYPE, JavaType.STRING_TYPE});
        int actual = typeBindings.size();
        assertEquals(2, actual);
    }

    @Test
    public void testGetBoundName() {
        TypeBindings typeBindings = TypeBindings.create(Car.class, new JavaType[]{JavaType.INT_TYPE, JavaType.STRING_TYPE});
        String actual = typeBindings.getBoundName(1);
        assertEquals("T", actual);
    }

    @Test
    public void testGetBoundType() {
        TypeBindings typeBindings = TypeBindings.create(Car.class, new JavaType[]{JavaType.INT_TYPE, JavaType.STRING_TYPE});
        JavaType actual = typeBindings.getBoundType(0);
        assertEquals(JavaType.INT_TYPE, actual);
    }

    @Test
    public void testGetTypeParameters() {
        TypeBindings typeBindings = TypeBindings.create(Car.class, new JavaType[]{JavaType.INT_TYPE, JavaType.STRING_TYPE});
        List<JavaType> actual = typeBindings.getTypeParameters();
        assertEquals(Arrays.asList(JavaType.INT_TYPE, JavaType.STRING_TYPE), actual);
    }

    @Test
    public void testHasUnbound() {
        TypeBindings typeBindings = TypeBindings.create(Car.class, new JavaType[]{});
        boolean actual = typeBindings.hasUnbound("T");
        assertFalse(actual);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings typeBindings = TypeBindings.create(Car.class, new JavaType[]{JavaType.INT_TYPE});
        TypeBindings actual = typeBindings.withUnboundVariable("T");
        assertNotNull(actual);
    }

    @Test
    public void testToString() {
        TypeBindings typeBindings = TypeBindings.create(Car.class, new JavaType[]{JavaType.INT_TYPE, JavaType.STRING_TYPE});
        String actual = typeBindings.toString();
        assertEquals("<int,java.lang.String>", actual);
    }

    @Test
    public void testHashCode() {
        TypeBindings typeBindings = TypeBindings.create(Car.class, new JavaType[]{JavaType.INT_TYPE, JavaType.STRING_TYPE});
        int actual = typeBindings.hashCode();
        assertEquals(-1334501927, actual);
    }

    @Test
    public void testEquals() {
        TypeBindings typeBindings = TypeBindings.create(Car.class, new JavaType[]{JavaType.INT_TYPE, JavaType.STRING_TYPE});
        TypeBindings other = TypeBindings.create(Car.class, new JavaType[]{JavaType.INT_TYPE, JavaType.STRING_TYPE});
        boolean actual = typeBindings.equals(other);
        assertTrue(actual);
    }

}