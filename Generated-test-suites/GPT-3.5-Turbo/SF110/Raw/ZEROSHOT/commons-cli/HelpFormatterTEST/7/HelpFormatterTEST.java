package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

public class HelpFormatterTest {
    
    private HelpFormatter helpFormatter;
    
    @Before
    public void setUp() {
        helpFormatter = new HelpFormatter();
    }
    
    @Test
    public void testSetWidth() {
        helpFormatter.setWidth(80);
        assertEquals(80, helpFormatter.getWidth());
    }
    
    @Test
    public void testSetLeftPadding() {
        helpFormatter.setLeftPadding(2);
        assertEquals(2, helpFormatter.getLeftPadding());
    }
    
    @Test
    public void testSetDescPadding() {
        helpFormatter.setDescPadding(4);
        assertEquals(4, helpFormatter.getDescPadding());
    }
    
    @Test
    public void testSetSyntaxPrefix() {
        helpFormatter.setSyntaxPrefix("Example: ");
        assertEquals("Example: ", helpFormatter.getSyntaxPrefix());
    }
    
    @Test
    public void testSetNewLine() {
        helpFormatter.setNewLine("\r\n");
        assertEquals("\r\n", helpFormatter.getNewLine());
    }
    
    @Test
    public void testSetOptPrefix() {
        helpFormatter.setOptPrefix("--");
        assertEquals("--", helpFormatter.getOptPrefix());
    }
    
    @Test
    public void testSetLongOptPrefix() {
        helpFormatter.setLongOptPrefix("::");
        assertEquals("::", helpFormatter.getLongOptPrefix());
    }
    
    @Test
    public void testSetArgName() {
        helpFormatter.setArgName("argument");
        assertEquals("argument", helpFormatter.getArgName());
    }
    
    @Test
    public void testSetOptionComparator() {
        HelpFormatter.OptionComparator comparator = new HelpFormatter.OptionComparator();
        helpFormatter.setOptionComparator(comparator);
        assertEquals(comparator, helpFormatter.getOptionComparator());
    }
    
    @Test
    public void testPrintWrapped() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        helpFormatter.printWrapped(printStream, 40, "This is a test");
        assertTrue(outputStream.toString().contains("This is a test"));
    }
    
    @Test
    public void testFindWrapPos() {
        int pos = helpFormatter.findWrapPos("This is a test", 10, 0);
        assertEquals(10, pos);
    }
    
    @Test
    public void testCreatePadding() {
        String padding = helpFormatter.createPadding(5);
        assertEquals("     ", padding);
    }
    
    @Test
    public void testRtrim() {
        String rtrim = helpFormatter.rtrim("This is a test ");
        assertEquals("This is a test", rtrim);
    }
    
    @Test
    public void testOptionComparatorCompare() {
        HelpFormatter.OptionComparator comparator = new HelpFormatter.OptionComparator();
        Option option1 = new Option("a", "add", false, "Add item");
        Option option2 = new Option("b", "backup", false, "Backup data");
        assertEquals(-1, comparator.compare(option1, option2));
    }
    
    // Add more test cases for each method
    
}