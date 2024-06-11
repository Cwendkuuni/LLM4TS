package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.OptionBuilder;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.commandline.SimpleParser;
import org.apache.commons.cli2.exceptions.ParseException;
import org.apache.commons.cli2.group.GroupImpl;
import org.apache.commons.cli2.test.TestUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GroupImplTest {

    @Test
    public void testGroupImplCreation() {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .withArgName("ARG1")
                .build();
        options.add(option1);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .withArgName("ARG2")
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description");

        assertEquals("groupName", group.getName());
        assertEquals("Group description", group.getDescription());
        assertEquals(options, group.getOptions());
        assertTrue(group.isRequired());
        assertEquals(2, group.getMinimum());
        assertEquals(2, group.getMaximum());
        assertEquals(Collections.emptyList(), group.getAnonymous());

        assertNull(group.getTriggers());
        assertEquals(Collections.singletonMap("option1", option1), group.getOptionMap());
        assertEquals(Collections.singleton("option1"), group.getPrefixes());
    }

    @Test
    public void testSimpleGroup() throws ParseException, OptionException {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description");
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Parser parser = new SimpleParser();
        parser.parse(commandLine, group.getOptions().toArray(new Option[0]));

        assertTrue(commandLine.hasOption("groupName"));
        assertTrue(commandLine.hasOption("option1"));
    }

    @Test
    public void testGroupWithAnonymous() throws ParseException, OptionException {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        Argument arg = OptionBuilder.withLongOpt("argument")
                .withDescription("Description for argument")
                .withArgName("ARG")
                .isRequired()
                .build();
        options.add(arg);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description");
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Parser parser = new SimpleParser();
        parser.parse(commandLine, group.getOptions().toArray(new Option[0]));

        assertTrue(commandLine.hasOption("groupName"));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasArgument("argument"));
        assertEquals("ARG", commandLine.getArgument("argument"));
    }

    @Test
    public void testGroupWithMinimum() throws ParseException, OptionException {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 1, 3);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Parser parser = new SimpleParser();
        parser.parse(commandLine, group.getOptions().toArray(new Option[0]));

        assertTrue(commandLine.hasOption("groupName"));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertTrue(commandLine.hasNoOption("option3"));
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMinimumAndLessOptions() throws ParseException, OptionException {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 3, 3);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Parser parser = new SimpleParser();
        parser.parse(commandLine, group.getOptions().toArray(new Option[0]));

        assertTrue(commandLine.hasOption("groupName"));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertTrue(commandLine.hasNoOption("option3"));

        // Should throw an exception since minimum is 3
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMaximum() throws ParseException, OptionException {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 0, 1);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Parser parser = new SimpleParser();
        parser.parse(commandLine, group.getOptions().toArray(new Option[0]));

        assertTrue(commandLine.hasOption("groupName"));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertTrue(commandLine.hasNoOption("option3"));

        // Should throw an exception since maximum is 1
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMaximumAndMoreOptions() throws ParseException, OptionException {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 0, 1);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Parser parser = new SimpleParser();
        parser.parse(commandLine, group.getOptions().toArray(new Option[0]));

        assertTrue(commandLine.hasOption("groupName"));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertTrue(commandLine.hasOption("option3"));

        // Should throw an exception since maximum is 1
        group.validate(commandLine);
    }

    @Test
    public void testGroupWithMinimumAndMaximum() throws ParseException, OptionException {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description", 1, 3);
        WriteableCommandLine commandLine = new WriteableCommandLine();

        Parser parser = new SimpleParser();
        parser.parse(commandLine, group.getOptions().toArray(new Option[0]));

        assertTrue(commandLine.hasOption("groupName"));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertTrue(commandLine.hasNoOption("option3"));

        // Should not throw an exception since minimum is 1 and maximum is 3
        group.validate(commandLine);
    }

    @Test
    public void testGroupProcess() throws ParseException, OptionException {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List<Object> arguments = new LinkedList<>();
        arguments.add("groupName");
        arguments.add("option1");

        group.process(commandLine, arguments.listIterator());

        assertTrue(commandLine.hasOption("groupName"));
        assertTrue(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
        assertNull(commandLine.getOption("option3"));
    }

    @Test
    public void testGroupProcessWithAnonymous() throws ParseException, OptionException {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        Argument arg = OptionBuilder.withLongOpt("argument")
                .withDescription("Description for argument")
                .withArgName("ARG")
                .isRequired()
                .build();
        options.add(arg);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List<Object> arguments = new LinkedList<>();
        arguments.add("groupName");
        arguments.add("option1");
        arguments.add("ARG");

        group.process(commandLine, arguments.listIterator());

        assertTrue(commandLine.hasOption("groupName"));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasArgument("argument"));
        assertEquals("ARG", commandLine.getArgument("argument"));
    }

    @Test
    public void testGroupCanProcess() {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List<Object> arguments = new LinkedList<>();
        arguments.add("groupName");
        arguments.add("option1");
        arguments.add("option2");
        arguments.add("option3");

        assertTrue(group.canProcess(commandLine, "option1"));
        assertTrue(group.canProcess(commandLine, "option2"));
        assertTrue(group.canProcess(commandLine, "option3"));
        assertFalse(group.canProcess(commandLine, "arg"));
        assertFalse(group.canProcess(commandLine, "otherOption"));
    }

    @Test
    public void testGroupValidate() throws ParseException, OptionException {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description");

        WriteableCommandLine commandLine = new WriteableCommandLine();

        List<Object> arguments = new LinkedList<>();
        arguments.add("groupName");

        // Validate with no options
        group.validate(commandLine);

        List<Object> arguments2 = new LinkedList<>();
        arguments2.add("groupName");
        arguments2.add("option1");

        // Validate with one option
        group.validate(commandLine);

        List<Object> arguments3 = new LinkedList<>();
        arguments3.add("groupName");
        arguments3.add("option1");
        arguments3.add("option2");

        // Validate with multiple options
        group.validate(commandLine);

        List<Object> arguments4 = new LinkedList<>();
        arguments4.add("groupName");
        arguments4.add("option1");
        arguments4.add("option2");
        arguments4.add("option3");

        // Validate with maximum number of options
        group.validate(commandLine);

        List<Object> arguments5 = new LinkedList<>();
        arguments5.add("groupName");
        arguments5.add("option1");
        arguments5.add("option2");
        arguments5.add("option3");
        arguments5.add("option4");

        // Validate with more options than maximum
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }

        List<Object> arguments6 = new LinkedList<>();
        arguments6.add("groupName");

        // Validate with no options
        try {
            group.validate(commandLine);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @Test
    public void testFindOption() {
        List<Option> options = new LinkedList<>();
        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Description for option1")
                .build();
        options.add(option1);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Description for option2")
                .build();
        options.add(option2);

        GroupImpl group = new GroupImpl(options, "groupName", "Group description");

        Option optionFound = group.findOption("option1");
        assertSame(option1, optionFound);

        Option optionNotFound = group.findOption("option3");
        assertNull(optionNotFound);
    }
}