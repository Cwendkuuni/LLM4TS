import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class GroupImplTest {

    private List<Option> options;
    private GroupImpl group;

    @Before
    public void setUp() {
        options = new ArrayList<>();

        Option option1 = new OptionImpl("option1", "Option 1");
        Option option2 = new OptionImpl("option2", "Option 2");
        Option option3 = new OptionImpl("option3", "Option 3");

        options.add(option1);
        options.add(option2);
        options.add(option3);

        group = new GroupImpl(options, "testGroup", "Test Group", 0, 3);
    }

    @Test
    public void testGetOptions() {
        List<Option> expectedOptions = options;
        List<Option> actualOptions = group.getOptions();

        assertEquals(expectedOptions, actualOptions);
    }

    @Test
    public void testGetAnonymous() {
        List<Option> expectedAnonymous = new ArrayList<>();
        List<Option> actualAnonymous = group.getAnonymous();

        assertEquals(expectedAnonymous, actualAnonymous);
    }

    @Test
    public void testGetMinimum() {
        int expectedMinimum = 0;
        int actualMinimum = group.getMinimum();

        assertEquals(expectedMinimum, actualMinimum);
    }

    @Test
    public void testGetMaximum() {
        int expectedMaximum = 3;
        int actualMaximum = group.getMaximum();

        assertEquals(expectedMaximum, actualMaximum);
    }

    @Test
    public void testIsRequired() {
        boolean expectedIsRequired = false;
        boolean actualIsRequired = group.isRequired();

        assertEquals(expectedIsRequired, actualIsRequired);
    }

    @Test
    public void testFindOption() {
        String trigger = "option1";

        Option expectedOption = options.get(0);
        Option actualOption = group.findOption(trigger);

        assertEquals(expectedOption, actualOption);
    }
}