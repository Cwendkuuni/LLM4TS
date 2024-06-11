package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

	@Test
	public void testFlatten_stopAtNonOption() {
		Options options = new Options();
		options.addOption("a", false, "Option a");
		options.addOption("b", true, "Option b");

		PosixParser parser = new PosixParser();

		String[] arguments = {"-a", "value1", "value2", "--", "-b", "value3"};
		String[] expected = {"-a", "value1", "value2", "--", "-b", "value3"};
		assertArrayEquals(expected, parser.flatten(options, arguments, true));
	}

	@Test
	public void testFlatten_notStopAtNonOption() {
		Options options = new Options();
		options.addOption("a", false, "Option a");
		options.addOption("b", true, "Option b");

		PosixParser parser = new PosixParser();

		String[] arguments = {"-a", "value1", "value2", "--", "-b", "value3"};
		String[] expected = {"-a", "value1", "value2", "--", "-b", "value3"};
		assertArrayEquals(expected, parser.flatten(options, arguments, false));
	}

	@Test
	public void testFlatten_longOptionWithValue() {
		Options options = new Options();
		options.addOption("a", false, "Option a");
		options.addOption("b", true, "Option b");

		PosixParser parser = new PosixParser();

		String[] arguments = {"--a=value1", "--b=value2"};
		String[] expected = {"--a", "value1", "--b", "value2"};
		assertArrayEquals(expected, parser.flatten(options, arguments, true));
	}

	@Test
	public void testFlatten_longOptionWithoutValue() {
		Options options = new Options();
		options.addOption("a", false, "Option a");
		options.addOption("b", true, "Option b");

		PosixParser parser = new PosixParser();

		String[] arguments = {"--a", "--b=value2"};
		String[] expected = {"--a", "--b", "value2"};
		assertArrayEquals(expected, parser.flatten(options, arguments, true));
	}

	@Test
	public void testFlatten_singleHyphen() {
		Options options = new Options();
		options.addOption("a", false, "Option a");
		options.addOption("b", true, "Option b");

		PosixParser parser = new PosixParser();

		String[] arguments = {"-"};
		String[] expected = {"-"};
		assertArrayEquals(expected, parser.flatten(options, arguments, true));
	}

	@Test
	public void testFlatten_singleHyphenAndValue() {
		Options options = new Options();
		options.addOption("a", false, "Option a");
		options.addOption("b", true, "Option b");

		PosixParser parser = new PosixParser();

		String[] arguments = {"-", "value1", "value2"};
		String[] expected = {"-", "value1", "value2"};
		assertArrayEquals(expected, parser.flatten(options, arguments, true));
	}

	@Test
	public void testFlatten_singleCharacterOption() {
		Options options = new Options();
		options.addOption("a", false, "Option a");
		options.addOption("b", true, "Option b");

		PosixParser parser = new PosixParser();

		String[] arguments = {"-a", "-b=value1", "-c"};
		String[] expected = {"-a", "-b", "value1", "-c"};
		assertArrayEquals(expected, parser.flatten(options, arguments, true));
	}

	@Test
	public void testFlatten_multiCharacterOption() {
		Options options = new Options();
		options.addOption("a", false, "Option a");
		options.addOption("bb", true, "Option bb");

		PosixParser parser = new PosixParser();

		String[] arguments = {"-a", "-bb=value1", "-c"};
		String[] expected = {"-a", "-bb", "value1", "-c"};
		assertArrayEquals(expected, parser.flatten(options, arguments, true));
	}

	@Test
	public void testFlatten_unrecognizedOption_stopAtNonOption() {
		Options options = new Options();
		options.addOption("a", false, "Option a");
		options.addOption("b", true, "Option b");

		PosixParser parser = new PosixParser();

		String[] arguments = {"-a", "-d=value1", "-b=value2"};
		String[] expected = {"-a", "--", "-d=value1", "-b", "value2"};
		assertArrayEquals(expected, parser.flatten(options, arguments, true));
	}

	@Test
	public void testFlatten_unrecognizedOption_notStopAtNonOption() {
		Options options = new Options();
		options.addOption("a", false, "Option a");
		options.addOption("b", true, "Option b");

		PosixParser parser = new PosixParser();

		String[] arguments = {"-a", "-d=value1", "-b=value2"};
		String[] expected = {"-a", "-d", "value1", "-b", "value2"};
		assertArrayEquals(expected, parser.flatten(options, arguments, false));
	}

	@Test
	public void testFlatten_noOption() {
		Options options = new Options();
		options.addOption("a", false, "Option a");
		options.addOption("b", true, "Option b");

		PosixParser parser = new PosixParser();

		String[] arguments = {"value1", "value2"};
		String[] expected = {"value1", "value2"};
		assertArrayEquals(expected, parser.flatten(options, arguments, true));
	}

}