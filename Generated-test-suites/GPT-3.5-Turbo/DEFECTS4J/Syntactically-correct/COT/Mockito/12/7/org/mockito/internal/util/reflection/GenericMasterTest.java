
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    @Test
    public void testGetGenericType(){
        GenericMaster genericMaster = new GenericMaster();

        // Test field with generic type
        Field field1 = getField(TestClass.class, "genericField1");
        Class<?> result1 = genericMaster.getGenericType(field1);
        Assert.assertEquals(String.class, result1);

        // Test field without generic type
        Field field2 = getField(TestClass.class, "nonGenericField");
        Class<?> result2 = genericMaster.getGenericType(field2);
        Assert.assertEquals(Object.class, result2);

        // Test field with nested generic type
        Field field3 = getField(TestClass.class, "genericField2");
        Class<?> result3 = genericMaster.getGenericType(field3);
        Assert.assertEquals(List.class, result3);
    }

    /**
     * Utility method to get a field from a class
     * @param clazz The class containing the field
     * @param fieldName The name of the field
     * @return The field object
     */
    private Field getField(Class<?> clazz, String fieldName){
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + fieldName, e);
        }
    }
}

class TestClass {
    private List<String> genericField1;
    private String nonGenericField;
    private List<List<String>> genericField2;
}
