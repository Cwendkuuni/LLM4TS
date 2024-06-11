import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.apache.commons.cli2.CliBuilder.option;
import static org.apache.commons.cli2.CliBuilder.group;

public class GroupImplTest {

    @Test
    public void testGroupImpl() {
        // given
        final List<Option> options = new LinkedList<>();
        final Option option1 = option("o1", "o1", false, "Option 1")
                .required(false)
                .build();
        final Option option2 = option("o2", "o2", false, "Option 2")
                .required(false)
                .build();
        final Argument arg1 = argument("a1", "a1").build();
        final Argument arg2 = argument("a2", "a2").build();
        final List<Argument> anonymous = new ArrayList<>();
        anonymous.add(arg1);
        anonymous.add(arg2);
        final Group group = group("g")
                .description("Group")
                .minimum(1)
                .maximum(3)
                .options(options)
                .anonymous(anonymous)
                .build();
        options.add(option1);
        options.add(option2);

        // when
        final WriteableCommandLine commandLine = new DefaultCommandLine();

        // test process method
        group.process(commandLine, commandLine.parser().getArgList().iterator());

        // then
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(2, commandLine.getArgList().size());
        Assert.assertTrue(commandLine.getArgList().get(0) instanceof Option);
        Assert.assertEquals("o1", commandLine.getArgList().get(0).getLongOpt());
        Assert.assertFalse(((Option) commandLine.getArgList().get(0)).isSet());
        Assert.assertFalse(((Option) commandLine.getArgList().get(0)).isSpecified());
        Assert.assertEquals(0, ((Option) commandLine.getArgList().get(0)).getValues().size());

        Assert.assertEquals("a1", commandLine.getArgList().get(1));
        Assert.assertEquals("a2", commandLine.getArgList().get(2));

        // test validate method
        group.validate(commandLine);

        // then
        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertEquals(2, commandLine.getArgList().size());

        Assert.assertTrue(option1.isSet());
        Assert.assertFalse(option1.isSpecified());
        Assert.assertEquals(Collections.singletonList("o1"), option1.getValues());

        Assert.assertTrue(option2.isSet());
        Assert.assertFalse(option2.isSpecified());
        Assert.assertEquals(Collections.singletonList("o2"), option2.getValues());

        // test canProcess method
        Assert.assertTrue(group.canProcess(commandLine, "o1"));
        Assert.assertTrue(group.canProcess(commandLine, "o2"));
        Assert.assertTrue(group.canProcess(commandLine, "a1"));
        Assert.assertTrue(group.canProcess(commandLine, "a2"));

        // test getPrefixes method
        Assert.assertEquals(Collections.emptySet(), group.getPrefixes());

        // test getTriggers method
        Assert.assertEquals(Collections.singleton("g"), group.getTriggers());

        // test getName method
        Assert.assertEquals("g", group.getName());

        // test getDescription method
        Assert.assertEquals("Group", group.getDescription());

        // test appendUsage method
        final StringBuilder sb = new StringBuilder();
        group.appendUsage(sb, Collections.emptySet(), null);
        Assert.assertEquals("g\n" +
                "\tGroup\n" +
                "\toptional: true\n" +
                "\tminimum: 1\n" +
                "\tmaximum: 3\n" +
                "\to1\tOption 1\n" +
                "\to2\tOption 2\n" +
                "\ta1\t\n" +
                "\ta2\n", sb.toString());

        // test helpLines method
        final List<HelpLine> helpLines = group.helpLines(0, Collections.emptySet(), null);
        final Map<String, HelpLine> helpLinesMap = new HashMap<>();
        for (HelpLine helpLine : helpLines) {
            helpLinesMap.put(helpLine.getHeadText(), helpLine);
        }
        final HelpLine helpLine = helpLinesMap.get("g");
        Assert.assertNotNull(helpLine);
        Assert.assertEquals("g", helpLine.getHeadText());
        Assert.assertEquals(0, helpLine.getOptions().size());
        Assert.assertEquals(1, helpLine.getArgs().size());
        Assert.assertEquals("g", helpLine.getOptions().get(0).getLongOpt());
        Assert.assertEquals("Group", helpLine.getOptions().get(0).getDescription());
        Assert.assertEquals("", helpLine.getArgs().get(0));

        // test getMinimum method
        Assert.assertEquals(1, group.getMinimum());

        // test getMaximum method
        Assert.assertEquals(3, group.getMaximum());

        // test isRequired method
        Assert.assertFalse(group.isRequired());
    }
}