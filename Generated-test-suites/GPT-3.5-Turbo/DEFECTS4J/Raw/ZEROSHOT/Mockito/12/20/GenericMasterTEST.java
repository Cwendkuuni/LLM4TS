import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType_genericField_ReturnsActualTypeArgument() {
        Field field = getField("fieldWithType", GenericType.class);
        Class result = genericMaster.getGenericType(field);
        Assert.assertEquals(GenericType.class, result);
    }

    @Test
    public void testGetGenericType_nonGenericField_ReturnsObjectClass() {
        Field field = getField("fieldWithoutType", Object.class);
        Class result = genericMaster.getGenericType(field);
        Assert.assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_nestedGenericField_ReturnsActualTypeArgument() {
        Field field = getField("nestedFieldWithType", NestedGenericType.class);
        Class result = genericMaster.getGenericType(field);
        Assert.assertEquals(NestedGenericType.class, result);
    }

    // Helper method to create a Field object with specified name and type
    private Field getField(String fieldName, Class<?> fieldType) {
        try {
            return TestClass.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Unable to create field", e);
        }
    }

    // Test classes for creating fields
    public static class TestClass {
        private GenericType<String> fieldWithType;
        private Object fieldWithoutType;
        private GenericType<NestedGenericType<String>> nestedFieldWithType;
    }

    public static class GenericType<T> {
        // not used
    }

    public static class NestedGenericType<T> {
        // not used
    }
}