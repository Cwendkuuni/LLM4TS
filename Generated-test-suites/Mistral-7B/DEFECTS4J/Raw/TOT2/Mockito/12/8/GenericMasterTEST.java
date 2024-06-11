import org.junit.jupiter.api.*;
import org.mockito.internal.util.reflection.*;
import org.mockito.internal.util.reflection.FieldTestUtils;

import java.lang.reflect.*;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = FieldTestUtils.createField(MyClass.class, "myField", new Type[] { int.class });
        Class<Integer> expected = Integer.class;
        Class<Integer> actual = genericMaster.getGenericType(field);
        assertEquals(expected, actual);
    }

    @Test
    public void testNullField() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = null;
        Assertions.assertThrows(NullPointerException.class, () -> genericMaster.getGenericType(field));
    }

    @Test
    public void testNonGenericField() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = FieldTestUtils.createField(MyClass.class, "myField", String.class);
        Class<String> expected = String.class;
        Class<Object> actual = genericMaster.getGenericType(field);
        assertEquals(expected, Object.class);
    }

    @Test
    public void testNestedGenerics() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = FieldTestUtils.createField(InnerClass.class, "myField", List.class, Integer.class);
        Class<List<Integer>> expected = List.class;
        Class<?> actual = genericMaster.getGenericType(field);
        assertTrue(List.class.isAssignableFrom(actual.getClass()));
    }

}

class MyClass {
    private List<Integer> myField;
}

class InnerClass {
    private List<Integer> myField;
}