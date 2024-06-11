package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.TokenStream;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class RenamePrototypesTest {

  private final AbstractCompiler compiler;
  private final CompilerPass renamePrototypes;
  private final LifeCycleStage stage;
  private final boolean aggressiveRenaming;

  public RenamePrototypesTest(boolean aggressiveRenaming) {
    this.aggressiveRenaming = aggressiveRenaming;
    this.compiler = new AbstractCompiler() {
      @Override
      public LifeCycleStage getLifeCycleStage() {
        return stage;
      }

      @Override
      public void setLifeCycleStage(LifeCycleStage stage) {
        this.stage = stage;
      }

      @Override
      public void initialize() {}

      @Override
      public void onCodeChange() {}
    };
    this.renamePrototypes = new RenamePrototypes(compiler, aggressiveRenaming, null, null);
  }

  @Parameterized.Parameters
  public static List<Object[]> data() {
    return Arrays.asList(new Object[][]{
        new Object[]{false},
        new Object[]{true}
    });
  }

  @Test
  public void testProcess() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "var foo = new Foo();\n" +
                   "foo.bar();";

    CompilerInput ci = new CompilerInput("test.js", input, null);

    Node root = new TokenStream(ci.getInput()).parse();

    renamePrototypes.process(ci.getExterns(), root);

    assertNotNull(renamePrototypes.getPropertyMap());

    if (aggressiveRenaming) {
      assertEquals("B", renamePrototypes.getPropertyMap().lookupName("bar"));
    } else {
      assertEquals("bar", renamePrototypes.getPropertyMap().lookupName("bar"));
    }
  }

  @Test
  public void testProcessNoRenaming() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "var foo = new Foo();\n" +
                   "foo.bar();";

    CompilerInput ci = new CompilerInput("test.js", input, null);

    Node root = new TokenStream(ci.getInput()).parse();

    RenamePrototypes noRenaming = new RenamePrototypes(compiler, false, null, null);

    noRenaming.process(ci.getExterns(), root);

    assertNotNull(noRenaming.getPropertyMap());

    assertEquals("bar", noRenaming.getPropertyMap().lookupName("bar"));
  }

  @Test
  public void testProcessEmpty() {
    String input = "";

    CompilerInput ci = new CompilerInput("test.js", input, null);

    Node root = new TokenStream(ci.getInput()).parse();

    renamePrototypes.process(ci.getExterns(), root);

    assertEquals(Collections.emptyMap(), renamePrototypes.getPropertyMap());
  }

  @Test
  public void testProcessWithAggressiveRenaming() {
    String input = "function Foo() {}\n" +
                   "Foo.prototype.bar = function() {}\n" +
                   "var foo = new Foo();\n" +
                   "foo.bar();";

    CompilerInput ci = new CompilerInput("test.js", input, null);

    Node root = new TokenStream(ci.getInput()).parse();

    CompilerPassManager pm = new CompilerPassManager(compiler);
    pm.addPass(renamePrototypes);
    pm.runPasses(ci, root);

    assertEquals("B", renamePrototypes.getPropertyMap().lookupName("bar"));
  }
}