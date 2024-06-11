import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testCheckGlobalThis_constructor() {
        // Given
        String sourceCode = "function Test() {\n" +
                            "    this.prop = 'test';\n" +
                            "}";
        
        // When
        
        // Then
        // Verify that GLOBAL_THIS warning is not reported
    }

    @Test
    public void testCheckGlobalThis_functionAnnotated() {
        // Given
        String sourceCode = "function Test() {\n" +
                            "    /** @constructor */\n" +
                            "    this.method = function() {\n" +
                            "        return this;\n" +
                            "    };\n" +
                            "}";
        
        // When
        
        // Then
        // Verify that GLOBAL_THIS warning is not reported
    }

    @Test
    public void testCheckGlobalThis_noLogicalPlace() {
        // Given
        String sourceCode = "function Test() {\n" +
                            "    var a = this;\n" +
                            "    a.useful = undefined;\n" +
                            "}";
        
        // When
        
        // Then
        // Verify that GLOBAL_THIS warning is not reported
    }

    @Test
    public void testCheckGlobalThis_unsafeUse() {
        // Given
        String sourceCode = "function Test() {\n" +
                            "    this.useful = undefined;\n" +
                            "}";
        
        // When
        
        // Then
        // Verify that GLOBAL_THIS warning is reported
    }

}