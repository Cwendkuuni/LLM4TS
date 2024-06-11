import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Compiler.compile;
import static com.google.javascript.jscomp.Compiler.getDefaultCompilerOptions;
import static com.google.javascript.rhino.NodeTree.*;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversingTreePrinter;
import com.google.javascript.jscomp.test.BaseCompilerTestCase;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

/**
 * Tests for the CrossModuleMethodMotion class.
 */
public class CrossModuleMethodMotionTest extends BaseCompilerTestCase {

  @Test
  public void testCrossModuleMethodMotion() {
    CompilerOptions options = getDefaultCompilerOptions();
    options.setClassToProcess("com.test.CrossModuleMethodMotionTest");

    SourceFile sourceFile = createSourceFile("CrossModuleMethodMotionTest.js",
        "class A {\n"
            + "  static b() {}\n"
            + "}\n"
            + "\n"
            + "class B extends A {}\n"
            + "\n"
            + "class C extends A {}\n");

    ListMultimap<Integer, DiagnosticType> diagnostics = compile(sourceFile,
        options);

    assertEquals(0, diagnostics.size());

    Node root = sourceFile.getTree();
    Node compilerRoot = compiler.getRootNode(root);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    pass.process(null, compilerRoot);

    Node bMethodA = findMethod(compilerRoot, "A", "b");
    Node bMethodB = findMethod(compilerRoot, "B", "b");

    assertNotNull(bMethodA);
    assertNotNull(bMethodB);

    // The original method A.b() should be replaced with a stub function
    Node expectedStubFunction = IR.function(
        IR.params(), IR.block(IR.returnStatement(undefined())));

    Node actualStubFunction = bMethodA;
    assertEquals(expectedStubFunction.toString(), actualStubFunction.toString());

    // The method B.b() should be moved to the deeper module C
    Node expectedBMethodC = IR.function(
        IR.params(), IR.block(IR.returnStatement(undefined())));
    Node actualBMethodC = bMethodB;
    assertEquals(expectedBMethodC.toString(), actualBMethodC.toString());
  }

  @Test
  public void testCrossModuleMethodMotionWithExterns() {
    CompilerOptions options = getDefaultCompilerOptions();
    options.setClassToProcess("com.test.CrossModuleMethodMotionTest");
    options.setExterns("externs.js");

    SourceFile sourceFile = createSourceFile("CrossModuleMethodMotionTest.js",
        "/**\n"
            + "@extern C.prototype.a = function() {}\n"
            + "*/\n"
            + "\n"
            + "class A {\n"
            + "  static b() {}\n"
            + "}\n"
            + "\n"
            + "class B extends A {}\n"
            + "\n"
            + "class C {}\n");

    ListMultimap<Integer, DiagnosticType> diagnostics = compile(sourceFile,
        options);

    assertEquals(0, diagnostics.size());

    Node root = sourceFile.getTree();
    Node compilerRoot = compiler.getRootNode(root);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    pass.process(null, compilerRoot);

    Node bMethodA = findMethod(compilerRoot, "A", "b");
    Node bMethodB = findMethod(compilerRoot, "B", "b");
    Node aMethodC = findMethod(compilerRoot, "C", "a");

    assertNotNull(bMethodA);
    assertNotNull(bMethodB);
    assertNotNull(aMethodC);

    // The original method A.b() should be replaced with a stub function
    Node expectedStubFunction = IR.function(
        IR.params(), IR.block(IR.returnStatement(undefined())));

    Node actualStubFunction = bMethodA;
    assertEquals(expectedStubFunction.toString(), actualStubFunction.toString());

    // The method B.b() should be moved to the deeper module C
    Node expectedBMethodC = IR.function(
        IR.params(), IR.block(IR.returnStatement(undefined())));
    Node actualBMethodC = bMethodB;
    assertEquals(expectedBMethodC.toString(), actualBMethodC.toString());

    // The method C.a() should not be moved since it's declared in externs
    Node expectedAMethodC = aMethodC;
    Node actualAMethodC = findMethod(compilerRoot, "C", "a");
    assertEquals(expectedAMethodC.toString(), actualAMethodC.toString());
  }

