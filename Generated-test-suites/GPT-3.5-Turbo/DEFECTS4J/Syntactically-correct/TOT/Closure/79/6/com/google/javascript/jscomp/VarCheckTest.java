import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {
    private VarCheck varCheck;
    
    @Before
    public void setup() {
        varCheck = new VarCheck();
    }
    
    @Test
    public void testProcess_WithUndefinedVar_ErrorIsReported() {
        // Arrange
        String code = "function test() { console.log(name); }";
        CompilerInput input = new CompilerInput(code, true);
        
        // Act
        varCheck.process(input);
        
        // Assert
        Assert.assertEquals("Expected 1 error", 1, varCheck.getErrors().size());
        Assert.assertEquals("Expected UNDEFINED_VAR_ERROR", VarCheck.UNDEFINED_VAR_ERROR, varCheck.getErrors().get(0).getType());
        Assert.assertEquals("Expected variable name to be 'name'", "name", varCheck.getErrors().get(0).getMessageParams()[0]);
    }
    
    @Test
    public void testProcess_WithDeclaredVar_NoErrorsAreReported() {
        // Arrange
        String code = "var name = 'John'; function test() { console.log(name); }";
        CompilerInput input = new CompilerInput(code, true);
        
        // Act
        varCheck.process(input);
        
        // Assert
        Assert.assertEquals("Expected 0 errors", 0, varCheck.getErrors().size());
    }
    
    @Test
    public void testProcess_WithDeclaredVarInExterns_NoErrorsAreReported() {
        // Arrange
        String code = "function test() { console.log(document); }";
        CompilerInput input = new CompilerInput(code, true, true);
        
        // Act
        varCheck.process(input);
        
        // Assert
        Assert.assertEquals("Expected 0 errors", 0, varCheck.getErrors().size());
    }
    
    @Test
    public void testProcess_WithVarDeclaredInDifferentModule_ErrorIsReported() {
        // Arrange
        String code1 = "var name = 'John';";
        String code2 = "function test() { console.log(name); }";
        CompilerInput input1 = new CompilerInput(code1, true);
        CompilerInput input2 = new CompilerInput(code2, true);
        input2.setModule("moduleA");
        
        // Act
        varCheck.process(input1);
        varCheck.process(input2);
        
        // Assert
        Assert.assertEquals("Expected 1 error", 1, varCheck.getErrors().size());
        Assert.assertEquals("Expected VIOLATED_MODULE_DEP_ERROR", VarCheck.VIOLATED_MODULE_DEP_ERROR, varCheck.getErrors().get(0).getType());
        Assert.assertEquals("Expected current module name to be 'moduleA'", "moduleA", varCheck.getErrors().get(0).getMessageParams()[0]);
        Assert.assertEquals("Expected var module name to be 'global'", "global", varCheck.getErrors().get(0).getMessageParams()[1]);
        Assert.assertEquals("Expected variable name to be 'name'", "name", varCheck.getErrors().get(0).getMessageParams()[2]);
    }
}