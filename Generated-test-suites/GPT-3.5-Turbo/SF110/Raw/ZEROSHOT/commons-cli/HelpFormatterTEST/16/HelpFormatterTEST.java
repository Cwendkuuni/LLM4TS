package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        assertEquals(80, formatter.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(2);
        assertEquals(2, formatter.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(4);
        assertEquals(4, formatter.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("Usage: ");
        assertEquals("Usage: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\r\n");
        assertEquals("\r\n", formatter.getNewLine());
    }

    @Test
    public void testSetOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("/");
        assertEquals("/", formatter.getOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("//");
        assertEquals("//", formatter.getLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("argument");
        assertEquals("argument", formatter.getArgName());
    }

    @Test
    public void testSetOptionComparator() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptionComparator(new OptionComparator());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testPrintHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("program", new Options());
    }

    @Test
    public void testPrintHelpWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("program", new Options(), true);
    }

    @Test
    public void testPrintHelpWithHeaderAndFooter() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("program", "header", new Options(), "footer");
    }

    @Test
    public void testPrintHelpWithHeaderFooterAndAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("program", "header", new Options(), "footer", true);
    }

    @Test
    public void testPrintHelpWithWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(80, "program", "header", new Options(), "footer", true);
    }

    @Test
    public void testPrintHelpWithWidthAndPrintWriter() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(new PrintWriter(System.out), 80, "program", "header", new Options(), 1, 3, "footer");
    }

    @Test
    public void testPrintUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(new PrintWriter(System.out), 80, "program", new Options());
    }

    @Test
    public void testPrintUsageWithWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(new PrintWriter(System.out), 80, "program");
    }

    @Test
    public void testPrintOptions() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printOptions(new PrintWriter(System.out), 80, new Options(), 1, 3);
    }

    @Test
    public void testPrintWrapped() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printWrapped(new PrintWriter(System.out), 80, "text");
    }

    @Test
    public void testPrintWrappedWithNextLineTabStop() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printWrapped(new PrintWriter(System.out), 80, 2, "text");
    }

    @Test
    public void testRenderOptions() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.renderOptions(new StringBuffer(), 80, new Options(), 1, 3);
    }

    @Test
    public void testRenderWrappedText() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.renderWrappedText(new StringBuffer(), 80, 2, "text");
    }

    @Test
    public void testFindWrapPos() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.findWrapPos("text", 80, 0);
    }

    @Test
    public void testCreatePadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.createPadding(10);
    }

    @Test
    public void testRtrim() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.rtrim("text");
    }
}