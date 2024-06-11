package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest {

    /**
     * Initializes the static variables in OptionBuilder to their default values before each test.
     */
    @BeforeClass
    public static void resetOptionBuilder() {
        OptionBuilder.reset();
    }

    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}.
     */
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String longOpt = "longOpt";

        optionBuilder.withLongOpt(longOpt);

        Option option = OptionBuilder.create();
        String actualLongOpt = option.getLongOpt();

        Assert.assertEquals(longOpt, actualLongOpt);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();

        optionBuilder.hasArg();

        Option option = OptionBuilder.create();
        int actualNumberOfArgs = option.getOptargs().length;

        Assert.assertEquals(1, actualNumberOfArgs);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArgBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();

        optionBuilder.hasArg(true);

        Option option = OptionBuilder.create();
        int actualNumberOfArgs = option.getOptargs().length;

        Assert.assertEquals(1, actualNumberOfArgs);
        Assert.assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String argName = "argName";

        optionBuilder.withArgName(argName);

        Option option = OptionBuilder.create();
        String actualArgName = option.getArgName();

        Assert.assertEquals(argName, actualArgName);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();

        optionBuilder.isRequired();

        Option option = OptionBuilder.create();
        boolean actualRequired = option.isRequired();

        Assert.assertTrue(actualRequired);
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        char valueSeparator = ':';

        optionBuilder.withValueSeparator(valueSeparator);

        Option option = OptionBuilder.create();
        char actualValueSeparator = option.getValueSeparator();

        Assert.assertEquals(valueSeparator, actualValueSeparator);
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();

        optionBuilder.withValueSeparator();

        Option option = OptionBuilder.create();
        char actualValueSeparator = option.getValueSeparator();

        Assert.assertEquals('=', actualValueSeparator);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();

        optionBuilder.isRequired(false);

        Option option = OptionBuilder.create();
        boolean actualRequired = option.isRequired();

        Assert.assertFalse(actualRequired);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();

        optionBuilder.hasArgs();

        Option option = OptionBuilder.create();
        int actualNumberOfArgs = option.getOptargs().length;

        Assert.assertTrue(Option.UNLIMITED_VALUES == actualNumberOfArgs);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        int numberOfArgs = 2;

        optionBuilder.hasArgs(numberOfArgs);

        Option option = OptionBuilder.create();
        int actualNumberOfArgs = option.getOptargs().length;

        Assert.assertEquals(numberOfArgs, actualNumberOfArgs);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();

        optionBuilder.hasOptionalArg();

        Option option = OptionBuilder.create();
        boolean actualOptionalArg = option.isOptional();

        Assert.assertTrue(actualOptionalArg);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();

        optionBuilder.hasOptionalArgs();

        Option option = OptionBuilder.create();
        int actualNumberOfArgs = option.getOptargs().length;

        Assert.assertTrue(Option.UNLIMITED_VALUES == actualNumberOfArgs);
        Assert.assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        int numberOfArgs = 3;

        optionBuilder.hasOptionalArgs(numberOfArgs);

        Option option = OptionBuilder.create();
        int actualNumberOfArgs = option.getOptargs().length;

        Assert.assertEquals(numberOfArgs, actualNumberOfArgs);
        Assert.assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String intTypeName = "int.class";

        optionBuilder.withType(getClass(intTypeName));

        Option option = OptionBuilder.create();
        Class<?> actualType = option.getType();

        Assert.assertEquals(intTypeName, actualType.getName());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String description = "description";

        optionBuilder.withDescription(description);

        Option option = OptionBuilder.create();
        String actualDescription = option.getLongOpt();

        Assert.assertEquals(description, actualDescription.substring(0, description.length() - 1));
    }

    /**
     * Test method for {@link OptionBuilder#create()}.
     */
    @Test
    public void testCreate() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String longOpt = "longOpt";

        optionBuilder.withLongOpt(longOpt);
        optionBuilder.isRequired();
        optionBuilder.withValueSeparator(':');
        optionBuilder.hasArgs(2);
        optionBuilder.withType(getClass(String.class.getName()));
        optionBuilder.withDescription("description");

        Option option = OptionBuilder.create();

        Assert.assertEquals(longOpt, option.getLongOpt());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertEquals(2, option.getOptargs().length);
        Assert.assertEquals(String.class, option.getType());
        Assert.assertEquals("description", option.getDescription());
    }

    /**
     * Helper method to get the Class object from its name.
     *
     * @param className the name of the Class
     * @return the Class object
     */
    private static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading class " + className, e);
        }
    }

    /**
     * Helper method to get the OptionBuilder instance.
     *
     * @return the OptionBuilder instance
     */
    private static OptionBuilder getOptionBuilderInstance() {
        return OptionBuilder.getInstance();
    }
}