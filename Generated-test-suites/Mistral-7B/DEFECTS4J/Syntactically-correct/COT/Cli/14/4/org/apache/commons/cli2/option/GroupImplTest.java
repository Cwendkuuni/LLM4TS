package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.test.TestHelper;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.*;
import java.util.List;

public class GroupImplTest {

    private List<Option> options;
    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        options = newArrayList(
                Option.builder("o1")
                        .required(true)
                        .build(),
                Option.builder("o2")
                        .longOpt("longOpt2")
                        .required(false)
                        .build(),
                Option.builder("o3")
                        .longOpt("longOpt3")
                        .required(false)
                        .build(),
                Argument.builder("arg1")
                        .index(0)
                        .build(),
                Argument.builder("arg2")
                        .index(1)
                        .build());

        group = new GroupImpl(options, "MyGroup", "This is my group", 1, 2);
        commandLine = new DefaultCommandLine();
    }

    @Test
    public void testCanProcessWithValidOption() throws Exception {
        Arrange.given(commandLine, options);
        Act.when(group.canProcess(commandLine, "o1"));
        Assert.assertTrue(Assert.isTrue(Act.result));
    }

    @Test
    public void testCanProcessWithInvalidOption() throws Exception {
        Arrange.given(commandLine, newArrayList("myOption"));
        Act.when(group.canProcess(commandLine, "myOption"));
        Assert.assertFalse(Assert.isFalse(Act.result));
    }

    @Test
    public void testCanProcessWithAnonymousArgument() throws Exception {
        Arrange.given(commandLine, newArrayList("myArg"));
        Act.when(group.canProcess(commandLine, "arg1"));
        Assert.assertTrue(Assert.isTrue(Act.result));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() throws Exception {
        Arrange.given(commandLine, options);
        Act.when(group.validate(commandLine));
        Assert.fail();
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() throws Exception {
        Arrange.given(commandLine, Arrays.asList(options.subList(0, 3)));
        Act.when(group.validate(commandLine));
        Assert.fail();
    }

    @Test
    public void testProcessWithValidOptions() throws Exception {
        Arrange.given(commandLine, options);

        Act.when(group.process(commandLine, commandLine.listIterator(0)))
                .thenReturn(true)
                .thenReturn(true);

        Assert.assertTrue(group.process(commandLine, commandLine.listIterator(0)));
        Assert.assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testProcessWithInvalidOption() throws Exception {
        Arrange.given(commandLine, options);

        Act.when(group.process(commandLine, commandLine.listIterator(0)))
                .thenReturn(false);

        Act.when(group.process(commandLine, commandLine.listIterator(0)))
                .thenThrow(new OptionException("Invalid option: invalidOption"));

        Assert.assertFalse(group.process(commandLine, commandLine.listIterator(0)));
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testProcessWithAnonymousArgument() throws Exception {
        Arrange.given(commandLine, newArrayList("myArg1", "myArg2"));
        final ListIterator<String> iterator = commandLine.listIterator();

        Act.when(group.process(commandLine, iterator))
                .thenReturn(true)
                .thenReturn(true);

        Assert.assertTrue(group.process(commandLine, iterator));
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(2, commandLine.getArgs().size());
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder stringBuilder = new StringBuilder();
        group.appendUsage(stringBuilder, new HashSet<>(), null);

        final String expected = "MyGroup:\n" +
                "This is my group:\n" +
                "o1 <argument>\n" +
                "o2\n" +
                "o3\n" +
                "arg1 <argument>\n" +
                "arg2 <argument>\n";

        Assert.assertEquals(expected, stringBuilder.toString());
    }

    @Test
    public void testHelpLines() {
        final List<HelpLine> helpLines = group.helpLines(1, new HashSet<>(), null);

        final int expectedSize = options.size() + 1;

        Assert.assertEquals(expectedSize, helpLines.size());
    }

    @Test
    public void testGetOptions() {
        Assert.assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        Assert.assertEquals(Arrays.asList(options.get(options.size() - 1),
                options.get(options.size() - 2)), group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        final Option option = group.findOption("o1");

        Assert.assertEquals("o1", option.getLongOpt());
        Assert.assertEquals(1, option.getIndex());
    }

    @Test
    public void testGetMinimum() {
        Assert.assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        Assert.assertEquals(2, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        Assert.assertTrue(group.isRequired());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDefaultsThrowsException() {
        Act.when(group.defaults(commandLine));
        Assert.fail();
    }

    static class DefaultCommandLine implements WriteableCommandLine {
        List<Option> options = new ArrayList<>();
        List<String> args = new ArrayList<>();

        @Override
        public List<Option> getOptions() {
            return options;
        }

        @Override
        public List<String> getArgs() {
            return args;
        }

        @Override
        public String getName() {
            return "defaultCommandLine";
        }

        @Override
        public String getUsage() {
            return "";
        }

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addArg(String arg) {
            args.add(arg);
        }

        @Override
        public void clear() {
            options.clear();
            args.clear();
        }

        @Override
        public boolean looksLikeOption(String arg) {
            return false;
        }
    }

    static class newArrayList<T> extends ArrayList<T> {
        newArrayList(T... elements) {
            super(elements.length);
            for (T element : elements) {
                add(element);
            }
        }
    }

    static class Arrange {
        static void given(WriteableCommandLine commandLine, List<Option> options) {
            commandLine.clear();
            for (Option option : options) {
                commandLine.addOption(option);
            }
        }
    }

    static class Act {
        static boolean result;

        static void when(GroupImpl group, String arg) {
            result = group.canProcess(commandLine, arg);
        }

        static void when(GroupImpl group, ListIterator<String> iterator) {
            result = group.process(commandLine, iterator);
        }

        static void when(GroupImpl group, int min, int max) {
            result = group.validate(commandLine);
        }
    }

    static class TestHelper {
        static <T> List<T> newArrayList(T... elements) {
            return new ArrayList<T>(Arrays.asList(elements));
        }

        static <T> List<T> newArrayList(Collection<T> collection) {
            return new ArrayList<T>(collection);
        }

        static <T> List<T> newArrayList(Iterable<T> iterable) {
            ArrayList<T> list = new ArrayList<T>();
            for (T t : iterable) {
                list.add(t);
            }
            return list;
        }
    }
}