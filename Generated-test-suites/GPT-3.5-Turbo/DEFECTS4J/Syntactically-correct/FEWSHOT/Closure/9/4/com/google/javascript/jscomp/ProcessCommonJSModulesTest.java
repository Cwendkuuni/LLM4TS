package com.google.javascript.jscomp;

import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProcessCommonJSModulesTest {
    
    private ProcessCommonJSModules processCommonJSModules;
    
    @Before
    public void setUp() {
        processCommonJSModules = new ProcessCommonJSModules(new AbstractCompiler(), ProcessCommonJSModules.DEFAULT_FILENAME_PREFIX);
    }
    
    @Test
    public void testGuessCJSModuleName() {
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("filename"));
        assertEquals("module$sub$filename", processCommonJSModules.guessCJSModuleName("sub/filename"));
        assertEquals("module$filename$js", processCommonJSModules.guessCJSModuleName("filename.js"));
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName(".//filename"));
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename"));
        assertEquals("module$filename$js", ProcessCommonJSModules.toModuleName("filename.js"));
        assertEquals("module$sub$filename", ProcessCommonJSModules.toModuleName("sub/filename"));
        assertEquals("module$filename$js", ProcessCommonJSModules.toModuleName("./filename.js"));
        assertEquals("module$sub$filename", ProcessCommonJSModules.toModuleName("../sub/filename"));
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        assertEquals("module$filename", ProcessCommonJSModules.toModuleName("filename", "filename"));
        assertEquals("module$filename$js", ProcessCommonJSModules.toModuleName("filename.js", "filename.js"));
        assertEquals("module$sub$filename", ProcessCommonJSModules.toModuleName("sub/filename", "filename"));
        assertEquals("module$sub$sub2$filename", ProcessCommonJSModules.toModuleName("sub/sub2/filename", "sub/sub3/filename"));
        assertEquals("module$subsub2$filename", ProcessCommonJSModules.toModuleName("../sub/sub2/filename", "sub/sub3/filename"));
        assertEquals("module$sub$sub3$filename", ProcessCommonJSModules.toModuleName("../../sub/sub3/filename", "sub/sub3/filename"));
    }

    @Test
    public void testProcess() {
        Node externs = IR.empty();
        Node root = IR.empty();
        processCommonJSModules.process(externs, root);
    }

    @Test
    public void testVisitRequireCall() {
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), null);
        Node parent = IR.empty();
        parent.addChildToFront(IR.name("require").srcref(new Node(1)));
        parent.addChildToFront(IR.string("module").srcref(new Node(2)));
        processCommonJSModules.visitRequireCall(t, parent.getChildAtIndex(0), parent);
    }

    @Test
    public void testVisitScript() {
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), null);
        Node script = IR.empty();
        processCommonJSModules.visitScript(t, script);
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        Node script = IR.empty();
        String moduleName = "module";
        processCommonJSModules.emitOptionalModuleExportsOverride(script, moduleName);
    }

    @Test
    public void testVisitModuleExports() {
        Node prop = IR.empty();
        processCommonJSModules.visitModuleExports(prop);
    }

    @Test
    public void testSuffixVarsCallback() {
        NodeTraversal t = new NodeTraversal(new AbstractCompiler(), null);
        Node name = IR.name("name");
        Node parent = IR.empty();
        parent.addChildToFront(name);
        processCommonJSModules.new SuffixVarsCallback("suffix").visit(t, name, parent);
    }
}