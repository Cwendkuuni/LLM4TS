import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("testOption1", "Test Option 1");
        optionGroup.addOption(option);
        Map<String, Option> optionMap = optionGroup.optionMap;
        Option expectedOption = optionMap.get("testOption1");
        Option actualOption = optionMap.get("testOption1");
        assertEquals(expectedOption, actualOption);

        Option option2 = new Option("testOption2", "Test Option 2");
        optionGroup.addOption(option2);
        assertEquals(2, optionMap.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullOption() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullKey() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option(null, "Test Option");
        optionGroup.addOption(option);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSameOptionTwice() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("testOption", "Test Option");
        optionGroup.addOption(option);
        optionGroup.addOption(option);
    }
}