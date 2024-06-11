import org.junit.Test;
import org.junit.Assert;

public class ArgumentImplTest {

    @Test
    public void testGetPreferredName() {
        String name = "arg";
        String description = "A description of the argument";
        int minimum = 0;
        int maximum = 1;
        char initialSeparator = '\0';
        char subsequentSeparator = '\0';
        Validator validator = null;
        String consumeRemaining = "--";
        List defaultValues = null;
        int id = 1;

        ArgumentImpl argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        String expected = "arg";
        String result = argument.getPreferredName();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetInitialSeparator() {
        String name = "arg";
        String description = "A description of the argument";
        int minimum = 0;
        int maximum = 1;
        char initialSeparator = '\0';
        char subsequentSeparator = '\0';
        Validator validator = null;
        String consumeRemaining = "--";
        List defaultValues = null;
        int id = 1;

        ArgumentImpl argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        char expected = '\0';
        char result = argument.getInitialSeparator();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetSubsequentSeparator() {
        String name = "arg";
        String description = "A description of the argument";
        int minimum = 0;
        int maximum = 1;
        char initialSeparator = '\0';
        char subsequentSeparator = '\0';
        Validator validator = null;
        String consumeRemaining = "--";
        List defaultValues = null;
        int id = 1;

        ArgumentImpl argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        char expected = '\0';
        char result = argument.getSubsequentSeparator();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetConsumeRemaining() {
        String name = "arg";
        String description = "A description of the argument";
        int minimum = 0;
        int maximum = 1;
        char initialSeparator = '\0';
        char subsequentSeparator = '\0';
        Validator validator = null;
        String consumeRemaining = "--";
        List defaultValues = null;
        int id = 1;

        ArgumentImpl argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        String expected = "--";
        String result = argument.getConsumeRemaining();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetDefaultValues() {
        String name = "arg";
        String description = "A description of the argument";
        int minimum = 0;
        int maximum = 1;
        char initialSeparator = '\0';
        char subsequentSeparator = '\0';
        Validator validator = null;
        String consumeRemaining = "--";
        List defaultValues = null;
        int id = 1;

        ArgumentImpl argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        List expected = null;
        List result = argument.getDefaultValues();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetValidator() {
        String name = "arg";
        String description = "A description of the argument";
        int minimum = 0;
        int maximum = 1;
        char initialSeparator = '\0';
        char subsequentSeparator = '\0';
        Validator validator = null;
        String consumeRemaining = "--";
        List defaultValues = null;
        int id = 1;

        ArgumentImpl argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        Validator expected = null;
        Validator result = argument.getValidator();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetMaximum() {
        String name = "arg";
        String description = "A description of the argument";
        int minimum = 0;
        int maximum = 1;
        char initialSeparator = '\0';
        char subsequentSeparator = '\0';
        Validator validator = null;
        String consumeRemaining = "--";
        List defaultValues = null;
        int id = 1;

        ArgumentImpl argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        int expected = 1;
        int result = argument.getMaximum();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetMinimum() {
        String name = "arg";
        String description = "A description of the argument";
        int minimum = 0;
        int maximum = 1;
        char initialSeparator = '\0';
        char subsequentSeparator = '\0';
        Validator validator = null;
        String consumeRemaining = "--";
        List defaultValues = null;
        int id = 1;

        ArgumentImpl argument = new ArgumentImpl(name, description, minimum, maximum, initialSeparator, subsequentSeparator, validator, consumeRemaining, defaultValues, id);

        int expected = 0;
        int result = argument.getMinimum();

        Assert.assertEquals(expected, result);
    }
}