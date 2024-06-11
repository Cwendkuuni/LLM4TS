package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTest;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.SourceFile;

import java.util.Collection;
import java.util.Iterator;

public class CrossModuleMethodMotionTest extends CompilerTestCase {

  public CrossModuleMethodMotionTest() {
    super(new CompilerPassTest() {
      @Override
      public CompilerPass getPass() {
        return new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
      }
    });
  }

  @Test
  public void testMoveMethods() throws Exception {
    String input =
        "foo.js:\n"
        + "function Foo() {}\n"
        + "Foo.prototype.b = function() {}\n"
        + "foo.bar.js:\n"
        + "function FooBar() {}\n"
        + "FooBar.prototype.b = function() {}\n"
        + "foo.baz.js:\n"
        + "function FooBaz() {}\n";

    SourceFile inputSrc = new SourceFile("input.js", input);
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(DiagnosticGroups.all, DiagnosticLevel.ERROR);

    Node root = jsCompile(inputSrc, options);
    JSModule fooModule = moduleGraph.getModuleByName("foo");
    JSModule fooBarModule = moduleGraph.getModuleByName("foo.bar");
    JSModule fooBazModule = moduleGraph.getModuleByName("foo.baz");

    CrossModuleMethodMotion pass = getPass();
    pass.process(null, root);

    Node fooBazProto = fooBazModule.getBody().getFirstChild();
    assertTrue(fooBazProto.isPrototype());
    Iterator<Symbol> fooBazDeclarations = fooBazProto.getChildNodes().getIterator();
    Symbol fooBazB = fooBazDeclarations.next();
    assertTrue(fooBazB instanceof Property);
    Property fooBazBP = (Property) fooBazB;
    Node fooBazBValue = fooBazBP.getValue();
    assertTrue(fooBazBValue.isCallExpression());
    assertSame(fooBarModule, fooBazBValue.getFirstChild().getParent());
    assertTrue(fooBazBValue.getFirstChild().isName());
    assertSame("b", fooBazBValue.getFirstChild().asName().getName());

    Node fooProto = fooModule.getBody().getFirstChild();
    assertTrue(fooProto.isPrototype());
    Iterator<Symbol> fooDeclarations = fooProto.getChildNodes().getIterator();
    Symbol fooB = fooDeclarations.next();
    assertTrue(fooB instanceof Property);
    Property fooBP = (Property) fooB;
    Node fooBValue = fooBP.getValue();
    assertTrue(fooBValue.isCallExpression());
    assertSame(fooModule, fooBValue.getFirstChild().getParent());
    assertTrue(fooBValue.getFirstChild().isName());
    assertSame("b", fooBValue.getFirstChild().asName().getName());

    Node fooBazBStub =
        fooBazModule.getBody().getFirstChild().getLastChild()
            .getLastChild();
    assertTrue(fooBazBStub.isFunctionDeclaration());
    assertEquals("JSCompiler_stubMethod(id)", fooBazBStub.toString());

    Node fooBarBStub =
        fooBarModule.getBody().getFirstChild().getLastChild()
            .getLastChild();
    assertTrue(fooBarBStub.isFunctionDeclaration());
    assertEquals("JSCompiler_unstubMethod(id, body)", fooBarBStub.toString());

    Node fooUnstubBody =
        fooModule.getBody().getLastChild().getLastChild();
    assertTrue(fooUnstubBody.isCallExpression());
    assertSame(fooBarModule, fooUnstubBody.getFirstChild().getParent());
    assertTrue(fooUnstubBody.getFirstChild().isName());
    assertSame("JSCompiler_unstubMethod", fooUnstubBody.getFirstChild().asName().getName());
    assertTrue(fooUnstubBody.getFirstChild().getNextSibling().isNumberLiteral());
    assertEquals(fooBazB.getValue().getFirstChild().getFirstChild().getFirstChild().getFirstChild().getNumber().intValue(),
        fooUnstubBody.getFirstChild().getNextSibling().asNumberLiteral().getValue().intValue());
    assertTrue(fooUnstubBody.getFirstChild().getNextSibling().isReturnStatement());
    assertTrue(fooUnstubBody.getFirstChild().getNextSibling().getFirstChild()
        .isGetprop());
    assertSame("b", fooUnstubBody.getFirstChild().getNextSibling().getFirstChild().getFirstChild().asName().getName());

    Node fooBStub = fooModule.getBody().getLastChild().getLastChild()
        .getLastChild();
    assertTrue(fooBStub.isFunctionDeclaration());
    assertEquals("JSCompiler_stubMethod(id)", fooBStub.toString());

    assertFalse(fooBazBValue.isFunctionDeclaration());
    assertNull(fooBazBValue.getPrototype());
    assertFalse(fooBValue.isFunctionDeclaration());
    assertNull(fooBValue.getPrototype());
  }
}