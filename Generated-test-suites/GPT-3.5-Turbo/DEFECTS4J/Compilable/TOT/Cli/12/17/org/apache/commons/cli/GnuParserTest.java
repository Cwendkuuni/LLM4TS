package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten_ValidOptionsAndArguments_ReturnFlattenedArguments() {
        Options options = new Options();
        options.addOption("D", true, "Set a system property");
        options.addOption("f", false, "Flag option");

        String[] arguments = {"-Dproperty=value", "--flag"};

        GnuParser parser = new GnuParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        String[] expectedArguments = {"-Dproperty=value", "--flag"};
        Assert.assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_NoOptionsAndArguments_ReturnUnchangedArguments() {
        Options options = new Options();

        String[] arguments = {"arg1", "arg2"};

        GnuParser parser = new GnuParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(arguments, flattenedArguments);
    }

    @Test
    public void testFlatten_StopAtNonOption_FlagStopAtNonOptionTrue_ReturnFlattenedArgumentsWithNonOptionStop() {
        Options options = new Options();
        options.addOption("D", true, "Set a system property");
        options.addOption("f", false, "Flag option");

        String[] arguments = {"-Dproperty=value", "nonOption"};

        GnuParser parser = new GnuParser();
        String[] flattenedArguments = parser.flatten(options, arguments, true);

        String[] expectedArguments = {"-Dproperty=value", "nonOption"};
        Assert.assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_StopAtNonOption_FlagStopAtNonOptionFalse_ReturnFlattenedArgumentsWithoutNonOptionStop() {
        Options options = new Options();
        options.addOption("D", true, "Set a system property");
        options.addOption("f", false, "Flag option");

        String[] arguments = {"-Dproperty=value", "nonOption"};

        GnuParser parser = new GnuParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        String[] expectedArguments = {"-Dproperty=value", "nonOption"};
        Assert.assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_HyphenOption_ReturnFlattenedArgumentsWithHyphenOption() {
        Options options = new Options();
        options.addOption("D", true, "Set a system property");
        options.addOption("f", false, "Flag option");

        String[] arguments = {"-"};

        GnuParser parser = new GnuParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        String[] expectedArguments = {"-"};
        Assert.assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_DoubleHyphenOption_ReturnFlattenedArgumentsWithDoubleHyphenOption() {
        Options options = new Options();
        options.addOption("D", true, "Set a system property");
        options.addOption("f", false, "Flag option");

        String[] arguments = {"--"};

        GnuParser parser = new GnuParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        String[] expectedArguments = {"--"};
        Assert.assertArrayEquals(expectedArguments, flattenedArguments);
    }

    @Test
    public void testFlatten_InvalidOption_ReturnFlattenedArgumentsWithInvalidOption() {
        Options options = new Options();
        options.addOption("D", true, "Set a system property");
        options.addOption("f", false, "Flag option");

        String[] arguments = {"-X", "--unknown"};

        GnuParser parser = new GnuParser();
        String[] flattenedArguments = parser.flatten(options, arguments, false);

        String[] expectedArguments = {"-X", "--unknown"};
        Assert.assertArrayEquals(expectedArguments, flattenedArguments);
    }
}
