
import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Field;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster gm = new GenericMaster();
        
        // Test case 1: Test a non-generic field
        Field nonGenericField = null; // Replace with actual non-generic field
        Class nonGenericType = gm.getGenericType(nonGenericField);
        Assert.assertEquals(Object.class, nonGenericType);
        
        // Test case 2: Test a generic field
        Field genericField = null; // Replace with actual generic field
        Class genericType = gm.getGenericType(genericField);
        // Replace with expected generic type
        Assert.assertEquals(ExpectedGenericType.class, genericType);
        
        // Test case 3: Test a field with nested generics
        Field nestedGenericField = null; // Replace with actual field with nested generics
        Class nestedGenericType = gm.getGenericType(nestedGenericField);
        // Replace with expected nested generic type
        Assert.assertEquals(ExpectedNestedGenericType.class, nestedGenericType);
    }
}

