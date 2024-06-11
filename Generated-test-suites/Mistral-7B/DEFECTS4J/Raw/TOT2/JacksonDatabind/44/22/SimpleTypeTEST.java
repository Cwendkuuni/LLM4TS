import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {
    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType stringType = new SimpleType(stringClass);
        assertEquals(String.class, stringType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), stringType.getBindings());
        assertNull(stringType.getSuperClass());
        assertNull(stringType.getSuperInterfaces());
        assertFalse(stringType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructorWithBindings() {
        Class<List<Integer>> listIntClass = List.class.getClassGenType(Integer.class);
        SimpleType listIntType = new SimpleType(listIntClass, TypeBindings.standardMapBindings());
        assertEquals(List.class, listIntType.getRawClass());
        assertNotEquals(TypeBindings.emptyBindings(), listIntType.getBindings());
        assertEquals(ArrayList.class, listIntType.getSuperClass());
        assertArrayEquals(new Class[] { Collection.class, Iterator.class }, listIntType.getSuperInterfaces());
        assertFalse(listIntType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructorWithMapClass() {
        SimpleType mapType = new SimpleType(Map.class);
        fail("Should throw IllegalArgumentException for Map class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructorWithCollectionClass() {
        SimpleType collectionType = new SimpleType(Collection.class);
        fail("Should throw IllegalArgumentException for Collection class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructorWithArrayClass() {
        SimpleType arrayType = new SimpleType(int[].class);
        fail("Should throw IllegalArgumentException for array class");
    }
}