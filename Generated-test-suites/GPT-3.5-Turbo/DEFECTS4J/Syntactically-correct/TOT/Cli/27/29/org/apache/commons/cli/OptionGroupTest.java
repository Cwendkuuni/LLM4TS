import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collection;

public class OptionGroupTest {

    private OptionGroup optionGroup;

    @Before
    public void setup() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void addOption_validOption_optionAddedToGroup() {
        Option option = new Option("t", "test", true, "Test option");
        optionGroup.addOption(option);
        Collection<org.apache.commons.cli.Option> options = optionGroup.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertTrue(options.contains(option));
    }

    @Test
    public void getNames_noOptions_emptyCollectionReturned() {
        Collection<String> names = optionGroup.getNames();
        Assert.assertTrue(names.isEmpty());
    }

    @Test
    public void getNames_optionsAddedWithOptions_namesCollectionContainsAddedOptions() {
        Option option1 = new Option("t", "test", true, "Test option");
        Option option2 = new Option("u", "user", true, "User option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<String> names = optionGroup.getNames();
        Assert.assertEquals(2, names.size());
        Assert.assertTrue(names.contains(option1.getKey()));
        Assert.assertTrue(names.contains(option2.getKey()));
    }

    @Test
    public void getOptions_noOptions_emptyCollectionReturned() {
        Collection<org.apache.commons.cli.Option> options = optionGroup.getOptions();
        Assert.assertTrue(options.isEmpty());
    }

    @Test
    public void getOptions_optionsAddedWithNoOptions_optionsCollectionContainsAddedOptions() {
        Option option1 = new Option("t", "test", true, "Test option");
        Option option2 = new Option("u", "user", true, "User option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        Collection<org.apache.commons.cli.Option> options = optionGroup.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void setSelected_nullOption_selectedIsNull() throws AlreadySelectedException {
        optionGroup.setSelected(null);
        Assert.assertNull(optionGroup.getSelected());
    }

    @Test
    public void setSelected_validOption_selectedIsSet() throws AlreadySelectedException {
        Option option = new Option("t", "test", true, "Test option");
        optionGroup.setSelected(option);
        Assert.assertEquals(option.getOpt(), optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void setSelected_sameOptionSelected_exceptionThrown() throws AlreadySelectedException {
        Option option1 = new Option("t", "test", true, "Test option");
        Option option2 = new Option("u", "user", true, "User option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option1);
    }

    @Test
    public void getSelected_noOptionSelected_selectedIsNull() {
        Assert.assertNull(optionGroup.getSelected());
    }

    @Test
    public void getSelected_optionSelected_selectedIsReturned() throws AlreadySelectedException {
        Option option = new Option("t", "test", true, "Test option");
        optionGroup.setSelected(option);
        Assert.assertEquals(option.getOpt(), optionGroup.getSelected());
    }

    @Test
    public void setRequired_requiredIsTrue_requiredIsSet() {
        optionGroup.setRequired(true);
        Assert.assertTrue(optionGroup.isRequired());
    }

    @Test
    public void setRequired_requiredIsFalse_requiredIsSet() {
        optionGroup.setRequired(false);
        Assert.assertFalse(optionGroup.isRequired());
    }

    @Test
    public void toString_noOptions_emptyStringReturned() {
        String expected = "[]";
        String actual = optionGroup.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void toString_optionsAddedWithOptions_stringRepresentationContainsOptionDetails() {
        Option option1 = new Option("t", "test", true, "Test option");
        Option option2 = new Option("u", "user", true, "User option");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[-t Test option, -u User option]";
        String actual = optionGroup.toString();
        Assert.assertEquals(expected, actual);
    }
}