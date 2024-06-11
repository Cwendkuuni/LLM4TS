
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

public class PatternBuilderTest {
    
    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        Option option = patternBuilder.create();
        
        Assert.assertNull(option);
    }
    
    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.reset();
        
        Option option = patternBuilder.create();
        
        Assert.assertNull(option);
    }
    
    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("!a");
        
        Option option = patternBuilder.create();
        
        Assert.assertNotNull(option);
        Assert.assertEquals("a", option.getShortName());
        Assert.assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithPatternMultipleRequired() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("!ab");
        
        Option option = patternBuilder.create();
        
        Assert.assertNotNull(option);
        Assert.assertEquals("ab", option.getShortName());
        Assert.assertTrue(option.isRequired());
    }
    
    @Test
    public void testWithPatternMultipleNotRequired() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("ab");
        
        Option option = patternBuilder.create();
        
        Assert.assertNotNull(option);
        Assert.assertEquals("ab", option.getShortName());
        Assert.assertFalse(option.isRequired());
    }
    
    @Test
    public void testWithPatternWithArgument() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern(":a");
        
        Option option = patternBuilder.create();
        
        Assert.assertNotNull(option);
        Assert.assertEquals("a", option.getShortName());
        Assert.assertNotNull(option.getArgument());
    }
    
    @Test
    public void testWithPatternWithArgumentMultiple() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern(":a*");
        
        Option option = patternBuilder.create();
        
        Assert.assertNotNull(option);
        Assert.assertEquals("a", option.getShortName());
        Assert.assertNotNull(option.getArgument());
        Assert.assertEquals(0, option.getArgument().getMinimum());
        Assert.assertEquals(Integer.MAX_VALUE, option.getArgument().getMaximum());
    }
    
    @Test
    public void testWithPatternWithArgumentInteger() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("%a");
        
        Option option = patternBuilder.create();
        
        Assert.assertNotNull(option);
        Assert.assertEquals("a", option.getShortName());
        Assert.assertNotNull(option.getArgument());
        Assert.assertTrue(option.getArgument().getValidator() instanceof NumberValidator);
    }
    
    @Test
    public void testWithPatternWithArgumentDate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("#a");
        
        Option option = patternBuilder.create();
        
        Assert.assertNotNull(option);
        Assert.assertEquals("a", option.getShortName());
        Assert.assertNotNull(option.getArgument());
        Assert.assertTrue(option.getArgument().getValidator() instanceof DateValidator);
    }
    
    @Test
    public void testWithPatternWithArgumentExistingFile() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("<a");
        
        Option option = patternBuilder.create();
        
        Assert.assertNotNull(option);
        Assert.assertEquals("a", option.getShortName());
        Assert.assertNotNull(option.getArgument());
        Assert.assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        Assert.assertTrue(option.getArgument().getValidator().isExisting());
        Assert.assertTrue(option.getArgument().getValidator().isFile());
    }
    
    @Test
    public void testWithPatternWithArgumentFile() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern(">*");
        
        Option option = patternBuilder.create();
        
        Assert.assertNotNull(option);
        Assert.assertEquals("*", option.getShortName());
        Assert.assertNotNull(option.getArgument());
        Assert.assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        Assert.assertFalse(option.getArgument().getValidator().isExisting());
        Assert.assertTrue(option.getArgument().getValidator().isFile());
    }
    
    @Test
    public void testWithPatternWithArgumentUrl() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("/");
        
        Option option = patternBuilder.create();
        
        Assert.assertNotNull(option);
        Assert.assertEquals("/", option.getShortName());
        Assert.assertNotNull(option.getArgument());
        Assert.assertTrue(option.getArgument().getValidator() instanceof UrlValidator);
    }
    
}
