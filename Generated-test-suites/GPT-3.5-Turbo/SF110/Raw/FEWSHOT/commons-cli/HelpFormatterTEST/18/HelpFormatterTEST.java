package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class HelpFormatterTest {
    @Test
    public void testSetWidth() {
        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(80);
        assertEquals(80, hf.getWidth());
    }

    @Test
    public void testSetLeftPadding() {
        HelpFormatter hf = new HelpFormatter();
        hf.setLeftPadding(2);
        assertEquals(2, hf.getLeftPadding());
    }

    @Test
    public void testSetDescPadding() {
        HelpFormatter hf = new HelpFormatter();
        hf.setDescPadding(4);
        assertEquals(4, hf.getDescPadding());
    }

    @Test
    public void testSetSyntaxPrefix() {
        HelpFormatter hf = new HelpFormatter();
        hf.setSyntaxPrefix("Usage:");
        assertEquals("Usage:", hf.getSyntaxPrefix());
    }

    @Test
    public void testSetNewLine() {
        HelpFormatter hf = new HelpFormatter();
        hf.setNewLine("\n");
        assertEquals("\n", hf.getNewLine());
    }

    @Test
    public void testSetOptPrefix() {
        HelpFormatter hf = new HelpFormatter();
        hf.setOptPrefix("-");
        assertEquals("-", hf.getOptPrefix());
    }

    @Test
    public void testSetLongOptPrefix() {
        HelpFormatter hf = new HelpFormatter();
        hf.setLongOptPrefix("--");
        assertEquals("--", hf.getLongOptPrefix());
    }

    @Test
    public void testSetArgName() {
        HelpFormatter hf = new HelpFormatter();
        hf.setArgName("arg");
        assertEquals("arg", hf.getArgName());
    }

    @Test
    public void testSetOptionComparator() {
        HelpFormatter hf = new HelpFormatter();
        assertEquals(OptionComparator.class, hf.getOptionComparator().getClass());

        Comparator c = new Comparator() {
            public int compare(Object o1, Object o2) {
                return 0;
            }
        };
        hf.setOptionComparator(c);
        assertEquals(c, hf.getOptionComparator());
    }

    // More test cases can be added to cover the other methods in HelpFormatter class
}