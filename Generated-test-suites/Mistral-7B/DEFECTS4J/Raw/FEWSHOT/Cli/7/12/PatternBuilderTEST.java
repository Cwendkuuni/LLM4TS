package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testWithPattern_noOptions() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("");
        assertTrue(builder.options.isEmpty());
    }

    @Test
    public void testWithPattern_singleOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:");
        assertTrue(builder.options.size() == 1);
        final Option option = (Option) builder.options.iterator().next();
        assertEquals("a", option.getShortName());
        assertNull(option.getLongName());
        assertNull(option.getArgName());
        assertNull(option.getValidator());
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_requiredOption() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:!");
        assertTrue(builder.options.size() == 1);
        final Option option = (Option) builder.options.iterator().next();
        assertEquals("a", option.getShortName());
        assertNull(option.getLongName());
        assertNull(option.getArgName());
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithPattern_optionWithArg() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s");
        assertTrue(builder.options.size() == 1);
        final Option option = (Option) builder.options.iterator().next();
        assertEquals("a", option.getShortName());
        assertNull(option.getLongName());
        assertEquals("s", option.getArgName());
        assertNull(option.getValidator());
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_optionWithArgAndValidator() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s<file>");
        assertTrue(builder.options.size() == 1);
        final Option option = (Option) builder.options.iterator().next();
        assertEquals("a", option.getShortName());
        assertNull(option.getLongName());
        assertEquals("s", option.getArgName());
        assertTrue(option.getValidator() instanceof FileValidator);
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_optionWithArgAndMultipleValidators() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s<file>:%d");
        assertTrue(builder.options.size() == 1);
        final Option option = (Option) builder.options.iterator().next();
        assertEquals("a", option.getShortName());
        assertNull(option.getLongName());
        assertEquals("s", option.getArgName());
        assertTrue(option.getValidator() instanceof FileValidator);
        assertTrue(option.getValidator() instanceof NumberValidator);
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_multipleOptions() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:s a:i");
        assertTrue(builder.options.size() == 2);
        final Option option1 = (Option) builder.options.iterator().next();
        assertEquals("a", option1.getShortName());
        assertEquals("", option1.getLongName());
        assertEquals("s", option1.getArgName());
        assertNull(option1.getValidator());
        assertFalse(option1.isRequired());

        final Option option2 = (Option) builder.options.iterator().next();
        assertEquals("a", option2.getShortName());
        assertEquals("i", option2.getLongName());
        assertNull(option2.getArgName());
        assertNull(option2.getValidator());
        assertFalse(option2.isRequired());
    }

    @Test
    public void testWithPattern_optionsWithGroups() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("g a:s g b:i");
        assertTrue(builder.options.size() == 2);
        final Option option1 = (Option) builder.options.iterator().next();
        assertTrue(option1 instanceof GroupOption);
        assertEquals("g", option1.getShortName());
        assertNull(option1.getLongName());
        assertTrue(option1.getOptions().size() == 1);
        final Option groupOption1 = (Option) option1.getOptions().iterator().next();
        assertEquals("a", groupOption1.getShortName());
        assertEquals("", groupOption1.getLongName());
        assertEquals("s", groupOption1.getArgName());
        assertNull(groupOption1.getValidator());
        assertFalse(groupOption1.isRequired());

        final Option option2 = (Option) builder.options.iterator().next();
        assertTrue(option2 instanceof GroupOption);
        assertEquals("g", option2.getShortName());
        assertNull(option2.getLongName());
        assertTrue(option2.getOptions().size() == 1);
        final Option groupOption2 = (Option) option2.getOptions().iterator().next();
        assertEquals("b", groupOption2.getShortName());
        assertEquals("i", groupOption2.getLongName());
        assertNull(groupOption2.getArgName());
        assertNull(groupOption2.getValidator());
        assertFalse(groupOption2.isRequired());
    }

    @Test
    public void testWithPattern_optionWithClassValidator() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("c:@java.lang.Number");
        assertTrue(builder.options.size() == 1);
        final Option option = (Option) builder.options.iterator().next();
        assertEquals("c", option.getShortName());
        assertNull(option.getLongName());
        assertNull(option.getArgName());
        assertTrue(option.getValidator() instanceof ClassValidator);
        assertTrue(((ClassValidator) option.getValidator()).getInstance().isInstance(new Integer(1)));
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_optionWithNumberValidator() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("n:/%d");
        assertTrue(builder.options.size() == 1);
        final Option option = (Option) builder.options.iterator().next();
        assertEquals("n", option.getShortName());
        assertNull(option.getLongName());
        assertNull(option.getArgName());
        assertTrue(option.getValidator() instanceof NumberValidator);
        assertTrue(((NumberValidator) option.getValidator()).isValid(new Double(1.5), null, null));
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_optionWithDateValidator() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("d:/%tR");
        assertTrue(builder.options.size() == 1);
        final Option option = (Option) builder.options.iterator().next();
        assertEquals("d", option.getShortName());
        assertNull(option.getLongName());
        assertNull(option.getArgName());
        assertTrue(option.getValidator() instanceof DateValidator);
        assertTrue(((DateValidator) option.getValidator()).isValid(new java.util.Date()));
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_optionWithFileValidator() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("f:/>file.txt");
        assertTrue(builder.options.size() == 1);
        final Option option = (Option) builder.options.iterator().next();
        assertEquals("f", option.getShortName());
        assertNull(option.getLongName());
        assertEquals("file.txt", option.getArgName());
        assertTrue(option.getValidator() instanceof FileValidator);
        assertTrue(((FileValidator) option.getValidator()).isValid("file.txt"));
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_optionWithUrlValidator() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("u:/url");
        assertTrue(builder.options.size() == 1);
        final Option option = (Option) builder.options.iterator().next();
        assertEquals("u", option.getShortName());
        assertNull(option.getLongName());
        assertNull(option.getArgName());
        assertTrue(option.getValidator() instanceof UrlValidator);
        assertTrue(((UrlValidator) option.getValidator()).isValid("http://example.com"));
        assertFalse(option.isRequired());
    }

    @Test
    public void testWithPattern_optionWithCustomValidator() {
        final PatternBuilder builder = new PatternBuilder();
        builder.withPattern("v:/my-validator MyValidatorClass");
        assertTrue(builder.options.size() == 1);
        final Option option = (Option) builder.options.iterator().next();
        assertEquals("v", option.getShortName());
        assertNull(option.getLongName());
        assertNull(option.getArgName());
        assertTrue(option.getValidator() instanceof MyValidatorClass);
        assertFalse(option.isRequired());
    }
}

class MyValidatorClass implements Validator {
    @Override
    public boolean isValid(String value, String message, Object... args) {
        // Implement custom validation logic here
        return true;
    }
}