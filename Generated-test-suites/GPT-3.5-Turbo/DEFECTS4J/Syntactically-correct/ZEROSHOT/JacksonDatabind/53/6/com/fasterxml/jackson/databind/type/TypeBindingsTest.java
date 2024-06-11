import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import java.util.*;
import com.fasterxml.jackson.databind.JavaType;

public class TypeBindingsTest {

    @Test
    public void testCreate_1() {
        Class<?> erasedType = null;
        List<JavaType> typeList = null;
        TypeBindings result = TypeBindings.create(erasedType, typeList);
        assertNotNull(result);
    }

    @Test
    public void testCreate_2() {
        Class<?> erasedType = null;
        JavaType[] types = null;
        TypeBindings result = TypeBindings.create(erasedType, types);
        assertNotNull(result);
    }

    @Test
    public void testCreate_3() {
        Class<?> erasedType = Collection.class;
        JavaType typeArg1 = null;
        TypeBindings result = TypeBindings.create(erasedType, typeArg1);
        assertNotNull(result);
    }

    @Test
    public void testCreate_4() {
        Class<?> erasedType = Collection.class;
        JavaType typeArg1 = null;
        JavaType typeArg2 = null;
        TypeBindings result = TypeBindings.create(erasedType, typeArg1, typeArg2);
        assertNotNull(result);
    }

    @Test
    public void testCreateIfNeeded_1() {
        Class<?> erasedType = Collection.class;
        JavaType typeArg1 = null;
        TypeBindings result = TypeBindings.createIfNeeded(erasedType, typeArg1);
        assertNotNull(result);
    }

    @Test
    public void testCreateIfNeeded_2() {
        Class<?> erasedType = null;
        JavaType[] types = null;
        TypeBindings result = TypeBindings.createIfNeeded(erasedType, types);
        assertNotNull(result);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings instance = null;
        String name = null;
        TypeBindings result = instance.withUnboundVariable(name);
        assertNotNull(result);
    }

    @Test
    public void testFindBoundType() {
        TypeBindings instance = null;
        String name = null;
        JavaType result = instance.findBoundType(name);
        assertNotNull(result);
    }

    @Test
    public void testIsEmpty() {
        TypeBindings instance = null;
        boolean result = instance.isEmpty();
        assertFalse(result);
    }

    @Test
    public void testSize() {
        TypeBindings instance = null;
        int result = instance.size();
        assertEquals(0, result);
    }

    @Test
    public void testGetBoundName() {
        TypeBindings instance = null;
        int index = 0;
        String result = instance.getBoundName(index);
        assertNull(result);
    }

    @Test
    public void testGetBoundType() {
        TypeBindings instance = null;
        int index = 0;
        JavaType result = instance.getBoundType(index);
        assertNull(result);
    }

    @Test
    public void testGetTypeParameters() {
        TypeBindings instance = null;
        List<JavaType> result = instance.getTypeParameters();
        assertNotNull(result);
    }

    @Test
    public void testHasUnbound() {
        TypeBindings instance = null;
        String name = null;
        boolean result = instance.hasUnbound(name);
        assertFalse(result);
    }

    @Test
    public void testTypeParameterArray() {
        TypeBindings instance = null;
        JavaType[] result = instance.typeParameterArray();
        assertNotNull(result);
    }

    @Test
    public void testToString() {
        TypeBindings instance = null;
        String result = instance.toString();
        assertNotNull(result);
    }

    @Test
    public void testHashCode() {
        TypeBindings instance = null;
        int result = instance.hashCode();
        assertNotNull(result);
    }

    @Test
    public void testEquals() {
        Object o = null;
        TypeBindings instance = null;
        boolean result = instance.equals(o);
        assertFalse(result);
    }
}