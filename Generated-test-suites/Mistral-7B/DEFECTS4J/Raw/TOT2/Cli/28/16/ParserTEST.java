import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser class.
 */
public class ParserTest
{
    @Test
    public void testParseWithSimpleOptions() throws ParseException
    {
        String[] args = {"-a", "1", "-b", "2", "--", "-c", "3"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args);

        Option optionA = new Option("a", true, false, "valueA");
        Option optionB = new Option("b", true, false, "valueB");
        Option optionC = new Option("c", true, false, "valueC");

        parser.setOptions(new Options());
        parser.setOptions(parser.getOptions());

        parser.setOptions(new Options().addOption(optionA).addOption(optionB).addOption(optionC));

        List<Option> parsedOptions = cmd.getOptions();

        assertEquals(3, parsedOptions.size());
        assertTrue(parsedOptions.get(0) instanceof Option);
        assertEquals(optionA.getKey(), parsedOptions.get(0).getKey());
        assertEquals(optionA.isRequired(), parsedOptions.get(0).isRequired());
        assertEquals(optionA.hasArg(), parsedOptions.get(0).hasArg());
        assertEquals("valueA", parsedOptions.get(0).getValue());

        assertTrue(parsedOptions.get(1) instanceof Option);
        assertEquals(optionB.getKey(), parsedOptions.get(1).getKey());
        assertEquals(optionB.isRequired(), parsedOptions.get(1).isRequired());
        assertEquals(optionB.hasArg(), parsedOptions.get(1).hasArg());
        assertEquals("valueB", parsedOptions.get(1).getValue());

        assertTrue(parsedOptions.get(2) instanceof Option);
        assertEquals(optionC.getKey(), parsedOptions.get(2).getKey());
        assertEquals(optionC.isRequired(), parsedOptions.get(2).isRequired());
        assertEquals(optionC.hasArg(), parsedOptions.get(2).hasArg());
        assertEquals("valueC", parsedOptions.get(2).getValue());
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        String[] args = {"-a", "1", "--", "-c", "3"};
        Parser parser = new DefaultParser();
        parser.setOptions(new Options().addOption(new Option("a", true, false, "valueA"))
                .addOption(new Option("c", true, false, "valueC")));
        parser.parse(parser.getOptions(), args, false);
        fail("Expected ParseException but no exception was thrown.");
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException
    {
        String[] args = {"--invalidOption", "-a", "1", "-b", "2", "--", "-c", "3"};
        Parser parser = new DefaultParser();
        parser.setOptions(new Options().addOption(new Option("a", true, false, "valueA"))
                .addOption(new Option("b", true, false, "valueB"))
                .addOption(new Option("c", true, false, "valueC")));
        parser.parse(parser.getOptions(), args, false);
        fail("Expected ParseException but no exception was thrown.");
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB.valueB", "valueB");
        properties.setProperty("optionC.valueC.valueC1", "valueC1");
        properties.setProperty("optionC.valueC.valueC2", "valueC2");

        String[] args = {"--", "-DoptionB.valueB=valueB", "--", "-DoptionC.valueC.valueC1=valueC1", "--", "-DoptionC.valueC.valueC2=valueC2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args, properties, false);

        Option optionA = new Option("a", true, false, "valueA");
        Option optionB = new Option("b", true, false, "valueB");
        Option optionC = new Option("c", true, true, "valueC");

        parser.setOptions(new Options().addOption(optionA).addOption(optionB).addOption(optionC));

        List<Option> parsedOptions = cmd.getOptions();

        assertEquals(3, parsedOptions.size());
        assertTrue(parsedOptions.get(0) instanceof Option);
        assertEquals(optionA.getKey(), parsedOptions.get(0).getKey());
        assertEquals(optionA.isRequired(), parsedOptions.get(0).isRequired());
        assertEquals(optionA.hasArg(), parsedOptions.get(0).hasArg());
        assertEquals("valueA", parsedOptions.get(0).getValue());

        assertTrue(parsedOptions.get(1) instanceof Option);
        assertEquals(optionB.getKey(), parsedOptions.get(1).getKey());
        assertEquals(optionB.isRequired(), parsedOptions.get(1).isRequired());
        assertEquals(optionB.hasArg(), parsedOptions.get(1).hasArg());
        assertEquals("valueB", parsedOptions.get(1).getValue());

        assertTrue(parsedOptions.get(2) instanceof OptionGroup);
        OptionGroup optionGroupC = (OptionGroup) parsedOptions.get(2);
        List<Option> optionGroupCOptions = optionGroupC.getOptions();
        assertEquals(2, optionGroupCOptions.size());

        assertTrue(optionGroupCOptions.get(0) instanceof Option);
        Option optionC1 = new Option("valueC", true, false, "valueC1");
        assertEquals(optionC1.getKey(), optionGroupCOptions.get(0).getKey());
        assertEquals(optionC1.isRequired(), optionGroupCOptions.get(0).isRequired());
        assertEquals(optionC1.hasArg(), optionGroupCOptions.get(0).hasArg());
        assertEquals("valueC1", optionGroupCOptions.get(0).getValue());

        assertTrue(optionGroupCOptions.get(1) instanceof Option);
        Option optionC2 = new Option("valueC", true, false, "valueC2");
        assertEquals(optionC2.getKey(), optionGroupCOptions.get(1).getKey());
        assertEquals(optionC2.isRequired(), optionGroupCOptions.get(1).isRequired());
        assertEquals(optionC2.hasArg(), optionGroupCOptions.get(1).hasArg());
        assertEquals("valueC2", optionGroupCOptions.get(1).getValue());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidProperties() throws ParseException
    {
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB.valueB", "valueB");
        properties.setProperty("optionC.valueC", "valueC");

        String[] args = {"--", "-DoptionB.valueB=valueB", "--", "-DoptionC.valueC.valueC1=valueC1", "--", "-DoptionC.valueC.valueC2=valueC2"};
        Parser parser = new DefaultParser();
        parser.setOptions(new Options().addOption(new Option("a", true, false, "valueA"))
                .addOption(new Option("b", true, false, "valueB"))
                .addOption(new Option("c", true, true, "valueC")));
        parser.parse(parser.getOptions(), args, properties, false);
        fail("Expected ParseException but no exception was thrown.");
    }

    @Test
    public void testParseWithLongOptions() throws ParseException
    {
        String[] args = {"--option-a", "valueA", "--option-b--with-value-b", "valueB"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new Options(), args);

        Option optionA = new Option("optionA", true, false, "valueA");
        Option optionB = new Option("optionB", true, false, "valueB");

        parser.setOptions(new Options().addOption(optionA).addOption(optionB));

        List<Option> parsedOptions = cmd.getOptions();

        assertEquals(2, parsedOptions.size());
        assertTrue(parsedOptions.get(0) instanceof Option);
        assertEquals(optionA.getKey(), parsedOptions.get(0).getKey());
        assertEquals(optionA.isRequired(), parsedOptions.get(0).isRequired());
        assertEquals(optionA.hasArg(), parsedOptions.get(0).hasArg());
        assertEquals("valueA", parsedOptions.get(0).getValue());

        assertTrue(parsedOptions.get(1) instanceof Option);
        assertEquals(optionB.getKey(), parsedOptions.get(1).getKey());
        assertEquals(optionB.isRequired(), parsedOptions.get(1).isRequired());
        assertEquals(optionB.hasArg(), parsedOptions.get(1).hasArg());
        assertTrue(parsedOptions.get(1).getValue().startsWith("--with-value-b="));
        assertEquals("valueB", parsedOptions.get(1).getValue().substring(parsedOptions.get(1).getValue().lastIndexOf("=") + 1));
    }
}