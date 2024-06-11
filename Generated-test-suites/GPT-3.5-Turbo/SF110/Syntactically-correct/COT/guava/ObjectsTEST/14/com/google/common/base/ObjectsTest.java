
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nullable;
import java.util.Arrays;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public class ObjectsTest {

    // Test equal method
    @Test
    public void testEqual() {
        // Test when both objects are null
        Assert.assertTrue(Objects.equal(null, null));

        // Test when one object is null and the other is not
        Assert.assertFalse(Objects.equal("hello", null));
        Assert.assertFalse(Objects.equal(null, "hello"));

        // Test when both objects are not null but equal
        Assert.assertTrue(Objects.equal("hello", "hello"));

        // Test when both objects are not null and not equal
        Assert.assertFalse(Objects.equal("hello", "world"));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        // Test when the object array is null
        Assert.assertEquals(0, Objects.hashCode(null));

        // Test when the object array is empty
        Assert.assertEquals(1, Objects.hashCode());

        // Test when the object array contains multiple elements
        Assert.assertEquals(Objects.hashCode("hello", "world"), Objects.hashCode("hello", "world"));
    }

    // Test toStringHelper method
    @Test
    public void testToStringHelper() {
        // Test toStringHelper with an object
        ToStringHelper helper1 = Objects.toStringHelper(new Object());
        Assert.assertNotNull(helper1);
        
        // Test toStringHelper with a class
        ToStringHelper helper2 = Objects.toStringHelper(String.class);
        Assert.assertNotNull(helper2);
        
        // Test toStringHelper with a class name
        ToStringHelper helper3 = Objects.toStringHelper("className");
        Assert.assertNotNull(helper3);
    }

    // Test firstNonNull method
    @Test
    public void testFirstNonNull() {
        // Test when both objects are null
        Assert.assertNull(Objects.firstNonNull(null, null));

        // Test when one object is null and the other is not
        Assert.assertEquals("hello", Objects.firstNonNull("hello", null));
        Assert.assertEquals("hello", Objects.firstNonNull(null, "hello"));

        // Test when both objects are not null
        Assert.assertEquals("hello", Objects.firstNonNull("hello", "world"));
    }

    // Test toString method of ToStringHelper class
    @Test
    public void testToString() {
        ToStringHelper helper = Objects.toStringHelper("className");
        helper.add("name", "John");
        helper.addValue(25);
        String expected = "className{name=John, 25}";
        Assert.assertEquals(expected, helper.toString());
    }
}
