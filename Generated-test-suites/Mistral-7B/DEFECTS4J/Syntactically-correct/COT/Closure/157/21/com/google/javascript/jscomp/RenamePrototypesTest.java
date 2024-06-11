import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Enum.TRAVERSE;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.VariableMap;

import org.junit.Test;

public class RenamePrototypesTest {
  @Test
  public void testRenamePrototypes() {
    

    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionTypesForCast(false);
    options.setAllowAllGeneratedNames(true);
    options.setAggressiveRenaming(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String sourceCode = "function Foo() {} Foo.prototype.bar = function() {};";

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    Node root = new Node(Token.PROGRAM, null);
    CompilerInput input = new CompilerInput("test", sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.setCallback(TRAVERSE, new RenamePrototypesTest_RenameCallback(), null);
    traversal.process(input);

    String renamedSourceCode = compiler.getDebugLog().toString();
    assertEquals("Expected the source code to be renamed", sourceCodeWithRenames(sourceCode), renamedSourceCode);

    
  }

  @Test
  public void testNoRenameForExportedProperties() {
    

    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionTypesForCast(false);
    options.setAggressiveRenaming(false);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String sourceCode = "function Foo() { export function bar() {}; }";

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    Node root = new Node(Token.PROGRAM, null);
    CompilerInput input = new CompilerInput("test", sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.setCallback(TRAVERSE, new RenamePrototypesTest_RenameCallback(), null);
    traversal.process(input);

    String renamedSourceCode = compiler.getDebugLog().toString();
    assertEquals("Expected no renaming for exported properties", sourceCode, renamedSourceCode);

    
  }

  @Test
  public void testNoRenameForPrivateProperties() {
    

    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionTypesForCast(false);
    options.setAggressiveRenaming(false);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String sourceCode = "function Foo() { var bar = function() {}; }";

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    Node root = new Node(Token.PROGRAM, null);
    CompilerInput input = new CompilerInput("test", sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.setCallback(TRAVERSE, new RenamePrototypesTest_RenameCallback(), null);
    traversal.process(input);

    String renamedSourceCode = compiler.getDebugLog().toString();
    assertEquals("Expected no renaming for private properties", sourceCode, renamedSourceCode);

    
  }

  @Test
  public void testRenameAggressively() {
    

    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionTypesForCast(false);
    options.setAggressiveRenaming(true);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String sourceCode = "function Foo() {} Foo.prototype.myFunction = function() {};";

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    Node root = new Node(Token.PROGRAM, null);
    CompilerInput input = new CompilerInput("test", sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.setCallback(TRAVERSE, new RenamePrototypesTest_RenameCallback(), null);
    traversal.process(input);

    String renamedSourceCode = compiler.getDebugLog().toString();
    String expectedRenamedSourceCode = "function Foo() {} Foo.prototype._myFunction_ = function() {};";
    assertEquals("Expected the source code to be renamed aggressively", expectedRenamedSourceCode, renamedSourceCode);

    
  }

  @Test
  public void testRenameWithReservedNames() {
    

    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionTypesForCast(false);
    options.setAggressiveRenaming(false);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String sourceCode = "function Foo() { this.indexOf = 0; }";

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    Node root = new Node(Token.PROGRAM, null);
    CompilerInput input = new CompilerInput("test", sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.setCallback(TRAVERSE, new RenamePrototypesTest_RenameCallback(), null);
    traversal.process(input);

    String renamedSourceCode = compiler.getDebugLog().toString();
    assertEquals("Expected the source code to keep reserved names", sourceCode, renamedSourceCode);

    
  }

  @Test
  public void testRenameWithObjsLit() {
    

    CompilerOptions options = new CompilerOptions(null);
    options.setAssumeFunctionTypesForCast(false);
    options.setAggressiveRenaming(false);
    AbstractCompiler compiler = new AbstractCompiler(options);

    String sourceCode = "function Foo() {} Foo.prototype = { getBar : function() {} };";

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, false, null, null);
    Node root = new Node(Token.PROGRAM, null);
    CompilerInput input = new CompilerInput("test", sourceCode, null, null);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.setCallback(TRAVERSE, new RenamePrototypesTest_RenameCallback(), null);
    traversal.process(input);

    String renamedSourceCode = compiler.getDebugLog().toString();
    String expectedRenamedSourceCode = "function Foo() {} Foo.prototype = { get_getBar_ : function() {} };";
    assertEquals("Expected the source code to be renamed with objs lit", expectedRenamedSourceCode, renamedSourceCode);

    
  }

  private static String sourceCodeWithRenames(String sourceCode) {
    Map<String, String> renameMap = new RenamePrototypes(null, false, null, null).getPropertyMap().asMap();
    for (Map.Entry<String, String> entry : renameMap.entrySet()) {
      sourceCode = sourceCode.replaceAll(entry.getKey(), entry.getValue());
    }
    return sourceCode;
  }

  private static class RenamePrototypesTest_RenameCallback extends NodeTraversal.AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (node instanceof CompilerInput) {
        CompilerInput input = (CompilerInput) node;
        input.setInput(sourceCodeWithRenames(input.getInput()));
      }
    }
  }
}