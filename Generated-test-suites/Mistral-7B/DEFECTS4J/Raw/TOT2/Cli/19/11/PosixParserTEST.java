package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test the {@link PosixParser} class.
 */
public class PosixParserTest {

    @Test
    public void testFlatten() throws Exception {
        // Given
        String[] args = {"--foo", "-o", "out", "foo", "--bar=baz", "-a", "-b", "--"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option");
        options.addOption("o", "o", "Output option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        // When & Then
        assertArrayEquals(new String[]{"--foo", "-o", "out", "-o", "foo", "--bar=baz", "-a", "-b", "--"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws Exception {
        // Given
        String[] args = {"--invalid"};
        Options options = new Options();

        PosixParser parser = new PosixParser();

        // When & Then
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws Exception {
        // Given
        String[] args = {"--foo", "-o", "out", "foo", "--bar=baz", "-a", "-b", "--", "nonOption"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option");
        options.addOption("o", "o", "Output option");

        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, args, true);

        // Then
        assertArrayEquals(new String[]{"--foo", "-o", "out", "foo", "--", "nonOption"}, result);
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testEmptyFlatten() throws Exception {
        // Given
        String[] args = {};
        Options options = new Options();

        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, args, false);

        // Then
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlattenWithOnlySpecialTokens() throws Exception {
        // Given
        String[] args = {"--", "--foo="};
        Options options = new Options();

        PosixParser parser = new PosixParser();

        // When
        String[] result = parser.flatten(options, args, false);

        // Then
        assertArrayEquals(new String[]{"--", "--foo="}, result);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "bar"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");

        parser.init();

        parser.flatten(options, args, false);

        assertSame(options.getOption("foo"), parser.currentOption);
        assertNull(parser.currentOption);
        assertEquals(3, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("bar", parser.tokens.get(1));
        assertEquals("foo", parser.tokens.get(2));
    }

    @Test
    public void testProcessWithValue() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "bar"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");

        parser.init();

        parser.flatten(options, args, false);

        assertSame(options.getOption("foo"), parser.currentOption);
        assertSame("bar", parser.currentOption.getValue());
        assertNull(parser.currentOption);
        assertEquals(3, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("bar", parser.tokens.get(1));
        assertEquals("foo", parser.tokens.get(2));
    }

    @Test
    public void testProcessWithNoCurrentOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "bar"};
        Options options = new Options();

        parser.init();

        parser.flatten(options, args, false);

        assertNull(parser.currentOption);
        assertEquals(3, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("bar", parser.tokens.get(1));
        assertEquals("foo", parser.tokens.get(2));
    }

    @Test
    public void testProcessWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "bar", "nonOption"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");

        parser.init();

        parser.flatten(options, args, true);

        assertNull(parser.currentOption);
        assertEquals(4, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("bar", parser.tokens.get(1));
        assertEquals("--", parser.tokens.get(2));
        assertEquals("nonOption", parser.tokens.get(3));
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testProcessWithTwoOptions() {
        PosixParser parser = new PosixParser();
        String[] args = {"--foo", "bar", "--bar", "baz"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option");

        parser.init();

        parser.flatten(options, args, false);

        assertSame(options.getOption("foo"), parser.currentOption);
        assertSame("bar", parser.currentOption.getValue());
        assertNull(parser.currentOption);
        assertEquals(5, parser.tokens.size());
        assertEquals("--foo", parser.tokens.get(0));
        assertEquals("bar", parser.tokens.get(1));
        assertEquals("--bar", parser.tokens.get(2));
        assertEquals("baz", parser.tokens.get(3));
        assertEquals("foo", parser.tokens.get(4));
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenWithInvalidOption() throws Exception {
        PosixParser parser = new PosixParser();

        parser.burstToken("foo", false);
    }

    @Test
    public void testBurstTokenWithValidOption() throws Exception {
        PosixParser parser = new PosixParser();

        parser.burstToken("-o", false);

        assertEquals(2, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("o", parser.tokens.get(1));
    }

    @Test
    public void testBurstTokenWithValidOptionWithValue() throws Exception {
        PosixParser parser = new PosixParser();

        parser.burstToken("-o=out", false);

        assertEquals(3, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("o", parser.tokens.get(1));
        assertEquals("out", parser.tokens.get(2));
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() throws Exception {
        PosixParser parser = new PosixParser();

        parser.burstToken("-o=out", true);

        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("out", parser.tokens.get(1));
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstTokenWithStopAtNonOptionAndValue() throws Exception {
        PosixParser parser = new PosixParser();

        parser.burstToken("-o=out nonOption", true);

        assertEquals(3, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals("out", parser.tokens.get(1));
        assertEquals("nonOption", parser.tokens.get(2));
        assertTrue(parser.eatTheRest);
    }

    @Test
    public void testBurstTokenWithStopAtNonOptionAndNoValue() throws Exception {
        PosixParser parser = new PosixParser();

        parser.burstToken("-o=", true);

        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertTrue(parser.eatTheRest);
    }

    private void assertArrayEquals(Object[] expected, Object[] actual) {
        Assert.assertArrayEquals(expected.getClass(), actual);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], actual[i]);
        }
    }
}