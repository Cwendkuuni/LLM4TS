package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.util.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OptionImplTest {

    private static final String PREFERRED_NAME = "testOption";
    private static final String DESCRIPTION = "Test Option";
    private static final String TRIGGER = "t";
    private static final String[] PREFIXES = new String[]{"--", "-t"};

    private OptionImpl option;
    private WriteableCommandLine commandLine;
    private ResourceHelper resourceHelper;

    @Before
    public void setup() {
        this.option = new TestOptionImpl(1, true);
        this.commandLine = new TestWriteableCommandLine();
        this.resourceHelper = ResourceHelper.getResourceHelper();
        this.option.setPreferredName(PREFERRED_NAME);
        this.option.setDescription(DESCRIPTION);
        this.option.setTriggers(new ArrayList<String>() {{
            add(TRIGGER);
        }});
        this.option.setPrefixes(new HashSet<String>() {{
            addAll(List.of(PREFIXES));
        }});
    }

    @Test
    public void testConstructor() {
        assertEquals(1, option.getId());
        assertTrue(option.isRequired());
    }

    @Test
    public void testCanProcess() {
        final ListIterator<String> arguments = TestUtils.getTestArguments().listIterator();
        assertTrue(option.canProcess(commandLine, arguments));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanProcessWithNoArguments() {
        final ListIterator<String> arguments = TestUtils.getTestArguments().listIterator();
        arguments.next();
        assertFalse(option.canProcess(commandLine, arguments));

        final String message =
            resourceHelper.getMessage(ResourceConstants.OPTION_NO_ARGUMENT, option.getPreferredName());
        option.canProcess(commandLine, arguments);
    }

    @Test
    public void testToString() {
        final String expected = "Test Option\n";
        final String result = option.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        final OptionImpl testOption2 = new TestOptionImpl(1, true);
        testOption2.setPreferredName(PREFERRED_NAME);
        testOption2.setDescription(DESCRIPTION);
        testOption2.setTriggers(new ArrayList<String>() {{
            add(TRIGGER);
        }});
        testOption2.setPrefixes(new HashSet<String>() {{
            addAll(List.of(PREFIXES));
        }});

        assertTrue(option.equals(testOption2));
        assertFalse(option.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        final int expected = 1 * 37 + PREFERRED_NAME.hashCode() + DESCRIPTION.hashCode()
                + PREFIXES[0].hashCode() + PREFIXES[1].hashCode() + TRIGGER.hashCode();
        assertEquals(expected, option.hashCode());
    }

    @Test
    public void testFindOption() {
        Option testOption = option.findOption(TRIGGER);
        assertEquals(option, testOption);
        testOption = option.findOption("invalidTrigger");
        assertNull(testOption);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsRequired() {
        assertTrue(option.isRequired());
    }

    @Test
    public void testDefaults() {
        // nothing to test
    }

    @Test
    public void testCheckPrefixes() {
        Set<String> prefixSet = new HashSet<String>() {{
            addAll(List.of(PREFIXES));
        }};
        option.checkPrefixes(prefixSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithNoPrefix() {
        Set<String> prefixSet = new HashSet<>();
        option.checkPrefixes(prefixSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesWithNoTriggerPrefix() {
        Set<String> prefixSet = new HashSet<String>() {{
            add("noPrefixTrigger");
        }};
        option.checkPrefixes(prefixSet);
    }
}

public class TestOptionImpl extends OptionImpl {
    public TestOptionImpl(int id, boolean required) {
        super(id, required);
    }
}

public class TestWriteableCommandLine implements WriteableCommandLine {
    @Override
    public void parse(List<String> arguments) {
        // no-op
    }
}