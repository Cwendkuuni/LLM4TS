package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.regex.Pattern;

import org.junit.Test;

public class ProcessCommonJSModulesTest {
    private ProcessCommonJSModules processCommonJSModules;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        processCommonJSModules = new ProcessCommonJSModules(compiler, ".");
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();

        processCommonJSModules.process(externs, root);

        // assert something
    }

    @Test
    public void testGuessCJSModuleName() {
        String filename = "test.js";
        String expected = "module$test";
        String actual = processCommonJSModules.guessCJSModuleName(filename);
        assertEquals(expected, actual);
    }

    @Test
    public void testToModuleName() {
        String filename = "test.js";
        String expected = "module$test";
        String actual = ProcessCommonJSModules.toModuleName(filename);
        assertEquals(expected, actual);
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        String requiredFilename = "sub/test.js";
        String currentFilename = "main/test.js";
        String expected = "module$sub$test";
        String actual = ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
        assertEquals(expected, actual);
    }

    @Test
    public void testNormalizeSourceName() {
        String filename = "./test.js";
        String expected = "test.js";
        String actual = processCommonJSModules.normalizeSourceName(filename);
        assertEquals(expected, actual);

        filename = "test.js";
        expected = "test.js";
        actual = processCommonJSModules.normalizeSourceName(filename);
        assertEquals(expected, actual);

        filename = "sub/test.js";
        expected = "sub/test.js";
        actual = processCommonJSModules.normalizeSourceName(filename);
        assertEquals(expected, actual);
    }

    @Test
    public void testProcessCommonJsModulesCallbackVisitRequireCall() {
        NodeTraversal t = new NodeTraversal(compiler, new MockScopeCreator());
        Node require = new Node(Token.CALL);
        Node moduleName = new Node(Token.STRING, "foo");
        moduleName.putProp(Node.REGEXP_PROP, false);
        moduleName.setString("foo");
        require.addChildToBack(new Node(Token.GETPROP, new Node(Token.NAME, "require"), moduleName));
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(require);

        processCommonJSModules.new ProcessCommonJsModulesCallback().visitRequireCall(t, require, parent);

        // assert something
    }

    @Test
    public void testProcessCommonJsModulesCallbackVisitScript() {
        NodeTraversal t = new NodeTraversal(compiler, new MockScopeCreator());
        Node script = new Node(Token.SCRIPT);
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(script);

        processCommonJSModules.new ProcessCommonJsModulesCallback().visitScript(t, script);

        // assert something
    }

    @Test
    public void testProcessCommonJsModulesCallbackVisitModuleExports() {
        Node prop = new Node(Token.GETPROP);
        Node module = new Node(Token.NAME, "module");
        Node exports = new Node(Token.NAME, "exports");
        module.putProp(Node.ORIGINALNAME_PROP, "module");
        module.setString("module");
        exports.putProp(Node.ORIGINALNAME_PROP, "exports");
        exports.setString("module$exports");
        prop.addChildToBack(module);
        prop.addChildToBack(exports);

        processCommonJSModules.new ProcessCommonJsModulesCallback().visitModuleExports(prop);

        // assert something
    }

    @Test
    public void testSuffixVarsCallbackVisit() {
        NodeTraversal t = new NodeTraversal(compiler, new MockScopeCreator());
        Node name = new Node(Token.NAME, "test");
        Node parent = new Node(Token.BLOCK);
        parent.addChildToFront(name);

        processCommonJSModules.new SuffixVarsCallback("module").visit(t, name, parent);

        // assert something
    }
}

class MockScopeCreator extends EmptyScopeCreator {
    public MockScopeCreator() {
        super();
    }

    @Override
    protected Scope createScope(Node node, Scope parent) {
        return new MockScope(parent);
    }

    class MockScope extends Scope {
        public MockScope(Scope parent) {
            super(parent);
        }

        @Override
        public Var getVar(String name) {
            return new Var(this, name, null, true);
        }
    }
}