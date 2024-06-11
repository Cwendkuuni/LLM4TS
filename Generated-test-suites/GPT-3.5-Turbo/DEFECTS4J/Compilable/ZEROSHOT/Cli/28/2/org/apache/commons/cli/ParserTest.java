package org.apache.commons.cli;
import org.junit.Test;
import org.apache.commons.cli.*;

public class ParserTest {

	@Test
	public void testParse() throws ParseException {
		Options options = new Options();
		
		Option help = new Option("h", "help", false, "print this message");
		Option version = new Option("v", "version", false, "print the version information");
		Option newFile = new Option("n", "new", true, "create new file with specified name");
		Option outputFile = new Option("o", "output", true, "output file to write to");
		
		options.addOption(help);
		options.addOption(version);
		options.addOption(newFile);
		options.addOption(outputFile);
		
		String[] args1 = new String[] {"-h"};
		String[] args2 = new String[] {"--help"};
		String[] args3 = new String[] {"-v"};
		String[] args4 = new String[] {"--version"};
		String[] args5 = new String[] {"-n", "file1.txt"};
		String[] args6 = new String[] {"--new", "file2.txt"};
		String[] args7 = new String[] {"-o", "output.txt"};
		String[] args8 = new String[] {"--output", "output.txt"};
		
		Parser parser = new Parser() {
			@Override
			protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
				return arguments;
			}
		};
		
		parser.parse(options, args1);
		parser.parse(options, args2);
		parser.parse(options, args3);
		parser.parse(options, args4);
		parser.parse(options, args5);
		parser.parse(options, args6);
		parser.parse(options, args7);
		parser.parse(options, args8);
	}
}