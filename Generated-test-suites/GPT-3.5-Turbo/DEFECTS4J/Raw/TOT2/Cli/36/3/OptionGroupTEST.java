import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

import java.util.Collection;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("a", "test option a");
        optionGroup.addOption(option);
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("b", "test option b");
        optionGroup.addOption(option);
        Collection<String> names = optionGroup.getNames();
        assertTrue(names.contains("b"));
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("c", "test option c");
        Option option2 = new Option("d", "test option d");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        try {
            optionGroup.setSelected(option1);
            assertEquals("c", optionGroup.getSelected());

            // Expecting exception as option1 is already selected
            assertThrows(AlreadySelectedException.class, () -> optionGroup.setSelected(option1));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSetRequired() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        assertTrue(optionGroup.isRequired());
    }

}