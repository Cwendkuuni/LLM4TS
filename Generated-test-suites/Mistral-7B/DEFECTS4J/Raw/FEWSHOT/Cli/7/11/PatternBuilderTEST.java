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
import org.apache.commons.cli2.validation.Validator;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilder() {
        PatternBuilder pb = new PatternBuilder();

        Option option = pb.withPattern("@<foo.bar>").create();
        assertTrue(option.getLongOpt().startsWith("--foo-bar-"));

        option = pb.withPattern("+FooBar").create();
        assertTrue(option.getLongOpt().startsWith("--foo-bar-"));
        assertTrue(option.getArgument().isRequired());

        option = pb.withPattern(":baz").create();
        assertTrue(option.getLongOpt().startsWith("--baz-"));
        assertNull(option.getArgument());

        option = pb.withPattern("%d").create();
        Argument argument = option.getArgument();
        assertTrue(NumberValidator.class.isAssignableFrom(argument.getValidator().getClass()));

        option = pb.withPattern("#dd-MMM-yyyy").create();
        argument = option.getArgument();
        assertTrue(DateValidator.class.isAssignableFrom(argument.getValidator().getClass()));

        option = pb.withPattern("<foo>").create();
        argument = option.getArgument();
        assertTrue(FileValidator.class.isAssignableFrom(argument.getValidator().getClass()));

        option = pb.withPattern("*/bar").create();
        argument = option.getArgument();
        assertTrue(FileValidator.class.isAssignableFrom(argument.getValidator().getClass()));
        assertTrue(argument.isMultipleValuesAllowed());

        option = pb.withPattern("/foo").create();
        argument = option.getArgument();
        assertTrue(UrlValidator.class.isAssignableFrom(argument.getValidator().getClass()));

        option = pb.withPattern("!foo").create();
        assertTrue(option.isRequired());

        option = pb.withPattern("*").create();
        assertNull(option.getArgument());
    }
}