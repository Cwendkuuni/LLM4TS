package org.apache.commons.cli;
import org.apache.commons.cli.*;
import org.junit.*;

public class PosixParserTest {
    
    private PosixParser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();
    }
    
    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"-a", "value1", "argument1", "--", "-b", "value2"};
        String[] expected = {"-a", "value1", "argument1", "--", "-b", "value2"};
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_ContinueParsing() {
        String[] arguments = {"-a", "value1", "argument1", "--", "-b", "value2"};
        String[] expected = {"-a", "value1", "argument1", "--", "-b", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_SingleHyphen() {
        String[] arguments = {"-"};
        String[] expected = {"-"};
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_CurrentOptionNotNull() {
        String[] arguments = {"-a", "value1", "-b", "value2"};
        String[] expected = {"-a", "value1", "-b", "value2"};
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_CurrentOptionNotNull_ArgumentValue() {
        String[] arguments = {"-a", "value1"};
        String[] expected = {"-a", "value1"};
        options.addOption("a", true, "Option a");
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_CurrentOptionNotNull_ArgumentValues() {
        String[] arguments = {"-b", "value1", "value2"};
        String[] expected = {"-b", "value1", "value2"};
        options.addOption("b", true, "Option b");
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_CurrentOptionNotNull_NoArgumentValue() {
        String[] arguments = {"-a"};
        String[] expected = {"-a"};
        options.addOption("a", false, "Option a");
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_OptionExists() {
        String[] arguments = {"-a", "value1"};
        String[] expected = {"-a", "value1"};
        options.addOption("a", true, "Option a");
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_OptionDoesNotExist_StopAtNonOption() {
        String[] arguments = {"-c", "value1", "argument1"};
        String[] expected = {"-c", "value1", "argument1"};
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_OptionDoesNotExist_ContinueParsing() {
        String[] arguments = {"-c", "value1", "argument1"};
        String[] expected = {"-c"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken() {
        String[] arguments = {"-ab", "value1"};
        String[] expected = {"-a", "-b", "value1"};
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken_StopAtNonOption() {
        String[] arguments = {"-abc", "value1", "argument1"};
        String[] expected = {"-a", "-b", "-c", "value1", "argument1"};
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_BurstToken_ContinueParsing() {
        String[] arguments = {"-abc", "value1", "argument1"};
        String[] expected = {"-a", "-b", "-c"};
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_NotHandledByPreviousRules_StopAtNonOption() {
        String[] arguments = {"value1", "value2"};
        String[] expected = {"--", "value1", "value2"};
        String[] result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_NotHandledByPreviousRules_ContinueParsing() {
        String[] arguments = {"value1", "value2"};
        String[] expected = {"value1", "value2"};
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(expected, result);
    }
    
    @After
    public void tearDown() {
        parser = null;
        options = null;
    }
}