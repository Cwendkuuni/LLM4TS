package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.ArgumentsBuilder;
import org.apache.commons.cli2.builder.OptionBuilder;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() throws ParseException {
        Option optionA = OptionBuilder.withLongOpt("optionA")
                .withArgName("argA")
                .isRequired()
                .withValueSeparator(':')
                .hasArg()
                .create();

        Option optionB = OptionBuilder.withLongOpt("optionB")
                .withArgName("argB")
                .isRequired()
                .create();

        Option optionC = OptionBuilder.withLongOpt("optionC")
                .withArgName("argC")
                .withValueSeparator(':')
                .hasArg()
                .create();

        ArgumentsBuilder argumentsBuilder = new ArgumentsBuilder();
        CommandLine cmd = argumentsBuilder.parse("--optionA=valA --optionB=valB --optionC=valC");

        WriteableCommandLineImpl writeableCmd = new WriteableCommandLineImpl(optionA, Collections.singletonList(cmd));

        writeableCmd.addOption(optionB);
        writeableCmd.addValue(optionB, "valB2");

        writeableCmd.addOption(optionC);
        writeableCmd.addValue(optionC, "valC1,valC2");

        writeableCmd.addSwitch(optionB, true);

        writeableCmd.addProperty(optionA, "propertyA", "propertyValueA");
        writeableCmd.addProperty(optionC, "propertyC", "propertyValueC");

        Assert.assertEquals("valA,valA:valA,valB:valB2,valB:valB2,valC:valC1,valC:valC2", writeableCmd.toString());
        Assert.assertEquals(2, writeableCmd.getOptions().size());
        Assert.assertEquals("valB2", writeableCmd.getValues(optionB, null).get(0).toString());
        Assert.assertEquals("valC1,valC2", writeableCmd.getValues(optionC, null).toString());
        Assert.assertEquals(true, writeableCmd.getSwitch(optionB, false));
        Assert.assertEquals("propertyValueA", writeableCmd.getProperty(optionA, "propertyA", "defaultValueA"));
        Assert.assertEquals("propertyValueC", writeableCmd.getProperty(optionC, "propertyC", "defaultValueC"));

        Assert.assertEquals(1, writeableCmd.getUndefaultedValues(optionB).size());
        Assert.assertEquals(Collections.EMPTY_LIST, writeableCmd.getUndefaultedValues(optionC));
    }
}