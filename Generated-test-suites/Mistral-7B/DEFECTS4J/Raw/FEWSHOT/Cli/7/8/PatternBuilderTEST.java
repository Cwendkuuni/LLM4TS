package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
    private PatternBuilder builder;

    @Before
    public void setup() {
        builder = new PatternBuilder();
    }

    @After
    public void teardown() {
        builder.reset();
    }

    @Test
    public void testCreateOptionWithNoPattern() {
        assertNull(builder.create().getShortName());
    }

    @Test
    public void testCreateOptionWithSingleCharacter() {
        builder.withPattern("p");
        Option option = builder.create();
        assertNotNull(option.getShortName());
        assertTrue(option.getShortName().length() == 1);
        assertTrue(option.getShortName().charAt(0) == 'p');
    }

    @Test
    public void testCreateOptionWithRequiredSingleCharacter() {
        builder.withPattern("!p");
        Option option = builder.create();
        assertNotNull(option.getShortName());
        assertTrue(option.getShortName().length() == 1);
        assertTrue(option.isRequired());
        assertTrue(option.getShortName().charAt(0) == 'p');
    }

    @Test
    public void testCreateOptionWithMultipleCharacters() {
        builder.withPattern("po");
        Option option = builder.create();
        assertNotNull(option.getShortName());
        assertTrue(option.getShortName().length() == 2);
        assertTrue(option.getShortName().charAt(0) == 'p');
        assertTrue(option.getShortName().charAt(1) == 'o');
    }

    @Test
    public void testCreateOptionWithRequiredMultipleCharacters() {
        builder.withPattern("!po");
        Option option = builder.create();
        assertNotNull(option.getShortName());
        assertTrue(option.getShortName().length() == 2);
        assertTrue(option.isRequired());
        assertTrue(option.getShortName().charAt(0) == 'p');
        assertTrue(option.getShortName().charAt(1) == 'o');
    }

    @Test
    public void testCreateOptionWithValidators() {
        builder.withPattern("%d");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof NumberValidator);
    }

    @Test
    public void testCreateOptionWithClassValidator() {
        builder.withPattern("@com.example.MyClass");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
    }

    @Test
    public void testCreateOptionWithDateValidator() {
        builder.withPattern("#yyyy-MM-dd");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof DateValidator);
    }

    @Test
    public void testCreateOptionWithFileValidatorForExistingFile() {
        builder.withPattern("<file.txt>");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertTrue(((FileValidator) option.getArgument().getValidator()).isExisting());
    }

    @Test
    public void testCreateOptionWithFileValidatorForNonExistingFile() {
        builder.withPattern("<file.txt>");
        builder.reset();
        builder.withPattern("<nonexistentfile.txt>");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertFalse(((FileValidator) option.getArgument().getValidator()).isExisting());
    }

    @Test
    public void testCreateOptionWithUrlValidator() {
        builder.withPattern("/www.example.com");
        Option option = builder.create();
        assertNotNull(option.getArgument());
        assertNotNull(option.getArgument().getValidator());
        assertTrue(option.getArgument().getValidator() instanceof UrlValidator);
    }

    @Test
    public void testCreateMultipleOptionsWithPattern() {
        builder.withPattern("po:s,pt:i");
        Option option1 = builder.create();
        Option option2 = builder.create();
        assertNotNull(option1);
        assertNotNull(option2);
        assertFalse(option1.equals(option2));
        assertTrue(option1.getShortName().equals("p"));
        assertTrue(option1.getLongName().equals("o"));
        assertTrue(option1.getArgument().isOptional());
        assertTrue(option1.getValidator() == null);
        assertTrue(option2.getShortName().equals("p"));
        assertTrue(option2.getLongName().equals("t"));
        assertTrue(option2.getArgument().isOptional());
        assertTrue(option2.getArgument().getType() == Argument.Type.INTEGER);
        assertTrue(option2.getValidator() != null);
    }

    @Test
    public void testCreateMultipleOptionsWithPatternWithSameShortName() {
        builder.withPattern("po:s,po:i");
        Option option1 = builder.create();
        Option option2 = builder.create();
        assertNotNull(option1);
        assertNotNull(option2);
        assertFalse(option1.equals(option2));
        assertTrue(option1.getShortName().equals("p"));
        assertTrue(option1.getLongName().equals("o"));
        assertTrue(option1.isRequired());
        assertTrue(option1.getArgument().isOptional());
        assertTrue(option1.getValidator() == null);
        assertTrue(option2.getShortName().equals("p"));
        assertTrue(option2.getLongName().equals("o"));
        assertTrue(option2.isRequired());
        assertTrue(option2.getArgument().isOptional());
        assertTrue(option2.getArgument().getType() == Argument.Type.STRING);
        assertTrue(option2.getValidator() == null);
    }

    @Test
    public void testCreateMultipleOptionsWithPatternWithSameLongName() {
        builder.withPattern("po:s,po:l");
        Option option1 = builder.create();
        Option option2 = builder.create();
        assertNotNull(option1);
        assertNotNull(option2);
        assertFalse(option1.equals(option2));
        assertTrue(option1.getShortName().equals("p"));
        assertTrue(option1.getLongName().equals("o"));
        assertTrue(option1.isRequired());
        assertTrue(option1.getArgument().isOptional());
        assertTrue(option1.getValidator() == null);
        assertTrue(option2.getShortName().equals("p"));
        assertTrue(option2.getLongName().equals("o"));
        assertTrue(option2.isRequired());
        assertTrue(option2.getArgument().isOptional());
        assertTrue(option2.getArgument().getType() == Argument.Type.LONG);
        assertTrue(option2.getValidator() == null);
    }
}