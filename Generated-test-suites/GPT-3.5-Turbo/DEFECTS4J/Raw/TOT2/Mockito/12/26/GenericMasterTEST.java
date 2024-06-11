import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_notGenericReturnTypeObjectClass() {
        GenericMaster gm = new GenericMaster();
        Field field = null; // Some non-generic field for testing
        Class result = gm.getGenericType(field);
        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_genericFieldReturnTypeParameterizedType() {
        GenericMaster gm = new GenericMaster();
        // Creating a field with generic type for testing
        Field field = SomeClass.class.getDeclaredField("genericField");
        Class result = gm.getGenericType(field);
        // Assert that the returned type is the expected generic type
        assertEquals(SomeGenericType.class, result);
    }

    @Test
    public void testGetGenericType_nestedGenerics_returnActualTypeArgument() {
        GenericMaster gm = new GenericMaster();
        // Creating a field with nested generic type for testing
        Field field = SomeClass.class.getDeclaredField("nestedGenericField");
        Class result = gm.getGenericType(field);
        // Assert that the returned type is the expected nested generic type
        assertEquals(NestedGenericType.class, result);
    }

    @Test
    public void testGetGenericType_nullFieldReturnTypeObjectClass() {
        GenericMaster gm = new GenericMaster();
        Class result = gm.getGenericType(null);
        // Assert that when field is null, it should return Object.class
        assertEquals(Object.class, result);
    }
}