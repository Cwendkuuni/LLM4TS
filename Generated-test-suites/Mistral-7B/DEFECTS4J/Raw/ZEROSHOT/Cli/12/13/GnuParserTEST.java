package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for GnuParser.
 */
public class GnuParserTest {

    @Test
    public void testFlattenWithOptionsAndSingleArguments() {
        String[] args = {"-Dmyprop=value1", "-Dmyprop2=value2", "-foo", "-bar"};
        Options options = new Options();
        options.addOption("D", "myprop", true, "My property");
        options.addOption("f", "foo", true, "Foo option");
        options.addOption("b", "bar", true, "Bar option");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"-D", "myprop=value1", "-D", "myprop2=value2", "-f", "foo", "-b", "bar"}, result);
    }

    @Test
    public void testFlattenWithOptionsAndMultipleArguments() {
        String[] args = {"--myprop=value1=value2", "-Dmyprop=value3", "-foo", "-bar", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("D", "myprop", true, "My property");
        options.addOption("f", "foo", true, "Foo option");
        options.addOption("b", "bar", true, "Bar option");
        options.addOption("m", "myprop", true, "My property with multiple values");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--", "myprop=value1=value2", "-D", "myprop=value3", "-f", "foo", "-b", "bar", "arg1", "arg2"}, result);
    }

    @Test
    public void testFlattenWithLongOptions() {
        String[] args = {"--long-option", "--long-option2", "--long-option=value1", "--long-option=value2", "-foo"};
        Options options = new Options();
        options.addOption("l", "long-option", true, "Long option");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--long-option", "--long-option", "--long-option=value1", "--long-option=value2", "-f", "foo"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"-Dmyprop=value1", "-Dmyprop=value2", "-foo", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("D", "myprop", true, "My property");
        options.addOption("f", "foo", true, "Foo option");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"-D", "myprop=value1", "-D", "myprop=value2"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() {
        String[] args = {"-invalid", "-Dmyprop=value1", "-Dmyprop=value2"};
        Options options = new Options();
        options.addOption("D", "myprop", true, "My property");

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);

        fail("ParseException expected");
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithEmptyArgument() {
        String[] args = {"-foo", "", "-bar"};
        Options options = new Options();
        options.addOption("f", "foo", true, "Foo option");
        options.addOption("b", "bar", true, "Bar option");

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);

        fail("ParseException expected");
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithMultiValueOptionWithEmptyValue() {
        String[] args = {"--myprop=value1,", "--myprop=value2"};
        Options options = new Options();
        options.addOption("m", "myprop", true, "My property with multiple values");

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);

        fail("ParseException expected");
    }
}