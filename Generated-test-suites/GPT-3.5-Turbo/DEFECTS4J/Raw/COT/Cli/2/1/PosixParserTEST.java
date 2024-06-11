import org.apache.commons.cli.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }

    @Test
    public void testFlatten_noArguments() {
        String[] args = {};
        String[] flattened = parser.flatten(options, args, false);
        assertEquals(0, flattened.length);
    }

    @Test
    public void testFlatten_singleOption() {
        String[] args = {"-a"};
        options.addOption("a", false, "Option A");
        String[] flattened = parser.flatten(options, args, false);
        assertEquals(1, flattened.length);
        assertEquals("-a", flattened[0]);
    }

    @Test
    public void testFlatten_multipleOptions() {
        String[] args = {"-a", "-b", "-c"};
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");
        String[] flattened = parser.flatten(options, args, false);
        assertEquals(3, flattened.length);
        assertTrue(Arrays.asList(flattened).contains("-a"));
        assertTrue(Arrays.asList(flattened).contains("-b"));
        assertTrue(Arrays.asList(flattened).contains("-c"));
    }

    @Test
    public void testFlatten_optionWithArgument() {
        String[] args = {"-a", "value"};
        options.addOption("a", true, "Option A with argument");
        String[] flattened = parser.flatten(options, args, false);
        assertEquals(2, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
    }

    @Test
    public void testFlatten_optionWithArguments() {
        String[] args = {"-a", "value1", "value2"};
        options.addOption("a", true, "Option A with arguments");
        String[] flattened = parser.flatten(options, args, false);
        assertEquals(3, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value1", flattened[1]);
        assertEquals("value2", flattened[2]);
    }

    @Test
    public void testFlatten_hyphenToken() {
        String[] args = {"-"};
        String[] flattened = parser.flatten(options, args, false);
        assertEquals(1, flattened.length);
        assertEquals("-", flattened[0]);
    }

    @Test
    public void testFlatten_doubleHyphenToken() {
        String[] args = {"--"};
        String[] flattened = parser.flatten(options, args, false);
        assertEquals(1, flattened.length);
        assertEquals("--", flattened[0]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        String[] args = {"-a", "value", "nonoption", "-b", "value"};
        options.addOption("a", true, "Option A with argument");
        options.addOption("b", true, "Option B with argument");
        String[] flattened = parser.flatten(options, args, true);
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
        assertEquals("nonoption", flattened[2]);
        assertEquals("-b", flattened[3]);
    }

    @Test
    public void testFlatten_withOptionsAndNonOptions() {
        String[] args = {"-a", "value", "nonoption", "-b", "value"};
        options.addOption("a", true, "Option A with argument");
        options.addOption("b", true, "Option B with argument");
        String[] flattened = parser.flatten(options, args, false);
        assertEquals(4, flattened.length);
        assertEquals("-a", flattened[0]);
        assertEquals("value", flattened[1]);
        assertEquals("nonoption", flattened[2]);
        assertEquals("-b", flattened[3]);
    }

    @Test
    public void testInit() {
        parser.init();
        assertNull(parser.currentOption);
        assertFalse(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testProcess_noCurrentOptionWithArg() {
        String value = "value";
        parser.process(value);
        assertTrue(parser.eatTheRest);
        assertEquals(2, parser.tokens.size());
        assertEquals("--", parser.tokens.get(0));
        assertEquals(value, parser.tokens.get(1));
    }

    @Test
    public void testProcess_currentOptionWithArg() {
        String value = "value";
        Option option = new Option("a", "Option A", true, "");
        parser.currentOption = option;
        parser.process(value);
        assertNull(parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals(value, parser.tokens.get(0));
    }

    @Test
    public void testProcess_currentOptionNoArg() {
        Option option = new Option("a", "Option A", false, "");
        parser.currentOption = option;
        parser.process("value");
        assertNull(parser.currentOption);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testProcess_noCurrentOptionNoArg() {
        String value = "value";
        parser.process(value);
        assertNull(parser.currentOption);
        assertEquals(1, parser.tokens.size());
        assertEquals(value, parser.tokens.get(0));
    }

    @Test
    public void testProcessSingleHyphen() {
        String hyphen = "-";
        parser.processSingleHyphen(hyphen);
        assertEquals(1, parser.tokens.size());
        assertEquals(hyphen, parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken_optionExists() {
        String optionId = "a";
        options.addOption(optionId, "Option A");
        parser.processOptionToken(optionId, false);
        assertNotNull(parser.currentOption);
        assertEquals(optionId, parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken_noOptionExists() {
        String optionId = "a";
        List<String> args = new ArrayList<>();
        args.add(optionId);
        args.add("value");
        parser.processOptionToken(optionId, true);
        assertTrue(parser.eatTheRest);
        assertEquals(0, parser.tokens.size());
    }

    @Test
    public void testBurstToken() {
        String token = "-abc";
        options.addOption("a", "Option A");
        options.addOption("b", "Option B");
        options.addOption("c", "Option C");
        parser.burstToken(token, false);
        assertEquals(3, parser.tokens.size());
        assertTrue(parser.tokens.contains("-a"));
        assertTrue(parser.tokens.contains("-b"));
        assertTrue(parser.tokens.contains("-c"));
    }

}