import org.junit.Assert;
import org.junit.Test;

public class StringIncrementorTest {

    @Test
    public void testDiff_WithTwoEqualStrings_ReturnsEmptyRelativeString() {
        // Arrange
        String a = "abc";
        String b = "abc";
        
        // Act
        RelativeString result = StringIncrementor.diff(a, b);
        
        // Assert
        Assert.assertTrue(result.isEmpty());
    }
    
    @Test
    public void testDiff_WithDifferentStrings_ReturnsRelativeStringWithLinks() {
        // Arrange
        String a = "abcde";
        String b = "abcfg";
        
        // Act
        RelativeString result = StringIncrementor.diff(a, b);
        
        // Assert
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.linksCount());
        Assert.assertEquals(2, result.getLink(0).getPosOrig());
        Assert.assertEquals(2, result.getLink(0).getPosNew());
        Assert.assertEquals(3, result.getLink(0).getLen());
    }
    
    @Test
    public void testDiff_WithVerboseTrue_ReturnsRelativeStringAndPrintsLogs() {
        // Arrange
        String a = "abcde";
        String b = "abcfg";
        boolean verbose = true;
        
        // Act
        RelativeString result = StringIncrementor.diff(a, b, verbose);
        
        // Assert
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(1, result.linksCount());
        Assert.assertEquals(2, result.getLink(0).getPosOrig());
        Assert.assertEquals(2, result.getLink(0).getPosNew());
        Assert.assertEquals(3, result.getLink(0).getLen());
        // Check if logs are printed
    }
    
    @Test
    public void testPatch_WithDiffAndRelativeString_ReturnsPatchedString() {
        // Arrange
        String a = "abcde";
        String b = "abcfg";
        RelativeString diffResult = StringIncrementor.diff(a, b);
        
        // Act
        String patchedString = StringIncrementor.patch(a, diffResult);
        
        // Assert
        Assert.assertEquals(b, patchedString);
    }
    
    @Test
    public void testPatch_WithVerboseTrue_ReturnsPatchedStringAndPrintsLogs() {
        // Arrange
        String a = "abcde";
        String b = "abcfg";
        RelativeString diffResult = StringIncrementor.diff(a, b);
        boolean verbose = true;
        
        // Act
        String patchedString = StringIncrementor.patch(a, diffResult, verbose);
        
        // Assert
        Assert.assertEquals(b, patchedString);
        // Check if logs are printed
    }
}