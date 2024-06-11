package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

public class PatternBuilderTest {
    @Test
    public void testPatternBuilderCreation() {
        PatternBuilder pb = new PatternBuilder();
        assertNotNull(pb);
    }

    @Test
    public void testCreateOptionWithNoArguments() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:");
        Option option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(0, option.getArgs().size());
    }

    @Test
    public void testCreateOptionWithOneMandatoryArgument() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:s");
        Option option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs().size());
    }

    @Test
    public void testCreateOptionWithOneOptionalArgument() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:s");
        pb.reset();
        pb.withPattern("a:s{1,}");
        Option option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(0, option.getArgs().size());

        pb.reset();
        pb.withPattern("a:s");
        pb.reset();
        pb.withPattern("a:s{1,}");
        option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs().size());
    }

    @Test
    public void testCreateOptionWithMultipleMandatoryArguments() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:ss");
        Option option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(2, option.getArgs().size());
    }

    @Test
    public void testCreateOptionWithMultipleOptionalArguments() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:ss");
        pb.reset();
        pb.withPattern("a:ss{1,}");
        Option option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(0, option.getArgs().size());

        pb.reset();
        pb.withPattern("a:ss");
        pb.reset();
        pb.withPattern("a:ss{1,}");
        option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(2, option.getArgs().size());

        pb.reset();
        pb.withPattern("a:ss");
        pb.reset();
        pb.withPattern("a:ss{1,}");
        option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(0, option.getArgs().size());
    }

    @Test
    public void testCreateOptionWithMandatoryAndOptionalArguments() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:s{1}s");
        Option option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs().size());

        pb.reset();
        pb.withPattern("a:s{1,}s");
        option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs().size());

        pb.reset();
        pb.withPattern("a:s{1}s");
        option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs().size());

        pb.reset();
        pb.withPattern("a:s{1,}s");
        option = pb.create();
        assertTrue(option.getShortOpt().equals("a"));
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals(2, option.getArgs().size());
    }

    @Test
    public void testCreateOptionWithValidators() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:@Foo");
        Option option = pb.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof ClassValidator);
    }

    @Test
    public void testCreateOptionWithDateValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:#yyyy-MM-dd");
        Option option = pb.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof DateValidator);
    }

    @Test
    public void testCreateOptionWithFileValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:<.");
        Option option = pb.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof FileValidator);
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:/.");
        Option option = pb.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof UrlValidator);
    }

    @Test
    public void testCreateOptionWithNumberValidator() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:%d");
        Option option = pb.create();
        assertNotNull(option.getValidator());
        assertTrue(option.getValidator() instanceof NumberValidator);
    }
}