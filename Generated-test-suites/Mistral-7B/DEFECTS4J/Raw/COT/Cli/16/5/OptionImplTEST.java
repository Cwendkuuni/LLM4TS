import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(JUnit4.class)
public class OptionImplTest {

    @Test
    public void testConstructor() {
        OptionImpl option = new OptionImpl(1, false);
        Assert.assertEquals(1, option.getId());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testCanProcess_whenArgumentMatchesOption_returnsTrue() {
        OptionImpl option = new TestOptionImpl(1, false);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--test");

        Assert.assertTrue(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test
    public void testCanProcess_whenArgumentDoesntMatchOption_returnsFalse() {
        OptionImpl option = new TestOptionImpl(1, false);
        WriteableCommandLine commandLine = new WriteableCommandLine();
        List<String> arguments = Arrays.asList("--not-test");

        Assert.assertFalse(option.canProcess(commandLine, arguments.listIterator()));
    }

    @Test
    public void testToString() {
        OptionImpl option = new TestOptionImpl(1, false);
        String expected = "test";

        String result = option.toString();

        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindOption_whenTriggerIsNotAPrefix_throwsException() {
        OptionImpl option = new TestOptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("no-prefix"));
        option.checkPrefixes(prefixes);

        Option notASuffixOption = option.findOption("test");
        Assert.assertNull(notASuffixOption);
    }

    @Test
    public void testFindOption_whenTriggerIsAPrefix_returnsOption() {
        OptionImpl option = new TestOptionImpl(1, false);
        Set<String> prefixes = new HashSet<>(Arrays.asList("test-prefix"));
        option.checkPrefixes(prefixes);

        Option testOption = option.findOption("test");
        Assert.assertEquals(option, testOption);
    }

    @Test
    public void testIsRequired() {
        OptionImpl option1 = new TestOptionImpl(1, true);
        OptionImpl option2 = new TestOptionImpl(2, false);

        Assert.assertTrue(option1.isRequired());
        Assert.assertFalse(option2.isRequired());
    }

    @Test
    public void testHashCode() {
        OptionImpl option1 = new TestOptionImpl(1, false);
        OptionImpl option2 = new TestOptionImpl(1, false);
        OptionImpl option3 = new TestOptionImpl(2, false);

        Assert.assertEquals(option1.hashCode(), option1.hashCode());
        Assert.assertEquals(option1.hashCode(), option2.hashCode());
        Assert.assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testEquals() {
        OptionImpl option1 = new TestOptionImpl(1, false);
        OptionImpl option2 = new TestOptionImpl(1, false);
        OptionImpl option3 = new TestOptionImpl(2, false);

        Assert.assertTrue(option1.equals(option1));
        Assert.assertTrue(option1.equals(option2));
        Assert.assertFalse(option1.equals(option3));
    }
}

abstract class TestOptionImpl extends OptionImpl {
    public TestOptionImpl(int id, boolean required) {
        super(id, required);
    }

    @Override
    public String getPreferredName() {
        return "test";
    }

    @Override
    public Set<String> getPrefixes() {
        return new HashSet<>(Arrays.asList("test-prefix"));
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList("test");
    }
}