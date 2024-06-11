package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        String longOpt = optionBuilder.getLongOpt();
        Assert.assertEquals("optionLong", longOpt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        Assert.assertEquals(1, numberOfArgs);
    }

    @Test
    public void testHasArg_WithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        String argName = optionBuilder.getArgName();
        Assert.assertEquals("argName", argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        boolean required = optionBuilder.isRequired();
        Assert.assertTrue(required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        char valueSeparator = optionBuilder.getValueSeparator();
        Assert.assertEquals(':', valueSeparator);
    }

    @Test
    public void testWithValueSeparator_NoArg() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        char valueSeparator = optionBuilder.getValueSeparator();
        Assert.assertEquals('=', valueSeparator);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, numberOfArgs);
    }

    @Test
    public void testHasArgs_WithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        Assert.assertEquals(5, numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        Assert.assertEquals(1, numberOfArgs);
        boolean optionalArg = optionBuilder.isOptionalArg();
        Assert.assertTrue(optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, numberOfArgs);
        boolean optionalArg = optionBuilder.isOptionalArg();
        Assert.assertTrue(optionalArg);
    }

    @Test
    public void testHasOptionalArgs_WithNum() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        int numberOfArgs = optionBuilder.getNumberOfArgs();
        Assert.assertEquals(3, numberOfArgs);
        boolean optionalArg = optionBuilder.isOptionalArg();
        Assert.assertTrue(optionalArg);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        Class type = optionBuilder.getType();
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Test Option Description");
        String description = optionBuilder.getDescription();
        Assert.assertEquals("Test Option Description", description);
    }

    @Test
    public void testCreate_ArgumentException() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
                .withDescription("Test Option Description")
                .isRequired()
                .hasArg()
                .withArgName("argName")
                .hasOptionalArg();

        try {
            optionBuilder.create('A');
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testCreate() throws Exception {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong")
                .withDescription("Test Option Description")
                .isRequired()
                .hasArg()
                .withArgName("argName")
                .hasOptionalArg();

        Option option = optionBuilder.create('O');

        Assert.assertEquals("optionLong", option.getLongOpt());
        Assert.assertEquals("Test Option Description", option.getDescription());
        Assert.assertTrue(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertTrue(option.isOptionalArg());
    }

    private void reflectivelyInvokeMethod(Object obj, String methodName, Object... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = obj.getClass().getMethod(methodName, args.getClass());
        method.invoke(obj, args);
    }

    @Test
    public void testReset() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option option = OptionBuilder.create('O');

        reflectivelyInvokeMethod(optionBuilder, "reset");

        String longOpt = optionBuilder.getLongOpt();
        Assert.assertNull(longOpt);
    }

    
}