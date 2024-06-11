import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.LifeCycleStage;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {

  private AbstractCompiler compiler;
  private RenamePrototypes renamePrototypes;
  private VariableMap prevUsedRenameMap;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    prevUsedRenameMap = mock(VariableMap.class);

    when(compiler.getCodingConvention()).thenReturn(mock(AbstractCompiler.CodingConvention.class));
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);

    renamePrototypes = new RenamePrototypes(compiler, false, null, prevUsedRenameMap);
  }

  @Test
  public void testProcess_ExternalsAreNotRenamed() {
    CompilerInput input = mock(CompilerInput.class);
    Node externs = mock(Node.class);
    Node root = mock(Node.class);

    when(compiler.getInputForSourceFile("Externals.js")).thenReturn(input);

    renamePrototypes.process(externs, root);

    assertEquals(0, renamePrototypes.properties.size());
  }

  @Test
  public void testProcess_PropertiesAreRenamedIfNotExportedOrPrivate() {
    CompilerInput input = mock(CompilerInput.class);
    Node externs = mock(Node.class);
    Node root = mock(Node.class);

    String oldName = "oldName";
    String newName = "newName";

    when(compiler.getInputForSourceFile("Source.js")).thenReturn(input);

    Node getProp = mock(Node.class);
    Node string = mock(Node.class);
    when(getProp.getFirstChild()).thenReturn(string);
    when(string.getString()).thenReturn(oldName);

    when(root.getFirstChild()).thenReturn(getProp);

    Property property = new Property(oldName);
    renamePrototypes.properties.put(oldName, property);

    renamePrototypes.process(externs, root);

    assertEquals(newName, property.newName);
  }

  @Test
  public void testProcess_AggressiveRenamingIsEnabled() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    String oldName = "oldName";
    String newName = "newName";

    renamePrototypes.process(mock(Node.class), mock(Node.class));

    assertEquals(newName, renamePrototypes.properties.get(oldName).newName);
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_LifeCycleStageIsNotNormalized() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);

    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZING);

    renamePrototypes.process(mock(Node.class), mock(Node.class));
  }
}