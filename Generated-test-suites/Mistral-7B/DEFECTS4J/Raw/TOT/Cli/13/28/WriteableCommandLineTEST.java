package org.apache.commons.cli2.test;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CmdLineParser().parse("--test", new DefaultCommandLineOptions());
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        assertNotNull(commandLine.getOptions().stream().filter(opt -> opt.longOpt().equals("test")).findFirst().get());
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(commandLine.getOptionByName("test"), "value1");
        assertEquals("value1", commandLine.getValuesForOption("test").get(0));
    }

    @Test
    public void testGetValuesForOption() {
        commandLine = new CmdLineParser().parse("--test=value1 --test=value2", new DefaultCommandLineOptions());
        List<String> values = commandLine.getValuesForOption("test");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }
}