import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "Option 1", false, "o1");

        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();

        assert(options.size() == 1);
        assert(options.iterator().next().getKey().equals("option1"));
    }
}