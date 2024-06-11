import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.MakeDeclaredNamesUnique.*;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testAddDeclaredName() {
        ContextualRenamer renamer = new ContextualRenamer();
        renamer.addDeclaredName("name1");
        String result = renamer.getReplacementName("name1");
        assertEquals("name1$$1", result);
    }

    @Test
    public void testGetReplacementName() {
        ContextualRenamer renamer = new ContextualRenamer();
        renamer.addDeclaredName("name1");
        String result = renamer.getReplacementName("name1");
        assertEquals("name1$$1", result);
    }

    @Test
    public void testForChildScope() {
        ContextualRenamer renamer = new ContextualRenamer();
        renamer.addDeclaredName("name1");
        Renamer childRenamer = renamer.forChildScope();
        childRenamer.addDeclaredName("name2");
        String result = childRenamer.getReplacementName("name1");
        assertEquals("name1$$1", result);
        result = childRenamer.getReplacementName("name2");
        assertEquals("name2$$1", result);
    }

    @Test
    public void testStripConstIfReplaced() {
        ContextualRenamer renamer = new ContextualRenamer();
        boolean result = renamer.stripConstIfReplaced();
        assertFalse(result);
    }

    @Test
    public void testInlineRenamer() {
        Supplier<String> uniqueIdSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "123";
            }
        };
        InlineRenamer renamer = new InlineRenamer(uniqueIdSupplier, "test_", true);
        renamer.addDeclaredName("name1");
        String result = renamer.getReplacementName("name1");
        assertEquals("name1$$test_123", result);
    }

}