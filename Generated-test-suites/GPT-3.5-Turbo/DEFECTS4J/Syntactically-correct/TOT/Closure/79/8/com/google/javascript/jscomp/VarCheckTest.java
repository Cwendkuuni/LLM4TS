import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

    @Test
    public void testProcess_ValidInput_ProperDeclaration() {
        // Arrange
        VarCheck varCheck = new VarCheck();

        // Act
        varCheck.process(externs, root);

        // Assert
        // TODO: add assertions
    }

    @Test
    public void testProcess_MissingModuleDependency_ReferenceError() {
        // Arrange
        VarCheck varCheck = new VarCheck();

        // Act
        varCheck.process(externsWithMissingDependency, root);

        // Assert
        // TODO: add assertions
    }

    @Test
    public void testProcess_ViolatedModuleDependency_ViolationError() {
        // Arrange
        VarCheck varCheck = new VarCheck();

        // Act
        varCheck.process(externs, rootWithViolation);

        // Assert
        // TODO: add assertions
    }

    @Test
    public void testProcess_UndefinedVarError_ErrorThrown() {
        // Arrange
        VarCheck varCheck = new VarCheck();

        // Act
        // TODO: call the process method with an input that causes an undefined var error

        // Assert
        // TODO: add assertions
    }

    @Test
    public void testProcess_NameRefInExternsWarning_WarningThrown() {
        // Arrange
        VarCheck varCheck = new VarCheck();

        // Act
        // TODO: call the process method with an input that causes a name reference in externs warning

        // Assert
        // TODO: add assertions
    }

    @Test
    public void testProcess_InvalidFunctionDeclaration_SyntaxError() {
        // Arrange
        VarCheck varCheck = new VarCheck();

        // Act
        // TODO: call the process method with an input that causes an invalid function declaration error

        // Assert
        // TODO: add assertions
    }

    @Test
    public void testProcess_InvalidExternVariable_UndefinedExternVarError() {
        // Arrange
        VarCheck varCheck = new VarCheck();

        // Act
        // TODO: call the process method with an input that causes an undefined extern variable error

        // Assert
        // TODO: add assertions
    }

}