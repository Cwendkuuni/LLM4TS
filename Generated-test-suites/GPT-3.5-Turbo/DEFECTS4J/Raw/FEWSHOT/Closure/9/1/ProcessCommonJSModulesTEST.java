/*
 * Copyright 2011 The Closure Compiler Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        processCommonJSModules = new ProcessCommonJSModules(null, "");
    }

    @Test
    public void testGuessCJSModuleName() {
        String moduleName = processCommonJSModules.guessCJSModuleName("script.js");
        assertEquals("module$script", moduleName);
    }

    @Test
    public void testToModuleName() {
        String moduleName = ProcessCommonJSModules.toModuleName("script.js");
        assertEquals("module$script", moduleName);
    }

    @Test
    public void testToModuleNameWithRelativeAddressing() {
        String moduleName = ProcessCommonJSModules.toModuleName("../src/script.js","/src/main.js");
        assertEquals("module$src$script", moduleName);
    }

    @Test
    public void testProcessRootNode() {
        NodeTraversal traversal = null;
        Node externs = null;
        Node root = null;
        processCommonJSModules.process(externs, root);
        // Test the logic of processRootNode here
        // ...
    }

    @Test
    public void testProcessRequireCall() {
        NodeTraversal traversal = null;
        Node require = null;
        Node parent = null;
        processCommonJSModules.processRequireCall(traversal, require, parent);
        // Test the logic of processRequireCall here
        // ...
    }

    @Test
    public void testProcessScriptNode() {
        NodeTraversal traversal = null;
        Node script = null;
        processCommonJSModules.processScriptNode(traversal, script);
        // Test the logic of processScriptNode here
        // ...
    }

    @Test
    public void testProcessModuleExports() {
        Node moduleExports = null;
        processCommonJSModules.processModuleExports(moduleExports);
        // Test the logic of processModuleExports here
        // ...
    }

    @Test
    public void testSuffixVarsCallback() {
        String suffix = "";
        SuffixVarsCallback callback = new SuffixVarsCallback(suffix);
        NodeTraversal traversal = null;
        Node n = null;
        Node parent = null;
        callback.visit(traversal, n, parent);
        // Test the logic of SuffixVarsCallback here
        // ...
    }

}