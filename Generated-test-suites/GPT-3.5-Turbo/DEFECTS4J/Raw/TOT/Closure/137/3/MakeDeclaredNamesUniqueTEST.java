import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import com.google.javascript.jscomp.MakeDeclaredNamesUnique.*;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testAddDeclaredName() {
        Renamer renamer = new ContextualRenamer();
        renamer.addDeclaredName("name1");
        renamer.addDeclaredName("name2");
        renamer.addDeclaredName("name3");
        assertEquals(3, renamer.nameUsage.count("name1"));
        assertEquals(2, renamer.nameUsage.count("name2"));
        assertEquals(1, renamer.nameUsage.count("name3"));
    }

    @Test
    public void testGetReplacementName() {
        Renamer renamer = new ContextualRenamer();
        renamer.addDeclaredName("name1");
        renamer.addDeclaredName("name2");
        renamer.addDeclaredName("name3");
        assertEquals("name1", renamer.getReplacementName("name1"));
        assertEquals("name2", renamer.getReplacementName("name2"));
        assertEquals("name3", renamer.getReplacementName("name3"));
    }

    @Test
    public void testStripConstIfReplaced() {
        Renamer renamer1 = new ContextualRenamer();
        Renamer renamer2 = new InlineRenamer(() -> "1", "prefix", true);
        assertEquals(false, renamer1.stripConstIfReplaced());
        assertEquals(true, renamer2.stripConstIfReplaced());
    }

    @Test
    public void testForChildScope() {
        Renamer renamer = new ContextualRenamer();
        renamer.addDeclaredName("name1");
        renamer.addDeclaredName("name2");
        renamer.addDeclaredName("name3");
        Renamer childRenamer = renamer.forChildScope();
        assertEquals(3, childRenamer.nameUsage.count("name1"));
        assertEquals(2, childRenamer.nameUsage.count("name2"));
        assertEquals(1, childRenamer.nameUsage.count("name3"));
    }
}