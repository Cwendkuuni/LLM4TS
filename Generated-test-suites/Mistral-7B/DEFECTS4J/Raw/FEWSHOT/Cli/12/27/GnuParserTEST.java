package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testGnuParserFlattenBasic() {
        String[] args = { "-Dfoo=bar", "-D", "--", "arg1", "--foo=value", "arg2" };
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"-D", "-Dfoo=bar", "--", "arg1", "--foo", "value", "arg2"}, result);
    }

    @Test
    public void testGnuParserFlattenSingleCharacterOption() {
        String[] args = { "-D", "-f" };
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"-D", "-f"}, result);
    }

    @Test
    public void testGnuParserFlattenDashDash() {
        String[] args = { "--" };
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--"}, result);
    }

    @Test
    public void testGnuParserFlattenLongOptionWithValue() {
        String[] args = { "--longOption=value" };
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"--longOption", "value"}, result);
    }

    @Test
    public void testGnuParserFlattenSingleArgument() {
        String[] args = { "arg" };
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"arg"}, result);
    }

    @Test
    public void testGnuParserFlattenStopAtNonOption() {
        String[] args = { "-D", "-f", "--nonOption", "--foo=value" };
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(new String[]{"-D", "-f"}, result);
    }

    @Test
    public void testGnuParserFlattenEmptyOptions() {
        String[] args = {};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testGnuParserFlattenNullArgs() {
        String[] args = null;
        Options options = new Options();

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertNull(result);
    }
}