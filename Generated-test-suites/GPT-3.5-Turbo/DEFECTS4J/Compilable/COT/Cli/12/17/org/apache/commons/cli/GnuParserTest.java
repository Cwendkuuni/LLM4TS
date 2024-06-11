package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-option1", "value1", "--option2=value2", "arg1", "-arg2"};

        String[] result = parser.flatten(options, arguments, true);

        // Assert that the result contains the expected flattened arguments
        Assert.assertArrayEquals(new String[]{"-option1", "value1", "--option2=value2", "arg1", "-arg2"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionFalse() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-option1", "value1", "--option2=value2", "arg1", "-arg2"};

        String[] result = parser.flatten(options, arguments, false);

        // Assert that the result contains the expected flattened arguments
        Assert.assertArrayEquals(new String[]{"-option1", "value1", "--option2=value2", "arg1", "-arg2"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOptionTrue() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-option1", "value1", "--option2=value2", "arg1", "-arg2"};

        String[] result = parser.flatten(options, arguments, true);

        // Assert that the result contains the expected flattened arguments
        Assert.assertArrayEquals(new String[]{"-option1", "value1", "--option2=value2", "arg1", "-arg2"}, result);
    }

    @Test
    public void testFlattenWithDoubleDashSeparator() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-option1", "--", "--option2=value2", "arg1", "-arg2"};

        String[] result = parser.flatten(options, arguments, true);

        // Assert that the result contains the expected flattened arguments
        Assert.assertArrayEquals(new String[]{"-option1", "--", "--option2=value2", "arg1", "-arg2"}, result);
    }

    @Test
    public void testFlattenWithSingleDashSeparator() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"-option1", "-", "--option2=value2", "arg1", "-arg2"};

        String[] result = parser.flatten(options, arguments, true);

        // Assert that the result contains the expected flattened arguments
        Assert.assertArrayEquals(new String[]{"-option1", "-", "--option2=value2", "arg1", "-arg2"}, result);
    }

    @Test
    public void testFlattenWithShortOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("opt", true, "Option description");
        String[] arguments = {"-opt", "value"};

        String[] result = parser.flatten(options, arguments, true);

        // Assert that the result contains the expected flattened arguments
        Assert.assertArrayEquals(new String[]{"-opt", "value"}, result);
    }

    @Test
    public void testFlattenWithShortOptionAndEqualsSign() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("opt", true, "Option description");
        String[] arguments = {"-opt=value"};

        String[] result = parser.flatten(options, arguments, true);

        // Assert that the result contains the expected flattened arguments
        Assert.assertArrayEquals(new String[]{"-opt=value"}, result);
    }

    @Test
    public void testFlattenWithLongOption() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("option", true, "Option description");
        String[] arguments = {"--option", "value"};

        String[] result = parser.flatten(options, arguments, true);

        // Assert that the result contains the expected flattened arguments
        Assert.assertArrayEquals(new String[]{"--option", "value"}, result);
    }

    @Test
    public void testFlattenWithLongOptionAndEqualsSign() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("option", true, "Option description");
        String[] arguments = {"--option=value"};

        String[] result = parser.flatten(options, arguments, true);

        // Assert that the result contains the expected flattened arguments
        Assert.assertArrayEquals(new String[]{"--option=value"}, result);
    }

    @Test
    public void testFlattenWithNonOptionArgument() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        String[] arguments = {"arg1"};

        String[] result = parser.flatten(options, arguments, true);

        // Assert that the result contains the expected flattened arguments
        Assert.assertArrayEquals(new String[]{"arg1"}, result);
    }
}

