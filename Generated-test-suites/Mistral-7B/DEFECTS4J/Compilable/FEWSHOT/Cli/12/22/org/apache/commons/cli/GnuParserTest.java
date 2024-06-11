package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class GnuParserTest {

    @Test
    public void testFlatten_NoOptions_NoHyphens() {
        String[] arguments = new String[]{"arg1", "arg2", "arg3"};
        Options options = new Options();

        String[] expectedArguments = new String[]{"arg1", "arg2", "arg3"};
        String[] resultArguments = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, resultArguments);
    }

    @Test
    public void testFlatten_NoOptions_WithHyphens() {
        String[] arguments = new String[]{"arg1", "-arg2", "-arg3", "arg4"};
        Options options = new Options();

        String[] expectedArguments = new String[]{"arg1", "-arg2", "-arg3", "arg4"};
        String[] resultArguments = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, resultArguments);
    }

    @Test
    public void testFlatten_SingleOption_NoValue() {
        String[] arguments = new String[]{"-o", "arg1"};
        Options options = new Options();
        options.addOption("o", true, "Option o");

        String[] expectedArguments = new String[]{"-o", "arg1"};
        String[] resultArguments = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, resultArguments);
    }

    @Test
    public void testFlatten_SingleOption_WithValue() {
        String[] arguments = new String[]{"-o", "arg1", "value"};
        Options options = new Options();
        options.addOption("o", true, "Option o");

        String[] expectedArguments = new String[]{"-o", "arg1", "value"};
        String[] resultArguments = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, resultArguments);
    }

    @Test
    public void testFlatten_MultipleOptions_NoValues_FirstWithTwoLetters() {
        String[] arguments = new String[]{"--foo", "-b", "-c", "--bar", "arg1"};
        Options options = new Options();
        options.addOption("foo", true, "Option foo");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        options.addOption("bar", true, "Option bar");

        String[] expectedArguments = new String[]{"--foo", "-b", "-c", "--bar", "arg1"};
        String[] resultArguments = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, resultArguments);
    }

    @Test
    public void testFlatten_MultipleOptions_Mixed_FirstWithTwoLetters() {
        String[] arguments = new String[]{"--foo", "-ab", "-c", "--bar=value", "arg1"};
        Options options = new Options();
        options.addOption("foo", true, "Option foo");
        options.addOption("a", true, "Option a");
        options.addOption("b", true, "Option b");
        options.addOption("c", true, "Option c");
        options.addOption("bar", true, "Option bar");

        String[] expectedArguments = new String[]{"--foo", "-a", "-b", "--bar=value", "arg1"};
        String[] resultArguments = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, resultArguments);
    }

    @Test
    public void testFlatten_MultipleOptions_StopAtNonOption() {
        String[] arguments = new String[]{"--foo", "-o", "value", "arg1"};
        Options options = new Options();
        options.addOption("foo", true, "Option foo");
        options.addOption("o", true, "Option o");

        String[] expectedArguments = new String[]{"--foo", "arg1"};
        String[] resultArguments = new GnuParser().flatten(options, arguments, true);

        assertArrayEquals(expectedArguments, resultArguments);
    }

    @Test
    public void testFlatten_SpecialPropertiesOption() {
        String[] arguments = new String[]{"-Dproperty1=value1", "-Dproperty2=value2"};
        Options options = new Options();

        String[] expectedArguments = new String[]{"-Dproperty1=value1", "-Dproperty2=value2"};
        String[] resultArguments = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, resultArguments);
    }

    @Test
    public void testFlatten_SpecialPropertiesOption_LongOptionFormat() {
        String[] arguments = new String[]{"--property1=value1", "--property2=value2"};
        Options options = new Options();

        String[] expectedArguments = new String[]{"--property1=value1", "--property2=value2"};
        String[] resultArguments = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, resultArguments);
    }

    @Test
    public void testFlatten_EmptyString() {
        String[] arguments = new String[]{"--"};
        Options options = new Options();

        String[] expectedArguments = new String[]{"--"};
        String[] resultArguments = new GnuParser().flatten(options, arguments, false);

        assertArrayEquals(expectedArguments, resultArguments);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_NullArguments() {
        Options options = new Options();
        new GnuParser().flatten(options, null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_NullOptions() {
        String[] arguments = new String[]{"--foo"};
        new GnuParser().flatten(null, arguments, false);
    }
}