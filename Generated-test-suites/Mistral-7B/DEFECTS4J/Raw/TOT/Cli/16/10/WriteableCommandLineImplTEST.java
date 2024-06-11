package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option option = new Option("o", "output", true, "Output file path");
        Option propertyOption = new Option("p", "prefix", true, "Prefix for option name");
        List<String> arguments = new ArrayList<>();
        arguments.add("--output");
        arguments.add("test.txt");
        arguments.add("--prefix=test");
        arguments.add("--o=file.txt");
        arguments.add("--p=testPrefix");

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, arguments);

        // When
        cmd.addOption(propertyOption);
        cmd.addProperty(propertyOption, "property", "propertyValue");

        // Then
        CommandLine parsedCmd = new DefaultParser().parse(option, arguments.toArray(new String[0]));
        WriteableCommandLine expectedCmd = new WriteableCommandLineImpl(option, new ArrayList<>(List.of("--output", "test.txt", "--prefix=test", "--o=file.txt", "--p=testPrefix")));
        expectedCmd.addOption(propertyOption);
        expectedCmd.addProperty(propertyOption, "property", "propertyValue");

        // Check options
        Assert.assertEquals(expectedCmd.getOptions(), cmd.getOptions());

        // Check property
        Assert.assertEquals(expectedCmd.getProperty("prefix"), cmd.getProperty("prefix"));
        Assert.assertEquals(expectedCmd.getProperty("output"), cmd.getProperty("output"));

        // Check values
        List<Object> expectedValues = expectedCmd.getValues(option, null);
        List<Object> cmdValues = cmd.getValues(option, null);
        Assert.assertEquals(expectedValues, cmdValues);

        // Check switch
        Boolean expectedSwitch = expectedCmd.getSwitch(option, null);
        Boolean cmdSwitch = cmd.getSwitch(option, null);
        Assert.assertEquals(expectedSwitch, cmdSwitch);

        // Check property values
        Properties expectedProperties = expectedCmd.getProperties(option);
        Properties cmdProperties = cmd.getProperties(option);
        Assert.assertEquals(expectedProperties, cmdProperties);

        // Check toString
        Assert.assertEquals(expectedCmd.toString(), cmd.toString());
    }
}