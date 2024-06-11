import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option option = Option.builder("o").longOpt("option")
                .valueSeparator('=')
                .hasArg(true)
                .required(false)
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option=value1", "--option=value2"));

        // When
        List values = commandLine.getValues(option, null);

        // Then
        Assert.assertEquals(Arrays.asList("value1", "value2"), values);

        // Given
        Option switchOption = Option.builder("s").longOpt("switch")
                .valueSeparator(null)
                .hasArg(false)
                .build();

        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option=value1", "--switch", "--option=value2"));

        // When
        Boolean switchValue = commandLine.getSwitch(switchOption, false);

        // Then
        Assert.assertTrue(switchValue);

        // Given
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option=value1", "--no-switch", "--option=value2"));

        // When
        switchValue = commandLine.getSwitch(switchOption, false);

        // Then
        Assert.assertFalse(switchValue);

        // Given
        try {
            commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--invalid-option"));
        } catch (ParseException e) {
            // Then
            Assert.assertEquals("Unrecognized option: invalid-option", e.getMessage());
        }
    }
}