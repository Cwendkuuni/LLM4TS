import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public Option getCurrentOption() {
                return null;
            }

            @Override
            public void setCurrentOption(Option currentOption) {

            }
        };

        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);

        Assert.assertNotNull(commandLine.getOption("o"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public Option getCurrentOption() {
                return null;
            }

            @Override
            public void setCurrentOption(Option currentOption) {

            }
        };

        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("value1"), values);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public Option getCurrentOption() {
                return null;
            }

            @Override
            public void setCurrentOption(Option currentOption) {

            }
        };

        Option option = Option.builder("o").longOpt("option").build();

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);

        // Test empty list
        Option emptyOption = Option.builder("e").longOpt("emptyOption").build();
        commandLine.addOption(emptyOption);
        values = commandLine.getUndefaultedValues(emptyOption);
        Assert.assertTrue(values.isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public Option getCurrentOption() {
                return null;
            }

            @Override
            public void setCurrentOption(Option currentOption) {

            }
        };

        Option option = Option.builder("o").longOpt("option").build();
        List defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(Arrays.asList("default1", "default2"), values);

        // Test empty list
        Option emptyOption = Option.builder("e").longOpt("emptyOption").build();
        commandLine.setDefaultValues(emptyOption, defaultValues);
        values = commandLine.getUndefaultedValues(emptyOption);
        Assert.assertTrue(values.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithValue() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public Option getCurrentOption() {
                return null;
            }

            @Override
            public void setCurrentOption(Option currentOption) {

            }
        };

        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);

        // This should throw an exception
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public Option getCurrentOption() {
                return null;
            }

            @Override
            public void setCurrentOption(Option currentOption) {

            }
        };

        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.getOption("o").isSet());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public Option getCurrentOption() {
                return null;
            }

            @Override
            public void setCurrentOption(Option currentOption) {

            }
        };

        Option option = Option.builder("o").longOpt("option").build();
        commandLine.setDefaultSwitch(option, true);

        Assert.assertTrue(commandLine.getOption("o").isSet());

        // Test false default
        commandLine = new WriteableCommandLine() {
            @Override
            public Option getCurrentOption() {
                return null;
            }

            @Override
            public void setCurrentOption(Option currentOption) {

            }
        };

        commandLine.setDefaultSwitch(option, false);

        Assert.assertFalse(commandLine.getOption("o").isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public Option getCurrentOption() {
                return null;
            }

            @Override
            public void setCurrentOption(Option currentOption) {

            }
        };

        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addProperty(option, "property", "value1");

        Assert.assertEquals("value1", commandLine.getOption("o").getProperties().get("property"));
    }

    @Test
    public void testAddPropertyWithDefault() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public Option getCurrentOption() {
                return null;
            }

            @Override
            public void setCurrentOption(Option currentOption) {

            }
        };

        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addProperty(option, "property");

        Assert.assertNull(commandLine.getOption("o").getProperties().get("property"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine() {
            @Override
            public Option getCurrentOption() {
                return null;
            }

            @Override
            public void setCurrentOption(Option currentOption) {

            }
        };

        Assert.assertTrue(commandLine.looksLikeOption("--option"));
        Assert.assertTrue(commandLine.looksLikeOption("-o"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }
}