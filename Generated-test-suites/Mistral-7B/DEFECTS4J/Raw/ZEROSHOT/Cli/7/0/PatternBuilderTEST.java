package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

class PatternBuilderTest {

    @Test
    void testPatternBuilder() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option option = patternBuilder.withPattern("a:@com.test.MyClass").create();
        Assertions.assertNotNull(option);
        Assertions.assertEquals("a", option.getLongOpt());
        Assertions.assertEquals("com.test.MyClass", option.getValueDescriptor().getType());

        option = patternBuilder.withPattern("b:+MyClass").create();
        Assertions.assertNotNull(option);
        Assertions.assertEquals("b", option.getLongOpt());
        Assertions.assertTrue(option.getValueDescriptor().isInstanceRequired());

        option = patternBuilder.withPattern("c:@com.test.MyClass:<test.txt>").create();
        Assertions.assertNotNull(option);
        Assertions.assertEquals("c", option.getLongOpt());
        Assertions.assertTrue(option.getValueDescriptor().isFileValueRequired());
        Assertions.assertTrue(option.getValueDescriptor().isFileExistingRequired());

        option = patternBuilder.withPattern("d:#%d%d%d%d%d%d").create();
        Argument argument = option.getValueDescriptor().getArgument();
        Assertions.assertNotNull(argument);
        Assertions.assertEquals(NumberValidator.class, argument.getValidator().getClass());
        Assertions.assertEquals(6, argument.getMinimum());
        Assertions.assertEquals(6, argument.getMaximum());

        option = patternBuilder.withPattern("e:@com.test.MyClass:<test.txt>:/test.com").create();
        Assertions.assertNotNull(option);
        Assertions.assertEquals("e", option.getLongOpt());
        Assertions.assertTrue(option.getValueDescriptor().isFileValueRequired());
        Assertions.assertTrue(option.getValueDescriptor().isFileExistingRequired());
        Assertions.assertEquals(UrlValidator.class, option.getValueDescriptor().getArgument().getValidator().getClass());

        option = patternBuilder.withPattern("f:/test.com").create();
        Assertions.assertNotNull(option);
        Assertions.assertEquals("f", option.getLongOpt());
        Assertions.assertEquals(UrlValidator.class, option.getValueDescriptor().getArgument().getValidator().getClass());

        option = patternBuilder.withPattern("g:<").create();
        Assertions.assertNotNull(option);
        Assertions.assertEquals("g", option.getLongOpt());
        Assertions.assertTrue(option.getValueDescriptor().isFileValueRequired());
        Assertions.assertFalse(option.getValueDescriptor().isFileExistingRequired());
        Assertions.assertEquals(FileValidator.class, option.getValueDescriptor().getArgument().getValidator().getClass());

        option = patternBuilder.withPattern("h:").create();
        Assertions.assertNull(option);

        option = patternBuilder.withPattern("i:@:").create();
        Assertions.assertNull(option);

        option = patternBuilder.withPattern("j:-").create();
        Assertions.assertNull(option);

        option = patternBuilder.withPattern("k:%d").create();
        Argument argument1 = option.getValueDescriptor().getArgument();
        Assertions.assertNotNull(argument1);
        Assertions.assertEquals(NumberValidator.class, argument1.getValidator().getClass());
        Assertions.assertEquals(1, argument1.getMinimum());
        Assertions.assertEquals(1, argument1.getMaximum());

        option = patternBuilder.withPattern("l:#%d%d-%02d-%02d").create();
        Argument argument2 = option.getValueDescriptor().getArgument();
        Assertions.assertNotNull(argument2);
        Assertions.assertEquals(DateValidator.class, argument2.getValidator().getClass());
        LocalDate localDate = LocalDate.of(2022, 11, 25);
        Assertions.assertEquals(localDate, argument2.getDefaultValue());

        option = patternBuilder.withPattern("m:#%d-%02d-%02d %02d:%02d:%02d").create();
        Argument argument3 = option.getValueDescriptor().getArgument();
        Assertions.assertNotNull(argument3);
        Assertions.assertEquals(DateValidator.class, argument3.getValidator().getClass());
        LocalDate localDate1 = LocalDate.of(2022, 11, 25);
        Assertions.assertEquals(localDate1, argument3.getDefaultValue());
    }

    @Test
    void testPatternBuilderNullOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.withPattern("n:").create();
        Assertions.assertNull(option);
    }
}