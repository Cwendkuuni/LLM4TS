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
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        Options result = options.addOptionGroup(optionGroup);

        assertTrue(result.getOption("f").getOpt().equals("f"));
        assertTrue(result.getOption("file").getLongOpt().equals("file"));
        assertTrue(result.getOption("d").getOpt().equals("d"));
        assertTrue(result.getOption("directory").getLongOpt().equals("directory"));
        assertTrue(result.getOptionGroup(result.getOption("f")).equals(optionGroup));
        assertTrue(result.getOptionGroup(result.getOption("file")).equals(optionGroup));
        assertTrue(result.getOptionGroup(result.getOption("d")).equals(optionGroup));
        assertTrue(result.getOptionGroup(result.getOption("directory")).equals(optionGroup));
    }

    @Test
    public void testAddOption() {
        options.addOption("f", "file", false, "File option");

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
    }

    @Test
    public void testAddOption_multipleOptions() {
        options.addOption("f", "file", false, "File option");
        options.addOption("d", "directory", false, "Directory option");

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.hasOption("directory"));
    }

    @Test
    public void testAddOption_shortNameOnly() {
        options.addOption("f", false, "File option");

        assertTrue(options.hasOption("f"));
        assertFalse(options.hasOption("file"));
    }

    @Test
    public void testAddOption_requiredOption() {
        options.addOption("f", true, "File option");

        assertTrue(options.hasOption("f"));
        assertTrue(options.getRequiredOptions().contains("f"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(options.getOptions().size(), 2);
        assertTrue(options.getOptions().contains(option1));
        assertTrue(options.getOptions().contains(option2));
    }

    @Test
    public void testGetOptions_noOptions() {
        assertEquals(options.getOptions().size(), 0);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("f", "file", false, "File option");
        options.addOption(option);

        assertEquals(options.getOption("f"), option);
        assertEquals(options.getOption("file"), option);
    }

    @Test
    public void testGetOption_multipleOptions() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        options.addOption(option1);
        options.addOption(option2);

        assertEquals(options.getOption("f"), option1);
        assertEquals(options.getOption("file"), option1);
        assertEquals(options.getOption("d"), option2);
        assertEquals(options.getOption("directory"), option2);
    }

    @Test
    public void testHasOption() {
        options.addOption("f", "file", false, "File option");

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
    }

    @Test
    public void testHasOption_multipleOptions() {
        options.addOption("f", "file", false, "File option");
        options.addOption("d", "directory", false, "Directory option");

        assertTrue(options.hasOption("f"));
        assertTrue(options.hasOption("file"));
        assertTrue(options.hasOption("d"));
        assertTrue(options.hasOption("directory"));
    }

    @Test
    public void testHasOption_shortNameOnly() {
        options.addOption("f", false, "File option");

        assertTrue(options.hasOption("f"));
        assertFalse(options.hasOption("file"));
    }

    @Test
    public void testHasOption_nonExistingOption() {
        assertFalse(options.hasOption("f"));
        assertFalse(options.hasOption("file"));
    }

    @Test
    public void testHasLongOption() {
        options.addOption("f", "file", false, "File option");

        assertFalse(options.hasLongOption("f"));
        assertTrue(options.hasLongOption("file"));
    }

    @Test
    public void testHasShortOption() {
        options.addOption("f", "file", false, "File option");

        assertTrue(options.hasShortOption("f"));
        assertFalse(options.hasShortOption("file"));
    }

    @Test
    public void testGetOptionGroup() {
        Option option1 = new Option("f", "file", false, "File option");
        Option option2 = new Option("d", "directory", false, "Directory option");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        options.addOptionGroup(optionGroup);

        assertEquals(options.getOptionGroup(option1), optionGroup);
        assertEquals(options.getOptionGroup(option2), optionGroup);
    }

    @Test
    public void testGetOptionGroup_noGroup() {
        Option option = new Option("f", "file", false, "File option");
        options.addOption(option);

        assertNull(options.getOptionGroup(option));
    }

    @Test
    public void testToString() {
        Option option = new Option("f", "file", false, "File option");
        options.addOption(option);

        String expected = "[ Options: [ short {f=org.apache.commons.cli.Option: {0} [{1}]}] [ long {file=org.apache.commons.cli.Option: {2} [{3}]}]";
        assertEquals(options.toString(), expected);
    }
}