import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OptionGroupTest {
    OptionGroup optionGroup;

    @BeforeAll
    public void setUp() {
        optionGroup = new OptionGroup();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("optionName", "Option description");
        optionGroup.addOption(option);
        assertEquals(1, optionGroup.getOptions().size());
    }

    @Test
    public void testCreateEmptyOptionGroup() {
        OptionGroup optionGroup = new OptionGroup();
        assertEquals(0, optionGroup.getOptions().size());
    }

    @Test
    public void testAddSameOptionTwice() {
        Option option = new Option("optionName", "Option description");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option);
        assertThrows(IllegalStateException.class, () -> optionGroup.addOption(option));
    }

    @Test
    public void testAddOptionWithNullName() {
        Option option = new Option(null, "Option description");
        OptionGroup optionGroup = new OptionGroup();
        assertThrows(IllegalArgumentException.class, () -> optionGroup.addOption(option));
    }

    @Test
    public void testAddOptionWithNullDescription() {
        Option option = new Option("optionName", null);
        OptionGroup optionGroup = new OptionGroup();
        assertThrows(IllegalArgumentException.class, () -> optionGroup.addOption(option));
    }

    @AfterAll
    public void tearDown() {
        optionGroup = null;
    }
}