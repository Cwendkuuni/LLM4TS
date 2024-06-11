package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.CompilerResult.Error;
import com.google.javascript.jscomp.CompilerTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.VariableMap;

public class RenamePrototypesTest extends CompilerTestBase {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    compiler = newCompiler();
    options = new CompilerOptions();
    options.setAssumeFunctionTypes(false);
    options.setKeepAllTypes(true);
    options.setAggressiveRenaming(false);
    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
  }

  @Test
  public void testProcess_NoProperties_NoStrings() {
    CompilerInput input = makeInput("function Foo() {}");
    CompilerResult result = compileWithPasses(input, ImmutableSet.of(renamePrototypes));
    assertEquals(0, result.errors().size());
  }

  @Test
  public void testProcess_NoProperties_Strings() {
    CompilerInput input = makeInput("function Foo() { var s = 'foo'; }");
    CompilerResult result = compileWithPasses(input, ImmutableSet.of(renamePrototypes));
    assertEquals(0, result.errors().size());
  }

  @Test
  public void testProcess_OneProperty_Prototype() {
    CompilerInput input = makeInput("function Foo() { this.bar = 1; }");
    CompilerResult result = compileWithPasses(input, ImmutableSet.of(renamePrototypes));
    assertEquals(0, result.errors().size());

    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("bar", "b");
    VariableMap actualRenameMap = renamePrototypes.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testProcess_OneProperty_ObjectLiteral() {
    CompilerInput input = makeInput("function Foo() { return { bar: 1 }; }");
    CompilerResult result = compileWithPasses(input, ImmutableSet.of(renamePrototypes));
    assertEquals(0, result.errors().size());

    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("bar", "b");
    VariableMap actualRenameMap = renamePrototypes.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testProcess_ConflictingPropertyNames() {
    CompilerInput input = makeInput("function Foo() { this.bar = 1; return { bar: 2 }; }");
    CompilerResult result = compileWithPasses(input, ImmutableSet.of(renamePrototypes));
    assertEquals(1, result.errors().size());

    Error error = result.errors().get(0);
    assertEquals("Error: Property name conflicts: 'bar'", error.message());
  }

  @Test
  public void testProcess_AggressiveRenaming() {
    options.setAggressiveRenaming(true);
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    CompilerInput input = makeInput("function Foo() { this.bar = 1; }");
    CompilerResult result = compileWithPasses(input, ImmutableSet.of(renamePrototypes));
    assertEquals(0, result.errors().size());

    Map<String, String> expectedRenameMap = new HashMap<>();
    // Since aggressive renaming is on, all property names will be renamed
    expectedRenameMap.put("bar", "a");
    VariableMap actualRenameMap = renamePrototypes.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }

  @Test
  public void testProcess_ReservedNames() {
    CompilerInput input = makeInput("function Foo() { this.indexOf = 1; }");
    CompilerResult result = compileWithPasses(input, ImmutableSet.of(renamePrototypes));
    assertEquals(1, result.errors().size());

    Error error = result.errors().get(0);
    assertEquals("Error: Property name 'indexOf' is reserved", error.message());
  }

  @Test
  public void testProcess_CustomReservedCharacters() {
    char[] customReservedCharacters = new char[] {'@'};
    renamePrototypes = new RenamePrototypes(compiler, false, customReservedCharacters, null);

    CompilerInput input = makeInput("function Foo() { this.@bar = 1; }");
    CompilerResult result = compileWithPasses(input, ImmutableSet.of(renamePrototypes));
    assertEquals(1, result.errors().size());

    Error error = result.errors().get(0);
    assertEquals("Error: Property name '@bar' contains reserved character '@'", error.message());
  }

  @Test
  public void testProcess_PrevUsedRenameMap() {
    CompilerInput input = makeInput("function Foo() { this.bar = 1; }");
    CompilerResult previousResult = compileWithPasses(input, ImmutableSet.of(renamePrototypes));
    Map<String, String> prevUsedRenameMap = new HashMap<>();
    prevUsedRenameMap.put("bar", "b");

    CompilerResult result = compileWithPasses(input,
        ImmutableSet.of(renamePrototypes),
        ImmutableSet.of(previousResult.getOutput()));
    assertEquals(0, result.errors().size());

    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("bar", "c"); // Since the previous map had 'bar' mapped to 'b', we need to ensure that the new mapping is different
    VariableMap actualRenameMap = renamePrototypes.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);
  }
}