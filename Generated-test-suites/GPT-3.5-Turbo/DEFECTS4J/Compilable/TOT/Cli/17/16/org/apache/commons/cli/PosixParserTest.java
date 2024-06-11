package org.apache.commons.cli;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class PosixParserTest {
	
	@Test
	public void testFlatten_validOption_returnsFlattenedStringArray() {
		Options options = new Options();
		Option option = new Option("a", false, "option a");
		options.addOption(option);
		
		String[] arguments = {"-a"};
		
		PosixParser parser = new PosixParser();
		String[] result = parser.flatten(options, arguments, true);
		
		String[] expectedResult = {"-a"};
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	public void testFlatten_validOptionMultipleValues_returnsFlattenedStringArray() {
		Options options = new Options();
		Option option = new Option("f", true, "option f");
		options.addOption(option);
		
		String[] arguments = {"-f", "file1.txt", "-f", "file2.txt"};
		
		PosixParser parser = new PosixParser();
		String[] result = parser.flatten(options, arguments, true);
		
		String[] expectedResult = {"-f", "file1.txt", "-f", "file2.txt"};
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	public void testFlatten_shortOptionWithValue_returnsFlattenedStringArray() {
		Options options = new Options();
		Option option = new Option("o", true, "option o");
		options.addOption(option);
		
		String[] arguments = {"-o", "value"};
		
		PosixParser parser = new PosixParser();
		String[] result = parser.flatten(options, arguments, true);
		
		String[] expectedResult = {"-o", "value"};
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	public void testFlatten_shortOptionWithMultipleValues_returnsFlattenedStringArray() {
		Options options = new Options();
		Option option = new Option("f", true, "option f");
		options.addOption(option);
		
		String[] arguments = {"-f", "file1.txt", "file2.txt"};
		
		PosixParser parser = new PosixParser();
		String[] result = parser.flatten(options, arguments, true);
		
		String[] expectedResult = {"-f", "file1.txt", "file2.txt"};
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	public void testFlatten_invalidOptionWithStopAtNonOption_returnsFlattenedStringArrayWithRemainingTokens() {
		Options options = new Options();
		
		String[] arguments = {"-r", "file.txt", "-f", "file1.txt", "file2.txt"};
		
		PosixParser parser = new PosixParser();
		String[] result = parser.flatten(options, arguments, true);
		
		String[] expectedResult = {"--", "-r", "file.txt", "-f", "file1.txt", "file2.txt"};
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	public void testFlatten_invalidOptionWithoutStopAtNonOption_returnsFlattenedStringArray() {
		Options options = new Options();
		
		String[] arguments = {"-r", "file.txt", "-f", "file1.txt", "file2.txt"};
		
		PosixParser parser = new PosixParser();
		String[] result = parser.flatten(options, arguments, false);
		
		String[] expectedResult = {"-r", "file.txt", "-f", "file1.txt", "file2.txt"};
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	public void testFlatten_hyphenToken_returnsFlattenedStringArray() {
		Options options = new Options();
		
		String[] arguments = {"-"};
		
		PosixParser parser = new PosixParser();
		String[] result = parser.flatten(options, arguments, true);
		
		String[] expectedResult = {"-"};
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	public void testFlatten_doubleHyphenToken_returnsFlattenedStringArray() {
		Options options = new Options();
		
		String[] arguments = {"--"};
		
		PosixParser parser = new PosixParser();
		String[] result = parser.flatten(options, arguments, true);
		
		String[] expectedResult = {"--"};
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	public void testFlatten_doubleHyphenOptionWithValue_returnsFlattenedStringArray() {
		Options options = new Options();
		Option option = new Option("o", true, "option o");
		options.addOption(option);
		
		String[] arguments = {"--option=value"};
		
		PosixParser parser = new PosixParser();
		String[] result = parser.flatten(options, arguments, true);
		
		String[] expectedResult = {"--option", "value"};
		assertArrayEquals(expectedResult, result);
	}
	
	@Test
	public void testFlatten_doubleHyphenOptionWithMultipleValues_returnsFlattenedStringArray() {
		Options options = new Options();
		Option option = new Option("f", true, "option f");
		options.addOption(option);
		
		String[] arguments = {"--files=file1.txt,file2.txt,file3.txt"};
		
		PosixParser parser = new PosixParser();
		String[] result = parser.flatten(options, arguments, true);
		
		String[] expectedResult = {"--files", "file1.txt,file2.txt,file3.txt"};
		assertArrayEquals(expectedResult, result);
	}

}