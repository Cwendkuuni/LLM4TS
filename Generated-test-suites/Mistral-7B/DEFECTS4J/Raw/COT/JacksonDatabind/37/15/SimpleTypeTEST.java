import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testConstruct() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = SimpleType.construct(stringClass);
        JavaType expected = new SimpleType(stringClass);
        assertEquals(expected, simpleType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        Class<Map<String, Integer>> mapClass = Map.class;
        SimpleType simpleType = SimpleType.construct(mapClass);
        fail("IllegalArgumentException not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        Class<List<String>> collectionClass = List.class;
        SimpleType simpleType = SimpleType.construct(collectionClass);
        fail("IllegalArgumentException not thrown");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        Class<int[]> arrayClass = int[].class;
        SimpleType simpleType = SimpleType.construct(arrayClass);
        fail("IllegalArgumentException not thrown");
    }

    @Test
    public void testNarrow() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(String.class);
        JavaType narrowedType = simpleType.narrow(String.class);
        JavaType expected = new SimpleType(String.class);
        assertEquals(expected, narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubclass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(int[].class);
        JavaType narrowedType = simpleType.narrow(stringClass);
        fail("IllegalArgumentException not thrown");
    }

    @Test
    public void testToString() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        String expected = "[simple type, class " + SimpleType.class.getName() + ".String]";
        String result = simpleType.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Class<String> stringClass1 = String.class;
        Class<String> stringClass2 = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass1);
        SimpleType simpleType2 = new SimpleType(stringClass2);
        assertTrue(simpleType1.equals(simpleType2));
    }

    @Test
    public void testNotEquals() {
        Class<String> stringClass1 = String.class;
        Class<Integer> integerClass = Integer.class;
        SimpleType simpleType1 = new SimpleType(stringClass1);
        SimpleType simpleType2 = new SimpleType(integerClass);
        assertFalse(simpleType1.equals(simpleType2));
    }

    @Test
    public void testHashCode() {
        Class<String> stringClass1 = String.class;
        Class<String> stringClass2 = String.class;
        SimpleType simpleType1 = new SimpleType(stringClass1);
        SimpleType simpleType2 = new SimpleType(stringClass2);
        int hashCode1 = simpleType1.hashCode();
        int hashCode2 = simpleType2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testBuildCanonicalName() {
        Class<List<String>> listClass = List.class;
        SimpleType simpleType = new SimpleType(listClass);
        String expected = "[java.util.List, 0, class java.lang.String]";
        String result = simpleType.buildCanonicalName();
        assertEquals(expected, result);
    }

    @Test
    public void testGetErasedSignature() {
        Class<List<String>> listClass = List.class;
        SimpleType simpleType = new SimpleType(listClass);
        StringBuilder expected = new StringBuilder("[java.util.List, 0]");
        StringBuilder result = simpleType.getErasedSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetGenericSignature() {
        Class<List<String>> listClass = List.class;
        SimpleType simpleType = new SimpleType(listClass);
        StringBuilder expected = new StringBuilder("[java.util.List<java.lang.String>]");
        StringBuilder result = simpleType.getGenericSignature(new StringBuilder());
        assertEquals(expected.toString(), result.toString());
    }
}