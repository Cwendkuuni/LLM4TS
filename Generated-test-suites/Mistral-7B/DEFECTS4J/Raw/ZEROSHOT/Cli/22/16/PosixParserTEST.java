package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Test case 1: Simple options
        String[] args1 = {"--foo", "--bar=baz", "--baz=qux"};
        Options options1 = new Options();
        options1.addOption("foo", true, false, "Foo option");
        options1.addOption("bar", true, true, "Bar option with argument");
        options1.addOption("baz", false, false, "Baz option");

        PosixParser parser1 = new PosixParser();
        String[] result1 = parser1.flatten(options1, args1, false);
        Assert.assertArrayEquals(new String[]{"--foo", "--bar=baz", "--baz"}, result1);

        // Test case 2: Long options with arguments
        String[] args2 = {"--long-option=value", "--other-option=another-value"};
        Options options2 = new Options();
        options2.addOption("long-option", true, true, "Long option with argument");
        options2.addOption("other-option", true, true, "Other option with argument");

        PosixParser parser2 = new PosixParser();
        String[] result2 = parser2.flatten(options2, args2, false);
        Assert.assertArrayEquals(new String[]{"--long-option=value", "--other-option=another-value"}, result2);

        // Test case 3: Short options
        String[] args3 = {"-f", "--foo", "-b", "--bar=baz", "-z"};
        Options options3 = new Options();
        options3.addOption("f", true, false, "Foo option");
        options3.addOption("b", false, false, "Baz option");

        PosixParser parser3 = new PosixParser();
        String[] result3 = parser3.flatten(options3, args3, false);
        Assert.assertArrayEquals(new String[]{"-f", "--foo", "-b", "--bar=baz"}, result3);

        // Test case 4: Bursting
        String[] args4 = {"-fobar", "-abc", "--foo=value", "-d", "--long-option=value"};
        Options options4 = new Options();
        options4.addOption("f", false, false, "Foo option");
        options4.addOption("o", true, false, "Option o");
        options4.addOption("bar", false, false, "Bar option");
        options4.addOption("a", true, true, "Option a with argument");
        options4.addOption("c", true, true, "Option c with argument");
        options4.addOption("long-option", true, true, "Long option with argument");

        PosixParser parser4 = new PosixParser();
        String[] result4 = parser4.flatten(options4, args4, false);
        Assert.assertArrayEquals(new String[]{"-f", "-o", "--foo=value", "-a", "--c=", "--", "bar", "--long-option=value"}, result4);

        // Test case 5: Stop at non-option
        String[] args5 = {"-fobar", "-abc", "--foo=value", "-d"};
        Options options5 = new Options();
        options5.addOption("f", false, false, "Foo option");
        options5.addOption("o", true, false, "Option o");
        options5.addOption("bar", false, false, "Bar option");

        PosixParser parser5 = new PosixParser();
        String[] result5 = parser5.flatten(options5, args5, true);
        Assert.assertArrayEquals(new String[]{"-fobar", "-abc", "--foo=value", "-d"}, result5);
    }

}