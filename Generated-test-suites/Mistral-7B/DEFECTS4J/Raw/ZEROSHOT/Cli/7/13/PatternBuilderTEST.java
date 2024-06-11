package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.ArgumentBuilder;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @org.junit.Before
    public void setUp() {
        // Set up
    }

    @org.junit.Test
    public void testPatternBuilder() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option option = patternBuilder.withPattern("a:") // option 'a'
                .withPattern("b@<file>") // option 'b' with validator FileValidator and required to be existing file
                .withPattern("c%d{3}") // option 'c' with validator NumberValidator and 3 digits
                .create();

        assertNotNull(option);
        assertTrue(option.getLongOpt().equals("a"));
        assertTrue(option.getShortName() == 'a');
        assertTrue(option.hasArg());

        Option optionWithArg = patternBuilder.withPattern("d:<file>") // option 'd' with validator FileValidator and required to be existing file with argument
                .create();

        assertNotNull(optionWithArg);
        assertTrue(optionWithArg.getLongOpt().equals("d"));
        assertTrue(optionWithArg.getShortName() == 'd');
        assertTrue(optionWithArg.hasArg());

        Option optionWithNumberValidator = patternBuilder.withPattern("e:%d") // option 'e' with validator NumberValidator and no minimum or maximum
                .create();

        assertNotNull(optionWithNumberValidator);
        assertTrue(optionWithNumberValidator.getLongOpt().equals("e"));
        assertTrue(optionWithNumberValidator.getShortName() == 'e');
        assertTrue(optionWithNumberValidator.hasArg());

        Option optionWithMinimum = patternBuilder.withPattern("f:+{3}") // option 'f' with validator NumberValidator and minimum 3 digits
                .create();

        assertNotNull(optionWithMinimum);
        assertTrue(optionWithMinimum.getLongOpt().equals("f"));
        assertTrue(optionWithMinimum.getShortName() == 'f');
        assertTrue(optionWithMinimum.hasArg());
        assertTrue(optionWithMinimum.getArgument().getMinimum() == 3);

        Option optionWithMaximum = patternBuilder.withPattern("g:*{2}") // option 'g' with validator NumberValidator and maximum 2 digits
                .create();

        assertNotNull(optionWithMaximum);
        assertTrue(optionWithMaximum.getLongOpt().equals("g"));
        assertTrue(optionWithMaximum.getShortName() == 'g');
        assertTrue(optionWithMaximum.hasArg());
        assertTrue(optionWithMaximum.getArgument().getMaximum() == 2);

        Option optionWithDateValidator = patternBuilder.withPattern("h:/d{yyyy-MM-dd}") // option 'h' with validator DateValidator and format yyyy-MM-dd
                .create();

        assertNotNull(optionWithDateValidator);
        assertTrue(optionWithDateValidator.getLongOpt().equals("h"));
        assertTrue(optionWithDateValidator.getShortName() == 'h');
        assertTrue(optionWithDateValidator.hasArg());

        Option optionWithClassValidator = patternBuilder.withPattern("i:@java.lang.String") // option 'i' with validator ClassValidator and expected class java.lang.String
                .create();

        assertNotNull(optionWithClassValidator);
        assertTrue(optionWithClassValidator.getLongOpt().equals("i"));
        assertTrue(optionWithClassValidator.getShortName() == 'i');
        assertTrue(optionWithClassValidator.hasArg());
        assertTrue(optionWithClassValidator.getArgument().isValidator() instanceof ClassValidator);
        assertTrue(((ClassValidator) optionWithClassValidator.getArgument().getValidator()).getClazz().equals(String.class));
    }

    @org.junit.Test
    public void testPatternBuilderWithClassValidatorInstance() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option optionWithClassValidatorInstance = patternBuilder.withPattern("j:@my.package.MyClass") // option 'j' with validator ClassValidator and expected class MyClass from package my.package
                .create();

        assertNotNull(optionWithClassValidatorInstance);
        assertTrue(optionWithClassValidatorInstance.getLongOpt().equals("j"));
        assertTrue(optionWithClassValidatorInstance.getShortName() == 'j');
        assertTrue(optionWithClassValidatorInstance.hasArg());
        assertTrue(optionWithClassValidatorInstance.getArgument().isValidator() instanceof ClassValidator);
        assertTrue(((ClassValidator) optionWithClassValidatorInstance.getArgument().getValidator()).getClazz().getName().equals("my.package.MyClass"));
    }

    @org.junit.Test
    public void testPatternBuilderWithNumberValidatorInstance() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option optionWithNumberValidatorInstance = patternBuilder.withPattern("k:%d{integer,min=0,max=5}") // option 'k' with validator NumberValidator and minimum 0 and maximum 5
                .create();

        assertNotNull(optionWithNumberValidatorInstance);
        assertTrue(optionWithNumberValidatorInstance.getLongOpt().equals("k"));
        assertTrue(optionWithNumberValidatorInstance.getShortName() == 'k');
        assertTrue(optionWithNumberValidatorInstance.hasArg());
        assertTrue(optionWithNumberValidatorInstance.getArgument().isValidator() instanceof NumberValidator);
        assertTrue(((NumberValidator) optionWithNumberValidatorInstance.getArgument().getValidator()).getIntegerType().isInteger());
        assertTrue(((NumberValidator) optionWithNumberValidatorInstance.getArgument().getValidator()).getMinValue() == 0);
        assertTrue(((NumberValidator) optionWithNumberValidatorInstance.getArgument().getValidator()).getMaxValue() == 5);
    }

    @org.junit.Test
    public void testPatternBuilderWithDateValidatorInstance() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option optionWithDateValidatorInstance = patternBuilder.withPattern("l:/d{date,format=\"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\"}") // option 'l' with validator DateValidator and format yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
                .create();

        assertNotNull(optionWithDateValidatorInstance);
        assertTrue(optionWithDateValidatorInstance.getLongOpt().equals("l"));
        assertTrue(optionWithDateValidatorInstance.getShortName() == 'l');
        assertTrue(optionWithDateValidatorInstance.hasArg());
        assertTrue(optionWithDateValidatorInstance.getArgument().isValidator() instanceof DateValidator);
        assertTrue(((DateValidator) optionWithDateValidatorInstance.getArgument().getValidator()).getDateFormat().equals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }

    @org.junit.Test
    public void testPatternBuilderWithUrlValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option optionWithUrlValidator = patternBuilder.withPattern("m:/") // option 'm' with validator UrlValidator
                .create();

        assertNotNull(optionWithUrlValidator);
        assertTrue(optionWithUrlValidator.getLongOpt().equals("m"));
        assertTrue(optionWithUrlValidator.getShortName() == 'm');
        assertTrue(optionWithUrlValidator.hasArg());
        assertTrue(optionWithUrlValidator.getArgument().isValidator() instanceof UrlValidator);
    }

    @org.junit.Test
    public void testPatternBuilderWithNoPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option optionWithNoPattern = patternBuilder.create();

        assertNotNull(optionWithNoPattern);
        assertTrue(optionWithNoPattern.getLongOpt() == null);
        assertTrue(optionWithNoPattern.getShortName() == 0);
        assertFalse(optionWithNoPattern.hasArg());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testPatternBuilderWithInvalidPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();

        patternBuilder.withPattern("n:$"); // option 'n' with invalid pattern '$'
        patternBuilder.create();
    }

    @org.junit.After
    public void tearDown() {
        // Tear down
    }
}