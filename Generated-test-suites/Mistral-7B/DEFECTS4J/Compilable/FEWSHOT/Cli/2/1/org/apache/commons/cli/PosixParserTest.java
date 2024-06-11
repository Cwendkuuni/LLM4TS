package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() {
        Options options = new Options();
        String[] args = {"--foo"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--foo", result[0]);
    }

    @Test
    public void testFlattenWithSingleValueOption() {
        Options options = new Options();
        options.addOption("f", "foo", false, "Print foo");
        String[] args = {"-f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("-f", result[0]);
    }

    @Test
    public void testFlattenWithValueOption() {
        Options options = new Options();
        options.addOption("f", "foo", true, "Print foo [value]");
        String[] args = {"-f", "bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("-f", result[0]);
        assertEquals("bar", result[1]);
    }

    @Test
    public void testFlattenWithMultipleValueOptions() {
        Options options = new Options();
        options.addOption("f", "foo", true, "Print foo [value]");
        options.addOption("b", "bar", true, "Print bar [value]");
        String[] args = {"-f", "foo1", "-b", "bar1", "-f", "foo2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(6, result.length);
        assertEquals("-f", result[0]);
        assertEquals("foo1", result[1]);
        assertEquals("-f", result[2]);
        assertEquals("foo2", result[3]);
        assertEquals("-b", result[4]);
        assertEquals("bar1", result[5]);
    }

    @Test
    public void testFlattenWithLongOption() {
        Options options = new Options();
        options.addOption("longOption", "Description for long option", false, "Print long option");
        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(1, result.length);
        assertEquals("--longOption", result[0]);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        Options options = new Options();
        options.addOption("longOption", "Description for long option", true, "Print long option [value]");
        String[] args = {"--longOption", "value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--longOption", result[0]);
        assertEquals("value", result[1]);
    }

    @Test
    public void testFlattenWithMultipleLongOptions() {
        Options options = new Options();
        options.addOption("longOption1", "Description for long option1", false, "Print long option1");
        options.addOption("longOption2", "Description for long option2", false, "Print long option2");
        String[] args = {"--longOption1", "--longOption2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(2, result.length);
        assertEquals("--longOption1", result[0]);
        assertEquals("--longOption2", result[1]);
    }

    @Test
    public void testFlattenWithMixedOptions() {
        Options options = new Options();
        options.addOption("f", "foo", true, "Print foo [value]");
        options.addOption("longOption", "Description for long option", false, "Print long option");
        String[] args = {"-f", "value1", "--longOption", "-f", "value2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(5, result.length);
        assertEquals("-f", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("--longOption", result[2]);
        assertEquals("-f", result[3]);
        assertEquals("value2", result[4]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("f", "foo", true, "Print foo [value]");
        String[] args = {"--nonOption"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(1, result.length);
        assertEquals("--nonOption", result[0]);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        String[] args = {"---foo---bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar", result[1]);
        assertEquals(null, result[2]);
    }

    @Test
    public void testBurstTokenWithValue() {
        Options options = new Options();
        String[] args = {"---foo---bar=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(4, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar", result[1]);
        assertEquals("value", result[2]);
        assertEquals(null, result[3]);
    }

    @Test
    public void testBurstTokenWithMultipleOptions() {
        Options options = new Options();
        options.addOption("f", "foo", true, "Print foo [value]");
        String[] args = {"---foo---bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        assertEquals(3, result.length);
        assertEquals("--foo", result[0]);
        assertEquals("--bar", result[1]);
        assertNull(result[2]);
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        Options options = new Options();
        String[] args = {"---nonOption---foo---bar"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        assertEquals(2, result.length);
        assertEquals("---nonOption---foo", result[0]);
        assertEquals("--bar", result[1]);
    }
}