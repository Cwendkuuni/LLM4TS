import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionName", "optionDescription", false, "o");

        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();

        assertEquals(1, options.size());
        assertEquals(option, options.iterator().next());
    }
}