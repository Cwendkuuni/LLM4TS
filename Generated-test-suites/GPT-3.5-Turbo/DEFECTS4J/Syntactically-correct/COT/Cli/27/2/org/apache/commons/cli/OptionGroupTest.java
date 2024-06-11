import org.junit.Test;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Option;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        assert group.getOptions().contains(option1);
        assert group.getOptions().contains(option2);
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        assert group.getNames().contains("a");
        assert group.getNames().contains("b");
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        assert group.getOptions().contains(option1);
        assert group.getOptions().contains(option2);
        assert group.getOptions().size() == 2;
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assert group.getSelected().equals("a");

        group.setSelected(option2);
        assert group.getSelected().equals("b");
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedAlreadySelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        group.setSelected(option2);
        group.setSelected(option1);
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);
        assert group.getSelected().equals("a");

        group.setSelected(option2);
        assert group.getSelected().equals("b");
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assert group.isRequired();
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("b", "option2", false, "Option 2");

        group.addOption(option1);
        group.addOption(option2);

        String expected = "[--option1 Option 1, --option2 Option 2]";
        assert group.toString().equals(expected);
    }

}