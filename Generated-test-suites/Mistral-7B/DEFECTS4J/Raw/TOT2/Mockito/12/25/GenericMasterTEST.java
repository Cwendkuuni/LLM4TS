import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import java.lang.reflect.Field;
import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_whenFieldHasGenericType_thenReturnGenericType() {
        Class clazz = SomeClass.class;
        Field field = clazz.getDeclaredField("fieldName");
        field.setAccessible(true);

        GenericMaster genericMaster = new GenericMaster();
        Class genericType = genericMaster.getGenericType(field);

        assertEquals(String.class, genericType); // replace String.class with the expected generic type
    }

    // Edge case: Test when field has no generic type
    @Test
    public void testGetGenericType_whenFieldHasNoGenericType_thenReturnObjectClass() {
        Class clazz = SomeClass.class;
        Field field = clazz.getDeclaredField("fieldNameWithoutGenericType");
        field.setAccessible(true);

        GenericMaster genericMaster = new GenericMaster();
        Class genericType = genericMaster.getGenericType(field);

        assertEquals(Object.class, genericType);
    }

    // Exception handling: Test when field is not accessible
    @Test(expected = IllegalAccessException.class)
    public void testGetGenericType_whenFieldIsNotAccessible_thenThrowIllegalAccessException() {
        Class clazz = SomeClass.class;
        Field field = clazz.getDeclaredField("fieldWithAccessibleFlagSetToFalse");
        // set accessible flag to false

        GenericMaster genericMaster = new GenericMaster();
        genericMaster.getGenericType(field);
    }
}