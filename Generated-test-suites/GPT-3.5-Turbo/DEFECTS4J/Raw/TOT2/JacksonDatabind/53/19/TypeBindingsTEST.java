import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
    }

    @Test
    public void testCreateWithOneType() {
        JavaType type = new JavaType();
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, type);
        assertEquals(1, bindings.size());
        assertEquals("TypeBindings", bindings.getBoundName(0));
        assertEquals(type, bindings.getBoundType(0));
    }

    @Test
    public void testFindBoundType() {
        JavaType type = new JavaType();
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, type);
        JavaType foundType = bindings.findBoundType("TypeBindings");
        assertEquals(type, foundType);
    }

    @Test
    public void testCreateWithTwoTypes() {
        JavaType type1 = new JavaType();
        JavaType type2 = new JavaType();
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, type1, type2);
        assertEquals(2, bindings.size());
        assertEquals("TypeBindings", bindings.getBoundName(0));
        assertEquals(type1, bindings.getBoundType(0));
        assertEquals("V", bindings.getBoundName(1));
        assertEquals(type2, bindings.getBoundType(1));
    }

    @Test
    public void testWithUnboundVariable() {
        JavaType type = new JavaType();
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, type);
        TypeBindings newBindings = bindings.withUnboundVariable("K");
        assertTrue(newBindings.hasUnbound("K"));
    }

    @Test
    public void testToString() {
        JavaType type = new JavaType();
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, type);
        String expected = "<java.util.List>";
        assertEquals(expected, bindings.toString());
    }

}