package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.Argument;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.WriteableCommandLine;
import org.apache.commons.cli.WriteableCommandLineImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = OptionBuilder.withLongOpt("myoption").withDescription("my description").build();
        WriteableCommandLine cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertNull(cmd.getOptions());
        assertEquals(option, cmd.getOption(option.getLongOpt()));
        assertEquals(Collections.emptyList(), cmd.getValues(option, null));
        assertFalse(cmd.hasOption(option));
        assertNull(cmd.getSwitch(option, null));
        assertNull(cmd.getProperty(null));
        assertNull(cmd.getProperty(option, null));
        assertNull(cmd.getProperties());
        assertFalse(cmd.looksLikeOption("myoption"));
        assertEquals("myoption", cmd.toString());
    }

    @Test
    public void testAddOption() {
        Option option = OptionBuilder.withLongOpt("myoption").withDescription("my description").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, Collections.emptyList());
        cmd.addOption(option);
        assertEquals(Collections.singletonList(option), cmd.getOptions());
        assertEquals(option, cmd.getOption(option.getLongOpt()));
        assertEquals(Collections.emptyList(), cmd.getValues(option, null));
        assertFalse(cmd.hasOption(option));
        assertNull(cmd.getSwitch(option, null));
        assertNull(cmd.getProperty(null));
        assertNull(cmd.getProperty(option, null));
        assertNull(cmd.getProperties());
        assertFalse(cmd.looksLikeOption("myoption"));
        assertEquals("myoption", cmd.toString());
    }

    @Test
    public void testAddValue() {
        Option option = OptionBuilder.withLongOpt("myoption").withValueSeparator('=').build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, Collections.emptyList());
        cmd.addOption(option);
        cmd.addValue(option, "value1");
        cmd.addValue(option, "value2");
        assertEquals(Collections.singletonList(option), cmd.getOptions());
        assertEquals(option, cmd.getOption(option.getLongOpt()));
        assertEquals(Arrays.asList("value1", "value2"), cmd.getValues(option, null));
        assertFalse(cmd.hasOption(option));
        assertNull(cmd.getSwitch(option, null));
        assertNull(cmd.getProperty(null));
        assertNull(cmd.getProperty(option, null));
        assertNull(cmd.getProperties());
        assertFalse(cmd.looksLikeOption("myoption"));
        assertEquals("myoption=value1 value2", cmd.toString());
    }

    @Test
    public void testAddSwitch() {
        Option option = OptionBuilder.withLongOpt("myswitch").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, Collections.emptyList());
        cmd.addOption(option);
        cmd.addSwitch(option, true);
        assertEquals(Collections.singletonList(option), cmd.getOptions());
        assertEquals(option, cmd.getOption(option.getLongOpt()));
        assertEquals(Collections.emptyList(), cmd.getValues(option, null));
        assertTrue(cmd.hasOption(option));
        assertTrue(cmd.getSwitch(option, false));
        assertNull(cmd.getProperty(null));
        assertNull(cmd.getProperty(option, null));
        assertNull(cmd.getProperties());
        assertFalse(cmd.looksLikeOption("myoption"));
        assertEquals("-m", cmd.toString());
    }

    @Test
    public void testHasOption() {
        Option option = OptionBuilder.withLongOpt("myoption").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertTrue(cmd.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = OptionBuilder.withLongOpt("myoption").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertEquals(option, cmd.getOption(option.getLongOpt()));
    }

    @Test
    public void testGetValues() {
        Option option = OptionBuilder.withLongOpt("myoption").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Arrays.asList("value1", "value2"));
        assertEquals(Arrays.asList("value1", "value2"), cmd.getValues(option, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = OptionBuilder.withLongOpt("myoption").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Arrays.asList("value1", "value2"));
        assertEquals(Collections.emptyList(), cmd.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = OptionBuilder.withLongOpt("myswitch").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, false));
        assertTrue(cmd.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        Option option = OptionBuilder.withLongOpt("myproperty").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        cmd.addProperty(option, "myproperty", "value1");
        assertEquals("value1", cmd.getProperty(option, "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        Option option = OptionBuilder.withLongOpt("myproperty").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        Properties properties = new Properties();
        properties.setProperty("myproperty", "value1");
        cmd.addProperty(option, "myproperty", "value1");
        assertEquals(properties, cmd.getProperties());
    }

    @Test
    public void testGetProperties() {
        Option option = OptionBuilder.withLongOpt("myproperty").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        Properties properties = new Properties();
        properties.setProperty("myproperty", "value1");
        cmd.addProperty(option, "myproperty", "value1");
        assertEquals(properties, cmd.getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        Option option = OptionBuilder.withLongOpt("myoption").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertTrue(cmd.looksLikeOption("--myoption"));
        assertFalse(cmd.looksLikeOption("--otheropt"));
    }

    @Test
    public void testToString() {
        Option option = OptionBuilder.withLongOpt("myoption").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertEquals("myoption", cmd.toString());
    }

    @Test
    public void testGetNormalised() {
        Option option = OptionBuilder.withLongOpt("myoption").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, Collections.emptyList());
        assertEquals(Collections.emptyList(), cmd.getNormalised());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = OptionBuilder.withLongOpt("myoption").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        List defaultValues = Arrays.asList("value1", "value2");
        cmd.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, cmd.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = OptionBuilder.withLongOpt("myswitch").build();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        cmd.setDefaultSwitch(option, true);
        assertTrue(cmd.getSwitch(option, false));
        assertTrue(cmd.getSwitch(option, true));
    }
}