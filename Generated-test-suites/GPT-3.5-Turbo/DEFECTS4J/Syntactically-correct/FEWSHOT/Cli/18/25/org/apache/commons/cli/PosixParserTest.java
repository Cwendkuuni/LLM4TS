
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Option option1 = Option.builder("f").build();
        Option option2 = Option.builder().longOpt("foo").build();
        Option option3 = Option.builder("o").hasArg().build();
        Option option4 = Option.builder().longOpt("bar").hasArg().build();

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);

        String[] arguments1 = {"-f", "--foo", "-o", "arg1", "--bar=arg2"};
        String[] expected1 = {"-f", "--foo", "-o", "arg1", "--bar=arg2"};
        assertArrayEquals(expected1, new PosixParser().flatten(options, arguments1, false));

        String[] arguments2 = {"-f", "--invalid_option", "-o", "arg1", "--bar=arg2"};
        String[] expected2 = {"-f", "--invalid_option", "-o", "arg1", "--bar=arg2"};
        assertArrayEquals(expected2, new PosixParser().flatten(options, arguments2, false));

        String[] arguments3 = {"-f", "--invalid_option", "-o", "arg1", "--bar=arg2"};
        String[] expected3 = {"-f", "--"};
        assertArrayEquals(expected3, new PosixParser().flatten(options, arguments3, true));

        String[] arguments4 = {"-f", "arg1", "--foo", "-o", "arg2", "--bar=arg3"};
        String[] expected4 = {"-f", "arg1", "--foo", "-o", "arg2", "--bar=arg3"};
        assertArrayEquals(expected4, new PosixParser().flatten(options, arguments4, false));

        String[] arguments5 = {"-o", "--", "-f", "arg1", "--foo", "--bar=arg2"};
        String[] expected5 = {"--", "-f", "arg1", "--foo", "--bar=arg2"};
        assertArrayEquals(expected5, new PosixParser().flatten(options, arguments5, false));
    }
}

