import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.cli2.builder.PatternBuilder.*;

/**
 * Tests for PatternBuilder
 */
public class PatternBuilderTest {

    @Test
    public void testOptionalStringArgument() {
        final PatternBuilder builder = new PatternBuilder();

        builder.withPattern("-s,--string=s");

        final Option option = builder.create();
        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"-s", "value"}));
            Assert.assertEquals("value", cmd.getOptionValue("string"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"--string"}));
            Assert.assertNull(cmd.getOptionValue("string"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionalNumberArgument() {
        final PatternBuilder builder = new PatternBuilder();

        builder.withPattern("-i,--integer=i");

        final Option option = builder.create();
        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"-i", "10"}));
            Assert.assertEquals(10, cmd.getOptionValue("integer", Integer. class).intValue());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"--integer"}));
            Assert.assertNull(cmd.getOptionValue("integer"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionalDateArgument() {
        final PatternBuilder builder = new PatternBuilder();

        builder.withPattern("-d,--date=d");

        final Option option = builder.create();
        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"-d", "2022-01-01"}));
            Assert.assertEquals("2022-01-01", cmd.getOptionValue("date"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"--date"}));
            Assert.assertNull(cmd.getOptionValue("date"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionalClassArgument() {
        final PatternBuilder builder = new PatternBuilder();

        builder.withPattern("-c,--class=cn.test.MyClass");

        final Option option = builder.create();
        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"-c", "cn.test.MyClass"}));
            Assert.assertEquals("cn.test.MyClass", cmd.getOptionValue("class"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"--class"}));
            Assert.assertNull(cmd.getOptionValue("class"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionalFileArgument() {
        final PatternBuilder builder = new PatternBuilder();

        builder.withPattern("-f,--file=file");

        final Option option = builder.create();
        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"-f", "/path/to/file.txt"}));
            Assert.assertNotNull(cmd.getOptionValue("file"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"--file"}));
            Assert.assertNull(cmd.getOptionValue("file"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMandatoryStringArgument() {
        final PatternBuilder builder = new PatternBuilder();

        builder.withPattern("s:,--string=s");

        final Option option = builder.create();
        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"--string", "value"}));
            Assert.assertEquals("value", cmd.getOptionValue("string"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{}));
            Assert.fail("Expected ParseException, but none was thrown");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testMandatoryNumberArgument() {
        final PatternBuilder builder = new PatternBuilder();

        builder.withPattern("i::,--integer=i");

        final Option option = builder.create();
        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"--integer", "10"}));
            Assert.assertEquals(10, cmd.getOptionValue("integer", Integer.class).intValue());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{}));
            Assert.fail("Expected ParseException, but none was thrown");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testMandatoryDateArgument() {
        final PatternBuilder builder = new PatternBuilder();

        builder.withPattern("d::,--date=d");

        final Option option = builder.create();
        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"--date", "2022-01-01"}));
            Assert.assertEquals("2022-01-01", cmd.getOptionValue("date"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{}));
            Assert.fail("Expected ParseException, but none was thrown");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testMandatoryClassArgument() {
        final PatternBuilder builder = new PatternBuilder();

        builder.withPattern("c::,--class=cn.test.MyClass");

        final Option option = builder.create();
        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"--class", "cn.test.MyClass"}));
            Assert.assertEquals("cn.test.MyClass", cmd.getOptionValue("class"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{}));
            Assert.fail("Expected ParseException, but none was thrown");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testMandatoryFileArgument() {
        final PatternBuilder builder = new PatternBuilder();

        builder.withPattern("f::,--file=file");

        final Option option = builder.create();
        final CommandLineParser parser = new DefaultParser();

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{"--file", "/path/to/file.txt"}));
            Assert.assertNotNull(cmd.getOptionValue("file"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            final CommandLine cmd = parser.parse(option.parse(new String[]{}));
            Assert.fail("Expected ParseException, but none was thrown");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testInvalidPattern() {
        final PatternBuilder builder = new PatternBuilder();

        boolean caughtException = false;

        try {
            builder.withPattern("x:,--string=s");
            final Option option = builder.create();
            new HelpFormatter().printHelp("test", option, true);
        } catch (IllegalArgumentException e) {
            caughtException = true;
        }

        Assert.assertTrue("Expected IllegalArgumentException, but none was thrown", caughtException);
    }
}