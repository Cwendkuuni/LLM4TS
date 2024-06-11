import org.junit.Test;

public class PatternBuilderTest {

    // Test create method with single option
    @Test
    public void createWithSingleOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a");
        Option option = patternBuilder.create();
        // Assert that the created option has the correct short name and is required
        assertEquals("-a", option.getShortName());
        assertTrue(option.isRequired());
    }

    // Test create method with multiple options
    @Test
    public void createWithMultipleOptions() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a! -b");
        Option option = patternBuilder.create();
        // Assert that the created option contains the correct options
        assertTrue(option instanceof Group);
        Group group = (Group) option;
        assertEquals("-a", group.getOptions().get(0).getShortName());
        assertTrue(group.getOptions().get(0).isRequired());
        assertEquals("-b", group.getOptions().get(1).getShortName());
        assertFalse(group.getOptions().get(1).isRequired());
    }

    // Test reset method
    @Test
    public void reset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a");
        patternBuilder.reset();
        Option option = patternBuilder.create();
        // Assert that after reset, no option is created
        assertNull(option);
    }

    // Test withPattern method with single option
    @Test
    public void withPatternWithSingleOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a");
        Option option = patternBuilder.create();
        // Assert that the created option has the correct short name and is required
        assertEquals("-a", option.getShortName());
        assertTrue(option.isRequired());
    }

    // Test withPattern method with multiple options
    @Test
    public void withPatternWithMultipleOptions() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("-a! -b");
        Option option = patternBuilder.create();
        // Assert that the created option contains the correct options
        assertTrue(option instanceof Group);
        Group group = (Group) option;
        assertEquals("-a", group.getOptions().get(0).getShortName());
        assertTrue(group.getOptions().get(0).isRequired());
        assertEquals("-b", group.getOptions().get(1).getShortName());
        assertFalse(group.getOptions().get(1).isRequired());
    }
}