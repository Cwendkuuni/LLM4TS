import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.ArrayList;

public class GnuParserTest {

    @Test
    public void testFlatten_noOptions_noHyphen() throws ParseException {
        String[] args = new String[]{"arg1", "arg2"};
        Options options = new Options();

        String[] result = new GnuParser().parse(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("arg1");
        expected.add("arg2");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_singleOption_withHyphen() throws ParseException {
        String[] args = new String[]{"-a", "arg1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] result = new GnuParser().parse(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("arg1");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_singleOption_withoutHyphen() throws ParseException {
        String[] args = new String[]{"a", "arg1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] result = new GnuParser().parse(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("arg1");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_singleOption_withValue() throws ParseException {
        String[] args = new String[]{"-a", "value1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] result = new GnuParser().parse(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("value1");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_multipleOptions_withHyphens() throws ParseException {
        String[] args = new String[]{"-a", "-b", "arg1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] result = new GnuParser().parse(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("-b");
        expected.add("arg1");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_multipleOptions_withoutHyphens() throws ParseException {
        String[] args = new String[]{"a", "b", "arg1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        String[] result = new GnuParser().parse(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("a");
        expected.add("b");
        expected.add("arg1");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_longOption() throws ParseException {
        String[] args = new String[]{"--longOption", "arg1"};
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");

        String[] result = new GnuParser().parse(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--longOption");
        expected.add("arg1");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_doubleHyphen() throws ParseException {
        String[] args = new String[]{"--", "arg1", "arg2"};
        Options options = new Options();

        String[] result = new GnuParser().parse(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("arg1");
        expected.add("arg2");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_doubleHyphen_noArguments() throws ParseException {
        String[] args = new String[]{"--"};
        Options options = new Options();

        String[] result = new GnuParser().parse(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("--");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_stopAtNonOption() throws ParseException {
        String[] args = new String[]{"-a", "arg1", "nonOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] result = new GnuParser().parse(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("arg1");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result, "Expected only 'a' and 'arg1'");
    }

    @Test
    public void testFlatten_stopAtNonOption_lastArgumentIsOption() throws ParseException {
        String[] args = new String[]{"-a", "arg1", "--option"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("option", true, "Option Option");

        String[] result = new GnuParser().parse(options, args, true);

        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("arg1");
        expected.add("--option");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result, "Expected 'a', 'arg1' and '--option'");
    }

    @Test
    public void testFlatten_specialOption() throws ParseException {
        String[] args = new String[]{"-Dproperty=value", "arg1"};
        Options options = new Options();

        String[] result = new GnuParser().parse(options, args, false);

        List<String> expected = new ArrayList<>();
        expected.add("-D");
        expected.add("property=value");
        expected.add("arg1");

        Assertions.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }
}