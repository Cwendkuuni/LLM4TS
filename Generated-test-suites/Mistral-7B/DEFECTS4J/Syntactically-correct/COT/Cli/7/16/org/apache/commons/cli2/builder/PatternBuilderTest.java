package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
    }

    @Test
    public void testCreateOptionWithValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%d");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        assertTrue(option.getValidator() instanceof NumberValidator);
    }

    @Test(expected = AssertionError.class)
    public void testCreateOptionWithInvalidCharacter() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("!@#%^&*");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        // The test fails with an AssertionError because the option creation is successful,
        // but the pattern contains an invalid character, so we expect an exception
    }

    @Test
    public void testCreateOptionWithClassValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("@java.lang.String");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        assertTrue(option.getValidator() instanceof ClassValidator);
    }

    @Test
    public void testCreateOptionWithDateValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("#yyyy-MM-dd");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        assertTrue(option.getValidator() instanceof DateValidator);
    }

    @Test
    public void testCreateOptionWithFileValidatorExisting() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("<path/to/existing/file>");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        assertTrue(option.getValidator() instanceof FileValidator);
        assertTrue(((FileValidator) option.getValidator()).isExisting());
    }

    @Test
    public void testCreateOptionWithFileValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern(">path/to/file");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        assertTrue(option.getValidator() instanceof FileValidator);
        assertFalse(((FileValidator) option.getValidator()).isExisting());
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("/http://example.com");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option instanceof Option);
        assertTrue(option.getValidator() instanceof UrlValidator);
    }

    @Test
    public void testCreateMultipleOptions() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("d:i:@java.lang.String:s:@java.util.List:f:");
        Option option1 = pb.create();
        Option option2 = pb.create();
        Option option3 = pb.create();
        Option option4 = pb.create();
        Option option5 = pb.create();

        assertNotNull(option1);
        assertTrue(option1 instanceof Option);
        assertTrue(option1.getValidator() instanceof NumberValidator);

        assertNotNull(option2);
        assertTrue(option2 instanceof Option);
        assertTrue(option2.getValidator() instanceof ClassValidator);

        assertNotNull(option3);
        assertTrue(option3 instanceof Option);
        assertTrue(option3.getValidator() instanceof ClassValidator);

        assertNotNull(option4);
        assertTrue(option4 instanceof Option);
        assertTrue(option4.getValidator() instanceof ClassValidator);

        assertNotNull(option5);
        assertTrue(option5 instanceof Option);
        assertTrue(option5.getValidator() instanceof FileValidator);
    }
}