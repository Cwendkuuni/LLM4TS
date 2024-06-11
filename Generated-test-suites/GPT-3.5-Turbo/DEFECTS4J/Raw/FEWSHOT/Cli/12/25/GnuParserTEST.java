package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(new Option("a", false, "option a"));
        options.addOption(new Option("b", true, "option b"));
        options.addOption(new Option("c", true, "option c"));

        String[] arguments1 = {"-a", "-b", "value"};
        assertArrayEquals(arguments1, GnuParser.flatten(options, arguments1, true));

        String[] arguments2 = {"--foo", "--bar", "baz"};
        assertArrayEquals(arguments2, GnuParser.flatten(options, arguments2, false));

        String[] arguments3 = {"-a", "value1", "--", "value2"};
        assertArrayEquals(arguments3, GnuParser.flatten(options, arguments3, true));

        String[] arguments4 = {"-a", "value1", "-b", "value2", "-c", "value3"};
        assertArrayEquals(arguments4, GnuParser.flatten(options, arguments4, false));
    }

}