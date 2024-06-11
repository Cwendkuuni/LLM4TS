import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.AlreadySelectedException;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        assert(group.getOptions().contains(option1));
        assert(group.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        assert(group.getNames().contains("o1"));
        assert(group.getNames().contains("o2"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        assert(group.getOptions().contains(option1));
        assert(group.getOptions().contains(option2));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        try {
            group.setSelected(option1);
        } catch (AlreadySelectedException e) {
            e.printStackTrace();
        }
        assert(group.getSelected().equals("o1"));
    }

    @Test
    public void testGetSelected() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        try {
            group.setSelected(option1);
        } catch (AlreadySelectedException e) {
            e.printStackTrace();
        }
        assert(group.getSelected().equals("o1"));
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assert(group.isRequired());
        group.setRequired(false);
        assert(!group.isRequired());
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        String expected = "[-o1 Option 1, -o2 Option 2]";
        assert(group.toString().equals(expected));
    }

}