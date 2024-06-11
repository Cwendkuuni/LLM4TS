import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

public class PosixParserTest {

    private Options options;
    private PosixParser parser;

    @Before
    public void setup() {
        options = mock(Options.class);
        parser = spy(new PosixParser());
    }

    @Test
    public void testFlatten_stopAtNonOption() {
        String[] arguments = { "arg1", "-f", "arg2" };

        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(true);
        when(options.getOption("-f")).thenReturn(option);
        when(options.hasOption("-f")).thenReturn(true);

        String[] result = parser.flatten(options, arguments, true);

        assertEquals(3, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("-f", result[1]);
        assertEquals("arg2", result[2]);
    }

    @Test
    public void testFlatten_notStopAtNonOption() {
        String[] arguments = { "arg1", "-f", "arg2" };

        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(true);
        when(options.getOption("-f")).thenReturn(option);
        when(options.hasOption("-f")).thenReturn(true);

        String[] result = parser.flatten(options, arguments, false);

        assertEquals(2, result.length);
        assertEquals("arg1", result[0]);
        assertEquals("-f", result[1]);

        verify(parser, never()).process(any());
        verify(parser, never()).gobble(any());
        verify(parser, never()).processOptionToken(anyString(), anyBoolean());
        verify(parser, never()).burstToken(anyString(), anyBoolean());
    }

    @Test
    public void testProcessOptionToken_withOption() {
        Option option = mock(Option.class);
        when(options.getOption("-f")).thenReturn(option);
        when(options.hasOption("-f")).thenReturn(true);

        parser.processOptionToken("-f", false);

        verify(parser).processOptionToken("-f", false);
        verify(parser).tokens.add("-f");
        verify(parser).currentOption = option;
    }

    @Test
    public void testProcessOptionToken_withoutOption_stopAtNonOption() {
        parser.processOptionToken("-x", true);

        verify(parser).processOptionToken("-x", true);
        verify(parser).eatTheRest = true;
        verify(parser).tokens.add("-x");
    }

    @Test
    public void testProcessOptionToken_withoutOption_notStopAtNonOption() {
        parser.processOptionToken("-x", false);

        verify(parser).processOptionToken("-x", false);
        verify(parser).tokens.add("-x");
    }

    @Test
    public void testProcess_withCurrentOption_hasArg() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(true);

        parser.currentOption = option;
        parser.process("arg");

        verify(parser).process("arg");
        verify(parser).tokens.add("arg");
        verify(parser).currentOption = null;
    }

    @Test
    public void testProcess_withCurrentOption_hasArgs() {
        Option option = mock(Option.class);
        when(option.hasArg()).thenReturn(false);
        when(option.hasArgs()).thenReturn(true);

        parser.currentOption = option;
        parser.process("arg");

        verify(parser).process("arg");
        verify(parser).tokens.add("arg");
    }

    @Test
    public void testProcess_withoutCurrentOption_stopAtNonOption() {
        parser.process("arg");

        verify(parser).process("arg");
        verify(parser).eatTheRest = true;
        verify(parser).tokens.add("--");
        verify(parser).tokens.add("arg");
    }

    @Test
    public void testProcess_withoutCurrentOption_notStopAtNonOption() {
        parser.process("arg");

        verify(parser).process("arg");
        verify(parser).tokens.add("arg");
    }

    @Test
    public void testBurstToken_nonOption_stopAtNonOption() {
        parser.burstToken("-abcd", true);

        verify(parser).burstToken("-abcd", true);
        verify(parser).process("-abcd");
        verify(parser, never()).tokens.add("-a");
        verify(parser, never()).tokens.add("-b");
        verify(parser, never()).tokens.add("-c");
        verify(parser, never()).tokens.add("-d");
    }

    @Test
    public void testBurstToken_nonOption_notStopAtNonOption() {
        parser.burstToken("-abcd", false);

        verify(parser).burstToken("-abcd", false);
        verify(parser).tokens.add("-a");
        verify(parser, never()).process(any());
    }

    @Test
    public void testBurstToken_withOption_hasArg() {
        Option option = mock(Option.class);
        when(options.hasOption("-a")).thenReturn(true);
        when(options.getOption("-a")).thenReturn(option);

        parser.burstToken("-abcd", false);

        verify(parser).burstToken("-abcd", false);
        verify(parser).tokens.add("-a");
        verify(parser).currentOption = option;
        verify(parser, never()).tokens.add(anyString());
    }

    @Test
    public void testBurstToken_withOption_hasArgs() {
        Option option = mock(Option.class);
        when(options.hasOption("-a")).thenReturn(true);
        when(options.getOption("-a")).thenReturn(option);
        when(option.hasArg()).thenReturn(false);
        when(option.hasArgs()).thenReturn(true);

        parser.burstToken("-abcd", false);

        verify(parser).burstToken("-abcd", false);
        verify(parser).tokens.add("-a");
        verify(parser, never()).process(any());
    }

    @Test
    public void testGobble() {
        Iterator iter = Arrays.asList("arg1", "arg2", "arg3").iterator();
        parser.eatTheRest = true;

        parser.gobble(iter);

        verify(parser).gobble(iter);
        verify(parser).tokens.add("arg1");
        verify(parser).tokens.add("arg2");
        verify(parser).tokens.add("arg3");
    }
}