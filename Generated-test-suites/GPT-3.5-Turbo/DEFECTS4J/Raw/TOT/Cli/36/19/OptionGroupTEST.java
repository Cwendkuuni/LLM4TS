import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;

public class OptionGroupTest {

    private Option optA;
    private Option optB;
    private Option optC;
    private Option optD;

    @Before
    public void setUp() {
        optA = new Option("a", "option A");
        optB = new Option("b", "option B");
        optC = new Option("c", "option C");
        optD = new Option("d", "option D");
    }

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        group.addOption(optA);
        assertTrue(group.getOptions().contains(optA));
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        group.addOption(optA);
        group.addOption(optB);
        assertEquals(2, group.getNames().size());
        assertTrue(group.getNames().contains("a"));
        assertTrue(group.getNames().contains("b"));
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        group.addOption(optA);
        group.addOption(optB);
        assertEquals(2, group.getOptions().size());
        assertTrue(group.getOptions().contains(optA));
        assertTrue(group.getOptions().contains(optB));
    }

    @Test
    public void testSetSelected() {
        OptionGroup group = new OptionGroup();
        group.addOption(optA);
        group.addOption(optB);
        group.addOption(optC);
        group.addOption(optD);
        try {
            group.setSelected(optA);
            assertEquals("a", group.getSelected());
            group.setSelected(optB);
            assertEquals("b", group.getSelected());
        } catch (AlreadySelectedException e) {
            fail("Unexpected AlreadySelectedException");
        }
    }

    @Test(expected = AlreadySelectedException.class)
    public void testSetSelected_AlreadySelected() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        group.addOption(optA);
        group.addOption(optB);
        group.setSelected(optA);
        group.setSelected(optB);
    }

    @Test
    public void testGetSelected() {
        OptionGroup group = new OptionGroup();
        group.addOption(optA);
        group.addOption(optB);
        group.setSelected(optA);
        assertEquals("a", group.getSelected());
    }

    @Test
    public void testSetRequired() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);
        assertTrue(group.isRequired());
        group.setRequired(false);
        assertFalse(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        group.addOption(optA);
        group.addOption(optB);
        group.addOption(optC);
        assertEquals("[-a option A, -b option B, -c option C]", group.toString());
    }

}