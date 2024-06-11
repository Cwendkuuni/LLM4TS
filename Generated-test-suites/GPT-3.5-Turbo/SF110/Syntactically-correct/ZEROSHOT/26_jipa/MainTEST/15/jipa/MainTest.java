import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MainTest {

    @Test
    public void testProcessInstruction_HaltOpcode_ShouldExit() {
        // Arrange
        String instruction = "halt";
        
        // Act
        Main.processInstruction(instruction);
        
        // Assert
        verify(System, times(1)).exit(0);
    }

    @Test
    public void testProcessInstruction_OutOpcode_ShouldPrintStringValue() {
        // Arrange
        String instruction = "out operand";
        String operand = "value";
        Main.Variable.setVariableValue(operand, "test");
        
        // Act
        Main.processInstruction(instruction);
        
        // Assert
        verify(System.out, times(1)).println("test");
    }

    @Test
    public void testProcessInstruction_InOpcode_ShouldSetVariableValue() {
        // Arrange
        String instruction = "in operand";
        String operand = "value";
        
        // Mock the readValue() method to return 5
        Main.readValue = mock(Integer.class);
        when(Main.readValue()).thenReturn(5);
        
        // Act
        Main.processInstruction(instruction);
        
        // Assert
        assertEquals(5, Main.Variable.getVariableValue(operand));
    }

    // More test methods for other opcodes...

    @Test
    public void testGetJumpValue_WithNumericOperand_ShouldReturnOperandMinusOne() {
        // Arrange
        String operand = "5";
        
        // Act
        int result = Main.getJumpValue(operand);
        
        // Assert
        assertEquals(4, result);
    }

    @Test
    public void testGetJumpValue_WithNonNumericOperand_ShouldReturnLabelIndexValueMinusOne() {
        // Arrange
        String operand = "label";
        
        // Mock the findLabel() method to return 5
        Main.Label.findLabel = mock(Integer.class);
        when(Main.Label.findLabel(operand)).thenReturn(5);
        
        // Act
        int result = Main.getJumpValue(operand);
        
        // Assert
        assertEquals(4, result);
    }

    // More test methods for other helper methods...

}