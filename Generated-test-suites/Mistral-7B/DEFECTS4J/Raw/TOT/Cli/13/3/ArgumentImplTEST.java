package org.apache.commons.cli2.option;

import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testSimpleArgument() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        arg.process(commandLine, null);
        commandLine.parse();

        assertEquals(1, commandLine.getArgList().size());
        assertEquals("", commandLine.getArgList().get(0));
    }

    @Test(expected = OptionException.class)
    public void testTooFewArguments() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        arg.process(commandLine, null);
        commandLine.parse();

        commandLine.addArgument("test");
        arg.process(commandLine, commandLine.getArgIterator());

        commandLine.parse();

        fail("Should throw OptionException for too few arguments");
    }

    @Test
    public void testMultipleArguments() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 3, ' ', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        arg.process(commandLine, null);
        commandLine.parse();

        commandLine.addArgument("test1");
        commandLine.addArgument("test2");
        arg.process(commandLine, commandLine.getArgIterator());

        commandLine.parse();

        assertEquals(3, commandLine.getArgList().size());
        assertEquals("test1", commandLine.getArgList().get(0));
        assertEquals("test2", commandLine.getArgList().get(1));
        assertEquals("", commandLine.getArgList().get(2));
    }

    @Test(expected = OptionException.class)
    public void testTooManyArguments() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 3, ' ', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        arg.process(commandLine, null);
        commandLine.parse();

        commandLine.addArgument("test1");
        commandLine.addArgument("test2");
        commandLine.addArgument("test3");
        arg.process(commandLine, commandLine.getArgIterator());

        commandLine.parse();

        fail("Should throw OptionException for too many arguments");
    }

    @Test
    public void testValueSeparator() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ':', null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        arg.process(commandLine, null);
        commandLine.parse();

        commandLine.addArgument("test:value");
        arg.process(commandLine, commandLine.getArgIterator());

        commandLine.parse();

        assertEquals(1, commandLine.getArgList().size());
        assertEquals("value", commandLine.getArgList().get(0));
    }

    @Test(expected = OptionException.class)
    public void testInvalidValueSeparator() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ':', null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        arg.process(commandLine, null);
        commandLine.parse();

        commandLine.addArgument("test:value1:value2");
        arg.process(commandLine, commandLine.getArgIterator());

        commandLine.parse();

        fail("Should throw OptionException for invalid separator");
    }

    @Test
    public void testConsumingArgument() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ' ', "--consume", null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        arg.process(commandLine, null);
        commandLine.parse();

        commandLine.addOption(OptionBuilder.withLongOpt("consume").create());
        arg.process(commandLine, commandLine.getArgIterator());

        commandLine.parse();

        assertEquals(0, commandLine.getArgList().size());
        assertFalse(commandLine.hasOption("consume"));
    }

    @Test
    public void testConsumingArgumentWithValues() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ' ', "--consume", null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        arg.process(commandLine, null);
        commandLine.parse();

        commandLine.addOption(OptionBuilder.withLongOpt("consume").hasArg(true).create());
        commandLine.addArgument("test");
        arg.process(commandLine, commandLine.getArgIterator());

        commandLine.parse();

        assertEquals(1, commandLine.getArgList().size());
        assertEquals("test", commandLine.getArgList().get(0));
        assertTrue(commandLine.hasOption("consume"));
    }

    @Test
    public void testValidator() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ' ', null, null, new MyValidator(), null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        arg.process(commandLine, null);
        commandLine.parse();

        commandLine.addArgument("test1");
        arg.process(commandLine, commandLine.getArgIterator());

        commandLine.parse();

        assertEquals(1, commandLine.getArgList().size());
        assertEquals("test1", commandLine.getArgList().get(0));

        commandLine.clear();
        commandLine.addArgument("test2");
        arg.process(commandLine, commandLine.getArgIterator());

        try {
            commandLine.parse();
            fail("Should throw InvalidArgumentException for invalid argument");
        } catch (OptionException e) {
            assertTrue(e.getCause() instanceof InvalidArgumentException);
        }
    }

    @Test
    public void testHelp() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        HelpFormatter formatter = new HelpFormatter();

        arg.process(commandLine, null);

        formatter.printHelp("Test", commandLine);

        assertTrue(commandLine.getArgList().isEmpty());
        assertTrue(commandLine.getOptionMap().isEmpty());
        assertEquals("A simple argument", commandLine.getOptionUsageLine());
        assertEquals("", commandLine.getArgUsageLine());
    }

    @Test
    public void testHelpWithValues() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        HelpFormatter formatter = new HelpFormatter();

        arg.process(commandLine, null);
        commandLine.addArgument("value1");
        arg.process(commandLine, commandLine.getArgIterator());

        formatter.printHelp("Test", commandLine);

        assertEquals(1, commandLine.getArgList().size());
        assertEquals("value1", commandLine.getArgList().get(0));
        assertTrue(commandLine.getOptionMap().isEmpty());
        assertEquals("A simple argument\n", commandLine.getOptionUsageLine());
        assertEquals("value1\n", commandLine.getArgUsageLine());
    }

    @Test
    public void testHelpWithSeparator() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ':', null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        HelpFormatter formatter = new HelpFormatter();

        arg.process(commandLine, null);
        commandLine.addArgument("value1:value2");
        arg.process(commandLine, commandLine.getArgIterator());

        formatter.printHelp("Test", commandLine);

        assertEquals(1, commandLine.getArgList().size());
        assertEquals("value1:value2", commandLine.getArgList().get(0));
        assertTrue(commandLine.getOptionMap().isEmpty());
        assertEquals("A simple argument\nvalue1:value2\n", commandLine.getOptionUsageLine());
        assertEquals("value1:value2\n", commandLine.getArgUsageLine());
    }

    @Test
    public void testHelpWithConsumingOption() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ' ', "--consume", null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        HelpFormatter formatter = new HelpFormatter();

        arg.process(commandLine, null);
        commandLine.addOption(OptionBuilder.withLongOpt("consume").create());
        arg.process(commandLine, commandLine.getArgIterator());

        formatter.printHelp("Test", commandLine);

        assertTrue(commandLine.getArgList().isEmpty());
        assertTrue(commandLine.getOptionMap().isEmpty());
        assertEquals("A simple argument\n", commandLine.getOptionUsageLine());
        assertEquals("", commandLine.getArgUsageLine());
    }

    @Test
    public void testHelpWithDefaultValues() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.setDefaultValues(arg, new String[]{"default1", "default2"});
        HelpFormatter formatter = new HelpFormatter();

        arg.process(commandLine, null);

        formatter.printHelp("Test", commandLine);

        assertEquals(0, commandLine.getArgList().size());
        assertTrue(commandLine.getOptionMap().isEmpty());
        assertEquals("A simple argument\n", commandLine.getOptionUsageLine());
        assertEquals("default1, default2\n", commandLine.getArgUsageLine());
    }

    @Test
    public void testHelpWithDefaultValuesAndValues() {
        Argument arg = new ArgumentImpl("arg", "A simple argument", 1, 1, ' ', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.setDefaultValues(arg, new String[]{"default1", "default2"});
        commandLine.addArgument("value1");
        arg.process(commandLine, commandLine.getArgIterator());

        HelpFormatter formatter = new HelpFormatter();

        formatter.printHelp("Test", commandLine);

        assertEquals(1, commandLine.getArgList().size());
        assertEquals("value1", commandLine.getArgList().get(0));
        assertTrue(commandLine.getOptionMap().isEmpty());
        assertEquals("A simple argument\nvalue1\n", commandLine.getOptionUsageLine());
        assertEquals("value1\n", commandLine.getArgUsageLine());
    }

    static class MyValidator implements Validator {
        @Override
        public void validate(List values) throws InvalidArgumentException {
            for (Object value : values) {
                if (!(value instanceof String) || value.toString().length() < 5) {
                    throw new InvalidArgumentException("Value must be a String with at least 5 characters");
                }
            }
        }
    }
}