import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class GenericMasterTest {

    GenericMaster gm = new GenericMaster();

    @Test
    public void testGetGenericType() {
        Field field = SampleClass.class.getDeclaredField("listField");
        Class result = gm.getGenericType(field);
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericTypeWithGenericField() {
        Field field = SampleClass.class.getDeclaredField("genericField");
        Class result = gm.getGenericType(field);
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericTypeWithNestedGenericField() {
        Field field = SampleClass.class.getDeclaredField("nestedGenericField");
        Class result = gm.getGenericType(field);
        assertEquals(Object.class, result);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetGenericTypeWithNoSuchField() throws NoSuchFieldException {
        Field field = SampleClass.class.getDeclaredField("nonExistentField");
        gm.getGenericType(field);
    }

    @Test
    public void testGetGenericTypeWithNonGenericField() {
        Field field = SampleClass.class.getDeclaredField("nonGenericField");
        Class result = gm.getGenericType(field);
        assertEquals(Object.class, result);
    }

    static class SampleClass {
        List<String> listField;
        List<String> genericField;
        List<List<String>> nestedGenericField;
        String nonGenericField;
    }
}