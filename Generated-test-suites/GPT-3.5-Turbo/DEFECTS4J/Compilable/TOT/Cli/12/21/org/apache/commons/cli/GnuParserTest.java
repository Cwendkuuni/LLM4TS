package org.apache.commons.cli;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

    private GnuParser parser;
    private Options options;
    private String[] arguments;
    private boolean stopAtNonOption;

    @Before
    public void setUp() {
        parser = new GnuParser();
        options = new Options();
        arguments = new String[0];
        stopAtNonOption = false;
    }

    @Test
    public void testFlatten_NoOptions_NoArguments() {
        String[] flattenedArgs = parser.flatten(options, arguments, stopAtNonOption);
        assertEquals(0, flattenedArgs.length);
    }

    @Test
    public void testFlatten_NoOptions_WithArguments() {
        arguments = new String[]{"arg1", "arg2"};
        String[] flattenedArgs = parser.flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(arguments, flattenedArgs);
    }

    @Test
    public void testFlatten_OptionExistsForFirstChar() {
        Option option = new Option("a", "first-char-option");
        options.addOption(option);
        arguments = new String[]{"-a", "arg1"};
        String[] flattenedArgs = parser.flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(arguments, flattenedArgs);
    }

    @Test
    public void testFlatten_NoOptionExistsForFirstChar() {
        Option option = new Option("b", "second-char-option");
        options.addOption(option);
        arguments = new String[]{"-a", "arg1"};
        String[] expected = new String[]{"arg1"};
        String[] flattenedArgs = parser.flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, flattenedArgs);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        arguments = new String[]{"-a", "arg1", "-b"};
        boolean stopAtNonOption = true;
        String[] expected = new String[]{"-a", "arg1", "-b"};
        String[] flattenedArgs = parser.flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, flattenedArgs);
    }

    @Test
    public void testFlatten_DoubleDash() {
        arguments = new String[]{"--", "-a", "arg1"};
        String[] expected = new String[]{"--", "-a", "arg1"};
        String[] flattenedArgs = parser.flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, flattenedArgs);
    }

    @Test
    public void testFlatten_Hyphen() {
        arguments = new String[]{"-"};
        String[] expected = new String[]{"-"};
        String[] flattenedArgs = parser.flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, flattenedArgs);
    }

    @Test
    public void testFlatten_SpecialPropertiesOption() {
        arguments = new String[]{"-Dproperty=value"};
        String[] expected = new String[]{"-D", "property=value"};
        String[] flattenedArgs = parser.flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, flattenedArgs);
    }

    @Test
    public void testFlatten_InvalidOptionFormat() {
        arguments = new String[]{"-invalid"};
        String[] expected = new String[]{"-invalid"};
        String[] flattenedArgs = parser.flatten(options, arguments, stopAtNonOption);
        assertArrayEquals(expected, flattenedArgs);
    }
}