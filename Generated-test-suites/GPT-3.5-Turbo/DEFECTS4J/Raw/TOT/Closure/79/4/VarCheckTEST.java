import org.junit.Assert;
import org.junit.Test;

public class VarCheckTest {

    @Test
    public void testVisit_WhenVarDeclaredAndReferencedInSameFile_NoErrorReported() {
        // Arrange
        Compiler compiler = new Compiler();
        VarCheck varCheck = new VarCheck(compiler);
        
        // Act
        Node varNode = new Node(Token.VAR);
        varNode.addChildToBack(new Node(Token.NAME, "x"));
        
        Node referenceNode = new Node(Token.NAME, "x");
        
        NodeTraversal t = new NodeTraversal(compiler, new AbstractPostOrderCallback(){}, null, null);
        t.getScope().declare("x", varNode, null, null);
        t.visit(null, referenceNode, null);
        
        // Assert
        Assert.assertEquals(0, compiler.getErrorCount());
    }

    @Test
    public void testVisit_WhenVarDeclaredAndReferencedInDifferentFiles_NoErrorReported() {
        // Arrange
        Compiler compiler = new Compiler();
        VarCheck varCheck = new VarCheck(compiler);

        // Create files
        CompilerInput input1 = createFileInput("file1");
        CompilerInput input2 = createFileInput("file2");

        // Create modules
        JSModule module1 = new JSModule("module1");
        module1.add(input1);
        JSModule module2 = new JSModule("module2");
        module2.add(input2);

        // Create module graph
        JSModuleGraph moduleGraph = compiler.getModuleGraph();
        moduleGraph.addModule(module1);
        moduleGraph.addModule(module2);
        moduleGraph.addDependency(module1, module2);

        // Set input modules
        compiler.getInputs().addAll(Lists.newArrayList(input1, input2));
        
        // Act
        Node varNode = new Node(Token.VAR);
        varNode.addChildToBack(new Node(Token.NAME, "x"));

        Node referenceNode = new Node(Token.NAME, "x");

        NodeTraversal t = new NodeTraversal(
            compiler,
            new AbstractPostOrderCallback() {},
            moduleGraph,
            module1);
        
        t.getScope().declare("x", varNode, input1, module1);
        t.visit(null, referenceNode, null);

        // Assert
        Assert.assertEquals(0, compiler.getErrorCount());
    }

    @Test
    public void testVisit_WhenVarDeclaredInDifferentFileAndReferenced_ErrorReported() {
        // Arrange
        Compiler compiler = new Compiler();
        VarCheck varCheck = new VarCheck(compiler);

        // Create files
        CompilerInput input1 = createFileInput("file1");
        CompilerInput input2 = createFileInput("file2");
        
        // Create modules
        JSModule module1 = new JSModule("module1");
        module1.add(input1);
        JSModule module2 = new JSModule("module2");
        module2.add(input2);
        
        // Create module graph
        JSModuleGraph moduleGraph = compiler.getModuleGraph();
        moduleGraph.addModule(module1);
        moduleGraph.addModule(module2);
        moduleGraph.addDependency(module1, module2);
        
        // Set input modules
        compiler.getInputs().addAll(Lists.newArrayList(input1, input2));
        
        // Act
        Node varNode = new Node(Token.VAR);
        varNode.addChildToBack(new Node(Token.NAME, "x"));
        
        Node referenceNode = new Node(Token.NAME, "x");

        NodeTraversal t = new NodeTraversal(
            compiler,
            new AbstractPostOrderCallback() {},
            moduleGraph,
            module2);
        
        t.getScope().declare("x", varNode, input1, module1);        
        t.visit(null, referenceNode, null);
        
        // Assert
        Assert.assertEquals(1, compiler.getErrorCount());
    }

    private CompilerInput createFileInput(String fileName) {
        String sourceCode = "console.log('This is " + fileName + "');";
        return new CompilerInput(sourceCode);
    }
}