import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link WriteableCommandLineImpl}
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testCreateWriteableCommandLineImpl() {
        Option rootOption = Option.builder("testOption").build();
        List arguments = Arrays.asList("--testOption", "first argument", "second argument");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        WriteableCommandLine expectedCommandLine = new WriteableCommandLineParser().parse(rootOption, arguments.toArray(new String[0]));
        assertEquals(expectedCommandLine, commandLine);
    }

    @Test
    public void testAddOption() {
        Option rootOption = Option.builder("testOption").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        Option testOption = Option.builder("testOption").build();
        commandLine.addOption(testOption);

        assertTrue(commandLine.hasOption(testOption));
        assertSame(testOption, commandLine.getOption("testOption"));
        assertNull(commandLine.getOption("nonExistentOption"));
    }

    @Test
    public void testAddValue() {
        Option argumentOption = Option.builder("testArg").argName("ARG").builder()
                .requiredOption()
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(argumentOption);
        commandLine.addValue(argumentOption, "value1");
        commandLine.addValue(argumentOption, "value2");

        List values = commandLine.getValues(argumentOption, null);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testAddSwitch() {
        Option testSwitchOption = Option.builder("testSwitch").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addSwitch(testSwitchOption, true);
        assertTrue(commandLine.getSwitch(testSwitchOption, false));

        commandLine.addSwitch(testSwitchOption, false);
        assertFalse(commandLine.getSwitch(testSwitchOption, true));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option testSwitchOption = Option.builder("testSwitch").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addSwitch(testSwitchOption, true);
        commandLine.addSwitch(testSwitchOption, true);
    }

    @Test
    public void testHasOption() {
        Option testOption = Option.builder("testOption").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(testOption);
        assertTrue(commandLine.hasOption(testOption));
        assertFalse(commandLine.hasOption(new Option("nonExistentOption")));
    }

    @Test
    public void testGetOption() {
        Option testOption = Option.builder("testOption").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(testOption);
        assertEquals(testOption, commandLine.getOption("testOption"));
        assertNull(commandLine.getOption("nonExistentOption"));
    }

    @Test
    public void testGetValues() {
        Option argumentOption = Option.builder("testArg").argName("ARG").builder()
                .requiredOption()
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(argumentOption);
        commandLine.addValue(argumentOption, "value1");
        commandLine.addValue(argumentOption, "value2");

        List values = commandLine.getValues(argumentOption, null);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        Option argumentOption = Option.builder("testArg").argName("ARG").builder()
                .requiredOption()
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(argumentOption);
        commandLine.addValue(argumentOption, "value1");
        commandLine.addValue(argumentOption, "value2");

        List values = commandLine.getUndefaultedValues(argumentOption);
        assertEquals(Collections.emptyList(), values);
    }

    @Test
    public void testGetSwitch() {
        Option testSwitchOption = Option.builder("testSwitch").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addSwitch(testSwitchOption, true);
        assertTrue(commandLine.getSwitch(testSwitchOption, false));

        commandLine.addSwitch(testSwitchOption, false);
        assertFalse(commandLine.getSwitch(testSwitchOption, true));
    }

    @Test
    public void testGetProperty() {
        Option propertyOption = Option.builder("testProperty")
                .builder()
                .withLongOpt("test-property")
                .isRequired(false)
                .hasArg(true)
                .withArgName("VALUE")
                .withValuesSeparator(',')
                .withValuesParser(PropertyParser.INSTANCE)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(propertyOption);
        commandLine.addProperty(propertyOption, "testProperty", "value1");
        commandLine.addProperty(propertyOption, "testProperty", "value2");

        String property = commandLine.getProperty("testProperty");
        assertEquals("value1,value2", property);
    }

    @Test
    public void testGetProperties() {
        Option propertyOption = Option.builder("testProperty")
                .builder()
                .withLongOpt("test-property")
                .isRequired(false)
                .hasArg(true)
                .withArgName("VALUE")
                .withValuesSeparator(',')
                .withValuesParser(PropertyParser.INSTANCE)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(propertyOption);
        commandLine.addProperty(propertyOption, "testProperty1", "value1");
        commandLine.addProperty(propertyOption, "testProperty2", "value2");

        Set properties = commandLine.getProperties(propertyOption);
        assertEquals(Sets.newHashSet("testProperty1=value1,testProperty2=value2"), properties);
    }

    @Test
    public void testToString() {
        Option testOption = Option.builder("testOption").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Arrays.asList("--testOption"));

        assertEquals("--testOption", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option testOption1 = Option.builder("testOption1").build();
        Option testOption2 = Option.builder("testOption2").build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(testOption1);
        commandLine.addOption(testOption2);

        List options = commandLine.getOptions();
        assertEquals(Arrays.asList(testOption1, testOption2), options);
    }

    @Test
    public void testGetOptionTriggers() {
        Option testOption1 = Option.builder("testOption1").build();
        Option testOption2 = Option.builder("testOption2").build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.addOption(testOption1);
        commandLine.addOption(testOption2);
        commandLine.addOption(testOption1);

        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(Sets.newHashSet("testOption1", "testOption2"), optionTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        Option testOption = Option.builder("testOption").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(testOption, defaultValues);

        List values = commandLine.getValues(testOption, null);
        assertEquals(defaultValues, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option testOption = Option.builder("testOption").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Collections.emptyList());

        commandLine.setDefaultSwitch(testOption, true);
        assertTrue(commandLine.getSwitch(testOption, false));
    }

    @Test
    public void testGetNormalised() {
        Option testOption = Option.builder("testOption").build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option[0], Arrays.asList("--testOption", "arg1", "arg2"));

        List normalised = commandLine.getNormalised();
        assertEquals(Arrays.asList("--testOption", "arg1", "arg2"), normalised);
    }
}