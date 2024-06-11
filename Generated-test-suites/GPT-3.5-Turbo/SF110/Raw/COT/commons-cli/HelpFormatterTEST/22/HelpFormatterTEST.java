
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

public class HelpFormatterTest {
	
	private HelpFormatter helpFormatter;
	private Options options;
	
	@Before
	public void setUp() {
		helpFormatter = new HelpFormatter();
		options = new Options();
		options.addOption("h", "help", false, "Display help");
		options.addOption("i", "input", true, "Input file");
		options.addOption("o", "output", true, "Output file");
	}
	
	@Test
	public void testSetWidth() {
		helpFormatter.setWidth(80);
		assertEquals(80, helpFormatter.getWidth());
	}
	
	@Test
	public void testSetLeftPadding() {
		helpFormatter.setLeftPadding(2);
		assertEquals(2, helpFormatter.getLeftPadding());
	}
	
	@Test
	public void testSetDescPadding() {
		helpFormatter.setDescPadding(4);
		assertEquals(4, helpFormatter.getDescPadding());
	}
	
	@Test
	public void testSetSyntaxPrefix() {
		helpFormatter.setSyntaxPrefix("Usage: ");
		assertEquals("Usage: ", helpFormatter.getSyntaxPrefix());
	}
	
	@Test
	public void testSetNewLine() {
		helpFormatter.setNewLine("\n");
		assertEquals("\n", helpFormatter.getNewLine());
	}
	
	@Test
	public void testSetOptPrefix() {
		helpFormatter.setOptPrefix("/");
		assertEquals("/", helpFormatter.getOptPrefix());
	}
	
	@Test
	public void testSetLongOptPrefix() {
		helpFormatter.setLongOptPrefix("++");
		assertEquals("++", helpFormatter.getLongOptPrefix());
	}
	
	@Test
	public void testSetArgName() {
		helpFormatter.setArgName("argument");
		assertEquals("argument", helpFormatter.getArgName());
	}
	
	@Test
	public void testSetOptionComparator() {
		helpFormatter.setOptionComparator(null);
		assertNotNull(helpFormatter.getOptionComparator());
	}
	
	@Test
	public void testPrintHelp() {
		helpFormatter.printHelp("command", options);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPrintHelpMissingCmdLineSyntax() {
		helpFormatter.printHelp(null, options);
	}
	
	@Test
	public void testPrintHelpWithHeader() {
		helpFormatter.printHelp("command", "Header", options, "Footer");
	}
	
	@Test
	public void testPrintHelpWithAutoUsage() {
		helpFormatter.printHelp("command", options, true);
	}
	
	@Test
	public void testPrintHelpWithHeaderAndAutoUsage() {
		helpFormatter.printHelp("command", "Header", options, "Footer", true);
	}
	
	@Test
	public void testPrintHelpWithWidth() {
		helpFormatter.printHelp(80, "command", options);
	}
	
	@Test
	public void testPrintHelpWithWidthAndHeader() {
		helpFormatter.printHelp(80, "command", "Header", options, "Footer");
	}
	
	@Test
	public void testPrintHelpWithWidthAndHeaderAndAutoUsage() {
		helpFormatter.printHelp(80, "command", "Header", options, "Footer", true);
	}
	
	@Test
	public void testPrintHelpWithPrintWriter() {
		helpFormatter.printHelp(new PrintWriter(System.out), 80, "command", options);
	}
	
	@Test
	public void testPrintHelpWithPrintWriterAndWidthAndHeader() {
		helpFormatter.printHelp(new PrintWriter(System.out), 80, "command", "Header", options, 2, 4, "Footer");
	}
	
	@Test
	public void testPrintUsage() {
		helpFormatter.printUsage(new PrintWriter(System.out), 80, "command", options);
	}
	
	@Test
	public void testPrintOptions() {
		helpFormatter.printOptions(new PrintWriter(System.out), 80, options, 2, 4);
	}
	
	@Test
	public void testPrintWrapped() {
		helpFormatter.printWrapped(new PrintWriter(System.out), 80, "This is a long message that needs to be wrapped to fit within the specified width.");
	}
	
	@Test
	public void testPrintWrappedWithNextLineTabStop() {
		helpFormatter.printWrapped(new PrintWriter(System.out), 80, 4, "This is a long message that needs to be wrapped to fit within the specified width.");
	}
	
	@Test
	public void testGetOptionComparator() {
		assertNotNull(helpFormatter.getOptionComparator());
	}
}
