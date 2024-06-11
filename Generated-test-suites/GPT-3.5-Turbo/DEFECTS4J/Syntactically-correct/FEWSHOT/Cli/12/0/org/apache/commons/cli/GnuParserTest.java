package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");

        GnuParser parser = new GnuParser();

        // Test with no arguments
        String[] emptyArguments = {};
        assertArrayEquals(emptyArguments, parser.flatten(options, emptyArguments, false));

        // Test with arguments containing options
        String[] argumentsWithOptions = {"-a", "--optionB=value"};
        assertArrayEquals(argumentsWithOptions, parser.flatten(options, argumentsWithOptions, false));

        // Test with arguments containing non-option values
        String[] argumentsWithValues = {"value1", "value2"};
        assertArrayEquals(argumentsWithValues, parser.flatten(options, argumentsWithValues, true));

        // Test with arguments containing stopAtNonOption=false
        String[] argumentsWithStopAtNonOptionFalse = {"-a", "value1", "-b", "--optionB=value2", "value3"};
        String[] expectedArgumentsWithStopAtNonOptionFalse = {"-a", "value1", "-b", "--optionB=value2", "value3"};
        assertArrayEquals(expectedArgumentsWithStopAtNonOptionFalse, parser.flatten(options, argumentsWithStopAtNonOptionFalse, false));

        // Test with arguments containing stopAtNonOption=true
        String[] argumentsWithStopAtNonOptionTrue = {"-a", "--optionB=value1", "value2", "value3"};
        String[] expectedArgumentsWithStopAtNonOptionTrue = {"-a", "--optionB=value1", "value2", "value3"};
        assertArrayEquals(expectedArgumentsWithStopAtNonOptionTrue, parser.flatten(options, argumentsWithStopAtNonOptionTrue, true));
    }
}