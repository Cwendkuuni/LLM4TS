package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RunWith(PowerMockRunner.class)
public class GroupImplTest {

    private List<Option> options;
    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        options = Arrays.asList(
                Mockito.mock(Option.class),
                Mockito.mock(Argument.class)
        );

        group = new GroupImpl(options, "MyGroup", "My Group", 1, 2);
        commandLine = Mockito.mock(WriteableCommandLine.class);
    }

    @Test
    public void testCanProcess_NoOption() {
        PowerMockito.when(commandLine.looksLikeOption("no_option")).thenReturn(false);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        Assert.assertFalse(group.canProcess(commandLine, "no_option"));
    }

    @Test
    public void testCanProcess_EmptyArgumentList() {
        PowerMockito.when(commandLine.looksLikeOption("option")).thenReturn(false);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        Assert.assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_OptionsBeforeGroup() {
        PowerMockito.when(commandLine.looksLikeOption("option1")).thenReturn(true);
        PowerMockito.when(commandLine.looksLikeOption("option2")).thenReturn(true);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        Assert.assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_OptionsAfterGroup() {
        PowerMockito.when(commandLine.looksLikeOption("option1")).thenReturn(true);
        PowerMockito.when(commandLine.looksLikeOption("option2")).thenReturn(true);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Arrays.asList("group"));

        Assert.assertTrue(group.canProcess(commandLine, "group"));
    }

    @Test
    public void testCanProcess_Bursting() {
        PowerMockito.when(commandLine.looksLikeOption("option1"))
                .thenReturn(true)
                .thenReturn(true);
        PowerMockito.when(commandLine.looksLikeOption("option3"))
                .thenReturn(false);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        Assert.assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcess_AnonymousArgument() {
        PowerMockito.when(commandLine.looksLikeOption("option1")).thenReturn(false);
        PowerMockito.when(commandLine.looksLikeOption("argument")).thenReturn(true);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        Assert.assertTrue(group.canProcess(commandLine, "argument"));
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_MaximumOptions() {
        PowerMockito.when(commandLine.looksLikeOption("option1"))
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(true);
        PowerMockito.when(commandLine.looksLikeOption("option3"))
                .thenReturn(true);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        group.canProcess(commandLine, null);
    }

    @Test(expected = OptionException.class)
    public void testCanProcess_MinimumOptions() {
        PowerMockito.when(commandLine.looksLikeOption("option1"))
                .thenReturn(false);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        group.canProcess(commandLine, null);
    }

    @Test
    public void testGetPrefixes() {
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        Assert.assertEquals(Collections.emptySet(), group.getPrefixes());
    }

    @Test
    public void testGetTriggers() {
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        Assert.assertEquals(Collections.emptySet(), group.getTriggers());
    }

    @Test
    public void testProcess() {
        PowerMockito.when(commandLine.looksLikeOption("option1"))
                .thenReturn(true);
        PowerMockito.when(commandLine.looksLikeOption("option2"))
                .thenReturn(true);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Arrays.asList("option1"));

        List<String> arguments = new ArrayList<>();
        group.process(commandLine, new ListIterator<>() {
            @Override
            public boolean hasNext() {
                return arguments.hasNext();
            }

            @Override
            public String next() {
                return arguments.remove(0);
            }

            @Override
            public void remove() {
                // not used in this test
            }

            @Override
            public void add(String e) {
                // not used in this test
            }

            @Override
            public void setNext(String e) {
                arguments.add(e);
            }
        });

        Assert.assertEquals(Collections.singletonList("option1"), group.getOptions());
    }

    @Test
    public void testValidate() {
        PowerMockito.when(commandLine.hasOption("option1"))
                .thenReturn(false);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_MinimumOptions() {
        PowerMockito.when(commandLine.hasOption("option1"))
                .thenReturn(false);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidate_MaximumOptions() {
        PowerMockito.when(commandLine.hasOption("option1"))
                .thenReturn(true);
        PowerMockito.when(commandLine.hasOption("option2"))
                .thenReturn(true);
        PowerMockito.when(commandLine.hasOption("option3"))
                .thenReturn(true);
        PowerMockito.when(commandLine.getArguments()).thenReturn(Collections.emptyList());

        group.validate(commandLine);
    }

    @Test
    public void testGetPreferredName() {
        Assert.assertEquals("MyGroup", group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals("My Group", group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        group.appendUsage(buffer, new LinkedHashMap<>(), null);

        Assert.assertEquals("My Group:\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        List<HelpLine> helpLines = group.helpLines(1, new LinkedHashMap<>(), null);

        Assert.assertEquals(1, helpLines.size());
    }

    @Test
    public void testGetOptions() {
        Assert.assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        List<Option> anonymous = Arrays.asList(Mockito.mock(Argument.class));

        Group group = new GroupImpl(Arrays.asList(), null, null, 1, 2) {
            @Override
            public List getAnonymous() {
                return anonymous;
            }
        };

        Assert.assertEquals(anonymous, group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        Option option = Mockito.mock(Option.class);

        Group group = new GroupImpl(Arrays.asList(option), null, null, 1, 2) {
            @Override
            public Option findOption(String trigger) {
                return option;
            }
        };

        Assert.assertEquals(option, group.findOption("trigger"));
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

    @Test
    public void testDefaults() {
        group.defaults(commandLine);

        PowerMockito.verify(commandLine, Mockito.atLeast(options.size() + 1))
                .defaultValue(Mockito.anyString(), Mockito.eq(null));
    }
}