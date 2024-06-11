import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilderWithValidArguments() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("abc:@java.lang.String,def:+org.apache.commons.cli2.Option,!g:@boolean,h:/http://example.com,i:#2022-01-01,j:<path/to/existingFile.txt,k:*path/to/nonExistingFile.txt,l:/path/to/anotherFile.url");

        Option option = pb.create();

        CommandLineParser parser = new org.apache.commons.cli2.BasicParser();

        try {
            parser.parse(option.getOpts(), new String[]{"-a", "abc", "-g", "true", "-h", "http://example.com", "-i", "2022-01-01", "-j", "path/to/existingFile.txt", "-k", "path/to/nonExistingFile.txt", "-l", "path/to/anotherFile.url"});
        } catch (ParseException e) {
            Assert.fail("ParseException thrown: " + e.getMessage());
        }

        new HelpFormatter().printHelp("Test", option.getLongOptHelp(), true);
    }

    @Test
    public void testPatternBuilderWithInvalidArgumentTypes() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("abc:@java.lang.Integer,def:+org.apache.commons.cli2.Argument,!g:@boolean,h:/http://example.com,i:#2022-01-01,j:<path/to/existingFile.txt,k:*path/to/nonExistingFile.txt,l:/path/to/anotherFile.url,m:@invalidType");

        try {
            pb.create();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testPatternBuilderWithMandatoryArguments() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("abc:@java.lang.String,def:*mandatoryArgument,!g:@boolean,h:/http://example.com,i:#2022-01-01,j:<path/to/existingFile.txt,k:*path/to/nonExistingFile.txt");

        Option option = pb.create();

        CommandLineParser parser = new org.apache.commons.cli2.BasicParser();

        try {
            parser.parse(option.getOpts(), new String[]{"-a"});
            Assert.fail("Expected ParseException for missing argument 'def'");
        } catch (ParseException e) {
            // Expected
        }
    }
}