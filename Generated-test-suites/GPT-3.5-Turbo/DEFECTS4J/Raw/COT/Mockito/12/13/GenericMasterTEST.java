import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = SomeClass.class.getDeclaredField("someField");

        // Test case for generic field
        Class genericType = genericMaster.getGenericType(field);
        assertEquals(String.class, genericType);

        // Test case for non-generic field
        field = SomeClass.class.getDeclaredField("anotherField");
        genericType = genericMaster.getGenericType(field);
        assertSame(Object.class, genericType);
    }

    @Test
    public void testGetGenericType_NullGeneric() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = SomeClass.class.getDeclaredField("nullGenericField");

        // Test case for null generic field
        Class genericType = genericMaster.getGenericType(field);
        assertSame(Object.class, genericType);
    }

    @Test
    public void testGetGenericType_NestedGeneric() throws NoSuchFieldException {
        GenericMaster genericMaster = new GenericMaster();
        Field field = SomeClass.class.getDeclaredField("nestedGenericField");

        // Test case for nested generic field
        Class genericType = genericMaster.getGenericType(field);
        assertEquals(ParameterizedType.class, genericType);
    }

    private static class SomeClass {
        private List<String> someField;
        private Object anotherField;
        private List<?> nullGenericField;
        private List<List<String>> nestedGenericField;
    }

}

