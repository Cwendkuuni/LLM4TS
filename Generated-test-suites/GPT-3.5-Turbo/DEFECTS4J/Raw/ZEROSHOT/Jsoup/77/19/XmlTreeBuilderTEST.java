package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        // TODO: Implement test
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        // TODO: Implement test
    }

    @Test
    public void testInitialiseParse() {
        // TODO: Implement test
    }

    @Test
    public void testProcess() {
        // TODO: Implement test
    }

    @Test
    public void testInsertNode() {
        // TODO: Implement test
    }

    @Test
    public void testInsertStartTag() {
        // TODO: Implement test
    }

    @Test
    public void testInsertComment() {
        // TODO: Implement test
    }

    @Test
    public void testInsertCharacter() {
        // TODO: Implement test
    }

    @Test
    public void testInsertDoctype() {
        // TODO: Implement test
    }

    @Test
    public void testPopStackToClose() {
        // TODO: Implement test
    }

    @Test
    public void testParseFragment() {
        // TODO: Implement test
    }
}