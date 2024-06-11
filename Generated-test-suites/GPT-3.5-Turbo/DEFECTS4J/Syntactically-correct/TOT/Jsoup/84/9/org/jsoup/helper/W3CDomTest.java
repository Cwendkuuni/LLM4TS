import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.*;
import org.junit.Test;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Typical Use Case
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assert w3cDoc != null;

        // Edge Case
        org.jsoup.nodes.Document nullJsoupDoc = null;
        Document nullW3cDoc = w3cDom.fromJsoup(nullJsoupDoc);
        assert nullW3cDoc == null;
    }

    @Test
    public void testConvert() {
        // Typical Use Case
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        Document out = w3cDom.fromJsoup(jsoupDoc);
        org.jsoup.nodes.Element rootEl = new org.jsoup.nodes.Element("root", "");
        jsoupDoc.appendChild(rootEl);
        w3cDom.convert(jsoupDoc, out);

        // Edge Case
        org.jsoup.nodes.Element emptyEl = new org.jsoup.nodes.Element("", "");
        jsoupDoc.appendChild(emptyEl);
        w3cDom.convert(jsoupDoc, out);
    }

    @Test
    public void testW3CBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document out = builder.newDocument();
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.jsoup.nodes.Element rootEl = new org.jsoup.nodes.Element("root", "");
        jsoupDoc.appendChild(rootEl);
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(out);
        w3cBuilder.head(rootEl, 0);
        w3cBuilder.tail(rootEl, 0);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("tag", "");
        Element el = new Element() {
            @Override
            public String getTagName() {
                return null;
            }

            @Override
            public String getAttribute(String name) {
                return null;
            }

            @Override
            public void setAttribute(String name, String value) {

            }

            @Override
            public void removeAttribute(String name) {

            }

            @Override
            public Attr getAttributeNode(String name) {
                return null;
            }

            @Override
            public Attr setAttributeNode(Attr newAttr) throws DOMException {
                return null;
            }

            @Override
            public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
                return null;
            }

            @Override
            public NodeList getElementsByTagName(String name) {
                return null;
            }

            @Override
            public String getAttributeNS(String namespaceURI, String localName) throws DOMException {
                return null;
            }

            @Override
            public void setAttributeNS(String namespaceURI, String qualifiedName, String value) throws DOMException {

            }

            @Override
            public void removeAttributeNS(String namespaceURI, String localName) throws DOMException {

            }

            @Override
            public Attr getAttributeNodeNS(String namespaceURI, String localName) throws DOMException {
                return null;
            }

            @Override
            public Attr setAttributeNodeNS(Attr newAttr) throws DOMException {
                return null;
            }

            @Override
            public NodeList getElementsByTagNameNS(String namespaceURI, String localName) throws DOMException {
                return null;
            }

            @Override
            public boolean hasAttribute(String name) {
                return false;
            }

            @Override
            public boolean hasAttributeNS(String namespaceURI, String localName) throws DOMException {
                return false;
            }

            @Override
            public TypeInfo getSchemaTypeInfo() {
                return null;
            }

            @Override
            public void setIdAttribute(String name, boolean isId) throws DOMException {

            }

            @Override
            public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {

            }

            @Override
            public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {

            }

            @Override
            public String getNodeName() {
                return null;
            }

            @Override
            public String getNodeValue() throws DOMException {
                return null;
            }

            @Override
            public void setNodeValue(String nodeValue) throws DOMException {

            }

            @Override
            public short getNodeType() {
                return 0;
            }

            @Override
            public Node getParentNode() {
                return null;
            }

            @Override
            public NodeList getChildNodes() {
                return null;
            }

            @Override
            public Node getFirstChild() {
                return null;
            }

            @Override
            public Node getLastChild() {
                return null;
            }

            @Override
            public Node getPreviousSibling() {
                return null;
            }

            @Override
            public Node getNextSibling() {
                return null;
            }

            @Override
            public NamedNodeMap getAttributes() {
                return null;
            }

            @Override
            public Document getOwnerDocument() {
                return null;
            }

            @Override
            public Node insertBefore(Node newChild, Node refChild) throws DOMException {
                return null;
            }

            @Override
            public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
                return null;
            }

            @Override
            public Node removeChild(Node oldChild) throws DOMException {
                return null;
            }

            @Override
            public Node appendChild(Node newChild) throws DOMException {
                return null;
            }

            @Override
            public boolean hasChildNodes() {
                return false;
            }

            @Override
            public Node cloneNode(boolean deep) {
                return null;
            }

            @Override
            public void normalize() {

            }

            @Override
            public boolean isSupported(String feature, String version) {
                return false;
            }

            @Override
            public String getNamespaceURI() {
                return null;
            }

            @Override
            public String getPrefix() {
                return null;
            }

            @Override
            public void setPrefix(String prefix) throws DOMException {

            }

            @Override
            public String getLocalName() {
                return null;
            }

            @Override
            public boolean hasAttributes() {
                return false;
            }

            @Override
            public String getBaseURI() {
                return null;
            }
        };
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(source, el);
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("tag", "");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        w3cBuilder.updateNamespaces(el);
        org.jsoup.nodes.Element elWithoutPrefix = new org.jsoup.nodes.Element("", "");
        w3cBuilder.updateNamespaces(elWithoutPrefix);
    }

    @Test
    public void testAsString() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        W3CDom w3cDom = new W3CDom();
        String str = w3cDom.asString(doc);
        assert str != null;
    }
}