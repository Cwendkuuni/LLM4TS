import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Field;

public class GenericMasterTest {

    GenericMaster genericMaster = new GenericMaster();

    @Test
    public void testGetGenericType_nonGenericField_shouldReturnObjectClass() {
        Field field = //create a non-generic field for testing
        Assert.assertEquals(Object.class, genericMaster.getGenericType(field));
    }

    @Test
    public void testGetGenericType_genericField_shouldReturnGenericType() {
        Field field = //create a generic field for testing
        Assert.assertEquals(ExpectedGenericType.class, genericMaster.getGenericType(field));
    }

    @Test
    public void testGetGenericType_nestedGenericField_shouldNotGoDeep() {
        Field field = //create a nested generic field for testing
        Assert.assertEquals(ExpectedGenericType.class, genericMaster.getGenericType(field));
    }

    @Test
    public void testGetGenericType_nullField_shouldReturnObjectClass() {
        Field field = null;
        Assert.assertEquals(Object.class, genericMaster.getGenericType(field));
    }
}