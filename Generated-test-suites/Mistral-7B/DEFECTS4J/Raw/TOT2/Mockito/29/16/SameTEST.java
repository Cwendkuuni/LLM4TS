import org.junit.Assert;
import org.mockito.internal.matchers.Same;

public class SameTest {
    @org.junit.Test
    public void testMatchesSameObject() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        Object actual = wanted;
        boolean result = same.matches(actual);
        Assert.assertTrue(result);
    }
}