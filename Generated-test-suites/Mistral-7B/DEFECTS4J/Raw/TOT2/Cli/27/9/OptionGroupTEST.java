import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("optionKey", "Option description", true, "optionValue");

        optionGroup.addOption(option);

        assertEquals("optionValue", optionGroup.getSelected());
        assertEquals(1, optionGroup.optionMap.size());
    }
}