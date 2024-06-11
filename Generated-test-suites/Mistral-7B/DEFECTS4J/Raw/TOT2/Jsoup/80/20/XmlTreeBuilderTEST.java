package org.jsoup.parser; // import statements removed for brevity

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @org.junit.Test
    public void testParseWithDefaultSettings() {
        String xmlInput = "<root><element attr=\"value\">Content</element></root>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(xmlInput), "baseUri");
        assertEquals("root", doc.tagName());
        Element root = doc.getElementsByTagName("root").get(0);
        Element element = root.getElementsByTagName("element").get(0);
        assertEquals("element", element.tagName());
        assertEquals("value", element.attribute("attr"));
        assertEquals("Content", element.text());
    }

    // Add test cases for each method, following the same pattern as above.

    @org.junit.Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader(""), "baseUri");
        Element root = doc.createElement("root");
        Element element1 = doc.createElement("element1");
        Element element2 = doc.createElement("element2");
        root.appendChild(element1);
        root.appendChild(element2);
        stack.push(root);

        xmlTreeBuilder.popStackToClose(new Token.EndTag(new Tag("element1")));

        assertEquals(1, stack.size());
        assertEquals(element2, stack.peek());
    }

    @org.junit.Test(expected = org.jsoup.parser.ParseException.class)
    public void testInvalidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("InvalidInput"), "baseUri");
        fail("Expected ParseException but no exception was thrown.");
    }

    // Test edge cases and error scenarios.

    @org.junit.Test
    public void testParseFragmentWithEmptyInput() {
        String emptyInput = "";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(emptyInput, "baseUri", ParseErrorList.getInstance(), ParseSettings.preserveCase);
        assertEquals(0, nodes.size());
    }

    // Complete the test file with test cases for other methods and edge cases.

    
}