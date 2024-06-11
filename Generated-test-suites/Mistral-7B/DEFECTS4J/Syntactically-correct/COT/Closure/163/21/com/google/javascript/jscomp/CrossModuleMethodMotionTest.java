import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerPass.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CrossModuleMethodMotionTest {

  @Test
  public void testCrossModuleMethodMotion() {
    // Given
    CompilerOptions options = new CompilerOptions(true);
    SourceFile externs = new SourceFile("externs.js");
    SourceFile root = new SourceFile("main.js");
    Compiler compiler = new Compiler(options);
    AbstractCompiler abstractCompiler = new Compiler(options);
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        abstractCompiler, idGenerator, false);

    // When
    root.setParent(compiler);
    pass.process(externs.getTree(), root.getTree());

    // Then
    assertTrue(idGenerator.hasGeneratedAnyIds());

    // Test for null deepest common module error
    try {
      AbstractCompiler compilerWithNullDeepestCommonModule = new Compiler(options);
      CrossModuleMethodMotion nullDeepestCommonModulePass = new CrossModuleMethodMotion(
          compilerWithNullDeepestCommonModule, idGenerator, false);
      SourceFile nullDeepestCommonModuleExterns = new SourceFile("nullExterns.js");
      SourceFile nullDeepestCommonModuleRoot = new SourceFile("nullMain.js");
      nullDeepestCommonModuleRoot.setParent(compilerWithNullDeepestCommonModule);
      nullDeepestCommonModulePass.process(nullDeepestCommonModuleExterns.getTree(),
          nullDeepestCommonModuleRoot.getTree());
      fail("Should have thrown an internal error for null deepest common module");
    } catch (IllegalStateException e) {
      assertEquals(DiagnosticType.ERROR, e.getCause().getType());
      assertTrue(e.getMessage().contains("JSC_INTERNAL_ERROR_MODULE_DEPEND"));
    }

    // Test for unreferenced method
    SourceFile unreferencedMethodExterns = new SourceFile("unreferencedMethodExterns.js");
    SourceFile unreferencedMethodRoot = new SourceFile("unreferencedMethod.js");
    unreferencedMethodRoot.setParent(compiler);
    pass.process(unreferencedMethodExterns.getTree(), unreferencedMethodRoot.getTree());
    Node unreferencedMethod = unreferencedMethodRoot.getTree().getFirstChild();
    assertNull(unreferencedMethod.getDeepestCommonModuleRef());

    // Test for getter or setter
    SourceFile getterSetterExterns = new SourceFile("GetterSetterExterns.js");
    SourceFile getterSetterRoot = new SourceFile("GetterSetter.js");
    getterSetterRoot.setParent(compiler);
    pass.process(getterSetterExterns.getTree(), getterSetterRoot.getTree());
    Node getterSetter = getterSetterRoot.getTree().getFirstChild();
    Node getterSetterPrototype = getterSetter.getFirstChild();
    Node getter = getterSetterPrototype.getFirstChild();
    Node setter = getterSetterPrototype.getLastChild();
    assertFalse(getter.getParent().isFunction());
    assertFalse(setter.getParent().isFunction());

    // Test for method moved from one module to another
    SourceFile methodToMoveExterns = new SourceFile("methodToMoveExterns.js");
    SourceFile methodToMoveRoot = new SourceFile("methodToMove.js");
    methodToMoveRoot.setParent(compiler);
    pass.process(methodToMoveExterns.getTree(), methodToMoveRoot.getTree());
    Node methodToMove = methodToMoveRoot.getTree().getFirstChild();
    JSModule originalModule = methodToMove.getDeepestCommonModuleRef();
    JSModule newModule = methodToMove.getParent().getDeepestCommonModuleRef();
    assertNotSame(originalModule, newModule);

    // Test for method stub declaration
    assertTrue(compiler.getAllNodesByKind( root.getTree(), Node.FUNCTION).isEmpty());
    assertTrue(compiler.getAllNodesByKind(root.getTree(), Node.NAME).isEmpty());
    assertTrue(compiler.getAllNodesByKind(root.getTree(), Node.CALL).isEmpty());
    JSModule topMostModule = compiler.getModuleGraph().getTopModule();
    Node stubFunctionDeclaration = topMostModule.getTree().getFirstChild();
    Node stubFunction = stubFunctionDeclaration.getLastChild();
    assertEquals(STUB_METHOD_NAME, stubFunction.getIdentifier().getName());
    assertTrue(stubFunction.getParent().isVar());
    Node stubMapDeclaration = stubFunctionDeclaration.getParent();
    Node stubMap = stubMapDeclaration.getLastChild();
    assertEquals("JSCompiler_stubMap", stubMap.getIdentifier().getName());
    assertTrue(stubMap.isVar());
    assertTrue(compiler.getAllNodesByKind(root.getTree(), Node.FUNCTION).isEmpty());
    assertTrue(compiler.getAllNodesByKind(root.getTree(), Node.NAME).isEmpty());
    assertTrue(compiler.getAllNodesByKind(root.getTree(), Node.CALL).isEmpty());

    // Test for method unstubbing in the deeper module
    Node unstubbedMethod = compiler.getNodeForCodeInsertion(newModule).getLastChild();
    assertTrue(unstubbedMethod.isFunction());
    assertEquals(methodToMove.getIdentifier().getName(),
        unstubbedMethod.getIdentifier().getName());
  }
}