package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Tests for the PosixParser class.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 3, "Option C with argument");
        options.addOption("d", 4, "Option D with argument");

        String[] args = {"--a", "--b=2", "--c=Hello", "--d=World", "-a", "-c", "-d", "-e"};
        String[] expected = {"--a", "-a", "--b", "-b", "--c", "-c", "--d", "-d", "--", "Hello", "World", "-e"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        Assert.assertArrayEquals(expected, result);

        // Test with stopAtNonOption = true
        String[] result2 = parser.flatten(options, args, true);
        String[] expected2 = {"--a", "-a", "--b", "-b=2", "--c", "-c", "--d", "-d", "--", "Hello", "World", "-e", "-e"};

        Assert.assertArrayEquals(expected2, result2);
    }
}