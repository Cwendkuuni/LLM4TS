package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  private Compiler compiler;
  private JSModule moduleA;
  private JSModule moduleB;
  private CompilerPass crossModuleMethodMotion;
  private IdGenerator idGenerator;

  @Before
  public void setUp() {
    CompilerOptions options = CompilerOptions.defaultOptions();
    compiler = new Compiler(options);
    moduleA = new JSModule("moduleA");
    moduleB = new JSModule("moduleB");
    idGenerator = new CrossModuleMethodMotion.IdGenerator();
    compiler.getModuleGraph().addModule(moduleA);
    compiler.getModuleGraph().addModule(moduleB);
    crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);
  }

  @Test
  public void testMoveMethodWhenDeepestCommonModuleIsNotNull() {
    // Given: A prototype property with a function value that is declared in
    // moduleA and read in moduleB.
    Node functionDefinition = IR.function(
        "functionName", null, null, IR.block(), null);
    moduleA.setPrototype(IR.getprop(null, IR.string("Object")), functionDefinition);

    // When: The method is moved to moduleB.
    crossModuleMethodMotion.process(null, null);

    // Then: The method is replaced with a stub function in moduleA and
    // unstubbed in moduleB.
    Node functionDefinitionInModuleA = moduleA.getPrototype().getChild(0);
    Node stubFunction = functionDefinitionInModuleA.getChild(0);
    assertSame(STUB_METHOD_NAME, stubFunction.getFirstChild().getName().asString());
    Node unstubFunction = moduleB.getPrototype().getChild(0);
    assertSame(functionDefinition, unstubFunction.getChild(0).getChild(0));
  }

  @Test
  public void testMoveMethodWhenDeepestCommonModuleIsNull() {
    // Given: A prototype property with a function value that is not
    // declared in any module.
    Node functionDefinition = IR.function(
        "functionName", null, null, IR.block(), null);

    // When: The method is not moved since the deepest common module is null.
    crossModuleMethodMotion.process(null, null);

    // Then: The method remains unchanged in all modules.
    Node functionDefinitionInModuleA = moduleA.getPrototype().getChild(0);
    Node functionDefinitionInModuleB = moduleB.getPrototype().getChild(0);
    assertNull(functionDefinitionInModuleA.getChild(0));
    assertNull(functionDefinitionInModuleB.getChild(0));
  }

  @Test
  public void testMoveMethodWhenFunctionValueIsGetterOrSetter() {
    // Given: A prototype property with a getter or setter value that is
    // declared in moduleA and read in moduleB.
    Node getterDefinition = IR.getterDef("getterName", null, null);
    moduleA.setPrototype(IR.getprop(null, IR.string("Object")), getterDefinition);

    // When: The getter is not moved since it's not a function.
    crossModuleMethodMotion.process(null, null);

    // Then: The getter remains unchanged in all modules.
    Node getterDefinitionInModuleA = moduleA.getPrototype().getChild(0);
    Node getterDefinitionInModuleB = moduleB.getPrototype().getChild(0);
    assertNull(getterDefinitionInModuleA.getChild(0));
    assertNull(getterDefinitionInModuleB.getChild(0));
  }

  @Test
  public void testMoveMethodWhenGlobalClassIsNotAvailable() {
    // Given: A prototype property with a function value that is declared
    // in moduleA and read in moduleB, but the global class is not
    // available in the top-most module.
    Node functionDefinition = IR.function(
        "functionName", null, null, IR.block(), null);
    moduleA.setPrototype(IR.getprop(null, IR.string("SomeGlobalClass")), functionDefinition);

    // When: The method is not moved since the global class is not
    // available in the top-most module.
    crossModuleMethodMotion.process(null, null);

    // Then: The method remains unchanged in all modules.
    Node functionDefinitionInModuleA = moduleA.getPrototype().getChild(0);
    Node functionDefinitionInModuleB = moduleB.getPrototype().getChild(0);
    assertNull(functionDefinitionInModuleA.getChild(0));
    assertNull(functionDefinitionInModuleB.getChild(0));
  }

  @Test
  public void testGenerateStubsWhenMovingMethods() {
    // Given: Multiple prototype properties with function values that are
    // declared in moduleA and read in moduleB.

    Node functionDefinition1 = IR.function(
        "functionName1", null, null, IR.block(), null);
    moduleA.setPrototype(IR.getprop(null, IR.string("Object")), functionDefinition1);

    Node functionDefinition2 = IR.function(
        "functionName2", null, null, IR.block(), null);
    moduleA.setPrototype(IR.getprop(null, IR.string("Object")), functionDefinition2);

    // When: The methods are moved to moduleB and stubs are generated in
    // the top-most module.
    crossModuleMethodMotion.process(null, null);

    // Then: The methods are replaced with stub functions in moduleA and
    // unstubbed in moduleB, and stub declarations are generated in the
    // top-most module.
    Node stubDeclarations = compiler.getNodeForCodeInsertion(null);
    assertFalse(stubDeclarations.getChild(0).isFunction());
    assertFalse(stubDeclarations.getChild(1).isFunction());

    Node functionDefinitionInModuleA1 = moduleA.getPrototype().getChild(0);
    Node functionDefinitionInModuleB1 = moduleB.getPrototype().getChild(0);
    Node stubFunction1 = functionDefinitionInModuleA1.getChild(0);
    assertSame(STUB_METHOD_NAME, stubFunction1.getFirstChild().getName().asString());
    Node unstubFunction1 = moduleB.getPrototype().getChild(0).getChild(0);
    assertSame(functionDefinition1, unstubFunction1);

    Node functionDefinitionInModuleA2 = moduleA.getPrototype().getChild(1);
    Node functionDefinitionInModuleB2 = moduleB.getPrototype().getChild(1);
    Node stubFunction2 = functionDefinitionInModuleA2.getChild(0);
    assertSame(STUB_METHOD_NAME, stubFunction2.getFirstChild().getName().asString());
    Node unstubFunction2 = moduleB.getPrototype().getChild(1).getChild(0);
    assertSame(functionDefinition2, unstubFunction2);
  }
}