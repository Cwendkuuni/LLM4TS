package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for GnuParser.
 */
public class GnuParserTest {

    @Test
    public void testFlattenSimple() {
        String[] args = {"-a", "--optionB", "-c", "d", "--optionD"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("d", true, "Option D");

        String[] expected = {"-a", "--optionB", "-c", "d", "--optionD"};
        String[] result = new GnuParser().flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionLongArg() {
        String[] args = {"--longOption"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        String[] expected = {"--longOption"};
        String[] result = new GnuParser().flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionShortArg() {
        String[] args = {"-o"};
        Options options = new Options();
        options.addOption("o", true, "Option O");

        String[] expected = {"-o"};
        String[] result = new GnuParser().flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSingleOptionWithValue() {
        String[] args = {"-a", "valueA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] expected = {"-a", "valueA"};
        String[] result = new GnuParser().flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptionsWithValue() {
        String[] args = {"-a", "valueA1", "-b", "valueB1", "-c", "valueC1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        String[] expected = {"-a", "valueA1", "-b", "valueB1", "-c", "valueC1"};
        String[] result = new GnuParser().flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOptionShort() {
        String[] args = {"-z"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        new GnuParser().flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOptionLong() {
        String[] args = {"--unknownOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        new GnuParser().flatten(options, args, false);
    }

    @Test
    public void testFlattenSpecialOption() {
        String[] args = {"-Dmyproperty=value"};
        Options options = new Options();

        String[] expected = {"-Dmyproperty", "value"};
        String[] result = new GnuParser().flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenSpecialOptionWithValue() {
        String[] args = {"-Dmyproperty=value1", "-Dmyotherproperty=value2"};
        Options options = new Options();

        String[] expected = {"-Dmyproperty", "value1", "-Dmyotherproperty", "value2"};
        String[] result = new GnuParser().flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"-a", "--optionB", "-c", "d", "--optionD", "e"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("d", true, "Option D");

        String[] expected = {"-a", "--optionB", "-c", "d"};
        String[] result = new GnuParser().flatten(options, args, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        String[] args = {"--", "-c", "d"};
        Options options = new Options();

        String[] expected = {"--", "-c", "d"};
        String[] result = new GnuParser().flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlattenDoubleDashWithArguments() {
        String[] args = {"--", "--optionB", "-c", "d"};
        Options options = new Options();
        options.addOption("b", false, "Option B");

        String[] expected = {"--optionB", "-c", "d"};
        String[] result = new GnuParser().flatten(options, args, false);

        assertArrayEquals(expected, result);
    }
}