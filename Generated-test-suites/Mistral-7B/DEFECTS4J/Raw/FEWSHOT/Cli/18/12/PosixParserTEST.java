package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        Option anotherOption = new Option("a", "another-option", false, "Another option description");
        options.addOption(option);
        options.addOption(anotherOption);

        String[] arguments = {"--option", "value", "--another-option"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(Arrays.asList("--option", "value", "--another-option"), Arrays.asList(result));

        String[] arguments2 = {"--option", "value1", "--option", "value2"};
        String[] result2 = parser.flatten(options, arguments2, false);

        assertEquals(Arrays.asList("--option", "value1", "--option", "value2"), Arrays.asList(result2));

        String[] arguments3 = {"--option", "value1", "--another-option"};
        String[] result3 = parser.flatten(options, arguments3, true);

        assertEquals(Arrays.asList("--option", "value1", "--another-option"), Arrays.asList(result3));

        String[] arguments4 = {"--option1", "value1", "--option2", "value2"};
        String[] result4 = parser.flatten(options, arguments4, false);

        assertEquals(Arrays.asList("--option1", "value1", "--option2", "value2"), Arrays.asList(result4));

        String[] arguments5 = {"--option", "value", "--"};
        String[] result5 = parser.flatten(options, arguments5, false);

        assertEquals(Arrays.asList("--option", "value", "--"), Arrays.asList(result5));

        String[] arguments6 = {"--option", "value", "arg1", "arg2"};
        String[] result6 = parser.flatten(options, arguments6, false);

        assertEquals(Arrays.asList("--option", "value", "arg1", "arg2"), Arrays.asList(result6));

        String[] arguments7 = {"--option", "value", "--another-option"};
        String[] result7 = parser.flatten(options, arguments7, true);

        assertEquals(Arrays.asList("--option", "value"), Arrays.asList(result7));

        String[] arguments8 = {"--option", "value1", "--option", "value2"};
        String[] result8 = parser.flatten(options, arguments8, true);

        assertEquals(Arrays.asList("--option", "value1"), Arrays.asList(result8));

        String[] arguments9 = {"--option", "value", "--"};
        String[] result9 = parser.flatten(options, arguments9, true);

        assertEquals(Arrays.asList("--option", "value"), Arrays.asList(result9));

        String[] arguments10 = {"--option", "value", "arg1", "arg2"};
        String[] result10 = parser.flatten(options, arguments10, true);

        assertEquals(Arrays.asList("--option", "value"), Arrays.asList(result10));

        String[] arguments11 = {"--option", "value", "--nonexistent-option"};
        String[] result11 = parser.flatten(options, arguments11, true);

        String[] expected = {"--option", "value", "--nonexistent-option"};
        assertArrayEquals(expected, result11);

        String[] arguments12 = {"--nonexistent-option", "value"};
        String[] result12 = parser.flatten(options, arguments12, false);

        assertNull(result12);

        String[] arguments13 = {"--nonexistent-option", "value"};
        String[] result13 = parser.flatten(options, arguments13, true);

        assertArrayEquals(new String[] {}, result13);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");

        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-o", false);

        assertEquals("-o", result[0]);
        assertSame(option, parser.currentOption);
        assertTrue(Arrays.asList(result).subList(1, result.length).isEmpty());

        String[] result2 = parser.burstToken("-o--arg", false);

        assertEquals("-o", result2[0]);
        assertSame(option, parser.currentOption);
        assertEquals("--arg", result2[1]);
        assertTrue(Arrays.asList(result2).subList(2, result2.length).isEmpty());

        String[] result3 = parser.burstToken("-o--arg=value", false);

        assertEquals("-o", result3[0]);
        assertSame(option, parser.currentOption);
        assertEquals("--arg=value", result3[1]);
        assertTrue(Arrays.asList(result3).subList(2, result3.length).isEmpty());

        String[] result4 = parser.burstToken("-o---arg=value", false);

        assertEquals("-o--", result4[0]);
        assertNull(parser.currentOption);
        assertEquals("--arg=value", result4[1]);
        assertTrue(Arrays.asList(result4).subList(2, result4.length).isEmpty());
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] result = parser.burstToken("-", false);

        assertEquals("-", result[0]);
        assertNull(parser.currentOption);
        assertTrue(Arrays.asList(result).subList(1, result.length).isEmpty());
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);

        PosixParser parser = new PosixParser();

        String[] result = parser.burstToken("-o", false);

        assertEquals("-o", result[0]);
        assertSame(option, parser.currentOption);
        assertTrue(Arrays.asList(result).subList(1, result.length).isEmpty());

        parser.init();

        String[] result2 = parser.burstToken("--option", false);

        assertEquals("--option", result2[0]);
        assertNull(parser.currentOption);
        assertTrue(Arrays.asList(result2).subList(1, result2.length).isEmpty());

        parser.init();

        String[] result3 = parser.burstToken("-o--", false);

        assertEquals("-o--", result3[0]);
        assertNull(parser.currentOption);
        assertTrue(Arrays.asList(result3).subList(1, result3.length).isEmpty());

        parser.init();

        String[] result4 = parser.burstToken("-o--arg", false);

        assertEquals("-o--", result4[0]);
        assertSame(option, parser.currentOption);
        assertEquals("arg", result4[1]);
        assertTrue(Arrays.asList(result4).subList(2, result4.length).isEmpty());

        parser.init();

        String[] result5 = parser.burstToken("-o--arg=value", false);

        assertEquals("-o--", result5[0]);
        assertSame(option, parser.currentOption);
        assertEquals("arg=value", result5[1]);
        assertTrue(Arrays.asList(result5).subList(2, result5.length).isEmpty());
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        Option option = new Option("o", "option", true, "Option description");
        options.addOption(option);

        PosixParser parser = new PosixParser();

        parser.process("arg1");

        assertEquals(1, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertNull(parser.currentOption);

        parser.init();

        parser.process("--option");

        assertEquals(1, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0));
        assertNull(parser.currentOption);

        parser.init();

        parser.process("--option=value");

        assertEquals(2, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0));
        assertEquals("value", parser.tokens.get(1));
        assertNull(parser.currentOption);

        parser.init();

        parser.process("--option arg1");

        assertEquals(2, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0));
        assertEquals("arg1", parser.tokens.get(1));
        assertNull(parser.currentOption);

        parser.init();

        parser.process("--option arg1 arg2");

        assertEquals(3, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0));
        assertEquals("arg1", parser.tokens.get(1));
        assertEquals("arg2", parser.tokens.get(2));
        assertNull(parser.currentOption);

        parser.init();

        parser.process("--option arg1=value arg2");

        assertEquals(4, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0));
        assertEquals("arg1=value", parser.tokens.get(1));
        assertEquals("arg2", parser.tokens.get(2));
        assertNull(parser.currentOption);

        parser.init();

        parser.process("--option arg1=value arg2 --another-option arg3");

        assertEquals(5, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0));
        assertEquals("arg1=value", parser.tokens.get(1));
        assertEquals("arg2", parser.tokens.get(2));
        assertEquals("--another-option", parser.tokens.get(3));
        assertEquals("arg3", parser.tokens.get(4));
        assertNull(parser.currentOption);

        parser.init();

        parser.process("--option arg1=value arg2 --another-option arg3 --");

        assertEquals(5, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0));
        assertEquals("arg1=value", parser.tokens.get(1));
        assertEquals("arg2", parser.tokens.get(2));
        assertEquals("--another-option", parser.tokens.get(3));
        assertEquals("--", parser.tokens.get(4));
        assertNull(parser.currentOption);

        parser.init();

        parser.process("--option arg1=value arg2 --another-option arg3 --arg1 arg4");

        assertEquals(6, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0));
        assertEquals("arg1=value", parser.tokens.get(1));
        assertEquals("arg2", parser.tokens.get(2));
        assertEquals("--another-option", parser.tokens.get(3));
        assertEquals("arg3", parser.tokens.get(4));
        assertEquals("--arg1", parser.tokens.get(5));
        assertEquals("arg4", parser.tokens.get(6));
        assertNull(parser.currentOption);

        parser.init();

        parser.process("--option arg1=value arg2 --another-option arg3 --arg1 arg4 --");

        assertEquals(6, parser.tokens.size());
        assertEquals("--option", parser.tokens.get(0));
        assertEquals("arg1=value", parser.tokens.get(1));
        assertEquals("arg2", parser.tokens.get(2));
        assertEquals("--another-option", parser.tokens.get(3));
        assertEquals("arg3", parser.tokens.get(4));
        assertEquals("--", parser.tokens.get(5));
        assertNull(parser.currentOption);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertEquals(0, parser.tokens.size());
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
    }
}