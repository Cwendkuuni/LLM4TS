import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.exceptions.OptionException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class OptionTest {

    @Test
    public void testProcess() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .withDescription("Test option")
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("test", "cmd", "option", "testOption", "arg").listIterator();

        option.process(commandLine, args);

        Assert.assertEquals("testOption", commandLine.getOptionValues("testOption").get(0));
    }

    @Test
    public void testDefaults() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLine();
        option.defaults(commandLine);

        Assert.assertNull(commandLine.getOptionValues("testOption"));
    }

    @Test
    public void testCanProcess() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("test", "cmd", "option", "arg").listIterator();

        Assert.assertTrue(option.canProcess(commandLine, args));
    }

    @Test(expected = OptionException.class)
    public void testCanProcessWithException() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLine();
        ListIterator<String> args = Arrays.asList("test", "cmd", "--testOption", "arg").listIterator();

        option.canProcess(commandLine, args);
    }

    @Test
    public void testGetTriggers() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .build();

        Set<String> triggers = option.getTriggers();

        Assert.assertNotNull(triggers);
        Assert.assertEquals(1, triggers.size());
        Assert.assertEquals("--testOption", triggers.iterator().next());
    }

    @Test
    public void testGetPrefixes() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .build();

        Set<String> prefixes = option.getPrefixes();

        Assert.assertNotNull(prefixes);
        Assert.assertEquals(1, prefixes.size());
        Assert.assertEquals("--", prefixes.iterator().next());
    }

    @Test(expected = OptionException.class)
    public void testValidate() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .build();

        WriteableCommandLine commandLine = new WriteableCommandLine();

        option.validate(commandLine);

        commandLine.setOptionValue("testOption", "invalid");

        option.validate(commandLine);
    }

    @Test
    public void testHelpLines() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .withDescription("Test option")
                .build();

        Set<String> helpSettings = new LinkedHashSet<>();
        Comparator<Option> comp = Comparator.comparing(Option::getId);

        List<Option> options = Arrays.asList(
                new DefaultOptionBuilder()
                        .withLongOpt("option1")
                        .build(),
                option,
                new DefaultOptionBuilder()
                        .withLongOpt("option3")
                        .build());

        List<Option> helpLines = option.helpLines(0, helpSettings, comp);

        Assert.assertNotNull(helpLines);
        Assert.assertEquals(3, helpLines.size());
    }

    @Test
    public void testAppendUsage() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .withDescription("Test option")
                .build();

        StringBuffer buffer = new StringBuffer();

        option.appendUsage(buffer, helpSettings(), comparator());

        String expected = "  --testOption\n" +
                "    Test option\n";

        Assert.assertEquals(expected, buffer.toString());
    }

    @Test
    public void testGetPreferredName() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .build();

        String name = option.getPreferredName();

        Assert.assertEquals("testOption", name);
    }

    @Test
    public void testGetDescription() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .withDescription("Test option")
                .build();

        String description = option.getDescription();

        Assert.assertEquals("Test option", description);
    }

    @Test
    public void testGetId() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .build();

        int id = option.getId();

        Assert.assertEquals(0, id);
    }

    @Test
    public void testFindOption() {
        Option option1 = new DefaultOptionBuilder()
                .withLongOpt("option1")
                .build();

        Option option2 = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .build();

        Option option3 = new DefaultOptionBuilder()
                .withLongOpt("option3")
                .build();

        Options options = new Options();
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        Option foundOption = option2.findOption("testOption");

        Assert.assertEquals(option2, foundOption);
    }

    @Test
    public void testIsRequired() {
        Option option = new DefaultOptionBuilder()
                .withLongOpt("testOption")
                .isRequired(true)
                .build();

        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testGetParent() {
        Option parentOption = new DefaultOptionBuilder()
                .withLongOpt("parentOption")
                .build();

        Option childOption = new DefaultOptionBuilder()
                .withLongOpt("childOption")
                .withParent(parentOption)
                .build();

        Assert.assertEquals(parentOption, childOption.getParent());
    }

    @Test
    public void testSetParent() {
        Option childOption = new DefaultOptionBuilder()
                .withLongOpt("childOption")
                .build();

        Option parentOption = new DefaultOptionBuilder()
                .withLongOpt("parentOption")
                .build();

        childOption.setParent(parentOption);

        Assert.assertEquals(parentOption, childOption.getParent());
    }

    private Comparator<Option> comparator() {
        return Comparator.comparing(Option::getId);
    }

    private Set<DisplaySetting> helpSettings() {
        return new HashSet<>();
    }
}