import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);

    CharSequence sourceCode = "function Foo() {} Foo.prototype.bar = function() {};";
    CompilerInput input = new CompilerInput(sourceCode, null);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);

    Node root = Node.externalInputToRoot(input);
    renamePrototypes.process(input.getExterns(), root);

    Node barFunction = findNodeByTypeAndName(root, Node.FUNCTION, "bar");
    assertEquals("bar_", barFunction.getString());

    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("bar", propertyMap.lookupOldName("bar_"));
    assertEquals("bar_", propertyMap.lookupNewName("bar"));
  }

  @Test
  public void testAggressiveRenaming() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);

    CharSequence sourceCode = "function Foo() {} Foo.prototype.Baz = function() {};";
    CompilerInput input = new CompilerInput(sourceCode, null);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    Node root = Node.externalInputToRoot(input);
    renamePrototypes.process(input.getExterns(), root);

    Node barFunction = findNodeByTypeAndName(root, Node.FUNCTION, "Baz");
    assertEquals("Baz__", barFunction.getString());

    VariableMap propertyMap = renamePrototypes.getPropertyMap();
    assertEquals("Baz", propertyMap.lookupOldName("Baz__"));
    assertEquals("Baz__", propertyMap.lookupNewName("Baz"));
  }

  @Test
  public void testNoRenaming() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);

    CharSequence sourceCode = "function Foo() {} Foo.prototype.indexOf = function() {};";
    CompilerInput input = new CompilerInput(sourceCode, null);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);

    Node root = Node.externalInputToRoot(input);
    renamePrototypes.process(input.getExterns(), root);

    Node indexOfFunction = findNodeByTypeAndName(root, Node.FUNCTION, "indexOf");
    assertEquals("indexOf", indexOfFunction.getString());
  }

  @Test
  public void testPrivateRenaming() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);

    CharSequence sourceCode = "function Foo() { var _bar = 0; }";
    CompilerInput input = new CompilerInput(sourceCode, null);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);

    Node root = Node.externalInputToRoot(input);
    renamePrototypes.process(input.getExterns(), root);

    Node _barVariable = findNodeByTypeAndName(root, Node.VAR, "_bar");
    assertEquals("_bar", _barVariable.getString());
  }

  @Test
  public void testExistingNameInExterns() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED);

    CharSequence sourceCode = "function Foo() {} Foo.prototype.bar = function() {};"
        + "var bar = 0;";
    CompilerInput input = new CompilerInput(sourceCode, null);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);

    Node root = Node.externalInputToRoot(input);
    renamePrototypes.process(input.getExterns(), root);

    Node barVariable = findNodeByTypeAndName(root, Node.VAR, "bar");
    assertEquals("bar", barVariable.getString());
  }

  @Test(expected = IllegalStateException.class)
  public void testTraversalAfterObfuscated() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());
    when(compiler.getLifeCycleStage()).thenReturn(LifeCycleStage.NORMALIZED_OBFUSCATED);

    CharSequence sourceCode = "function Foo() {}";
    CompilerInput input = new CompilerInput(sourceCode, null);

    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    renamePrototypes.process(input.getExterns(), Node.externalInputToRoot(input));

    compiler.setLifeCycleStage(LifeCycleStage.NORMALIZED);
    renamePrototypes.process(input.getExterns(), Node.externalInputToRoot(input));
  }

  private Node findNodeByTypeAndName(Node node, int nodeType, String nodeName) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.getType() == nodeType && child.getString().equals(nodeName)) {
        return child;
      }
    }
    return null;
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