import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DefaultOption;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        DefaultOption option1 = new DefaultOption.Builder("option1")
                .withDescription("Option 1")
                .withArgument(new Argument.Builder("arg1").create())
                .create();
        DefaultOption option2 = new DefaultOption.Builder("option2")
                .withDescription("Option 2")
                .create();
        DefaultOption option3 = new DefaultOption.Builder("option3")
                .withDescription("Option 3")
                .create();

        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<String>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
    }

    @Test
    public void testAddOption() {
        DefaultOption option4 = new DefaultOption.Builder("option4")
                .withDescription("Option 4")
                .create();

        commandLine.addOption(option4);

        List<DefaultOption> expectedOptions = Arrays.asList(
                new DefaultOption.Builder("option1")
                        .withDescription("Option 1")
                        .withArgument(new Argument.Builder("arg1").create())
                        .create(),
                new DefaultOption.Builder("option2")
                        .withDescription("Option 2")
                        .create(),
                new DefaultOption.Builder("option3")
                        .withDescription("Option 3")
                        .create(),
                new DefaultOption.Builder("option4")
                        .withDescription("Option 4")
                        .create()
        );

        Assert.assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testAddValue() {
        DefaultOption option1 = new DefaultOption.Builder("option1")
                .withDescription("Option 1")
                .withArgument(new Argument.Builder("arg1").create())
                .create();

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        List<String> expectedValues = Arrays.asList("value1", "value2");

        Assert.assertEquals(expectedValues, commandLine.getValues(option1, new ArrayList<>()));
    }

    @Test
    public void testAddSwitch() {
        DefaultOption option2 = new DefaultOption.Builder("option2")
                .withDescription("Option 2")
                .create();

        commandLine.addSwitch(option2, true);

        Assert.assertTrue(commandLine.getSwitch(option2, false));
    }

    @Test
    public void testHasOption() {
        DefaultOption option3 = new DefaultOption.Builder("option3")
                .withDescription("Option 3")
                .create();

        Assert.assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testGetOption() {
        DefaultOption option1 = new DefaultOption.Builder("option1")
                .withDescription("Option 1")
                .withArgument(new Argument.Builder("arg1").create())
                .create();

        Assert.assertEquals(option1, commandLine.getOption("option1"));
    }

    @Test
    public void testGetValues() {
        DefaultOption option1 = new DefaultOption.Builder("option1")
                .withDescription("Option 1")
                .withArgument(new Argument.Builder("arg1").create())
                .create();

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");

        List<String> defaultValues = Arrays.asList("default1", "default2");

        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option1, defaultValues));
    }
  
    @Test
    public void testGetSwitch() {
        DefaultOption option2 = new DefaultOption.Builder("option2")
                .withDescription("Option 2")
                .create();

        commandLine.addSwitch(option2, true);

        Assert.assertTrue(commandLine.getSwitch(option2, false));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Assert.assertEquals("value1", commandLine.getProperty("property1", ""));
        Assert.assertEquals("value2", commandLine.getProperty("property2", ""));
    }
    
    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("-option1"));
        Assert.assertTrue(commandLine.looksLikeOption("--option2"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(commandLine.getOption("option1"), "value1");
        commandLine.addValue(commandLine.getOption("option2"), "value2");

        Assert.assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testSetDefaultValues() {
        DefaultOption option1 = new DefaultOption.Builder("option1")
                .withDescription("Option 1")
                .withArgument(new Argument.Builder("arg1").create())
                .create();

        commandLine.setDefaultValues(option1, Arrays.asList("default1", "default2"));

        Assert.assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option1, new ArrayList<>()));
    }

    @Test
    public void testSetDefaultSwitch() {
        DefaultOption option2 = new DefaultOption.Builder("option2")
                .withDescription("Option 2")
                .create();

        commandLine.setDefaultSwitch(option2, true);

        Assert.assertTrue(commandLine.getSwitch(option2, false));
    }

    @Test
    public void testGetNormalised() {
        commandLine.addValue(commandLine.getOption("option1"), "value1");
        commandLine.addValue(commandLine.getOption("option2"), "value2");

        Assert.assertEquals(Arrays.asList("value1", "value2"), commandLine.getNormalised());
    }
}