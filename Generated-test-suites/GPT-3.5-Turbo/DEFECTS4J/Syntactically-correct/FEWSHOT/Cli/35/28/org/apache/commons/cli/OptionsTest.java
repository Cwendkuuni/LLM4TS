package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionsTest {
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
    }

    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup();

        Option option1 = new Option("f", "foo", false, "Foo option");
        group.addOption(option1);
        options.addOptionGroup(group);

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasLongOption("foo"));

        OptionGroup retrievedGroup = options.getOptionGroup(option1);
        assertEquals(group, retrievedGroup);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("f", "foo", false, "Foo option");
        options.addOption(option);

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasLongOption("foo"));

        Option retrievedOption = options.getOption("f");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testAddOptionWithHasArg() {
        Option option = new Option("f", "foo", true, "Foo option");
        options.addOption(option);

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasLongOption("foo"));

        Option retrievedOption = options.getOption("f");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testAddOptionWithLongOpt() {
        Option option = new Option(null, "foo", false, "Foo option");
        options.addOption(option);

        assertFalse(options.hasOption("f"));
        assertTrue(options.hasLongOption("foo"));

        Option retrievedOption = options.getOption("f");
        assertNull(retrievedOption);

        retrievedOption = options.getOption("foo");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetMatchingOptions() {
        Option option1 = new Option("f", "foo", false, "Foo option");
        Option option2 = new Option("b", "bar", false, "Bar option");

        options.addOption(option1);
        options.addOption(option2);

        assertEquals(2, options.getMatchingOptions("f").size());
        assertEquals(1, options.getMatchingOptions("b").size());
        assertEquals(0, options.getMatchingOptions("m").size());
    }

    @Test
    public void testHasOption() {
        assertFalse(options.hasOption("f"));

        Option option = new Option("f", "foo", false, "Foo option");
        options.addOption(option);

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasLongOption("foo"));
    }

    @Test
    public void testToString() {
        Option option = new Option("f", "foo", false, "Foo option");
        options.addOption(option);

        String expected = "[ Options: [ short {f=Option{argName='null', " +
                          "required=false, longOpt='foo', hasArg=false, " +
                          "description='Foo option'}} ] [ long {foo=Option{" +
                          "argName='null', required=false, longOpt='foo', " +
                          "hasArg=false, description='Foo option'}} ]]";
        assertEquals(expected, options.toString());
    }
}