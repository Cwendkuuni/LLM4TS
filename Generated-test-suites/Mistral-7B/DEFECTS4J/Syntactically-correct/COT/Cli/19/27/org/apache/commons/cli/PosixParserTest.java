package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        // Given
        String[] args = {"--foo", "--bar=baz", "-a", "-b", "-c", "--", "-d", "e", "f"};
        Options options = new Options();
        options.addOption("a", false, null, "Option A");
        options.addOption("b", true, "valueB", "Option B");
        options.addOption("c", true, "valueC", "Option C");
        options.addOption("d", true, "valueD", "Option D");
        options.addOption("e", false, null, "Option E");
        options.addOption("foo", true, null, "Option FOO");
        options.addOption("bar", true, "value", "Option BAR");

        PosixParser parser = new PosixParser();

        // When
        String[] parsedArgs = parser.flatten(options, args, false);

        // Then
        Assert.assertArrayEquals(new String[]{"--foo", "--bar=value", "-a", "-b", "-c", "--", "-d", "e", "f"}, parsedArgs);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        // Given
        String[] args = {"--foo", "--bar=baz", "-a", "-b", "-c", "--", "-d", "e", "f"};
        Options options = new Options();
        options.addOption("a", false, null, "Option A");
        options.addOption("b", true, "valueB", "Option B");
        options.addOption("c", true, "valueC", "Option C");

        PosixParser parser = new PosixParser();

        // When
        String[] parsedArgs = parser.flatten(options, args, true);

        // Then
        Assert.assertArrayEquals(new String[]{"--foo", "-b", "-c", "--", "-d", "e", "f"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        // Given
        String[] args = {"--foo", "--bar=baz", "-a", "-b", "-c", "--", "-d", "e", "f", "--invalid"};
        Options options = new Options();
        options.addOption("a", false, null, "Option A");
        options.addOption("b", true, "valueB", "Option B");
        options.addOption("c", true, "valueC", "Option C");
        options.addOption("d", true, "valueD", "Option D");

        PosixParser parser = new PosixParser();

        // When & Then
        parser.flatten(options, args, false);

        Assert.fail("Expected ParseException but it wasn't thrown.");
    }

    @Test
    public void testBurstToken() {
        // Given
        String token = "--abc-def";
        PosixParser parser = new PosixParser();

        // When
        String[] parsedArgs = parser.burstToken(token, false);

        // Then
        Assert.assertArrayEquals(new String[]{"--abc", "-d", "e", "f"}, parsedArgs);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        // Given
        String token = "--abc-def";
        PosixParser parser = new PosixParser();

        // When
        String[] parsedArgs = parser.burstToken(token, true);

        // Then
        Assert.assertArrayEquals(new String[]{"--abc--", "d", "e", "f"}, parsedArgs);
    }

    @Test
    public void testBurstTokenWithEmptyToken() {
        // Given
        String token = "--";
        PosixParser parser = new PosixParser();

        // When
        String[] parsedArgs = parser.burstToken(token, false);

        // Then
        Assert.assertArrayEquals(new String[]{"--"}, parsedArgs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenWithEmptyInput() {
        // Given
        String token = "";
        PosixParser parser = new PosixParser();

        // When & Then
        parser.burstToken(token, false);

        Assert.fail("Expected IllegalArgumentException but it wasn't thrown.");
    }
}