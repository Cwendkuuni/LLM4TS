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
package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private static final QName TEST_QNAME = new QName("test", "http://www.example.com");
    private JDOMAttributeIterator iterator;
    private NodePointer parent;
    private Element element;
    private Attribute attr1;
    private Attribute attr2;
    private Attribute attr3;
    
    @Before
    public void setup() {
        parent = new JDOMNodePointer();
        element = new Element("testElement");
        attr1 = new Attribute("attr1", "value1");
        attr2 = new Attribute("attr2", "value2", Namespace.getNamespace("test", "http://www.example.com"));
        attr3 = new Attribute("attr3", "value3", Namespace.getNamespace("test", "http://www.example.com"));
        element.setAttribute(attr1);
        element.setAttribute(attr2);
        element.setAttribute(attr3);
        parent.setValue(element);
    }
    
    @Test
    public void testConstructor_WithPrefix() {
        iterator = new JDOMAttributeIterator(parent, TEST_QNAME);
        
        assertTrue(iterator.setPosition(1));
        assertEquals(attr2, ((JDOMAttributePointer)iterator.getNodePointer()).getAttribute());
        
        assertTrue(iterator.setPosition(2));
        assertEquals(attr3, ((JDOMAttributePointer)iterator.getNodePointer()).getAttribute());
        
        assertFalse(iterator.setPosition(3));
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testConstructor_WithoutPrefix() {
        iterator = new JDOMAttributeIterator(parent, new QName(null, "*"));
        
        assertTrue(iterator.setPosition(1));
        assertEquals(attr1, ((JDOMAttributePointer)iterator.getNodePointer()).getAttribute());
        
        assertTrue(iterator.setPosition(2));
        assertEquals(attr2, ((JDOMAttributePointer)iterator.getNodePointer()).getAttribute());
        
        assertTrue(iterator.setPosition(3));
        assertEquals(attr3, ((JDOMAttributePointer)iterator.getNodePointer()).getAttribute());
        
        assertFalse(iterator.setPosition(4));
        assertNull(iterator.getNodePointer());
    }
    
    @Test
    public void testConstructor_NoAttributes() {
        element.removeAttribute(attr1.getName());
        element.removeAttribute(attr2.getName());
        element.removeAttribute(attr3.getName());
        
        iterator = new JDOMAttributeIterator(parent, TEST_QNAME);
        
        assertFalse(iterator.setPosition(1));
        assertNull(iterator.getNodePointer());
    }
}