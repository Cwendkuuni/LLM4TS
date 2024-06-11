import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseResult;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option optionA = new Option("a", "argument-a", true, "Argument A");
        Option optionB = new Option("b", "boolean-b", false, "Boolean B");
        Option optionC = new Option("c", "choice-c", true, "Choice C");
        Option optionD = new Option("d", "double-d", true, "Double D");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("app"), new ArrayList());

        commandLine.addOption(optionA);
        commandLine.addOption(optionB);
        commandLine.addOption(optionC);
        commandLine.addOption(optionD);

        // Test adding an argument
        commandLine.addValue(optionA, "test-arg-a");

        // Test adding a boolean switch
        commandLine.addSwitch(optionB, true);

        // Test adding a choice
        commandLine.addValue(optionC, "choice-c-1");

        // Test adding a double value
        commandLine.addValue(optionD, new Double(1.2));
        commandLine.addValue(optionD, new Double(2.3));

        // Test parsing the command line
        ParseResult parseResult = commandLine.parse("--a test-arg-a --b --c choice-c-1 --d 1.2 --d 2.3");
        CommandLine parsedCommandLine = parseResult.getCommandLine();

        Assert.assertEquals("test-arg-a", parsedCommandLine.getArgString(optionA.getLongOpt()));
        Assert.assertEquals(true, parsedCommandLine.hasOption(optionB));
        Assert.assertEquals(true, parsedCommandLine.hasOption(optionC, "choice-c-1"));
        Assert.assertEquals(1.2, parsedCommandLine.getValue(optionD, Double.class), 0.0);
        Assert.assertEquals(2.3, parsedCommandLine.getValue(optionD, Double.class), 0.0);
    }

    @Test
    public void testWriteableCommandLineImplWithInvalidOptions() {
        Option optionA = new Option("a", "argument-a", true, "Argument A");
        Option optionB = new Option("b", "boolean-b", false, "Boolean B");
        Option optionC = new Option("c", "choice-c", true, "Choice C");
        Option optionD = new Option("d", "double-d", true, "Double D");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("app"), new ArrayList());

        commandLine.addOption(optionA);
        commandLine.addOption(optionB);
        commandLine.addOption(optionC);
        commandLine.addOption(optionD);

        // Test adding an invalid argument
        try {
            commandLine.addValue(optionA, "invalid-arg");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test adding an invalid choice
        try {
            commandLine.addValue(optionC, "invalid-choice");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test adding an invalid double value
        try {
            commandLine.addValue(optionD, "invalid-double");
            Assert.fail();
        } catch (NumberFormatException e) {
            // expected
        }

        // Test parsing the command line with invalid options
        ParseResult parseResult = commandLine.parse("--a invalid-arg --b invalid-boolean --c invalid-choice --d invalid-double");
        CommandLine parsedCommandLine = parseResult.getCommandLine();
        Assert.assertTrue(parsedCommandLine.getParseErrors().size() > 0);
    }

    @Test
    public void testWriteableCommandLineImplWithMixedOptions() {
        Option optionA = new Option("a", "argument-a", true, "Argument A");
        Option optionB = new Option("b", "boolean-b", false, "Boolean B");
        Option optionC = new Option("c", "choice-c", true, "Choice C");
        Option optionD = new Option("d", "double-d", true, "Double D");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("app"), new ArrayList());

        commandLine.addOption(optionA);
        commandLine.addOption(optionB);
        commandLine.addOption(optionC);
        commandLine.addOption(optionD);

        // Test adding an argument and a boolean switch
        commandLine.addValue(optionA, "test-arg-a");
        commandLine.addSwitch(optionB, true);

        // Test adding an argument and a choice
        try {
            commandLine.addValue(optionC, "test-arg-a");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test adding a boolean switch and a choice
        commandLine.addValue(optionC, "choice-c-1");
        commandLine.addSwitch(optionB, false);

        // Test adding an argument, a choice, and a double value
        commandLine.addValue(optionA, "test-arg-a");
        commandLine.addValue(optionC, "choice-c-1");
        commandLine.addValue(optionD, new Double(1.2));

        // Test parsing the command line with mixed options
        ParseResult parseResult = commandLine.parse("--a test-arg-a --b --c choice-c-1 --d 1.2");
        CommandLine parsedCommandLine = parseResult.getCommandLine();

        Assert.assertEquals("test-arg-a", parsedCommandLine.getArgString(optionA.getLongOpt()));
        Assert.assertEquals(true, parsedCommandLine.hasOption(optionB));
        Assert.assertEquals(true, parsedCommandLine.hasOption(optionC, "choice-c-1"));
        Assert.assertEquals(1.2, parsedCommandLine.getValue(optionD, Double.class), 0.0);
    }
}