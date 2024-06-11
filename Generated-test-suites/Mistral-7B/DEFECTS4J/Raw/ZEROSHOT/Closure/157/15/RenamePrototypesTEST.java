import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class RenamePrototypesTest {

  private AbstractCompiler compiler;
  private NodeTraversal traversal;
  private CompilerPass renamePrototypes;
  private VariableMap prevUsedRenameMap;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    traversal = mock(NodeTraversal.class);
    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    prevUsedRenameMap = mock(VariableMap.class);
  }

  @Test
  public void testProcessNormalCase() {
    CompilerInput input = createInput("Foo.prototype.bar = function() { return 1; }");
    renamePrototypes.process(null, input.getRootNode());

    ArgumentCaptor<VariableMap> mapCaptor = ArgumentCaptor.forClass(VariableMap.class);
    assertEquals(RenamePrototypes.class, compiler.getPassManager().getPass(LifeCycleStage.NORMALIZED).getImplementationClass());
    when(compiler.getPassManager().getPass(LifeCycleStage.NORMALIZED)).thenReturn(renamePrototypes);
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));
    when(compiler.reportCodeChange()).thenReturn(mock(CodeChangeReporter.class));

    // Verify that the property was renamed.
    Property property = getProperty("bar");
    assertEquals("bar", property.oldName);
    assertNotNull(property.newName);
    assertEquals("renamed_bar", property.newName);
  }

  @Test
  public void testProcessAggressiveRenamingCase() {
    RenamePrototypes aggressiveRenamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), true, null, null);

    CompilerInput input = createInput("Foo.prototype.bar = function() { return 1; }");
    aggressiveRenamePrototypes.process(null, input.getRootNode());

    // Verify that the property was renamed aggressively.
    Property property = getProperty("bar");
    assertEquals("bar", property.oldName);
    assertNotNull(property.newName);
    assertEquals("aggressively_renamed_bar", property.newName);
  }

  @Test
  public void testProcessReservedNameCase() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);

    CompilerInput input = createInput("Foo.prototype.indexOf = function() { return 1; }");
    renamePrototypes.process(null, input.getRootNode());

    // Verify that the reserved name was not renamed.
    Property property = getProperty("indexOf");
    assertEquals("indexOf", property.oldName);
    assertNull(property.newName);
  }

  @Test
  public void testProcessPrivateNameCase() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);

    CompilerInput input = createInput("Foo.__private_bar = function() { return 1; }");
    renamePrototypes.process(null, input.getRootNode());

    // Verify that the private name was renamed.
    Property property = getProperty("__private_bar");
    assertEquals("__private_bar", property.oldName);
    assertNotNull(property.newName);
    assertEquals("__private_renamed_bar", property.newName);
  }

  @Test
  public void testProcessAggressivePrivateNameCase() {
    RenamePrototypes aggressiveRenamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), true, null, null);

    CompilerInput input = createInput("Foo.__private_bar = function() { return 1; }");
    aggressiveRenamePrototypes.process(null, input.getRootNode());

    // Verify that the private name was renamed aggressively.
    Property property = getProperty("__private_bar");
    assertEquals("__private_bar", property.oldName);
    assertNotNull(property.newName);
    assertEquals("__aggressively_renamed_private_bar", property.newName);
  }

  @Test
  public void testReusePrototypeNames() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, createUsedRenameMap());

    renamePrototypes.process(null, createInput("Foo.prototype.bar = function() { return 1; }").getRootNode());
    renamePrototypes.process(null, createInput("Foo.prototype.bar = function() { return 2; }").getRootNode());

    // Verify that the name was reused.
    Property property = getProperty("bar");
    assertEquals("bar", property.oldName);
    assertNotNull(property.newName);
    assertEquals("renamed_bar", property.newName);
  }

  @Test
  public void testGenerateNewNames() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);

    // Verify that new names are generated when none are available.
    renamePrototypes.process(null, createInput("Foo.prototype.newName = function() { return 1; }").getRootNode());

    // Verify that the new name starts with a valid character.
    Property property = getProperty("newName");
    assertEquals("newName", property.oldName);
    assertNotNull(property.newName);
    assertEquals("newName_", property.newName);

    // Verify that names are not generated for externed properties.
    renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);
    renamePrototypes.process(null, createInput("Foo.prototype.toString = function() { return 1; }").getRootNode());

    property = getProperty("toString");
    assertEquals("toString", property.oldName);
    assertNull(property.newName);
  }

  @Test
  public void testProcessObjLitProperties() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);

    CompilerInput input = createInput("{ foo : 1, bar : 2 }");
    renamePrototypes.process(null, input.getRootNode());

    // Verify that obj lit properties are not renamed.
    for (Node n = input.getRootNode(); n != null; n = n.getNext()) {
      if (n.getType() == Token.OBJECTLIT) {
        for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
          if (child.getType() == Token.STRING) {
            Property property = getProperty(child.getString());
            assertEquals(child.getString(), property.oldName);
            assertNull(property.newName);
          }
        }
      }
    }
  }

  @Test
  public void testProcessPrototypeAssignments() {
    RenamePrototypes renamePrototypes = new RenamePrototypes(mock(AbstractCompiler.class), false, null, null);

    CompilerInput input = createInput("Foo.prototype = { getBar : function() { return 1; } }");
    renamePrototypes.process(null, input.getRootNode());

    // Verify that prototype assignments are processed correctly.
    for (Node n = input.getRootNode(); n != null; n = n.getNext()) {
      if (n.getType() == Token.ASSIGN) {
        if (n.getFirstChild().getType() == Token.IDENT && n.getFirstChild().getString().equals("Foo")) {
          if (n.getLastChild().getType() == Token.OBJECTLIT) {
            for (Node child = n.getLastChild().getFirstChild(); child != null; child = child.getNext()) {
              if (child.getType() == Token.STRING) {
                Property property = getProperty(child.getString());
                assertEquals(child.getString(), property.oldName);
                assertNotNull(property.newName);
                assertEquals("renamed_property_" + getPropertyIndex(property), property.newName);
              }
            }
          }
        }
      }
    }
  }

  private int getPropertyIndex(Property property) {
    int count = 0;
    for (Property prop : renamePrototypes.properties.values()) {
      if (prop == property) {
        return count;
      }
      count++;
    }
    throw new RuntimeException("Unknown property: " + property.oldName);
  }

  private VariableMap createUsedRenameMap() {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("bar", "renamed_bar");
    return new VariableMap(map);
  }

  private CompilerInput createInput(String code) {
    TokenStream ts = new TokenStream(new StringReader(code));
    return new CompilerInput(ts, null);
  }

  private Property getProperty(String name) {
    return renamePrototypes.properties.get(name);
  }
}