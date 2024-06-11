package org.apache.commons.cli; // for importing Option class

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

/**
 * Test cases for OptionBuilder class methods.
 */
public class OptionBuilderTest
{
    

    // Test Method: withLongOpt(String newLongopt)
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String newLongopt = "testLongOpt";
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.withLongOpt(newLongopt);

        OptionBuilder actualOptionBuilder = optionBuilder.withLongOpt(newLongopt);
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
    }

    // Test Method: hasArg()
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasArg();

        OptionBuilder actualOptionBuilder = optionBuilder.hasArg();
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(Option.UNINITIALIZED, actualOptionBuilder.numberOfArgs());

        OptionBuilder optionBuilderWithArgName = OptionBuilder.getInstance();
        String argName = "testArgName";
        OptionBuilder expectedOptionBuilderWithArgName = OptionBuilder.getInstance();
        expectedOptionBuilderWithArgName.hasArg().withArgName(argName);

        OptionBuilder actualOptionBuilderWithArgName = optionBuilderWithArgName.hasArg().withArgName(argName);
        assertEquals(expectedOptionBuilderWithArgName, actualOptionBuilderWithArgName);
        assertEquals(argName, actualOptionBuilderWithArgName.argName());
        assertEquals(1, actualOptionBuilderWithArgName.numberOfArgs());
    }

    // Test Method: isRequired()
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.isRequired();

        OptionBuilder actualOptionBuilder = optionBuilder.isRequired();
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertFalse(actualOptionBuilder.isOptionalArg());

        OptionBuilder optionBuilderWithOptionalArg = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilderWithOptionalArg = OptionBuilder.getInstance();
        expectedOptionBuilderWithOptionalArg.isRequired(false);

        OptionBuilder actualOptionBuilderWithOptionalArg = optionBuilderWithOptionalArg.isRequired(false);
        assertEquals(expectedOptionBuilderWithOptionalArg, actualOptionBuilderWithOptionalArg);
        assertTrue(actualOptionBuilderWithOptionalArg.isOptionalArg());
    }

    // Test Method: withValueSeparator(char sep)
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        char sep = ':';
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.withValueSeparator(sep);

        OptionBuilder actualOptionBuilder = optionBuilder.withValueSeparator(sep);
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(sep, actualOptionBuilder.getValueSeparator());

        OptionBuilder optionBuilderWithValueSeparator = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilderWithValueSeparator = OptionBuilder.getInstance();
        expectedOptionBuilderWithValueSeparator.withValueSeparator();

        OptionBuilder actualOptionBuilderWithValueSeparator = optionBuilderWithValueSeparator.withValueSeparator();
        assertEquals(expectedOptionBuilderWithValueSeparator, actualOptionBuilderWithValueSeparator);
        assertEquals('=', actualOptionBuilderWithValueSeparator.getValueSeparator());
    }

    // Test Method: isRequired(boolean newRequired)
    @Test
    public void testIsRequiredWithBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        boolean newRequired = false;
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.isRequired(newRequired);

        OptionBuilder actualOptionBuilder = optionBuilder.isRequired(newRequired);
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertFalse(actualOptionBuilder.isRequired());
        assertTrue(actualOptionBuilder.isOptionalArg());

        OptionBuilder optionBuilderWithRequired = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilderWithRequired = OptionBuilder.getInstance();
        expectedOptionBuilderWithRequired.isRequired(true);

        OptionBuilder actualOptionBuilderWithRequired = optionBuilderWithRequired.isRequired(true);
        assertEquals(expectedOptionBuilderWithRequired, actualOptionBuilderWithRequired);
        assertFalse(actualOptionBuilderWithRequired.isOptionalArg());
    }

    // Test Method: hasArgs()
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasArgs();

        OptionBuilder actualOptionBuilder = optionBuilder.hasArgs();
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, actualOptionBuilder.numberOfArgs());

        OptionBuilder optionBuilderWithNumArgs = OptionBuilder.getInstance();
        int numArgs = 3;
        OptionBuilder expectedOptionBuilderWithNumArgs = OptionBuilder.getInstance();
        expectedOptionBuilderWithNumArgs.hasArgs(numArgs);

        OptionBuilder actualOptionBuilderWithNumArgs = optionBuilderWithNumArgs.hasArgs(numArgs);
        assertEquals(expectedOptionBuilderWithNumArgs, actualOptionBuilderWithNumArgs);
        assertEquals(numArgs, actualOptionBuilderWithNumArgs.numberOfArgs());
    }

    // Test Method: hasArgs(int num)
    @Test
    public void testHasArgsWithInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        int num = 5;
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasArgs(num);

        OptionBuilder actualOptionBuilder = optionBuilder.hasArgs(num);
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(num, actualOptionBuilder.numberOfArgs());
    }

    // Test Method: hasOptionalArg()
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasOptionalArg();

        OptionBuilder actualOptionBuilder = optionBuilder.hasOptionalArg();
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertTrue(actualOptionBuilder.isOptionalArg());
        assertEquals(Option.UNINITIALIZED, actualOptionBuilder.numberOfArgs());

        OptionBuilder optionBuilderWithOptionalArgName = OptionBuilder.getInstance();
        String argName = "testArgName";
        OptionBuilder expectedOptionBuilderWithOptionalArgName = OptionBuilder.getInstance();
        expectedOptionBuilderWithOptionalArgName.hasArg().withArgName(argName).hasOptionalArg();

        OptionBuilder actualOptionBuilderWithOptionalArgName = optionBuilderWithOptionalArgName.hasArg().withArgName(argName).hasOptionalArg();
        assertEquals(expectedOptionBuilderWithOptionalArgName, actualOptionBuilderWithOptionalArgName);
        assertTrue(actualOptionBuilderWithOptionalArgName.isOptionalArg());
        assertEquals(argName, actualOptionBuilderWithOptionalArgName.argName());
        assertEquals(1, actualOptionBuilderWithOptionalArgName.numberOfArgs());
    }

    // Test Method: hasOptionalArgs()
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasOptionalArgs();

        OptionBuilder actualOptionBuilder = optionBuilder.hasOptionalArgs();
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, actualOptionBuilder.numberOfArgs());
        assertTrue(actualOptionBuilder.isOptionalArg());

        OptionBuilder optionBuilderWithNumOptionalArgs = OptionBuilder.getInstance();
        int numOptionalArgs = 4;
        OptionBuilder expectedOptionBuilderWithNumOptionalArgs = OptionBuilder.getInstance();
        expectedOptionBuilderWithNumOptionalArgs.hasOptionalArgs(numOptionalArgs);

        OptionBuilder actualOptionBuilderWithNumOptionalArgs = optionBuilderWithNumOptionalArgs.hasOptionalArgs(numOptionalArgs);
        assertEquals(expectedOptionBuilderWithNumOptionalArgs, actualOptionBuilderWithNumOptionalArgs);
        assertEquals(numOptionalArgs, actualOptionBuilderWithNumOptionalArgs.numberOfArgs());
        assertTrue(actualOptionBuilderWithNumOptionalArgs.isOptionalArg());
    }

    // Test Method: hasOptionalArgs(int numArgs)
    @Test
    public void testHasOptionalArgsWithInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        int numArgs = 2;
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.hasOptionalArgs(numArgs);

        OptionBuilder actualOptionBuilder = optionBuilder.hasOptionalArgs(numArgs);
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(numArgs, actualOptionBuilder.numberOfArgs());
        assertTrue(actualOptionBuilder.isOptionalArg());
    }

    // Test Method: withType(Class newType)
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Class newType = String.class;
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.withType(newType);

        OptionBuilder actualOptionBuilder = optionBuilder.withType(newType);
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(newType, actualOptionBuilder.getType());

        Option option = OptionBuilder.create('T');
        Option expectedOption = new Option("testOption", "testDescription", true, "testLongOpt");
        Option actualOption = optionBuilder.withType(newType).create('T');

        assertEquals(expectedOption.getClass(), actualOption.getClass());
        assertEquals(expectedOption.getType(), actualOption.getType());
    }

    // Test Method: withDescription(String newDescription)
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String newDescription = "testDescription";
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance();
        expectedOptionBuilder.withDescription(newDescription);

        OptionBuilder actualOptionBuilder = optionBuilder.withDescription(newDescription);
        assertEquals(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(newDescription, actualOptionBuilder.getDescription());

        Option option = OptionBuilder.create('T');
        Option expectedOption = new Option("testOption", "testDescription", true, "testLongOpt");
        Option actualOption = optionBuilder.withDescription(newDescription).create('T');

        assertEquals(expectedOption.getDescription(), actualOption.getDescription());
        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
    }

    // Test Method: create(char opt)
    @Test
    public void testCreateWithChar()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option option = OptionBuilder.create('T');
        Option expectedOption = new Option("testOption", null, true, "testLongOpt");

        assertEquals(expectedOption, option);
    }

    // Test Method: create()
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        Option option = OptionBuilder.create();
    }

    
}