package com.jstevh.viewer;

import org.junit.Test;
import static org.junit.Assert.*;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testStaticMethods() {
        // Add tests for static methods here
    }
}