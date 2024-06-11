import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
    private PatternBuilder builder;

    @Before
    public void setUp() {
        builder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option1 = new Option.Builder("-f").create();
        Option option2 = new Option.Builder("-b").create();
        builder.options.add(option1);
        builder.options.add(option2);

        Option result = builder.create();

        assertEquals(option1, result);
    }

    @Test
    public void testReset() {
        Option option1 = new Option.Builder("-f").create();
        builder.options.add(option1);

        builder.reset();

        assertTrue(builder.options.isEmpty());
    }

    @Test
    public void testWithPattern() {
        builder.withPattern("-f");

        Option option = new Option.Builder("-f").create();
        assertTrue(builder.options.contains(option));
    }

    @Test
    public void testValidator() {
        ClassValidator classValidator = new ClassValidator();
        classValidator.setInstance(true);
        assertEquals(classValidator, builder.validator('@'));

        ClassValidator instanceValidator = new ClassValidator();
        assertEquals(instanceValidator, builder.validator('+'));
        
        NumberValidator numberValidator = NumberValidator.getNumberInstance();
        assertEquals(numberValidator, builder.validator('%'));

        DateValidator dateValidator = DateValidator.getDateInstance();
        assertEquals(dateValidator, builder.validator('#'));

        FileValidator existingValidator = new FileValidator();
        existingValidator.setExisting(true);
        existingValidator.setFile(true);
        assertEquals(existingValidator, builder.validator('<'));

        FileValidator fileValidator = new FileValidator();
        assertEquals(fileValidator, builder.validator('>'));
        
        UrlValidator urlValidator = new UrlValidator();
        assertEquals(urlValidator, builder.validator('/'));

        assertNull(builder.validator(':'));
    }
}