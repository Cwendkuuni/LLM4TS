import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        OptionGroup group = new OptionGroup();
        
        group.addOption(option1);
        group.addOption(option2);
        
        assertTrue(group.getOptions().contains(option1));
        assertTrue(group.getOptions().contains(option2));
    }

    @Test
    public void testGetNames() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        OptionGroup group = new OptionGroup();
        
        group.addOption(option1);
        group.addOption(option2);
        
        ArrayList<String> expectedNames = new ArrayList<>();
        expectedNames.add("a");
        expectedNames.add("b");
        
        assertEquals(expectedNames, new ArrayList<>(group.getNames()));
    }

    @Test
    public void testSetSelected() {
        Option option1 = new Option("a", "option A");
        OptionGroup group = new OptionGroup();
        
        group.addOption(option1);
        
        try {
            group.setSelected(option1);
            assertEquals("a", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException thrown");
        }
    }

    @Test
    public void testSetSelectedThrowsException() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        OptionGroup group = new OptionGroup();
        
        group.addOption(option1);
        group.addOption(option2);
        
        try {
            group.setSelected(option1);
            group.setSelected(option2);
            fail("Expected AlreadySelectedException not thrown");
        } catch (AlreadySelectedException e) {
            // Expected exception
        }
    }

    @Test
    public void testToString() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("b", "option B");
        OptionGroup group = new OptionGroup();
        
        group.addOption(option1);
        group.addOption(option2);
        
        String expectedString = "[\"-a option A\", \"-b option B\"]";
        
        assertEquals(expectedString, group.toString());
    }
}