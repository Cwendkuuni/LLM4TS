package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test class for {@link GnuParser} class.
 */
public class GnuParserTest {

    @Test
    public void testFlatten() {
        String[] arguments = {
                "--foo", "1", "--bar=2", "-Dproperty=value", "--", "-b", "-c", "-d"};

        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option");
        options.addOption("D", true, "Define property");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("1");
        expected.add("--bar=2");
        expected.add("-D");
        expected.add("property");
        expected.add("value");
        expected.add("--");
        expected.add("-b");
        expected.add("-c");
        expected.add("-d");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] arguments = {
                "--foo", "1", "--bar=2", "-Dproperty=value", "--", "arg", "-b", "-c", "-d"};

        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option");
        options.addOption("D", true, "Define property");
        options.addOption("b", false, "Option b");
        options.addOption("c", false, "Option c");

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, true);

        List<String> expected = new ArrayList<>();
        expected.add("--foo");
        expected.add("1");
        expected.add("--bar=2");
        expected.add("-D");
        expected.add("property");
        expected.add("value");
        expected.add("--");

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);

        // Check that the remaining arguments are not added to the result
        Assert.assertArrayNotEquals(new String[]{"arg", "-b", "-c", "-d"}, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        String[] arguments = {"--invalid-option", "arg1", "arg2"};

        Options options = new Options();
        GnuParser parser = new GnuParser();

        parser.flatten(options, arguments, false);
    }
}