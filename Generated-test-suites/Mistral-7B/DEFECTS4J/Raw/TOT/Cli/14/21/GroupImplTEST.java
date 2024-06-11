package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Options options;

    @Before
    public void setUp() {
        // Create a group with 2 options and a min/max of 1
        List optionsList = new ArrayList();
        optionsList.add(OptionBuilder.withLongOpt("optionOne")
                .hasArg()
                .isRequired()
                .withValueSeparator('=')
                .withDescription("this is option one")
                .create());
        optionsList.add(OptionBuilder.withLongOpt("optionTwo")
                .hasArg()
                .isRequired()
                .withValueSeparator('=')
                .withDescription("this is option two")
                .create());
        this.options = new Options();
        this.options.addOptionGroup(new OptionGroupImpl(optionsList, "optionsGroup", "Options Group"));
    }

    @Test
    public void testProcessWithValidArguments() throws ParseException {
        CommandLine cmd = new CommandLine("-o", new String[]{"-optionOne=A", "-optionTwo=B"});
        this.options.parse(cmd);
        assertNotNull(cmd.getOptionValues("optionOne"));
        assertNotNull(cmd.getOptionValues("optionTwo"));
        assertEquals("A", cmd.getOptionValues("optionOne")[0]);
        assertEquals("B", cmd.getOptionValues("optionTwo")[0]);
    }

    @Test(expected = ParseException.class)
    public void testProcessWithInvalidArguments() throws ParseException {
        CommandLine cmd = new CommandLine("-o", new String[]{"-optionOne=A", "-optionTwo=B", "-optionOne=C"});
        this.options.parse(cmd);
    }

    @Test
    public void testProcessWithNoArguments() throws ParseException {
        CommandLine cmd = new CommandLine("-o");
        this.options.parse(cmd);
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("Test", this.options, true);
        assertTrue(help.contains("Options Group"));
        assertTrue(help.contains("this is option one"));
        assertTrue(help.contains("this is option two"));
    }
}