import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

	@Test
	public void testGetPreferredName() {
		ArgumentImpl arg = new ArgumentImpl("name", "description", 0, 1, ' ', ' ', null, "--", null, 0);
		assertEquals("name", arg.getPreferredName());
	}

	@Test
	public void testProcessValues() {
		// Create a mock WriteableCommandLine object
		WriteableCommandLine mockCommandLine = new WriteableCommandLine() {
			public void addValue(Option option, Object value) {
			}

			public List getValues(Option option) {
				return null;
			}

			public void setDefaultValues(Option option, List values) {
			}

			public boolean looksLikeOption(String arg) {
				return false;
			}
		};

		// Create a mock ListIterator object
		ListIterator mockIterator = new ListIterator() {
			public boolean hasNext() {
				return false;
			}

			public Object next() {
				return null;
			}

			public boolean hasPrevious() {
				return false;
			}

			public Object previous() {
				return null;
			}

			public int nextIndex() {
				return 0;
			}

			public int previousIndex() {
				return 0;
			}

			public void remove() {
			}

			public void set(Object e) {
			}

			public void add(Object e) {
			}
		};

		// Create a mock Option object
		Option mockOption = new OptionImpl(0, false);

		ArgumentImpl arg = new ArgumentImpl("name", "description", 0, 1, ' ', ' ', null, "--", null, 0);
		try {
			arg.processValues(mockCommandLine, mockIterator, mockOption);
		} catch (OptionException e) {
			fail("Unexpected OptionException");
		}
	}

	@Test
	public void testProcess() {
		// Create a mock WriteableCommandLine object
		WriteableCommandLine mockCommandLine = new WriteableCommandLine() {
			public void addValue(Option option, Object value) {
			}

			public List getValues(Option option) {
				return null;
			}

			public void setDefaultValues(Option option, List values) {
			}

			public boolean looksLikeOption(String arg) {
				return false;
			}
		};

		// Create a mock ListIterator object
		ListIterator mockIterator = new ListIterator() {
			public boolean hasNext() {
				return false;
			}

			public Object next() {
				return null;
			}

			public boolean hasPrevious() {
				return false;
			}

			public Object previous() {
				return null;
			}

			public int nextIndex() {
				return 0;
			}

			public int previousIndex() {
				return 0;
			}

			public void remove() {
			}

			public void set(Object e) {
			}

			public void add(Object e) {
			}
		};

		ArgumentImpl arg = new ArgumentImpl("name", "description", 0, 1, ' ', ' ', null, "--", null, 0);
		try {
			arg.process(mockCommandLine, mockIterator);
		} catch (OptionException e) {
			fail("Unexpected OptionException");
		}
	}

	@Test
	public void testValidate() {
		// Create a mock WriteableCommandLine object
		WriteableCommandLine mockCommandLine = new WriteableCommandLine() {
			public void addValue(Option option, Object value) {
			}

			public List getValues(Option option) {
				return null;
			}

			public void setDefaultValues(Option option, List values) {
			}

			public boolean looksLikeOption(String arg) {
				return false;
			}
		};

		Option mockOption = new OptionImpl(0, false);

		ArgumentImpl arg = new ArgumentImpl("name", "description", 0, 1, ' ', ' ', null, "--", null, 0);
		try {
			arg.validate(mockCommandLine, mockOption);
		} catch (OptionException e) {
			fail("Unexpected OptionException");
		}
	}

	@Test
	public void testAppendUsage() {
		StringBuffer buffer = new StringBuffer();
		Set helpSettings = new HashSet();
		Comparator comp = null;
		ArgumentImpl arg = new ArgumentImpl("name", "description", 0, 1, ' ', ' ', null, "--", null, 0);
		arg.appendUsage(buffer, helpSettings, comp);
		assertEquals("name", buffer.toString());
	}

	@Test
	public void testGetDescription() {
		ArgumentImpl arg = new ArgumentImpl("name", "description", 0, 1, ' ', ' ', null, "--", null, 0);
		assertEquals("description", arg.getDescription());
	}

	@Test
	public void testHelpLines() {
		Set helpSettings = new HashSet();
		Comparator comp = null;
		ArgumentImpl arg = new ArgumentImpl("name", "description", 0, 1, ' ', ' ', null, "--", null, 0);
		List<HelpLine> lines = arg.helpLines(1, helpSettings, comp);
		assertEquals(1, lines.size());
		assertEquals("name: description", lines.get(0).toString());
	}

	@Test
	public void testGetMaximum() {
		ArgumentImpl arg = new ArgumentImpl("name", "description", 0, 1, ' ', ' ', null, "--", null, 0);
		assertEquals(1, arg.getMaximum());
	}

	@Test
	public void testGetMinimum() {
		ArgumentImpl arg = new ArgumentImpl("name", "description", 0, 1, ' ', ' ', null, "--", null, 0);
		assertEquals(0, arg.getMinimum());
	}

	@Test
	public void testIsRequired() {
		ArgumentImpl arg1 = new ArgumentImpl("name", "description", 0, 1, ' ', ' ', null, "--", null, 0);
		assertFalse(arg1.isRequired());

		ArgumentImpl arg2 = new ArgumentImpl("name", "description", 1, 1, ' ', ' ', null, "--", null, 0);
		assertTrue(arg2.isRequired());
	}

	@Test
	public void testDefaults() {
		// Create a mock WriteableCommandLine object
		WriteableCommandLine mockCommandLine = new WriteableCommandLine() {
			public void addValue(Option option, Object value) {
			}

			public List getValues(Option option) {
				return null;
			}

			public void setDefaultValues(Option option, List values) {
			}

			public boolean looksLikeOption(String arg) {
				return false;
			}
		};

		ArgumentImpl arg = new ArgumentImpl("name", "description", 0, 1, ' ', ' ', null, "--", null, 0);
		arg.defaults(mockCommandLine);
	}

}