import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption_WhenOptionExists_ReturnTrue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", false, "Option A"));

        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testHasOption_WhenOptionDoesNotExist_ReturnFalse() {
        CommandLine cmd = new CommandLine();

        assertFalse(cmd.hasOption("x"));
    }

    @Test
    public void testHasOption_WhenOptionIsNull_ReturnFalse() {
        CommandLine cmd = new CommandLine();

        assertFalse(cmd.hasOption(null));
    }

    @Test
    public void testHasOption_WhenOptionIsCharacter_ReturnTrue() {
        CommandLine cmd = new CommandLine();
        cmd.addOption(new Option("a", "optionA", false, "Option A"));

        assertTrue(cmd.hasOption('a'));
    }

    @Test
    public void testHasOption_WhenOptionIsCharacterAndDoesNotExist_ReturnFalse() {
        CommandLine cmd = new CommandLine();

        assertFalse(cmd.hasOption('x'));
    }

    @Test
    public void testHasOption_WhenOptionIsCharacterAndIsNull_ReturnFalse() {
        CommandLine cmd = new CommandLine();

        assertFalse(cmd.hasOption((Character) null));
    }

    @Test
    public void testGetOptionObject_WhenOptionExists_ReturnOptionValue() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("a", "optionA", false, "Option A");
        option.addValue("value");
        cmd.addOption(option);

        assertEquals("value", cmd.getOptionObject("a"));
    }

    @Test
    public void testGetOptionObject_WhenOptionDoesNotExist_ReturnNull() {
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionObject("a"));
    }

    @Test
    public void testGetOptionObject_WhenOptionIsNull_ReturnNull() {
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionObject(null));
    }

    @Test
    public void testGetOptionObject_WhenOptionIsCharacter_ReturnOptionValue() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("a", "optionA", false, "Option A");
        option.addValue("value");
        cmd.addOption(option);

        assertEquals("value", cmd.getOptionObject('a'));
    }

    @Test
    public void testGetOptionObject_WhenOptionIsCharacterAndDoesNotExist_ReturnNull() {
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionObject('a'));
    }

    @Test
    public void testGetOptionObject_WhenOptionIsCharacterAndIsNull_ReturnNull() {
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionObject((Character) null));
    }

    @Test
    public void testGetOptionValue_WhenOptionExists_ReturnOptionValue() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("a", "optionA", false, "Option A");
        option.addValue("value");
        cmd.addOption(option);

        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValue_WhenOptionDoesNotExist_ReturnNull() {
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionValue("a"));
    }

    @Test
    public void testGetOptionValue_WhenOptionIsCharacter_ReturnOptionValue() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("a", "optionA", false, "Option A");
        option.addValue("value");
        cmd.addOption(option);

        assertEquals("value", cmd.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValue_WhenOptionIsCharacterAndDoesNotExist_ReturnNull() {
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionValue('a'));
    }

    @Test
    public void testGetOptionValue_WhenOptionIsNull_ReturnNull() {
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionValue(null));
    }

    @Test
    public void testGetOptionValues_WhenOptionExists_ReturnOptionValues() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("a", "optionA", false, "Option A");
        option.addValue("value1");
        option.addValue("value2");
        cmd.addOption(option);

        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues("a"));
    }

    @Test
    public void testGetOptionValues_WhenOptionDoesNotExist_ReturnNull() {
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionValues("a"));
    }

    @Test
    public void testGetOptionValues_WhenOptionIsCharacter_ReturnOptionValues() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("a", "optionA", false, "Option A");
        option.addValue("value1");
        option.addValue("value2");
        cmd.addOption(option);

        assertArrayEquals(new String[]{"value1", "value2"}, cmd.getOptionValues('a'));
    }

    @Test
    public void testGetOptionValues_WhenOptionIsCharacterAndDoesNotExist_ReturnNull() {
        CommandLine cmd = new CommandLine();

        assertNull(cmd.getOptionValues('a'));
    }

    @Test
    public void testGetOptionValueWithDefaultValue_WhenOptionExists_ReturnOptionValue() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("a", "optionA", false, "Option A");
        option.addValue("value");
        cmd.addOption(option);

        assertEquals("value", cmd.getOptionValue("a", "default"));
    }

    @Test
    public void testGetOptionValueWithDefaultValue_WhenOptionDoesNotExist_ReturnDefaultValue() {
        CommandLine cmd = new CommandLine();

        assertEquals("default", cmd.getOptionValue("a", "default"));
    }

    @Test
    public void testGetOptionValueWithDefaultValue_WhenOptionIsCharacter_ReturnOptionValue() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("a", "optionA", false, "Option A");
        option.addValue("value");
        cmd.addOption(option);

        assertEquals("value", cmd.getOptionValue('a', "default"));
    }

    @Test
    public void testGetOptionValueWithDefaultValue_WhenOptionIsCharacterAndDoesNotExist_ReturnDefaultValue() {
        CommandLine cmd = new CommandLine();

        assertEquals("default", cmd.getOptionValue('a', "default"));
    }

    @Test
    public void testGetArgs_WhenArgsExist_ReturnArgsArray() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");

        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, cmd.getArgs());
    }

    @Test
    public void testGetArgs_WhenNoArgsExist_ReturnEmptyArray() {
        CommandLine cmd = new CommandLine();

        assertArrayEquals(new String[0], cmd.getArgs());
    }

    @Test
    public void testGetArgList_WhenArgsExist_ReturnArgList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");

        assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, cmd.getArgList().toArray());
    }

    @Test
    public void testGetArgList_WhenNoArgsExist_ReturnEmptyList() {
        CommandLine cmd = new CommandLine();

        assertEquals(0, cmd.getArgList().size());
    }

    @Test
    public void testAddArg_WhenAddingArg_ArgIsAddedToList() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg");

        assertEquals(1, cmd.getArgList().size());
        assertEquals("arg", cmd.getArgList().get(0));
    }

    @Test
    public void testAddOption_WhenAddingOption_OptionIsAddedToMap() {
        CommandLine cmd = new CommandLine();
        Option option = new Option("a", "optionA", false, "Option A");
        cmd.addOption(option);

        assertEquals(1, cmd.getOptions().length);
        assertEquals(option, cmd.getOptions()[0]);
        assertTrue(cmd.hasOption("a"));
    }
}