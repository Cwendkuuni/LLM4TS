package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser gnuParser;

    @Before
    public void setUp() {
        gnuParser = new GnuParser();
    }
    
    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "bar"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--foo", "bar"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"--foo", "bar", "non-option"};

        String[] result = gnuParser.flatten(options, arguments, true);

        assertArrayEquals(new String[]{"--foo", "bar"}, result);
    }

    @Test
    public void testFlattenWithHyphen() {
        Options options = new Options();
        String[] arguments = new String[]{"--", "-value"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--", "-value"}, result);
    }

    @Test
    public void testFlattenWithSingleHyphen() {
        Options options = new Options();
        String[] arguments = new String[]{"--", "-"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"--", "-"}, result);
    }

    @Test
    public void testFlattenWithOptionPrefix() {
        Options options = new Options();
        options.addOption("D", true, "property value");
        String[] arguments = new String[]{"-Dproperty=value"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-D", "property=value"}, result);
    }

    @Test
    public void testFlattenWithoutOptionPrefix() {
        Options options = new Options();
        String[] arguments = new String[]{"-Dproperty=value"};

        String[] result = gnuParser.flatten(options, arguments, false);

        assertArrayEquals(new String[]{"-Dproperty=value"}, result);
    }
}