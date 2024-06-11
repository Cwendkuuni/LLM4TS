package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
@MethodSorts(MethodSorts.NAME_ASCENDING)
public class GnuParserTest {

    private Options options;
    private String[] arguments;
    private boolean stopAtNonOption;
    private String[] expected;

    @Parameterized.Parameters(name = "{index}: flatten({0}, {1}, {2}) = {3}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                        {new Options(), new String[]{}, false, new String[]{}},
                        {new Options(), new String[]{"--"}, false, new String[]{"--"}},
                        {new Options(), new String[]{"-"}, false, new String[]{"-"}},
                        {new Options(), new String[]{"-a"}, false, new String[]{"-a"}},
                        {new Options(), new String[]{"-a", "--foo=bar"}, false, new String[]{"-a", "--foo=bar"}},
                        {new Options(), new String[]{"-a", "--foo"}, false, new String[]{"-a", "--foo"}},
                        {new Options(), new String[]{"-a", "-b"}, false, new String[]{"-a", "-b"}},
                        {new Options(), new String[]{"-a", "-b", "--foo=bar"}, false, new String[]{"-a", "-b", "--foo=bar"}},
                        {new Options(), new String[]{"-a", "-b", "-c"}, false, new String[]{"-a", "-b", "-c"}},
                        {new Options(), new String[]{"-a", "-b", "-c", "d"}, false, new String[]{"-a", "-b", "-c", "d"}},
                        {new Options(), new String[]{"-a", "-b", "-c", "d", "--"}, false, new String[]{"-a", "-b", "-c", "d", "--"}},
                        {new Options(), new String[]{"-a", "-b", "-c", "--foo=bar"}, false, new String[]{"-a", "-b", "-c", "--foo=bar"}},
                        {new Options(), new String[]{"-a", "-b", "-c", "--foo"}, false, new String[]{"-a", "-b", "-c", "--foo"}},
                        {new Options(), new String[]{"-a", "-b", "-c", "--foo", "-d"}, false, new String[]{"-a", "-b", "-c", "--foo", "-d"}},
                        {new Options(), new String[]{"-a", "-b", "-c", "--foo", "-d", "--"}, false, new String[]{"-a", "-b", "-c", "--foo", "-d", "--"}},
                        {new Options(), new String[]{"-a", "-b", "-c", "--foo=bar", "d"}, false, new String[]{"-a", "-b", "-c", "--foo=bar", "d"}},
                        {new Options(), new String[]{"-a", "-b", "-c", "--foo=bar", "d", "--"}, false, new String[]{"-a", "-b", "-c", "--foo=bar", "d", "--"}},
                        {new Options(), new String[]{"-a", "-b", "-c", "--foo=bar", "d", "--foo=baz"}, false, new String[]{"-a", "-b", "-c", "--foo=bar", "d", "--foo=baz"}},
                        {new Options(), new String[]{"-a", "-b", "-c", "--foo=bar", "d", "--foo=baz", "e"}, false, new String[]{"-a", "-b", "-c", "--foo=bar", "d", "--foo=baz", "e"}},
                        {new Options(), new String[]{"-a", "-b", "-c", "--foo=bar", "d", "--foo=baz", "e", "--"}, false, new String[]{"-a", "-b", "-c", "--foo=bar", "d", "--foo=baz", "e", "--"}},

                        // Test edge cases and exceptions
                        {new Options(), new String[]{"--foo"}, false, new String[]{"--foo"}},
                        {new Options(), new String[]{"-foo"}, false, new String[]{"-foo"}},
                        {new Options(), new String[]{"--foo="}, false, new String[]{"--foo="}},
                        {new Options(), new String[]{"--foo="}, true, new String[]{"--foo="}},
                        {new Options(), new String[]{"--foo=", "bar"}, false, new String[]{"--foo=", "bar"}},
                        {new Options(), new String[]{"--foo=", "bar"}, true, new String[]{"--foo=", "bar"}},
                        {new Options(), new String[]{"--foo=bar", "--bar"}, false, new String[]{"--foo=bar", "--bar"}},
                        {new Options(), new String[]{"--foo=bar", "--bar"}, true, new String[]{"--foo=bar", "--bar"}},
                        {new Options(), new String[]{"--foo=bar", "--foo"}, false, new String[]{"--foo=bar", "--foo"}},
                        {new Options(), new String[]{"--foo=bar", "--foo"}, true, new String[]{"--foo=bar", "--foo"}},
                        {new Options(), new String[]{"--foo=bar", "-b"}, false, new String[]{"--foo=bar", "-b"}},
                        {new Options(), new String[]{"--foo=bar", "-b"}, true, new String[]{"--foo=bar", "-b"}},
                        {new Options(), new String[]{"--foo=bar", "-b", "--"}, false, new String[]{"--foo=bar", "-b", "--"}},
                        {new Options(), new String[]{"--foo=bar", "-b", "--"}, true, new String[]{"--foo=bar", "-b", "--"}},
                        {new Options(), new String[]{"--foo=bar", "-b", "b"}, false, new String[]{"--foo=bar", "-b", "b"}},
                        {new Options(), new String[]{"--foo=bar", "-b", "b"}, true, new String[]{"--foo=bar", "-b", "b"}},
                        {new Options(), new String[]{"--foo=bar", "-b", "b", "a"}, false, new String[]{"--foo=bar", "-b", "b", "a"}},
                        {new Options(), new String[]{"--foo=bar", "-b", "b", "a"}, true, new String[]{"--foo=bar", "-b", "b", "a"}},
                        {new Options(), new String[]{"--foo=bar", "-b", "b", "a", "a"}, false, new String[]{"--foo=bar", "-b", "b", "a", "a"}},
                        {new Options(), new String[]{"--foo=bar", "-b", "b", "a", "a"}, true, new String[]{"--foo=bar", "-b", "b", "a", "a"}}
                });
    }

    @Parameterized.Constructor
    public GnuParserTest(Options options, String[] arguments, boolean stopAtNonOption, String[] expected) {
        this.options = options;
        this.arguments = arguments;
        this.stopAtNonOption = stopAtNonOption;
        this.expected = expected;
    }

    @Test
    public void testFlatten() {
        try {
            String[] actual = new GnuParser().flatten(options, arguments, stopAtNonOption);
            Assert.assertArrayEquals(expected, actual);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}