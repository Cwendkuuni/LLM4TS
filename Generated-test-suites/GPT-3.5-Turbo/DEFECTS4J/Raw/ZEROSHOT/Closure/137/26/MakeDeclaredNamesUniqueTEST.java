import org.junit.Assert;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {
    
    @Test
    public void testAddDeclaredName() {
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name");
        
        Assert.assertEquals("name$$0", renamer.getReplacementName("name"));
    }
    
    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique.ContextualRenamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name");
        renamer.addDeclaredName("name");
        
        Assert.assertEquals("name$$0", renamer.getReplacementName("name"));
        Assert.assertEquals("name$$1", renamer.getReplacementName("name"));
    }
    
    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique.ContextualRenamer parentRenamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        parentRenamer.addDeclaredName("name");
        parentRenamer.addDeclaredName("name");
        
        MakeDeclaredNamesUnique.ContextualRenamer childRenamer = (MakeDeclaredNamesUnique.ContextualRenamer) parentRenamer.forChildScope();
        childRenamer.addDeclaredName("name");
        
        Assert.assertEquals("name$$0", parentRenamer.getReplacementName("name"));
        Assert.assertEquals("name$$1", parentRenamer.getReplacementName("name"));
        Assert.assertEquals("name$$2", childRenamer.getReplacementName("name"));
    }
    
    @Test
    public void testStripConstIfReplaced() {
        MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "", true);
        
        Assert.assertTrue(renamer.stripConstIfReplaced());
    }
    
    @Test
    public void testGetReplacementName_InlineRenamer() {
        MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "", false);
        renamer.addDeclaredName("name");
        
        Assert.assertEquals("name$$0", renamer.getReplacementName("name"));
    }
    
    @Test
    public void testGetReplacementName_InlineRenamer_WithPrefix() {
        MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "prefix", false);
        renamer.addDeclaredName("name");
        
        Assert.assertEquals("name$$prefix0", renamer.getReplacementName("name"));
    }
    
    @Test
    public void testGetReplacementName_InlineRenamer_RemoveConstness() {
        MakeDeclaredNamesUnique.InlineRenamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "", true);
        renamer.addDeclaredName("const");
        
        Assert.assertEquals("const$$0", renamer.getReplacementName("const"));
    }
}