import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.cli2.builder.DefaultOptionBuilder.option;
import static org.apache.commons.cli2.builder.DefaultOptionBuilder.withLongOpt;
import static org.apache.commons.cli2.builder.DefaultOptionBuilder.withOptionalArg;
import static org.apache.commons.cli2.builder.DefaultOptionBuilder.withShortOpt;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = option("o", "opt")
                .withLongOpt("option")
                .withOptionalArg(withOptionalArg(true).isUnicodeProperty(true));

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
        Assert.assertEquals(option, writeableCommandLine.getOption(option.getPreferredName()));
        Assert.assertFalse(writeableCommandLine.hasOption(option.getLongOpt()));
        Assert.assertFalse(writeableCommandLine.hasOption(option.getShortOpt()));

        writeableCommandLine.addOption(option);
        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = option("a", "arg")
                .withLongOpt("argument")
                .isRepeating(true);

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
        Assert.assertEquals("value1", writeableCommandLine.getValues(option, null).get(0));

        writeableCommandLine.addValue(option, "value2");
        Assert.assertEquals("value1", writeableCommandLine.getValues(option, null).get(0));
        Assert.assertEquals("value2", writeableCommandLine.getValues(option, null).get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = option("s", "switch")
                .withLongOpt("switch");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, false);
        Assert.assertEquals(Boolean.FALSE, writeableCommandLine.getSwitch(option, Boolean.TRUE));

        writeableCommandLine.addSwitch(option, true);
        Assert.assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, Boolean.TRUE));
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = option("o", "opt")
                .withLongOpt("option")
                .withOptionalArg(withOptionalArg(true).isUnicodeProperty(true));

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
        Assert.assertTrue(writeableCommandLine.hasOption(option.getPreferredName()));
        Assert.assertTrue(writeableCommandLine.hasOption(option.getLongOpt()));
        Assert.assertTrue(writeableCommandLine.hasOption(option.getShortOpt()));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = option("o", "opt")
                .withLongOpt("option")
                .withOptionalArg(withOptionalArg(true).isUnicodeProperty(true));

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Option retrievedOption = writeableCommandLine.getOption(option.getPreferredName());
        Assert.assertEquals(option, retrievedOption);
        Assert.assertEquals(option, writeableCommandLine.getOption(option.getLongOpt()));
        Assert.assertEquals(option, writeableCommandLine.getOption(option.getShortOpt()));
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = option("a", "arg")
                .isRepeating(true);

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        values = writeableCommandLine.getValues(option, Collections.singletonList("value3"));
        Assert.assertEquals(3, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
        Assert.assertEquals("value3", values.get(2));
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = option("a", "arg")
                .isRepeating(true);

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));

        values = writeableCommandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = option("s", "switch")
                .withLongOpt("switch");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        Assert.assertEquals(Boolean.FALSE, writeableCommandLine.getSwitch(option, Boolean.TRUE));

        writeableCommandLine.addSwitch(option, true);
        Assert.assertEquals(Boolean.TRUE, writeableCommandLine.getSwitch(option, Boolean.TRUE));

        writeableCommandLine.addSwitch(option, false);
        Assert.assertEquals(Boolean.FALSE, writeableCommandLine.getSwitch(option, Boolean.TRUE));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new PropertyOption("p", "property")
                .withLongOpt("property");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addProperty(option, "property", "value1");
        Assert.assertEquals("value1", writeableCommandLine.getProperty(option, "property", "defaultValue"));
        Assert.assertEquals("value1", writeableCommandLine.getProperty(option, "property"));

        writeableCommandLine.addProperty(option, "property", "value2");
        Assert.assertEquals("value2", writeableCommandLine.getProperty(option, "property", "defaultValue"));
        Assert.assertEquals("value2", writeableCommandLine.getProperty(option, "property"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = new PropertyOption("p", "property")
                .withLongOpt("property");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addProperty(option, "property1", "value1");
        writeableCommandLine.addProperty(option, "property2", "value2");

        Map properties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("value1", properties.get("property1"));
        Assert.assertEquals("value2", properties.get("property2"));

        properties = writeableCommandLine.getProperties();
        Assert.assertEquals(0, properties.size());
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, new ArrayList<>());

        Assert.assertFalse(writeableCommandLine.looksLikeOption(""));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("--"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("-"));
        Assert.assertFalse(writeableCommandLine.looksLikeOption("test"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("-t"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--test"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("-tester"));
        Assert.assertTrue(writeableCommandLine.looksLikeOption("--tester"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        List arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");

        Option option = option("o", "opt")
                .withLongOpt("option")
                .withOptionalArg(withOptionalArg(true).isUnicodeProperty(true));

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        writeableCommandLine.addSwitch(option, true);

        Assert.assertEquals("arg1 arg2 -o option=\"value1 value2\" -o option\" value2 \" -o option --switch", writeableCommandLine.toString());
    }
}