import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupImplTest {

    private GroupImpl group;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();
        Option option1 = new OptionImpl("option1", "Option 1", false);
        Option option2 = new OptionImpl("option2", "Option 2", true);
        options.add(option1);
        options.add(option2);
        List<Option> anonymous = new ArrayList<>();
        GroupImpl group1 = new GroupImpl(options, "group1", "Group 1", 1, 1, false);
        anonymous.add(group1);
        group = new GroupImpl(options, "group", "Group", 1, 2, false);
    }

    @Test
    public void shouldProcessValidOptions() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("option1", "option2").listIterator();
        group.process(commandLine, arguments);
        Assert.assertTrue(commandLine.hasOption("option1"));
        Assert.assertTrue(commandLine.hasOption("option2"));
    }

    @Test
    public void shouldNotProcessInvalidOptions() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("invalid").listIterator();
        group.process(commandLine, arguments);
        Assert.assertFalse(commandLine.hasOption("invalid"));
    }

    @Test
    public void shouldValidateRequiredOptions() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        try {
            group.validate(commandLine);
            Assert.fail("OptionException should have been thrown");
        } catch (OptionException e) {
            Assert.assertEquals("org.apache.commons.cli2.resource.ResourceConstants.MISSING_OPTION", e.getMessage());
        }
    }

    @Test
    public void shouldValidateExceedingMaximumOptions() {
        WriteableCommandLine commandLine = new CommandLineImpl();
        commandLine.addOption("option1");
        commandLine.addOption("option2");
        try {
            group.validate(commandLine);
            Assert.fail("OptionException should have been thrown");
        } catch (OptionException e) {
            Assert.assertEquals("org.apache.commons.cli2.resource.ResourceConstants.UNEXPECTED_TOKEN", e.getMessage());
        }
    }

    @Test
    public void shouldReturnPreferredName() {
        String preferredName = group.getPreferredName();
        Assert.assertEquals("group", preferredName);
    }

    @Test
    public void shouldReturnDescription() {
        String description = group.getDescription();
        Assert.assertEquals("Group", description);
    }

    @Test
    public void shouldReturnOptions() {
        List<Option> options = group.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(new OptionImpl("option1", "Option 1", false)));
        Assert.assertTrue(options.contains(new OptionImpl("option2", "Option 2", true)));
    }

    @Test
    public void shouldReturnAnonymousOptions() {
        List<Option> anonymous = group.getAnonymous();
        Assert.assertEquals(1, anonymous.size());
        Assert.assertTrue(anonymous.contains(new GroupImpl(new ArrayList<>(), "group1", "Group 1", 1, 1, false)));
    }

    @Test
    public void shouldFindOption() {
        Option option = group.findOption("option1");
        Assert.assertNotNull(option);
        Assert.assertEquals("option1", option.getPreferredName());
    }

    @Test
    public void shouldReturnMinimum() {
        int minimum = group.getMinimum();
        Assert.assertEquals(1, minimum);
    }

    @Test
    public void shouldReturnMaximum() {
        int maximum = group.getMaximum();
        Assert.assertEquals(2, maximum);
    }

    @Test
    public void shouldReturnIsOptional() {
        boolean isOptional = group.isOptional();
        Assert.assertFalse(isOptional);
    }
}