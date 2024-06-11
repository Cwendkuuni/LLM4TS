package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.WriteableCommandLine;
import org.apache.commons.cli.WriteableCommandLineImpl;
import org.apache.commons.cli.commandline.WriteableCommandLineTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Test empty WriteableCommandLineImpl
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(null, Collections.emptyList());
        assertEquals(Collections.emptyList(), cmd.getOptions());
        assertEquals(Collections.emptySet(), cmd.getOptionTriggers());
        assertNull(cmd.getNormalised());

        // Test WriteableCommandLineImpl with options
        Options opts = new Options();
        Option opt1 = OptionBuilder.withLongOpt("opt1")
                .withValueSeparator(',')
                .withDescription("Option 1")
                .create();
        Option opt2 = OptionBuilder.withLongOpt("opt2")
                .withDescription("Option 2")
                .create();
        Option opt3 = OptionBuilder.withLongOpt("opt3")
                .withDescription("Option 3")
                .create();
        Option opt4 = OptionBuilder.withLongOpt("opt4")
                .withDescription("Option 4")
                .create();
        Option opt5 = OptionBuilder.withLongOpt("opt5")
                .withDescription("Option 5")
                .create();
        opts.addOption(opt1);
        opts.addOption(opt2);
        opts.addOption(opt3);
        opts.addOption(opt4);
        opts.addOption(opt5);

        List args = Arrays.asList("--opt1", "arg1", "--opt2", "arg2", "--opt1,arg3,arg4", "--opt3", "--", "file.txt");
        WriteableCommandLineImpl cmd1 = new WriteableCommandLineImpl(opt1, args);

        assertEquals(Arrays.asList(opt1, opt2, opt3, opt4, opt5), cmd1.getOptions());
        assertEquals(Arrays.asList("opt1", "opt1", "opt3"), cmd1.getOptionTriggers());
        assertEquals(Arrays.asList("arg1", "arg2", null, null, Arrays.asList("arg3", "arg4")), cmd1.getValues(opt1, null));
        assertTrue(cmd1.hasOption(opt1));
        assertSame(opt1, cmd1.getOption("opt1"));
        assertEquals(Collections.singletonList(opt1), cmd1.getOptions().subList(0, 1));
        assertEquals(Collections.singleton("opt1"), cmd1.getOptionTriggers().stream().limit(1).collect(Collectors.toList()));
        assertEquals("Option 1", cmd1.getOption(opt1).getDescription());
        assertEquals(Arrays.asList("arg1", "arg3", "arg4"), cmd1.getUndefaultedValues(opt1));
        assertFalse(cmd1.getSwitch(opt1, null));
        assertNull(cmd1.getProperty(opt1, null));

        WriteableCommandLineImpl cmd2 = new WriteableCommandLineImpl(opt2, args);
        assertEquals(Arrays.asList(opt1, opt2, opt3, opt4, opt5), cmd1.getOptions());
        assertEquals(Arrays.asList("opt1", "opt1", "opt3", "opt2"), cmd1.getOptionTriggers());
        assertEquals(Arrays.asList("arg1", null, null, Arrays.asList("arg3", "arg4"), null), cmd1.getValues(opt1, null));
        assertEquals(Arrays.asList("arg2"), cmd2.getValues(opt2, null));
        assertTrue(cmd2.hasOption(opt2));
        assertSame(opt2, cmd2.getOption("opt2"));
        assertEquals(Collections.singletonList(opt2), cmd2.getOptions().subList(1, 2));
        assertEquals(Collections.singleton("opt2"), cmd2.getOptionTriggers().stream().limit(2).collect(Collectors.toList()));
        assertEquals("Option 2", cmd2.getOption(opt2).getDescription());

        WriteableCommandLineImpl cmd3 = new WriteableCommandLineImpl(opt3, args);
        assertEquals(Arrays.asList(opt1, opt2, opt3, opt4, opt5), cmd1.getOptions());
        assertEquals(Arrays.asList("opt1", "opt1", "opt3", "opt2", "opt3"), cmd1.getOptionTriggers());
        assertEquals(Arrays.asList("arg1", null, Arrays.asList("arg3", "arg4"), null, null), cmd1.getValues(opt1, null));
        assertEquals(Collections.emptyList(), cmd3.getValues(opt3, null));
        assertTrue(cmd3.hasOption(opt3));
        assertSame(opt3, cmd3.getOption("opt3"));
        assertEquals(Collections.singletonList(opt3), cmd3.getOptions().subList(2, 3));
        assertEquals(Collections.singleton("opt3"), cmd3.getOptionTriggers().stream().limit(3).collect(Collectors.toList()));
        assertEquals("Option 3", cmd3.getOption(opt3).getDescription());

        WriteableCommandLineImpl cmd4 = new WriteableCommandLineImpl(opt4, args);
        assertEquals(Arrays.asList(opt1, opt2, opt3, opt4, opt5), cmd1.getOptions());
        assertEquals(Arrays.asList("opt1", "opt1", "opt3", "opt2", "opt3", "opt4"), cmd1.getOptionTriggers());
        assertEquals(Arrays.asList("arg1", null, Arrays.asList("arg3", "arg4"), null, null, null), cmd1.getValues(opt1, null));
        assertFalse(cmd4.hasOption(opt4));
        assertNull(cmd4.getOption("opt4"));
        assertEquals(Collections.emptyList(), cmd4.getOptions().subList(3, 4));
        assertEquals(Collections.emptyList(), cmd4.getOptionTriggers().stream().skip(3).collect(Collectors.toList()));

        WriteableCommandLineImpl cmd5 = new WriteableCommandLineImpl(opt5, args);
        assertEquals(Arrays.asList(opt1, opt2, opt3, opt4, opt5), cmd1.getOptions());
        assertEquals(Arrays.asList("opt1", "opt1", "opt3", "opt2", "opt3", "opt4", "opt5"), cmd1.getOptionTriggers());
        assertEquals(Arrays.asList("arg1", null, Arrays.asList("arg3", "arg4"), null, null, null, null), cmd1.getValues(opt1, null));
        assertFalse(cmd5.hasOption(opt5));
        assertNull(cmd5.getOption("opt5"));
        assertEquals(Collections.singletonList(opt5), cmd5.getOptions().subList(4, 5));
        assertEquals(Collections.singleton("opt5"), cmd5.getOptionTriggers().stream().skip(4).collect(Collectors.toList()));

        // Test WriteableCommandLineImpl with switch
        Option switchOpt = OptionBuilder.withLongOpt("switch")
                .withDescription("Switch")
                .create();
        opts.addOption(switchOpt);
        List argsSwitch = Arrays.asList("--switch");
        WriteableCommandLineImpl cmdSwitch1 = new WriteableCommandLineImpl(switchOpt, argsSwitch);
        assertFalse(cmdSwitch1.getSwitch(switchOpt, null));

        cmdSwitch1.addSwitch(switchOpt, true);
        assertTrue(cmdSwitch1.getSwitch(switchOpt, null));

        List argsSwitchOn = Arrays.asList("--switch");
        WriteableCommandLineImpl cmdSwitch2 = new WriteableCommandLineImpl(switchOpt, argsSwitchOn);
        assertTrue(cmdSwitch2.getSwitch(switchOpt, Boolean.FALSE));

        cmdSwitch2.addSwitch(switchOpt, false);
        assertFalse(cmdSwitch2.getSwitch(switchOpt, null));

        // Test WriteableCommandLineImpl with properties
        Option optProp = OptionBuilder.withLongOpt("optprop")
                .withDescription("Option with properties")
                .create();
        opts.addOption(optProp);
        Properties defaultProps = new Properties();
        defaultProps.setProperty("key1", "value1");
        defaultProps.setProperty("key2", "value2");
        WriteableCommandLineImpl cmdProp = new WriteableCommandLineImpl(optProp, Arrays.asList("--optprop", "key1=value3", "key2=value4"));
        assertEquals(defaultProps, cmdProp.getProperties(optProp));
        assertEquals(2, cmdProp.getProperties(optProp).size());
        assertEquals("value3", cmdProp.getProperty(optProp, "key1"));
        assertEquals("value4", cmdProp.getProperty(optProp, "key2"));
        assertEquals("value1", cmdProp.getProperty(optProp, "key1", "value1"));
        assertEquals("value2", cmdProp.getProperty(optProp, "key2", "value2"));
        assertNull(cmdProp.getProperty(optProp, "key3"));

        cmdProp.addProperty(optProp, "key3", "value3");
        assertEquals("value3", cmdProp.getProperty(optProp, "key3"));
        assertEquals(3, cmdProp.getProperties(optProp).size());

        WriteableCommandLineImpl cmdProp2 = new WriteableCommandLineImpl(optProp, Arrays.asList("--optprop", "key1=value3", "key1=value5"));
        Map<String, String> propMap = cmdProp2.getProperties();
        Map<String, String> mergedMap = new HashMap<>();
        mergedMap.putAll(cmdProp.getProperties(optProp));
        mergedMap.putAll(cmdProp2.getProperties(optProp));
        assertEquals("value3", mergedMap.get("key1"));
        assertEquals("value5", mergedMap.get("key1"));

        assertEquals(Arrays.asList("value1", "value3", "value5"), cmdProp2.getProperties(optProp).stringPropertyNames().stream()
                .map(propName -> cmdProp2.getProperty(optProp, propName))
                .collect(Collectors.toList()));

        WriteableCommandLineTest.assertWriteableCommandlineEquals(cmd1, "--opt1=arg1 --opt1=arg3,arg4 --opt3");
        WriteableCommandLineTest.assertWriteableCommandlineEquals(cmd2, "--opt2=arg2");
        WriteableCommandLineTest.assertWriteableCommandlineEquals(cmd3, "--opt3");
        WriteableCommandLineTest.assertWriteableCommandlineEquals(cmd4, "");
        WriteableCommandLineTest.assertWriteableCommandlineEquals(cmd5, "");
    }
}