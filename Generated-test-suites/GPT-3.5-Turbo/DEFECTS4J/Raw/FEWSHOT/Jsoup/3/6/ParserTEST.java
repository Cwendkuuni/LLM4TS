package org.jsoup.parser;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 Parses HTML into a {@link Document}. Generally best to use one of the  more convenient parse methods in {@link org.jsoup.Jsoup}.

 @author Jonathan Hedley, jonathan@hedley.net */
public class ParserTest {
    private Parser parser;
    private Document doc;

    @Before
    public void setup() {
        parser = new Parser("", "", false);
        doc = new Document("");
    }

    @Test
    public void testParse() {
        
    }

    @Test
    public void testParseComment() {
        
    }

    @Test
    public void testParseXmlDecl() {
        
    }

    @Test
    public void testParseEndTag() {
        
    }

    @Test
    public void testParseStartTag() {
        
    }

    @Test
    public void testParseTextNode() {
        
    }

    @Test
    public void testParseCdata() {
        
    }

    @Test
    public void testAddChildToParent() {
        
    }

    @Test
    public void testStackHasValidParent() {
        
    }

    @Test
    public void testPopStackToSuitableContainer() {
        
    }

    @Test
    public void testPopStackToClose() {
        
    }

    @Test
    public void testLast() {
        
    }
}