import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenamePrototypesTest {

  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    renamePrototypes = new RenamePrototypes(null, false, null, null);
  }

  @Test
  public void testPropertyCount() {
    RenamePrototypes.Property property = renamePrototypes.new Property("testName");
    assertEquals(0, property.count());
  }

  @Test
  public void testCanRenamePrototypeProperty() {
    RenamePrototypes.Property property = renamePrototypes.new Property("testName");
    assertTrue(property.canRenamePrototypeProperty());
  }

  @Test
  public void testCanRenameObjLitProperty() {
    RenamePrototypes.Property property = renamePrototypes.new Property("testName");
    assertFalse(property.canRenameObjLitProperty());
  }

  @Test
  public void testGetPropertyNotFound() {
    assertNull(renamePrototypes.getProperty("testName"));
  }

  @Test
  public void testGetPropertyFound() {
    RenamePrototypes.Property property = renamePrototypes.new Property("testName");
    renamePrototypes.properties.put("testName", property);
    assertEquals(property, renamePrototypes.getProperty("testName"));
  }

  @Test
  public void testProcessProperties() {
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node node = new Node(Token.GETPROP);
    node.addChildToBack(new Node(Token.STRING, "testName"));
    renamePrototypes.processProperties.visit(traversal, node, null);
    RenamePrototypes.Property property = renamePrototypes.getProperty("testName");
    assertNotNull(property);
    assertEquals(0, property.prototypeCount);
    assertEquals(0, property.objLitCount);
    assertEquals(1, property.refCount);
  }

  @Test
  public void testProcessPrototypeParent() {
    NodeTraversal traversal = new NodeTraversal(null, null);
    Node node = new Node(Token.GETPROP);
    node.addChildToBack(new Node(Token.STRING, "testName"));
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToBack(node);
    renamePrototypes.processPrototypeParent(parent, null);
    RenamePrototypes.Property property = renamePrototypes.getProperty("testName");
    assertNotNull(property);
    assertEquals(1, property.prototypeCount);
    assertEquals(0, property.objLitCount);
    assertEquals(0, property.refCount);
  }

  @Test
  public void testMarkPrototypePropertyCandidate() {
    Node node = new Node(Token.STRING, "testName");
    renamePrototypes.markPrototypePropertyCandidate(node, null);
    RenamePrototypes.Property property = renamePrototypes.getProperty("testName");
    assertNotNull(property);
    assertEquals(1, property.prototypeCount);
  }

  @Test
  public void testMarkObjLitPropertyCandidate() {
    Node node = new Node(Token.STRING, "testName");
    renamePrototypes.markObjLitPropertyCandidate(node, null);
    RenamePrototypes.Property property = renamePrototypes.getProperty("testName");
    assertNotNull(property);
    assertEquals(1, property.objLitCount);
  }

  @Test
  public void testMarkPropertyAccessCandidate() {
    Node node = new Node(Token.STRING, "testName");
    renamePrototypes.markPropertyAccessCandidate(node, null);
    RenamePrototypes.Property property = renamePrototypes.getProperty("testName");
    assertNotNull(property);
    assertEquals(1, property.refCount);
  }
}