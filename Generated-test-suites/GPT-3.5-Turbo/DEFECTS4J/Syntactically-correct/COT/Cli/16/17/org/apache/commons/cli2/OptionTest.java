import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class OptionTest {

	@Test
	public void testProcess() {
		WriteableCommandLine commandLine = new WriteableCommandLine();
		ListIterator args = Arrays.asList("-a").listIterator();
		Option option = new OptionImpl();
		try {
			option.process(commandLine, args);
			assertTrue(commandLine.hasOption("a"));
			assertEquals("null", commandLine.getValue("a"));
		} catch (OptionException e) { fail(); }
	}
	
	@Test
	public void testDefaults() {
		WriteableCommandLine commandLine = new WriteableCommandLine();
		Option option = new OptionImpl();
		option.defaults(commandLine);
		assertFalse(commandLine.hasOption("a"));
		assertEquals("default", commandLine.getValue("a"));
	}
	
	@Test
	public void testCanProcessWithArgument() {
		WriteableCommandLine commandLine = new WriteableCommandLine();
		Option option = new OptionImpl();
		assertTrue(option.canProcess(commandLine, "-a"));
		assertFalse(option.canProcess(commandLine, "-b"));
	}
	
	@Test
	public void testCanProcessWithArguments() {
		WriteableCommandLine commandLine = new WriteableCommandLine();
		ListIterator args = Arrays.asList("-a").listIterator();
		Option option = new OptionImpl();
		assertTrue(option.canProcess(commandLine, args));
	}
	
	@Test
	public void testGetTriggers() {
		Option option = new OptionImpl();
		Set triggers = option.getTriggers();
		assertTrue(triggers.contains("a"));
		assertTrue(triggers.contains("b"));
	}
	
	@Test
	public void testGetPrefixes() {
		Option option = new OptionImpl();
		Set prefixes = option.getPrefixes();
		assertTrue(prefixes.contains("--"));
		assertTrue(prefixes.contains("-"));
	}
	
	@Test
	public void testValidate() {
		WriteableCommandLine commandLine = new WriteableCommandLine();
		Option option = new OptionImpl();
		try {
			option.validate(commandLine);
		} catch (OptionException e) { fail(); }
	}
	
	@Test
	public void testHelpLines() {
		Option option = new OptionImpl();
		List helpLines = option.helpLines(0, new HashSet<HelpSetting>(), null);
		assertEquals(1, helpLines.size());
	}
	
	@Test
	public void testAppendUsage() {
		StringBuffer buffer = new StringBuffer();
		Option option = new OptionImpl();
		option.appendUsage(buffer, new HashSet<DisplaySetting>(), null);
		assertEquals("--a, -a [null] (default: default)", buffer.toString());
	}
	
	@Test
	public void testGetPreferredName() {
		Option option = new OptionImpl();
		assertEquals("a", option.getPreferredName());
	}
	
	@Test
	public void testGetDescription() {
		Option option = new OptionImpl();
		assertEquals("option a", option.getDescription());
	}
	
	@Test
	public void testGetId() {
		Option option = new OptionImpl();
		assertEquals(1, option.getId());
	}
	
	@Test
	public void testFindOption() {
		Option option = new OptionImpl();
		assertNull(option.findOption("c"));
		assertNotNull(option.findOption("a"));
	}
	
	@Test
	public void testIsRequired() {
		Option option = new OptionImpl();
		assertFalse(option.isRequired());
	}
	
	@Test
	public void testSetParent() {
		Option option = new OptionImpl();
		option.setParent(null);
		// Assert parent is set to null
	}

}