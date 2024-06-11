import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypeWithAggressiveRenaming() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5);
    options.setTarget(CompilerOptions.Output.FORMAT_DEFAULT);
    options.addCompilerPass(new RenamePrototypes(null, true, null, null));

    Compiler compiler = new Compiler();
    compiler.init(options);

    CompilerInput input = new CompilerInput("test.js", "test.js");
    input.setSourceText("function Foo() {} Foo.prototype.oldBar = function() {};");

    compiler.compile(input, null, null);

    VariableMap renameMap = ((RenamePrototypes) compiler.getPassManager()
        .getPass(RenamePrototypes.class)).getPropertyMap();

    assertNotNull(renameMap);
    String newName = renameMap.lookupNewName("oldBar");
    assertNotNull(newName);
    assertTrue(newName.length() > 0);
    assertFalse(newName.equals("oldBar"));

    input = new CompilerInput("test.js", "test.js");
    input.setSourceText("function Foo() {} Foo.prototype.oldBar = function() {};");
    compiler.compile(input, null, null);

    renameMap = ((RenamePrototypes) compiler.getPassManager()
        .getPass(RenamePrototypes.class)).getPropertyMap();

    newName = renameMap.lookupNewName("oldBar");
    assertNotNull(newName);
    assertTrue(newName.length() > 0);
    assertFalse(newName.equals("oldBar"));
  }

  @Test
  public void testRenamePrototypeWithDefaultRenaming() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5);
    options.setTarget(CompilerOptions.Output.FORMAT_DEFAULT);
    options.addCompilerPass(new RenamePrototypes(null, false, null, null));

    Compiler compiler = new Compiler();
    compiler.init(options);

    CompilerInput input = new CompilerInput("test.js", "test.js");
    input.setSourceText("function Foo() {} Foo.prototype.oldBar = function() {};");

    compiler.compile(input, null, null);

    VariableMap renameMap = ((RenamePrototypes) compiler.getPassManager()
        .getPass(RenamePrototypes.class)).getPropertyMap();

    assertNotNull(renameMap);
    String newName = renameMap.lookupNewName("oldBar");

    if (newName.matches("^[a-z]+$")) {
      // The property name contains only lowercase letters, so it should not be renamed
      assertNull(newName);
    } else {
      assertNotNull(newName);
      assertTrue(newName.length() > 0);
      assertFalse(newName.equals("oldBar"));
    }
  }

  @Test
  public void testRenamePrototypeWithPrivateName() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5);
    options.setTarget(CompilerOptions.Output.FORMAT_DEFAULT);
    options.addCompilerPass(new RenamePrototypes(null, false, null, null));

    Compiler compiler = new Compiler();
    compiler.init(options);

    CompilerInput input = new CompilerInput("test.js", "test.js");
    input.setSourceText("function Foo() {} Foo.prototype._oldBar = function() {};");

    compiler.compile(input, null, null);

    VariableMap renameMap = ((RenamePrototypes) compiler.getPassManager()
        .getPass(RenamePrototypes.class)).getPropertyMap();

    assertNotNull(renameMap);
    String newName = renameMap.lookupNewName("_oldBar");
    assertNotNull(newName);
    assertTrue(newName.length() > 0);
    assertFalse(newName.equals("_oldBar"));
  }

  @Test
  public void testRenamePrototypeWithReservedName() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5);
    options.setTarget(CompilerOptions.Output.FORMAT_DEFAULT);
    options.addCompilerPass(new RenamePrototypes(null, false, null, null));

    Compiler compiler = new Compiler();
    compiler.init(options);

    CompilerInput input = new CompilerInput("test.js", "test.js");
    input.setSourceText("function Foo() {} Foo.prototype.indexOf = function() {};");

    compiler.compile(input, null, null);

    VariableMap renameMap = ((RenamePrototypes) compiler.getPassManager()
        .getPass(RenamePrototypes.class)).getPropertyMap();

    String newName = renameMap.lookupNewName("indexOf");
    assertNull(newName);
  }

  @Test
  public void testRenameObjLitProperty() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5);
    options.setTarget(CompilerOptions.Output.FORMAT_DEFAULT);
    options.addCompilerPass(new RenamePrototypes(null, false, null, null));

    Compiler compiler = new Compiler();
    compiler.init(options);

    CompilerInput input = new CompilerInput("test.js", "test.js");
    input.setSourceText("var o = { oldBar: 1 };");

    compiler.compile(input, null, null);

    VariableMap renameMap = ((RenamePrototypes) compiler.getPassManager()
        .getPass(RenamePrototypes.class)).getPropertyMap();

    String newName = renameMap.lookupNewName("oldBar");

    if (newName.matches("^[a-z]+$")) {
      // The property name contains only lowercase letters, so it should not be renamed
      assertNull(newName);
    } else {
      assertNotNull(newName);
      assertTrue(newName.length() > 0);
      assertFalse(newName.equals("oldBar"));
    }
  }

  @Test
  public void testRenameObjLitPropertyWithReservedName() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5);
    options.setTarget(CompilerOptions.Output.FORMAT_DEFAULT);
    options.addCompilerPass(new RenamePrototypes(null, false, null, null));

    Compiler compiler = new Compiler();
    compiler.init(options);

    CompilerInput input = new CompilerInput("test.js", "test.js");
    input.setSourceText("var o = { indexOf: 1 };");

    compiler.compile(input, null, null);

    VariableMap renameMap = ((RenamePrototypes) compiler.getPassManager()
        .getPass(RenamePrototypes.class)).getPropertyMap();

    String newName = renameMap.lookupNewName("indexOf");
    assertNull(newName);
  }

  @Test
  public void testRenamePrototypeWithAggressiveRenamingAndPrototypeAssignment() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5);
    options.setTarget(CompilerOptions.Output.FORMAT_DEFAULT);
    options.addCompilerPass(new RenamePrototypes(null, true, null, null));

    Compiler compiler = new Compiler();
    compiler.init(options);

    CompilerInput input = new CompilerInput("test.js", "test.js");
    input.setSourceText("function Foo() {} Foo.prototype = { oldBar: function() {} };");

    compiler.compile(input, null, null);

    VariableMap renameMap = ((RenamePrototypes) compiler.getPassManager()
        .getPass(RenamePrototypes.class)).getPropertyMap();

    String newName = renameMap.lookupNewName("oldBar");

    if (newName.matches("^[a-z]+$")) {
      // The property name contains only lowercase letters, so it should be renamed
      assertNotNull(newName);
      assertTrue(newName.length() > 0);
      assertFalse(newName.equals("oldBar"));
    } else {
      assertNull(newName);
    }
  }

  @Test
  public void testRenamePrototypeWithDefaultRenamingAndPrototypeAssignment() {
    CompilerOptions options = new CompilerOptions();
    options.setLanguageIn(CompilerOptions.Language.ECMASCRIPT5);
    options.setTarget(CompilerOptions.Output.FORMAT_DEFAULT);
    options.addCompilerPass(new RenamePrototypes(null, false, null, null));

    Compiler compiler = new Compiler();
    compiler.init(options);

    CompilerInput input = new CompilerInput("test.js", "test.js");
    input.setSourceText("function Foo() {} Foo.prototype = { oldBar: function() {} };");

    compiler.compile(input, null, null);

    VariableMap renameMap = ((RenamePrototypes) compiler.getPassManager()
        .getPass(RenamePrototypes.class)).getPropertyMap();

    String newName = renameMap.lookupNewName("oldBar");

    if (newName.matches("^[a-z]+$")) {
      // The property name contains only lowercase letters, so it should not be renamed
      assertNull(newName);
    } else {
      assertNotNull(newName);
      assertTrue(newName.length() > 0);
      assertFalse(newName.equals("oldBar"));
    }
  }
}