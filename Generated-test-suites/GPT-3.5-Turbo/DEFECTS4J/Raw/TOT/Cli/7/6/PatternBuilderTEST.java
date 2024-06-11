import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {

    private PatternBuilder builder;

    @Before
    public void setup() {
        builder = new PatternBuilder();
    }

    @Test
    public void create_NoOptions_ReturnNull() {
        assertNull(builder.create());
    }

    @Test
    public void reset_AfterCreate_ReturnNull() {
        builder.create();
        builder.reset();
        assertNull(builder.create());
    }

    @Test
    public void withPattern_ValidPattern_CreatesOption() {
        builder.withPattern("!@:ABC");
        assertNotNull(builder.create());
    }

    @Test
    public void withPattern_ValidPattern_CreatesOptionWithCorrectArgument() {
        builder.withPattern("!@:ABC");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument() instanceof Argument);
        Argument argument = (Argument) option.getArgument();
        assertTrue(argument.getValidator() instanceof ClassValidator);
        assertTrue(argument.isMinimumSet());
        assertEquals(1, argument.getMinimum());
        assertTrue(argument.isMaximumSet());
        assertEquals(1, argument.getMaximum());
    }

    @Test
    public void withPattern_InvalidPattern_ReturnsNull() {
        builder.withPattern("!@:ABC**");
        assertNull(builder.create());
    }

    @Test
    public void withPattern_ValidPatternMultipleOptions_CreatesGroupBuilder() {
        builder.withPattern("@:ABC:*");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument() instanceof Argument);
        Argument argument = (Argument) option.getArgument();
        assertTrue(argument.getValidator() instanceof ClassValidator);
        assertTrue(argument.isMinimumSet());
        assertEquals(1, argument.getMinimum());
        assertFalse(argument.isMaximumSet());

        assertNotNull(option.getArgument());
        assertTrue(option.getArgument() instanceof Argument);
        Argument argument2 = (Argument) option.getArgument();
        assertTrue(argument2.getValidator() instanceof ClassValidator);
        assertTrue(argument2.isMinimumSet());
        assertEquals(1, argument2.getMinimum());
        assertFalse(argument2.isMaximumSet());
    }

    @Test
    public void withPattern_ValidPatternMultipleOptions_CreatesGroupBuilderWithCorrectOptions() {
        builder.withPattern("@:ABC:*");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument() instanceof Argument);
        Argument argument = (Argument) option.getArgument();
        assertTrue(argument.getValidator() instanceof ClassValidator);
        assertTrue(argument.isMinimumSet());
        assertEquals(1, argument.getMinimum());
        assertFalse(argument.isMaximumSet());

        assertNotNull(option.getArgument());
        assertTrue(option.getArgument() instanceof Argument);
        Argument argument2 = (Argument) option.getArgument();
        assertTrue(argument2.getValidator() instanceof ClassValidator);
        assertTrue(argument2.isMinimumSet());
        assertEquals(1, argument2.getMinimum());
        assertFalse(argument2.isMaximumSet());
    }

    @Test
    public void withPattern_ValidPatternWithoutArgument_CreatesOptionWithNullArgument() {
        builder.withPattern("@");
        Option option = builder.create();
        assertNull(option.getArgument());
    }

    @Test
    public void withPattern_ValidPatternWithRequiredArgument_CreatesOptionWithRequiredArgument() {
        builder.withPattern("!@");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument() instanceof Argument);
        Argument argument = (Argument) option.getArgument();
        assertTrue(argument.getValidator() instanceof ClassValidator);
        assertTrue(argument.isMinimumSet());
        assertEquals(1, argument.getMinimum());
        assertFalse(argument.isMaximumSet());
    }

    @Test
    public void withPattern_ValidPatternWithMultipleRequiredArguments_CreatesOptionWithCorrectArguments() {
        builder.withPattern("!@:ABC!:");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertTrue(option.getArgument() instanceof Argument);
        Argument argument = (Argument) option.getArgument();
        assertTrue(argument.getValidator() instanceof ClassValidator);
        assertTrue(argument.isMinimumSet());
        assertEquals(1, argument.getMinimum());
        assertTrue(argument.isMaximumSet());
        assertEquals(1, argument.getMaximum());

        assertNotNull(option.getArgument());
        assertTrue(option.getArgument() instanceof Argument);
        Argument argument2 = (Argument) option.getArgument();
        assertTrue(argument2.getValidator() instanceof ClassValidator);
        assertTrue(argument2.isMinimumSet());
        assertEquals(1, argument2.getMinimum());
        assertTrue(argument2.isMaximumSet());
        assertEquals(1, argument2.getMaximum());
    }

    @Test
    public void validator_NoType_ReturnNullValidator() {
        assertNull(PatternBuilder.validator('a'));
    }

    @Test
    public void validator_Class_ReturnClassValidator() {
        assertTrue(PatternBuilder.validator('@') instanceof ClassValidator);
    }

    @Test
    public void validator_Number_ReturnNumberValidator() {
        assertTrue(PatternBuilder.validator('%') instanceof NumberValidator);
    }

    @Test
    public void validator_Date_ReturnDateValidator() {
        assertTrue(PatternBuilder.validator('#') instanceof DateValidator);
    }

    @Test
    public void validator_FileExisting_ReturnFileValidatorWithExistingTrue() {
        Validator validator = PatternBuilder.validator('<');
        assertTrue(validator instanceof FileValidator);
        FileValidator fileValidator = (FileValidator) validator;
        assertTrue(fileValidator.isExisting());
        assertTrue(fileValidator.isFile());
    }

    @Test
    public void validator_File_ReturnFileValidator() {
        assertTrue(PatternBuilder.validator('*') instanceof FileValidator);
    }

    @Test
    public void validator_Url_ReturnUrlValidator() {
        assertTrue(PatternBuilder.validator('/') instanceof UrlValidator);
    }

}