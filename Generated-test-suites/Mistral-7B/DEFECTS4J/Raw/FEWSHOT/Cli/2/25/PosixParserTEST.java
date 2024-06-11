package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArguments() {
        String[] arguments = {};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_NoOptions() {
        String[] arguments = {"foo", "bar", "baz"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"foo", "bar", "baz"}, result);
    }

    @Test
    public void testFlatten_SimpleOptions() {
        String[] arguments = {"--option1", "--option2=value1", "--option3"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, null, "option1");
        options.addOption("option2", true, null, "option2");
        options.addOption("option3", true, null, "option3");
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--option1", "--option2", "--option3"}, result);
    }

    @Test
    public void testFlatten_LongOptions() {
        String[] arguments = {"--long-option-with-arg=value1", "--long-option2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption1", true, null, "longOption1");
        options.addOption("longOption2", true, null, "longOption2");
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--long-option-with-arg=value1", "--long-option2"}, result);
    }

    @Test
    public void testFlatten_MixedOptions() {
        String[] arguments = {"-a", "--option1=value1", "-b", "--option2", "--"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, null, "option1");
        options.addOption("option2", true, null, "option2");
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"-a", "--option1=value1", "-b", "--option2", "--"}, result);
    }

    @Test
    public void testFlatten_BurstedOptions() {
        String[] arguments = {"-abcd", "--long-option-with-arg=value1", "--long-option2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, null, "option1");
        options.addOption("option2", true, null, "option2");
        options.addOption("longOption1", true, null, "longOption1");
        String[] result = parser.flatten(options, arguments, false);
        assertArrayEquals(new String[]{"--option1", "--", "-a", "--long-option-with-arg=value1", "--option2", "--long-option2"}, result);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] arguments = {"foo", "--option1=value1", "bar", "baz"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, null, "option1");
        String[] result = parser.flatten(options, arguments, true);
        assertArrayEquals(new String[]{"foo", "--option1=value1", "bar", "baz"}, result);
    }
}