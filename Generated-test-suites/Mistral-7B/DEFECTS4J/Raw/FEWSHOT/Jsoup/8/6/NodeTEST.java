package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Nodes;

public class NodeTest {

    @Test
    public void testAbstractNodeMethods() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        attributes.put("class", "test");
        Node node = new Element("div") {
            {
                this.baseUri = baseUri;
                this.attributes = attributes;
            }
        };

        assertEquals("div", node.nodeName());
        assertEquals("test", node.attr("class"));
        assertEquals("", node.attr("nonExistentAttribute"));
        assertTrue(node.hasAttr("class"));
        assertTrue(node.hasAttr("nonExistentAttribute"));
        assertFalse(node.hasAttr("nonexistentElement"));
        assertEquals("", node.attributes().toString());
        assertEquals("test", node.attributes().get("class"));
        node.attr("class", "newClass");
        assertEquals("newClass", node.attributes().get("class"));
        assertFalse(node.hasAttr("nonexistentElement"));
        node.removeAttr("class");
        assertEquals("", node.attributes().get("class"));

        assertEquals(baseUri, node.baseUri());
        node.setBaseUri("http://test.com/");
        assertEquals("http://test.com/", node.baseUri());

        String relUrl = "child.html";
        String absUrl = node.absUrl(relUrl);
        assertEquals("", absUrl);
        node.attributes().put("href", relUrl);
        assertEquals("http://test.com/child.html", absUrl);

        Node child = new TextNode("Child");
        node.appendChild(child);
        Node child2 = new Element("p");
        node.appendChild(child2);

        List<Node> children = node.childNodes();
        assertEquals(2, children.size());
        assertEquals(child, children.get(0));
        assertEquals(child2, children.get(1));

        Node childNode = node.childNode(0);
        assertEquals(child, childNode);

        Node parent = new Element("body");
        node.setParentNode(parent);
        assertEquals(parent, node.parent());
        assertEquals(parent, child.parent());
        assertEquals(null, child2.parent());

        Document doc = new Document();
        doc.appendChild(node);
        assertEquals(doc, node.ownerDocument());

        node.remove();
        assertEquals(null, node.ownerDocument());
    }

    @Test
    public void testNodeManipulation() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node node = new Element("div", baseUri, attributes) {
            {
                this.attributes = attributes;
            }
        };

        assertEquals(2, node.childNodes().size());
        assertNull(node.childNodes().get(1));

        Node child = new Element("p");
        node.appendChild(child);
        assertEquals(3, node.childNodes().size());
        assertEquals(child, node.childNodes().get(2));

        Node sibling = new TextNode("Sibling");
        node.parentNode().insertBefore(sibling, node);
        assertEquals(4, node.childNodes().size());
        assertEquals(sibling, node.childNodes().get(0));

        Node replace = new Element("span");
        node.replaceChild(node.childNodes().get(1), replace);
        assertEquals(3, node.childNodes().size());
        assertNull(node.childNodes().get(1));
        assertEquals(replace, node.childNodes().get(1));

        Node remove = node.childNodes().get(0);
        node.removeChild(remove);
        assertEquals(2, node.childNodes().size());
        assertNull(node.childNodes().get(0));

        Node newChild = new Element("a", baseUri, attributes);
        node.addChildren(newChild);
        assertEquals(3, node.childNodes().size());
        assertEquals(newChild, node.childNodes().get(2));

        Node newFirstChild = new Element("ul");
        node.addChildren(0, newFirstChild);
        assertEquals(4, node.childNodes().size());
        assertEquals(newFirstChild, node.childNodes().get(0));

        Nodes nodes = node.siblingNodes();
        assertEquals(0, nodes.size());

        Node nextSibling = new Element("span");
        node.parentNode().insertBefore(nextSibling, node.nextSibling());
        nodes = node.siblingNodes();
        assertEquals(1, nodes.size());
        assertEquals(nextSibling, nodes.get(0));

        Node previousSibling = new Element("span");
        node.parentNode().insertBefore(previousSibling, node.previousSibling());
        nodes = node.siblingNodes();
        assertEquals(2, nodes.size());
        assertEquals(previousSibling, nodes.get(0));
        assertEquals(nextSibling, nodes.get(1));
    }

    @Test
    public void testNodeTraversal() {
        String baseUri = "http://example.com/";
        Attributes attributes = new Attributes();
        Node body = new Element("body", baseUri, attributes);

        Node div = new Element("div", baseUri, attributes);
        body.appendChild(div);

        Node p1 = new Element("p", baseUri, attributes);
        Node p2 = new Element("p", baseUri, attributes);
        div.appendChild(p1);
        div.appendChild(p2);

        Node text1 = new TextNode("Text 1");
        Node text2 = new TextNode("Text 2");
        p1.appendChild(text1);
        p2.appendChild(text2);

        Node span1 = new Element("span", baseUri, attributes);
        Node span2 = new Element("span", baseUri, attributes);
        p1.appendChild(span1);
        p2.appendChild(span2);

        Node head = body.getElementsByTag("head").get(0);
        Node title = new Element("title", baseUri, attributes);
        head.appendChild(title);

        Node link = new Element("link", baseUri, attributes);
        link.attr("rel", "stylesheet");
        link.attr("href", "/styles.css");
        head.appendChild(link);

        Node script = new Element("script", baseUri, attributes);
        script.attr("src", "/script.js");
        head.appendChild(script);

        Node input = new Element("input", baseUri, attributes);
        input.attr("type", "text");
        input.attr("name", "username");
        new Element("label", baseUri, attributes).appendChild(input).appendChild(new TextNode("Username:"));
        body.appendChild(input);

        Node button = new Element("button", baseUri, attributes);
        button.attr("type", "button");
        button.appendChild(new TextNode("Submit"));
        body.appendChild(button);

        Node form = new Element("form", baseUri, attributes);
        form.appendChild(input);
        form.appendChild(button);
        body.appendChild(form);

        Node table = new Element("table", baseUri, attributes);
        Node thead = new Element("thead", baseUri, attributes);
        Node tbody = new Element("tbody", baseUri, attributes);
        table.appendChild(thead);
        table.appendChild(tbody);
        body.appendChild(table);

        Node tr1 = new Element("tr", baseUri, attributes);
        Node td1 = new Element("td", baseUri, attributes);
        tr1.appendChild(td1);
        tbody.appendChild(tr1);

        Node tr2 = new Element("tr", baseUri, attributes);
        Node td2 = new Element("td", baseUri, attributes);
        td2.appendChild(new TextNode("Data 1"));
        td1.appendChild(td2);
        tbody.appendChild(tr2);

        Node tr3 = new Element("tr", baseUri, attributes);
        Node td3 = new Element("td", baseUri, attributes);
        td3.appendChild(new TextNode("Data 2"));
        tr2.appendChild(td3);

        Node h1 = new Element("h1", baseUri, attributes);
        h1.appendChild(new TextNode("Title"));
        body.insertBefore(h1, body.getFirstChild());

        Node headTitle = head.selectFirst("title");
        assertEquals("Title", headTitle.text());

        Node inputUsername = body.selectFirst("input[name=username]");
        assertEquals("Username:", inputUsername.previousElementSibling().text());

        Node buttonSubmit = body.selectFirst("button:contains(Submit)");
        assertEquals("Submit", buttonSubmit.text());

        Node trData1 = table.selectFirst("tr td:contains(Data 1)");
        assertEquals("Data 1", trData1.previousElementSibling().previousElementSibling().text());
    }
}