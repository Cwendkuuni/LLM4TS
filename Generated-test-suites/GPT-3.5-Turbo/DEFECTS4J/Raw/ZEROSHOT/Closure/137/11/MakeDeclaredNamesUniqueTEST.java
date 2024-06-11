import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {
    @Test
    public void testEnterScope() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "prefix", true);
        renamer.addDeclaredName("name");
        String replacementName = renamer.getReplacementName("name");
        assert replacementName.equals("name$$prefix");
    }

    @Test
    public void testExitScope() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "prefix", true);
        renamer.addDeclaredName("name");
        renamer.addDeclaredName("name");
        String replacementName = renamer.getReplacementName("name");
        assert replacementName.equals("name$$prefix");
    }

    @Test
    public void testShouldTraverse() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "prefix", true);
        renamer.addDeclaredName("name");
        boolean stripConstIfReplaced = renamer.stripConstIfReplaced();
        assert stripConstIfReplaced == true;
    }

    @Test
    public void testShouldTraverse2() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "prefix", true);
        renamer.addDeclaredName("name");
        String replacementName = renamer.getReplacementName("name");
        assert replacementName.equals("name$$prefix");
    }

    @Test
    public void testVisit() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "prefix", true);
        renamer.addDeclaredName("name");
        String replacementName = renamer.getReplacementName("name");
        assert replacementName.equals("name$$prefix");
    }

    @Test
    public void testVisit2() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "prefix", true);
        renamer.addDeclaredName("name");
        String replacementName = renamer.getReplacementName("name");
        assert replacementName.equals("name$$prefix");
    }

    @Test
    public void testFindDeclaredNames() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "prefix", true);
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        makeDeclaredNamesUnique.findDeclaredNames(null, null, renamer);
        String replacementName = renamer.getReplacementName("name");
        assert replacementName == null;
    }

    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(null, "prefix", true);
        renamer.addDeclaredName("name");
        String replacementName = renamer.getReplacementName("name");
        assert replacementName.equals("name$$prefix");
    }
}