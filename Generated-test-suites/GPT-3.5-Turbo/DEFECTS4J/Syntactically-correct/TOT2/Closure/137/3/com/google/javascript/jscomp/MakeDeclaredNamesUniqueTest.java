import static org.junit.Assert.*;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testRenameFunctionParams() {
        // Test renaming function parameters
        // Given
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        
        // When
        renamer.addDeclaredName("param1");
        renamer.addDeclaredName("param2");
        
        // Then
        assertEquals("param1$$0", renamer.getReplacementName("param1"));
        assertEquals("param2$$0", renamer.getReplacementName("param2"));
    }

    @Test
    public void testRenameFunctionRecursiveName() {
        // Test renaming function recursive name
        // Given
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        
        // When
        renamer.addDeclaredName("foo");
        
        // Then
        assertEquals("foo$$0", renamer.getReplacementName("foo"));
    }

    @Test
    public void testRenameCatchException() {
        // Test renaming catch exceptions
        // Given
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        
        // When
        renamer.addDeclaredName("e");
        
        // Then
        assertEquals("e$$0", renamer.getReplacementName("e"));
    }

    @Test
    public void testUniqueName() {
        // Test generating unique name
        // Given
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        
        // When
        String uniqueName = renamer.getUniqueName("testName", 1);
        
        // Then
        assertEquals("testName$$1", uniqueName);
    }

    @Test
    public void testInlineRenamer() {
        // Test InlineRenamer functionality
        // Given
        MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "prefix", true);
        
        // When
        renamer.addDeclaredName("varName");
        String replacement = renamer.getReplacementName("varName");
        
        // Then
        assertEquals("varName$$prefix1", replacement);
    }

}