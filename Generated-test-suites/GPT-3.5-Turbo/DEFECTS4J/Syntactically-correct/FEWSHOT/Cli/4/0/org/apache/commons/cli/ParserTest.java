package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ParserTest {
    @Test
    public void testParse() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        
        String[] arguments = {"-a", "-b", "value"};
        
        CommandLine cmd = new CommandLine();
        cmd.addOption(option1);
        cmd.addOptionValue(option2, "value");
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        CommandLine parsedCmd = parser.parse(options, arguments);
        
        assertEquals(cmd, parsedCmd);
    }

    @Test
    public void testProcessArgs() throws ParseException {
        Option option = new Option("a", "option", true, "Option");
        ListIterator iterator = Arrays.asList("-a", "value").listIterator();
        
        CommandLine cmd = new CommandLine();
        cmd.addOptionValue(option, "value");
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        parser.processArgs(option, iterator);
        
        assertEquals(cmd, parser.cmd);
    }

    @Test
    public void testProcessOption() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", true, "Option 2");
        options.addOption(option1);
        options.addOption(option2);
        
        ListIterator iterator = Arrays.asList("-a").listIterator();
        
        CommandLine cmd = new CommandLine();
        cmd.addOption(option1);
        
        Parser parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return arguments;
            }
        };
        
        parser.processOption("-a", iterator);
        
        assertEquals(cmd, parser.cmd);
    }
}