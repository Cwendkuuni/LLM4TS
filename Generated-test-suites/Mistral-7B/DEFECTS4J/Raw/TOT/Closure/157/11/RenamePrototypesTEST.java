import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerTestBase;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.rhino.Node;

import java.util.Map;

public class RenamePrototypesTest extends CompilerTestBase {

  @org.junit.Test
  public void testRenamePrototypeProperties() {
    String jsCode = "function Foo() {} Foo.prototype.bar = function() {};";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one error", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("only one property to be renamed", 1, renameMap.size());
    assertEquals("actual renaming", "bar => baz", renameMap.entrySet().iterator().next().getValue());

    Node renamedRoot = result.getAST();
    Node renamedBar = getFirstChildOfType(renamedRoot, NodeType.GETPROP, "Foo", "prototype");
    Node renamedBarFunction = getFirstChildOfType(renamedBar, NodeType.GETPROP, "baz");
    assertNotNull("renamed function should exist", renamedBarFunction);
  }

  @org.junit.Test
  public void testDontRenameExternedProperties() {
    String jsCode = "function Foo() {} Foo.prototype.toString = String.prototype.toString;";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one error", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("no properties to be renamed", 0, renameMap.size());

    Node toString = getFirstChildOfType(root, NodeType.GETPROP, "Foo", "prototype", "toString");
    assertNotNull("toString property should exist and not be renamed", toString);
  }

  @org.junit.Test
  public void testDontRenamePrivateProperties() {
    String jsCode = "function Foo() {} function Foo__private() {} Foo.prototype.__private = function() {};";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one error", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("no properties to be renamed", 0, renameMap.size());

    Node __private = getFirstChildOfType(root, NodeType.GETPROP, "Foo", "prototype", "__private");
    assertNotNull("__private property should exist and not be renamed", __private);
  }

  @org.junit.Test
  public void testRenamePrototypePropertiesWithAggressiveRenaming() {
    String jsCode = "function Foo() {} Foo.prototype.prototypeFoo = function() {};";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one error", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("only one property to be renamed", 1, renameMap.size());
    assertEquals("actual renaming", "prototypeFoo => p_0", renameMap.entrySet().iterator().next().getValue());

    Node renamedRoot = result.getAST();
    Node renamedPrototypeFoo = getFirstChildOfType(renamedRoot, NodeType.GETPROP, "Foo", "prototype", "p_0");
    assertNotNull("renamed function should exist", renamedPrototypeFoo);
  }

  @org.junit.Test
  public void testDontRenameBuiltInProperties() {
    String jsCode = "function Foo() {} Foo.prototype.indexOf = Array.prototype.indexOf;";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one warning", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("no properties to be renamed", 0, renameMap.size());

    Node indexOf = getFirstChildOfType(root, NodeType.GETPROP, "Foo", "prototype", "indexOf");
    assertNotNull("indexOf property should exist and not be renamed", indexOf);
  }

  @org.junit.Test
  public void testRenameObjLitProperties() {
    String jsCode = "var o = { bar: 1 };";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one error", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("only one property to be renamed", 1, renameMap.size());
    assertEquals("actual renaming", "bar => bar_0", renameMap.entrySet().iterator().next().getValue());

    Node renamedRoot = result.getAST();
    Node renamedBar = getFirstChildOfType(renamedRoot, NodeType.GETPROPTAG, null, "bar_0");
    assertNotNull("renamed property should exist", renamedBar);
  }

  @org.junit.Test
  public void testDontRenameObjLitPropertiesWithNoQuotes() {
    String jsCode = "var o = { foo: 1 };";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, false, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected no errors or warnings", 0, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("no properties to be renamed", 0, renameMap.size());

    Node foo = getFirstChildOfType(root, NodeType.GETPROPTAG, null, "foo");
    assertNotNull("foo property should exist and not be renamed", foo);
  }

