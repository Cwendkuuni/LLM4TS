package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class GnuParserTest {
    private Options options;
    private GnuParser parser;

    @Before
    public void setUp() {
        options = mock(Options.class);
        parser = new GnuParser();
    }

    @Test
    public void testFlatten_withStopAtNonOptionTrue() {
        String[] arguments = {"-a", "-b", "--", "-c", "-d", "-e"};

        when(options.hasOption("-a")).thenReturn(true);
        when(options.hasOption("-b")).thenReturn(true);
        when(options.hasOption("--")).thenReturn(false);
        when(options.hasOption("-c")).thenReturn(false);
        when(options.hasOption("-d")).thenReturn(false);
        when(options.hasOption("-e")).thenReturn(false);

        String[] expected = {"-a", "-b", "--", "-c", "-d", "-e"};
        String[] actual = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, actual);

        verify(options).hasOption("-a");
        verify(options).hasOption("-b");
        verify(options).hasOption("--");
        verify(options).hasOption("-c");
        verify(options).hasOption("-d");
        verify(options).hasOption("-e");
    }

    @Test
    public void testFlatten_withStopAtNonOptionFalse() {
        String[] arguments = {"-a", "-b", "--", "-c", "-d", "-e"};

        when(options.hasOption("-a")).thenReturn(true);
        when(options.hasOption("-b")).thenReturn(true);
        when(options.hasOption("--")).thenReturn(false);
        when(options.hasOption("-c")).thenReturn(false);
        when(options.hasOption("-d")).thenReturn(false);
        when(options.hasOption("-e")).thenReturn(false);

        String[] expected = {"-a", "-b", "--", "-c", "-d", "-e"};
        String[] actual = parser.flatten(options, arguments, false);

        assertArrayEquals(expected, actual);

        verify(options).hasOption("-a");
        verify(options).hasOption("-b");
        verify(options).hasOption("--");
        verify(options).hasOption("-c");
        verify(options).hasOption("-d");
        verify(options).hasOption("-e");
    }
}