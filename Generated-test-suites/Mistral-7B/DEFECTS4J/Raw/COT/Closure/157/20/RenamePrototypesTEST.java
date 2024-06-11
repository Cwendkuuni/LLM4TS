import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RenamePrototypesTest {

  @Test
  public void testNewInstance() {
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);
    assertNotNull(renamer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNewInstanceWithNullCompiler() {
    new RenamePrototypes(null, false, null, null);
  }

  @Test
  public void testProcessor() {
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);

    Node externs = new Node(Token.OBJECTLIT);
    Node root = new Node(Token.PROGRAM);

    renamer.process(externs, root);

    // No change in the absence of properties to rename
  }

  @Test
  public void testProcessorWithStringAccess() {
    String oldName = "oldProperty";
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);

    Node externs = new Node(Token.OBJECTLIT);
    Node root = new Node(Token.PROGRAM);

    externs.addChild(createPropertyAccessNode("Foo.prototype", oldName));
    root.addChild(createPropertyAccessNode("Foo.prototype", oldName));

    renamer.process(externs, root);

    Node renamedExtern = externs.getFirstChild().getLastChild();
    Node renamedRoot = root.getFirstChild().getLastChild();

    assertEquals(renamer.getNewName(oldName), renamedExtern.getString());
    assertEquals(renamedExtern.getString(), renamedRoot.getString());
  }

  @Test
  public void testProcessorWithPrivateStringAccess() {
    String oldName = "_oldProperty";
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);

    Node externs = new Node(Token.OBJECTLIT);
    Node root = new Node(Token.PROGRAM);

    externs.addChild(createPropertyAccessNode("Foo.prototype", oldName));
    root.addChild(createPropertyAccessNode("Foo.prototype", oldName));

    renamer.process(externs, root);

    Node renamedExtern = externs.getFirstChild().getLastChild();
    Node renamedRoot = root.getFirstChild().getLastChild();

    assertEquals(renamedExtern.getString(), renamedRoot.getString());
    assertNull(renamer.getNewName(oldName));
  }

  @Test
  public void testProcessorWithExportedStringAccess() {
    String oldName = "IndexedDB";
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);

    Node externs = new Node(Token.OBJECTLIT);
    Node root = new Node(Token.PROGRAM);

    externs.addChild(createPropertyAccessNode("self.IndexedDB", oldName));
    root.addChild(createPropertyAccessNode("self.IndexedDB", oldName));

    renamer.process(externs, root);

    Node renamedExtern = externs.getFirstChild().getLastChild();
    Node renamedRoot = root.getFirstChild().getLastChild();

    assertEquals(oldName, renamedExtern.getString());
    assertEquals(oldName, renamedRoot.getString());
    assertNull(renamer.getNewName(oldName));
  }

  @Test
  public void testProcessorWithAggressiveRenaming() {
    String oldName = "oldProperty";
    RenamePrototypes renamer = new RenamePrototypes(null, true, null, null);

    Node externs = new Node(Token.OBJECTLIT);
    Node root = new Node(Token.PROGRAM);

    externs.addChild(createPropertyAccessNode("Foo.prototype", oldName));
    root.addChild(createPropertyAccessNode("Foo.prototype", oldName));

    renamer.process(externs, root);

    Node renamedExtern = externs.getFirstChild().getLastChild();
    Node renamedRoot = root.getFirstChild().getLastChild();

    String newName = renamer.getNewName(oldName);
    assertEquals(newName, renamedExtern.getString());
    assertEquals(newName, renamedRoot.getString());
  }

  @Test
  public void testProcessorWithAggressiveRenamingAndPrivateName() {
    String oldName = "_oldProperty";
    RenamePrototypes renamer = new RenamePrototypes(null, true, null, null);

    Node externs = new Node(Token.OBJECTLIT);
    Node root = new Node(Token.PROGRAM);

    externs.addChild(createPropertyAccessNode("Foo.prototype", oldName));
    root.addChild(createPropertyAccessNode("Foo.prototype", oldName));

    renamer.process(externs, root);

    Node renamedExtern = externs.getFirstChild().getLastChild();
    Node renamedRoot = root.getFirstChild().getLastChild();

    assertEquals(renamer.getNewName(oldName), renamedExtern.getString());
    assertEquals(renamedExtern.getString(), renamedRoot.getString());
  }

  @Test
  public void testProcessorWithAggressiveRenamingAndExportedName() {
    String oldName = "IndexedDB";
    RenamePrototypes renamer = new RenamePrototypes(null, true, null, null);

    Node externs = new Node(Token.OBJECTLIT);
    Node root = new Node(Token.PROGRAM);

    externs.addChild(createPropertyAccessNode("self.IndexedDB", oldName));
    root.addChild(createPropertyAccessNode("self.IndexedDB", oldName));

    renamer.process(externs, root);

    Node renamedExtern = externs.getFirstChild().getLastChild();
    Node renamedRoot = root.getFirstChild().getLastChild();

    assertEquals(oldName, renamedExtern.getString());
    assertEquals(oldName, renamedRoot.getString());
    assertNull(renamer.getNewName(oldName));
  }

  @Test
  public void testProcessorWithStringAssignment() {
    String oldName = "oldProperty";
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);

    Node externs = new Node(Token.PROGRAM);
    Node root = new Node(Token.PROGRAM);

    externs.addChild(
        new Node(Token.ASSIGN,
            new Node(Token.GETPROP, new Node(Token.STRING, "Foo.prototype"), oldName)));
    root.addChild(
        new Node(Token.ASSIGN,
            new Node(Token.GETPROP, new Node(Token.STRING, "Foo.prototype"), oldName),
            new Node(Token.STRING, renamer.getNewName(oldName))));

    renamer.process(null, externs);
    renamer.process(root, null);

    Node renamedExtern = externs.getFirstChild();
    Node renamedRoot = root.getFirstChild();

    assertEquals(renamer.getNewName(oldName), renamedRoot.getLastChild().getString());
    assertEquals(renamedRoot.getLastChild().getString(), renamedExtern.getLastChild().getString());
  }

  @Test
  public void testProcessorWithStringAssignmentAndPrivateName() {
    String oldName = "_oldProperty";
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);

    Node externs = new Node(Token.PROGRAM);
    Node root = new Node(Token.PROGRAM);

    externs.addChild(
        new Node(Token.ASSIGN,
            new Node(Token.GETPROP, new Node(Token.STRING, "Foo.prototype"), oldName)));
    root.addChild(
        new Node(Token.ASSIGN,
            new Node(Token.GETPROP, new Node(Token.STRING, "Foo.prototype"), oldName),
            new Node(Token.STRING, renamer.getNewName(oldName))));

    renamer.process(null, externs);
    renamer.process(root, null);

    Node renamedExtern = externs.getFirstChild();
    Node renamedRoot = root.getFirstChild();

    assertNull(renamer.getNewName(oldName));
    assertEquals(renamedRoot.getLastChild().getString(), renamedExtern.getLastChild().getString());
  }

  @Test
  public void testProcessorWithStringAssignmentAndExportedName() {
    String oldName = "IndexedDB";
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);

    Node externs = new Node(Token.PROGRAM);
    Node root = new Node(Token.PROGRAM);

    externs.addChild(
        new Node(Token.ASSIGN,
            new Node(Token.GETPROP, new Node(Token.STRING, "self.IndexedDB"), oldName)));
    root.addChild(
        new Node(Token.ASSIGN,
            new Node(Token.GETPROP, new Node(Token.STRING, "self.IndexedDB"), oldName),
            new Node(Token.STRING, renamer.getNewName(oldName))));

    renamer.process(null, externs);
    renamer.process(root, null);

    Node renamedExtern = externs.getFirstChild();
    Node renamedRoot = root.getFirstChild();

    assertEquals(oldName, renamedExtern.getLastChild().getString());
    assertEquals(oldName, renamedRoot.getLastChild().getString());
    assertNull(renamer.getNewName(oldName));
  }

  @Test
  public void testProcessorWithStringAssignmentAndAggressiveRenaming() {
    String oldName = "oldProperty";
    RenamePrototypes renamer = new RenamePrototypes(null, true, null, null);

    Node externs = new Node(Token.PROGRAM);
    Node root = new Node(Token.PROGRAM);

    externs.addChild(
        new Node(Token.ASSIGN,
            new Node(Token.GETPROP, new Node(Token.STRING, "Foo.prototype"), oldName)));
    root.addChild(
        new Node(Token.ASSIGN,
            new Node(Token.GETPROP, new Node(Token.STRING, "Foo.prototype"), oldName),
            new Node(Token.STRING, renamer.getNewName(oldName))));

    renamer.process(null, externs);
    renamer.process(root, null);

    Node renamedExtern = externs.getFirstChild();
    Node renamedRoot = root.getFirstChild();

    String newName = renamer.getNewName(oldName);
    assertEquals(newName, renamedRoot.getLastChild().getString());
    assertEquals(newName, renamedExtern.getLastChild().getString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessorWithNullExterns() {
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);

    renamer.process(null, new Node(Token.PROGRAM));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessorWithNullRoot() {
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);

    renamer.process(new Node(Token.PROGRAM), null);
  }

  private Node createPropertyAccessNode(Node n1, String name) {
    Node n2 = new Node(Token.GETPROP, n1, new Node(Token.STRING, name));
    n1.addChild(n2);
    return n2;
  }

  private String getRenamedName(RenamePrototypes renamer, String name) {
    return renamer.getNewName(name);
  }
}