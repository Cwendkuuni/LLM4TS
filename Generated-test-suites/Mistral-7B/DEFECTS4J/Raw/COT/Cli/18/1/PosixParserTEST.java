package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test case for flatten method with no options.
     */
    @Test
    public void testFlattenNoOptions() {
        String[] args = {"myApp", "arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("arg1");
        expected.add("arg2");
        expected.add("arg3");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for flatten method with single option.
     */
    @Test
    public void testFlattenSingleOption() {
        String[] args = {"myApp", "--option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("--option");
        expected.add("value");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for flatten method with multiple options.
     */
    @Test
    public void testFlattenMultipleOptions() {
        String[] args = {"myApp", "--option1", "value1", "--option2", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("--option1");
        expected.add("value1");
        expected.add("--option2");
        expected.add("value2");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for flatten method with long option.
     */
    @Test
    public void testFlattenLongOption() {
        String[] args = {"myApp", "--long-option", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("--long-option");
        expected.add("value");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for flatten method with option having multiple arguments.
     */
    @Test
    public void testFlattenOptionWithArguments() {
        String[] args = {"myApp", "--option", "value1", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("option", true, "Option description"), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("--option");
        expected.add("value1");
        expected.add("value2");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for flatten method with stopAtNonOption.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"myApp", "--option", "value", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, true);
        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("--option");
        expected.add("value");
        expected.add("nonOption");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for flatten method with single hyphen as an argument.
     */
    @Test
    public void testFlattenSingleHyphen() {
        String[] args = {"myApp", "--option", "-"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("--option-");

        assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    /**
     * Test case for burstToken method with valid option.
     */
    @Test
    public void testBurstTokenValidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "Option1 description");
        options.addOption("option2", true, "Option2 description");

        String[] args = {"myApp", "--option1--option2", "value"};
        parser.burstToken("option1--option2", options, false);

        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("--option1");
        expected.add("--option2");

        assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < parser.tokens.size(); i++) {
            assertEquals(expected.get(i), parser.tokens.get(i));
        }
    }

    /**
     * Test case for burstToken method with no option.
     */
    @Test
    public void testBurstTokenNoOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] args = {"myApp", "--noOption", "value"};
        assertThrows(ParseException.class, () -> parser.burstToken("noOption", options, false));

        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("--noOption");
        expected.add("value");

        assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < parser.tokens.size(); i++) {
            assertEquals(expected.get(i), parser.tokens.get(i));
        }
    }

    /**
     * Test case for burstToken method with invalid option and stopAtNonOption.
     */
    @Test
    public void testBurstTokenInvalidOptionStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] args = {"myApp", "--invalidOption--anotherOption"};
        assertThrows(ParseException.class, () -> parser.burstToken("invalidOption--anotherOption", options, true));

        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("--invalidOption--anotherOption");

        assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < parser.tokens.size(); i++) {
            assertEquals(expected.get(i), parser.tokens.get(i));
        }
    }

    /**
     * Test case for burstToken method with invalid option and no stopAtNonOption.
     */
    @Test
    public void testBurstTokenInvalidOptionNoStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] args = {"myApp", "--invalidOption--anotherOption"};
        parser.burstToken("invalidOption--anotherOption", options, false);

        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("--invalidOption");
        expected.add("anotherOption");

        assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < parser.tokens.size(); i++) {
            assertEquals(expected.get(i), parser.tokens.get(i));
        }
    }

    /**
     * Test case for burstToken method with invalid option and valid option in between.
     */
    @Test
    public void testBurstTokenInvalidOptionValidOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("validOption", true, "Valid option description");

        String[] args = {"myApp", "--invalidOption--validOption", "value"};
        parser.burstToken("invalidOption--validOption", options, false);

        List<String> expected = new ArrayList<>();
        expected.add("myApp");
        expected.add("--invalidOption");
        expected.add("validOption");

        assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < parser.tokens.size(); i++) {
            assertEquals(expected.get(i), parser.tokens.get(i));
        }
    }

    /**
     * Test case for init method.
     */
    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();

        assertFalse(parser.eatTheRest);
        assertNull(parser.currentOption);
        assertTrue(parser.tokens.isEmpty());
    }
}