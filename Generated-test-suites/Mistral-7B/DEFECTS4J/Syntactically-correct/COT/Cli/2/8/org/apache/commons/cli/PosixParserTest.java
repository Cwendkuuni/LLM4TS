package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with no options and no arguments
     */
    @Test
    public void testFlattenNoOptionsNoArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[0];
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);
        Assert.assertEquals(0, parser.tokens.size());
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with no options and one argument
     */
    @Test
    public void testFlattenNoOptionsOneArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"arg"};
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"arg"}, result);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("arg", parser.tokens.get(0));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with one option and no arguments
     */
    @Test
    public void testFlattenOneOptionNoArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option"};
        Options options = new Options();
        options.addRequiredOption("option", false, "o");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option"}, result);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("--option", parser.tokens.get(0));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with one option and one argument
     */
    @Test
    public void testFlattenOneOptionOneArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option=arg"};
        Options options = new Options();
        options.addRequiredOption("option", true, "o");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option", "arg"}, result);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("--option", parser.tokens.get(0));
        Assert.assertEquals("arg", parser.tokens.get(1));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with one option requiring an array argument and one argument
     */
    @Test
    public void testFlattenOneOptionArrayArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option=value1,value2"};
        Options options = new Options();
        options.addRequiredOption("option", true, "o");
        options.addOption("option2", true, "o2");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option", "value1", "--option", "value2"}, result);
        Assert.assertEquals(5, parser.tokens.size());
        Assert.assertEquals("--option", parser.tokens.get(0));
        Assert.assertEquals("value1", parser.tokens.get(1));
        Assert.assertEquals("--option", parser.tokens.get(3));
        Assert.assertEquals("value2", parser.tokens.get(4));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with one option requiring an array argument and multiple arguments
     */
    @Test
    public void testFlattenOneOptionArrayMultipleArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option=value1,value2,value3"};
        Options options = new Options();
        options.addRequiredOption("option", true, "o");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option", "value1", "--option", "value2", "--option", "value3"}, result);
        Assert.assertEquals(6, parser.tokens.size());
        Assert.assertEquals("--option", parser.tokens.get(0));
        Assert.assertEquals("value1", parser.tokens.get(1));
        Assert.assertEquals("--option", parser.tokens.get(3));
        Assert.assertEquals("value2", parser.tokens.get(4));
        Assert.assertEquals("--option", parser.tokens.get(5));
        Assert.assertEquals("value3", parser.tokens.get(6));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with one option and a long argument
     */
    @Test
    public void testFlattenOneOptionLongArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--longOption=thisIsALongArgument"};
        Options options = new Options();
        options.addOption("longOption", true, "l");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "thisIsALongArgument"}, result);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("--longOption", parser.tokens.get(0));
        Assert.assertEquals("thisIsALongArgument", parser.tokens.get(1));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with one option and a long argument containing spaces
     */
    @Test
    public void testFlattenOneOptionLongArgumentWithSpaces() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--longOption=this Is A Long Argument"};
        Options options = new Options();
        options.addOption("longOption", true, "l");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption", "this%20Is%20A%20Long%20Argument"}, result);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("--longOption", parser.tokens.get(0));
        Assert.assertEquals("this Is A Long Argument", parser.tokens.get(1));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with one option and multiple long arguments
     */
    @Test
    public void testFlattenOneOptionMultipleLongArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--longOption1=value1", "--longOption2=value2"};
        Options options = new Options();
        options.addOption("longOption1", true, "l1");
        options.addOption("longOption2", true, "l2");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--longOption1", "value1", "--longOption2", "value2"}, result);
        Assert.assertEquals(5, parser.tokens.size());
        Assert.assertEquals("--longOption1", parser.tokens.get(0));
        Assert.assertEquals("value1", parser.tokens.get(1));
        Assert.assertEquals("--longOption2", parser.tokens.get(3));
        Assert.assertEquals("value2", parser.tokens.get(4));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with one option and an empty value
     */
    @Test
    public void testFlattenOneOptionEmptyValue() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option="};
        Options options = new Options();
        options.addOption("option", true, "o");
        try {
            parser.flatten(options, arguments, false);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("--option", parser.tokens.get(0));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with one option and invalid value
     */
    @Test
    public void testFlattenOneOptionInvalidValue() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option=invalid"};
        Options options = new Options();
        options.addOption("option", true, "o");
        try {
            parser.flatten(options, arguments, false);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("--option", parser.tokens.get(0));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with multiple options
     */
    @Test
    public void testFlattenMultipleOptions() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option1", "--option2=value2"};
        Options options = new Options();
        options.addOption("option1", false, "o1");
        options.addOption("option2", true, "o2");
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--option1");
        expected.add("--option2");
        expected.add("value2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        Assert.assertEquals(3, parser.tokens.size());
        Assert.assertEquals("--option1", parser.tokens.get(0));
        Assert.assertEquals("--option2", parser.tokens.get(1));
        Assert.assertEquals("value2", parser.tokens.get(2));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with multiple options and an option requiring an array argument
     */
    @Test
    public void testFlattenMultipleOptionsArrayArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option1", "--option2=value1,value2"};
        Options options = new Options();
        options.addOption("option1", false, "o1");
        options.addOption("option2", true, "o2");
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--option1");
        expected.add("--option2");
        expected.add("value1");
        expected.add("value2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        Assert.assertEquals(5, parser.tokens.size());
        Assert.assertEquals("--option1", parser.tokens.get(0));
        Assert.assertEquals("--option2", parser.tokens.get(1));
        Assert.assertEquals("value1", parser.tokens.get(3));
        Assert.assertEquals("value2", parser.tokens.get(4));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with multiple options and a long argument
     */
    @Test
    public void testFlattenMultipleOptionsLongArgument() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option1=value1", "--option2=thisIsALongArgument"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--option1");
        expected.add("value1");
        expected.add("--option2");
        expected.add("thisIsALongArgument");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        Assert.assertEquals(5, parser.tokens.size());
        Assert.assertEquals("--option1", parser.tokens.get(0));
        Assert.assertEquals("value1", parser.tokens.get(1));
        Assert.assertEquals("--option2", parser.tokens.get(3));
        Assert.assertEquals("thisIsALongArgument", parser.tokens.get(4));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with multiple options and mixed long and short arguments
     */
    @Test
    public void testFlattenMultipleOptionsMixedLongAndShortArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option1=value1", "--option2=value2,value3", "--option3=value4"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        options.addOption("option3", true, "o3");
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--option1");
        expected.add("value1");
        expected.add("--option2");
        expected.add("value2");
        expected.add("value3");
        expected.add("--option3");
        expected.add("value4");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        Assert.assertEquals(7, parser.tokens.size());
        Assert.assertEquals("--option1", parser.tokens.get(0));
        Assert.assertEquals("value1", parser.tokens.get(1));
        Assert.assertEquals("--option2", parser.tokens.get(3));
        Assert.assertEquals("value2", parser.tokens.get(4));
        Assert.assertEquals("value3", parser.tokens.get(5));
        Assert.assertEquals("--option3", parser.tokens.get(6));
        Assert.assertEquals("value4", parser.tokens.get(7));
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
     * with multiple options requiring array arguments
     */
    @Test
    public void testFlattenMultipleOptionsArrayArguments() {
        PosixParser parser = new PosixParser();
        String[] arguments = new String[]{"--option1=value1,value2", "--option2=value3,value4"};
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = new ArrayList<>();
        expected.add("--option1");
        expected.add("value1");
        expected.add("value2");
        expected.add("--option2");
        expected.add("value3");
        expected.add("value4");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        Assert.assertEquals(6, parser.tokens.size());
        Assert.assertEquals("--option1", parser.tokens.get(0));
        Assert.assertEquals("value1", parser.tokens.get(1));
        Assert.assertEquals("value2", parser.tokens.get(2));
        Assert.assertEquals("--option2", parser.tokens.get(4));
        Assert.assertEquals("value3", parser.tokens.get(5));
        Assert.assertEquals("value4", parser.tokens.get(6));
    }
}