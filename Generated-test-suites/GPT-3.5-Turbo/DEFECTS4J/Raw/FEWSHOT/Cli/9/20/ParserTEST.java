package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] arguments = { "-f", "filename", "--option", "value" };
	Properties properties = new Properties();
	properties.setProperty("filename", "file.txt");
        
	Options options = new Options();
        options.addOption("f", true, "File name");
	options.addOption("option", true, "Option value");
        
	Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments, properties);
        
        assertEquals("filename", cmd.getOptionValue("f"));
        assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptionsMissing() throws ParseException {
        String[] arguments = { "--option", "value" };
        Options options = new Options();
        options.addOption("f", true, "File name").setRequired(true);
	options.addOption("option", true, "Option value");
        
	Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments);
        
        parser.checkRequiredOptions();
    }

    @Test
    public void testCheckRequiredOptionsPresent() throws ParseException {
        String[] arguments = { "-f", "filename", "--option", "value" };
        Options options = new Options();
        options.addOption("f", true, "File name").setRequired(true);
	options.addOption("option", true, "Option value");
        
	Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments);
        
        parser.checkRequiredOptions();
    }
    
    @Test
    public void testProcessProperties() throws ParseException {
        String[] arguments = { "-f", "filename", "--option", "value" };
	Properties properties = new Properties();
	properties.setProperty("filename", "file.txt");

        Options options = new Options();
        options.addOption("f", true, "File name");
	options.addOption("option", true, "Option value");
        
	Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments, properties);
        
        assertEquals("file.txt", cmd.getOptionValue("f"));
        assertEquals("value", cmd.getOptionValue("option"));
    }
    
    @Test
    public void testProcessArgs() throws ParseException {
        String[] arguments = { "-f", "filename.txt", "--option", "value1", "value2" };
        Options options = new Options();
        options.addOption("f", true, "File name");
	options.addOption("option", true, "Option value");
        
	Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments);
        
        assertEquals("filename.txt", cmd.getOptionValue("f"));
        assertEquals("value1", cmd.getOptionValue("option"));
        assertEquals("value2", cmd.getArgList().get(0));
    }

    @Test(expected = MissingArgumentException.class)
    public void testProcessArgsMissing() throws ParseException {
        String[] arguments = { "-f", "--option", "value" };
        Options options = new Options();
        options.addOption("f", true, "File name");
	options.addOption("option", true, "Option value");
        
	Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments);
    }

    @Test
    public void testProcessOption() throws ParseException {
        String[] arguments = { "-f", "filename.txt", "--option", "value" };
        Options options = new Options();
        options.addOption("f", true, "File name");
	options.addOption("option", true, "Option value");
        
	Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments);
        
        assertEquals("filename.txt", cmd.getOptionValue("f"));
        assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOptionUnrecognized() throws ParseException {
        String[] arguments = { "-f", "filename.txt", "--option", "value" };
        Options options = new Options();
        options.addOption("option", true, "Option value");
        
	Parser parser = new ParserImpl();
        CommandLine cmd = parser.parse(options, arguments);
    }
}