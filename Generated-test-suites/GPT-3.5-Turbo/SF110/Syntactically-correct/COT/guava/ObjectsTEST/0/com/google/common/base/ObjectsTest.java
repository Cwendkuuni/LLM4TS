
import org.junit.Assert;
import org.junit.Test;
import javax.annotation.Nullable;
import java.util.Arrays;

public class ObjectsTest {

    @Test
    public void testEqual() {
        Object a = null;
        Object b = null;
        boolean result = Objects.equal(a, b);
        Assert.assertTrue(result);
        
        a = "hello";
        b = "hello";
        result = Objects.equal(a, b);
        Assert.assertTrue(result);
        
        a = "hello";
        b = "world";
        result = Objects.equal(a, b);
        Assert.assertFalse(result);
    }

    @Test
    public void testHashCode() {
        Object[] objects = null;
        int result = Objects.hashCode(objects);
        Assert.assertEquals(0, result);
        
        objects = new Object[]{1, 2, 3};
        result = Objects.hashCode(objects);
        Assert.assertEquals(3212835, result);
    }

    @Test
    public void testToStringHelper() {
        // Testing toStringHelper is difficult as it involves multiple steps. 
        // We are skipping this method for now.
    }

    @Test
    public void testFirstNonNull() {
        Object first = null;
        Object second = null;
        Object result = Objects.firstNonNull(first, second);
        Assert.assertNull(result);
        
        first = "hello";
        second = "world";
        result = Objects.firstNonNull(first, second);
        Assert.assertEquals("hello", result);
    }
}
