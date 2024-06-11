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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator attributeIterator;

    @Before
    public void setup() {
        NodePointer parent = new DOMNodePointer();
        QName name = new QName("test", "attribute");
        attributeIterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();

        assertTrue(nodePointer instanceof DOMAttributePointer);
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        Attr attribute = attributePointer.getAttribute();
        assertNotNull(attribute);
        assertEquals("test", attribute.getName());
        assertNull(attribute.getValue());
    }

    @Test
    public void testGetPosition() {
        int position = attributeIterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = attributeIterator.setPosition(1);
        assertTrue(result);

        int position = attributeIterator.getPosition();
        assertEquals(1, position);
    }

    @Test
    public void testSetPosition_invalid() {
        boolean result = attributeIterator.setPosition(2);
        assertFalse(result);

        int position = attributeIterator.getPosition();
        assertEquals(0, position);
    }
}