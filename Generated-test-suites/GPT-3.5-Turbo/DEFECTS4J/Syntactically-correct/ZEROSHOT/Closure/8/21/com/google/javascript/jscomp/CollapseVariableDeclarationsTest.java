import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollapseVariableDeclarationsTest {
    
    private CollapseVariableDeclarations collapseVariableDeclarations;
    
    @Before
    public void setup() {
        collapseVariableDeclarations = new CollapseVariableDeclarations();
    }
    
    @Test
    public void testVariableDeclarationCollapse() {
        // Arrange
        String input = "var a;\nvar b = 1;\nvar c = 2;";
        String expectedOutput = "var a, b = 1, c = 2;";
        
        // Act
        String actualOutput = collapseVariableDeclarations.collapse(input);
        
        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testAssignCollapse() {
        // Arrange
        String input = "a = true;\nb = true;\nvar c = true;";
        String expectedOutput = "var c = b = a = true;";
        
        // Act
        String actualOutput = collapseVariableDeclarations.collapse(input);
        
        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
    
    @Test
    public void testVariableAndAssignCollapse() {
        // Arrange
        String input = "var a;\nvar b = 1;\nvar c = 2;\na = true;\nb = true;\nvar d = true;";
        String expectedOutput = "var a, b = 1, c = 2;\nvar d = b = a = true;";
        
        // Act
        String actualOutput = collapseVariableDeclarations.collapse(input);
        
        // Assert
        assertEquals(expectedOutput, actualOutput);
    }
    
}