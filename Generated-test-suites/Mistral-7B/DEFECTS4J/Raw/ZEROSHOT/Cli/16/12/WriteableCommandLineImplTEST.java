import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        String[] args = new String[]{"--optionA", "value1", "--optionB"};
        Option optionA = new Option("a", "optionA").required(false);
        Option optionB = new Option("b", "optionB").required(false);

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(optionA, new ArrayList<>(List.of(args)));

        // When
        writeableCommandLine.addOption(optionB);
        writeableCommandLine.addValue(optionA, "value2");
        writeableCommandLine.addSwitch(optionA, true);
        writeableCommandLine.addProperty(optionA, "propertyA", "valuePropertyA");

        // Then
        Assert.assertEquals(2, writeableCommandLine.getOptions().size());
        Assert.assertEquals(optionA, writeableCommandLine.getOption(optionA.getPreferredName()));
        Assert.assertEquals(optionB, writeableCommandLine.getOption(optionB.getPreferredName()));

        List values = writeableCommandLine.getValues(optionA, null);
        Assert.assertNotNull(values);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value2", values.get(1));

        Assert.assertTrue(writeableCommandLine.hasOption(optionA));
        Assert.assertTrue(writeableCommandLine.hasOption(optionB));

        Boolean switchValue = writeableCommandLine.getSwitch(optionA, false);
        Assert.assertTrue(switchValue);

        String propertyValue = writeableCommandLine.getProperty(optionA, "propertyA");
        Assert.assertEquals("valuePropertyA", propertyValue);

        Set properties = writeableCommandLine.getProperties(optionA);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("propertyA", properties.iterator().next());

        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(3, normalised.size());
        Assert.assertEquals("--optionA", ((String) normalised.get(0)).trim());

        // Test toString method
        String toString = writeableCommandLine.toString();
        Assert.assertEquals("--optionA value1 --optionA value2 --optionB", toString);

        // Test hasOption method
        Assert.assertTrue(writeableCommandLine.hasOption(optionA));
        Assert.assertFalse(writeableCommandLine.hasOption(new Option("c", "optionC")));

        // Test getOption method
        Option option = writeableCommandLine.getOption("optionA");
        Assert.assertEquals(optionA, option);

        // Test getValues method
        List values1 = writeableCommandLine.getValues(optionA, null);
        Assert.assertNotNull(values1);
        Assert.assertEquals(2, values1.size());
        Assert.assertEquals("value2", values1.get(1));

        // Test getUndefaultedValues method
        List values2 = writeableCommandLine.getUndefaultedValues(optionA);
        Assert.assertNotNull(values2);
        Assert.assertEquals(0, values2.size());

        // Test getSwitch method
        Boolean switchValue1 = writeableCommandLine.getSwitch(optionA, null);
        Assert.assertTrue(switchValue1);

        // Test getProperty method
        String propertyValue1 = writeableCommandLine.getProperty(optionA, "propertyA");
        Assert.assertEquals("valuePropertyA", propertyValue1);

        // Test getProperties method
        Set properties1 = writeableCommandLine.getProperties(optionA);
        Assert.assertEquals(1, properties1.size());
        Assert.assertEquals("propertyA", properties1.iterator().next());

        // Test getProperties method with no property option
        Set properties2 = writeableCommandLine.getProperties();
        Assert.assertTrue(properties2.isEmpty());
    }
}