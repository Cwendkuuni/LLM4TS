import static org.junit.Assert.*;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testContextualRenamerConstructor() {
        ContextualRenamer renamer = new ContextualRenamer();
        assertNotNull(renamer);
    }
    
    @Test
    public void testInlineRenamerAddDeclaredName() {
        Supplier<String> uniqueIdSupplier = () -> "1";
        InlineRenamer renamer = new InlineRenamer(uniqueIdSupplier, "test", true);
        renamer.addDeclaredName("test");
        String replacement = renamer.getReplacementName("test");
        assertEquals("test$$test$1", replacement);
    }

    @Test
    public void testMakeDeclaredNamesUniqueConstructor() {
        MakeDeclaredNamesUnique uniqueNamer = new MakeDeclaredNamesUnique();
        assertNotNull(uniqueNamer);
    }

    // Add more test cases for each method in the MakeDeclaredNamesUnique class

    @Test
    public void testGetOrginalName() {
        String originalName = ContextualRenameInverter.getOrginalName("test$$1");
        assertEquals("test", originalName);
    }

    @Test
    public void testIndexOfSeparator() {
        int index = ContextualRenameInverter.indexOfSeparator("test$$1");
        assertEquals(4, index);
    }

    // Add more test cases for edge cases and exception handling scenarios

}