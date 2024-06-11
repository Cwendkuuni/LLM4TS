import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilderCreation() {
        PatternBuilder patternBuilder = new PatternBuilder();
        assertNotNull(patternBuilder);
    }

    @Test
    public void testCreateOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
        assertFalse(option.isHidden());
        assertNull(option.getArgs());

        patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:b:");
        option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
        assertFalse(option.isHidden());
        assertEquals(1, option.getArgs().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateMultipleOptionsWithSameShortName() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:");
        patternBuilder.create();
        patternBuilder.withPattern("a:");
    }

    @Test
    public void testCreateMultipleOptionsWithDifferentShortNames() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:");
        Option optionA = patternBuilder.create();
        patternBuilder.withPattern("b:");
        Option optionB = patternBuilder.create();
        assertEquals(2, patternBuilder.options.size());
        assertNotNull(optionA);
        assertNotNull(optionB);
        assertFalse(optionA.equals(optionB));
    }

    @Test
    public void testCreateWithLongName() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("longName:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isHidden());
        assertTrue(option.getLongOpt().isPresent());
        assertEquals("longName", option.getLongOpt().get());
        assertNull(option.getShortName());
    }

    @Test
    public void testCreateWithFlagOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("!");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isHidden());
        assertNull(option.getLongOpt());
        assertFalse(option.getValue().isPresent());
        assertNull(option.getArgs());
        assertTrue(option.isOptional());
    }

    @Test
    public void testCreateWithValueOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%d:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isHidden());
        assertNull(option.getLongOpt());
        assertFalse(option.getValue().isPresent());
        assertNotNull(option.getArgs());
        assertEquals(1, option.getArgs().size());
        assertTrue(option.getValue().isPresent());
    }

    @Test
    public void testCreateWithClassValidation() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("@com.example.MyClass:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isHidden());
        assertNotNull(option.getClassValidator());
        assertTrue(option.getClassValidator() instanceof ClassValidator);
    }

    @Test
    public void testCreateWithNumberValidation() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%d:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isHidden());
        assertNotNull(option.getNumberValidator());
        assertTrue(option.getNumberValidator() instanceof NumberValidator);
    }

    @Test
    public void testCreateWithDateValidation() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("#yyyy-MM-dd:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isHidden());
        assertNotNull(option.getDateValidator());
        assertTrue(option.getDateValidator() instanceof DateValidator);
    }

    @Test
    public void testCreateWithFileValidation() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("<file:>");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isHidden());
        assertNotNull(option.getFileValidator());
        assertTrue(option.getFileValidator() instanceof FileValidator);
    }

    @Test
    public void testCreateWithUrlValidation() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("/url:>");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isHidden());
        assertNotNull(option.getUrlValidator());
        assertTrue(option.getUrlValidator() instanceof UrlValidator);
    }

    @Test
    public void testCreateWithMultipleTypes() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%d:<file:>:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isHidden());
        assertEquals(2, option.getArgs().size());
        assertTrue(option.getValue().isPresent());
        assertTrue(option.getFileValidator() instanceof FileValidator);
    }

    @Test
    public void testCreateWithMinMaxArguments() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%s{2}:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isHidden());
        assertEquals(2, option.getMinimum());
        assertEquals(2, option.getMaximum());
        assertEquals(2, option.getArgs().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:b:");
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:");
        patternBuilder.create();
        Option option = patternBuilder.create();
        assertEquals(2, patternBuilder.options.size());
        patternBuilder.reset();
        assertEquals(0, patternBuilder.options.size());
        assertNull(option);
    }

    @Test
    public void testCreateGroup() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("g:");
        Option groupOption = patternBuilder.create();
        assertNotNull(groupOption);
        assertTrue(groupOption.isHidden());
        assertEquals(0, groupOption.getArgs().size());
        GroupBuilder groupBuilder = groupOption.getGroupBuilder();
        Group optionGroup = groupBuilder.create();
        assertEquals(1, groupOption.getGroup().size());
        assertEquals(1, optionGroup.getOptions().size());
        Option groupOption2 = patternBuilder.withPattern("g:g:").create();
        assertEquals(2, groupOption2.getGroup().size());
    }

}