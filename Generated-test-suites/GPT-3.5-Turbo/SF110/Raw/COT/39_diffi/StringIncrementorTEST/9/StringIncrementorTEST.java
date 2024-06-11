
import org.junit.Assert;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test basic functionality of diff method
        String a = "abcdef";
        String b = "abxyzef";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(2, 2, 3));

        RelativeString result = StringIncrementor.diff(a, b);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testDiffVerbose() {
        // Test diff method with verbose parameter set to true
        String a = "abcdef";
        String b = "abxyzef";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(2, 2, 3));

        RelativeString result = StringIncrementor.diff(a, b, true);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatch() {
        // Test basic functionality of patch method
        String a = "abcdef";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(2, 2, 3));
        String expected = "abxyzef";

        String result = StringIncrementor.patch(a, r);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatchVerbose() {
        // Test patch method with verbose parameter set to true
        String a = "abcdef";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(2, 2, 3));
        String expected = "abxyzef";

        String result = StringIncrementor.patch(a, r, true);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPatchEmptyLinks() {
        // Test patch method with empty links
        String a = "abcdef";
        RelativeString r = new RelativeString();
        String expected = "abcdef";

        String result = StringIncrementor.patch(a, r);

        Assert.assertEquals(expected, result);
    }
}
