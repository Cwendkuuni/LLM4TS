import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GroupImplTest {

    private GroupImpl groupImpl;
    private Option option1;
    private Option option2;
    private Option option3;

    @Before
    public void setUp() {
        option1 = new OptionImpl(1, false);
        option2 = new OptionImpl(2, true);
        option3 = new OptionImpl(3, false);

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);
        options.add(option3);

        groupImpl = new GroupImpl(options, "Group1", "This is a group", 1, 2);
    }

    @Test
    public void testCanProcess() {

        // Test if argument is null
        assertEquals(false, groupImpl.canProcess(null, null));

        // Test if argument is an option within the group
        assertEquals(true, groupImpl.canProcess(null, "1"));

        // Test if argument is an option that requires bursting
        assertEquals(true, groupImpl.canProcess(null, "2"));

        // Test if argument is not an option within the group
        assertEquals(false, groupImpl.canProcess(null, "4"));

        // Test if argument is an anonymous argument
        assertEquals(true, groupImpl.canProcess(null, "arg1"));

    }

    @Test
    public void testGetPrefixes() {
        assertEquals(0, groupImpl.getPrefixes().size());
    }

    @Test
    public void testGetTriggers() {
        assertEquals(3, groupImpl.getTriggers().size());
    }

    @Test
    public void testGetPreferredName() {
        assertEquals("Group1", groupImpl.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals("This is a group", groupImpl.getDescription());
    }

    @Test
    public void testGetOptions() {
        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);
        expectedOptions.add(option3);

        assertEquals(expectedOptions, groupImpl.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> expectedAnonymous = new ArrayList<>();
        assertEquals(expectedAnonymous, groupImpl.getAnonymous());
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, groupImpl.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(2, groupImpl.getMaximum());
    }

}