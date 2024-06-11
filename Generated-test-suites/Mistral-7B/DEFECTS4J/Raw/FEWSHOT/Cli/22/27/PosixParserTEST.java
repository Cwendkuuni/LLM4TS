package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Test empty arguments
        Options options = new Options();
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[0], result);

        // Test simple options
        options.addOption("f", false, null, "Print file.");
        arguments = new String[]{"-f", "input.txt"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-f", "input.txt"}, result);

        arguments = new String[]{"--foo"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo"}, result);

        arguments = new String[]{"--foo=bar"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--foo", "bar"}, result);

        // Test long options with values
        options.addOption("i", false, true, "Input file.");
        arguments = new String[]{"--input", "input.txt"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--input", "input.txt"}, result);

        // Test multiple options
        options.addOption("o", false, true, "Output file.");
        options.addOption("v", false, false, "Verbose.");
        arguments = new String[]{"-o", "output.txt", "-v"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-o", "output.txt", "-v"}, result);

        // Test multiple options with values
        arguments = new String[]{"--output", "output.txt", "--verbose"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--output", "output.txt"}, result);
        assertSame(options.findOption("verbose"), result[1] == null ? null : new Option("v", false));

        // Test long option with value and short option
        arguments = new String[]{"--output=output.txt", "-o", "output2.txt"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--output", "output2.txt"}, result);
        assertSame(options.findOption("output"), new Option("o", "output2.txt"));

        // Test options with short and long aliases
        options.addOption("a", "apple", false, "Print apples.");
        options.addOption("f", "banana", false, "Print bananas.");
        arguments = new String[]{"--apple", "--banana"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--apple"}, result);

        arguments = new String[]{"-a", "-f"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a", "-f"}, result);

        // Test options with mandatory arguments
        options.addOption("x", true, false, "Print something.");
        arguments = new String[]{"--x"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--x"}, result);
        assertNull(parser.getValues("x"));

        arguments = new String[]{"--x", "value"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--x", "value"}, result);
        assertEquals("value", parser.getValues("x")[0]);

        // Test options with optional arguments
        options.addOption("z", true, true, "Print something with a value.");
        arguments = new String[]{"--z"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--z"}, result);
        assertNull(parser.getValues("z"));

        arguments = new String[]{"--z", "value"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--z", "value"}, result);
        assertEquals("value", parser.getValues("z")[0]);

        // Test bursting
        options.clearOptions();
        options.addOption("h", false, false, "Print help.");
        options.addOption("g", true, false, "Print group.");
        arguments = new String[]{"--group=test", "-g", "--help"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--group", "test", "--help"}, result);

        arguments = new String[]{"--help", "--group=test", "-g"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--help", "--group", "test"}, result);
        assertSame(options.findOption("group"), result[1] == null ? null : new Option("g"));

        // Test bursting with multiple groups
        options.clearOptions();
        options.addOption("h", false, false, "Print help.");
        options.addOption("g", true, false, "Print group.");
        options.addOption("s", true, false, "Print subgroup.");
        arguments = new String[]{"--help", "--group=test1 --subgroup=subtest1", "-s", "--group=test2 --subgroup=subtest2"};
        result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--help", "--group", "test1", "--subgroup", "subtest1", "--group", "test2", "--subgroup", "subtest2"}, result);

        // Test stopping at non-option
        options.clearOptions();
        options.addOption("h", false, false, "Print help.");
        arguments = new String[]{"--help", "nothing"};
        result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--help", "nothing"}, result);

        // Test stopping at non-option with short option
        options.clearOptions();
        options.addOption("h", false, false, "Print help.");
        arguments = new String[]{"-h", "nothing"};
        result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--help", "nothing"}, result);

        // Test stopping at non-option with long option
        options.clearOptions();
        options.addOption("h", false, false, "Print help.");
        arguments = new String[]{"--help-all", "nothing"};
        result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--help-all", "nothing"}, result);

        // Test stopping at non-option with multiple options
        options.clearOptions();
        options.addOption("h", false, false, "Print help.");
        options.addOption("g", true, false, "Print group.");
        arguments = new String[]{"--help", "--group", "test", "nothing"};
        result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--help", "--group", "test", "nothing"}, result);

        // Test stopping at non-option with long and short options
        options.clearOptions();
        options.addOption("h", false, false, "Print help.");
        options.addOption("g", true, false, "Print group.");
        arguments = new String[]{"--help-all", "-g", "test", "nothing"};
        result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--help-all", "--group", "test", "nothing"}, result);

        // Test bursting and stopping at non-option
        options.clearOptions();
        options.addOption("h", false, false, "Print help.");
        options.addOption("g", true, false, "Print group.");
        arguments = new String[]{"--group=test1 --subgroup=subtest1 --help"};
        result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"--group", "test1", "--help"}, result);
    }

    @Test
    public void testGetValues() {
        // Test empty
        PosixParser parser = new PosixParser();

        assertNull(parser.getValues(""));
        assertNull(parser.getValues(" "));

        // Test single option
        Options options = new Options();
        options.addOption("f", false, null, "Print file.");
        parser = new PosixParser();
        String[] arguments = new String[]{"-f"};
        parser.parse(options, arguments);
        assertSame(parser.getValues("f"), null);

        // Test option with value
        arguments = new String[]{"-f", "input.txt"};
        parser = new PosixParser();
        parser.parse(options, arguments);
        List values = Arrays.asList(parser.getValues("f"));
        assertEquals(1, values.size());
        assertEquals("input.txt", values.get(0));

        // Test long option
        options.clearOptions();
        options.addOption("i", false, true, "Input file.");
        parser = new PosixParser();
        arguments = new String[]{"--input", "input.txt"};
        parser.parse(options, arguments);
        List values2 = Arrays.asList(parser.getValues("i"));
        assertEquals(1, values2.size());
        assertEquals("input.txt", values2.get(0));

        // Test multiple options
        options.addOption("o", false, true, "Output file.");
        arguments = new String[]{"-o", "output.txt"};
        parser = new PosixParser();
        parser.parse(options, arguments);
        List values3 = Arrays.asList(parser.getValues("o"));
        assertEquals(1, values3.size());
        assertEquals("output.txt", values3.get(0));

        // Test multiple options with values
        arguments = new String[]{"--output", "output.txt"};
        parser = new PosixParser();
        parser.parse(options, arguments);
        List values4 = Arrays.asList(parser.getValues("o"));
        assertEquals(1, values4.size());
        assertEquals("output.txt", values4.get(0));

        // Test optional argument
        options.clearOptions();
        options.addOption("z", true, false, "Print something.");
        parser = new PosixParser();
        arguments = new String[]{"--z"};
        parser.parse(options, arguments);
        assertEquals(Collections.emptyList(), parser.getValues("z"));

        // Test mandatory argument
        options.addOption("x", true, false, "Print something.");
        arguments = new String[]{"--x", "value"};
        parser = new PosixParser();
        parser.parse(options, arguments);
        List values5 = Arrays.asList(parser.getValues("x"));
        assertEquals(1, values5.size());
        assertEquals("value", values5.get(0));

        // Test multiple options with values
        arguments = new String[]{"--x", "value1", "--x", "value2"};
        parser = new PosixParser();
        parser.parse(options, arguments);
        List values6 = Arrays.asList(parser.getValues("x"));
        assertEquals(2, values6.size());
        List values7 = Arrays.asList(values6.get(0));
        assertEquals(1, values7.size());
        assertEquals("value1", values7.get(0));

        List values8 = Arrays.asList(values6.get(1));
        assertEquals(1, values8.size());
        assertEquals("value2", values8.get(0));

        // Test long and short options with values
        arguments = new String[]{"--x", "value1", "-x", "value2"};
        parser = new PosixParser();
        parser.parse(options, arguments);
        List values9 = Arrays.asList(parser.getValues("x"));
        assertEquals(2, values9.size());
        List values10 = Arrays.asList(values9.get(0));
        assertEquals(1, values10.size());
        assertEquals("value1", values10.get(0));

        List values11 = Arrays.asList(values9.get(1));
        assertEquals(1, values11.size());
        assertEquals("value2", values11.get(0));

        // Test long option with multiple values
        options.addOption("z", true, true, "Print something with a value.");
        arguments = new String[]{"--z", "value1", "--z", "value2"};
        parser = new PosixParser();
        parser.parse(options, arguments);
        List values12 = Arrays.asList(parser.getValues("z"));
        assertEquals(2, values12.size());
        List values13 = Arrays.asList(values12.get(0));
        assertEquals(1, values13.size());
        assertEquals("value1", values13.get(0));

        List values14 = Arrays.asList(values12.get(1));
        assertEquals(1, values14.size());
        assertEquals("value2", values14.get(0));

        // Test long option with no value
        arguments = new String[]{"--z"};
        parser = new PosixParser();
        parser.parse(options, arguments);
        assertNull(parser.getValues("z"));

        // Test long option with empty value
        arguments = new String[]{"--z", ""};
        parser = new PosixParser();
        parser.parse(options, arguments);
        assertNull(parser.getValues("z"));

        // Test long option with multiple values and empty value
        arguments = new String[]{"--z", "value1", "--z", "", "--z", "value3"};
        parser = new PosixParser();
        parser.parse(options, arguments);
        List values15 = Arrays.asList(parser.getValues("z"));
        assertEquals(3, values15.size());
        List values16 = Arrays.asList(values15.get(0));
        assertEquals(1, values16.size());
        assertEquals("value1", values16.get(0));

        List values17 = Arrays.asList(values15.get(1));
        assertEquals(0, values17.size());

        List values18 = Arrays.asList(values15.get(2));
        assertEquals(1, values18.size());
        assertEquals("value3", values18.get(0));
    }
}