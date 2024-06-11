package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    @Test
    public void testFlatten_withValidOptions_returnsFlattenedArguments() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option A"));
        options.addOption(new Option("b", "beta", false, "Option B"));
        options.addOption(new Option("c", "charlie", true, "Option C"));

        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "-c", "value1", "arg1", "-b", "--"};
        String[] expectedFlattenedArguments = {"-a", "-c", "value1", "arg1", "-b"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedFlattenedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_withNonOptionStop_returnsFlattenedArgumentsUntilNonOption() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option A"));
        options.addOption(new Option("b", "beta", false, "Option B"));
        options.addOption(new Option("c", "charlie", true, "Option C"));

        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "arg1", "-b", "--", "arg2"};
        String[] expectedFlattenedArguments = {"-a", "arg1", "-b", "--"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedFlattenedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_withMultipleHyphens_returnsFlattenedArgumentsWithHyphens() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option A"));
        options.addOption(new Option("b", "beta", false, "Option B"));
        options.addOption(new Option("c", "charlie", true, "Option C"));

        GnuParser parser = new GnuParser();
        String[] arguments = {"-", "--", "-a"};
        String[] expectedFlattenedArguments = {"-", "--", "-a"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedFlattenedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_withOptionAndNonOption_returnsFlattenedArgumentsWithBoth() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option A"));
        options.addOption(new Option("b", "beta", false, "Option B"));

        GnuParser parser = new GnuParser();
        String[] arguments = {"-a", "-nonOption", "-b"};
        String[] expectedFlattenedArguments = {"-a", "-nonOption", "-b"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedFlattenedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_withOptionAbbreviation_returnsFlattenedArgumentsWithAbbreviatedOption() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option A"));
        options.addOption(new Option("b", "beta", false, "Option B"));

        GnuParser parser = new GnuParser();
        String[] arguments = {"-al"};
        String[] expectedFlattenedArguments = {"-a"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedFlattenedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_withOptionAbbreviationAndValue_returnsFlattenedArgumentsWithAbbreviatedOptionAndValue() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", true, "Option A"));

        GnuParser parser = new GnuParser();
        String[] arguments = {"-al=test"};
        String[] expectedFlattenedArguments = {"-a", "test"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedFlattenedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_withEmptyArguments_returnsEmptyArray() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option A"));

        GnuParser parser = new GnuParser();
        String[] arguments = {};
        String[] expectedFlattenedArguments = {};
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedFlattenedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_withDashSeparator_returnsFlattenedArgumentsWithDashSeparator() {
        Options options = new Options();
        options.addOption(new Option("a", "alpha", false, "Option A"));

        GnuParser parser = new GnuParser();
        String[] arguments = {"--", "arg1", "-a"};
        String[] expectedFlattenedArguments = {"--", "arg1", "-a"};
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        assertArrayEquals(expectedFlattenedArguments, flattenedArguments);
    }
}

