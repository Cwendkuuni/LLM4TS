import org.junit.Assert;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

    @Test
    public void testProcess_ExternRootIsNull_ShouldNotMoveMethods() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        boolean canModifyExterns = true;
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        Node externRoot = null;
        Node root = new Node();

        // Act
        crossModuleMethodMotion.process(externRoot, root);

        // Assert
        // No methods should be moved
        Assert.assertEquals(0, idGenerator.currentId);
    }

    @Test
    public void testProcess_OnlyOneModule_ShouldNotMoveMethods() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        boolean canModifyExterns = true;
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        Node externRoot = new Node();
        Node root = new Node();
        JSModuleGraph moduleGraph = new JSModuleGraph();
        moduleGraph.addModule(new JSModule("Module1"));
        compiler.setModuleGraph(moduleGraph);

        // Act
        crossModuleMethodMotion.process(externRoot, root);

        // Assert
        // No methods should be moved
        Assert.assertEquals(0, idGenerator.currentId);
    }

    @Test
    public void testProcess_CanModifyExternsIsFalse_ShouldNotMoveExternProperties() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        boolean canModifyExterns = false;
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        Node externRoot = new Node();
        Node root = new Node();
        JSModuleGraph moduleGraph = new JSModuleGraph();
        moduleGraph.addModule(new JSModule("Module1"));
        moduleGraph.addModule(new JSModule("Module2"));
        compiler.setModuleGraph(moduleGraph);

        // Act
        crossModuleMethodMotion.process(externRoot, root);

        // Assert
        // No methods should be moved
        Assert.assertEquals(0, idGenerator.currentId);
    }

    @Test
    public void testProcess_ValidModules_CorrectMethodsMoved() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        boolean canModifyExterns = true;
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        Node externRoot = new Node();
        Node root = new Node();
        JSModuleGraph moduleGraph = new JSModuleGraph();
        JSModule module1 = new JSModule("Module1");
        JSModule module2 = new JSModule("Module2");
        JSModule module3 = new JSModule("Module3");
        moduleGraph.addModule(module1);
        moduleGraph.addModule(module2);
        moduleGraph.addModule(module3);
        compiler.setModuleGraph(moduleGraph);

        // Create some name info and add them to the analyzer
        AnalyzePrototypeProperties.NameInfo nameInfo1 = new AnalyzePrototypeProperties.NameInfo();
        AnalyzePrototypeProperties.NameInfo nameInfo2 = new AnalyzePrototypeProperties.NameInfo();
        collection.add(nameInfo1);
        collection.add(nameInfo2);

        // Create a property with deepestCommonModuleRef being module1
        AnalyzePrototypeProperties.Property property1 = new AnalyzePrototypeProperties.Property();
        property1.setModule(module2);
        Node prototype1 = new Node();
        property1.setPrototype(prototype1);
        property1.setValue(new Node(IR.function()));
        nameInfo1.addDeclaration(property1);

        // Create a property with deepestCommonModuleRef being module2
        AnalyzePrototypeProperties.Property property2 = new AnalyzePrototypeProperties.Property();
        property2.setModule(module3);
        Node prototype2 = new Node();
        property2.setPrototype(prototype2);
        property2.setValue(new Node(IR.function()));
        nameInfo2.addDeclaration(property2);

        crossModuleMethodMotion.moveMethods(collection);

        // Act
        crossModuleMethodMotion.process(externRoot, root);

        // Assert
        // Method from module2 should be moved to module1
        Assert.assertEquals(module1, prototype1);

        // Method from module3 should be moved to module2
        Assert.assertEquals(module2, prototype2);

        // Methods in module1 and module2 should be stubbed
        Assert.assertTrue(property1.getValue().isCall());
        Assert.assertTrue(property2.getValue().isCall());

        // Methods in deeper modules should be unstubbed
        Assert.assertTrue(property1.getSymbol().isAssign());
        Assert.assertEquals(module1, property1.getDeclarations().next().getModule());
        Assert.assertTrue(property2.getSymbol().isAssign());
        Assert.assertEquals(module2, property2.getDeclarations().next().getModule());

        // Check if code change has been reported
        Assert.assertTrue(compiler.hasReportedCodeChange());
    }

    @Test
    public void testProcess_UnreferencedNameInfo_ShouldSkipNameInfo() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        boolean canModifyExterns = true;
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        Node externRoot = new Node();
        Node root = new Node();
        JSModuleGraph moduleGraph = new JSModuleGraph();
        moduleGraph.addModule(new JSModule("Module1"));
        moduleGraph.addModule(new JSModule("Module2"));
        compiler.setModuleGraph(moduleGraph);

        // Create name info but it is unreferenced
        AnalyzePrototypeProperties.NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
        nameInfo.setDeepestCommonModuleRef(null);

        // Add the name info to the collection
        collection.add(nameInfo);

        // Act
        crossModuleMethodMotion.moveMethods(collection);
        crossModuleMethodMotion.process(externRoot, root);

        // Assert
        // No methods should be moved
        Assert.assertEquals(0, idGenerator.currentId);
    }

    @Test
    public void testProcess_PrototypeMethodReadsClosureVariables_ShouldSkipMethod() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        boolean canModifyExterns = true;
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
        Node externRoot = new Node();
        Node root = new Node();
        JSModuleGraph moduleGraph = new JSModuleGraph();
        moduleGraph.addModule(new JSModule("Module1"));
        moduleGraph.addModule(new JSModule("Module2"));
        compiler.setModuleGraph(moduleGraph);

        // Create name info where the prototype method reads closure variables
        AnalyzePrototypeProperties.NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
        nameInfo.setDeepestCommonModuleRef(new JSModule("SomeModule"));
        nameInfo.setReadsClosureVariables(true);

        // Add the name info to the collection
        collection.add(nameInfo);

        // Act
        crossModuleMethodMotion.moveMethods(collection);
        crossModuleMethodMotion.process(externRoot, root);

        // Assert
        // No methods should be moved
        Assert.assertEquals(0, idGenerator.currentId);
    }
}