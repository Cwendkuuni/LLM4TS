
import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
    
    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreateWithSingleOption() {
        patternBuilder.withPattern("a");
        
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
    }
    
    @Test
    public void testCreateWithMultipleOptions() {
        patternBuilder.withPattern("a|b");
        
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(2, option.getOptions().size());
    }
    
    @Test
    public void testReset() {
        patternBuilder.withPattern("a");
        patternBuilder.reset();
        
        Option option = patternBuilder.create();
        
        assertNull(option);
    }
    
    @Test
    public void testCreateWithRequiredOption() {
        patternBuilder.withPattern("!a");
        
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testCreateWithMultipleRequiredOptions() {
        patternBuilder.withPattern("!a|!b");
        
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertTrue(option.isRequired());
        assertEquals(2, option.getOptions().size());
    }
    
    @Test
    public void testCreateWithArgument() {
        patternBuilder.withPattern("a:@");
        
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getArgument());
    }
    
    @Test
    public void testCreateWithMultipleArguments() {
        patternBuilder.withPattern("a:@|b:@");
        
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(2, option.getOptions().size());
        assertNotNull(option.getArgument());
    }
    
    @Test
    public void testCreateWithMultipleArgumentsAndRequiredOption() {
        patternBuilder.withPattern("!a:@|!b:@");
        
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertTrue(option.isRequired());
        assertEquals(2, option.getOptions().size());
        assertNotNull(option.getArgument());
    }
    
    @Test
    public void testCreateWithInstanceValidator() {
        patternBuilder.withPattern("a:@");
        
        Option option = patternBuilder.create();
        
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        assertTrue(((ClassValidator) option.getArgument().getValidator()).isInstance());
    }
    
    @Test
    public void testCreateWithClassValidator() {
        patternBuilder.withPattern("a:+");
        
        Option option = patternBuilder.create();
        
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        assertFalse(((ClassValidator) option.getArgument().getValidator()).isInstance());
    }
    
    @Test
    public void testCreateWithStringValidator() {
        patternBuilder.withPattern("a:");
        
        Option option = patternBuilder.create();
        
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getArgument());
        assertNull(option.getArgument().getValidator());
    }
    
    @Test
    public void testCreateWithNumberValidator() {
        patternBuilder.withPattern("a:%");
        
        Option option = patternBuilder.create();
        
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof NumberValidator);
    }
    
    @Test
    public void testCreateWithDateValidator() {
        patternBuilder.withPattern("a:#");
        
        Option option = patternBuilder.create();
        
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof DateValidator);
    }
    
    @Test
    public void testCreateWithExistingFileValidator() {
        patternBuilder.withPattern("a:<");
        
        Option option = patternBuilder.create();
        
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertTrue(((FileValidator) option.getArgument().getValidator()).isExisting());
        assertTrue(((FileValidator) option.getArgument().getValidator()).isFile());
    }
    
    @Test
    public void testCreateWithFileValidator() {
        patternBuilder.withPattern("a:>");
        
        Option option = patternBuilder.create();
        
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertFalse(((FileValidator) option.getArgument().getValidator()).isExisting());
        assertTrue(((FileValidator) option.getArgument().getValidator()).isFile());
    }
    
    @Test
    public void testCreateWithUrlValidator() {
        patternBuilder.withPattern("a:/");
        
        Option option = patternBuilder.create();
        
        assertEquals(1, option.getOptions().size());
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument().getValidator() instanceof UrlValidator);
    }

}

