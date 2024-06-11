import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

import static org.junit.Assert.assertEquals;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("test", "test", false, "Test Option");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        assertEquals(1, options.size());
    }
}