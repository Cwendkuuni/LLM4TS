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
import org.apache.commons.cli2.validator.Validator;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testWithPatternEmpty() {
        PatternBuilder builder = new PatternBuilder();
        Option option = builder.create();
        assertTrue(option.isUnbound());
    }

    @Test
    public void testWithPatternSingleCharacterOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a");
        Option option = builder.create();
        assertEquals("a", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertTrue(option.isUnbound());
    }

    @Test
    public void testWithPatternSingleCharacterRequiredOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("!a");
        Option option = builder.create();
        assertEquals("a", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithPatternSingleCharacterOptionalGroup() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("a", option.getLongOpt());
        assertTrue(option.isUnbound());
    }

    @Test
    public void testWithPatternSingleCharacterOptionalGroupWithArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a::");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("a", option.getLongOpt());
        assertTrue(option.hasArg());
    }

    @Test
    public void testWithPatternMultipleCharacterOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("ab");
        Option option = builder.create();
        assertEquals("ab", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertTrue(option.isUnbound());
    }

    @Test
    public void testWithPatternMultipleCharacterRequiredOption() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("!ab");
        Option option = builder.create();
        assertEquals("ab", option.getOpt());
        assertEquals(null, option.getLongOpt());
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithPatternMultipleCharacterOptionalGroup() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("ab:");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("ab", option.getLongOpt());
        assertTrue(option.isUnbound());
    }

    @Test
    public void testWithPatternMultipleCharacterOptionalGroupWithArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("ab::");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("ab", option.getLongOpt());
        assertTrue(option.hasArg());
    }

    @Test
    public void testWithPatternMandatoryArg() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a/");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("a", option.getLongOpt());
        assertTrue(option.hasArg());
        assertTrue(option.isUnbound());
    }

    @Test
    public void testWithPatternOptionalArg() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:*");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("a", option.getLongOpt());
        assertTrue(option.hasArg());
        assertTrue(option.isUnbound());
    }

    @Test
    public void testWithPatternOptionalArgWithValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:%");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("a", option.getLongOpt());
        assertTrue(option.hasArg());
        assertTrue(option.isUnbound());
        Validator validator = new NumberValidator();
        assertEquals(validator, option.getArgumentValidator());
    }

    @Test
    public void testWithPatternOptionalGroupWithValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a:@ClassName");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("a", option.getLongOpt());
        assertTrue(option.isUnbound());
        ClassValidator classValidator = new ClassValidator();
        assertEquals(classValidator, option.getValidator());
    }

    @Test
    public void testWithPatternOptionalGroupWithArgumentAndValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("a::@ClassName");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("a", option.getLongOpt());
        assertTrue(option.hasArg());
        ClassValidator classValidator = new ClassValidator();
        assertEquals(classValidator, option.getValidator());
    }

    @Test
    public void testWithPatternGroup() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("(ab):");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("ab", option.getLongOpt());
        assertTrue(option.isUnbound());
        GroupBuilder groupBuilder = (GroupBuilder) option.getGroupBuilder();
        Option groupOption = groupBuilder.create();
        assertEquals("ab", groupOption.getOpt());
        assertEquals("ab", groupOption.getLongOpt());
        assertTrue(groupOption.isUnbound());
    }

    @Test
    public void testWithPatternGroupWithArgument() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("(ab)::");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("ab", option.getLongOpt());
        assertTrue(option.isUnbound());
        GroupBuilder groupBuilder = (GroupBuilder) option.getGroupBuilder();
        Option groupOption = groupBuilder.create();
        assertEquals(null, groupOption.getOpt());
        assertEquals("ab", groupOption.getLongOpt());
        assertTrue(groupOption.hasArg());
    }

    @Test
    public void testWithPatternGroupWithArgumentAndValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("(ab)::@ClassName");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("ab", option.getLongOpt());
        assertTrue(option.isUnbound());
        GroupBuilder groupBuilder = (GroupBuilder) option.getGroupBuilder();
        Option groupOption = groupBuilder.create();
        assertEquals(null, groupOption.getOpt());
        assertEquals("ab", groupOption.getLongOpt());
        assertTrue(groupOption.hasArg());
        ClassValidator classValidator = new ClassValidator();
        assertEquals(classValidator, groupOption.getValidator());
    }

    @Test
    public void testWithPatternGroupWithArgumentAndOptionalShortName() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("(ab):--foo");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("ab", option.getLongOpt());
        assertTrue(option.isUnbound());
        GroupBuilder groupBuilder = (GroupBuilder) option.getGroupBuilder();
        Option groupOption = groupBuilder.create();
        assertEquals("foo", groupOption.getOpt());
        assertEquals("ab", groupOption.getLongOpt());
        assertTrue(groupOption.isUnbound());
    }

    @Test
    public void testWithPatternGroupWithArgumentAndRequiredShortName() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("(ab):--foo!");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("ab", option.getLongOpt());
        assertTrue(option.isUnbound());
        GroupBuilder groupBuilder = (GroupBuilder) option.getGroupBuilder();
        Option groupOption = groupBuilder.create();
        assertEquals("foo", groupOption.getOpt());
        assertEquals("ab", groupOption.getLongOpt());
        assertTrue(groupOption.isRequired());
    }

    @Test
    public void testWithPatternGroupWithArgumentAndOptionalShortNameAndValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("(ab):--foo:%");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("ab", option.getLongOpt());
        assertTrue(option.isUnbound());
        GroupBuilder groupBuilder = (GroupBuilder) option.getGroupBuilder();
        Option groupOption = groupBuilder.create();
        assertEquals("foo", groupOption.getOpt());
        assertEquals("ab", groupOption.getLongOpt());
        assertTrue(groupOption.isUnbound());
        NumberValidator numberValidator = new NumberValidator();
        assertEquals(numberValidator, groupOption.getArgumentValidator());
    }

    @Test
    public void testWithPatternGroupWithArgumentAndRequiredShortNameAndValidator() {
        PatternBuilder builder = new PatternBuilder();
        builder.withPattern("(ab):--foo:%!");
        Option option = builder.create();
        assertEquals(null, option.getOpt());
        assertEquals("ab", option.getLongOpt());
        assertTrue(option.isUnbound());
        GroupBuilder groupBuilder = (GroupBuilder) option.getGroupBuilder();
        Option groupOption = groupBuilder.create();
        assertEquals("foo", groupOption.getOpt());
        assertEquals("ab", groupOption.getLongOpt());
        assertTrue(groupOption.isRequired());
        NumberValidator numberValidator = new NumberValidator();
        assertEquals(numberValidator, groupOption.getArgumentValidator());
    }

    private void assertEquals(char expected, char actual) {
        assertEquals(Character.toString(expected), Character.toString(actual));
    }

    private static Option createOption(PatternBuilder builder, char... pattern) {
        builder.withPattern(String.valueOf(pattern));
        return builder.create();
    }
}