  @org.junit.Test
  public void testRenamePrototypePropertyWithObjLitAssignment() {
    String jsCode = "function Foo() {} Foo.prototype = { bar: function() {} };";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one error", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("only one property to be renamed", 1, renameMap.size());
    assertEquals("actual renaming", "bar => baz", renameMap.entrySet().iterator().next().getValue());

    Node renamedRoot = result.getAST();
    Node renamedBar = getFirstChildOfType(renamedRoot, NodeType.GETPROPTAG, null, "baz");
    assertNotNull("renamed property should exist", renamedBar);
  }

  @org.junit.Test
  public void testRenamePrototypePropertyWithPrototypeAssignment() {
    String jsCode = "function Foo() {} Foo.prototype = function() {};";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one error", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("no properties to be renamed", 0, renameMap.size());

    Node prototype = getFirstChildOfType(root, NodeType.ASSIGN, null, null, "prototype");
    Node functionNode = getFirstChildOfType(prototype, NodeType.FUNCTION);
    assertNotNull("function should exist", functionNode);
  }

  @org.junit.Test
  public void testRenamePrototypePropertiesWithObjLitAssignment() {
    String jsCode = "function Foo() {} Foo.prototype = { bar: function() {}, baz: 1 };";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one error", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("two properties to be renamed", 2, renameMap.size());
    assertEquals("actual renamings", "bar => bar_0, baz => baz_1", renameMap.toString());

    Node renamedRoot = result.getAST();
    Node renamedBar = getFirstChildOfType(renamedRoot, NodeType.GETPROPTAG, null, "bar_0");
    Node renamedBaz = getFirstChildOfType(renamedRoot, NodeType.GETPROPTAG, null, "baz_1");
    assertNotNull("renamed properties should exist", renamedBar, renamedBaz);
  }

  @org.junit.Test
  public void testDontRenamePrototypePropertiesWithObjLitAssignmentAndAggressiveRenaming() {
    String jsCode = "function Foo() {} Foo.prototype = { toString: function() {}, valueOf: function() {} };";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one error", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("no properties to be renamed", 0, renameMap.size());

    Node toString = getFirstChildOfType(root, NodeType.GETPROPTAG, null, "toString");
    Node valueOf = getFirstChildOfType(root, NodeType.GETPROPTAG, null, "valueOf");
    assertNotNull("toString and valueOf properties should exist and not be renamed", toString, valueOf);
  }

  @org.junit.Test
  public void testRenamePrototypePropertyWithPrototypePrototypeAssignment() {
    String jsCode = "function Foo() {} function Foo__proto__() {} Foo.prototype = Foo__proto__;";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one error", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("no properties to be renamed", 0, renameMap.size());

    Node prototypePrototype = getFirstChildOfType(root, NodeType.FUNCTION, null, null, "Foo__proto__");
    Node prototype = getFirstChildOfType(root, NodeType.GETPROP, null, "Foo", "prototype");
    assertNotNull("prototypePrototype and prototype should exist and not be renamed", prototypePrototype, prototype);
  }

  @org.junit.Test
  public void testRenamePrototypePropertiesWithPrototypePrototypeAssignmentAndAggressiveRenaming() {
    String jsCode = "function Foo() {} function Foo__proto__() {} Foo.prototype = Foo__proto__; Foo.prototype.toString = function() {};";

    CompilerOptions options = new CompilerOptions(null);
    CompilerResult result = compile(options, jsCode);
    Node root = result.getAST();

    RenamePrototypes pass = new RenamePrototypes(compiler, true, null, null);
    pass.process(null, root);

    CompilerInput input = result.getTreeMap().getInput();
    assertEquals("expected exactly one error", 1, input.getDiagnostics().length());

    Map<String, String> renameMap = pass.getPropertyMap();
    assertEquals("one property to be renamed", 1, renameMap.size());
    assertEquals("actual renaming", "toString => toString_0", renameMap.entrySet().iterator().next().getValue());

    Node renamedRoot = result.getAST();
    Node renamedToString = getFirstChildOfType(renamedRoot, NodeType.GETPROPTAG, null, "toString_0");
    assertNotNull("renamed property should exist", renamedToString);
  }
}