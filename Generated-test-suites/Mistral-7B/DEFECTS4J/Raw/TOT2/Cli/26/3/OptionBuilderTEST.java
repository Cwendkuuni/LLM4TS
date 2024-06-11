package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String newLongopt = "testLongOpt";
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance().withLongOpt(newLongopt);
        assertEquals(expectedOptionBuilder, optionBuilder.withLongOpt(newLongopt));
    }

    // Test cases for other methods

    // Testing when hasArg() is called multiple times
    @Test
    public void testHasArgMultipleTimes() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasArg();
        optionBuilder.hasArg();
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance().hasArg().hasArg();
        assertEquals(expectedOptionBuilder, optionBuilder);
    }

    // Testing with empty strings for arguments
    @Test
    public void testHasArgEmptyString() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String argName = "";
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance().withArgName(argName);
        assertEquals(expectedOptionBuilder, optionBuilder.withArgName(argName));
    }

    // Testing for invalid input types in withType(Object newType)
    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeInvalidInputType() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        String wrongType = "String";
        optionBuilder.withType(wrongType);
    }

    // Testing when isRequired() is called after hasArg() or hasOptionalArg()
    @Test(expected = IllegalArgumentException.class)
    public void testIsRequiredAfterHasArgOrHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasArg();
        optionBuilder.isRequired();
    }

    // Testing when hasOptionalArgs() is called multiple times with different numbers
    @Test
    public void testHasOptionalArgsWithDifferentNumbers() {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasOptionalArgs(2);
        optionBuilder.hasOptionalArgs(3);
        OptionBuilder expectedOptionBuilder = OptionBuilder.getInstance().hasOptionalArgs(2).hasOptionalArgs(3);
        assertEquals(expectedOptionBuilder, optionBuilder);
    }
}