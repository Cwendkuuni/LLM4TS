import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;
import org.mockito.internal.util.reflection.FieldTestUtil;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        // Given: A valid Field object
        Field testField = FieldTestUtil.getTestField();

        // When: Calling getGenericType method with the given Field object
        Class expectedType = String.class; // assuming the tested field is of String type
        Class actualType = new GenericMaster().getGenericType(testField);

        // Then: The returned type should match the expected type
        Assert.assertEquals("The returned type does not match the expected type", expectedType, actualType);
    }

    @Test(expected = NullPointerException.class)
    public void testGetGenericTypeWithNullField() {
        // Given: A null Field object
        Field testField = null;

        // When: Calling getGenericType method with the null Field object
        new GenericMaster().getGenericType(testField);
    }

    @Test(expected = IllegalAccessException.class)
    public void testGetGenericTypeWithInaccessibleField() {
        // Given: An inaccessible Field object
        Field testField = new Field("testField", Integer.TYPE, GenericMaster.class); // make it inaccessible
        testField.setAccessible(false);

        // When: Calling getGenericType method with the inaccessible Field object
        new GenericMaster().getGenericType(testField);
    }

    @Test
    public void testGetGenericTypeWithNonParameterizedType() {
        // Given: A Field object with non-parameterized type
        Field testField = FieldTestUtil.getTestFieldWithNonParameterizedType();

        // When: Calling getGenericType method with the given Field object
        Class actualType = new GenericMaster().getGenericType(testField);

        // Then: The returned type should be Object.class
        Assert.assertEquals("The returned type should be Object.class", Object.class, actualType);
    }

    @Test
    public void testGetGenericTypeWithDeeplyNestedGenericType() {
        // Given: A Field object with deeply nested generic type
        Field testField = FieldTestUtil.getTestFieldWithDeeplyNestedGenericType();

        // When: Calling getGenericType method with the given Field object
        Class expectedType = List.class;
        Class actualType = new GenericMaster().getGenericType(testField);

        // Then: The returned type should match the expected type (List in this case)
        Assert.assertEquals("The returned type does not match the expected type", expectedType, actualType);
    }

    @Test
    public void testGetGenericTypeWithArrayType() {
        // Given: A Field object with an array type
        Field testField = FieldTestUtil.getTestFieldWithArrayType();

        // When: Calling getGenericType method with the given Field object
        Class expectedType = int[].class;
        Class actualType = new GenericMaster().getGenericType(testField);

        // Then: The returned type should match the expected type (int[] in this case)
        Assert.assertEquals("The returned type does not match the expected type", expectedType, actualType);
    }
}