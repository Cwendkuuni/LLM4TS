import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private AbstractCompiler compiler;
    private JSModuleGraph moduleGraph;
    private AnalyzePrototypeProperties analyzer;
    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setup() {
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        compiler = new AbstractCompiler();
        moduleGraph = new JSModuleGraph();
        analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, true, false);
        crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);
    }

    @Test
    public void testProcess_withMultipleModules_shouldMoveMethodsDeeper() {
        // Setup
        Node externRoot = new Node(IR.script());
        Node root = new Node(IR.script());
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        moduleGraph.addModule(module1);
        moduleGraph.addModule(module2);
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Property(
                "prototypeMethod",
                IR.function(IR.name(""), IR.paramList(), IR.block()),
                module1,
                module2
        );
        nameInfo.addDeclaration(symbol);
        analyzer.addNameInfo(nameInfo);

        // Test
        crossModuleMethodMotion.process(externRoot, root);

        // Verify
        // Verify that the prototype method is moved to module2
        assertTrue(moduleGraph.dependsOn(module2, module1));
        assertFalse(moduleGraph.dependsOn(module1, module2));
        assertEquals(module2, symbol.getDeepestCommonModuleRef());
    }

    @Test
    public void testProcess_withUnreferencedNameInfo_shouldNotMoveMethods() {
        // Setup
        Node externRoot = new Node(IR.script());
        Node root = new Node(IR.script());
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        moduleGraph.addModule(module1);
        moduleGraph.addModule(module2);
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();

        // Test
        crossModuleMethodMotion.process(externRoot, root);

        // Verify
        assertFalse(moduleGraph.dependsOn(module2, module1));
        assertFalse(moduleGraph.dependsOn(module1, module2));
    }

    @Test
    public void testProcess_withReadsClosureVariables_shouldNotMoveMethods() {
        // Setup
        Node externRoot = new Node(IR.script());
        Node root = new Node(IR.script());
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        moduleGraph.addModule(module1);
        moduleGraph.addModule(module2);
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        nameInfo.setReadsClosureVariables(true);

        // Test
        crossModuleMethodMotion.process(externRoot, root);

        // Verify
        assertFalse(moduleGraph.dependsOn(module2, module1));
        assertFalse(moduleGraph.dependsOn(module1, module2));
    }

    @Test
    public void testProcess_withNullCommonModuleRef_shouldReportError() {
        // Setup
        Node externRoot = new Node(IR.script());
        Node root = new Node(IR.script());
        JSModule module1 = new JSModule("module1");
        moduleGraph.addModule(module1);
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Property(
                "prototypeMethod",
                IR.function(IR.name(""), IR.paramList(), IR.block()),
                module1,
                null
        );
        nameInfo.addDeclaration(symbol);
        analyzer.addNameInfo(nameInfo);

        // Test
        crossModuleMethodMotion.process(externRoot, root);

        // Verify
        // Verify that a null common module reference results in an error
        assertTrue(compiler.getErrorCount() > 0);
        assertFalse(moduleGraph.dependsOn(module1, null));
    }

    @Test
    public void testMoveMethods_withFunction_shouldMoveMethodAcrossModules() {
        // Setup
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Property(
                "prototypeMethod",
                IR.function(IR.name(""), IR.paramList(), IR.block()),
                module1,
                module2
        );
        nameInfo.addDeclaration(symbol);
        analyzer.addNameInfo(nameInfo);

        // Test
        crossModuleMethodMotion.moveMethods(analyzer.getAllNameInfo());

        // Verify
        // Verify that the prototype method is moved to module2
        assertTrue(moduleGraph.dependsOn(module2, module1));
        assertFalse(moduleGraph.dependsOn(module1, module2));
        assertEquals(module2, symbol.getDeepestCommonModuleRef());
    }

    @Test
    public void testMoveMethods_withGetter_shouldNotMoveMethod() {
        // Setup
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Property(
                "getterMethod",
                IR.getterDef(IR.name(""), IR.paramList(), IR.block()),
                module1,
                module2
        );
        nameInfo.addDeclaration(symbol);
        analyzer.addNameInfo(nameInfo);

        // Test
        crossModuleMethodMotion.moveMethods(analyzer.getAllNameInfo());

        // Verify
        // Verify that the getter method is not moved across modules
        assertFalse(moduleGraph.dependsOn(module2, module1));
        assertFalse(moduleGraph.dependsOn(module1, module2));
        assertNull(symbol.getDeepestCommonModuleRef());
    }

    @Test
    public void testMoveMethods_withSetter_shouldNotMoveMethod() {
        // Setup
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Property(
                "setterMethod",
                IR.setterDef(IR.name(""), IR.paramList(), IR.block()),
                module1,
                module2
        );
        nameInfo.addDeclaration(symbol);
        analyzer.addNameInfo(nameInfo);

        // Test
        crossModuleMethodMotion.moveMethods(analyzer.getAllNameInfo());

        // Verify
        // Verify that the setter method is not moved across modules
        assertFalse(moduleGraph.dependsOn(module2, module1));
        assertFalse(moduleGraph.dependsOn(module1, module2));
        assertNull(symbol.getDeepestCommonModuleRef());
    }

    @Test
    public void testMoveMethods_withPrototypeMethodAndNoStubDeclaration_shouldDeclareStubMethodsInTopModule() {
        // Setup
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Property(
                "prototypeMethod",
                IR.function(IR.name(""), IR.paramList(), IR.block()),
                module1,
                module2
        );
        nameInfo.addDeclaration(symbol);
        analyzer.addNameInfo(nameInfo);
        compiler.setSyntheticCodeMode(true); // To enable synthetic code insertion

        // Test
        crossModuleMethodMotion.moveMethods(analyzer.getAllNameInfo());

        // Verify
        // Verify that stub declaration is added to the top module
        assertEquals(1, compiler.getNodeForCodeInsertion(null).getChildCount());
        assertEquals("JSCompiler_stubMethod", compiler.getNodeForCodeInsertion(null).getChildAtIndex(0).getFirstChild().getQualifiedName());
        assertEquals("JSCompiler_unstubMethod", compiler.getNodeForCodeInsertion(null).getChildAtIndex(0).getLastChild().getQualifiedName());
    }

    @Test
    public void testMoveMethods_withPrototypeMethodAndStubDeclaration_shouldNotDeclareStubMethodsInTopModule() {
        // Setup
        JSModule module1 = new JSModule("module1");
        JSModule module2 = new JSModule("module2");
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Property(
                "prototypeMethod",
                IR.function(IR.name(""), IR.paramList(), IR.block()),
                module1,
                module2
        );
        nameInfo.addDeclaration(symbol);
        analyzer.addNameInfo(nameInfo);
        idGenerator.newId(); // Generate a stub ID
        compiler.setSyntheticCodeMode(true); // To enable synthetic code insertion

        // Test
        crossModuleMethodMotion.moveMethods(analyzer.getAllNameInfo());

        // Verify
        // Verify that stub declaration is not added to the top module
        assertEquals(0, compiler.getNodeForCodeInsertion(null).getChildCount());
    }
}