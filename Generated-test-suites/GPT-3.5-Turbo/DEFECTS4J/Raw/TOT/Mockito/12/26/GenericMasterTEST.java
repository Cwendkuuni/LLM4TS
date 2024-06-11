import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_RegularGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = GenericMaster.class.getDeclaredField("regularGenericTypeField");

        Class result = genericMaster.getGenericType(field);

        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_NotGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = GenericMaster.class.getDeclaredField("notGenericTypeField");

        Class result = genericMaster.getGenericType(field);

        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_NullGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = GenericMaster.class.getDeclaredField("nullGenericTypeField");

        Class result = genericMaster.getGenericType(field);

        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_NestedGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = GenericMaster.class.getDeclaredField("nestedGenericTypeField");

        Class result = genericMaster.getGenericType(field);

        assertEquals(Integer.class, result);
    }
}