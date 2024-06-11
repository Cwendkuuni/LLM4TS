package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("i", "input", true, "input file");
        Option option2 = new Option("o", "output", true, "output file");
        Option option3 = new Option("v", "verbose", false, "write verbose output");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        cmd.addOption(option2);
        cmd.addSwitch(option3, true);
        cmd.addValue(option, "test.txt");
        cmd.addValue(option2, "output.txt");

        Assert.assertTrue(cmd.hasOption(option));
        Assert.assertTrue(cmd.hasOption(option2));
        Assert.assertTrue(cmd.hasSwitch(option3));
        Assert.assertEquals("test.txt", cmd.getValues(option, null).get(0));
        Assert.assertEquals("output.txt", cmd.getValues(option2, null).get(0));
        Assert.assertTrue(cmd.getSwitch(option3, false));

        // Test parsing with invalid input
        try {
            cmd.parse();
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }

        // Test parsing with valid input
        List args = new ArrayList();
        args.add("cmd.exe");
        args.add("/c");
        args.add("java");
        args.add("-jar");
        args.add("commons-cli-utils-3.1.jar");
        args.add("org.apache.commons.cli2.commandline.WriteableCommandLineImplTest");
        args.add("-i");
        args.add("test.txt");
        args.add("-o");
        args.add("output.txt");
        args.add("-v");

        cmd = new WriteableCommandLineImpl(option, args);
        Assert.assertTrue(cmd.hasOption(option));
        Assert.assertTrue(cmd.hasOption(option2));
        Assert.assertTrue(cmd.hasSwitch(option3));
        Assert.assertEquals("test.txt", cmd.getValues(option, null).get(0));
        Assert.assertEquals("output.txt", cmd.getValues(option2, null).get(0));
        Assert.assertTrue(cmd.getSwitch(option3, false));

        // Test getProperties
        Option propertyOption = new Option("p", "property", true, "property name");
        cmd = new WriteableCommandLineImpl(propertyOption, null);
        cmd.addProperty(propertyOption, "propertyName", "propertyValue");
        Assert.assertEquals("propertyValue", cmd.getProperty(propertyOption, "defaultValue"));
        Assert.assertEquals(1, cmd.getProperties(propertyOption).size());

        // Test setDefaultValues and getValues
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        cmd.setDefaultValues(option, defaultValues);
        Assert.assertEquals(defaultValues, cmd.getValues(option, null));

        // Test addValue with multiple values
        List multipleValues = new ArrayList();
        multipleValues.add("value1");
        multipleValues.add("value2");
        cmd.addValue(option, multipleValues);
        Assert.assertEquals(multipleValues, cmd.getValues(option, null));
    }
}