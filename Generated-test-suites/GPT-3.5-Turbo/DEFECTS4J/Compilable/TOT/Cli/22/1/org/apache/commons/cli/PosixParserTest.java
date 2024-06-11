package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_LongOptionWithoutValue_ShouldAddToTokens() {
        Options options = new Options();
        options.addOption("foo", false, "Foo option");

        String[] args = { "--foo" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("--foo", result[0]);
    }

    @Test
    public void testFlatten_LongOptionWithValue_ShouldAddToTokens() {
        Options options = new Options();
        options.addOption("foo", true, "Foo option");

        String[] args = { "--foo=bar" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(2, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testFlatten_InvalidLongOption_ShouldAddToTokens() {
        String[] args = { "--invalid" };

        String[] result = parser.flatten(new Options(), args, false);

        assertEquals(1, result.length);
        assertEquals("--invalid", result[0]);
    }

    @Test
    public void testFlatten_SingleHyphenOption_ShouldAddToTokens() {
        String[] args = { "-" };

        String[] result = parser.flatten(new Options(), args, false);

        assertEquals(1, result.length);
        assertEquals("-", result[0]);
    }

    @Test
    public void testFlatten_SingleCharacterOption_ShouldAddToTokens() {
        Options options = new Options();
        options.addOption("f", false, "F option");

        String[] args = { "-f" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlatten_InvalidSingleCharacterOption_ShouldAddToTokens() {
        String[] args = { "-i" };

        String[] result = parser.flatten(new Options(), args, false);

        assertEquals(1, result.length);
        assertEquals("-i", result[0]);
    }

    @Test
    public void testFlatten_StopAtNonOptionEnabled_ShouldAddRemainingTokens() {
        String[] args = { "--foo", "bar", "value" };

        String[] result = parser.flatten(new Options(), args, true);

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("foo", result[1]);
        assertEquals("bar", result[2]);
    }

    @Test
    public void testFlatten_StopAtNonOptionDisabled_ShouldAddToTokens() {
        String[] args = { "--foo", "bar", "value" };

        String[] result = parser.flatten(new Options(), args, false);

        assertEquals(3, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("bar", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_BurstTokenWithValidOptions_ShouldAddToTokens() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        String[] args = { "-abc", "value" };

        String[] result = parser.flatten(options, args, false);

        assertEquals(3, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
        assertEquals("cvalue", result[2]);
    }

    @Test
    public void testFlatten_BurstTokenWithInvalidOptionsAndStopAtNonOptionEnabled_ShouldAddRemainingTokens() {
        String[] args = { "-abc", "value" };

        String[] result = parser.flatten(new Options(), args, true);

        assertEquals(3, result.length);
        assertEquals("--", result[0]);
        assertEquals("abc", result[1]);
        assertEquals("value", result[2]);
    }

    @Test
    public void testFlatten_BurstTokenWithInvalidOptionsAndStopAtNonOptionDisabled_ShouldAddToTokens() {
        String[] args = { "-abc", "value" };

        String[] result = parser.flatten(new Options(), args, false);

        assertEquals(1, result.length);
        assertEquals("-abc", result[0]);
    }
}