  @Test
  public void testCrossModuleMethodMotionWithoutCanModifyExterns() {
    CompilerOptions options = getDefaultCompilerOptions();
    options.setClassToProcess("com.test.CrossModuleMethodMotionTest");
    options.setExterns("externs.js");

    SourceFile sourceFile = createSourceFile("CrossModuleMethodMotionTest.js",
        "/**\n"
            + "@extern C.prototype.a = function() {}\n"
            + "*/\n"
            + "\n"
            + "class A {\n"
            + "  static b() {}\n"
            + "}\n"
            + "\n"
            + "class B extends A {}\n"
            + "\n"
            + "class C {}\n");

    ListMultimap<Integer, DiagnosticType> diagnostics = compile(sourceFile,
        options);

    DiagnosticType diagnostic = diagnostics.get(0);
    assertEquals("JSC_INTERNAL_ERROR_MODULE_DEPEND: null deepest common module",
        diagnostic.getMessage(Compiler.getDefaultErrorManager()));

    Node root = sourceFile.getTree();
    Node compilerRoot = compiler.getRootNode(root);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, new CrossModuleMethodMotion.IdGenerator(), false);

    ListMultimap<Integer, DiagnosticType> diagnostics2 = pass
        .processDiagnostics(diagnostics);

    assertEquals(1, diagnostics2.size());
    Node errorNode = findErrorNode(compilerRoot, diagnostics2.get(0).getCode());
    assertNotNull(errorNode);
    assertEquals("A.b", errorNode.toString());
  }

  private Node findMethod(Node node, String className, String methodName) {
    for (Node child : node.getChildren()) {
      if (child instanceof MemberExpression) {
        MemberExpression memberExpression = (MemberExpression) child;
        Node member = memberExpression.getObject();
        String classNameFromMember = member.getClass().getName();
        if (classNameFromMember.equals(className)) {
          if (memberExpression.getComputedPropertyName() != null) {
            if (methodName.equals(methodName)) {
              return memberExpression;
            }
            continue;
          }
          if (memberExpression.getIdentifier().getText().equals(methodName)) {
            return memberExpression;
          }
        }
      }
      Node result = findMethod(child, className, methodName);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private Node findErrorNode(Node node, int errorCode) {
    for (Node child : node.getChildren()) {
      if (child instanceof Error) {
        Error error = (Error) child;
        if (error.getErrorNumber() == errorCode) {
          return error;
        }
      }
      Node result = findErrorNode(child, errorCode);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private void printTree(Node node) {
    NodeTraversingTreePrinter printer = new NodeTraversingTreePrinter(
        System.out, "  ");
    printer.print(node);
  }

  @Test
  public void testPrintTree() {
    CompilerOptions options = getDefaultCompilerOptions();
    options.setClassToProcess("com.test.CrossModuleMethodMotionTest");

    SourceFile sourceFile = createSourceFile("CrossModuleMethodMotionTest.js",
        "class A {\n"
            + "  static b() {}\n"
            + "}\n"
            + "\n"
            + "class B extends A {}\n"
            + "\n"
            + "class C {}\n");

    ListMultimap<Integer, DiagnosticType> diagnostics = compile(sourceFile,
        options);

    DiagnosticType diagnostic = diagnostics.get(0);
    int errorCode = diagnostic.getCode();

    Node root = sourceFile.getTree();
    Node compilerRoot = compiler.getRootNode(root);

    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(
        compiler, new CrossModuleMethodMotion.IdGenerator(), true);

    ListMultimap<Integer, DiagnosticType> diagnostics2 = pass
        .processDiagnostics(diagnostics);

    Node errorNode = findErrorNode(compilerRoot, errorCode);
    assertNotNull(errorNode);

    System.out.println("Original tree:");
    printTree(root);

    pass.process(null, compilerRoot);

    System.out.println("\nModified tree:");
    printTree(compilerRoot);
  }
}