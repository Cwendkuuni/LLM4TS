import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;
import java.util.Collection;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("t", "test", false, "Test option");
        OptionGroup result = optionGroup.addOption(option);
        Collection options = optionGroup.getOptions();
        assert(options.contains(option));
        assert(result == optionGroup);
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("t", "test", false, "Test option");
        optionGroup.addOption(option);
        Collection names = optionGroup.getNames();
        assert(names.contains("t"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("t", "test", false, "Test option");
        optionGroup.addOption(option);
        Collection options = optionGroup.getOptions();
        assert(options.contains(option));
    }

    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assert(optionGroup.getSelected().equals("t"));
        optionGroup.setSelected(option2);
        assert(optionGroup.getSelected().equals("u"));
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedWithAlreadySelectedOption() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
        optionGroup.setSelected(option1);
    }

    @Test
    public void testGetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("t", "test1", false, "Test option 1");
        optionGroup.addOption(option1);
        optionGroup.setSelected(option1);
        String selected = optionGroup.getSelected();
        assert(selected.equals("t"));
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assert(optionGroup.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("t", "test1", false, "Test option 1");
        Option option2 = new Option("u", "test2", false, "Test option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String result = optionGroup.toString();
        assert(result.equals("[-t Test option 1, -u Test option 2]"));
    }

}