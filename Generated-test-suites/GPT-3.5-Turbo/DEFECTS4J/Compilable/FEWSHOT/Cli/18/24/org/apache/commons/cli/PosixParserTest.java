package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private PosixParser parser;

    @Before
    public void setup() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = {"-f", "value", "-abc", "--option=optionValue", "arg1", "arg2"};
        boolean stopAtNonOption = false;

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-a", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("--option", result[5]);
        assertEquals("optionValue", result[6]);
        assertEquals("arg1", result[7]);
        assertEquals("arg2", result[8]);
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        Options options = new Options();
        String[] arguments = {"-f", "value", "-abc", "--option=optionValue", "arg1", "arg2"};
        boolean stopAtNonOption = true;

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-abc", result[2]);
        assertEquals("--option=optionValue", result[3]);
        assertEquals("arg1", result[4]);
        assertEquals("arg2", result[5]);
    }

    @Test
    public void testFlatten_noOptions() {
        Options options = new Options();
        String[] arguments = {"arg1", "arg2"};
        boolean stopAtNonOption = false;

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals("arg1", result[0]);
        assertEquals("arg2", result[1]);
    }

    @Test
    public void testFlatten_nullOptions() {
        String[] arguments = {"-f", "value", "-abc", "--option=optionValue", "arg1", "arg2"};
        boolean stopAtNonOption = false;

        String[] result = parser.flatten(null, arguments, stopAtNonOption);

        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-abc", result[2]);
        assertEquals("--option=optionValue", result[3]);
        assertEquals("arg1", result[4]);
        assertEquals("arg2", result[5]);
    }
    
    @Test
    public void testFlatten_nullArguments() {
        Options options = new Options();
        boolean stopAtNonOption = false;

        String[] result = parser.flatten(options, null, stopAtNonOption);

        assertEquals(null, result);
    }

    @Test
    public void testFlatten_withOptionsAndArguments() {
        Options options = new Options();
        options.addOption("f", true, "");
        options.addOption("a", false, "");
        options.addOption("b", false, "");
        options.addOption("c", false, "");
        options.addOption(OptionBuilder.withLongOpt("option").withArgName("value").hasArg().create());
        String[] arguments = {"-f", "value", "-abc", "--option=optionValue", "arg1", "arg2"};
        boolean stopAtNonOption = false;

        String[] result = parser.flatten(options, arguments, stopAtNonOption);

        assertEquals("-f", result[0]);
        assertEquals("value", result[1]);
        assertEquals("-a", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("-c", result[4]);
        assertEquals("--option", result[5]);
        assertEquals("optionValue", result[6]);
        assertEquals("arg1", result[7]);
        assertEquals("arg2", result[8]);
    }
}