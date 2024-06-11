import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.VarCheck;

public class VarCheckTest {
  
  private VarCheck varCheck;
  
  @Before
  public void setUp() {
    varCheck = new VarCheck();
  }
  
  @Test
  public void testProcess_WhenVarIsDeclaredInSameFile_ShouldNotReportError() {
    // Arrange
    String code = "var x;";
    
    // Act
    varCheck.process(code);
    
    // Assert
    assertFalse(varCheck.hasError());
  }
  
  @Test
  public void testProcess_WhenVarIsDeclaredInDifferentFile_ShouldCheckModuleDependencies() {
    // Arrange
    String code1 = "var x;";
    String code2 = "x = 5;";
    
    // Act
    varCheck.process(code1);
    varCheck.process(code2);
    
    // Assert
    assertFalse(varCheck.hasError());
  }
  
  @Test
  public void testProcess_WhenVarIsUndefined_ShouldReportUndefinedError() {
    // Arrange
    String code = "y = 5;";
    
    // Act
    varCheck.process(code);
    
    // Assert
    assertTrue(varCheck.hasError());
    assertEquals("variable y is undefined", varCheck.getError());
  }
  
  @Test
  public void testProcess_WhenVarReferenceViolatesModuleDependency_ShouldReportViolationError() {
    // Arrange
    String code1 = "var x;";
    String code2 = "y = x;";
    
    // Act
    varCheck.process(code1);
    varCheck.process(code2);
    
    // Assert
    assertTrue(varCheck.hasError());
    assertEquals("module module2 cannot reference y, defined in module module1, since module1 loads after module2", varCheck.getError());
  }
  
  @Test
  public void testProcess_WhenVarReferenceHasNoModuleDependency_ShouldReportMissingWarning() {
    // Arrange
    String code1 = "var x;";
    String code2 = "y = z;";
    
    // Act
    varCheck.process(code1);
    varCheck.process(code2);
    
    // Assert
    assertTrue(varCheck.hasWarning());
    assertEquals("missing module dependency; module module2 should depend on module module1 because it references z", varCheck.getWarning());
  }
  
  @Test
  public void testProcess_WhenFunctionDeclarationHasEmptyName_ShouldReportSyntaxError() {
    // Arrange
    String code = "function() {}";
    
    // Act
    varCheck.process(code);
    
    // Assert
    assertTrue(varCheck.hasError());
    assertEquals("Syntax error: function declaration must have a name", varCheck.getError());
  }
  
  @Test
  public void testProcess_WhenFunctionDeclarationHasDuplicateVar_ShouldNotReportDuplicateError() {
    // Arrange
    String code1 = "var x;";
    String code2 = "function x() {}";
    
    // Act
    varCheck.process(code1);
    varCheck.process(code2);
    
    // Assert
    assertFalse(varCheck.hasError());
  }
  
  @Test
  public void testProcess_WhenVarIsDeclaredWithConstantName_ShouldMarkVariableAsConstant() {
    // Arrange
    String code = "const PI = 3.14;";
    
    // Act
    varCheck.process(code);
    
    // Assert
    assertFalse(varCheck.hasError());
    assertTrue(varCheck.isConstant("PI"));
  }
  
  @Test
  public void testProcess_WhenNameReferenceIsInExterns_ShouldNotReportError() {
    // Arrange
    String code = "goog.provide('x'); x = 5;";
    
    // Act
    varCheck.process(code);
    
    // Assert
    assertFalse(varCheck.hasWarning());
  }
  
  @Test
  public void testProcess_WhenNameReferenceIsNotInExterns_ShouldReportUndefinedExternVarWarning() {
    // Arrange
    String code = "y = 5;";
    
    // Act
    varCheck.process(code);
    
    // Assert
    assertTrue(varCheck.hasWarning());
    assertEquals("name y is not undefined in the externs.", varCheck.getWarning());
  }
  
  @Test
  public void testProcess_WhenVarCheckIsUsedInSanityCheckMode_ShouldThrowIllegalStateExceptionOnUndefinedVariable() {
    // Arrange
    String code = "y = 5;";
    varCheck = new VarCheck(true);
    
    // Act & Assert
    assertThrows(IllegalStateException.class, () -> {
      varCheck.process(code);
    });
  }
  
}