package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.junit.Test;

public class OptionGroupTest {

    @Test
    public void testAddOption() {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();

        group.addOption(option1);
        group.addOption(option2);

        assertEquals(2, group.getOptions().size());
        assertEquals(option1, group.getOptions().toArray()[0]);
        assertEquals(option2, group.getOptions().toArray()[1]);
    }

    @Test
    public void testGetNames() {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();
        group.addOption(option1);
        group.addOption(option2);

        assertEquals(Arrays.asList("o1", "o2"), group.getNames());
    }

    @Test
    public void testGetOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();
        group.addOption(option1);
        group.addOption(option2);

        assertEquals(Arrays.asList(option1, option2), group.getOptions());
    }

    @Test
    public void testSetSelectedNull() {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(null);

        assertNull(group.getSelected());
    }

    @Test
    public void testSetSelectedSame() {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertEquals(option1.getOpt(), group.getSelected());
    }

    @Test
    public void testSetSelectedDifferent() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertEquals(option1.getOpt(), group.getSelected());

        assertThrows(AlreadySelectedException.class, () -> {
            group.setSelected(option2);
        });
    }

    @Test
    public void testSetSelectedNullAfterReset() throws AlreadySelectedException {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        assertEquals(option1.getOpt(), group.getSelected());

        group.setSelected(null);

        assertNull(group.getSelected());

        group.setSelected(option2);

        assertEquals(option2.getOpt(), group.getSelected());
    }

    @Test
    public void testIsRequiredFalse() {
        OptionGroup group = new OptionGroup();
        group.setRequired(false);

        assertFalse(group.isRequired());
    }

    @Test
    public void testIsRequiredTrue() {
        OptionGroup group = new OptionGroup();
        group.setRequired(true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testToString() {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();
        group.addOption(option1);
        group.addOption(option2);

        Option longOption1 = Option.builder("l1").longOpt("long-o1").build();
        Option longOption2 = Option.builder("l2").longOpt("long-o2").build();
        group.addOption(longOption1);
        group.addOption(longOption2);

        String expected = "[-o1 Option one\n" +
                "         -o2 Option two\n" +
                "         --long-o1 long option one\n" +
                "         --long-o2 long option two]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringEmpty() {
        OptionGroup group = new OptionGroup();

        String expected = "[ ]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringSingleShortOption() {
        OptionGroup group = new OptionGroup();
        Option option = Option.builder("o").build();
        group.addOption(option);

        String expected = "[ -o ]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringSingleLongOption() {
        OptionGroup group = new OptionGroup();
        Option option = Option.builder("l").longOpt("long-o").build();
        group.addOption(option);

        String expected = "[ --long-o ]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringSingleBoth() {
        OptionGroup group = new OptionGroup();
        Option optionShort = Option.builder("s").build();
        Option optionLong = Option.builder("l").longOpt("long-o").build();
        group.addOption(optionShort);
        group.addOption(optionLong);

        String expected = "[ -s, --long-o ]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringMultipleShortOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[ -o1, -o2 ]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringMultipleLongOptions() {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("l1").longOpt("long-o1").build();
        Option option2 = Option.builder("l2").longOpt("long-o2").build();
        group.addOption(option1);
        group.addOption(option2);

        String expected = "[ --long-o1, --long-o2 ]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringMultipleShortAndLongOptions() {
        OptionGroup group = new OptionGroup();
        Option optionShort = Option.builder("s").build();
        Option optionLong = Option.builder("l").longOpt("long-o").build();
        group.addOption(optionShort);
        group.addOption(optionLong);

        String expected = "[ -s, --long-o ]";

        assertEquals(expected, group.toString());
    }

    @Test
    public void testToStringWithSelected() {
        OptionGroup group = new OptionGroup();
        Option option1 = Option.builder("o1").build();
        Option option2 = Option.builder("o2").build();
        group.addOption(option1);
        group.addOption(option2);

        group.setSelected(option1);

        String expected = "[ -o1, -- " + Option.BUILDER_DEFAULT_LONG_OPT_PREFIX + "o1 ]";

        assertEquals(expected, group.toString());
    }
}