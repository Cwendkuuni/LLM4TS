import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;

import java.util.Date;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder(new GroupBuilder(), new OptionBuilder(), new ArgumentBuilder());
    }

    @Test
    public void testCreateWithSingleOption() {
        patternBuilder.withPattern("!o");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertTrue(option.getOptions().contains("o"));
        assertTrue(option.isRequired());
        assertEquals(0, option.getArguments().size());
    }

    @Test
    public void testCreateWithMultipleOptions() {
        patternBuilder.withPattern("abc");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(3, option.getOptions().size());
        assertTrue(option.getOptions().contains("a"));
        assertTrue(option.getOptions().contains("b"));
        assertTrue(option.getOptions().contains("c"));
        assertFalse(option.isRequired());
        assertEquals(0, option.getArguments().size());
    }

    @Test
    public void testCreateWithOptionAndArgument() {
        patternBuilder.withPattern("o#");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertTrue(option.getOptions().contains("o"));
        assertFalse(option.isRequired());
        assertEquals(1, option.getArguments().size());
        
        Argument argument = option.getArguments().get(0);
        assertTrue(argument.hasValidator(DateValidator.class));
    }

    @Test
    public void testCreateWithMultipleOptionsAndArgument() {
        patternBuilder.withPattern("abc@");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(3, option.getOptions().size());
        assertTrue(option.getOptions().contains("a"));
        assertTrue(option.getOptions().contains("b"));
        assertTrue(option.getOptions().contains("c"));
        assertFalse(option.isRequired());
        assertEquals(1, option.getArguments().size());
        
        Argument argument = option.getArguments().get(0);
        assertTrue(argument.hasValidator(ClassValidator.class));
        assertTrue(argument.hasInstanceValidator());
    }

    @Test
    public void testCreateWithComplexPattern() {
        patternBuilder.withPattern("!o/a");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(1, option.getOptions().size());
        assertTrue(option.getOptions().contains("o"));
        assertTrue(option.isRequired());
        assertEquals(1, option.getArguments().size());
        
        Argument argument = option.getArguments().get(0);
        assertTrue(argument.hasValidator(FileValidator.class));
    }

    @Test
    public void testReset() {
        patternBuilder.withPattern("o#");
        patternBuilder.reset();
        
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertEquals(0, option.getOptions().size());
        assertFalse(option.isRequired());
        assertEquals(0, option.getArguments().size());
    }

}