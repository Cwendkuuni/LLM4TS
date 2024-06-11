package org.jsoup.helper;
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // typical use case
        // edge case: when input is null

        // error scenario: ParserConfigurationException
    }

    @Test
    public void testConvert() {
        // typical use case
        // edge case: when input document is empty
        // edge case: when input location is not null
    }

    @Test
    public void testW3CBuilder() {
        // typical use case
        // edge case: when source is Element
        // edge case: when source is TextNode
        // edge case: when source is Comment
        // edge case: when source is DataNode
    }

    @Test
    public void testAsString() {
        // typical use case
        // edge case: when document is empty
        // error scenario: TransformerException
    }
}