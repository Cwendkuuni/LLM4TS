/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jxpath.ri.model.dom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;

    @Before
    public void setUp() {
        parent = new DOMNodePointer(null);
        name = new QName("attribute");
    }

    @Test
    public void testConstructor() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        assertNotNull(attributeIterator);
    }

    @Test
    public void testGetNodePointer() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertNull(nodePointer);

        Element element = new DOMNodePointer(new Element() {
            @Override
            public String getAttribute(String name) {
                return null;
            }

            @Override
            public void setAttribute(String name, String value) {}

            @Override
            public void removeAttribute(String name) {}

            @Override
            public Attr getAttributeNode(String name) {
                return null;
            }

            @Override
            public Attr setAttributeNode(Attr newAttr) {
                return null;
            }

            @Override
            public Attr removeAttributeNode(Attr oldAttr) {
                return null;
            }

            @Override
            public NodeList getElementsByTagName(String name) {
                return null;
            }

            @Override
            public String getAttributeNS(String namespaceURI, String localName) {
                return null;
            }

            @Override
            public void setAttributeNS(String namespaceURI, String qualifiedName, String value) {}

            @Override
            public void removeAttributeNS(String namespaceURI, String localName) {}

            @Override
            public Attr getAttributeNodeNS(String namespaceURI, String localName) {
                return null;
            }

            @Override
            public Attr setAttributeNodeNS(Attr newAttr) {
                return null;
            }

            @Override
            public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
                return null;
            }

            @Override
            public boolean hasAttribute(String name) {
                return false;
            }

            @Override
            public boolean hasAttributeNS(String namespaceURI, String localName) {
                return false;
            }

            @Override
            public TypeInfo getSchemaTypeInfo() {
                return null;
            }

            @Override
            public void setIdAttribute(String name, boolean isId) {}

            @Override
            public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) {}

            @Override
            public void setIdAttributeNode(Attr idAttr, boolean isId) {}

            @Override
            public String getNodeName() {
                return null;
            }

            @Override
            public String getNodeValue() {
                return null;
            }

            @Override
            public void setNodeValue(String nodeValue) {}

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
            public void normalize() {}

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
            public void setPrefix(String prefix) throws DOMException {}

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

            @Override
            public short compareDocumentPosition(Node other) throws DOMException {
                return 0;
            }

            @Override
            public String getTextContent() throws DOMException {
                return null;
            }

            @Override
            public void setTextContent(String textContent) throws DOMException {}

            @Override
            public boolean isSameNode(Node other) {
                return false;
            }

            @Override
            public String lookupPrefix(String namespaceURI) {
                return null;
            }

            @Override
            public boolean isDefaultNamespace(String namespaceURI) {
                return false;
            }

            @Override
            public String lookupNamespaceURI(String prefix) {
                return null;
            }

            @Override
            public boolean isEqualNode(Node arg) {
                return false;
            }

            @Override
            public Object getFeature(String feature, String version) {
                return null;
            }

            @Override
            public Object setUserData(String key, Object data, UserDataHandler handler) {
                return null;
            }

            @Override
            public Object getUserData(String key) {
                return null;
            }
        });

        attributeIterator = new DOMAttributeIterator(parent, name);
        nodePointer = attributeIterator.getNodePointer();
        assertNotNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        int position = attributeIterator.getPosition();
        assertEquals(0, position);

        attributeIterator.setPosition(1);
        position = attributeIterator.getPosition();
        assertEquals(1, position);
    }

    @Test
    public void testSetPosition() {
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(parent, name);
        boolean result = attributeIterator.setPosition(0);
        assertTrue(result);

        result = attributeIterator.setPosition(1);
        assertTrue(result);

        result = attributeIterator.setPosition(2);
        assertTrue(!result);
    }
}