import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testParse() throws Exception
    {
        String[] args = new String[]{"-a", "1", "--optionB", "valueB", "--optionC=valueC", "--", "arg1", "arg2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(createOptions(), args, null);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));

        Option optionA = cmd.getOption("a");
        assertEquals("1", optionA.getValue());

        Option optionB = cmd.getOption("optionB");
        assertNotNull(optionB);
        assertEquals("valueB", Arrays.asList(optionB.getValues()).get(0));

        Option optionC = cmd.getOption("optionC");
        assertEquals("valueC", optionC.getValue());

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("optionB"));
        assertTrue(cmd.hasOption("optionC"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgumentException() throws Exception
    {
        String[] args = new String[]{"-a", "1", "--optionB"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(createOptions(), args, null);

        Option optionB = cmd.getOption("optionB");
        assertNotNull(optionB);
        assertTrue(optionB.hasValue());

        Option optionMissingArg = cmd.getOptions().get(1);
        assertEquals("optionMissingArg", optionMissingArg.getKey());
        assertTrue(optionMissingArg.isRequired());
        assertNull(optionMissingArg.getValue());

        parser = new DefaultParser();
        cmd = parser.parse(createOptions(), args, null);

        OptionGroup group = cmd.getOptions().get(0);
        assertEquals("group", group.getKey());
        assertTrue(group.isRequired());
        assertNull(group.getSelectedOption());

        Option optionGroup = cmd.getOption("optionGroup");
        assertEquals("optionGroup", optionGroup.getKey());
        assertFalse(optionGroup.isRequired());
        assertNull(optionGroup.getValue());
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOptionException() throws Exception
    {
        String[] args = new String[]{"--unknownOption"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(createOptions(), args, null);

        fail("MissingOptionException expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOptionException() throws Exception
    {
        String[] args = new String[]{"--unknownOption=value", "-a", "1"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(createOptions(), args, null);

        fail("UnrecognizedOptionException expected");
    }

    private Options createOptions()
    {
        Options options = new Options();

        Option optionA = new Option("a", true, true, "An option with a flag");
        optionA.setRequired(false);
        options.addOption(optionA);

        Option optionB = new Option("optionB", true, false, "An option with a value");
        optionB.setRequired(true);
        options.addOption(optionB);

        Option optionC = new Option("optionC", true, false, "An option with a value");
        optionC.setRequired(false);
        optionC.setArgName("VALUE");
        options.addOption(optionC);

        OptionGroup group = new OptionGroup("group", "A group of options");
        group.setRequired(true);
        options.addOptionGroup(group);

        Option optionGroup = new Option("optionGroup", "An option group");
        optionGroup.setAllowedValues(new String[]{"value1", "value2"});
        options.addOption(optionGroup);

        return options;
    }
}