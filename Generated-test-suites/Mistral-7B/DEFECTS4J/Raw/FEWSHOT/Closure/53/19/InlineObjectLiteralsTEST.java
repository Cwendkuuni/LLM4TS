package com.google.javascript.jscomp.test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import static com.google.common.truth.Truth.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() throws Exception {
    String sourceCode = "function f() {" +
        "  var x = {a: 1, b: 2};" +
        "  var y = {b: 2, a: 1};" +
        "  var z = x;" +
        "  var w = y;" +
        "  x = {c: 3};" +
        "  y = {c: 3};" +
        "  return x == y;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setPrintTempVars(true);
    options.setKeepAll(true);
    options.setWarningLevel(-1);

    List<CompilerPass> passes = Lists.newArrayList(
        new InlineObjectLiterals(compiler, new StringSupplier()) {
          @Override
          void afterProcess(Node externs, Node root) {
            super.afterProcess(externs, root);
            // Verify that the pass has generated the expected variables
            Scope rootScope = root.getFirstChild().getScope();
            for (Scope scope = rootScope; scope != null; scope = scope.getOuterScope()) {
              for (Iterator<Var> it = scope.getVars(); it.hasNext();) {
                Var var = it.next();
                String name = var.name;
                if (name.startsWith("JSCompiler_object_inline_")) {
                  assertEquals(1, var.getInitialValue().getNumChildren());
                  ReferenceCollection refs = compiler.getReferenceCollector()
                      .getReferenceInfo(var);
                  int refCount = 0;
                  for (Reference ref : refs.references) {
                    if (refCount++ > 1) {
                      // There should only be one reference.
                      fail("There should only be one reference to the variable: " + name);
                    }
                    Node refNode = ref.getNode();
                    if (refNode != null) {
                      // The reference should be a GETPROP node,
                      // whose property name matches the variable name.
                      Node getprop = refNode.getParent();
                      Node prop = getprop.getLastChild();
                      assertEquals(name, prop.getString());
                    }
                  }
                }
              }
            }
          }
        });

    compiler.compile(sourceCode, options);
    Node root = compiler.getOutput();
    passes.forEach(pass -> pass.process(externs, root));

    // Verify that 'x' and 'y' have been replaced with individual
    // variables.
    Node x = root.getFirstChild().getLastChild()
        .getChildAtIndex(0)
        .getLastChild();
    Node y = root.getFirstChild().getLastChild()
        .getChildAtIndex(1)
        .getLastChild();
    assertTrue(x.getType() == Token.NAME);
    assertTrue(y.getType() == Token.NAME);
    assertNotSame(x, y);

    // Verify that 'z' and 'w' still reference the original objects.
    Node z = root.getFirstChild().getLastChild()
        .getChildAtIndex(2);
    Node w = root.getFirstChild().getLastChild()
        .getChildAtIndex(3);
    assertTrue(z.getType() == Token.GETPROP);
    assertTrue(w.getType() == Token.GETPROP);
    Node zProp = z.getLastChild();
    Node wProp = w.getLastChild();
    assertEquals("x", zProp.getString());
    assertEquals("y", wProp.getString());

    // Verify that 'x' and 'y' are still equal after inlining.
    Node xEqY = root.getLastChild();
    assertTrue(xEqY.getType() == Token.BINOP);
    assertTrue(xEqY.getOperator().getCode() == Token.EQ);
    Node xValue = xEqY.getChildAtIndex(0);
    Node yValue = xEqY.getChildAtIndex(1);
    assertTrue(xValue.getType() == Token.NAME);
    assertTrue(yValue.getType() == Token.Name);
    assertEquals(xValue, yValue);
  }
}

class StringSupplier implements Supplier<String> {
  private int counter = 0;

  @Override
  public String get() {
    return "JSCompiler_object_inline_key_" + counter++;
  }
}