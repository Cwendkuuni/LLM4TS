import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

	@Test
	public void testParse() throws ParseException {
		Options options = new Options();

		Option option1 = new Option("f", "file", true, "input file");
		option1.setRequired(true);
		options.addOption(option1);

		Option option2 = new Option("d", "directory", true, "input directory");
		options.addOption(option2);

		Parser parser = new DefaultParser();

		String[] args1 = {"-f", "input.txt"};
		CommandLine cmd1 = parser.parse(options, args1);
		assertTrue(cmd1.hasOption("f"));
		assertEquals("input.txt", cmd1.getOptionValue("f"));

		String[] args2 = {"-f", "input.txt", "-d", "input"};
		CommandLine cmd2 = parser.parse(options, args2);
		assertTrue(cmd2.hasOption("f"));
		assertEquals("input.txt", cmd2.getOptionValue("f"));
		assertTrue(cmd2.hasOption("d"));
		assertEquals("input", cmd2.getOptionValue("d"));

		String[] args3 = {"-d", "input"};
		try {
			parser.parse(options, args3);
			fail("Should throw MissingOptionException");
		} catch (MissingOptionException e) {
			// expected
		}
	}

	@Test
	public void testFlatten() {
		Options options = new Options();

		Option option1 = new Option("f", "file", true, "input file");
		option1.setRequired(true);
		options.addOption(option1);

		Option option2 = new Option("d", "directory", true, "input directory");
		options.addOption(option2);

		Parser parser = new DefaultParser();

		String[] args1 = {"-f", "input.txt"};
		String[] flattenedArgs1 = parser.flatten(options, args1, false);
		assertArrayEquals(args1, flattenedArgs1);

		String[] args2 = {"-f", "input.txt", "-d", "input"};
		String[] flattenedArgs2 = parser.flatten(options, args2, false);
		assertArrayEquals(args2, flattenedArgs2);

		String[] args3 = {"-f", "input.txt", "-d", "input"};
		String[] flattenedArgs3 = parser.flatten(options, args3, true);
		assertEquals(3, flattenedArgs3.length);
		assertEquals("-f", flattenedArgs3[0]);
		assertEquals("input.txt", flattenedArgs3[1]);
		assertEquals("-d", flattenedArgs3[2]);
	}

	@Test
	public void testProcessArgs() throws ParseException {
		Options options = new Options();

		Option option1 = new Option("f", "file", true, "input file");
		option1.setRequired(true);
		options.addOption(option1);

		Option option2 = new Option("d", "directory", true, "input directory");
		options.addOption(option2);

		Parser parser = new DefaultParser();

		Option opt = options.getOption("file");
		ListIterator<String> iterator = Arrays.asList("input.txt", "-d", "input").listIterator();

		parser.processArgs(opt, iterator);
		assertTrue(opt.hasValue());
		assertEquals("input.txt", opt.getValue());

		assertTrue(iterator.hasNext());
		assertEquals("-d", iterator.next());
		assertTrue(iterator.hasNext());
		assertEquals("input", iterator.next());

		Option opt2 = options.getOption("directory");
		parser.processArgs(opt2, iterator);
		assertTrue(opt2.hasValue());
		assertEquals("input", opt2.getValue());

		assertFalse(iterator.hasNext());
	}

	@Test
	public void testProcessOption() throws ParseException {
		Options options = new Options();

		Option option1 = new Option("f", "file", true, "input file");
		option1.setRequired(true);
		options.addOption(option1);

		Option option2 = new Option("d", "directory", true, "input directory");
		options.addOption(option2);

		Parser parser = new DefaultParser();

		ListIterator<String> iterator = Arrays.asList("-f", "input.txt", "-d", "input").listIterator();

		parser.processOption("-f", iterator);
		assertTrue(iterator.hasNext());
		assertEquals("input.txt", iterator.next());

		parser.processOption("-d", iterator);
		assertTrue(iterator.hasNext());
		assertEquals("input", iterator.next());

		assertFalse(iterator.hasNext());
	}

	@Test(expected = UnrecognizedOptionException.class)
	public void testProcessOptionInvalid() throws ParseException {
		Options options = new Options();

		Option option1 = new Option("f", "file", true, "input file");
		option1.setRequired(true);
		options.addOption(option1);

		Parser parser = new DefaultParser();

		ListIterator<String> iterator = Arrays.asList("-i", "input.txt").listIterator();

		parser.processOption("-i", iterator);
	}

	@Test(expected = MissingArgumentException.class)
	public void testProcessArgsMissing() throws ParseException {
		Options options = new Options();

		Option option1 = new Option("f", "file", true, "input file");
		option1.setRequired(true);
		options.addOption(option1);

		Parser parser = new DefaultParser();

		Option opt = options.getOption("file");
		ListIterator<String> iterator = Arrays.asList("-f").listIterator();

		parser.processArgs(opt, iterator);
	}

	@Test(expected = MissingOptionException.class)
	public void testCheckRequiredOptions() throws ParseException {
		Options options = new Options();

		Option option1 = new Option("f", "file", true, "input file");
		option1.setRequired(true);
		options.addOption(option1);

		Option option2 = new Option("d", "directory", true, "input directory");
		option2.setRequired(true);
		options.addOption(option2);

		Option option3 = new Option("o", "output", true, "output file");
		option3.setRequired(true);
		options.addOption(option3);

		Parser parser = new DefaultParser();

		String[] args = {"-f", "input.txt"};

		parser.parse(options, args);
	}
}