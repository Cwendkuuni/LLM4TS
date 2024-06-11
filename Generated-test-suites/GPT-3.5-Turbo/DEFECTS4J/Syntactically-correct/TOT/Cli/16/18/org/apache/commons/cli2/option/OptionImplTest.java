import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

    @Test
    public void testCanProcess_ValidArgument_True() {
        OptionImpl option = new ConcreteOptionImpl(1, true);
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        cmd.addArgument("argument");

        assertTrue(option.canProcess(cmd, cmd.getArguments().listIterator()));
    }

    @Test
    public void testCanProcess_NoArgument_False() {
        OptionImpl option = new ConcreteOptionImpl(1, true);
        WriteableCommandLine cmd = new WriteableCommandLineImpl();

        assertFalse(option.canProcess(cmd, cmd.getArguments().listIterator()));
    }

    @Test
    public void testToString_NonEmptyDescription_ValidStringReturned() {
        OptionImpl option = new ConcreteOptionImpl(1, true);
        option.setDescription("This is a test option");

        assertEquals("This is a test option", option.toString());
    }

    @Test
    public void testToString_EmptyDescription_EmptyStringReturned() {
        OptionImpl option = new ConcreteOptionImpl(1, true);

        assertEquals("", option.toString());
    }

    @Test
    public void testEquals_EqualOptions_True() {
        OptionImpl option1 = new ConcreteOptionImpl(1, true);
        OptionImpl option2 = new ConcreteOptionImpl(1, true);

        assertTrue(option1.equals(option2));
    }

    @Test
    public void testEquals_DifferentIds_False() {
        OptionImpl option1 = new ConcreteOptionImpl(1, true);
        OptionImpl option2 = new ConcreteOptionImpl(2, true);

        assertFalse(option1.equals(option2));
    }

    @Test
    public void testEquals_DifferentPreferredNames_False() {
        OptionImpl option1 = new ConcreteOptionImpl(1, true);
        option1.setPreferredName("option1");
        OptionImpl option2 = new ConcreteOptionImpl(1, true);
        option2.setPreferredName("option2");

        assertFalse(option1.equals(option2));
    }

    @Test
    public void testEquals_DifferentDescriptions_False() {
        OptionImpl option1 = new ConcreteOptionImpl(1, true);
        option1.setDescription("This is option1");
        OptionImpl option2 = new ConcreteOptionImpl(1, true);
        option2.setDescription("This is option2");

        assertFalse(option1.equals(option2));
    }

    @Test
    public void testEquals_DifferentPrefixes_False() {
        OptionImpl option1 = new ConcreteOptionImpl(1, true);
        option1.setPrefixes(set("option1"));
        OptionImpl option2 = new ConcreteOptionImpl(1, true);
        option2.setPrefixes(set("option2"));

        assertFalse(option1.equals(option2));
    }

    @Test
    public void testEquals_DifferentTriggers_False() {
        OptionImpl option1 = new ConcreteOptionImpl(1, true);
        option1.setTriggers(set("trigger1"));
        OptionImpl option2 = new ConcreteOptionImpl(1, true);
        option2.setTriggers(set("trigger2"));

        assertFalse(option1.equals(option2));
    }

    @Test
    public void testHashCode_EqualOptions_SameHashCode() {
        OptionImpl option1 = new ConcreteOptionImpl(1, true);
        OptionImpl option2 = new ConcreteOptionImpl(1, true);

        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testFindOption_ExistingTrigger_ReturnsOption() {
        OptionImpl option = new ConcreteOptionImpl(1, true);
        option.setTriggers(set("trigger"));

        assertEquals(option, option.findOption("trigger"));
    }

    @Test
    public void testFindOption_NonExistingTrigger_ReturnsNull() {
        OptionImpl option = new ConcreteOptionImpl(1, true);
        option.setTriggers(set("trigger"));

        assertNull(option.findOption("nonexisting"));
    }

    @Test
    public void testIsRequired_IsRequired_ReturnsTrue() {
        OptionImpl option = new ConcreteOptionImpl(1, true);

        assertTrue(option.isRequired());
    }

    @Test
    public void testIsRequired_IsNotRequired_ReturnsFalse() {
        OptionImpl option = new ConcreteOptionImpl(1, false);

        assertFalse(option.isRequired());
    }

    @Test
    public void testDefaults_NoAction_NothingHappens() {
        OptionImpl option = new ConcreteOptionImpl(1, true);
        WriteableCommandLine cmd = new WriteableCommandLineImpl();

        option.defaults(cmd);

        assertEquals(0, cmd.getArguments().size());
    }

    private Set<String> set(String... items) {
        return new HashSet<>(Arrays.asList(items));
    }
}


class ConcreteOptionImpl extends OptionImpl {
    public ConcreteOptionImpl(int id, boolean required) {
        super(id, required);
    }
}