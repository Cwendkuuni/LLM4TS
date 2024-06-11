import static org.junit.Assert.assertEquals;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {
    // ... other imports and tests here

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionName", "Option description");
        optionGroup.addOption(option);

        Collection<Option> options = optionGroup.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.iterator().next());
    }
}