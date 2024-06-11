import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

    @Test
    public void testProcess_WithValidJSCode_ShouldPass() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        VarCheck varCheck = new VarCheck(compiler);

        String jsCode = "var x = 5;";

        // Act
        varCheck.process(null, new Node(jsCode));

        // Assert
        // No exceptions should be thrown
    }

    @Test
    public void testProcess_WithUndefinedVariable_ShouldThrowError() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        VarCheck varCheck = new VarCheck(compiler);

        String jsCode = "console.log(x);";

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> {
            varCheck.process(null, new Node(jsCode));
        });
    }

    @Test
    public void testProcess_WithVariableInDifferentModuleWithoutDependency_ShouldThrowError() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        VarCheck varCheck = new VarCheck(compiler);

        String jsCode1 = "var x = 5;";
        String jsCode2 = "console.log(x);";

        Node node1 = new Node(jsCode1);
        node1.setInput(new CompilerInput(compiler));
        node1.getInput().setModule(new JSModule("module1"));

        Node node2 = new Node(jsCode2);
        node2.setInput(new CompilerInput(compiler));
        node2.getInput().setModule(new JSModule("module2"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            varCheck.process(null, node1);
            varCheck.process(null, node2);
        });
    }

    @Test
    public void testProcess_WithVariableInDifferentModuleWithDependency_ShouldPass() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        VarCheck varCheck = new VarCheck(compiler);

        String jsCode1 = "var x = 5;";
        String jsCode2 = "'use strict'; console.log(x);";

        Node node1 = new Node(jsCode1);
        node1.setInput(new CompilerInput(compiler));
        node1.getInput().setModule(new JSModule("module1"));

        Node node2 = new Node(jsCode2);
        node2.setInput(new CompilerInput(compiler));
        node2.getInput().setModule(new JSModule("module2"));

        compiler.getModuleGraph().addDependency(node2.getInput().getModule(), node1.getInput().getModule());

        // Act and Assert
        assertDoesNotThrow(() -> {
            varCheck.process(null, node1);
            varCheck.process(null, node2);
        });
    }
}