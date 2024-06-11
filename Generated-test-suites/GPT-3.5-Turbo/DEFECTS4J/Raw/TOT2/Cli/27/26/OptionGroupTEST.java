import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Collection;
import java.util.Iterator;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "Option 1");
        OptionGroup result = optionGroup.addOption(option1);
        assertTrue(result.getNames().contains("a"));
    }

    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "Option 1");
        optionGroup.addOption(option1);
        Collection names = optionGroup.getNames();
        assertTrue(names.contains("a"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "Option 1");
        optionGroup.addOption(option1);
        Collection options = optionGroup.getOptions();
        Iterator iterator = options.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testSetSelected() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "Option 1");
        Option option2 = new Option("b", "bbb", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        assertEquals("a", optionGroup.getSelected());
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedException() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "Option 1");
        Option option2 = new Option("b", "bbb", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }

    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("a", "aaa", false, "Option 1");
        Option option2 = new Option("b", "bbb", false, "Option 2");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        String expected = "[--aaa Option 1, --bbb Option 2]";
        assertEquals(expected, optionGroup.toString());
    }
}