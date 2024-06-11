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

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;

public class NamespaceResolverTest {
    @Test
    public void testRegisterNamespace() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        nsResolver.registerNamespace("fooPrefix", "fooNamespace");
        assertEquals("fooNamespace", nsResolver.getNamespaceURI("fooPrefix"));
        nsResolver.registerNamespace("barPrefix", "barNamespace");
        assertEquals("barNamespace", nsResolver.getNamespaceURI("barPrefix"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        nsResolver.seal();
        nsResolver.registerNamespace("fooPrefix", "fooNamespace");
    }

    // TODO: Add test cases for setNamespaceContextPointer() and getNamespaceContextPointer()

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        nsResolver.registerNamespace("fooPrefix", "fooNamespace");
        nsResolver.registerNamespace("barPrefix", "barNamespace");
        nsResolver.registerNamespace("bazPrefix", "bazNamespace");
        assertEquals("fooNamespace", nsResolver.getNamespaceURI("fooPrefix"));
        assertEquals("barNamespace", nsResolver.getNamespaceURI("barPrefix"));
        assertEquals("bazNamespace", nsResolver.getNamespaceURI("bazPrefix"));
    }

    // TODO: Add test cases for getNamespaceURI with context node

    @Test
    public void testGetNamespaceURIWithoutNamespacePrefix() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        assertNull(nsResolver.getNamespaceURI("fooPrefix")); // No namespace registered
        nsResolver.registerNamespace("", "defaultNamespace");
        assertEquals("defaultNamespace", nsResolver.getNamespaceURI("")); // Default namespace registered
        nsResolver.registerNamespace("barPrefix", "barNamespace");
        assertEquals("defaultNamespace", nsResolver.getNamespaceURI("")); // Default namespace still returns correct value
        assertEquals("barNamespace", nsResolver.getNamespaceURI("barPrefix")); // Non-default namespace returns correct value
    }

    // TODO: Add test cases for getNamespaceURI with parent resolver

    @Test
    public void testGetPrefix() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        nsResolver.registerNamespace("fooPrefix", "fooNamespace");
        nsResolver.registerNamespace("barPrefix", "barNamespace");
        nsResolver.registerNamespace("bazPrefix", "bazNamespace");
        assertEquals("fooPrefix", nsResolver.getPrefix("fooNamespace"));
        assertEquals("barPrefix", nsResolver.getPrefix("barNamespace"));
        assertEquals("bazPrefix", nsResolver.getPrefix("bazNamespace"));
    }

    // TODO: Add test cases for getPrefix with parent resolver

    @Test
    public void testGetPrefixWithoutNamespaceURI() {
        NamespaceResolver nsResolver = new NamespaceResolver();
        assertNull(nsResolver.getPrefix("fooNamespace")); // Namespace URI not registered
        nsResolver.registerNamespace("", "defaultNamespace");
        assertEquals("", nsResolver.getPrefix("defaultNamespace")); // Default namespace registered
    }

    // TODO: Add edge cases and additional test cases for registerNamespace, setNamespaceContextPointer, getNamespaceURI, and getPrefix

    @Test
    public void testClone() {
        NamespaceResolver nsResolver1 = new NamespaceResolver();
        nsResolver1.registerNamespace("fooPrefix", "fooNamespace");
        nsResolver1.registerNamespace("barPrefix", "barNamespace");

        NamespaceResolver nsResolver2 = (NamespaceResolver) nsResolver1.clone();

        assertEquals("fooNamespace", nsResolver2.getNamespaceURI("fooPrefix"));
        assertEquals("barNamespace", nsResolver2.getNamespaceURI("barPrefix"));
    }
}