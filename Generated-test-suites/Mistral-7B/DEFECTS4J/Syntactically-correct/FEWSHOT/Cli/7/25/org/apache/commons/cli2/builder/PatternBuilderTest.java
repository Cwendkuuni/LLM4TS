package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

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
    public void testCreateOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.createOption('%', false, '*');
        Option option = builder.create();
        assertNotNull(option);
        NumberValidator validator = (NumberValidator) option.getValidator();
        assertNotNull(validator);
        validator.setMinimum(1);
        validator.setMaximum(3);
        assertNull(option.getArgument());

        builder.reset();
        builder.createOption('@', false, 'A');
        option = builder.create();
        assertNotNull(option);
        ClassValidator classValidator = (ClassValidator) option.getValidator();
        assertNotNull(classValidator);
        classValidator.setTargetType(String[].class);
    }

    @Test
    public void testWithPatternString() {
        PatternBuilder builder = new PatternBuilder();

        builder.withPattern("a:");
        Option option = builder.create();
        assertEquals("a", option.getShortName());
        assertNull(option.getArgument());
        assertNull(option.getValidator());

        builder.reset();

        builder.withPattern("d:@");
        option = builder.create();
        assertEquals("d", option.getShortName());
        ClassValidator classValidator = (ClassValidator) option.getValidator();
        assertNotNull(classValidator);
        classValidator.setTargetType(String[].class);

        builder.reset();

        builder.withPattern("g:+");
        option = builder.create();
        assertEquals("g", option.getShortName());
        ClassValidator instanceValidator = (ClassValidator) option.getValidator();
        assertNotNull(instanceValidator);

        builder.reset();

        builder.withPattern("i:/");
        option = builder.create();
        assertEquals("i", option.getShortName());
        UrlValidator urlValidator = (UrlValidator) option.getValidator();
        assertNull(option.getArgument());

        builder.reset();

        builder.withPattern("j:*");
        option = builder.create();
        assertEquals("j", option.getShortName());
        FileValidator fileValidator = (FileValidator) option.getValidator();
        assertNull(option.getArgument());

        builder.reset();

        builder.withPattern("k:<");
        option = builder.create();
        assertEquals("k", option.getShortName());
        FileValidator existingFileValidator = (FileValidator) option.getValidator();
        assertNull(option.getArgument());
        existingFileValidator.setExisting(true);
        existingFileValidator.setFile(true);

        builder.reset();

        builder.withPattern("l:<");
        option = builder.create();
        assertEquals("l", option.getShortName());
        FileValidator existingFileValidator2 = (FileValidator) option.getValidator();
        assertNull(option.getArgument());
        existingFileValidator2.setExisting(false);
        existingFileValidator2.setFile(true);

        builder.reset();

        builder.withPattern("m:");
        option = builder.create();
        assertEquals("m", option.getShortName());
        assertNull(option.getArgument());
        assertNull(option.getValidator());

        builder.reset();

        builder.withPattern("n:!");
        option = builder.create();
        assertEquals("n", option.getShortName());
        assertNotNull(option.getArgument());
        assertEquals(Argument.TYPE_BOOLEAN, option.getArgument().getType());

        builder.reset();

        builder.withPattern("o:@!");
        option = builder.create();
        assertEquals("o", option.getShortName());
        ClassValidator classValidator2 = (ClassValidator) option.getValidator();
        assertNotNull(classValidator2);
        classValidator2.setTargetType(Boolean.class);

        builder.reset();

        builder.withPattern("p:%");
        option = builder.create();
        assertEquals("p", option.getShortName());
        NumberValidator numberValidator = (NumberValidator) option.getValidator();
        assertNotNull(numberValidator);
        numberValidator.setMinimum(1);
        numberValidator.setMaximum(10);
        Argument argument = option.getArgument();
        assertEquals(Argument.TYPE_STRING, argument.getType());
    }

    @Test
    public void testCreateWithMultipleOptions() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:@,b:,c:#,d:>");
        Option optionA = builder.create();
        assertEquals("a", optionA.getShortName());
        ClassValidator classValidatorA = (ClassValidator) optionA.getValidator();
        assertNotNull(classValidatorA);
        classValidatorA.setTargetType(String[].class);

        Option optionB = builder.create();
        assertEquals("b", optionB.getShortName());
        assertNull(optionB.getArgument());
        assertNull(optionB.getValidator());

        Option optionC = builder.create();
        assertEquals("c", optionC.getShortName());
        DateValidator dateValidator = (DateValidator) optionC.getValidator();
        assertNotNull(dateValidator);

        Option optionD = builder.create();
        assertEquals("d", optionD.getShortName());
        FileValidator fileValidatorD = (FileValidator) optionD.getValidator();
        assertNull(optionD.getArgument());
        fileValidatorD.setExisting(true);
        fileValidatorD.setFile(true);
    }
}