import org.junit.Assert;
import org.junit.Test;

public class SameTest {

    @Test
    public void testMatches() {
        Same same = new Same("test");
        Assert.assertTrue(same.matches("test"));
        Assert.assertFalse(same.matches("other"));
        Assert.assertFalse(same.matches(null));
    }

    @Test
    public void testDescribeTo() {
        Same same = new Same("test");
        Description description = new Description();
        same.describeTo(description);
        Assert.assertEquals("same(\"test\")", description.toString());
    }
}