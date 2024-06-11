import org.junit.Test;
import static org.junit.Assert.*;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "add", false, "Add option");
        optionGroup.addOption(option);
        assertTrue(optionGroup.getOptions().contains(option));
    }

}