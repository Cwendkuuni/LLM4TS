import static org.junit.Assert.*;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

	@Test
	public void testDeclareName() {
		MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
		renamer.addDeclaredName("name");
		String replacementName = renamer.getReplacementName("name");
		// Check if replacement name is correct
		assertEquals("name$$0", replacementName);
	}
	
	@Test
	public void testInlineRenamer() {
		MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "1", "prefix_", true);
		renamer.addDeclaredName("name");
		String replacementName = renamer.getReplacementName("name");
		// Check if replacement name is correct
		assertEquals("name$$prefix_1", replacementName);
	}
	
	@Test
	public void testContextualRenameInverter() {
		MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
		renamer.addDeclaredName("name");
		
		MakeDeclaredNamesUnique.ContextualRenameInverter renamerInverter = new MakeDeclaredNamesUnique.ContextualRenameInverter(new AbstractCompiler());
		renamerInverter.nameMap.put(new Scope.Var(null, null, null, null, null, null), "name$$0");
		String newName = renamerInverter.getOrginalName("name$$0");
		// Check if original name is correct
		assertEquals("name", newName);
	}

}