import org.junit.Test;

import static org.junit.Assert.*;

public class ProcessCommonJSModulesTest {

    @Test
    public void testGuessCJSModuleName() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        String filename = "test.js";
        String expectedModuleName = "module$test";
        String actualModuleName = pcm.guessCJSModuleName(filename);
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleNameWithLeadingDotSlash() {
        String filename = "./test";
        String currentFilename = "src/module.js";
        String expectedModuleName = "module$test";
        String actualModuleName = ProcessCommonJSModules.toModuleName(filename, currentFilename);
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleNameWithLeadingDoubleDotSlash() {
        String filename = "../test";
        String currentFilename = "src/folder/module.js";
        String expectedModuleName = "module$folder$test";
        String actualModuleName = ProcessCommonJSModules.toModuleName(filename, currentFilename);
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testToModuleNameWithNoRelativeAddressing() {
        String filename = "test.js";
        String currentFilename = "src/module.js";
        String expectedModuleName = "module$test";
        String actualModuleName = ProcessCommonJSModules.toModuleName(filename, currentFilename);
        assertEquals(expectedModuleName, actualModuleName);
    }

    @Test
    public void testNormalizeSourceNameWithPrefix() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "src");
        String filename = "src/test.js";
        String expectedNormalizedName = "test.js";
        String actualNormalizedName = pcm.normalizeSourceName(filename);
        assertEquals(expectedNormalizedName, actualNormalizedName);
    }

    @Test
    public void testNormalizeSourceNameWithoutPrefix() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, "src");
        String filename = "test.js";
        String expectedNormalizedName = "test.js";
        String actualNormalizedName = pcm.normalizeSourceName(filename);
        assertEquals(expectedNormalizedName, actualNormalizedName);
    }

    @Test
    public void testVisitRequireCall() {
        // TODO: Implement test case
    }

    @Test
    public void testVisitScript() {
        // TODO: Implement test case
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        Node script = new Node(Token.SCRIPT);
        String moduleName = "module";
        pcm.emitOptionalModuleExportsOverride(script, moduleName);
        Node ifNode = script.getLastChild();
        assertNotNull(ifNode);
        assertEquals(Token.IF, ifNode.getType());
        Node moduleExportsProp = ifNode.getFirstChild();
        assertNotNull(moduleExportsProp);
        assertEquals(Token.GETPROP, moduleExportsProp.getType());
        Node moduleNameNode = moduleExportsProp.getFirstChild();
        assertNotNull(moduleNameNode);
        assertEquals(Token.NAME, moduleNameNode.getType());
        assertEquals(moduleName, moduleNameNode.getString());
        Node moduleExportsNode = moduleExportsProp.getLastChild();
        assertNotNull(moduleExportsNode);
        assertEquals(Token.STRING, moduleExportsNode.getType());
        assertEquals("module$exports", moduleExportsNode.getString());
        Node block = ifNode.getLastChild();
        assertNotNull(block);
        assertEquals(Token.BLOCK, block.getType());
        Node exprResult = block.getFirstChild();
        assertNotNull(exprResult);
        assertEquals(Token.EXPR_RESULT, exprResult.getType());
        Node assignNode = exprResult.getFirstChild();
        assertNotNull(assignNode);
        assertEquals(Token.ASSIGN, assignNode.getType());
        Node moduleNameAssignNode = assignNode.getFirstChild();
        assertNotNull(moduleNameAssignNode);
        assertEquals(Token.NAME, moduleNameAssignNode.getType());
        assertEquals(moduleName, moduleNameAssignNode.getString());
        Node moduleExportsAssignNode = assignNode.getLastChild();
        assertNotNull(moduleExportsAssignNode);
        assertEquals(Token.GETPROP, moduleExportsAssignNode.getType());
        moduleExportsNode = moduleExportsAssignNode.getLastChild();
        assertNotNull(moduleExportsNode);
        assertEquals(Token.STRING, moduleExportsNode.getType());
        assertEquals("module$exports", moduleExportsNode.getString());
    }

    @Test
    public void testVisitModuleExports() {
        ProcessCommonJSModules pcm = new ProcessCommonJSModules(null, null);
        Node prop = new Node(Token.GETPROP);
        Node moduleNode = new Node(Token.NAME, "module");
        Node exportsNode = new Node(Token.NAME, "exports");
        prop.addChildToBack(moduleNode);
        prop.addChildToBack(exportsNode);
        pcm.visitModuleExports(prop);
        assertEquals("module$exports", exportsNode.getString());
        assertEquals("module$module", moduleNode.getString());
        assertEquals("module", moduleNode.getProp(Node.ORIGINALNAME_PROP));
        assertEquals("exports", exportsNode.getProp(Node.ORIGINALNAME_PROP));
    }

    @Test
    public void testSuffixVarsCallback() {
        // TODO: Implement test case
    }
}