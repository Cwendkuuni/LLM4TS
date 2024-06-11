package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GnuParserTest {

    private Options options;
    private String[] arguments;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("D", "debug", true, "Run in debug mode");
        options.addOption("h", "help", false, "Print help message");
    }

    @Test
    public void testFlattenEmptyArguments() {
        String[] result = new GnuParser().flatten(options, new String[0], false);
        assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSingleArgument() {
        String[] result = new GnuParser().flatten(options, new String[] {"foo"}, false);
        assertArrayEquals(new String[] {"foo"}, result);
    }

    @Test
    public void testFlattenSingleOption() {
        String[] result = new GnuParser().flatten(options, new String[] {"-D"}, false);
        assertArrayEquals(new String[] {"-D"}, result);
    }

    @Test
    public void testFlattenOptionWithValue() {
        String[] result = new GnuParser().flatten(options, new String[] {"-Dproperty=value"}, false);
        assertArrayEquals(new String[] {"-D", "property", "value"}, result);
    }

    @Test
    public void testFlattenDoubleDash() {
        String[] result = new GnuParser().flatten(options, new String[] {"--"}, false);
        assertArrayEquals(new String[] {"--"}, result);
    }

    @Test
    public void testFlattenDoubleDashWithArguments() {
        String[] result = new GnuParser().flatten(options, new String[] {"--arg1", "arg2", "arg3"}, false);
        assertArrayEquals(new String[] {"--arg1", "arg2", "arg3"}, result);
    }

    @Test
    public void testFlattenDoubleDashAsLastArgument() {
        String[] result = new GnuParser().flatten(options, new String[] {"arg1", "--"}, false);
        assertArrayEquals(new String[] {"arg1", "--"}, result);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] result = new GnuParser().flatten(options, new String[] {"-D", "arg1", "-h"}, true);
        assertArrayEquals(new String[] {"-D", "-h"}, result);
    }

    @Test
    public void testFlattenStopAtNonOptionWithEmptyArgument() {
        String[] result = new GnuParser().flatten(options, new String[] {"-D", "", "-h"}, true);
        assertArrayEquals(new String[] {"-D", "-h"}, result);
    }

    @Test
    public void testFlattenDoubleOption() {
        String[] result = new GnuParser().flatten(options, new String[] {"--foo", "--foo"}, false);
        assertArrayEquals(new String[] {"--foo"}, result);
    }

    @Test
    public void testFlattenDoubleOptionWithArguments() {
        String[] result = new GnuParser().flatten(options, new String[] {"--foo=arg1", "--foo=arg2"}, false);
        assertArrayEquals(new String[] {"--foo", "arg1", "--foo", "arg2"}, result);
    }

    @Test
    public void testFlattenSingleLongOption() {
        String[] result = new GnuParser().flatten(options, new String[] {"--longOption"}, false);
        assertArrayEquals(new String[] {"--longOption"}, result);
    }

    @Test
    public void testFlattenLongOptionWithArguments() {
        String[] result = new GnuParser().flatten(options, new String[] {"--longOption=arg1", "--longOption=arg2"}, false);
        assertArrayEquals(new String[] {"--longOption", "arg1", "--longOption", "arg2"}, result);
    }

    @Test
    public void testFlattenLongOptionAsLastArgument() {
        String[] result = new GnuParser().flatten(options, new String[] {"arg1", "--longOption"}, false);
        assertArrayEquals(new String[] {"arg1", "--longOption"}, result);
    }

    @Test
    public void testFlattenLongOptionAndSingleOption() {
        String[] result = new GnuParser().flatten(options, new String[] {"--longOption", "-h"}, false);
        assertArrayEquals(new String[] {"--longOption", "-h"}, result);
    }

    @Test
    public void testFlattenLongOptionWithLongOption() {
        String[] result = new GnuParser().flatten(options, new String[] {"--longOption1", "--longOption2"}, false);
        assertArrayEquals(new String[] {"--longOption1", "--longOption2"}, result);
    }

    @Test
    public void testFlattenLongOptionWithLongOptionAndArguments() {
        String[] result = new GnuParser().flatten(options, new String[] {"--longOption1=arg1", "--longOption2=arg2"}, false);
        assertArrayEquals(new String[] {"--longOption1", "arg1", "--longOption2", "arg2"}, result);
    }

    @Test
    public void testFlattenLongOptionWithSingleOptionAndArguments() {
        String[] result = new GnuParser().flatten(options, new String[] {"--longOption=arg1", "-h", "arg2"}, false);
        assertArrayEquals(new String[] {"--longOption", "arg1", "-h", "arg2"}, result);
    }
}