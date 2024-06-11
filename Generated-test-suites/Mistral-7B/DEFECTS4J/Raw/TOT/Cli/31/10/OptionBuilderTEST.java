package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testSimpleOption() {
        OptionBuilder ob = new OptionBuilder();
        Option opt = ob.withLongOpt("optionA")
                .isRequired()
                .create('a');

        CommandLine cmd = new DefaultParser().parse(new String[]{"-a"});
        assertFalse(cmd.hasOption("optionB"));
        assertNotNull(cmd.getOptionValue("optionA"));

        try {
            cmd = new DefaultParser().parse(new String[]{"--optionB"});
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testOptionalArgument() {
        OptionBuilder ob = new OptionBuilder();
        Option opt = ob.withLongOpt("optionB")
                .hasArg()
                .create('b');

        CommandLine cmd = new DefaultParser().parse(new String[]{"-b", "arg1"});
        assertEquals("arg1", cmd.getOptionValue("optionB"));

        cmd = new DefaultParser().parse(new String[]{"-b"});
        assertNull(cmd.getOptionValue("optionB"));
    }

    @Test
    public void testMultipleArguments() {
        OptionBuilder ob = new OptionBuilder();
        Option opt = ob.withLongOpt("optionC")
                .hasArgs(2)
                .create('c');

        CommandLine cmd = new DefaultParser().parse(new String[]{"-c", "arg1", "arg2"});
        assertEquals("arg1 arg2", cmd.getOptionValue("optionC"));

        try {
            cmd = new DefaultParser().parse(new String[]{"-c", "arg1"});
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testValueSeparator() {
        OptionBuilder ob = new OptionBuilder();
        Option opt = ob.withLongOpt("optionD")
                .hasArgs()
                .withValueSeparator(':')
                .create('d');

        CommandLine cmd = new DefaultParser().parse(new String[]{"-d", "key1:value1", "key2:value2"});
        assertEquals("key1:value1, key2:value2", cmd.getOptionValue("optionD"));
    }
}