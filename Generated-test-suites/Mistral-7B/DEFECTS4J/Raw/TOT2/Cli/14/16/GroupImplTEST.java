import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.builder.DefaultParser;
import org.apache.commons.cli2.exceptions.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupImplWithValidOptions() throws ParseException {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .description("Option A")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .description("Option B")
                        .build(),
                Option.builder("c")
                        .longOpt("optionC")
                        .description("Option C")
                        .build()
        );

        final Group group = new GroupImpl(options, "MyGroup", "My Group", 1, 3);
        final CommandLine commandLine = new DefaultParser().parse(group, Arrays.asList("--optionA", "--optionB"));

        Assert.assertEquals("--optionA, --optionB", commandLine.getOptions().toString());
        Assert.assertEquals(2, group.getOptions().size());
    }

    @Test
    public void testGroupImplWithMinimumOptions() throws ParseException {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .description("Option A")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .description("Option B")
                        .build()
        );

        final Group group = new GroupImpl(options, "MyGroup", "My Group", 2, 3);
        final CommandLine commandLine = new DefaultParser().parse(group, Collections.emptyList());

        Assert.assertEquals("--optionA, --optionB", commandLine.getOptions().toString());
        Assert.assertEquals(2, group.getOptions().size());
    }

    @Test(expected = ParseException.class)
    public void testGroupImplWithTooFewOptions() throws ParseException {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .description("Option A")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .description("Option B")
                        .build()
        );

        final Group group = new GroupImpl(options, "MyGroup", "My Group", 3, 3);
        new DefaultParser().parse(group, Arrays.asList("--optionA"));
    }

    @Test(expected = ParseException.class)
    public void testGroupImplWithTooManyOptions() throws ParseException {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .description("Option A")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .description("Option B")
                        .build()
        );

        final Group group = new GroupImpl(options, "MyGroup", "My Group", 1, 3);
        new DefaultParser().parse(group, Arrays.asList("--optionA", "--optionA", "--optionB"));
    }

    @Test
    public void testGroupImplWithAnonymousArguments() throws ParseException {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .argName("arg")
                        .description("Option A")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .argName("arg")
                        .description("Option B")
                        .build(),
                Option.builder("c")
                        .longOpt("optionC")
                        .argName("arg")
                        .description("Option C")
                        .build(),
                Option.builder("x")
                        .longOpt("optionX")
                        .argName("arg")
                        .description("Option X")
                        .build()
        );

        final List anonymousArgs = Arrays.asList(
                Option.builder("a")
                        .argName("arg")
                        .description("Anonymous argument A")
                        .build(),
                Option.builder("b")
                        .argName("arg")
                        .description("Anonymous argument B")
                        .build()
        );

        final Group group = new GroupImpl(options, null, null, 1, 1, anonymousArgs);
        final CommandLine commandLine = new DefaultParser().parse(group, Arrays.asList("--optionA=valueA", "--optionB=valueB"));

        Assert.assertEquals("--optionA=valueA, --optionB=valueB", commandLine.getOptions().toString());
        Assert.assertEquals(4, group.getOptions().size());
        Assert.assertEquals(2, group.getAnonymous().size());
    }

    @Test
    public void testGroupImplWithMixedOptionsAndAnonymousArguments() throws ParseException {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .description("Option A")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .argName("arg")
                        .description("Option B")
                        .build(),
                Option.builder("x")
                        .longOpt("optionX")
                        .argName("arg")
                        .description("Option X")
                        .build()
        );

        final List anonymousArgs = Arrays.asList(
                Option.builder("a")
                        .argName("arg")
                        .description("Anonymous argument A")
                        .build(),
                Option.builder("b")
                        .argName("arg")
                        .description("Anonymous argument B")
                        .build()
        );

        final Group group = new GroupImpl(options, null, null, 1, 3, anonymousArgs);
        final CommandLine commandLine = new DefaultParser().parse(group, Arrays.asList("--optionA", "--optionB=valueB", "--a=valueA"));

        Assert.assertEquals("--optionA, --optionB=valueB, --a=valueA", commandLine.getOptions().toString());
        Assert.assertEquals(4, group.getOptions().size());
        Assert.assertEquals(2, group.getAnonymous().size());
    }

    @Test
    public void testGroupImplWithEmptyName() throws ParseException {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .description("Option A")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .description("Option B")
                        .build()
        );

        final Group group = new GroupImpl(options, null, null, 1, 3);
        new DefaultParser().parse(group, Arrays.asList("--optionA", "--optionB"));
    }

    @Test
    public void testGroupImplWithEmptyDescription() throws ParseException {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .description("")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .description("")
                        .build()
        );

        final Group group = new GroupImpl(options, null, null, 1, 3);
        new DefaultParser().parse(group, Arrays.asList("--optionA", "--optionB"));
    }

    @Test
    public void testGroupImplWithEmptyGroupName() throws ParseException {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .description("Option A")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .description("Option B")
                        .build()
        );

        final Group group = new GroupImpl(options, null, null, 1, 3);
        new DefaultParser().parse(group, Arrays.asList("--optionA", "--optionB"));
    }

    @Test
    public void testGroupImplWithEmptyGroupDescription() throws ParseException {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .description("Option A")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .description("")
                        .build()
        );

        final Group group = new GroupImpl(options, null, null, 1, 3);
        new DefaultParser().parse(group, Arrays.asList("--optionA", "--optionB"));
    }

    @Test
    public void testHelp() {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .description("Option A")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .description("Option B")
                        .build()
        );

        final Group group = new GroupImpl(options, "MyGroup", "My Group", 1, 3);
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyProgram", "1.0", "usage: MyProgram [options...]", true, group, true);
    }

    @Test
    public void testHelpWithAnonymousArguments() {
        final List options = Arrays.asList(
                Option.builder("a")
                        .longOpt("optionA")
                        .description("Option A")
                        .build(),
                Option.builder("b")
                        .longOpt("optionB")
                        .argName("arg")
                        .description("Option B takes an argument")
                        .build(),
                Option.builder("c")
                        .longOpt("optionC")
                        .argName("arg")
                        .description("Option C takes an argument")
                        .build(),
                Option.builder("x")
                        .longOpt("optionX")
                        .argName("arg")
                        .description("Option X takes an argument")
                        .build()
        );

        final List anonymousArgs = Arrays.asList(
                Option.builder("a")
                        .argName("arg")
                        .description("Anonymous argument A")
                        .build(),
                Option.builder("b")
                        .argName("arg")
                        .description("Anonymous argument B")
                        .build()
        );

        final Group group = new GroupImpl(options, null, null, 1, 1, anonymousArgs);
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("MyProgram", "1.0", "usage: MyProgram [options...]", true, group, true);
    }
}