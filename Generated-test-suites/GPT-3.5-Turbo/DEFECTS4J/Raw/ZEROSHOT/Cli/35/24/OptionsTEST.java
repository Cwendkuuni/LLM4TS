import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group1 = new OptionGroup();
        group1.addOption(new Option("a", "aaa", false, "option a"));
        group1.addOption(new Option("b", "bbb", false, "option b"));
        options.addOptionGroup(group1);

        OptionGroup group2 = new OptionGroup();
        group2.addOption(new Option("c", "ccc", true, "option c"));
        group2.addOption(new Option("d", "ddd", true, "option d"));
        options.addOptionGroup(group2);

        assertEquals(group1, options.getOptionGroup(group1.getOptions().get(0)));
        assertEquals(group2, options.getOptionGroup(group2.getOptions().get(1)));
    }

    @Test
    public void testAddOption() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", "ccc", true, "option c");

        assertEquals(3, options.getOptions().size());
        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("ccc"));
        assertFalse(options.hasShortOption("bbb"));
    }

    @Test
    public void testGetOption() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", "ccc", true, "option c");

        Option optionA = options.getOption("a");
        Option optionB = options.getOption("b");
        Option optionC = options.getOption("ccc");

        assertNotNull(optionA);
        assertNotNull(optionB);
        assertNotNull(optionC);
        assertEquals("a", optionA.getOpt());
        assertEquals("b", optionB.getOpt());
        assertEquals("ccc", optionC.getLongOpt());
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", "ccc", true, "option c");

        assertEquals(3, options.getMatchingOptions("o").size());
        assertEquals(2, options.getMatchingOptions("a").size());
        assertEquals(1, options.getMatchingOptions("ccc").size());
        assertEquals(0, options.getMatchingOptions("d").size());
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", "ccc", true, "option c");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasLongOption("ccc"));
        assertFalse(options.hasShortOption("bbb"));
        assertFalse(options.hasOption("d"));
    }

    @Test
    public void testToString() {
        Options options = new Options();
        options.addOption("a", "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", "ccc", true, "option c");

        String expected = "[ Options: [ short {a=Option{ opt='a', longOpt=null, hasArg=false, description='option a', required=false }" +
                ", b=Option{ opt='b', longOpt=null, hasArg=true, description='option b', required=false }" +
                ", c=Option{ opt='c', longOpt='ccc', hasArg=true, description='option c', required=false }} ] " +
                "[ long {ccc=Option{ opt='c', longOpt='ccc', hasArg=true, description='option c', required=false }} ]";
        assertEquals(expected, options.toString());
    }
}