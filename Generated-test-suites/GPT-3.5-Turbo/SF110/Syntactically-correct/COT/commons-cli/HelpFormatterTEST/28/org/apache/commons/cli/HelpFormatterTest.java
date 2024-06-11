import org.junit.Test;
import org.apache.commons.cli.Options;

public class HelpFormatterTest {

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        assertSame(80, formatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(2);
        assertSame(2, formatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(4);
        assertSame(4, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("usage: ");
        assertSame("usage: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\n");
        assertSame("\n", formatter.getNewLine());
    }

    @Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("-");
        assertSame("-", formatter.getOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--");
        assertSame("--", formatter.getLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("arg");
        assertSame("arg", formatter.getArgName());
    }

    @Test
    public void testSetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        Comparator<org.apache.commons.cli.Option> comparator = new Comparator<org.apache.commons.cli.Option>() {
            @Override
            public int compare(org.apache.commons.cli.Option o1, org.apache.commons.cli.Option o2) {
                return o1.getDescription().compareTo(o2.getDescription());
            }
        };
        formatter.setOptionComparator(comparator);
        assertSame(comparator, formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelp() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        formatter.printHelp("cmdLineSyntax", options);
    }

    @Test
    public void testPrintUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage("pw", 80, "cmdLineSyntax");
    }

    @Test
    public void testPrintOptions() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        formatter.printOptions("pw", 80, options, 2, 4);
    }

    @Test
    public void testPrintWrapped() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printWrapped("pw", 80, "text");
    }

    @Test
    public void testPrintWrappedWithNextLineTabStop() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printWrapped("pw", 80, 4, "text");
    }
}