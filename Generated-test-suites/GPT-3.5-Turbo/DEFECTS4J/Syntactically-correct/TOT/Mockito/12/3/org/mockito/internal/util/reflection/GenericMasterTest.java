import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster gm = new GenericMaster();

        // Test case 1: Field with generic type
        Field field1 = MyClass.class.getDeclaredField("field1");
        Class expected1 = String.class;
        Class actual1 = gm.getGenericType(field1);
        assertEquals("Field with generic type should return the correct generic type", expected1, actual1);

        // Test case 2: Field with no generic type
        Field field2 = MyClass.class.getDeclaredField("field2");
        Class expected2 = Object.class;
        Class actual2 = gm.getGenericType(field2);
        assertEquals("Field with no generic type should return Object.class", expected2, actual2);

        // Test case 3: Field with nested generics
        Field field3 = MyClass.class.getDeclaredField("field3");
        Class expected3 = List.class;
        Class actual3 = gm.getGenericType(field3);
        assertEquals("Field with nested generics should return the correct generic type", expected3, actual3);

        // Test case 4: Field with multiple actual type arguments
        Field field4 = MyClass.class.getDeclaredField("field4");
        Class expected4 = String.class;
        Class actual4 = gm.getGenericType(field4);
        assertEquals("Field with multiple actual type arguments should return the first generic type", expected4, actual4);

        // Test case 5: Field with no actual type arguments
        Field field5 = MyClass.class.getDeclaredField("field5");
        Class expected5 = Object.class;
        Class actual5 = gm.getGenericType(field5);
        assertEquals("Field with no actual type arguments should return Object.class", expected5, actual5);
    }

    private class MyClass {
        private List<String> field1;
        private Object field2;
        private List<List<String>> field3;
        private Map<String, List<Integer>> field4;
        private List field5;
    }
}