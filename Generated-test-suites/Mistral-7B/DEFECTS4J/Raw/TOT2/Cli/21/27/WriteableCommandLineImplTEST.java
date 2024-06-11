import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // when
        writeableCommandLine.addOption(option);

        // then
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // when
        writeableCommandLine.addValue(option, "value");

        // then
        assertEquals("[value]", writeableCommandLine.getValues(option, Collections.emptyList()).toString());
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // when
        writeableCommandLine.addSwitch(option, true);

        // then
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // when
        writeableCommandLine.addOption(option);

        // then
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addOption(option);

        // when
        Option result = writeableCommandLine.getOption("o");

        // then
        assertSame(option, result);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        // when
        List values = writeableCommandLine.getValues(option, Collections.emptyList());

        // then
        assertEquals("[value1, value2]", values.toString());
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        // when
        List values = writeableCommandLine.getUndefaultedValues(option);

        // then
        assertEquals(Collections.emptyList(), values);
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addSwitch(option, true);

        // when
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);

        // then
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addProperty(option, "property", "value");

        // when
        String propertyValue = writeableCommandLine.getProperty(option, "property");

        // then
        assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        // when
        writeableCommandLine.addProperty(option, "property", "value");

        // then
        assertEquals("value", writeableCommandLine.getProperty(option, "property"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty_multiple() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        // when
        String property1Value = writeableCommandLine.getProperty(option, "property1");
        String property2Value = writeableCommandLine.getProperty(option, "property2");

        // then
        assertEquals("value1", property1Value);
        assertEquals("value2", property2Value);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        // when
        Set<String> properties = writeableCommandLine.getProperties(option);

        // then
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        // given
        Option option = Option.builder("o").longOpt("option").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        // when
        String commandLineString = writeableCommandLine.toString();

        // then
        assertEquals("arg1 arg2", commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        // given
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        writeableCommandLine.addOption(option2);

        // when
        List<Option> options = writeableCommandLine.getOptions();

        // then
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        // given
        Option option1 = Option.builder("o1").longOpt("option1").build();
        Option option2 = Option.builder("o2").longOpt("option2").build();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        writeableCommandLine.addOption(option2);

        // when
        Set<String> optionTriggers = writeableCommandLine.getOptionTriggers();

        // then
        assertTrue(optionTriggers.contains("o1"));
        assertTrue(optionTriggers.contains("o2"));
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        // given
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        // when
        boolean result = writeableCommandLine.looksLikeOption("o");

        // then
        assertTrue(result);
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption_false() {
        // given
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, Collections.emptyList());

        // when
        boolean result = writeableCommandLine.looksLikeOption("arg");

        // then
        assertFalse(result);
    }
}