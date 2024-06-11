import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    private ProcessCommonJSModules processCommonJSModules;

    @Before
    public void setUp() {
        processCommonJSModules = new ProcessCommonJSModules(null, "");
    }

    @Test
    public void testToModuleName() {
        assertEquals("module$filename", processCommonJSModules.toModuleName("filename"));
        assertEquals("module$filename$hello$world", processCommonJSModules.toModuleName("filename/hello/world"));
        assertEquals("module$directory$filename", processCommonJSModules.toModuleName("./directory/filename"));
        assertEquals("module$filename", processCommonJSModules.toModuleName("././././filename"));
        assertEquals("module$parent$directory$filename", processCommonJSModules.toModuleName("../parent/directory/filename"));
    }

    @Test
    public void testToModuleNameWithCurrentFilename() {
        assertEquals("module$../filename$hello$world", processCommonJSModules.toModuleName("filename/hello/world", "../filename"));
        assertEquals("module$hello$world$filename", processCommonJSModules.toModuleName("../hello/world/filename", "filename"));
    }

    @Test
    public void testGuessCJSModuleName() {
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("filename"));
        assertEquals("module$filename$hello$world", processCommonJSModules.guessCJSModuleName("filename/hello/world"));
        assertEquals("module$directory$filename", processCommonJSModules.guessCJSModuleName("./directory/filename"));
        assertEquals("module$filename", processCommonJSModules.guessCJSModuleName("././././filename"));
        assertEquals("module$parent$directory$filename", processCommonJSModules.guessCJSModuleName("../parent/directory/filename"));
    }

    @Test
    public void testProcessRequireCall() {
        NodeTraversal t = null;
        Node require = new Node(Node.CALL);
        Node requireFirstChild = new Node(Node.NAME, "require");
        Node requireSecondChild = new Node(Node.STRING, "requiredModule");
        require.addChildToBack(requireFirstChild);
        requireFirstChild.addChildToFront(requireSecondChild);
        Node parent = new Node(Node.SCRIPT);
        parent.addChildToBack(require);

        String moduleName = processCommonJSModules.toModuleName("requiredModule", "");
        Node moduleRef = new Node(Node.NAME, moduleName);
        parent.replaceChild(require, moduleRef);
        Node script = new Node(Node.SCRIPT);
        parent.addChildToFront(script);
        script.addChildToBack(new Node(Node.EXPR_RESULT,
                new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"),
                        new Node(Node.STRING, "require")), new Node(Node.STRING, moduleName))));

        processCommonJSModules.visitRequireCall(t, require, parent);

        assertEquals(parent.toStringTree(), script.toStringTree());
    }

    @Test
    public void testProcessScript() {
        NodeTraversal t = null;
        Node script = new Node(Node.SCRIPT);
        processCommonJSModules.scriptNodeCount = 1;

        String moduleName = processCommonJSModules.guessCJSModuleName("");
        Node var = new Node(Node.VAR);
        var.addChildToFront(new Node(Node.NAME, moduleName));
        var.addChildToBack(new Node(Node.OBJECTLIT));
        script.addChildToFront(var);
        Node scriptFirstChild = new Node(Node.EXPR_RESULT,
                new Node(Node.CALL, new Node(Node.GETPROP, new Node(Node.NAME, "goog"),
                        new Node(Node.STRING, "provide")), new Node(Node.STRING, moduleName)));
        script.addChildToFront(scriptFirstChild);

        processCommonJSModules.visitScript(t, script);

        assertEquals(parent.toStringTree(), script.toStringTree());
    }

    @Test
    public void testEmitOptionalModuleExportsOverride() {
        Node script = new Node(Node.SCRIPT);
        String moduleName = processCommonJSModules.guessCJSModuleName("");

        processCommonJSModules.modulesWithExports.add(moduleName);
        Node moduleExportsProp = new Node(Node.GETPROP, new Node(Node.NAME, moduleName),
                new Node(Node.STRING, "module$exports"));
        Node ifNode = new Node(Node.IF, moduleExportsProp,
                new Node(Node.BLOCK, new Node(Node.EXPR_RESULT, new Node(Node.ASSIGN,
                        new Node(Node.NAME, moduleName), moduleExportsProp.cloneTree()))));
        script.addChildToBack(ifNode);
        processCommonJSModules.emitOptionalModuleExportsOverride(script, moduleName);

        assertEquals(ifNode.toStringTree(), script.getLastChild().toStringTree());
    }

    @Test
    public void testVisitModuleExports() {
        Node prop = new Node(Node.GETPROP);
        prop.addChildToFront(new Node(Node.NAME, "module"));
        prop.addChildToBack(new Node(Node.STRING, "exports"));

        String moduleName = processCommonJSModules.guessCJSModuleName("");
        Node module = new Node(Node.NAME, moduleName);
        module.putProp(Node.ORIGINALNAME_PROP, "module");
        prop.replaceChild(prop.getFirstChild(), module);
        Node exports = new Node(Node.STRING, "module$exports");
        exports.putProp(Node.ORIGINALNAME_PROP, "exports");
        prop.replaceChild(prop.getLastChild(), exports);
        processCommonJSModules.modulesWithExports.add(moduleName);

        processCommonJSModules.visitModuleExports(prop);

        assertEquals(module.toStringTree(), prop.getFirstChild().toStringTree());
        assertEquals(exports.toStringTree(), prop.getLastChild().toStringTree());
        assertEquals(moduleName, module.getString());
        assertEquals("module$exports", exports.getString());
    }

    @Test
    public void testSuffixVarsCallback() {
        NodeTraversal t = null;
        Node n = new Node(Node.VAR);
        n.addChildToFront(new Node(Node.NAME, "varName"));
        n.addChildToBack(new Node(Node.STRING, "hello"));
        Node parent = new Node(Node.SCRIPT);
        parent.addChildToBack(n);
        String suffix = "suffix";

        processCommonJSModules.new SuffixVarsCallback(suffix).visit(t, n, parent);

        assertEquals("varName$$suffix", n.getFirstChild().getString());
        assertNull(n.getFirstChild().getProp(Node.ORIGINALNAME_PROP));
    }
}