import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void shouldReportDangerousUseOfGlobalThisObject() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        DiagnosticType expectedDiagnosticType = CheckGlobalThis.GLOBAL_THIS;

        // Act
        checkGlobalThis.visit(n, parent);
        DiagnosticType actualDiagnosticType = compiler.getDiagnosticType();

        // Assert
        Assert.assertEquals(expectedDiagnosticType, actualDiagnosticType);
    }

    @Test
    public void shouldNotReportSafeUseOfThisObject() {
        // Arrange
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
        DiagnosticType expectedDiagnosticType = null;

        // Act
        checkGlobalThis.visit(n, parent);
        DiagnosticType actualDiagnosticType = compiler.getDiagnosticType();

        // Assert
        Assert.assertEquals(expectedDiagnosticType, actualDiagnosticType);
    }
}