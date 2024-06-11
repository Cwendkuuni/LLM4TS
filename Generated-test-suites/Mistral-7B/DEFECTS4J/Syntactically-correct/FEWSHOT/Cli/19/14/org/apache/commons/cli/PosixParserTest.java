package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("o", "option", false, "Option description");
        options.addOption("a", "another", true, "Another option description");

        String[] args = {"-o", "--another=value", "-a", "arg", "--", "value", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("o");
        expected.add("--");
        expected.add("another");
        expected.add("value");
        expected.add("a");
        expected.add("arg");
        expected.add("--");
        expected.add("value");
        expected.add("arg2");

        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }

        // test with stopAtNonOption = true
        result = parser.flatten(options, args, true);
        expected.clear();
        expected.add("o");
        expected.add("--another=value");
        expected.add("a");
        expected.add("arg");
        expected.add("--");
        expected.add("value");
        expected.add("arg2");

        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
    }

    @Test
    public void testBurstToken() {
        PosixParser parser = new PosixParser();

        // test empty token
        String[] result = parser.burstToken("", false);
        assertNull(result);

        // test single character token
        result = parser.burstToken("-a", false);
        assertEquals("-a", result[0]);

        // test valid option
        result = parser.burstToken("-oa", false);
        assertEquals("-o", result[0]);
        assertEquals("-a", result[1]);

        // test option with argument
        result = parser.burstToken("-oa=value", false);
        assertEquals("-o", result[0]);
        assertEquals("value", result[1]);

        // test option with multiple characters and argument
        result = parser.burstToken("--longOption=value", false);
        assertEquals("--longOption", result[0]);
        assertEquals("value", result[1]);

        // test option with multiple characters and no argument
        result = parser.burstToken("--longOption", false);
        assertEquals("--longOption", result[0]);

        // test option with multiple characters and stopAtNonOption = true
        result = parser.burstToken("--longOptionArg=value", true);
        assertNull(result);

        // test invalid token
        result = parser.burstToken("-invalidOption", false);
        assertNull(result);
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();

        // test no current option
        parser.process("--");
        parser.process("value");
        assertEquals("--value", Arrays.toString(parser.tokens.toArray()));

        // test option without argument
        parser.init();
        parser.process("-o");
        parser.process("value");
        assertEquals("[o, --value]", Arrays.toString(parser.tokens.toArray()));

        // test option with argument
        parser.init();
        parser.process("-o");
        parser.process("value");
        parser.process("arg");
        assertEquals("[o, arg, --]", Arrays.toString(parser.tokens.toArray()));

        // test option with multiple arguments
        parser.init();
        parser.process("-o");
       parser.process("value");
        parser.process("arg1");
        parser.process("arg2");
        assertEquals("[o, arg1, arg2, --]", Arrays.toString(parser.tokens.toArray()));

        // test current option with argument
        parser.init();
        parser.process("-o");
        parser.process("value");
        parser.process("--another=arg");
        assertEquals("[o, --another=arg, --]", Arrays.toString(parser.tokens.toArray()));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();

        // test valid option
        parser.processOptionToken("-o", false);
        assertTrue(parser.currentOption != null);
        assertEquals("o", parser.currentOption.getLongOpt());

        // test invalid option
        parser.init();
        parser.processOptionToken("invalidOption", false);
        assertNull(parser.currentOption);

        // test option with stopAtNonOption = true
        parser.init();
        parser.processOptionToken("-o", true);
        assertNull(parser.currentOption);
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();

        String[] args = {"--", "value1", "value2", "value3"};
        Iterator<String> iter = Arrays.asList(args).iterator();
        parser.gobble(iter);
        assertEquals("[--, value1, value2, value3]", Arrays.toString(parser.tokens.toArray()));

        args = new String[]{"--", "value1", "value2", "value3", "--"};
        iter = Arrays.asList(args).iterator();
        parser.init();
        parser.gobble(iter);
        assertEquals("[--, value1, value2, value3]", Arrays.toString(parser.tokens.toArray()));

        args = new String[]{"--", "value1", "value2", "value3", "--", "value4"};
        iter = Arrays.asList(args).iterator();
        parser.init();
        parser.gobble(iter);
        assertEquals("[--, value1, value2, value3, value4]", Arrays.toString(parser.tokens.toArray()));
    }

    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("l", "longOption", false, "Long option description");
        options.addOption("lo", "longOption2", true, "Long option 2 description");

        String[] args = {"--longOption", "--longOption2=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("l");
        expected.add("--");
        expected.add("lo");
        expected.add("value");

        for (int i = 0; i < result.length; i++) {
            assertEquals(expected.get(i), result[i]);
        }
    }
}