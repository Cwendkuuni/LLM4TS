import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

public class OptionGroupTest {
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("option1", "description1", true, "o1");
        Option addedOption = optionGroup.addOption(option);
        assertSame(option, addedOption);
    }
}