import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;
import com.google.common.base.Preconditions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

import javax.annotation.Nullable;

public class RenamePrototypesTest {

  private AbstractCompiler compiler;
  private RenamePrototypes renamePrototypes;
  private VariableMap prevUsedRenameMap;
  private NodeTraversal nodeTraversal;
  private CompilerInput input;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    prevUsedRenameMap = mock(VariableMap.class);
    nodeTraversal = mock(NodeTraversal.class);
    input = new CompilerInput("test.js", "test.js", null);

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testProcess_ExistingProperties_RenameProperties() {
    // Given
    Node rootNode = createTestNode("test.js", "function Foo() {} Foo.prototype.bar = function() {}");
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());

    // When
    renamePrototypes.process(createExternsNode(), rootNode);

    // Then
    ArgumentCaptor<CompilerPass> captor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(compiler).addPass(captor.capture());
    CompilerPass passedPass = captor.getValue();
    assertEquals(RenamePrototypes.class, passedPass.getClass());

    Property expectedProperty = new Property("bar");
    expectedProperty.newName = "_bar";

    verify(compiler).setLifeCycleStage(LifeCycleStage.NORMALIZED_OBFUSCATED);
    assertEquals(1, renamePrototypes.properties.size());
    Property actualProperty = renamePrototypes.properties.get("bar");
    assertEquals(expectedProperty.oldName, actualProperty.oldName);
    assertEquals(expectedProperty.newName, actualProperty.newName);
  }

  @Test
  public void testProcess_NewProperties_RenameProperties() {
    // Given
    Node rootNode = createTestNode("test.js", "function Foo() { this.baz = 42; }");
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());

    // When
    renamePrototypes.process(createExternsNode(), rootNode);

    // Then
    ArgumentCaptor<CompilerPass> captor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(compiler).addPass(captor.capture());
    CompilerPass passedPass = captor.getValue();
    assertEquals(RenamePrototypes.class, passedPass.getClass());

    Property expectedProperty = new Property("baz");
    expectedProperty.newName = "_baz";

    verify(compiler).setLifeCycleStage(LifeCycleStage.NORMALIZED_OBFUSCATED);
    assertEquals(1, renamePrototypes.properties.size());
    Property actualProperty = renamePrototypes.properties.get("baz");
    assertEquals(expectedProperty.oldName, actualProperty.oldName);
    assertEquals(expectedProperty.newName, actualProperty.newName);
  }

  @Test
  public void testProcess_PrivateProperties_DoNotRename() {
    // Given
    Node rootNode = createTestNode("test.js", "function Foo() { var _bar = 42; }");
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());

    // When
    renamePrototypes.process(createExternsNode(), rootNode);

    // Then
    ArgumentCaptor<CompilerPass> captor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(compiler).addPass(captor.capture());
    CompilerPass passedPass = captor.getValue();
    assertEquals(RenamePrototypes.class, passedPass.getClass());

    Property expectedProperty = new Property("_bar");
    expectedProperty.newName = "_bar";

    verify(compiler).setLifeCycleStage(LifeCycleStage.NORMALIZED_OBFUSCATED);
    assertEquals(1, renamePrototypes.properties.size());
    Property actualProperty = renamePrototypes.properties.get("_bar");
    assertEquals(expectedProperty.oldName, actualProperty.oldName);
    assertEquals(expectedProperty.newName, actualProperty.newName);
  }

  @Test
  public void testProcess_ReservedNames_DoNotRename() {
    // Given
    Node rootNode = createTestNode("test.js", "function Foo() { IndexOf(1, 2); }");
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());

    // When
    renamePrototypes.process(createExternsNode(), rootNode);

    // Then
    ArgumentCaptor<CompilerPass> captor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(compiler).addPass(captor.capture());
    CompilerPass passedPass = captor.getValue();
    assertEquals(RenamePrototypes.class, passedPass.getClass());

    // Properties should not be renamed
    assertEquals(0, renamePrototypes.properties.size());
  }

  // Add more test cases here for edge cases and exception handling scenarios

  private Node createTestNode(String fileName, String sourceCode) {
    TokenStream tokenStream = new TokenStream(new StringReader(sourceCode));
    return new Node(new CompilerContext(), tokenStream);
  }

  private Node createExternsNode() {
    return new Node(new CompilerContext(), new StringReader(""));
  }

  private static class CodingConvention {
    public boolean isExported(String name) {
      return false;
    }

    public boolean isPrivate(String name) {
      return name.startsWith("_");
    }
  }
}