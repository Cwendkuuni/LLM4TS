package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.validation.Validator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setup() {
        patternBuilder = new PatternBuilder();
    }

    /**
     * Test creating a new Option instance with no pattern defined
     */
    @Test
    public void testCreateNewOption() {
        Option option = patternBuilder.create();
        assertNull(option);
    }

    /**
     * Test creating an Option with a simple pattern: "A"
     */
    @Test
    public void testCreateOptionWithSingleCharacter() {
        patternBuilder.withPattern("A");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
        assertNull(option.getArgument());
        assertNull(option.getShortName());
    }

    /**
     * Test creating an Option with a pattern containing a required option: "-a"
     */
    @Test
    public void testCreateOptionWithSingleRequiredShortOption() {
        patternBuilder.withPattern("-a");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isRequired());
        assertNull(option.getArgument());
        assertEquals("-a", option.getShortName());
    }

    /**
     * Test creating an Option with a pattern containing an optional short option: "-a [F]"
     */
    @Test
    public void testCreateOptionWithOptionalSingleShortOption() {
        patternBuilder.withPattern("-a [F]");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertFalse(option.isRequired());
        assertNull(option.getArgument());
        assertEquals("-a", option.getShortName());
    }

    /**
     * Test creating an Option with a pattern containing a required short option: "--file"
     */
    @Test
    public void testCreateOptionWithRequiredShortOption() {
        patternBuilder.withPattern("--file");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertTrue(option.isRequired());
        assertNull(option.getArgument());
        assertNull(option.getShortName());
    }

    /**
     * Test creating an Option with a pattern containing a long option: "--file"
     */
    @Test
    public void testCreateOptionWithLongOption() {
        patternBuilder.withPattern("--file");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertNull(option.getShortName());
        assertEquals("file", option.getLongName());
        assertNull(option.getArgument());
    }

    /**
     * Test creating an Option with a pattern containing an argument: "--file <FILE>"
     */
    @Test
    public void testCreateOptionWithArgument() {
        patternBuilder.withPattern("--file <FILE>");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertNull(option.getShortName());
        assertEquals("file", option.getLongName());
        assertNotNull(option.getArgument());
        assertEquals(Argument.UNDEF, option.getArgument().getName());
        assertEquals(Argument.REQUIRED_ARGUMENT, option.getArgument().getType());
    }

    /**
     * Test creating an Option with a pattern containing a number validator: "--num <N>"
     */
    @Test
    public void testCreateOptionWithNumberValidator() {
        patternBuilder.withPattern("--num <N>");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertNull(option.getShortName());
        assertEquals("num", option.getLongName());
        assertNotNull(option.getArgument());
        assertEquals(Argument.UNDEF, option.getArgument().getName());
        assertEquals(Argument.VALUE_INVALID, option.getArgument().getType());
        assertTrue(option.isValid());
        option.setValue("123");
        assertTrue(option.isValid());
        option.setValue("456abc");
        assertFalse(option.isValid());
    }

    /**
     * Test creating an Option with a pattern containing a date validator: "--date <DD-MM-YYYY>"
     */
    @Test
    public void testCreateOptionWithDateValidator() {
        patternBuilder.withPattern("--date <DD-MM-YYYY>");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertNull(option.getShortName());
        assertEquals("date", option.getLongName());
        assertNotNull(option.getArgument());
        assertEquals(Argument.UNDEF, option.getArgument().getName());
        assertEquals(Argument.VALUE_INVALID, option.getArgument().getType());
        assertTrue(option.isValid());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        option.setValue("31-12-2022");
        assertTrue(option.isValid());
        option.setValue("32-12-2022");
        assertFalse(option.isValid());
    }

    /**
     * Test creating an Option with a pattern containing a class validator: "--class <ClassName>"
     */
    @Test
    public void testCreateOptionWithClassValidator() {
        patternBuilder.withPattern("--class <ClassName>");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertNull(option.getShortName());
        assertEquals("class", option.getLongName());
        assertNotNull(option.getArgument());
        assertEquals(Argument.UNDEF, option.getArgument().getName());
        assertEquals(Argument.VALUE_INVALID, option.getArgument().getType());
        assertTrue(option.isValid());
        try {
            Class<?> clazz = Class.forName("java.lang.String");
            option.setValue(clazz.getName());
            assertTrue(option.isValid());
        } catch (ClassNotFoundException e) {
            // expected
        }
        option.setValue("java.lang.Intройка");
        assertFalse(option.isValid());
    }

    /**
     * Test creating an Option with a pattern containing a file validator: "--file <FILE>"
     */
    @Test
    public void testCreateOptionWithFileValidator() {
        patternBuilder.withPattern("--file <FILE>");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertNull(option.getShortName());
        assertEquals("file", option.getLongName());
        assertNotNull(option.getArgument());
        assertEquals(Argument.UNDEF, option.getArgument().getName());
        assertEquals(Argument.VALUE_INVALID, option.getArgument().getType());
        assertTrue(option.isValid());
        FileValidator fileValidator = new FileValidator();
        option.setValidator(fileValidator);
        option.setValue("/path/to/file.txt");
        assertTrue(option.isValid());
        option.setValue("/path/to/non-existent-file.txt");
        assertFalse(option.isValid());
    }

    /**
     * Test creating an Option with a pattern containing multiple validators: "--num <N> --date <DD-MM-YYYY>"
     */
    @Test
    public void testCreateOptionWithMultipleValidators() {
        patternBuilder.withPattern("--num <N> --date <DD-MM-YYYY>");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertNull(option.getShortName());
        assertEquals("num", option.getLongName());
        assertNotNull(option.getArgument());
        assertEquals(Argument.UNDEF, option.getArgument().getName());
        assertEquals(Argument.VALUE_INVALID, option.getArgument().getType());
        assertTrue(option.isValid());
        option.setValue("123");
        assertTrue(option.isValid());
        option.setValue("456abc");
        assertFalse(option.isValid());

        assertNull(option.getLongName());
        assertEquals("date", option.getLongName());
        assertNotNull(option.getArgument());
        assertEquals(Argument.UNDEF, option.getArgument().getName());
        assertEquals(Argument.VALUE_INVALID, option.getArgument().getType());
        assertTrue(option.isValid());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        option.setValue("31-12-2022");
        assertTrue(option.isValid());
        option.setValue("32-12-2022");
        assertFalse(option.isValid());
    }

    /**
     * Test creating an Option with a pattern containing multiple options: "--num <N> --file <FILE>"
     */
    @Test
    public void testCreateOptionWithMultipleOptions() {
        patternBuilder.withPattern("--num <N> --file <FILE>");
        Option optionNum = patternBuilder.create();
        Option optionFile = patternBuilder.create();

        assertNotNull(optionNum);
        assertNull(optionNum.getLongName());
        assertEquals("num", optionNum.getLongName());
        assertNotNull(optionNum.getArgument());
        assertEquals(Argument.UNDEF, optionNum.getArgument().getName());
        assertEquals(Argument.VALUE_INVALID, optionNum.getArgument().getType());
        assertTrue(optionNum.isValid());
        optionNum.setValue("123");
        assertTrue(optionNum.isValid());
        optionNum.setValue("456abc");
        assertFalse(optionNum.isValid());

        assertNotNull(optionFile);
        assertNull(optionFile.getLongName());
        assertEquals("file", optionFile.getLongName());
        assertNotNull(optionFile.getArgument());
        assertEquals(Argument.UNDEF, optionFile.getArgument().getName());
        assertEquals(Argument.VALUE_INVALID, optionFile.getArgument().getType());
        assertTrue(optionFile.isValid());
        FileValidator fileValidator = new FileValidator();
        optionFile.setValidator(fileValidator);
        optionFile.setValue("/path/to/file.txt");
        assertTrue(optionFile.isValid());
        optionFile.setValue("/path/to/non-existent-file.txt");
        assertFalse(optionFile.isValid());
    }

    /**
     * Test creating an Option with a pattern containing a long and a short option: "--file [F]"
     */
    @Test
    public void testCreateOptionWithLongAndShortOption() {
        patternBuilder.withPattern("--file [F]");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertNull(option.getShortName());
        assertEquals("file", option.getLongName());
        assertNull(option.getArgument());

        Option shortOption = patternBuilder.create();
        assertNotNull(shortOption);
        assertEquals("-f", shortOption.getShortName());
        assertNull(shortOption.getLongName());
        assertNull(shortOption.getArgument());
        assertTrue(option.isCompatibleWith(shortOption));
    }

    /**
     * Test creating an Option with a pattern containing multiple pattern characters: "!@#%&*+-/:<>"
     */
    @Test
    public void testCreateOptionWithMultiplePatternCharacters() {
        patternBuilder.withPattern("!@#%&*+-/:<>");
        Option option = patternBuilder.create();
        assertNotNull(option);
        assertNull(option.getShortName());
        assertNull(option.getLongName());
        assertNull(option.getArgument());
        assertTrue(option.isValid());
    }
}