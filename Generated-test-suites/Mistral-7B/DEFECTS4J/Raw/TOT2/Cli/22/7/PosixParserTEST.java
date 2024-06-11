package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Given
        String[] args = new String[]{"--foo", "--bar=baz", "-abc", "-d", "--def=ghi"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option with argument");
        options.addOption("abc", false, "Abc option");
        options.addOption("d", false, "D option");
        options.addOption("def", true, "Def option with argument");

        PosixParser parser = new PosixParser();
        String[] expected = new String[]{"--foo", "-", "foo", "--bar", "baz", "-abc", "-d", "--def", "ghi"};

        // When
        String[] actual = parser.flatten(options, args, false);

        // Then
        Assert.assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithNullOptions() {
        PosixParser parser = new PosixParser();
        String[] args = new String[]{"--foo", "--bar"};
        Options options = null;

        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithNullArguments() {
        PosixParser parser = new PosixParser();
        String[] args = null;
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithEmptyArguments() {
        PosixParser parser = new PosixParser();
        String[] args = new String[0];
        Options options = new Options();

        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        // Given
        String[] args = new String[]{"--foo", "--bar=baz", "-abc", "--", "def=ghi"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option with argument");
        options.addOption("abc", false, "Abc option");
        options.addOption("def", true, "Def option with argument");

        PosixParser parser = new PosixParser();
        String[] expected = new String[]{"--foo", "--", "foo", "--bar=baz", "def=ghi"};

        // When
        String[] actual = parser.flatten(options, args, true);

        // Then
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();
        String token = "--foo-bar-baz";

        // When
        String[] actual = parser.burstToken(token, false);

        // Then
        String[] expected = new String[]{"--foo", "-", "bar", "-", "baz"};
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        String token = "--foo";

        parser.processOptionToken(token, false);

        List<String> tokens = parser.getTokens();
        Assert.assertEquals("--foo", tokens.get(0));
        Assert.assertEquals("-", tokens.get(1));
        Assert.assertEquals("foo", tokens.get(2));
    }

    @Test
    public void testProcessNonOptionToken() {
        PosixParser parser = new PosixParser();
        String token = "arg";

        parser.processNonOptionToken(token);

        List<String> tokens = parser.getTokens();
        Assert.assertEquals("--", tokens.get(0));
        Assert.assertEquals("arg", tokens.get(1));
    }
}