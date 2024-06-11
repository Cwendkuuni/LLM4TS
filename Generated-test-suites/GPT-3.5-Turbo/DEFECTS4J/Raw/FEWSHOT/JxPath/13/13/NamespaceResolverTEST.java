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
package org.apache.commons.jxpath.ri;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class NamespaceResolverTest {
    
    private NamespaceResolver resolver;
    
    @Before
    public void setUp() {
        resolver = new NamespaceResolver();
    }

    @Test
    public void testRegisterNamespace() {
        assertEquals(null, resolver.getNamespaceURI("prefix"));
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        resolver.seal();
        resolver.registerNamespace("prefix", "namespaceURI");
    }
    
    @Test
    public void testSetNamespaceContextPointer() {
        assertNull(resolver.getNamespaceContextPointer());
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, resolver.getNamespaceContextPointer());
    }
    
    @Test
    public void testGetNamespaceURI() {
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetNamespaceURIPointer() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("namespaceURI");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetNamespaceURIWithParent() {
        resolver = new NamespaceResolver(new NamespaceResolver());
        resolver.getParent().registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
    }
    
    @Test
    public void testGetPrefix() {
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("prefix", resolver.getPrefix("namespaceURI"));
    }
    
    @Test
    public void testGetPrefixPointer() {
        NodePointer pointer = new NodePointer();
        pointer.setNamespaceURI("namespaceURI");
        pointer.setName(new QName("prefix"));
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("prefix", resolver.getPrefix("namespaceURI"));
    }
    
    @Test
    public void testGetPrefixWithParent() {
        resolver = new NamespaceResolver(new NamespaceResolver());
        resolver.getParent().registerNamespace("prefix", "namespaceURI");
        assertEquals("prefix", resolver.getPrefix("namespaceURI"));
    }
}