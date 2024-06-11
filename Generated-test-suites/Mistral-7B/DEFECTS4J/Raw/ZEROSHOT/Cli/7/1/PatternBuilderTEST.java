package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.CommandLineParser;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilderCreateOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName().equals("a"));
        assertTrue(option.getLongName() == null);
        assertTrue(option.isRequired());
        assertTrue(option.getArgument() == null);

        patternBuilder.reset();
        patternBuilder.withPattern("a:s");
        option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName().equals("a"));
        assertTrue(option.getLongName() == null);
        assertTrue(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getName().equals("s"));

        patternBuilder.reset();
        patternBuilder.withPattern("a:s,b:i");
        option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName().equals("a"));
        assertTrue(option.getLongName() == null);
        assertTrue(option.isRequired());
        argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getName().equals("s"));

        Option option2 = patternBuilder.create();
        assertNotNull(option2);
        assertTrue(option2.getShortName().equals("b"));
        assertTrue(option2.getLongName() == null);
        assertTrue(option2.isRequired());
        argument = option2.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getName().equals("i"));
    }

    @Test
    public void testPatternBuilderCreateGroup() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("g{abc}:");
        Option groupOption = patternBuilder.create();
        assertNotNull(groupOption);
        assertTrue(groupOption.getShortName() == null);
        assertTrue(groupOption.getLongName().equals("abc"));
        assertTrue(groupOption.isRequired());
        GroupBuilder groupBuilder = (GroupBuilder)groupOption.getBuilder();
        Option groupOption1 = groupBuilder.getOptions().get(0);
        Option option1 = groupBuilder.create();
        patternBuilder.reset();
        patternBuilder.withPattern("g{abc:a}:");
        Option groupOption2 = patternBuilder.create();
        Option option2 = groupBuilder.create();

        assertNotNull(groupOption);
        assertTrue(groupOption.isAllowedMultiple());
        assertNotNull(groupOption1);
        assertTrue(groupOption1.getShortName().equals("a"));
        assertTrue(groupOption1.getLongName() == null);
        assertTrue(groupOption1.isRequired());
        assertTrue(groupOption1.getArgument() == null);

        assertNotNull(groupOption2);
        assertTrue(groupOption2.isAllowedMultiple());
        assertNotNull(option2);
        assertTrue(option2.getShortName().equals("a"));
        assertTrue(option2.getLongName() == null);
        assertTrue(option2.isRequired());
        Argument argument = option2.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getName().equals(""));
    }

    @Test
    public void testPatternBuilderCreateWithClassValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("c:C{java.lang.String}");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName().equals("c"));
        assertTrue(option.getLongName() == null);
        assertTrue(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getName().equals(""));
        assertTrue(option.getValidator() instanceof ClassValidator);
        ClassValidator classValidator = (ClassValidator)option.getValidator();
        assertTrue(classValidator.isInstance());
        Class<?> expectedClass = String.class;
        CommandLineParser commandLineParser = new DefaultParser();
        try {
            commandLineParser.parse(option, new String[0]);
            Object result = commandLineParser.getResult().getValues("c")[0];
            assertTrue(result.getClass().equals(expectedClass));
        } catch (ParseException e) {
            // ignore
        }
    }

    @Test
    public void testPatternBuilderCreateWithNumberValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("%:i{1,2}");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName().equals("%"));
        assertTrue(option.getLongName() == null);
        assertTrue(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getName().equals(""));
        assertTrue(option.getValidator() instanceof NumberValidator);
        NumberValidator numberValidator = (NumberValidator)option.getValidator();
        assertTrue(numberValidator.isMinimum(1));
        assertTrue(numberValidator.isMaximum(2));
    }

    @Test
    public void testPatternBuilderCreateWithDateValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("#:yyyy-MM-dd");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName().equals("#"));
        assertTrue(option.getLongName() == null);
        assertTrue(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getName().equals(""));
        assertTrue(option.getValidator() instanceof DateValidator);
        DateValidator dateValidator = (DateValidator)option.getValidator();
        //TODO Add test for valid date
    }

    @Test
    public void testPatternBuilderCreateWithFileValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("<:file");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName().equals("<"));
        assertTrue(option.getLongName() == null);
        assertTrue(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getName().equals("file"));
        assertTrue(option.getValidator() instanceof FileValidator);
        FileValidator fileValidator = (FileValidator)option.getValidator();
        //TODO Add test for existing file
    }

    @Test
    public void testPatternBuilderCreateWithUrlValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("/:url");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.getShortName().equals("/"));
        assertTrue(option.getLongName() == null);
        assertTrue(option.isRequired());
        Argument argument = option.getArgument();
        assertNotNull(argument);
        assertTrue(argument.getName().equals("url"));
        assertTrue(option.getValidator() instanceof UrlValidator);
        UrlValidator urlValidator = (UrlValidator)option.getValidator();
        //TODO Add test for valid url
    }

    @Test
    public void testPatternBuilderCreateWithCustomValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        //TODO Implement custom validator and test it here
    }
}