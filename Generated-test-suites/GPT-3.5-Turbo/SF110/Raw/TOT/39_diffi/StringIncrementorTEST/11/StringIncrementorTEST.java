import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StringIncrementorTest {

    private StringIncrementor stringIncrementor;

    @Before
    public void setUp() {
        stringIncrementor = new StringIncrementor();
    }

    @After
    public void tearDown() {
        stringIncrementor = null;
    }

    @Test
    public void testDiffWithBothParametersSame() {
        String a = "hello";
        String b = "hello";
        RelativeString diff = stringIncrementor.diff(a, b);
        Assert.assertEquals("The diff should be empty when both parameters are same strings", 0, diff.linksCount());
    }

    @Test
    public void testDiffWithNoCommonSubstrings() {
        String a = "abcde";
        String b = "fghij";
        RelativeString diff = stringIncrementor.diff(a, b);
        Assert.assertEquals("The diff should be empty when there are no common substrings", 0, diff.linksCount());
    }

    @Test
    public void testDiffWithCommonSubstrings() {
        String a = "abcxyxyz";
        String b = "xyz123xyz";
        RelativeString diff = stringIncrementor.diff(a, b);
        Assert.assertEquals("The diff should contain 2 links when there are 2 common substrings", 2, diff.linksCount());
    }

    @Test
    public void testPatchWithEmptyDiff() {
        String a = "abcde";
        RelativeString r = new RelativeString();
        String patchedString = stringIncrementor.patch(a, r);
        Assert.assertEquals("The patched string should be same as input when diff is empty", a, patchedString);
    }

    @Test
    public void testPatchWithDiff() {
        String a = "abcxyxyz";
        RelativeString r = new RelativeString();
        StringLink link1 = new StringLink(0, 0, 3);
        StringLink link2 = new StringLink(4, 3, 3);
        r.addLink(link1);
        r.addLink(link2);
        String patchedString = stringIncrementor.patch(a, r);
        String expectedPatchedString = "xyz123xyz";
        Assert.assertEquals("The patched string should be expected patched string when diff is provided", expectedPatchedString, patchedString);
    }
}