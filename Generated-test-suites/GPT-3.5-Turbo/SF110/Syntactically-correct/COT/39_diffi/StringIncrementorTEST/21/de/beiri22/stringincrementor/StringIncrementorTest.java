import org.junit.Assert;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        // Test basic functionality
        RelativeString result = StringIncrementor.diff("abcde", "abzde");
        Assert.assertEquals(1, result.linksCount());
    
        // Test case sensitivity
        result = StringIncrementor.diff("ABCDE", "abcde");
        Assert.assertEquals(0, result.linksCount());
    }
    
    @Test
    public void testDiffVerbose() {
        // Test basic functionality with verbose output
        RelativeString result = StringIncrementor.diff("abcde", "abzde", true);
        Assert.assertEquals(1, result.linksCount());
    }
    
    @Test
    public void testPatch() {
        // Test basic functionality
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 5));
        String result = StringIncrementor.patch("abzde", relativeString);
        Assert.assertEquals("abcde", result);
    }
    
    @Test
    public void testPatchVerbose() {
        // Test basic functionality with verbose output
        RelativeString relativeString = new RelativeString();
        relativeString.addLink(new StringLink(0, 0, 5));
        String result = StringIncrementor.patch("abzde", relativeString, true);
        Assert.assertEquals("abcde", result);
    }
}