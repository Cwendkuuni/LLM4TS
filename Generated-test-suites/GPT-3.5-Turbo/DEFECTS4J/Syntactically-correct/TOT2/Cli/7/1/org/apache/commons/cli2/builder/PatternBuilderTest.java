import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.builder.*;

public class PatternBuilderTest {

    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        // Test with single option
        patternBuilder.withPattern("-a");
        Option option1 = patternBuilder.create();
        assertNotNull(option1);
        
        // Test with multiple options in a group
        patternBuilder.withPattern("-a-b");
        patternBuilder.withPattern("-c");
        Option option2 = patternBuilder.create();
        assertNotNull(option2);
        
        // Test with mandatory and multiple options
        patternBuilder.withPattern("-a!-b!-c");
        Option option3 = patternBuilder.create();
        assertNotNull(option3);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("-a");
        patternBuilder.reset();
        assertEquals(0, patternBuilder.options.size());
    }

    @Test
    public void testCreateOption() {
        PatternBuilder patternBuilder = new PatternBuilder();

        char type = '#'; // Date type
        boolean required = true;
        char opt = 'd';
        patternBuilder.createOption(type, required, opt);
        Option option = patternBuilder.obuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();

        patternBuilder.withPattern("-a-b-c");
        Option option = patternBuilder.create();
        assertNotNull(option);
        
        patternBuilder.reset();
        patternBuilder.withPattern("-d-!-e");
        Option option2 = patternBuilder.create();
        assertNotNull(option2);
    }

    @Test
    public void testValidator() {
        assertEquals(ClassValidator.class, PatternBuilder.validator('@').getClass());
        assertEquals(ClassValidator.class, PatternBuilder.validator('+').getClass());
        assertEquals(NumberValidator.class, PatternBuilder.validator('%').getClass());
        assertEquals(DateValidator.class, PatternBuilder.validator('#').getClass());
        assertEquals(FileValidator.class, PatternBuilder.validator('<').getClass());
        assertEquals(FileValidator.class, PatternBuilder.validator('>').getClass());
        assertEquals(FileValidator.class, PatternBuilder.validator('*').getClass());
        assertEquals(UrlValidator.class, PatternBuilder.validator('/').getClass());
    }
}